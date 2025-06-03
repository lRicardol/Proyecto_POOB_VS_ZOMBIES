
import domain.Plant;
import domain.Board;
import domain.ECIZombie;
import domain.Peashooter;
import org.junit.Test;
import static org.junit.Assert.*;

public class ECIZombieTest {

    @Test
    public void testECIZombieCreation() {
        // Crear el tablero
        Board board = new Board(5, 5);

        // Crear el ECIZombie en la posición (2, 3)
        ECIZombie eciZombie = new ECIZombie(board, 2, 3);

        // Verificar que el ECIZombie se ha creado correctamente
        assertNotNull(eciZombie);
    }

    @Test
    public void testECIZombieGifName() {
        // Crear el tablero
        Board board = new Board(5, 5);

        // Crear el ECIZombie en la posición (2, 3)
        ECIZombie eciZombie = new ECIZombie(board, 2, 3);

        // Verificar que el nombre del GIF sea el esperado
        assertEquals("src/resources/ECIzombie.gif", eciZombie.getGifName());
    }

    @Test
    public void testAttackPeashooter() {
        // Crear el tablero
        Board board = new Board(5, 5);

        // Crear el ECIZombie en la posición (2, 3)
        ECIZombie eciZombie = new ECIZombie(board, 2, 3);

        // Crear un Peashooter en la posición (2, 2)
        Peashooter peashooter = new Peashooter(board, 2, 2);
        board.addPlant(2,2,peashooter);  // Asumimos que el método addPlant() existe

        // Realizar el ataque
        eciZombie.attack(peashooter);

        // Verificar que el Peashooter haya recibido el daño esperado
        assertEquals(50, peashooter.getHealth()); // El Peashooter debería recibir 50 de daño
    }

    @Test
    public void testFindTarget() {
        // Crear el tablero
        Board board = new Board(5, 5);

        // Crear el ECIZombie en la posición (2, 3)
        ECIZombie eciZombie = new ECIZombie(board, 2, 3);

        // Crear un Peashooter en la posición (2, 2)
        Peashooter peashooter = new Peashooter(board, 2, 2);
        board.addPlant(2,2,peashooter);  // Asumimos que el método addPlant() existe

        // Verificar que el ECIZombie puede encontrar al Peashooter como objetivo
        Plant target = eciZombie.findTarget();

        // Verificar que el objetivo encontrado es el Peashooter
        assertNotNull(target);
        assertEquals(peashooter, target);
    }

    @Test
    public void testMove() {
        // Crear el tablero
        Board board = new Board(5, 5);

        // Crear el ECIZombie en la posición (2, 3)
        ECIZombie eciZombie = new ECIZombie(board, 2, 3);

        // Intentar mover el zombie (pero no debería moverse)
        eciZombie.move();

        // Verificar que la posición no ha cambiado
        assertEquals(3, eciZombie.getCol());
    }
}

