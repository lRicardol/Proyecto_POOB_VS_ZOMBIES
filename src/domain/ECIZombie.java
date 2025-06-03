package domain;

public class ECIZombie extends Zombie {
    private static final double SHOOTING_INTERVAL = 3.0;
    private double elapsedTime;

    /**
     * Constructor de ECIZombie
     * Inicializa el zombie con sus atributos de salud, costo, daño, y posición en el tablero.
     * @param board El tablero en el que se encuentra el zombie.
     * @param row   La fila donde se posiciona el zombie.
     * @param col   La columna donde se posiciona el zombie.
     */
    public ECIZombie(Board board, int row, int col) {
        super(200, 250, 50, SHOOTING_INTERVAL, board, row, col);
        this.elapsedTime = 0;
    }

    /**
     * Método attack
     * Realiza un ataque disparando a la primera planta que se encuentre en su fila.
     * Si no hay objetivos, imprime un mensaje indicando que no tiene objetivo.
     */
    @Override
    public void attack(Plant plant) {
        Plant target = findTarget();
        if (target != null) {
            target.takeDamage(getDamage());
            System.out.println("ECIZombie shoots and deals " + getDamage() + " damage to a plant.");
        } else {
            System.out.println("ECIZombie has no target to shoot at.");
        }
    }

    /**
     * Método findTarget
     * Encuentra el primer objetivo (planta) en la misma fila del zombie.
     * @return La primera planta en la fila del zombie o null si no hay plantas.
     */
    public Plant findTarget() {
        if (board == null) {
            System.out.println("Board is not initialized for this ECIZombie.");
            return null;
        }
        return board.getFirstPlantInRow(row);
    }

    /**
     * Método move
     * Este zombie no se mueve, por lo que este método está intencionalmente vacío.
     */
    @Override
    public void move() {
    }

    /**
     * Método getGifName
     * Proporciona la ruta del archivo GIF que representa al zombie en la interfaz.
     * @return Ruta relativa del archivo GIF que representa al ECIZombie.
     */
    @Override
    public String getGifName() {
        return "src/resources/ECIzombie.gif";
    }
}
