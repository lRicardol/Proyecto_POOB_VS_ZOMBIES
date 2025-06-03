
import domain.Board;
import domain.ZombiesOriginal;
import domain.Zombie;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Random;

public class ZombiesOriginalTest {

    private Board board;
    private ZombiesOriginal zombiesOriginal;

    @Before
    public void setUp() {
        // Crear el tablero antes de cada prueba
        board = new Board(5, 5);
        zombiesOriginal = new ZombiesOriginal(board);
    }

    @Test
    public void testZombiesOriginalCreation() {
        // Verificar que el objeto ZombiesOriginal se cree correctamente
        assertNotNull(zombiesOriginal);
    }

    @Test
    public void testExecuteStrategy() {
        // Ejecutar la estrategia para añadir un zombi
        zombiesOriginal.executeStrategy();

        // Verificar que un zombi se haya colocado en el tablero
        // Dado que es aleatorio, verificamos que el zombi se haya colocado dentro de las filas y columnas correctas
        int randomRow = new Random().nextInt(5);
        int randomCol = 7 + new Random().nextInt(3); // Columna entre 7 y 9

        Zombie zombie = board.getZombieAt(randomRow, randomCol);
        assertNotNull(zombie); // El zombi debe estar en la celda

        System.out.println("Zombi colocado en (" + randomRow + ", " + randomCol + ")");
    }


}
