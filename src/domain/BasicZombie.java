package domain;

public class BasicZombie extends Zombie {

    private int elapsedTime; // Tiempo acumulado para manejar el ataque

    /**
     * Constructor de BasicZombie
     * Inicializa un zombi básico con salud, daño, costo y un intervalo de ataque predeterminados.
     * @param board Tablero en el que se posicionará el zombi.
     * @param row   Fila inicial donde se ubicará el zombi.
     * @param col   Columna inicial donde se ubicará el zombi.
     */
    public BasicZombie(Board board, int row, int col) {
        super(100, 100, 10, 1.0, board, row, col); // Salud: 100, Costo: 100, Daño: 10, Intervalo de ataque: 1 segundo
        this.elapsedTime = 0;
    }

    /**
     * Método move
     * Define el comportamiento del movimiento del zombi.
     * Si no hay una planta en la celda adyacente a la izquierda, el zombi está listo para moverse.
     */
    @Override
    public void move() {
        if (col > 0) {
            Plant plant = board.getPlantAt(row, col - 1);
            if (plant == null) {
                System.out.println("Zombie at (" + row + ", " + col + ") ready to move.");
            }
        }
    }

    /**
     * Método attack
     * Ataca a una planta específica, infligiendo daño basado en el atributo `damage`.
     * El ataque solo ocurre si se ha acumulado el tiempo suficiente basado en el intervalo de ataque del zombi.
     * @param plant La planta que será atacada.
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
     * Retorna la ruta del archivo GIF asociado al zombi básico.
     * @return String Ruta relativa del archivo GIF.
     */
    @Override
    public String getGifName() {
        return "src/resources/Zombie.gif"; // Ruta relativa al archivo GIF del zombi
    }
}
