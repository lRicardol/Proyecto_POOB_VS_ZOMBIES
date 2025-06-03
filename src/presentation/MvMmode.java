package presentation;

import domain.*;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Random;

public class MvMmode extends JFrame {

    private JLabel backgroundLabel;
    private JLayeredPane layeredPane;
    private ImageIcon originalIcon;
    private List<Plant> importedPlants;
    private Board board;
    private MachineVSMachine MachineVSMachine;

    private JPanel gridPanel;
    private JLabel[][] cellLabels;
    private Plant selectedPlant = null;
    private static Clip musicClip;
    private JLabel sunPointsLabel;
    private boolean isShovelModeActive = false;
    private Timer sunTimer;
    private JLabel timerLabel;
    private Timer gameTimer;
    private int remainingTime = 600;

    private Timer zombieSpawnTimer;
    private Timer zombieMoveTimer;

    private Random random;
    private String[] zombies = {"BasicZombie", "ConeheadZombie", "BucketheadZombie", "BrainsteinZombie", "ECIZombie"};
    private int currentSunPoints;
    private ZombieMachine zombieMachine;

    public MvMmode(List<Plant> selectedPlants) {
        setTitle("Jugando MvsM");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        stopMusic();
        playHordaMusic();

        this.importedPlants = selectedPlants;
        int initialSunPoints = getInitialSunPointsFromUser();
        board = new Board(initialSunPoints, 50);
        String[] plantStrategies = {"PlantsIntelligent", "PlantsStrategic"};
        String selectedPlantStrategy = (String) JOptionPane.showInputDialog(
                null,
                "Selecciona la estrategia de las plantas:",
                "Estrategia de Plantas",
                JOptionPane.QUESTION_MESSAGE,
                null,
                plantStrategies,
                plantStrategies[0]
        );

        String[] zombieStrategies = {"ZombiesOriginal", "ZombiesStrategic"};
        String selectedZombieStrategy = (String) JOptionPane.showInputDialog(
                null,
                "Selecciona la estrategia de los zombies:",
                "Estrategia de Zombies",
                JOptionPane.QUESTION_MESSAGE,
                null,
                zombieStrategies,
                zombieStrategies[0]
        );
        PlantMachine plantMachine;
        if ("PlantsIntelligent".equals(selectedPlantStrategy)) {
            plantMachine = new PlantsIntelligent(board);
        } else {
            plantMachine = new PlantsStrategic(board);
        }

        if ("ZombiesOriginal".equals(selectedZombieStrategy)) {
            this.zombieMachine = new ZombiesOriginal(board);
        } else {
            this.zombieMachine = new ZombiesStrategic(board);
        }
        if (board == null) {
            System.err.println("Error: el tablero (board) no está inicializado.");
        }
        if (zombieMachine == null) {
            System.err.println("Error: zombieMachine no está inicializado.");
        } else {
            System.out.println("zombieMachine inicializado correctamente: " + zombieMachine.getClass().getSimpleName());
        }
        this.MachineVSMachine = new MachineVSMachine(plantMachine, zombieMachine, initialSunPoints, 50);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int windowWidth = (int) (screenWidth * 0.8);
        int windowHeight = (int) (screenHeight * 0.6);
        originalIcon = new ImageIcon("src/resources/background1.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(windowWidth, windowHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        backgroundLabel = new JLabel(scaledIcon);
        backgroundLabel.setBounds(0, 0, windowWidth, windowHeight);

        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(windowWidth, windowHeight));
        layeredPane.add(backgroundLabel, Integer.valueOf(0));

        showPreparationGif(layeredPane, windowWidth, windowHeight);
        createSunPointsLabel(layeredPane);
        updateSunPointsLabel();
        createGridPanel(layeredPane, windowWidth, windowHeight);
        showSelectedPlants(layeredPane, windowWidth, windowHeight);
        addPauseButton(layeredPane, windowWidth, windowHeight);
        addShovelButton(layeredPane, windowWidth, windowHeight);
        addAcquireSunButton(layeredPane, windowWidth, windowHeight);

        setupTimerLabel();
        startCountdownTimer();

        initializeBoard();

        initializeGame(selectedPlants, initialSunPoints);

        setSize(windowWidth, windowHeight);
        setContentPane(layeredPane);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void placePlant(int row, int col, Plant plant) {
        if (row < 0 || row >= 5 || col < 0 || col >= 10) return;
        JLabel cellLabel = cellLabels[row][col];

        if (plant != null) {
            String gifPath = plant.getGifName();
            if (gifPath != null) {
                ImageIcon gifIcon = new ImageIcon(gifPath);
                cellLabel.setIcon(gifIcon);
            } else {
                System.err.println("No se encontró el GIF para la planta en (" + row + ", " + col + ")");
            }
        }
        cellLabel.revalidate();
        cellLabel.repaint();
    }

    private void plantTurn() {
        MachineVSMachine.plantTurn();

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                Plant plant = board.getPlantAt(row, col);
                if (plant != null) {
                    placePlant(row, col, plant);
                }
            }
        }
        updateSunPointsLabel();
    }

    private void zombieTurn() {
        MachineVSMachine.zombieTurn();
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                Zombie zombie = board.getZombieAt(row, col);
                if (zombie != null) {
                    placeZombie(row, col, zombie);
                }
            }
        }
    }

    /**
     * Método para actualizar el tablero visualmente al colocar un zombie.
     * @param row    Fila donde se colocará el zombie.
     * @param col    Columna donde se colocará el zombie.
     * @param zombie El zombie a colocar.
     */
    private void placeZombie(int row, int col, Zombie zombie) {
        if (row < 0 || row >= 5 || col < 0 || col >= 10) return;
        JLabel cellLabel = cellLabels[row][col];

        if (zombie != null) {
            String gifPath = zombie.getGifName();
            if (gifPath != null) {
                ImageIcon gifIcon = new ImageIcon(gifPath);
                cellLabel.setIcon(gifIcon);
            } else {
                System.err.println("No se encontró el GIF para el zombie en (" + row + ", " + col + ")");
            }
        }
        cellLabel.revalidate();
        cellLabel.repaint();
    }

    /**
     * Método que sincroniza las plantas lógicas con las visuales en el tablero.
     */
    private void updatePlantsOnBoard() {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                Plant plant = board.getPlantAt(row, col);
                if (plant != null) {
                    placePlant(row, col, plant);
                }
            }
        }
    }

    private void showPreparationGif(JLayeredPane layeredPane, int windowWidth, int windowHeight) {
        JLabel gifLabel = new JLabel(new ImageIcon("src/resources/PreparadosA.gif"));
        gifLabel.setBounds(
                (windowWidth - 400) / 2,
                (windowHeight - 200) / 2,
                400, 200
        );
        layeredPane.add(gifLabel, Integer.valueOf(3));

        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                layeredPane.remove(gifLabel);
                layeredPane.repaint();
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }

    private void initializeGame(List<Plant> selectedPlants, int initialSunPoints) {
        if (board == null) {
            throw new IllegalStateException("Error crítico: El tablero (board) no está inicializado.");
        }
        if (zombieMachine == null) {
            throw new IllegalStateException("Error crítico: zombieMachine no está inicializado.");
        }

        System.out.println("Inicializando el juego con " + initialSunPoints + " soles iniciales.");
        String[] plantStrategies = {"PlantsIntelligent", "PlantsStrategic"};
        String selectedPlantStrategy = (String) JOptionPane.showInputDialog(
                null,
                "Selecciona la estrategia de las plantas:",
                "Estrategia de Plantas",
                JOptionPane.QUESTION_MESSAGE,
                null,
                plantStrategies,
                plantStrategies[0]
        );

        PlantMachine plantMachine;
        if ("PlantsIntelligent".equals(selectedPlantStrategy)) {
            plantMachine = new PlantsIntelligent(board);
        } else {
            plantMachine = new PlantsStrategic(board);
        }

        this.MachineVSMachine = new MachineVSMachine(plantMachine, zombieMachine, initialSunPoints, 50);
        Timer gameLoopTimer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Ejecutando turno...");
                plantTurn();
                zombieTurn();
                moveZombies();
                printZombiePositions();
            }
        });
        startZombieSpawning();
        gameLoopTimer.start();
        System.out.println("El juego se ha inicializado correctamente.");
    }

    private void startZombieSpawning() {
        zombieSpawnTimer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spawnZombie();
                moveZombies();
            }
        });
        zombieSpawnTimer.start();
    }

    private int getInitialSunPointsFromUser() {
        while (true) {
            try {
                String input = JOptionPane.showInputDialog(
                        null,
                        "Ingrese la cantidad de soles inicial (número entero):",
                        "Configuración inicial",
                        JOptionPane.QUESTION_MESSAGE
                );

                if (input == null) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Se requiere un valor para comenzar el juego.",
                            "Advertencia",
                            JOptionPane.WARNING_MESSAGE
                    );
                    continue;
                }

                int sunPoints = Integer.parseInt(input);
                if (sunPoints < 0) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Por favor, ingrese un número positivo.",
                            "Advertencia",
                            JOptionPane.WARNING_MESSAGE
                    );
                    continue;
                }

                return sunPoints;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        null,
                        "Entrada no válida. Por favor, ingrese un número entero.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void createSunPointsLabel(JLayeredPane layeredPane) {
        sunPointsLabel = new JLabel("" + board.getSunPoints());
        sunPointsLabel.setFont(new Font("Arial", Font.BOLD, 20));
        sunPointsLabel.setForeground(Color.YELLOW);
        sunPointsLabel.setBounds(20, 20, 200, 30);
        layeredPane.add(sunPointsLabel, Integer.valueOf(1));

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int newX = (int) (getWidth() * 0.165);
                int newY = (int) (getHeight() * 0.04);
                sunPointsLabel.setBounds(newX, newY, 200, 30);
            }
        });
    }

    private void updateSunPointsLabel() {
        if (sunPointsLabel != null) {
            System.out.println("Actualizando etiqueta de puntos de sol: " + board.getSunPoints());
            sunPointsLabel.setText(String.valueOf(board.getSunPoints()));
        } else {
            System.err.println("Error: sunPointsLabel es null y no puede actualizarse.");
        }
    }

    private void playHordaMusic() {
        try {
            stopMusic();
            System.out.println("Reproduciendo música del nivel 3...");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("src/resources/musica-modo-horda.wav"));
            musicClip = AudioSystem.getClip();
            musicClip.open(audioStream);
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            musicClip.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al reproducir la música del modo Horda.");
        }
    }

    private void stopLoadingScreenMusic() {
        LoadingScreen.stopMusic();
    }

    @Override
    public void dispose() {
        super.dispose();
        stopMusic();
    }

    private void showSelectedPlants(JLayeredPane layeredPane, int windowWidth, int windowHeight) {
        int startX = (int) (windowWidth * 0.02);
        int startY = (int) (windowHeight * 0.02);
        int plantWidth = 100;
        int plantHeight = 70;
        int padding = 10;

        for (int i = 0; i < importedPlants.size(); i++) {
            Plant plant = importedPlants.get(i);
            String imageName = plant.getImageName();
            if (!imageName.isEmpty()) {
                if (plant.getImageName() != null) {
                    ImageIcon icon = new ImageIcon(plant.getImageName());
                    Image img = icon.getImage().getScaledInstance(plantWidth, plantHeight, Image.SCALE_SMOOTH);
                    JLabel plantLabel = new JLabel(new ImageIcon(img));
                    plantLabel.setBounds(startX, startY + i * (plantHeight + padding), plantWidth, plantHeight);
                    plantLabel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            selectedPlant = plant;
                            System.out.println("Selected: " + plant.getClass().getSimpleName());
                        }
                    });
                    layeredPane.add(plantLabel, Integer.valueOf(1));
                } else {
                    System.err.println("No se encontró la imagen para: " + plant.getClass().getSimpleName());
                }
            }
        }

        layeredPane.revalidate();
        layeredPane.repaint();
    }

    private void addSelectedPlants(List<Plant> selectedPlants) {
        this.importedPlants = selectedPlants;
    }

    private void createGridPanel(JLayeredPane layeredPane, int windowWidth, int windowHeight) {
        gridPanel = new JPanel(new GridLayout(5, 10));
        gridPanel.setOpaque(false);
        gridPanel.setBounds(
                (int) (windowWidth * 0.115),
                (int) (windowHeight * 0.14),
                (int) (windowWidth * 0.6),
                (int) (windowHeight * 0.81)
        );

        cellLabels = new JLabel[5][10];

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                JLabel cellLabel = new JLabel();
                cellLabel.setHorizontalAlignment(SwingConstants.CENTER);
                cellLabels[row][col] = cellLabel;
                gridPanel.add(cellLabel);
                final int currentRow = row;
                final int currentCol = col;
                if (col == 0) {
                    LawnMower lawnMower = new LawnMower(currentRow, layeredPane, board.getZombiesInRow(currentRow), null, null, null, this);
                    JLabel lawnMowerLabel = new JLabel(new ImageIcon("src/resources/LawnCleaner.png"));
                    int lawnMowerX = gridPanel.getX() - 80;
                    int lawnMowerY = (int) (gridPanel.getY() + row * cellLabel.getHeight() * 1.1);
                    lawnMowerLabel.setBounds(lawnMowerX, lawnMowerY, 70, 70);
                    lawnMowerLabel.putClientProperty("lawnMower", lawnMower);
                    layeredPane.add(lawnMowerLabel, Integer.valueOf(2));
                    Timer checkForZombieTimer = new Timer(100, e -> {
                        Zombie zombieInSecondColumn = board.getZombieAt(currentRow, 1);
                        boolean isActive = false;
                        if (zombieInSecondColumn != null && !isActive) {
                            lawnMower.activate(board);
                            System.out.println("LawnMower activated at row " + currentRow);
                        }
                    });
                    checkForZombieTimer.start();
                } else {
                    cellLabel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (isShovelModeActive) {
                                Plant plant = board.getPlantAt(currentRow, currentCol);
                                if (plant != null) {
                                    Shovel shovel = new Shovel();
                                    shovel.removePlant(board, currentRow, currentCol);
                                    updateCell(currentRow, currentCol);
                                    isShovelModeActive = false;
                                }
                            } else {
                                if (selectedPlant != null) {
                                    int plantCost = selectedPlant.getCost();
                                    if (board.getSunPoints() >= plantCost) {
                                        if (board.getPlantAt(currentRow, currentCol) == null) {
                                            boolean added = board.addPlant(currentRow, currentCol, selectedPlant);
                                            if (added) {
                                                board.addSunPoints(-plantCost);
                                                MachineVSMachine.addPlant(currentRow, currentCol, selectedPlant);
                                                String gifName = selectedPlant.getGifName();
                                                if (!gifName.isEmpty()) {
                                                    cellLabel.setIcon(new ImageIcon(gifName));
                                                    System.out.println("Colocada: " + selectedPlant.getClass().getSimpleName() + " en (" + currentRow + ", " + currentCol + ")");
                                                }
                                                if (selectedPlant instanceof Sunflower) {
                                                    int x = cellLabel.getX();
                                                    int y = cellLabel.getY();
                                                    Sunflower sunflower = new Sunflower(board, x, y);
                                                    sunflower.generateSunButton(layeredPane, x, y, () -> {
                                                        board.addSunPoints(25);
                                                        updateSunPointsLabel();
                                                    });
                                                    board.addPlant(currentRow, currentCol, sunflower);
                                                }

                                                if (selectedPlant instanceof ECIPlant) {
                                                    int x = cellLabel.getX();
                                                    int y = cellLabel.getY();
                                                    ECIPlant eciPlant = new ECIPlant(board, x, y);
                                                    eciPlant.generateSunButton(layeredPane, x, y, () -> {
                                                        board.addSunPoints(50);
                                                        updateSunPointsLabel();
                                                    });
                                                    board.addPlant(currentRow, currentCol, eciPlant);
                                                }
                                            } else {
                                                System.out.println("No se pudo colocar la planta.");
                                            }
                                        } else {
                                            System.out.println("La celda ya contiene una planta.");
                                        }
                                    } else {
                                        System.out.println("No tienes suficientes puntos de sol para colocar esta planta.");
                                    }
                                    selectedPlant = null;
                                } else {
                                    System.out.println("Selecciona una planta primero.");
                                }
                            }
                        }
                    });
                }
            }
        }

        layeredPane.add(gridPanel, Integer.valueOf(1));
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    private void updateCell(int row, int col) {
        JLabel cellLabel = cellLabels[row][col];
        Plant plant = board.getPlantAt(row, col);
        Zombie zombie = board.getZombieAt(row, col);

        if (plant != null) {
            String gifPath = plant.getGifName();
            if (gifPath != null) {
                cellLabel.setIcon(new ImageIcon(gifPath));
            } else {
                System.err.println("No se encontró el GIF para la planta en (" + row + ", " + col + ")");
            }
        } else if (zombie != null) {
            String gifPath = zombie.getGifName();
            if (gifPath != null) {
                cellLabel.setIcon(new ImageIcon(gifPath));
            } else {
                System.err.println("No se encontró el GIF para el zombie en (" + row + ", " + col + ")");
            }
        } else {
            cellLabel.setIcon(null);
        }

        cellLabel.revalidate();
        cellLabel.repaint();
    }

    private void initializeBoard() {
        SwingUtilities.invokeLater(() -> {
            board.clearBoard();
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 10; col++) {
                    updateCell(row, col);
                }
            }
            System.out.println("Board initialized.");
        });
    }

    private void addPauseButton(JLayeredPane layeredPane, int windowWidth, int windowHeight) {
        JButton pauseButton = new JButton("");
        int pauseButtonWidth = (int) (windowWidth * 0.06);
        int pauseButtonHeight = (int) (windowHeight * 0.05);
        pauseButton.setBounds((int) (windowWidth - pauseButtonWidth - 20), 20, pauseButtonWidth, pauseButtonHeight);
        pauseButton.setFont(new Font("Arial", Font.BOLD, 18));
        pauseButton.setOpaque(false);
        pauseButton.setContentAreaFilled(false);
        pauseButton.setBorderPainted(false);
        pauseButton.setForeground(Color.WHITE);
        pauseButton.setFocusPainted(false);
        pauseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pauseButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showPauseMenu();
            }
        });
        layeredPane.add(pauseButton, Integer.valueOf(2));
    }
    private void showPauseMenu() {
        System.out.println("Juego en pausa");

        PauseMenu pauseMenu = new PauseMenu(
                this,
                () -> System.out.println("Juego reanudado"),
                () -> {
                    System.out.println("Regresando al mapa...");
                    stopMusic();
                    dispose();
                    new MainMenuScreen();
                },
                () -> {
                    System.out.println("Reiniciando el nivel...");
                    stopMusic();
                    dispose();
                    new SelectorPlantasMVM();
                },
                () -> saveGame(),
                () -> openGame()
        );
        pauseMenu.show();
    }

    private void stopMusicAndPlayLoadingScreen() {
        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
            musicClip.close();
        }
    }

    public static void stopMusic() {
        if (musicClip != null) {
            if (musicClip.isRunning()) {
                System.out.println("Deteniendo música...");
                musicClip.stop();
            }
            musicClip.close();
            musicClip = null;
        } else {
            System.out.println("No hay música activa para detener.");
        }
    }

    private void addShovelButton(JLayeredPane layeredPane, int windowWidth, int windowHeight) {
        JButton shovelButton = new JButton("");
        shovelButton.setFont(new Font("Arial", Font.BOLD, 18));
        shovelButton.setOpaque(false);
        shovelButton.setContentAreaFilled(false);
        shovelButton.setBorderPainted(false);
        shovelButton.setForeground(Color.WHITE);
        shovelButton.setFocusPainted(false);
        shovelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        int buttonWidth = 100;
        int buttonHeight = 85;
        shovelButton.setBounds(
                windowWidth - buttonWidth - 20,
                windowHeight - buttonHeight - 20,
                buttonWidth,
                buttonHeight
        );

        shovelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isShovelModeActive = true;
                System.out.println("Modo pala activado.");
            }
        });

        layeredPane.add(shovelButton, Integer.valueOf(2));
    }

    private void addAcquireSunButton(JLayeredPane layeredPane, int windowWidth, int windowHeight) {
        AcquireSunButton acquireSunButton = new AcquireSunButton(
                windowWidth,
                windowHeight,
                25,
                this::updateSunPointsLabel,
                () -> board.addSunPoints(25)
        );
        layeredPane.add(acquireSunButton.getButton(), Integer.valueOf(2));
    }

    private void startCountdownTimer() {
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (remainingTime > 0) {
                    remainingTime--;
                    updateTimerLabel();
                } else {
                    gameTimer.stop();
                    showVictoryMessage();
                }
            }
        });
        gameTimer.start();
    }

    private void updateTimerLabel() {
        int minutes = remainingTime / 60;
        int seconds = remainingTime % 60;
        timerLabel.setText(String.format("Sobrevive: %d:%02d", minutes, seconds));
    }

    private void setupTimerLabel() {
        timerLabel = new JLabel("Sobrevive: 10:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerLabel.setForeground(Color.BLACK);

        int labelWidth = 300;
        int labelHeight = 30;

        SwingUtilities.invokeLater(() -> {
            int x = (getWidth() - labelWidth) / 2;
            int y = 10;
            timerLabel.setBounds(x, y, labelWidth, labelHeight);
            timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        });
        getLayeredPane().add(timerLabel, Integer.valueOf(1));
    }

    private void showVictoryMessage() {
        JOptionPane.showMessageDialog(
                this,
                "¡Felicitaciones, ganaste!",
                "Victoria",
                JOptionPane.INFORMATION_MESSAGE
        );
        dispose();
        stopMusic();
        new MainMenuScreen();
    }

    private void spawnZombie() {
        if (zombieMachine == null) {
            System.err.println("zombieMachine es null. Asegúrate de inicializarlo correctamente.");
            return;
        }

        zombieMachine.executeStrategy();

        int row = zombieMachine.getRowLastZombieCreated();
        Zombie newZombie = zombieMachine.getLastZombieCreated();

        if (row >= 0 && row < 5 && newZombie != null) {
            boolean added = board.addZombie(row, 9, newZombie);
            if (added) {
                placeZombie(row, 9, newZombie);
                System.out.println("Zombi añadido en fila: " + row + ", columna: 9");
            } else {
                System.err.println("No se pudo añadir el zombi en la fila: " + row + ", columna: 9");
            }
        }
    }

    private void moveZombies() {
        for (int row = 0; row < 5; row++) {
            for (int col = 9; col >= 0; col--) {
                Zombie zombie = board.getZombieAt(row, col);
                if (zombie != null) {
                    int nextCol = col - 1;
                    if (nextCol >= 0) {
                        if (board.getPlantAt(row, nextCol) == null && board.getZombieAt(row, nextCol) == null) {
                            slideZombie(row, col, row, nextCol, zombie);
                        } else if (board.getPlantAt(row, nextCol) != null) {
                            zombie.attack(board.getPlantAt(row, nextCol));
                        }
                    } else {
                        System.out.println("¡Un zombi llegó a la casa! Fila: " + row);
                        gameOver();
                    }
                }
            }
        }
    }


    private void slideZombie(int startRow, int startCol, int endRow, int endCol, Zombie zombie) {
        JLabel startCell = cellLabels[startRow][startCol];
        JLabel endCell = cellLabels[endRow][endCol];
        startCell.setIcon(null);

        int startX = startCell.getLocationOnScreen().x - layeredPane.getLocationOnScreen().x;
        int startY = startCell.getLocationOnScreen().y - layeredPane.getLocationOnScreen().y;
        int endX = endCell.getLocationOnScreen().x - layeredPane.getLocationOnScreen().x;
        int endY = endCell.getLocationOnScreen().y - layeredPane.getLocationOnScreen().y;

        JLabel zombieLabel = new JLabel(new ImageIcon(zombie.getGifName()));
        zombieLabel.setBounds(startX, startY, startCell.getWidth(), startCell.getHeight());
        layeredPane.add(zombieLabel, Integer.valueOf(2));

        int totalSteps = 50;
        int stepX = (endX - startX) / totalSteps;
        int stepY = (endY - startY) / totalSteps;
        int stepDuration = 5000 / totalSteps;

        Timer slideTimer = new Timer(stepDuration, new ActionListener() {
            int currentStep = 0;
            int currentX = startX;
            int currentY = startY;

            @Override
            public void actionPerformed(ActionEvent e) {
                currentX += stepX;
                currentY += stepY;
                zombieLabel.setLocation(currentX, currentY);
                currentStep++;

                if (currentStep >= totalSteps) {
                    ((Timer) e.getSource()).stop();
                    layeredPane.remove(zombieLabel);
                    layeredPane.revalidate();
                    layeredPane.repaint();
                    endCell.setIcon(new ImageIcon(zombie.getGifName()));
                    board.moveZombie(startRow, startCol, endRow, endCol);
                }
            }
        });
        slideTimer.start();
    }

    private void printZombiePositions() {
        System.out.println("Posiciones actuales de los zombies:");
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                Zombie zombie = board.getZombieAt(row, col);
                if (zombie != null) {
                    System.out.println("Zombie en posición: (" + row + ", " + col + ")");
                }
            }
        }
        System.out.println("--------------------");
    }

    private void gameOver() {
        if (zombieSpawnTimer != null) zombieSpawnTimer.stop();
        if (zombieMoveTimer != null) zombieMoveTimer.stop();
        if (gameTimer != null) gameTimer.stop();
        JOptionPane.showMessageDialog(
                this,
                "¡Has perdido! Los zombies alcanzaron tu casa.",
                "Game Over",
                JOptionPane.ERROR_MESSAGE
        );
        stopMusic();
        this.dispose();
        new MainMenuScreen();
    }

    private void deductSunPoints(int amount) {
        currentSunPoints -= amount;
        updateSunPointsLabel();
    }

    private void setInitialSunPoints(int initialPoints) {
        this.currentSunPoints = initialPoints;
        updateSunPointsLabel();
    }

    private void saveGame() {
        try {
            File file = new File("game_state_mvm.txt");
            MachineVSMachine.saveGame(file);
            JOptionPane.showMessageDialog(this, "Partida guardada con éxito.", "Guardar Partida", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar la partida.", "Guardar Partida", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void openGame() {
        try {
            File file = new File("game_state_mvm.txt");
            if (!file.exists()) {
                JOptionPane.showMessageDialog(this, "No se encontró el archivo de guardado.", "Abrir Partida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            MachineVSMachine.openGame(file);
            JOptionPane.showMessageDialog(this, "Partida cargada con éxito.", "Abrir Partida", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar la partida.", "Abrir Partida", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
