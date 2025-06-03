package domain;

import javax.swing.*;

public abstract class Zombie {
    protected int health;
    protected int cost; // Costo del zombie
    protected int damage;
    protected double attackInterval; // Intervalo de ataque
    protected int row; // Fila del zombie
    protected int col; // Columna del zombie
    protected Board board;
    protected JLabel zombieLabel; // Representación gráfica del zombie

    /**
     * Constructor para inicializar un Zombie.
     * @param health         Salud inicial del zombie.
     * @param cost           Costo del zombie.
     * @param damage         Daño que el zombie inflige.
     * @param attackInterval Intervalo de tiempo entre ataques.
     * @param board          Tablero donde se encuentra el zombie.
     * @param row            Fila inicial del zombie.
     * @param col            Columna inicial del zombie.
     */
    public Zombie(int health, int cost, int damage, double attackInterval, Board board, int row, int col) {
        if(health <= 0){
            throw new IllegalArgumentException("Health must be greater than 0");
        }
        if(row < 0 || col < 0){
            throw new IllegalArgumentException("Row and column must be non-negative");
        }
        this.health = health;
        this.cost = cost;
        this.damage = damage;
        this.attackInterval = attackInterval;
        this.board = board;
        this.row = row;
        this.col = col;
        ImageIcon icon = new ImageIcon(getGifName());
        this.zombieLabel = new JLabel(icon);
    }

    /**
     * Método abstracto para mover al zombie.
     */
    public abstract void move();

    /**
     * Método abstracto para atacar una planta.
     * @param plant La planta objetivo del ataque.
     */
    public abstract void attack(Plant plant);

    /**
     * Método abstracto para obtener el nombre del GIF asociado al zombie.
     * @return Ruta del archivo GIF.
     */
    public abstract String getGifName();

    /**
     * Reduce la salud del zombie al recibir daño.
     * Si la salud cae a 0 o menos, el zombie es destruido.
     * @param damage Cantidad de daño recibido.
     */
    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            System.out.println("Zombie destroyed.");
            destroy();
        }
    }

    /**
     * Destruye al zombie, eliminándolo del tablero y actualizando la interfaz gráfica.
     */
    public void destroy() {
        System.out.println("Zombie at (" + row + ", " + col + ") is destroyed!");
        if (board != null) {
            JLabel dieGifLabel = new JLabel(new ImageIcon("src/resources/ZombieDie.gif"));
            dieGifLabel.setBounds(zombieLabel.getBounds());
            if (zombieLabel.getParent() != null) {
                zombieLabel.getParent().add(dieGifLabel, Integer.valueOf(3));
                zombieLabel.getParent().revalidate();
                zombieLabel.getParent().repaint();
            }
            Timer removeTimer = new Timer(3500, e -> {
                if (dieGifLabel.getParent() != null) {
                    dieGifLabel.getParent().remove(dieGifLabel);
                    dieGifLabel.getParent().revalidate();
                    dieGifLabel.getParent().repaint();
                }
            });
            removeTimer.setRepeats(false);
            removeTimer.start();
        }
        if (board != null) {
            board.removeZombieAt(row, col);
        }
        if (zombieLabel != null && zombieLabel.getParent() != null) {
            zombieLabel.getParent().remove(zombieLabel);
            zombieLabel.getParent().revalidate();
            zombieLabel.getParent().repaint();
        }
        row = -1;
        col = -1;
        board = null;
        zombieLabel = null;
    }

    /**
     * Obtiene el costo del zombie.
     * @return Costo del zombie.
     */
    public int getCost() {
        return cost;
    }

    /**
     * Obtiene la salud actual del zombie.
     * @return Salud actual.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Obtiene el daño que el zombie puede infligir.
     * @return Daño del zombie.
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Obtiene la fila donde se encuentra el zombie.
     * @return Fila actual del zombie.
     */
    public int getRow() {
        return row;
    }

    /**
     * Obtiene la columna donde se encuentra el zombie.
     * @return Columna actual del zombie.
     */
    public int getCol() {
        return col;
    }

    /**
     * Establece una nueva columna para el zombie.
     * @param col Nueva columna.
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * Obtiene el componente gráfico (JLabel) asociado al zombie.
     * @return JLabel del zombie.
     */
    public JLabel getLabel() {
        return zombieLabel;
    }

    /**
     * Establece el costo del zombie.
     * Nuevo costo.
     */
    public void setCost(int i) {
        this.cost = i;
    }

    /**
     * Establece una nueva fila para el zombie.
     * Nueva fila.
     */
    public void setRow(int currentRow) {
        this.row = currentRow;
    }
}
