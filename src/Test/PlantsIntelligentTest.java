

import domain.Board;
import domain.PlantsIntelligent;
import domain.Peashooter;
import domain.Sunflower;
import domain.WallNut;
import domain.TallNut;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PlantsIntelligentTest {

    private Board board;
    private PlantsIntelligent plantsIntelligent;

    @Before
    public void setUp() {
        // Crear el tablero antes de cada prueba
        board = new Board(5, 5);
        plantsIntelligent = new PlantsIntelligent(board);
    }

    @Test
    public void testPlantsIntelligentCreation() {
        // Verificar que el objeto PlantsIntelligent se crea correctamente
        assertNotNull(plantsIntelligent);
    }

    @Test
    public void testExecuteStrategyProductionPlants() {
        // Verificar que las plantas de producción (girasoles) se coloquen correctamente
        plantsIntelligent.executeStrategy();

        // Verificar que un girasol se haya colocado en la primera fila, columna 1
        Sunflower sunflower = (Sunflower) board.getPlantAt(1, 1);
        assertNotNull(sunflower);
    }

    @Test
    public void testExecuteStrategyAttackPlants() {
        // Verificar que las plantas de ataque (lanzaguisantes) se coloquen correctamente
        plantsIntelligent.executeStrategy();
        plantsIntelligent.executeStrategy(); // Ejecutar más de una vez para añadir más lanzaguisantes

        // Verificar que se hayan colocado lanzaguisantes en las posiciones correctas
        Peashooter peashooter1 = (Peashooter) board.getPlantAt(2, 2);
        Peashooter peashooter2 = (Peashooter) board.getPlantAt(3, 3);

        assertNotNull(peashooter1);
        assertNotNull(peashooter2);
    }

    @Test
    public void testExecuteStrategyDefensePlants() {
        // Verificar que las plantas de defensa (nueces) se coloquen correctamente
        plantsIntelligent.executeStrategy();
        plantsIntelligent.executeStrategy();
        plantsIntelligent.executeStrategy(); // Ejecutar más de una vez para añadir más plantas de defensa

        // Verificar que se hayan colocado nueces y nueces altas en las posiciones correctas
        WallNut wallNut = (WallNut) board.getPlantAt(4, 4);
        TallNut tallNut = (TallNut) board.getPlantAt(5, 5);

        assertNotNull(wallNut);
        assertNotNull(tallNut);
    }

}

