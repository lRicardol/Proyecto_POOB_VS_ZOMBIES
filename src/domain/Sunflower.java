package domain;


import javax.swing.*;
import javax.swing.Timer;

public class Sunflower extends Plant {
    private static final int SUN_PRODUCTION = 25;
    private static final int PRODUCTION_INTERVAL = 20000;
    private Timer productionTimer;

    /**
     * Constructor para inicializar un Sunflower.
     * @param board El tablero del juego.
     * @param row   La fila donde se coloca el Sunflower.
     * @param col   La columna donde se coloca el Sunflower.
     */
    public Sunflower(Board board,int row, int col) {
        super(300, 50, 0, 0,board,row,col);
    }

    /**
     * Realiza la acción del Sunflower (generación de sol).
     * En este caso, no tiene acción activa inmediata.
     */
    @Override
    public void performAction() {
    }

    /**
     * Devuelve si el Sunflower es un TallNut.
     * @return false, ya que no es un TallNut.
     */
    @Override
    public boolean isTallNut(){
        return false;
    }

    /**
     * Obtiene el nombre de la imagen estática del Sunflower.
     * @return La ruta de la imagen estática.
     */
    @Override
    public String getImageName() {
        return "src/resources/Sunflower.png";
    }

    /**
     * Obtiene el nombre del GIF animado del Sunflower.
     * @return La ruta del GIF animado.
     */
    @Override
    public String getGifName() {
        return "src/resources/SunflowerA.gif";
    }

    /**
     * Devuelve la cantidad de puntos de sol que produce el Sunflower.
     * @return La cantidad de puntos de sol producidos.
     */
    public int getSunProduction() {
        return SUN_PRODUCTION;
    }

    /**
     * Genera un botón de sol en la interfaz gráfica que otorga puntos de sol al presionarlo.
     * @param layeredPane El panel en capas donde se agrega el botón.
     * @param x La posición X del botón.
     * @param y La posición Y del botón.
     * @param updateSunPointsLabel Runnable para actualizar la etiqueta de puntos de sol en la interfaz.
     */
    public void generateSunButton(JLayeredPane layeredPane, int x, int y, Runnable updateSunPointsLabel) {
        JButton sunButton = new JButton();
        sunButton.setBounds(x + 145, y + 100, 50, 50);
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