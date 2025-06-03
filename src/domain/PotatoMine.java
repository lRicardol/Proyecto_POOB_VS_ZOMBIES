package domain;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PotatoMine extends Plant {

    private static final int ACTIVATION_DELAY = 14000;
    private boolean isActive;
    private Timer activationTimer;
    private JButton potatoButton;

    /**
     * Constructor de PotatoMine.
     * @param board El tablero donde se coloca la planta.
     * @param row   La fila en la que se coloca la PotatoMine.
     * @param col   La columna en la que se coloca la PotatoMine.
     */
    public PotatoMine(Board board, int row, int col) {
        super(100, 25, 0, 0, board, row, col);
        this.isActive = false;
        this.activationTimer = new Timer(ACTIVATION_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isActive = true;
                if (potatoButton != null) {
                    potatoButton.setIcon(new ImageIcon(getGifName()));
                }
                activationTimer.stop();
            }
        });
        this.activationTimer.setRepeats(false);
        this.activationTimer.start();
    }

    /**
     * Indica si la planta es un TallNut.
     * @return Falso, ya que PotatoMine no es un TallNut.
     */
    @Override
    public boolean isTallNut(){
        return false;
    }

    /**
     * Realiza la acción de la PotatoMine, verificando si hay un zombi en la misma posición.
     * Si está activada y encuentra un zombi, explota y elimina al zombi.
     */
    @Override
    public void performAction() {
        if (!isActive) {
            return;
        }

        Zombie zombie = board.getZombieAt(row, col);
        if (zombie != null) {
            System.out.println("PotatoMine explotó en (" + row + ", " + col + ") y mató al zombi.");
            zombie.takeDamage(zombie.getHealth());
            this.takeDamage(this.getHealth());
        }
    }

    /**
     * Maneja el daño recibido por la PotatoMine.
     * Si está activada, explota al recibir daño y ejecuta su acción.
     * Si no está activada, solo reduce su salud.
     * @param damage La cantidad de daño recibido.
     */
    @Override
    public void takeDamage(int damage) {
        if (isActive) {
            System.out.println("PotatoMine en (" + row + ", " + col + ") explotó al recibir daño.");
            performAction();
        } else {
            super.takeDamage(damage);
            System.out.println("PotatoMine en (" + row + ", " + col + ") recibió daño mientras estaba inactiva.");
        }
    }

    /**
     * Obtiene el nombre del archivo de imagen estática de la PotatoMine.
     * @return Ruta del archivo de imagen de la PotatoMine.
     */
    @Override
    public String getImageName() {
        return "src/resources/PotatoMine.png";
    }

    /**
     * Obtiene el nombre del archivo GIF de la PotatoMine cuando está activa.
     * @return Ruta del archivo GIF activo de la PotatoMine.
     */
    @Override
    public String getGifName() {
        return "src/resources/PotatoMineA.gif";
    }

    /**
     * Obtiene el nombre del archivo GIF de la PotatoMine cuando está inactiva.
     * @return Ruta del archivo GIF inactivo de la PotatoMine.
     */
    public String getInactiveGifName() {
        return "src/resources/PotatoMineNotReady.gif";
    }

    /**
     * Genera un botón visual para representar la PotatoMine en la interfaz.
     * @param layeredPane          La capa donde se colocará el botón.
     * @param x                    Coordenada X para la posición del botón.
     * @param y                    Coordenada Y para la posición del botón.
     * @param updateSunPointsLabel Acción para actualizar los puntos de sol en la interfaz.
     */
    public void generatePotatoButton(JLayeredPane layeredPane, int x, int y, Runnable updateSunPointsLabel) {
        potatoButton = new JButton();
        potatoButton.setBounds(x + 145, y + 100, 50, 50);
        potatoButton.setOpaque(false);
        potatoButton.setContentAreaFilled(false);
        potatoButton.setBorderPainted(false);
        potatoButton.setIcon(new ImageIcon(getInactiveGifName()));
        layeredPane.add(potatoButton, Integer.valueOf(2));
        layeredPane.revalidate();
        layeredPane.repaint();
        Timer iconUpdateTimer = new Timer(ACTIVATION_DELAY, e -> {
            potatoButton.setIcon(new ImageIcon(getGifName()));
            System.out.println("PotatoMine botón activado visualmente.");
        });
        iconUpdateTimer.setRepeats(false);
        iconUpdateTimer.start();
        potatoButton.addActionListener(e -> {
            if (isActive) {
                performAction();
            } else {
                System.out.println("PotatoMine aún no está activada.");
            }
        });
    }
}
