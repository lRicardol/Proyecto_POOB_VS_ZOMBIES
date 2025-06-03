
import domain.Board;
import domain.BucketheadZombie;
import domain.Peashooter;
import org.junit.Test;
import static org.junit.Assert.*;

public class BucketheadZombieTest {

    @Test
    public void testBucketheadZombieCreation() {
        // Crear el tablero
        Board board = new Board(5, 5);

        // Crear el BucketheadZombie en la posición (2, 3)
        BucketheadZombie bucketheadZombie = new BucketheadZombie(board, 2, 3);

        // Verificar que el BucketheadZombie se ha creado correctamente
        assertNotNull(bucketheadZombie);
    }

    @Test
    public void testBucketheadZombieGifName() {
        // Crear el tablero
        Board board = new Board(5, 5);

        // Crear el BucketheadZombie en la posición (2, 3)
        BucketheadZombie bucketheadZombie = new BucketheadZombie(board, 2, 3);

        // Verificar que el nombre del GIF sea el esperado
        assertEquals("src/resources/BucketheadZombie.gif", bucketheadZombie.getGifName());
    }

    @Test
    public void testMoveZombie() {
        // Crear el tablero
        Board board = new Board(5, 5);

        // Crear el BucketheadZombie en la posición (2, 3)
        BucketheadZombie bucketheadZombie = new BucketheadZombie(board, 2, 3);

        // Crear un Peashooter en la posición (2, 2)
        Peashooter peashooter = new Peashooter(board, 2, 2);
        board.addPlant(1,1,peashooter);  // Asumimos que el método addPlant() existe

        // Intentar mover el zombie a la izquierda
        bucketheadZombie.move();

        // Verificar que el zombie no se haya movido, ya que hay un Peashooter en su camino
        assertEquals(3, bucketheadZombie.getCol());

        // Eliminar el Peashooter y mover nuevamente
        board.removePlantAt(1,1);  // Asumimos que el método removePlant() existe
        bucketheadZombie.move();

        // Verificar que el zombie se haya movido correctamente
        assertEquals(2, bucketheadZombie.getCol());
    }

    @Test
    public void testAttackPeashooter() {
        // Crear el tablero
        Board board = new Board(5, 5);

        // Crear el BucketheadZombie en la posición (2, 3)
        BucketheadZombie bucketheadZombie = new BucketheadZombie(board, 2, 3);

        // Crear un Peashooter en la posición (2, 2)
        Peashooter peashooter = new Peashooter(board, 2, 2);
        board.addPlant(0,0,peashooter);  // Asumimos que el método addPlant() existe

        // Realizar el ataque
        bucketheadZombie.attack(peashooter);

        // Verificar que el Peashooter haya recibido el daño esperado
        assertEquals(50, peashooter.getHealth()); // El Peashooter debería recibir 50 de daño
    }
}
