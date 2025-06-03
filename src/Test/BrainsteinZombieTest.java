
import domain.Board;
import domain.BrainsteinZombie;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;

public class BrainsteinZombieTest {

    @Test
    public void testBrainsteinZombieCreation() {
        // Crear el tablero
        Board board = new Board(5, 5);

        // Crear el BrainsteinZombie en la posición (2, 3)
        BrainsteinZombie brainsteinZombie = new BrainsteinZombie(board, 2, 3);

        // Verificar que el BrainsteinZombie se ha creado correctamente
        assertNotNull(brainsteinZombie);
    }

    @Test
    public void testBrainsteinZombieGifName() {
        // Crear el tablero
        Board board = new Board(5, 5);

        // Crear el BrainsteinZombie en la posición (2, 3)
        BrainsteinZombie brainsteinZombie = new BrainsteinZombie(board, 2, 3);

        // Verificar que el nombre del GIF sea el esperado
        assertEquals("src/resources/FlagZombie.gif", brainsteinZombie.getGifName());
    }

    @Test
    public void testGenerateBrainButton() {
        // Crear el tablero
        Board board = new Board(5, 5);

        // Crear el BrainsteinZombie en la posición (2, 3)
        BrainsteinZombie brainsteinZombie = new BrainsteinZombie(board, 2, 3);

        // Crear un JPanel (JLayeredPane) para el test
        JLayeredPane layeredPane = new JLayeredPane();

        // Crear un Runnable que simula la actualización de los puntos de cerebro
        Runnable updateBrainPointsLabel = () -> {};

        // Verificar que no se lance ningún error al generar el botón de cerebro
        brainsteinZombie.generateBrainButton(layeredPane, 100, 100, updateBrainPointsLabel);

        // Verificar que el botón se haya añadido al panel
        assertEquals(1, layeredPane.getComponentCount());

        // Verificar que el botón es visible
        assertTrue(layeredPane.getComponent(0).isVisible());
    }
}

