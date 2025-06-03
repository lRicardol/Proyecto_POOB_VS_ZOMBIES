package domain;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Peashooter extends Plant {
    private static final double SHOOTING_INTERVAL = 1.5;
    private Timer shootingTimer;

    /**
     * Constructor para crear una instancia de Peashooter.
     * @param board el tablero de juego en el que se encuentra la planta.
     * @param row   la fila donde se colocará la planta.
     * @param col   la columna donde se colocará la planta.
     */
    public Peashooter(Board board, int row, int col) {
        super(300, 100, 20, SHOOTING_INTERVAL, board, row, col);
        //shootingTimer = new Timer((int) (SHOOTING_INTERVAL * 1000), e -> shoot());
        //shootingTimer.start();
    }

    /**
     * Realiza la acción principal de la planta, que consiste en disparar proyectiles.
     */
    @Override
    public void performAction() {
        shoot();
    }

    /**
     * Devuelve la ruta de la imagen estática que representa a la planta.
     * @return la ruta de la imagen de la planta.
     */
    @Override
    public String getImageName() {
        return "src/resources/lanzaguisantes.png";
    }

    /**
     * Devuelve la ruta del archivo GIF que representa la animación de la planta.
     * @return la ruta del archivo GIF.
     */
    @Override
    public String getGifName() {
        return "src/resources/Peashooter.gif";
    }

    /**
     * Indica si la planta es del tipo TallNut.
     * @return false, ya que Peashooter no es una planta TallNut.
     */
    @Override
    public boolean isTallNut() {
        return false;
    }

    /**
     * Dispara un proyectil para atacar a los zombis en el camino.
     * Este método gestiona el comportamiento de disparo.
     */
    private void shoot() {
        if (row < 0 || col < 0) {
            System.err.println("Error: Peashooter no puede disparar desde una posición inválida (" + row + ", " + col + ").");
            return;
        }
        //showBulletEffect();
    }

    /**
     * Genera el efecto visual de disparo de un proyectil.
     * Crea un objeto gráfico que representa la bala y lo mueve por la pantalla.
     */
    private void showBulletEffect() {
        if (board.getLayeredPane() == null) {
            System.err.println("Error: JLayeredPane no está disponible en Board.");
            return;
        }

        JLabel bulletLabel = new JLabel(new ImageIcon("src/resources/PB00.png"));
        int cellWidth = 175;
        int cellHeight = 100;
        int startX = (col * cellWidth) + 60;
        int startY = (row * cellHeight) + 30;
        bulletLabel.setBounds(startX, startY, 30, 30);
        board.getLayeredPane().add(bulletLabel, Integer.valueOf(3));
        Timer bulletTimer = new Timer(50, new AbstractAction() {
            int x = startX;

            @Override
            public void actionPerformed(ActionEvent e) {
                x += 10; // Mueve la bala hacia la derecha
                bulletLabel.setLocation(x, startY);

                if (x > board.getColumns() * cellWidth) {
                    ((Timer) e.getSource()).stop(); // Detener el temporizador de la bala
                    board.getLayeredPane().remove(bulletLabel); // Eliminar el JLabel de la bala
                    board.getLayeredPane().revalidate();
                    board.getLayeredPane().repaint();
                }
            }
        });

        bulletTimer.start();
    }

    /**
     * Detiene la planta y su temporizador de disparo, eliminando cualquier acción en curso.
     */
    @Override
    public void destroy() {
        if (shootingTimer != null) {
            shootingTimer.stop();
            System.out.println("Peashooter detenido en (" + row + ", " + col + ")");
        }
        super.destroy();
    }
}
