package domain;

public class BucketheadZombie extends Zombie {

    private int elapsedTime;

    /**
     * Constructor de BucketheadZombie
     * Inicializa un zombi con características específicas: alta salud y ataque moderado.
     * @param board Tablero en el que se encuentra el zombi.
     * @param row   Fila inicial donde se posiciona el zombi.
     * @param col   Columna inicial donde se posiciona el zombi.
     */
    public BucketheadZombie(Board board, int row, int col) {
        super(800, 200, 100, 0.5, board, row, col);
        this.elapsedTime = 0;
    }

    /**
     * Método move
     * Controla el movimiento del zombi en el tablero. Si no hay una planta en la celda frontal,
     * el zombi avanza una posición hacia la izquierda.
     */
    @Override
    public void move() {
        if (col > 0) {
            Plant plant = board.getPlantAt(row, col - 1);
            if (plant == null) {
                board.moveZombie(row, col, row, col - 1);
                col--;
                System.out.println("Zombie moved to position (" + row + ", " + col + ")");
            }
        }
    }

    /**
     * Método attack
     * Realiza un ataque a una planta adyacente, infligiéndole daño cada cierto intervalo de tiempo.
     * @param plant La planta objetiva que recibirá el daño.
     */
    @Override
    public void attack(Plant plant) {
        elapsedTime += 0.1;
        if (elapsedTime >= attackInterval) {
            plant.takeDamage(damage);
            System.out.println("Zombie attacks plant for " + damage + " damage");
            elapsedTime = 0;
        }
    }

    /**
     * Método getGifName
     * Retorna la ruta del archivo GIF asociado al BucketheadZombie.
     * @return String Ruta relativa del archivo GIF.
     */
    @Override
    public String getGifName() {
        return "src/resources/BucketheadZombie.gif";
    }
}
