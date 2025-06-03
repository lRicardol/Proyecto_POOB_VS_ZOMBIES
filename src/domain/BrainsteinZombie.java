package domain;

import javax.swing.Timer;
import javax.swing.*;

public class BrainsteinZombie extends Zombie {

    private static final int BRAIN_PRODUCTION = 25;
    private static final int PRODUCTION_INTERVAL = 20000;
    private Timer productionTimer;

    /**
     * Constructor de BrainsteinZombie
     * Inicializa un zombi que no ataca ni se mueve, pero genera cerebros.
     * @param board Tablero en el que se encuentra el zombi.
     * @param row   Fila inicial donde se posiciona el zombi.
     * @param col   Columna inicial donde se posiciona el zombi.
     */
    public BrainsteinZombie(Board board, int row, int col) {
        super(300, 50, 0, 0, board, row, col);
    }

    /**
     * Obtiene la cantidad de cerebros que produce este zombi.
     * @return int Cantidad de cerebros generados.
     */
    public int getBrainProduction() {
        return BRAIN_PRODUCTION;
    }

    /**
     * Método getGifName
     * Retorna la ruta del archivo GIF asociado al BrainsteinZombie.
     * @return String Ruta relativa del archivo GIF.
     */
    @Override
    public String getGifName() {
        return "src/resources/FlagZombie.gif";
    }

    /**
     * Método attack
     * Este zombi no ataca a las plantas. Método sobrescrito para definir su comportamiento.
     * @param plant La planta objetiva (no utilizado en este caso).
     */
    @Override
    public void attack(Plant plant) {
    }

    /**
     * Método move
     * Este zombi no se mueve. Método sobrescrito para definir su comportamiento.
     */
    @Override
    public void move() {
    }

    /**
     * Genera un botón de cerebro que el jugador puede recoger para obtener puntos de cerebro.
     * @param layeredPane Panel donde se añadirá el botón de cerebro.
     * @param x Coordenada X del botón.
     * @param y Coordenada Y del botón.
     * @param updateBrainPointsLabel Método que actualiza la etiqueta de cerebros en la interfaz.
     */
    public void generateBrainButton(JLayeredPane layeredPane, int x, int y, Runnable updateBrainPointsLabel) {
        JButton brainButton = new JButton();
        brainButton.setBounds(x + 145, y + 100, 50, 50);
        brainButton.setOpaque(false);
        brainButton.setContentAreaFilled(false);
        brainButton.setBorderPainted(false);
        brainButton.setIcon(new ImageIcon("src/resources/Brain.gif"));

        layeredPane.add(brainButton, Integer.valueOf(2));
        layeredPane.revalidate();
        layeredPane.repaint();

        Timer reappearTimer = new Timer(PRODUCTION_INTERVAL, e -> brainButton.setVisible(true));
        reappearTimer.setRepeats(false);

        brainButton.addActionListener(e -> {
            brainButton.setVisible(false);
            board.addBrainPoints(BRAIN_PRODUCTION);
            updateBrainPointsLabel.run();
            reappearTimer.start();
        });

        if (productionTimer == null) {
            productionTimer = new Timer(PRODUCTION_INTERVAL, e -> brainButton.setVisible(true));
            productionTimer.start();
        }
    }
}
