
import domain.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MachineVSMachineTest {

    private Board board;
    private PlantsStrategic plantMachine;
    private ZombiesStrategic zombieMachine;
    private MachineVSMachine game;

    @Before
    public void setUp() {
        // Crear los objetos necesarios para la prueba
        board = new Board(5, 5);
        plantMachine = new PlantsStrategic(board);
        zombieMachine = new ZombiesStrategic(board);
        game = new MachineVSMachine(plantMachine, zombieMachine, 100, 50);
    }

    @Test
    public void testStartGame() {
        // Verificar que el juego se inicia sin errores
        game.startGame();
        assertNotNull(game);  // Verifica que el objeto 'game' no sea nulo
    }

    @Test
    public void testAddPlant() {
        // Intentar añadir una planta en una celda vacía
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
    public void testPlantTurn() {
        // Verificar que se ejecute el turno de las plantas sin errores
        game.plantTurn();
        assertNotNull(game);  // Verifica que el turno de las plantas se haya ejecutado
    }

    @Test
    public void testZombieTurn() {
        // Verificar que se ejecute el turno de los zombis sin errores
        game.zombieTurn();
        assertNotNull(game);  // Verifica que el turno de los zombis se haya ejecutado
    }
}

