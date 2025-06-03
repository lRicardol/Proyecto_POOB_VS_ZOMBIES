
import domain.BasicZombie;
import domain.Board;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BasicZombieTest {
    private Board board;
    private BasicZombie basicZombie;

    @Before
    public void setUp() {
        board = new Board(0, 0); // Crear el tablero
        basicZombie = new BasicZombie(board, 2, 9); // Crear el BasicZombie en la fila 2, columna 9
    }

    @Test
    public void testInitialization() {
        assertEquals(50, basicZombie.getHealth()); // Verificar la salud inicial
        assertEquals(2, basicZombie.getRow()); // Verificar la fila inicial
        assertEquals(9, basicZombie.getCol()); // Verificar la columna inicial
    }

    @Test
    public void testMovement() {
        basicZombie.move(); // Llamar a mover
        assertEquals(8, basicZombie.getCol()); // Verificar que se movió a la columna correcta
    }

    @Test
    public void testTakeDamage() {
        basicZombie.takeDamage(20); // Reducir salud
        assertEquals(30, basicZombie.getHealth()); // Verificar salud después del daño

        basicZombie.takeDamage(40); // Reducir salud nuevamente
        assertTrue(basicZombie.getHealth() <= 0); // Verificar que está destruido
    }
}
