package domain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class AcquireSunButton {
    private JButton button;
    private Timer slideTimer;
    private Timer reappearTimer;

    /**
     * Constructor de AcquireSunButton
     * Crea un botón interactivo que representa un "sol" flotante.
     * Este botón aparece en la parte superior de la ventana, se desliza hacia abajo y otorga puntos de sol al hacer clic.
     * Después de desaparecer, reaparece tras un tiempo predeterminado.
     * @param windowWidth       Ancho de la ventana del juego, utilizado para calcular la posición inicial del botón.
     * @param windowHeight      Alto de la ventana del juego, utilizado para establecer el límite inferior del movimiento.
     * @param sunPointsToAdd    Cantidad de puntos de sol que se añadirán al hacer clic en el botón.
     * @param updateSunPointsLabel Acción a ejecutar para actualizar visualmente la etiqueta de puntos de sol.
     * @param addSunPoints      Acción a ejecutar para agregar los puntos de sol a la lógica del juego.
     */
    public AcquireSunButton(int windowWidth, int windowHeight, int sunPointsToAdd, Runnable updateSunPointsLabel, Runnable addSunPoints) {
        // Crear el botón
        button = new JButton();
        ImageIcon sunGif = new ImageIcon("src/resources/Sun.gif"); // Cambia el path si es necesario

        // Configurar el GIF como ícono del botón
        button.setIcon(sunGif);

        // Posición inicial del botón
        int initialX = (windowWidth / 2) + 375; // Posición inicial X
        int initialY = 10;                     // Posición inicial Y
        button.setBounds(initialX, initialY, sunGif.getIconWidth(), sunGif.getIconHeight());

        // Hacer que el botón no tenga fondo ni borde
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);

        // Crear un Timer para deslizar el botón hacia abajo
        slideTimer = new Timer(10, null);
        slideTimer.addActionListener(event -> {
            // Obtener la posición actual
            Rectangle bounds = button.getBounds();
            if (bounds.y + bounds.height < windowHeight - 20) { // Verificar si el botón aún no llegó al fondo
                bounds.y += 2; // Mover el botón hacia abajo
                button.setBounds(bounds);
            } else {
                slideTimer.stop(); // Detener el Timer al alcanzar la posición final
            }
        });

        // Iniciar el deslizamiento del botón
        slideTimer.start();

        // Acción del botón al hacer clic
        button.addActionListener(e -> {
            addSunPoints.run(); // Incrementa los puntos de sol en el tablero
            updateSunPointsLabel.run(); // Actualiza la etiqueta de los puntos de sol
            System.out.println("Se han adquirido " + sunPointsToAdd + " puntos de sol.");

            // Hacer desaparecer el botón
            button.setVisible(false);

            // Crear un Timer para que reaparezca después de 10 segundos
            reappearTimer = new Timer(10000, reappearEvent -> {
                button.setBounds(initialX, initialY, sunGif.getIconWidth(), sunGif.getIconHeight()); // Regresa a la posición original
                button.setVisible(true); // Reaparecer el botón
                slideTimer.start(); // Iniciar el deslizamiento nuevamente
            });
            reappearTimer.setRepeats(false); // Asegúrate de que no se repita
            reappearTimer.start(); // Inicia el Timer
        });
    }

    public JButton getButton() {
        return button;
    }
}
