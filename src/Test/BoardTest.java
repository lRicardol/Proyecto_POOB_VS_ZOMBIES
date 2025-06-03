
import domain.Board;
import domain.Plant;
import domain.Zombie;
import org.junit.Test;
import static org.junit.Assert.*;

public class BoardTest {

    @Test
    public void testInitializeBoard() {
        // Crear el tablero con puntos iniciales
        Board board = new Board(100, 5);

        // Verificar que el tablero está vacío inicialmente (sin plantas ni zombis)
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                assertNull(board.getPlantAt(row, col));
                assertNull(board.getZombieAt(row, col));
            }
        }
    }

    @Test
    public void testAddPlant() {
        Board board = new Board(100, 5);

        // Crear una planta con un costo de 50 puntos solares, daño de 10, y otros valores arbitrarios
        Plant plant = new Plant(100, 50, 10, 1.0, board, 2, 3) {
            @Override
            public void performAction() {
                // Acción de la planta, puede estar vacía para este ejemplo
            }

            @Override
            public String getGifName() {
                return "plant.gif";
            }

            @Override
            public String getImageName() {
                return "plant.png";
            }

            @Override
            public boolean isTallNut() {
                return false;
            }
        };

        // Intentar añadir la planta
        boolean success = board.addPlant(2, 3, plant);

        // Verificar que la planta fue añadida correctamente
        assertTrue(success);
        assertEquals(50, board.getSunPoints()); // Después de agregar la planta, los puntos solares deberían ser 50
        assertNotNull(board.getPlantAt(2, 3)); // La planta debe estar en la posición (2, 3)
    }

    @Test
    public void testAddZombie() {
        Board board = new Board(100, 5);

        // Crear un zombie con salud de 100, daño de 10, y otros valores arbitrarios
        Zombie zombie = new Zombie(100, 50, 10, 1.5, board, 2, 5) {
            @Override
            public void move() {
                // Implementación básica de movimiento
                this.setCol(this.getCol() - 1);
            }

            @Override
            public void attack(Plant plant) {
                // Implementación básica de ataque
                plant.takeDamage(this.getDamage());
            }

            @Override
            public String getGifName() {
                return "zombie.gif";
            }
        };

        // Intentar añadir el zombie
        boolean success = board.addZombie(2, 5, zombie);

        // Verificar que el zombie fue añadido correctamente
        assertTrue(success);
        assertNotNull(board.getZombieAt(2, 5)); // El zombie debe estar en la posición (2, 5)
    }

    @Test
    public void testZombieMovement() {
        Board board = new Board(100, 5);

        // Crear un zombie
        Zombie zombie = new Zombie(100, 50, 10, 1.5, board, 2, 9) {
            @Override
            public void move() {
                // Mover el zombie hacia la izquierda (simula un movimiento)
                this.setCol(this.getCol() - 1);
            }

            @Override
            public void attack(Plant plant) {
                // No implementado en este caso
            }

            @Override
            public String getGifName() {
                return "zombie.gif";
            }
        };

        // Añadir el zombie
        board.addZombie(2, 9, zombie);

        // Mover los zombies
        zombie.move();

        // Verificar que el zombie se movió correctamente
        assertEquals(8, zombie.getCol()); // El zombie debería haberse movido a la columna 8
    }
}
