package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuScreen extends JFrame {

    private JLabel backgroundLabel;
    private ImageIcon originalIcon;

    /**
     * Constructor de MainMenuScreen.
     * Configura el diseño y los componentes del menú principal, incluyendo:
     * - Fondo escalable.
     * - Botones para los modos de juego (Aventura, PvP, MvM).
     * - Etiqueta con el nombre del jugador.
     */
    public MainMenuScreen() {
        setTitle("Menú Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        int windowWidth = (int) (screenWidth * 0.8);
        int windowHeight = (int) (screenHeight * 0.6);

        originalIcon = new ImageIcon("src/resources/MainMenu.png");

        Image scaledImage = originalIcon.getImage().getScaledInstance(windowWidth, windowHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        backgroundLabel = new JLabel(scaledIcon);
        backgroundLabel.setBounds(0, 0, windowWidth, windowHeight);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(windowWidth, windowHeight));

        layeredPane.add(backgroundLabel, Integer.valueOf(0));

        JLabel nombreLabel = new JLabel("Jugador: " + LoadingScreen.nombreUsuario);
        nombreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nombreLabel.setForeground(Color.WHITE);
        nombreLabel.setBounds((int) (windowWidth * 0.065), (int) (windowHeight * 0.06), 200, 30);
        layeredPane.add(nombreLabel, Integer.valueOf(1));

        int buttonWidth = (int) (windowWidth * 0.3);
        int buttonHeight = (int) (windowHeight * 0.1);
        int buttonX = (int) (windowWidth * 0.465);
        int buttonY = (int) (windowHeight * 0.25);

        JButton aventuraButton = new JButton("");
        aventuraButton.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
        aventuraButton.setOpaque(false);
        aventuraButton.setContentAreaFilled(false);
        aventuraButton.setBorderPainted(false);
        aventuraButton.setFocusPainted(false);

        aventuraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new WorldSelectorScreen();
            }
        });

        int pvpbuttonWidth = (int) (windowWidth * 0.3);
        int pvpbuttonHeight = (int) (windowHeight * 0.1);
        int pvpbuttonX = (int) (windowWidth * 0.465);
        int pvpbuttonY = (int) (windowHeight * 0.42);

        JButton pvpButton = new JButton("");
        pvpButton.setBounds(pvpbuttonX, pvpbuttonY, pvpbuttonWidth, pvpbuttonHeight);
        pvpButton.setOpaque(false);
        pvpButton.setContentAreaFilled(false);
        pvpButton.setBorderPainted(false);
        pvpButton.setFocusPainted(false);

        pvpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new SelectorPlantasPVP();
            }
        });

        int mvmbuttonWidth = (int) (windowWidth * 0.3);
        int mvmbuttonHeight = (int) (windowHeight * 0.1);
        int mvmbuttonX = (int) (windowWidth * 0.465);
        int mvmbuttonY = (int) (windowHeight * 0.57);

        JButton mvmButton = new JButton("");
        mvmButton.setBounds(mvmbuttonX, mvmbuttonY, mvmbuttonWidth, mvmbuttonHeight);
        mvmButton.setOpaque(false);
        mvmButton.setContentAreaFilled(false);
        mvmButton.setBorderPainted(false);
        mvmButton.setFocusPainted(false);

        mvmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new SelectorPlantasMVM();
            }
        });

        layeredPane.add(aventuraButton, Integer.valueOf(1));
        layeredPane.add(pvpButton, Integer.valueOf(1));
        layeredPane.add(mvmButton, Integer.valueOf(1));

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int newWidth = getWidth();
                int newHeight = getHeight();
                Image newScaledImage = originalIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                backgroundLabel.setIcon(new ImageIcon(newScaledImage));
                backgroundLabel.setBounds(0, 0, newWidth, newHeight);


                nombreLabel.setBounds((int) (newWidth * 0.065), (int) (newHeight * 0.06), 200, 30);

                aventuraButton.setBounds((int) (newWidth * 0.465), (int) (newHeight * 0.25), (int) (newWidth * 0.3), (int) (newHeight * 0.1));

                pvpButton.setBounds((int) (newWidth * 0.465), (int) (newHeight * 0.42), (int) (newWidth * 0.3), (int) (newHeight * 0.1));

                mvmButton.setBounds((int) (newWidth * 0.465), (int) (newHeight * 0.57), (int) (newWidth * 0.3), (int) (newHeight * 0.1));
            }
        });

        setSize(windowWidth, windowHeight);
        setContentPane(layeredPane);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
