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


public class Nivel3 extends JFrame {

    private JLabel backgroundLabel;
    private JLayeredPane layeredPane;
    private ImageIcon originalIcon;
    private List<Plant> importedPlants;
    private Board board;
    private PlayerVSMachine playerVsMachine;
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
    private static boolean isPaused = false;


    public Nivel3(List<Plant> selectedPlants) {
        setTitle("Jugando Horda");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        stopMusic();
        playHordaMusic();

        this.importedPlants = selectedPlants;

        int initialSunPoints = getInitialSunPointsFromUser();

        board = new Board(initialSunPoints, 50);
        board.setSunPoints(initialSunPoints);
        board.setLayeredPane(layeredPane);

        this.playerVsMachine = new PlayerVSMachine(board, initialSunPoints, 0);

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
        board = new Board(initialSunPoints, 50);
        playerVsMachine.setSelectedPlants(selectedPlants);
        board.setLayeredPane(layeredPane);

        new Thread(() -> playerVsMachine.startGame()).start();

        Timer plantActionTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPaused) {
                    for (int row = 0; row < 5; row++) {
                        for (int col = 0; col < 10; col++) {
                            Plant plant = board.getPlantAt(row, col);
                            if (plant != null && plant instanceof Peashooter) {
                                ((Peashooter) plant).performAction();
                            }
                        }
                    }
                }
            }
        });
        plantActionTimer.start();

        zombieSpawnTimer = new Timer(20000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPaused) {
                    spawnZombie();
                }
            }
        });
        zombieSpawnTimer.setRepeats(true);
        zombieSpawnTimer.start();

        zombieMoveTimer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPaused) {
                    moveZombies();
                }
            }
        });
        zombieMoveTimer.start();

        System.out.println("Juego inicializado con las plantas seleccionadas.");
    }


    private void pauseGame() {
        isPaused = true;
        playerVsMachine.pauseGame();
        if (zombieSpawnTimer != null) zombieSpawnTimer.stop();
        if (zombieMoveTimer != null) zombieMoveTimer.stop();
        if (gameTimer != null) gameTimer.stop();
        if (sunTimer != null) sunTimer.stop();
        System.out.println("Juego en pausa.");
    }

    private void resumeGame() {
        isPaused = false;
        playerVsMachine.resumeGame();
        if (zombieSpawnTimer != null) zombieSpawnTimer.start();
        if (zombieMoveTimer != null) zombieMoveTimer.start();
        if (gameTimer != null) gameTimer.start();
        if (sunTimer != null) sunTimer.start();
        System.out.println("Juego reanudado.");
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
                    layeredPane.add(plantLabel, Integer.valueOf(3));
                } else {
                    System.err.println("No se encontró la imagen para: " + plant.getClass().getSimpleName());
                }
            }
        }

        layeredPane.revalidate();
        layeredPane.repaint();
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
                cellLabel.setVerticalAlignment(SwingConstants.CENTER);
                cellLabel.setLayout(null);
                cellLabels[row][col] = cellLabel;
                gridPanel.add(cellLabel);
                final int currentRow = row;
                final int currentCol = col;

                if (col == 0) {
                    LawnMower lawnMower = new LawnMower(currentRow, layeredPane, board.getZombiesInRow(currentRow), this, null, null, null);
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
                                    plant.destroy();
                                    Shovel shovel = new Shovel();
                                    shovel.removePlant(board, currentRow, currentCol);
                                    updateCell(currentRow, currentCol);
                                    isShovelModeActive = false;
                                }
                            } else {
                                if (board.getPlantAt(currentRow, currentCol) != null) {
                                    System.out.println("La celda ya contiene una planta.");
                                    return;
                                }
                                if (selectedPlant != null) {
                                    boolean added = board.addPlant(currentRow, currentCol, selectedPlant);
                                    if (added) {
                                        playerVsMachine.addPlant(currentRow, currentCol, selectedPlant);
                                        String gifName = selectedPlant.getGifName();
                                        if (!gifName.isEmpty()) {
                                            ImageIcon gifIcon = new ImageIcon(selectedPlant.getGifName());
                                            cellLabel.setIcon(gifIcon);
                                            System.out.println("Colocada: " + selectedPlant.getClass().getSimpleName() + " en (" + currentRow + ", " + currentCol + ")");
                                        }
                                        updateSunPointsLabel();

                                        if (selectedPlant instanceof Sunflower) {
                                            int x = cellLabel.getX();
                                            int y = cellLabel.getY();
                                            Sunflower sunflower = new Sunflower(board, x, y);
                                            sunflower.generateSunButton(layeredPane, x, y, () -> updateSunPointsLabel());
                                            board.addPlant(currentRow, currentCol, sunflower);
                                        }

                                        if (selectedPlant instanceof ECIPlant) {
                                            int x = cellLabel.getX();
                                            int y = cellLabel.getY();
                                            ECIPlant eciPlant = new ECIPlant(board, x, y);
                                            eciPlant.generateSunButton(layeredPane, x, y, () -> updateSunPointsLabel());
                                            board.addPlant(currentRow, currentCol, eciPlant);
                                        }

                                        if (selectedPlant instanceof Peashooter) {
                                            Peashooter peashooter = new Peashooter(board, currentRow, currentCol);
                                            board.addPlant(currentRow, currentCol, peashooter);
                                            board.getLayeredPane().revalidate();
                                            board.getLayeredPane().repaint();
                                        }

                                        selectedPlant = null;
                                    } else {
                                        System.out.println("No se pudo colocar la planta.");
                                    }
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

    public void updateCell(int row, int col) {
        JLabel cellLabel = cellLabels[row][col];
        cellLabel.removeAll();
        Plant plant = board.getPlantAt(row, col);
        Zombie zombie = board.getZombieAt(row, col);

        if (plant != null) {
            cellLabel.setIcon(new ImageIcon(getClass().getResource(plant.getGifName())));
        } else if (zombie != null) {
            cellLabel.setIcon(new ImageIcon(getClass().getResource(zombie.getGifName())));
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
        JButton pauseButton = new JButton();
        int pauseButtonWidth = (int) (windowWidth * 0.06);
        int pauseButtonHeight = (int) (windowHeight * 0.05);

        pauseButton.setBounds((int) (windowWidth - pauseButtonWidth - 20), 20, pauseButtonWidth, pauseButtonHeight);

        pauseButton.setOpaque(false);
        pauseButton.setContentAreaFilled(false);
        pauseButton.setBorderPainted(false);
        pauseButton.setForeground(Color.WHITE);
        pauseButton.setFocusPainted(false);
        pauseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pauseButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                pauseGame();
                showPauseMenu();

            }
        });

        layeredPane.add(pauseButton, Integer.valueOf(2));
    }

    private void showPauseMenu() {
        PauseMenu pauseMenu = new PauseMenu(
                this,
                () -> {
                    resumeGame();
                    System.out.println("Reanudando juego");
                },
                () -> {
                    stopMusic();
                    dispose();
                    System.out.println("Regresando al selector de niveles");
                    new NivelDiaScreen();
                },
                () -> {
                    System.out.println("Reiniciando el nivel");
                    stopMusic();
                    dispose();
                    new selectorDePlantas();
                },
                () -> saveGame(),
                () -> openGame()

        );
        pauseMenu.show();
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
                    isPaused = true;
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
        new NivelDiaScreen();
    }

    private void spawnZombie() {
        int randomRow = (int) (Math.random() * 5);
        int lastCol = 9;
        Random random = new Random();
        String randomZombie = zombies[random.nextInt(zombies.length)];
        Zombie zombie = playerVsMachine.createZombie(randomZombie,randomRow,lastCol);
        board.addZombie(randomRow, lastCol, zombie);
        playerVsMachine.addZombie(randomRow, zombie);
        updateZombieUI(randomRow, lastCol, zombie);
        System.out.println("BasicZombie spawned at (" + randomRow + ", " + lastCol + ").");
    }

    private void updateZombieUI(int row, int col, Zombie zombie) {
        JLabel cellLabel = cellLabels[row][col];
        if (zombie != null) {
            JLabel zombieLabel = new JLabel(new ImageIcon(zombie.getGifName()));
            zombieLabel.setBounds(0, 0, cellLabel.getWidth(), cellLabel.getHeight());
            cellLabel.add(zombieLabel);
            Timer wiggleTimer = new Timer(100, new ActionListener() {
                int wiggleStep = 1;
                boolean movingRight = true;

                @Override
                public void actionPerformed(ActionEvent e) {
                    Point location = zombieLabel.getLocation();
                    if (movingRight) {
                        location.x += wiggleStep;
                        if (location.x >= 5) {
                            movingRight = false;
                        }
                    } else {
                        location.x -= wiggleStep;
                        if (location.x <= -5) {
                            movingRight = true;
                        }
                    }
                    zombieLabel.setLocation(location);
                }
            });
            wiggleTimer.start();
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
                        LawnMower lawnMower = getLawnMowerInRow(row);
                        if (lawnMower != null && !lawnMower.isActive()) {
                            lawnMower.activate(board);
                        } else if (lawnMower == null || lawnMower.isActive()) {
                            System.out.println("Zombie reached the house in row " + row + "! Player loses!");
                            gameOver();
                            return;
                        }
                    }
                }
            }
        }
    }

    private LawnMower getLawnMowerInRow(int row) {
        for (Component component : layeredPane.getComponents()) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                if (label.getName() != null && label.getName().equals("LawnMowerRow" + row)) {
                    return (LawnMower) label.getClientProperty("lawnMower");
                }
            }
        }
        return null;
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
        int totalSteps = 30;
        int stepX = (endX - startX) / totalSteps;
        int stepY = (endY - startY) / totalSteps;

        Timer slideTimer = new Timer(100, new ActionListener() {
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
        isPaused = true;
        this.dispose();
        new NivelDiaScreen();
    }

    private void saveGame() {
        try {
            File file = new File("game_state.txt");
            playerVsMachine.saveGame(file);
            JOptionPane.showMessageDialog(this, "Partida guardada con éxito.", "Guardar Partida", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar la partida.", "Guardar Partida", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void openGame() {
        try {
            File file = new File("game_state.txt");
            if (!file.exists()) {
                JOptionPane.showMessageDialog(this, "No se encontró el archivo de guardado.", "Abrir Partida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            playerVsMachine.openGame(file);
            JOptionPane.showMessageDialog(this, "Partida cargada con éxito.", "Abrir Partida", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar la partida.", "Abrir Partida", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}