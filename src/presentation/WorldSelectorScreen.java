package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;

public class WorldSelectorScreen extends JFrame {

    private JLabel backgroundLabel;
    private ImageIcon originalIcon;

    public WorldSelectorScreen() {
        setTitle("Selector de Mundos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        int windowWidth = (int) (screenWidth * 0.8);
        int windowHeight = (int) (screenHeight * 0.6);

        originalIcon = new ImageIcon("src/resources/Seleccion_de_mundos.png");

        Image scaledImage = originalIcon.getImage().getScaledInstance(windowWidth, windowHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        backgroundLabel = new JLabel(scaledIcon);
        backgroundLabel.setBounds(0, 0, windowWidth, windowHeight);


        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(windowWidth, windowHeight));

        layeredPane.add(backgroundLabel, Integer.valueOf(0));

        int backButtonX = (int) (windowWidth * 0.02);
        int backButtonY = (int) (windowHeight * 0.85);
        int backButtonSize = (int) (windowWidth * 0.1);

        JButton backButton = new JButton("");
        backButton.setBounds(backButtonX, backButtonY, backButtonSize, backButtonSize);
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new MainMenuScreen();
            }
        });

        int diaButtonX = (int) (windowWidth * 0.24);
        int diaButtonY = (int) (windowHeight * 0.67);
        int diaButtonWidth = (int) (windowWidth * 0.12);
        int diaButtonHeight = (int) (windowHeight * 0.2);

        JButton diaButton = new JButton("");
        diaButton.setBounds(diaButtonX, diaButtonY, diaButtonWidth, diaButtonHeight);
        diaButton.setOpaque(false);
        diaButton.setContentAreaFilled(false);
        diaButton.setBorderPainted(false);
        diaButton.setForeground(Color.PINK);
        diaButton.setFont(new Font("Arial", Font.BOLD, 18));
        diaButton.setFocusPainted(false);

        diaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new NivelDiaScreen();
            }
        });

        int NocheButtonX = (int) (windowWidth * 0.34);
        int NocheButtonY = (int) (windowHeight * 0.67);
        int NocheButtonWidth = (int) (windowWidth * 0.15);
        int NocheButtonHeight = (int) (windowHeight * 0.2);

        JButton NocheButton = new JButton("");
        NocheButton.setBounds(NocheButtonX, NocheButtonY, NocheButtonWidth, NocheButtonHeight);
        NocheButton.setOpaque(false);
        NocheButton.setContentAreaFilled(false);
        NocheButton.setBorderPainted(false);
        NocheButton.setForeground(Color.black);
        NocheButton.setFont(new Font("Arial", Font.BOLD, 18));
        NocheButton.setFocusPainted(false);

        NocheButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new NivelNocheScreen();
            }
        });

        layeredPane.add(backButton, Integer.valueOf(1));
        layeredPane.add(diaButton, Integer.valueOf(1));
        layeredPane.add(NocheButton, Integer.valueOf(1));

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int newWidth = getWidth();
                int newHeight = getHeight();
                Image newScaledImage = originalIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                backgroundLabel.setIcon(new ImageIcon(newScaledImage));
                backgroundLabel.setBounds(0, 0, newWidth, newHeight);

                backButton.setBounds((int) (newWidth * 0.02), (int) (newHeight * 0.85), (int) (newWidth * 0.1), (int) (newWidth * 0.1));
                diaButton.setBounds((int) (newWidth * 0.24), (int) (newHeight * 0.67), (int) (newWidth * 0.12), (int) (newHeight * 0.2));
                NocheButton.setBounds((int) (newWidth * 0.34), (int) (newHeight * 0.67), (int) (newWidth * 0.15), (int) (newHeight * 0.2));
            }
        });

        setSize(windowWidth, windowHeight);
        setContentPane(layeredPane);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}