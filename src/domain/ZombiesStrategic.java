package domain;

import java.util.Random;

public class ZombiesStrategic extends ZombieMachine{

    /**
     * Constructor para inicializar una instancia de ZombiesStrategic.
     * @param board El tablero del juego donde se generarán los zombis estratégicamente.
     */
    public ZombiesStrategic(Board board) {
        super(board);
    }

    /**
     * Método que ejecuta la estrategia de generación de zombis.
     * Genera un zombi en la fila con menos plantas, utilizando un tipo de zombi seleccionado aleatoriamente.
     */
    @Override
    public void executeStrategy() {
        int row = board.getRowWithLessPlants();
        Random random = new Random();
        String randomZombie = zombies[random.nextInt(zombies.length)];
        Zombie zombie = createZombie(randomZombie, row, 9);

        if (board.addZombie(row, 9, zombie)) {
            rowLastZombieCreated = row;
            lastZombieCreated = zombie;
            System.out.println("Zombi estratégico añadido en (" + row + ", 9).");
        } else {
            System.err.println("No se pudo añadir el zombi estratégico en (" + row + ", 9).");
        }
    }
}
