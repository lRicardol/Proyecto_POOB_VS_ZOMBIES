package domain;

import javax.swing.*;
import javax.swing.Timer;

/**
 * Clase ECIPlant
 * Representa una planta especial que produce soles de manera periódica.
 * Esta planta no inflige daño y está diseñada para proporcionar recursos
 * adicionales al jugador.
 */
public class ECIPlant extends Plant {

    private static final int SUN_PRODUCTION = 50;
    private static final int PRODUCTION_INTERVAL = 20000;
    private Timer productionTimer;

    /**
     * Constructor de ECIPlant
     * Inicializa la planta con sus valores de salud, costo y atributos específicos.
     * @param board El tablero en el que se encuentra la planta.
     * @param row   La fila donde se posiciona la planta.
     * @param col   La columna donde se posiciona la planta.
     */
    public ECIPlant(Board board, int row, int col) {
        super(150, 75, 0, 0, board, row, col);
    }

    /**
     * Método performAction
     * Realiza la acción principal de la planta. En el caso de esta planta,
     * no inflige daño ni realiza acciones adicionales.
     */
    @Override
    public void performAction() {
    }

    /**
     * Método isTallNut
     * Indica si la planta es del tipo "Tall Nut".
     * @return false, ya que esta planta no es una "Tall Nut".
     */
    @Override
    public boolean isTallNut() {
        return false;
    }

    /**
     * Método getImageName
     * Retorna la ruta de la imagen que representa a la planta.
     * @return String con la ruta relativa de la imagen.
     */
    @Override
    public String getImageName() {
        return "src/resources/ECIPlant.png";
    }

    /**
     * Método getGifName
     * Retorna la ruta del GIF animado que representa a la planta.
     * @return String con la ruta relativa del archivo GIF.
     */
    @Override
    public String getGifName() {
        return "src/resources/eciplantA.gif";
    }

    /**
     * Método generateSunButton
     * Genera un botón interactivo en la interfaz que representa un sol producido por la planta.
     * Este botón permite al jugador recolectar puntos de sol.
     * @param layeredPane El panel en capas donde se agrega el botón.
     * @param x La posición X inicial del botón en el tablero.
     * @param y La posición Y inicial del botón en el tablero.
     * @param updateSunPointsLabel Método ejecutable para actualizar la etiqueta de los puntos de sol.
     */
    public void generateSunButton(JLayeredPane layeredPane, int x, int y, Runnable updateSunPointsLabel) {
        JButton sunButton = new JButton();
        sunButton.setBounds(x + 145, y + 100, 70, 70);
        sunButton.setOpaque(false);
        sunButton.setContentAreaFilled(false);
        sunButton.setBorderPainted(false);
        sunButton.setIcon(new ImageIcon("src/resources/Sun.gif"));
        layeredPane.add(sunButton, Integer.valueOf(2));
        layeredPane.revalidate();
        layeredPane.repaint();
        Timer reappearTimer = new Timer(PRODUCTION_INTERVAL, e -> sunButton.setVisible(true));
        reappearTimer.setRepeats(false);

        sunButton.addActionListener(e -> {
            sunButton.setVisible(false);
            board.addSunPoints(SUN_PRODUCTION);
            updateSunPointsLabel.run();
            reappearTimer.start();
        });

        if (productionTimer == null) {
            productionTimer = new Timer(PRODUCTION_INTERVAL, e -> sunButton.setVisible(true));
            productionTimer.start();
        }
    }
}