
import domain.Board;
import domain.PlantsStrategic;
import domain.Peashooter;
import domain.Sunflower;
import domain.WallNut;
import domain.TallNut;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PlantsStrategicTest {

    private Board board;
    private PlantsStrategic plantsStrategic;

    @Before
    public void setUp() {
        // Crear el tablero antes de cada prueba
        board = new Board(5, 5);
        plantsStrategic = new PlantsStrategic(board);
    }

    @Test
    public void testPlantsStrategicCreation() {
        // Verificar que el objeto PlantsStrategic se crea correctamente
        assertNotNull(plantsStrategic);
    }

    @Test
    public void testExecuteStrategyProductionPlants() {
        // Verificar que las plantas de producción (girasoles) se coloquen correctamente de manera equilibrada
        plantsStrategic.executeStrategy();

        // Verificar que un girasol se haya colocado en la primera fila de la columna 0
        Sunflower sunflower = (Sunflower) board.getPlantAt(0, 0);
        assertNotNull(sunflower);
    }

    @Test
    public void testExecuteStrategyAttackPlants() {
        // Verificar que las plantas de ataque (lanzaguisantes) se coloquen correctamente de manera estratégica
        plantsStrategic.executeStrategy();
        plantsStrategic.executeStrategy(); // Ejecutar más de una vez para añadir más lanzaguisantes

        // Verificar que se hayan colocado lanzaguisantes en las posiciones correctas
        Peashooter peashooter = (Peashooter) board.getPlantAt(0, 1);
        assertNotNull(peashooter);
    }

    @Test
    public void testExecuteStrategyDefensePlants() {
        // Verificar que las plantas de defensa (nueces y nueces altas) se coloquen correctamente
        plantsStrategic.executeStrategy();
        plantsStrategic.executeStrategy();
        plantsStrategic.executeStrategy(); // Ejecutar más de una vez para añadir más plantas de defensa

        // Verificar que se haya colocado un WallNut o TallNut en las posiciones correctas
        WallNut wallNut = (WallNut) board.getPlantAt(0, 4);
        TallNut tallNut = (TallNut) board.getPlantAt(0, 3);

        assertNotNull(wallNut);
        assertNotNull(tallNut);
    }

}
