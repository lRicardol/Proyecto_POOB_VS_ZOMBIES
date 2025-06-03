package domain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;

public class PauseMenu {
    private final JDialog pauseDialog;

    /**
     * Constructor para crear un menú de pausa en el juego.
     * Este menú de pausa permite realizar acciones como reanudar el juego, salir al mapa, reiniciar el nivel,
     * guardar el progreso o cargar un juego guardado.
     * @param parentFrame El `JFrame` principal de la aplicación que será el contenedor del diálogo de pausa.
     * @param onResume Acción a ejecutar cuando el jugador selecciona reanudar el juego.
     * @param onExitToMap Acción a ejecutar cuando el jugador selecciona salir al mapa.
     * @param onRestart Acción a ejecutar cuando el jugador selecciona reiniciar el nivel.
     * @param onSave Acción a ejecutar cuando el jugador selecciona guardar el progreso del juego.
     * @param onOpen Acción a ejecutar cuando el jugador selecciona cargar un juego guardado.
     */
    public PauseMenu(JFrame parentFrame, Runnable onResume, Runnable onExitToMap, Runnable onRestart, Runnable onSave, Runnable onOpen) {
        JPanel pausePanel = new JPanel();
        pausePanel.setLayout(null);
        pausePanel.setOpaque(false);
        ImageIcon pauseImageIcon = new ImageIcon("src/resources/pausa.png");
        JLabel pauseImageLabel = new JLabel(pauseImageIcon);
        pauseImageLabel.setBounds(0, 0, pauseImageIcon.getIconWidth(), pauseImageIcon.getIconHeight());
        pauseDialog = new JDialog(parentFrame, "Pausa", true);
        pauseDialog.setSize(pauseImageIcon.getIconWidth(), pauseImageIcon.getIconHeight());
        pauseDialog.setUndecorated(true);
        pauseDialog.setBackground(new Color(0, 0, 0, 0));
        pauseDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        pauseDialog.setLocationRelativeTo(parentFrame);

        int dialogWidth = pauseImageIcon.getIconWidth();
        int dialogHeight = pauseImageIcon.getIconHeight();

        JButton resumeButton = createButton("", (dialogWidth * 0.5 - 60), (dialogHeight * 0.68), () -> {
            onResume.run();
            pauseDialog.dispose();
        });

        JButton exitToMapButton = createButton("", (dialogWidth * 0.57 + 80), (dialogHeight * 0.62), onExitToMap);

        JButton restartButton = createButton("", (dialogWidth * 0.57 + 80), (dialogHeight * 0.51), onRestart);

        JButton saveButton = createButton("", (dialogWidth * 0.32), (dialogHeight * 0.51), onSave);

        JButton openButton = createButton("", (dialogWidth * 0.32), (dialogHeight * 0.62), onOpen);

        pausePanel.add(pauseImageLabel);
        pausePanel.add(resumeButton);
        pausePanel.add(exitToMapButton);
        pausePanel.add(restartButton);
        pausePanel.add(saveButton);
        pausePanel.add(openButton);
        pausePanel.setComponentZOrder(resumeButton, 0);
        pausePanel.setComponentZOrder(exitToMapButton, 0);
        pausePanel.setComponentZOrder(restartButton, 0);
        pausePanel.setComponentZOrder(saveButton, 0);
        pausePanel.setComponentZOrder(openButton, 0);
        pausePanel.setComponentZOrder(pauseImageLabel, 1);
        pauseDialog.setContentPane(pausePanel);
    }

    /**
     * Crea un botón personalizado con un diseño transparente y una acción asociada.
     * @param text Texto que aparecerá en el botón. Si es vacío, el botón no tendrá texto visible.
     * @param x Posición X del botón dentro de su contenedor.
     * @param y Posición Y del botón dentro de su contenedor.
     * @param action Acción que se ejecutará cuando se haga clic en el botón.
     * @return Un objeto `JButton` configurado con las propiedades especificadas.
     */
    private JButton createButton(String text, double x, double y, Runnable action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBounds((int) x, (int) y, 120, 40);
        button.addActionListener(e -> action.run());
        return button;
    }

    /**
     * Muestra el diálogo de pausa en la pantalla.
     * Este método hace que el diálogo de pausa sea visible, bloqueando la interacción
     * con el resto de la interfaz de usuario hasta que se cierre.
     */
    public void show() {
        pauseDialog.setVisible(true);
    }
}
