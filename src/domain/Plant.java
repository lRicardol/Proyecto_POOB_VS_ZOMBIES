package domain;

public abstract class Plant implements Cloneable {
    private int health;
    private int cost;
    private int damage;
    private double attackInterval;
    protected Board board;
    protected int row;
    protected int col;

    /**
     * Constructor para inicializar los atributos de una planta.
     * @param health          la salud inicial de la planta (debe ser mayor a 0).
     * @param cost            el costo en puntos de sol para colocar la planta (debe ser no negativo).
     * @param damage          el daño que la planta inflige (debe ser no negativo).
     * @param attackInterval  el intervalo de ataque en segundos (debe ser mayor a 0).
     * @param board           referencia al tablero donde se encuentra la planta.
     * @param row             fila inicial donde se coloca la planta.
     * @param col             columna inicial donde se coloca la planta.
     * @throws IllegalArgumentException si algún parámetro no cumple con las condiciones esperadas.
     */
    public Plant(int health, int cost, int damage, double attackInterval, Board board, int row, int col) {
        if (health <= 0) {
            throw new IllegalArgumentException("Health must be greater than 0");
        }
        if (cost < 0) {
            throw new IllegalArgumentException("Cost must not be negative");
        }
        if (damage < 0) {
            throw new IllegalArgumentException("Damage must not be negative");
        }
        if (attackInterval < 0) {
            throw new IllegalArgumentException("Attack interval must be greater than 0");
        }

        this.health = health;
        this.cost = cost;
        this.damage = damage;
        this.attackInterval = attackInterval;
        this.board = board;
        this.row = row;
        this.col = col;
    }

    /**
     * Devuelve el costo de la planta en puntos de sol.
     * @return el costo de la planta.
     */
    public int getCost() {
        return cost;
    }

    /**
     * Devuelve el daño que puede infligir la planta.
     * @return el daño de la planta.
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Devuelve la salud actual de la planta.
     * @return la salud de la planta.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Establece una nueva posición para la planta.
     * @param newRow la nueva fila donde se colocará la planta.
     * @param newCol la nueva columna donde se colocará la planta.
     * @throws IllegalArgumentException si la nueva posición no es válida en el tablero.
     */
    public void setPosition(int newRow, int newCol) {
        if (!board.isValidPosition(newRow, newCol)) {
            throw new IllegalArgumentException("Invalid position: (" + newRow + ", " + newCol + ")");
        }
        this.row = newRow;
        this.col = newCol;
    }

    /**
     * Reduce la salud de la planta al recibir daño.
     * @param amount la cantidad de daño recibido (debe ser no negativa).
     * @throws IllegalArgumentException si la cantidad de daño es negativa.
     */
    public void takeDamage(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Damage amount cannot be negative");
        }
        this.health -= amount;
        System.out.println("Plant at (" + row + ", " + col + ") took " + amount + " damage. Remaining health: " + health);
    }

    /**
     * Comprueba si la planta ha muerto (su salud es 0 o menor).
     * @return true si la planta está muerta, false en caso contrario.
     */
    public boolean isDead() {
        return this.health <= 0;
    }

    /**
     * Realiza la acción principal de la planta (debe ser implementada por las subclases).
     */
    public abstract void performAction();

    /**
     * Devuelve la ruta del archivo GIF que representa la animación de la planta.
     * @return la ruta del archivo GIF.
     */
    public abstract String getGifName();

    /**
     * Devuelve la ruta de la imagen estática que representa la planta.
     * @return la ruta de la imagen.
     */
    public abstract String getImageName();

    /**
     * Indica si la planta es del tipo TallNut.
     * @return true si es una planta TallNut, false en caso contrario.
     */
    public abstract boolean isTallNut();

    /**
     * Destruye la planta, eliminándola del tablero.
     */
    public void destroy() {
        if (board != null) {
            System.out.println("Destroying plant at (" + row + ", " + col + ")");
            board.removePlant(row, col); // Remueve la planta del tablero
        }
    }

    /**
     * Verifica si la planta está en una posición válida en el tablero.
     * @return true si la posición es válida, false en caso contrario.
     */
    public boolean isInValidPosition() {
        return board.isValidPosition(row, col);
    }

    /**
     * Mueve la planta a una nueva posición en el tablero.
     * @param newRow la nueva fila donde se moverá la planta.
     * @param newCol la nueva columna donde se moverá la planta.
     * @throws IllegalArgumentException si la nueva posición no es válida.
     */
    public void move(int newRow, int newCol) {
        if (!board.isValidPosition(newRow, newCol)) {
            throw new IllegalArgumentException("Invalid position: (" + newRow + ", " + newCol + ")");
        }
        System.out.println("Moving plant from (" + row + ", " + col + ") to (" + newRow + ", " + newCol + ")");
        this.row = newRow;
        this.col = newCol;
    }

    /**
     * Crea una copia de la planta.
     * @return una nueva instancia que es un clon de la planta actual.
     * @throws AssertionError si ocurre un error al clonar la planta.
     */
    @Override
    public Plant clone() {
        try {
            return (Plant) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Error al clonar la planta", e);
        }
    }
}
