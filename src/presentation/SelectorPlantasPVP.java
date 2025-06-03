package presentation;

import domain.*;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;


public class SelectorPlantasPVP extends JFrame {

    private JLabel backgroundLabel;
    private ImageIcon originalIcon;
    private List<Plant> availablePlants;
    private List<Plant> selectedPlants;
    private List<JLabel> plantLabels;
    private JPanel selectionPanel;
    private double[][] relativePositions;
    private JButton backButton;
    private JLabel peashooterImageLabel;
    private JLabel wallNutImageLabel;
    private JLabel sunflowerImageLabel;
    private JLabel tallNutImageLabel;
    private JLabel potatoMineImageLabel;
    private JLabel eciPlantImageLabel;
    private Map<Class<? extends Plant>, JLabel> plantImageMap;
    public static Clip musicClip;

    /**
     * Constructor de `selectorDePlantas`.
     * Inicializa la pantalla para seleccionar las plantas antes de comenzar el juego.
     * - Fondo gráfico personalizable.
     * - Panel para mostrar plantas seleccionables.
     * - Interactividad para seleccionar y confirmar plantas.
     */
    public SelectorPlantasPVP() {
        setTitle("Jugando Modo PvsP");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        int windowWidth = (int) (screenWidth * 0.8);
        int windowHeight = (int) (screenHeight * 0.6);

        originalIcon = new ImageIcon("src/resources/selector de planta.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(windowWidth, windowHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        backgroundLabel = new JLabel(scaledIcon);
        backgroundLabel.setBounds(0, 0, windowWidth, windowHeight);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(windowWidth, windowHeight));

        layeredPane.add(backgroundLabel, Integer.valueOf(0));

        availablePlants = new ArrayList<>();
        selectedPlants = new ArrayList<>();
        plantLabels = new ArrayList<>();

        Board board = new Board(50, 50);
        availablePlants.add(new Sunflower(board,0,0));
        availablePlants.add(new Peashooter(board, 0,0));
        availablePlants.add(new WallNut(board,0,0));
        availablePlants.add(new TallNut(board,0,0));
        availablePlants.add(new PotatoMine(board,0,0));
        availablePlants.add(new ECIPlant(board,0,0));

        relativePositions = new double[][]{
                {0.89, 0.053},
                {0.63, 0.053},
                {0.76, 0.053},
                {0.76, 0.365},
                {0.63, 0.365},
                {0.89, 0.365}
        };

        peashooterImageLabel = new JLabel();
        peashooterImageLabel.setBounds(
                (int) (windowWidth * 0.02),
                (int) (windowHeight * 0.02),
                100,
                70
        );
        peashooterImageLabel.setVisible(false);
        peashooterImageLabel.setOpaque(true);
        layeredPane.add(peashooterImageLabel, Integer.valueOf(2));

        wallNutImageLabel = new JLabel();
        wallNutImageLabel.setBounds(
                (int) (windowWidth * 0.02),
                (int) (windowHeight * 0.18),
                100,
                70
        );
        wallNutImageLabel.setVisible(false);
        layeredPane.add(wallNutImageLabel, Integer.valueOf(2));

        sunflowerImageLabel = new JLabel();
        sunflowerImageLabel.setBounds(
                (int) (windowWidth * 0.02),
                (int) (windowHeight * 0.34),
                100,
                70
        );
        sunflowerImageLabel.setVisible(false);
        layeredPane.add(sunflowerImageLabel, Integer.valueOf(2));

        tallNutImageLabel = new JLabel();
        tallNutImageLabel.setBounds(
                (int) (windowWidth * 0.02),
                (int) (windowHeight * 0.34),
                100,
                70
        );
        tallNutImageLabel.setVisible(false);
        layeredPane.add(tallNutImageLabel, Integer.valueOf(2));

        potatoMineImageLabel = new JLabel();
        potatoMineImageLabel.setBounds(
                (int) (windowWidth * 0.02),
                (int) (windowHeight * 0.34),
                100,
                70
        );
        potatoMineImageLabel.setVisible(false);
        layeredPane.add(potatoMineImageLabel, Integer.valueOf(2));

        eciPlantImageLabel = new JLabel();
        eciPlantImageLabel.setBounds(
                (int) (windowWidth * 0.08),
                (int) (windowHeight * 0.34),
                100,
                70
        );
        eciPlantImageLabel.setVisible(false);
        layeredPane.add(eciPlantImageLabel, Integer.valueOf(2));

        backButton = new JButton("");
        int backButtonSize = (int) (windowWidth * 0.1);
        backButton.setBounds((int) (windowWidth * 0.02), (int) (windowHeight * 0.85), backButtonSize, backButtonSize);
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setForeground(Color.BLACK);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backButton.addActionListener(e -> {
            dispose();
            new MainMenuScreen();
        });
        layeredPane.add(backButton, Integer.valueOf(1));
        for (int i = 0; i < availablePlants.size(); i++) {
            Plant plant = availablePlants.get(i);
            JLabel plantLabel = new JLabel(plant.getClass().getSimpleName(), SwingConstants.CENTER);
            plantLabel.setFont(new Font("Arial", Font.BOLD, 16));
            plantLabel.setForeground(Color.black);
            plantLabel.setBounds(
                    (int) (relativePositions[i][0] * windowWidth),
                    (int) (relativePositions[i][1] * windowHeight),
                    100, 50);
            plantLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            plantLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectPlant(plant, plantLabel);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    plantLabel.setForeground(Color.YELLOW);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    plantLabel.setForeground(Color.BLACK);
                }
            });
            plantLabels.add(plantLabel);
            layeredPane.add(plantLabel, Integer.valueOf(1));
        }
        selectionPanel = new JPanel();
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.Y_AXIS));
        selectionPanel.setOpaque(false);
        selectionPanel.setBounds(
                (int) (windowWidth * 0.02),
                (int) (windowHeight * 0.02),
                (int) (windowWidth * 0.2),
                (int) (windowHeight * 0.3)
        );
        layeredPane.add(selectionPanel, Integer.valueOf(1));
        JLabel confirmLabel = new JLabel("Lets Rock", SwingConstants.CENTER);
        confirmLabel.setFont(new Font("Arial", Font.BOLD, 16));
        confirmLabel.setForeground(Color.BLACK);
        confirmLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmLabel.setBounds((int) (windowWidth * 0.39), (int) (windowHeight * 0.81), 200, 50);
        confirmLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                startGame();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                confirmLabel.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                confirmLabel.setForeground(Color.black);
            }
        });
        layeredPane.add(confirmLabel, Integer.valueOf(1));
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int newWidth = getWidth();
                int newHeight = getHeight();

                Image newScaledImage = originalIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                backgroundLabel.setIcon(new ImageIcon(newScaledImage));
                backgroundLabel.setBounds(0, 0, newWidth, newHeight);

                selectionPanel.setBounds(10, 10, 150, newHeight - 20);
                updateSelectedPlants();

                backButton.setBounds((int) (newWidth * 0.02), (int) (newHeight * 0.85), (int) (newWidth * 0.1), (int) (newWidth * 0.1));
            }
        });
        setSize(windowWidth, windowHeight);
        setContentPane(layeredPane);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Método para seleccionar una planta.
     * Verifica si la planta ya ha sido seleccionada o si se ha alcanzado el límite de selección.
     * @param plant La planta seleccionada.
     * @param label El `JLabel` asociado a la planta seleccionada.
     */
    private void selectPlant(Plant plant, JLabel label) {
        for (Plant selectedPlant : selectedPlants) {
            if (selectedPlant.getClass().equals(plant.getClass())) {
                JOptionPane.showMessageDialog(this, "Ya has seleccionado esta planta.", "Duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        if (selectedPlants.size() < 5) {
            selectedPlants.add(plant);
            label.setForeground(Color.GRAY);
            label.setEnabled(false);
            updateSelectedPlants();
        } else {
            JOptionPane.showMessageDialog(this, "Ya has seleccionado 5 plantas.", "Límite alcanzado", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Actualiza el panel de selección para mostrar las plantas seleccionadas.
     * Limpia el panel anterior y agrega etiquetas gráficas para las plantas seleccionadas.
     */
    private void updateSelectedPlants() {
        selectionPanel.removeAll();
        int yOffset = 40;
        int spacing = 100;
        for (Plant plant : selectedPlants) {
            JLabel plantImageLabel = new JLabel();
            String imageResource = plant.getImageName();
            ImageIcon icon = new ImageIcon(plant.getImageName());
            Image img = icon.getImage().getScaledInstance(100, 70, Image.SCALE_SMOOTH);
            plantImageLabel.setIcon(new ImageIcon(img));
            plantImageLabel.setBounds(10, yOffset, 100, 70);
            yOffset += 70 + spacing;
            selectionPanel.add(plantImageLabel);
        }
        selectionPanel.revalidate();
        selectionPanel.repaint();
    }

    /**
     * Comienza el juego con las plantas seleccionadas.
     * Verifica que al menos una planta haya sido seleccionada antes de iniciar.
     * Detiene la música de fondo y lanza la siguiente pantalla.
     */
    private void startGame() {
        if (selectedPlants.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar al menos una planta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        System.out.println("Plantas seleccionadas: ");
        for (Plant plant : selectedPlants) {
            System.out.println(plant.getClass().getSimpleName());
        }
        System.out.println("Deteniendo música del selector de plantas...");
        PvPmode.stopMusic();
        LoadingScreen.stopMusic();
        SwingUtilities.invokeLater(() -> {
            dispose();
            new PvPmode(selectedPlants);
        });
    }
}
