
import domain.Board;
import domain.Peashooter;
import org.junit.Test;
import static org.junit.Assert.*;

public class PeashooterTest {

    @Test
    public void testPeashooterCreation() {
        // Crear el tablero
        Board board = new Board(5, 5);

        // Crear el Peashooter en la posición (2, 3)
        Peashooter peashooter = new Peashooter(board, 2, 3);

        // Verificar que el Peashooter se creó correctamente
        assertNotNull(peashooter);
    }

    @Test
    public void testPeashooterGetImageName() {
        // Crear el tablero
        Board board = new Board(5, 5);

        // Crear el Peashooter en la posición (2, 3)
        Peashooter peashooter = new Peashooter(board, 2, 3);

        // Verificar que el nombre de la imagen sea el esperado
        assertEquals("src/resources/lanzaguisantes.png", peashooter.getImageName());
    }

    @Test
    public void testPeashooterGetGifName() {
        // Crear el tablero
        Board board = new Board(5, 5);

        // Crear el Peashooter en la posición (2, 3)
        Peashooter peashooter = new Peashooter(board, 2, 3);

        // Verificar que el nombre del GIF sea el esperado
        assertEquals("src/resources/Peashooter.gif", peashooter.getGifName());
    }

    @Test
    public void testPeashooterPerformAction() {
        // Crear el tablero
        Board board = new Board(5, 5);

        // Crear el Peashooter en la posición (2, 3)
        Peashooter peashooter = new Peashooter(board, 2, 3);

        // Llamar a performAction para ver si se ejecuta correctamente
        // Esto no debería causar ningún error, pero no esperamos efectos directos sin un zombie
        peashooter.performAction();
    }
}
