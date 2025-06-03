package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class NivelNocheScreen extends JFrame {

    private JLabel backgroundLabel;
    private ImageIcon originalIcon;
    private JButton backButton;
    private JButton nivel3Button;
    private JButton nivel2Button;
    private JButton nivel1Button;

    /**
     * Constructor de NivelDiaScreen.
     * Configura la pantalla de selección de niveles del mundo "Día".
     * - Fondo personalizado.
     * - Botones para regresar al selector de mundos y para seleccionar niveles.
     * - Comportamiento responsivo al redimensionar la ventana.
     */
    public NivelNocheScreen() {
        setTitle("Niveles de Día");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int windowWidth = (int) (screenWidth * 0.8);
        int windowHeight = (int) (screenHeight * 0.6);

        originalIcon = new ImageIcon("src/resources/Niveles_noche.png");

        Image scaledImage = originalIcon.getImage().getScaledInstance(windowWidth, windowHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        backgroundLabel = new JLabel(scaledIcon);
        backgroundLabel.setBounds(0, 0, windowWidth, windowHeight);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(windowWidth, windowHeight));
        layeredPane.add(backgroundLabel, Integer.valueOf(0));

        backButton = new JButton("");
        int backButtonSize = (int) (windowWidth * 0.1);
        backButton.setBounds((int) (windowWidth * 0.02), (int) (windowHeight * 0.85), backButtonSize, backButtonSize);
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new WorldSelectorScreen();
            }
        });
        layeredPane.add(backButton, Integer.valueOf(1));
        int nivel3ButtonX = (int) (windowWidth * 0.67);
        int nivel3ButtonY = (int) (windowHeight * 0.26);
        int nivel3ButtonWidth = (int) (windowWidth * 0.15);
        int nivel3ButtonHeight = (int) (windowHeight * 0.2);

        nivel3Button = new JButton("");
        nivel3Button.setBounds(nivel3ButtonX, nivel3ButtonY, nivel3ButtonWidth, nivel3ButtonHeight);
        nivel3Button.setOpaque(false);
        nivel3Button.setContentAreaFilled(false);
        nivel3Button.setBorderPainted(false);
        nivel3Button.setFont(new Font("Arial", Font.BOLD, 18));
        nivel3Button.setForeground(Color.WHITE);
        nivel3Button.setFocusPainted(false);
        nivel3Button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nivel3Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new selectorPlantasNoche();
            }
        });

        int nivel2ButtonX = (int) (windowWidth * 0.45);
        int nivel2ButtonY = (int) (windowHeight * 0.26);
        int nivel2ButtonWidth = (int) (windowWidth * 0.15);
        int nivel2ButtonHeight = (int) (windowHeight * 0.2);

        nivel2Button = new JButton("");
        nivel2Button.setBounds(nivel2ButtonX, nivel2ButtonY, nivel2ButtonWidth, nivel2ButtonHeight);
        nivel2Button.setOpaque(false);
        nivel2Button.setContentAreaFilled(false);
        nivel2Button.setBorderPainted(false);
        nivel2Button.setFont(new Font("Arial", Font.BOLD, 18));
        nivel2Button.setForeground(Color.WHITE);
        nivel2Button.setFocusPainted(false);
        nivel2Button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nivel2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new selectorPlantasNoche();
            }
        });
        int nivel1ButtonX = (int) (windowWidth * 0.22);
        int nivel1ButtonY = (int) (windowHeight * 0.26);
        int nivel1ButtonWidth = (int) (windowWidth * 0.15);
        int nivel1ButtonHeight = (int) (windowHeight * 0.2);

        nivel1Button = new JButton("");
        nivel1Button.setBounds(nivel1ButtonX, nivel1ButtonY, nivel1ButtonWidth, nivel1ButtonHeight);
        nivel1Button.setOpaque(false);
        nivel1Button.setContentAreaFilled(false);
        nivel1Button.setBorderPainted(false);
        nivel1Button.setFont(new Font("Arial", Font.BOLD, 18));
        nivel1Button.setForeground(Color.WHITE);
        nivel1Button.setFocusPainted(false);
        nivel1Button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nivel1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new selectorPlantasNoche();
            }
        });

        layeredPane.add(nivel3Button, Integer.valueOf(1));
        layeredPane.add(nivel2Button, Integer.valueOf(1));
        layeredPane.add(nivel1Button, Integer.valueOf(1));
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int newWidth = getWidth();
                int newHeight = getHeight();
                Image newScaledImage = originalIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                backgroundLabel.setIcon(new ImageIcon(newScaledImage));
                backgroundLabel.setBounds(0, 0, newWidth, newHeight);
                int newBackButtonSize = (int) (newWidth * 0.1);
                backButton.setBounds((int) (newWidth * 0.02), (int) (newHeight * 0.85), newBackButtonSize, newBackButtonSize);
                int newnivel3ButtonX = (int) (newWidth * 0.67);
                int newnivel3ButtonY = (int) (newHeight * 0.26);
                int newnivel3ButtonWidth = (int) (newWidth * 0.15);
                int newnivel3ButtonHeight = (int) (newHeight * 0.2);
                nivel3Button.setBounds(newnivel3ButtonX, newnivel3ButtonY, newnivel3ButtonWidth, newnivel3ButtonHeight);
                int newnivel2ButtonX = (int) (newWidth * 0.45);
                int newnivel2ButtonY = (int) (newHeight * 0.26);
                int newnivel2ButtonWidth = (int) (newWidth * 0.15);
                int newnivel2ButtonHeight = (int) (newHeight * 0.2);
                nivel2Button.setBounds(newnivel2ButtonX, newnivel2ButtonY, newnivel2ButtonWidth, newnivel2ButtonHeight);
                int newnivel1ButtonX = (int) (newWidth * 0.22);
                int newnivel1ButtonY = (int) (newHeight * 0.26);
                int newnivel1ButtonWidth = (int) (newWidth * 0.15);
                int newnivel1ButtonHeight = (int) (newHeight * 0.2);
                nivel1Button.setBounds(newnivel1ButtonX, newnivel1ButtonY, newnivel1ButtonWidth, newnivel1ButtonHeight);
            }
        });

        setSize(windowWidth, windowHeight);
        setContentPane(layeredPane);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}