package domain;

import java.util.Random;

public class ZombiesOriginal extends ZombieMachine{

    /**
     * Constructor para inicializar una instancia de ZombiesOriginal.
     * @param board El tablero del juego donde se generarán los zombis.
     */
    public ZombiesOriginal(Board board){
         super(board);
    }

    /**
     * Método que ejecuta la estrategia de generación de zombis.
     * Genera un zombi aleatorio en una posición aleatoria dentro de un rango permitido.
     */
    @Override
    public void executeStrategy() {
        int randomRow = new Random().nextInt(5);
        int randomCol = 7 + new Random().nextInt(3);

        String randomZombie = zombies[new Random().nextInt(zombies.length)];
        Zombie zombie = createZombie(randomZombie, randomRow, randomCol);

        if (board.addZombie(randomRow, randomCol, zombie)) {
            System.out.println("Zombie añadido en (" + randomRow + ", " + randomCol + ")");
        } else {
            System.out.println("No se pudo añadir el zombie en (" + randomRow + ", " + randomCol + ")");
        }
    }

}
