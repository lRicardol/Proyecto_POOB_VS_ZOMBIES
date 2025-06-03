
import domain.Board;
import domain.ConeheadZombie;
import domain.Peashooter;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConeheadZombieTest {

    @Test
    public void testConeheadZombieCreation() {
        // Crear el tablero
        Board board = new Board(5, 5);

        // Crear el ConeheadZombie en la posición (2, 3)
        ConeheadZombie coneheadZombie = new ConeheadZombie(board, 2, 3);

        // Verificar que el ConeheadZombie se ha creado correctamente
        assertNotNull(coneheadZombie);
    }

    @Test
    public void testConeheadZombieGifName() {
        // Crear el tablero
        Board board = new Board(5, 5);

        // Crear el ConeheadZombie en la posición (2, 3)
        ConeheadZombie coneheadZombie = new ConeheadZombie(board, 2, 3);

        // Verificar que el nombre del GIF sea el esperado
        assertEquals("src/resources/ConeheadZombie.gif", coneheadZombie.getGifName());
    }

    @Test
    public void testMoveZombie() {
        // Crear el tablero
        Board board = new Board(5, 5);

        // Crear el ConeheadZombie en la posición (2, 3)
        ConeheadZombie coneheadZombie = new ConeheadZombie(board, 2, 3);

        // Crear un Peashooter en la posición (2, 2)
        Peashooter peashooter = new Peashooter(board, 2, 2);
        board.addPlant(4,3,peashooter);  // Asumimos que el método addPlant() existe

        // Intentar mover el zombie a la izquierda
        coneheadZombie.move();

        // Verificar que el zombie no se haya movido, ya que hay un Peashooter en su camino
        assertEquals(3, coneheadZombie.getCol());

        // Eliminar el Peashooter y mover nuevamente
        board.removePlantAt(4,3);  // Asumimos que el método removePlant() existe
        coneheadZombie.move();

        // Verificar que el zombie se haya movido correctamente
        assertEquals(2, coneheadZombie.getCol());
    }

    @Test
    public void testAttackPeashooter() {
        // Crear el tablero
        Board board = new Board(5, 5);

        // Crear el ConeheadZombie en la posición (2, 3)
        ConeheadZombie coneheadZombie = new ConeheadZombie(board, 2, 3);

        // Crear un Peashooter en la posición (2, 2)
        Peashooter peashooter = new Peashooter(board, 2, 2);
        board.addPlant(3,3,peashooter);  // Asumimos que el método addPlant() existe

        // Realizar el ataque
        coneheadZombie.attack(peashooter);

        // Verificar que el Peashooter haya recibido el daño esperado
        assertEquals(50, peashooter.getHealth()); // El Peashooter debería recibir 50 de daño
    }
}

