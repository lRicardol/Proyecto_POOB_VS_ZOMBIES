package presentation;

import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class LoadingScreen extends JFrame {

    public static String nombreUsuario;
    private static Clip musicClip;

    /**
     * Constructor de LoadingScreen.
     * Configura la interfaz gráfica de la pantalla de carga, incluyendo un formulario para el ingreso del nombre del jugador
     * y un temporizador para mostrar elementos adicionales después de un retraso.
     */
    public LoadingScreen() {
        setTitle("Pantalla de Carga");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        int windowWidth = (int) (screenWidth * 0.7);
        int windowHeight = (int) (screenHeight * 0.7);
        setSize(windowWidth, windowHeight);

        ImageIcon loadingImage = new ImageIcon("src/resources/titleScreen.png");
        Image scaledImage = loadingImage.getImage().getScaledInstance(windowWidth, windowHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setBounds(0, 0, windowWidth, windowHeight);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(windowWidth, windowHeight));
        layeredPane.add(imageLabel, Integer.valueOf(0));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(null);
        inputPanel.setOpaque(false);

        JPanel imagePanel = new JPanel();
        imagePanel.setOpaque(false);
        imagePanel.setLayout(null);
        imagePanel.setBounds(0, 0, windowWidth, windowHeight);


        ImageIcon playerImageIcon = new ImageIcon("src/resources/nombreplayer.png");
        int playerImageWidth = (int) (windowWidth * 0.8);
        int playerImageHeight = (int) (playerImageWidth * playerImageIcon.getIconHeight() / playerImageIcon.getIconWidth());

        JLabel playerImageLabel = new JLabel();
        playerImageLabel.setIcon(new ImageIcon(playerImageIcon.getImage().getScaledInstance(playerImageWidth, playerImageHeight, Image.SCALE_SMOOTH)));

        int playerImageX = (windowWidth - playerImageWidth) / 2;
        int playerImageY = (windowHeight - playerImageHeight) / 2;
        playerImageLabel.setBounds(playerImageX, playerImageY, playerImageWidth, playerImageHeight);

        imagePanel.add(playerImageLabel);
        imagePanel.setVisible(false);

        layeredPane.add(imagePanel, Integer.valueOf(1));

        add(layeredPane);

        setLocationRelativeTo(null);
        setVisible(true);

        int panelWidth = (int) (windowWidth * 0.3);
        int panelHeight = (int) (windowHeight * 0.25);
        inputPanel.setBounds((windowWidth - panelWidth) / 2, (windowHeight - panelHeight) / 2, panelWidth, panelHeight);

        JLabel promptLabel = new JLabel("Ingrese su nombre:");
        promptLabel.setFont(new Font("Arial", Font.BOLD, 18));
        promptLabel.setForeground(Color.BLACK);
        promptLabel.setBounds(0, 0, panelWidth, 30);
        promptLabel.setHorizontalAlignment(SwingConstants.CENTER);
        inputPanel.add(promptLabel);

        JTextField nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));
        nameField.setForeground(Color.BLACK);
        nameField.setOpaque(false);
        nameField.setBorder(BorderFactory.createEmptyBorder());
        nameField.setBounds(0, 40, panelWidth, 30);
        inputPanel.add(nameField);

        JButton startButton = new JButton("Iniciar");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setBounds((panelWidth - 100) / 2, 80, 100, 40);
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);
        startButton.setOpaque(false);
        startButton.addActionListener(e -> {
            nombreUsuario = nameField.getText().trim();
            if (nombreUsuario.isEmpty()) {
                nombreUsuario = "Jugador";
            }
            dispose();
            new MainMenuScreen();
        });

        inputPanel.add(startButton);

        layeredPane.add(inputPanel, Integer.valueOf(2));

        inputPanel.setVisible(false);

        playMusic();

        new Timer(2500, e -> {
            inputPanel.setVisible(true);
            imagePanel.setVisible(true);
        }).start();
    }

    /**
     * Método estático para reproducir música de fondo.
     * Carga y reproduce en bucle continuo el archivo de música asociado con la pantalla de carga.
     * Muestra mensajes de error en caso de problemas al cargar o reproducir la música.
     */
    public static void playMusic() {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("src/resources/Plants-vs-Zombies-Soundtrack.-_Main-Menu_.wav"));
            musicClip = AudioSystem.getClip();
            musicClip.open(audioStream);
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            musicClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            System.err.println("Error al reproducir la música de LoadingScreen.");
        }
    }

    /**
     * Método estático para detener la música de fondo.
     * Finaliza la reproducción de música activa si existe y libera los recursos asociados al clip.
     * Proporciona mensajes de depuración según el estado de la música.
     */
    public static void stopMusic() {
        if (musicClip != null) {
            if (musicClip.isRunning()) {
                System.out.println("Deteniendo música de LoadingScreen...");
                musicClip.stop();
            } else {
                System.out.println("La música de LoadingScreen ya estaba detenida.");
            }
            musicClip.close();
            musicClip = null;
        } else {
            System.out.println("No hay música activa para detener en LoadingScreen.");
        }
    }

    /**
     * Método main.
     * Punto de entrada principal del programa que inicia la pantalla de carga.
     */
    public static void main(String[] args) {
        new LoadingScreen();
    }
}