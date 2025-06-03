

import domain.Board;
import domain.PlayerVSMachine;
import domain.Sunflower;
import domain.Zombie;
import domain.BasicZombie;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PlayerVSMachineTest {

    private Board board;
    private PlayerVSMachine game;

    @Before
    public void setUp() {
        // Crear un tablero vacío antes de cada prueba
        board = new Board(5, 5);
        game = new PlayerVSMachine(board, 100, 50);
    }

    @Test
    public void testStartGame() {
        // Verificar que el juego se inicia sin errores
        game.startGame();
        assertNotNull(game);  // Verifica que el objeto 'game' no sea nulo
    }

    @Test
    public void testAddPlant() {
        // Intentar añadir una planta a una celda vacía
        Sunflower plant = new Sunflower(board, 0, 0);
        boolean result = game.addPlant(0, 0, plant);
        assertTrue(result);  // Verifica que la planta se añadió correctamente
    }

    @Test
    public void testAddZombie() {
        // Intentar añadir un zombi en la última columna
        Zombie zombie = new BasicZombie(board, 0, 9);
        boolean result = game.addZombie(0, zombie);
        assertTrue(result);  // Verifica que el zombi se añadió correctamente
    }

    @Test
    public void testCheckWinCondition() {
        // Verificar la condición de victoria (cuando no hay zombis)
        boolean winCondition = game.checkWinCondition();
        assertFalse(winCondition);  // No debe haber victoria al principio
    }

    @Test
    public void testCheckLoseCondition() {
        // Añadir un zombi a la primera columna para simular la derrota
        Zombie zombie = new BasicZombie(board, 0, 0);
        board.addZombie(0, 0, zombie);

        boolean loseCondition = game.checkLoseCondition();
        assertTrue(loseCondition);  // La condición de derrota debe cumplirse
    }

    @Test
    public void testAutoGenerateZombies() {
        // Ejecutar el método de generación automática de zombis
        game.autoGenerateZombies();

        // Verificar que se haya generado al menos un zombi
        boolean zombiesGenerated = false;
        for (int row = 0; row < 5; row++) {
            if (board.getZombieAt(row, 9) != null) {
                zombiesGenerated = true;
                break;
            }
        }

        assertTrue(zombiesGenerated);  // Se debe haber generado al menos un zombi
    }
}

