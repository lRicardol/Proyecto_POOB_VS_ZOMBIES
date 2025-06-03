package domain;

public abstract class ZombieMachine {
    protected static Board board;
    protected String[] zombies = {"BasicZombie", "ConeheadZombie", "BucketheadZombie", "BrainsteinZombie", "ECIZombie"};
    protected int rowLastZombieCreated;
    protected Zombie lastZombieCreated;

    /**
     * Constructor para inicializar una ZombieMachine.
     * @param board El tablero del juego donde se generarán los zombis.
     * @throws IllegalArgumentException Si el tablero es null.
     */
    public ZombieMachine(Board board) {
        if (board == null) {
            throw new IllegalArgumentException("El tablero no puede ser null.");
        }
        this.board = board;
    }

    /**
     * Método abstracto para ejecutar la estrategia de generación de zombis.
     */
    public abstract void executeStrategy();

    /**
     * Obtiene la fila donde se creó el último zombi.
     * @return La fila del último zombi creado.
     */
    public int getRowLastZombieCreated(){
        return rowLastZombieCreated;
    }

    /**
     * Obtiene el último zombi creado.
     * @return El último objeto Zombie creado.
     */
    public Zombie getLastZombieCreated(){
        return lastZombieCreated;
    }

    /**
     * Crea un zombi del tipo especificado en la fila y columna indicadas.
     * @param zombieType Tipo del zombi a crear. Ejemplos: "BasicZombie", "ConeheadZombie".
     * @param row Fila donde se colocará el zombi.
     * @param col Columna donde se colocará el zombi.
     * @return El objeto Zombie creado.
     */
    public Zombie createZombie(String zombieType, int row, int col) {
        Zombie newZombie;
        switch (zombieType) {
            case "BasicZombie":
                newZombie = new BasicZombie(board, row, col);
                break;
            case "BrainsteinZombie":
                newZombie = new BrainsteinZombie(board, row, col);
                break;
            case "BucketheadZombie":
                newZombie = new BucketheadZombie(board, row, col);
                break;
            case "ConeheadZombie":
                newZombie = new ConeheadZombie(board, row, col);
                break;
            case "ECIZombie":
                newZombie = new ECIZombie(board, row, col);
                break;
            default:
                newZombie = new BasicZombie(board, row, col);
        }
        newZombie.setCost(0);
        return newZombie;
    }
}
