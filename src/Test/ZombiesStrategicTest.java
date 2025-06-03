
import domain.Board;
import domain.ZombiesStrategic;
import domain.Zombie;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZombiesStrategicTest {

    private Board board;
    private ZombiesStrategic zombiesStrategic;

    @Before
    public void setUp() {
        // Crear el tablero antes de cada prueba
        board = new Board(5, 5);
        zombiesStrategic = new ZombiesStrategic(board);
    }

    @Test
    public void testZombiesStrategicCreation() {
        // Verificar que el objeto ZombiesStrategic se cree correctamente
        assertNotNull(zombiesStrategic);
    }

    @Test
    public void testExecuteStrategy() {
        // Ejecutar la estrategia para añadir un zombi estratégico
        zombiesStrategic.executeStrategy();

        // Verificar que un zombi se haya colocado en la fila con menos plantas y la columna 9
        int rowWithLessPlants = board.getRowWithLessPlants(); // La fila con menos plantas
        Zombie zombie = board.getZombieAt(rowWithLessPlants, 9); // Verifica que el zombi esté en la columna 9

        assertNotNull(zombie); // El zombi debe estar en la celda especificada
        System.out.println("Zombi estratégico colocado en (" + rowWithLessPlants + ", 9)");
    }



}

