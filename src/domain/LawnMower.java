package domain;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import presentation.*;

public class LawnMower {
    private boolean isActive;
    private final int row;
    private int currentColumn;
    private final JLabel lawnMowerLabel;
    private final JLayeredPane layeredPane;
    private final List<Zombie> zombiesInRow;
    private final Nivel3 nivel3;
    private final Nivel3Noche Nivel3Noche;
    private final PvPmode PvPmode;
    private final MvMmode MvMmode;

    /**
     * Constructor para la clase LawnMower.
     * Inicializa una podadora de césped en el juego, configurando su posición, estado y representación gráfica.
     * @param row          La fila en la que opera la podadora.
     * @param layeredPane  El panel de capas utilizado para mostrar la podadora en la interfaz.
     * @param zombiesInRow La lista de zombis presentes en la fila correspondiente.
     * @param nivel3       Referencia al nivel 3 diurno del juego.
     * @param nivel3Noche  Referencia al nivel 3 nocturno del juego.
     * @param pvPmode      Referencia al modo de juego jugador contra jugador.
     * @param mvMmode      Referencia al modo de juego máquina contra máquina.
     */
    public LawnMower(int row, JLayeredPane layeredPane, List<Zombie> zombiesInRow, Nivel3 nivel3, Nivel3Noche nivel3Noche, PvPmode pvPmode, MvMmode mvMmode) {
        this.row = row;
        this.currentColumn = 0;
        this.isActive = false;
        this.layeredPane = layeredPane;
        this.zombiesInRow = zombiesInRow;
        this.nivel3 = nivel3;
        this.Nivel3Noche = nivel3Noche;
        this.PvPmode = pvPmode;
        this.MvMmode = mvMmode;
        ImageIcon icon = new ImageIcon("src/resources/LawnCleaner.png");
        this.lawnMowerLabel = new JLabel(icon);
        int x = 130;
        int y = row * 100;
        this.lawnMowerLabel.setBounds(x, y, 80, 140);
        layeredPane.add(lawnMowerLabel, Integer.valueOf(2));
    }

    /**
     * Activa la podadora en la fila correspondiente.
     * Si la podadora no está activa, su estado cambia a activo, eliminando todos los zombis de la fila
     * y moviéndose a lo largo de la fila hasta salir del tablero. Además, notifica a los observadores del tablero
     * sobre su activación. Si ya ha sido activada, no realiza ninguna acción adicional.
     * @param board El tablero del juego donde se encuentra la podadora.
     */
    public void activate(Board board) {
        if (!isActive) {
            isActive = true;
            logMessage("LawnMower in row " + row + " is activated!");
            for (Board.BoardObserver observer : board.getObservers()) {
                observer.updateLawnMower(row, true);
            }
            removeAllZombiesInRow();

            moveLawnMower(board);
        } else {
            logMessage("LawnMower in row " + row + " has already been used.");
        }
    }

    /**
     * Elimina todos los zombis presentes en la fila donde se encuentra la podadora.
     * Para cada zombi en la lista de zombis de la fila:
     * Una vez procesados todos los zombis, la lista de zombis en la fila se limpia.
     */
    private void removeAllZombiesInRow() {
        for (Zombie zombie : zombiesInRow) {
            if (zombie.getRow() == row) {
                logMessage("Zombie at (" + row + ", " + zombie.getCol() + ") was mowed down!");
                zombie.takeDamage(Integer.MAX_VALUE);
                nivel3.updateCell(row, zombie.getCol());
            }
        }
        zombiesInRow.clear();
    }

    /**
     * Mueve la podadora a lo largo de la fila, eliminando todos los zombis en su camino.
     * @param board Tablero del juego donde se procesan los zombis y la podadora.
     */
    private void moveLawnMower(Board board) {
        Timer timer = new Timer(100, new ActionListener() {
            private int currentX = lawnMowerLabel.getX();
            private final int stepSize = 50;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentColumn < Board.getColumns()) {
                    currentX += stepSize;
                    lawnMowerLabel.setLocation(currentX, lawnMowerLabel.getY());
                    layeredPane.repaint();
                    Zombie zombie = board.getZombieAt(row, currentColumn);
                    if (zombie != null) {
                        zombie.takeDamage(Integer.MAX_VALUE);
                        board.removeZombieAt(row, currentColumn);
                        nivel3.updateCell(row, currentColumn);
                        System.out.println("Zombie removed at (" + row + ", " + currentColumn + ")");
                    }
                    currentColumn++;
                } else {
                    ((Timer) e.getSource()).stop();
                    layeredPane.remove(lawnMowerLabel);
                    layeredPane.revalidate();
                    layeredPane.repaint();
                    System.out.println("LawnMower in row " + row + " has exited the screen.");
                }
            }
        });
        timer.start();
    }

    /**
     * Elimina todos los zombis presentes en la fila donde se encuentra la podadora.
     * @param board Tablero del juego donde se procesan los zombis.
     */
    private void removeZombiesInCell(Board board) {
        for (Zombie zombie : zombiesInRow) {
            if (zombie.getRow() == row) {
                logMessage("LawnMower hits zombie at (" + row + ", " + zombie.getCol() + ")");
                zombie.takeDamage(Integer.MAX_VALUE);
            }
        }
    }

    /**
     * Obtiene el componente visual de la podadora (JLabel).
     * @return JLabel que representa la podadora en la interfaz.
     */
    public JLabel getLabel() {
        return lawnMowerLabel;
    }

    /**
     * Verifica si la podadora está activa.
     * @return true si la podadora está activa, false en caso contrario.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Obtiene la fila donde se encuentra la podadora.
     * @return Fila actual de la podadora.
     */
    public int getRow() {
        return row;
    }

    /**
     * Registra un mensaje en la consola.
     * @param message Mensaje a registrar.
     */
    private void logMessage(String message) {
        System.out.println(message);
    }

    /**
     * Verifica si hay zombis en la fila actual y activa la podadora si se detecta un zombi en la columna inicial.
     * @param board Tablero del juego donde se procesan los zombis.
     */
    public void checkForZombie(Board board) {
        System.out.println("Checking for zombies in row " + row);
        if (!isActive) {
            for (Zombie zombie : zombiesInRow) {
                if (zombie.getCol() == 1) {
                    System.out.println("Zombie detected in row " + row + ", column 1");
                    activate(board);
                    break;
                }
            }
        }
    }
}
