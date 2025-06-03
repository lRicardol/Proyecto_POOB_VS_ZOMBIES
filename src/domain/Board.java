package domain;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Represents the game board where plants, zombies, and other elements interact.
 * This class manages the placement and actions of plants, zombies, and lawn mowers.
 * It tracks resources such as sun points and brains, and updates the game state.
 */
public class Board {
    private static final int ROWS = 5;
    private static final int COLUMNS = 10;
    private LawnMower[] lawnMowers;
    private Plant[][] plants;
    private Zombie[][] zombies;
    private static int sunPoints;
    private static int brains;
    private List<BoardObserver> observers;
    private JLayeredPane layeredPane;

    /**
     * Constructor de la clase Board.
     * Inicializa el tablero con las plantas, zombis, podadoras y recursos iniciales.
     * @param initialSunPoints cantidad inicial de puntos de sol.
     * @param initialBrains cantidad inicial de cerebros.
     */
    public Board(int initialSunPoints, int initialBrains) {
        this.plants = new Plant[ROWS][COLUMNS];
        this.zombies = new Zombie[ROWS][COLUMNS];
        this.sunPoints = initialSunPoints;
        this.brains = initialBrains;
        this.observers = new ArrayList<>();
        this.lawnMowers = new LawnMower[ROWS];
        initializeLawnMowers();
    }

    public void notifyCellUpdated(int productionPlantRow, int i) {

    }

    public void removePlant(int row, int col) {
    }

    public int getBrainsPoints()  {
        return brains;
    }

    public class LawnMower {
        private int row;
        private boolean active;

        public LawnMower(int row) {
            this.row = row;
        }

        public boolean isActive() {
            return active;
        }

        public void activate(Board board) {
            if (!active) {
                active = true;
                System.out.println("Lawnmower activated in row " + row);

                for (int col = 0; col < COLUMNS; col++) {
                    if (board.getZombieAt(row, col) != null) {
                        board.removeZombieAt(row, col);
                        System.out.println("Zombie removed at (" + row + ", " + col + ")");
                    }
                }
            }
        }
    }
    private void initializeLawnMowers() {
        for (int i = 0; i < ROWS; i++) {
            this.lawnMowers[i] = new LawnMower(i);
        }
    }

    public void addObserver(BoardObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(int row, int col) {
        for (BoardObserver observer : observers) {
            observer.updateCell(row, col, plants[row][col], zombies[row][col]);
        }
    }

    public List<BoardObserver> getObservers() {
        return observers;
    }

    /**
     * Añade una planta al tablero en una posición específica.
     * @param row fila en la que se colocará la planta.
     * @param col columna en la que se colocará la planta.
     * @param plant instancia de la planta a agregar.
     * @return true si la planta se añadió correctamente, false en caso contrario.
     */
    public boolean addPlant(int row, int col, Plant plant) {
        if (isValidPosition(row, col) && plants[row][col] == null) {
            if (sunPoints >= plant.getCost()) {
                plants[row][col] = plant;
                sunPoints -= plant.getCost();
                System.out.println("Plant added at position (" + row + ", " + col + ")");
                notifyObservers(row, col);
                return true;
            } else {
                System.out.println("Not enough sun points.");
            }
        }
        return false;
    }

    /**
     * Añade un zombi al tablero en una posición específica.
     * @param row fila en la que se colocará el zombi.
     * @param col columna en la que se colocará el zombi.
     * @param zombie instancia del zombi a agregar.
     * @return true si el zombi se añadió correctamente, false en caso contrario.
     */
    public boolean addZombie(int row, int col, Zombie zombie) {
        if (isValidPosition(row, col) && zombies[row][col] == null) {
            zombies[row][col] = zombie;
            notifyObservers(row, col);
            return true;
        }
        return false;
    }

    public boolean addZombie(int row, Zombie zombie) {
        return addZombie(row, 9, zombie);
    }


    public int getColOfFirstZombieInRow(int row) {
        if (isValidPosition(row, 0)) {
            for (int col = 0; col < COLUMNS; col++) {
                if (zombies[row][col] != null) {
                    return col;
                }
            }
        }
        return 0; // Si no hay zombies en la fila
    }

    public boolean tallNutInThisRow(int row) {
        for (int col = 0; col < COLUMNS; col++) {
            if (plants[row][col] != null && plants[row][col].isTallNut()) {
                return true;
            }
        }
        return false;
    }

    public boolean isCellEmpty(int row, int col) {
        return isValidPosition(row, col) && plants[row][col] == null && zombies[row][col] == null;
    }

    /**
     * Mueve todos los zombis en el tablero.
     * Si un zombi alcanza la casa, el jugador pierde.
     */
    public void moveZombies() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = COLUMNS - 1; col >= 0; col--) {
                Zombie zombie = zombies[row][col];
                if (zombie != null) {
                    int nextCol = col - 1;
                    if (nextCol < 0 && lawnMowers[row] != null && !lawnMowers[row].isActive()) {
                        lawnMowers[row].activate(this);
                        removeZombieAt(row, col);
                        break;
                    }

                    if (nextCol >= 0) {
                        Plant plant = plants[row][nextCol];
                        if (plant == null) {
                            moveZombie(row, col, row, nextCol);
                        } else {
                            zombie.attack(plant);
                            if (plant.isDead()) {
                                removePlantAt(row, nextCol);
                            }
                        }
                    } else {
                        System.out.println("Zombie reached the house in row " + row + "! Player loses!");
                    }
                }
            }
        }
    }


    public void updatePlants() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Plant plant = plants[row][col];
                if (plant != null) {
                    plant.performAction();
                }
            }
        }
    }

    /**
     * Mueve un zombi de una posición a otra en el tablero.
     * @param oldRow fila de origen.
     * @param oldCol columna de origen.
     * @param newRow fila de destino.
     * @param newCol columna de destino.
     */
    public void moveZombie(int oldRow, int oldCol, int newRow, int newCol) {
        if (isValidPosition(newRow, newCol)) {
            zombies[newRow][newCol] = zombies[oldRow][oldCol];
            zombies[oldRow][oldCol] = null;
            notifyObservers(oldRow, oldCol);
            notifyObservers(newRow, newCol);
        }
    }

    public Zombie getZombieAt(int row, int col) {
        return isValidPosition(row, col) ? zombies[row][col] : null;
    }

    public Plant getPlantAt(int row, int col) {
        return isValidPosition(row, col) ? plants[row][col] : null;
    }

    public void removePlantAt(int row, int col) {
        if (isValidPosition(row, col)) {
            plants[row][col] = null;
            notifyObservers(row, col);
        }
    }

    /**
     * Elimina una planta del tablero en una posición específica.
     * @param row fila de la planta a eliminar.
     * @param col columna de la planta a eliminar.
     */
    public void removeZombieAt(int row, int col) {
        if (isValidPosition(row, col) && zombies[row][col] != null) {
            zombies[row][col] = null;
            notifyObservers(row, col);
        }
    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLUMNS;
    }

    public int getSunPoints() {
        return sunPoints;
    }

    public int getBrainPoints(){return brains;}

    public static int getColumns() {
        return COLUMNS;
    }

    public void setSunPoints(int sunPoints) {
        this.sunPoints = sunPoints;
        System.out.println("Sun points set to: " + sunPoints);
    }

    public void addSunPoints(int points) {
        this.sunPoints += points;
        System.out.println("Sun points updated to: " + this.sunPoints);
    }

    /**
     * Limpia el tablero eliminando todas las plantas y zombis.
     */
    public void clearBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                plants[row][col] = null;
                zombies[row][col] = null;
                notifyObservers(row, col);
            }
        }
        System.out.println("Board cleared.");
    }

    public boolean noZombiesLeft() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (zombies[row][col] != null) {
                    return false;
                }
            }
        }
        return true;
    }

    public interface BoardObserver {
        void updateCell(int row, int col, Plant plant, Zombie zombie);

        void updateLawnMower(int row, boolean isActive);
    }

    public Plant getFirstPlantInRow(int row) {
        if (row < 0 || row >= ROWS) {
            System.err.println("Row out of bounds: " + row);
            return null;
        }
        for (int col = 0; col < COLUMNS; col++) {
            if (plants[row][col] != null) {
                return plants[row][col];
            }
        }
        return null;
    }

    public int getFirstRowWithoutPlant(int col){
        for(int row = 0; row < ROWS; row++){
            if(plants[row][col] == null){
                return row;
            }
        }
        return 10;
    }

    public void addBrainPoints(int points) {
        this.brains += points;
        System.out.println("Brain points updated: " + this.brains);
    }

    public int getRowWithLessPlants() {
        int currentRowWithLessPlants = 0;
        int minPlantsCount = Integer.MAX_VALUE;

        for (int row = 0; row < ROWS; row++) {
            int numberOfPlantsInOneRow = 0;

            for (int col = 0; col < COLUMNS; col++) {
                if (plants[row][col] != null) {
                    numberOfPlantsInOneRow++;
                }
            }
            if (numberOfPlantsInOneRow < minPlantsCount) {
                minPlantsCount = numberOfPlantsInOneRow;
                currentRowWithLessPlants = row;
            }
        }

        return currentRowWithLessPlants;
    }

    public int getRowWithMostZombies() {
        int currentRowWithMostZombies = 0;
        int maxZombiesCount = 0;

        for (int row = 0; row < ROWS; row++) {
            int numberOfZombiesInOneRow = 0;

            for (int col = 0; col < COLUMNS; col++) {
                if (zombies[row][col] != null) {
                    numberOfZombiesInOneRow++;
                }
            }

            if (numberOfZombiesInOneRow > maxZombiesCount) {
                maxZombiesCount = numberOfZombiesInOneRow;
                currentRowWithMostZombies = row;
            }
        }

        return currentRowWithMostZombies;
    }

    public List<Zombie> getZombiesInRow(int row) {
        List<Zombie> zombiesInRow = new ArrayList<>();
        for (int col = 0; col < COLUMNS; col++) {
            Zombie zombie = zombies[row][col];
            if (zombie != null) {
                zombiesInRow.add(zombie);
            }
        }
        return zombiesInRow;
    }

    public void setLayeredPane(JLayeredPane layeredPane) {
        this.layeredPane = layeredPane;
    }

    public JLayeredPane getLayeredPane() {
        return this.layeredPane;
    }

    public void saveState(BufferedWriter writer) throws IOException {
        writer.write("SunPoints:" + sunPoints + "\n");
        writer.write("Brains:" + brains + "\n");

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (plants[row][col] != null) {
                    writer.write("Plant:" + plants[row][col].getClass().getSimpleName() + "," + row + "," + col + "\n");
                }
            }
        }

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (zombies[row][col] != null) {
                    writer.write("Zombie:" + zombies[row][col].getClass().getSimpleName() + "," + row + "," + col + "\n");
                }
            }
        }
    }

    public void loadState(String line) {
        if (line.startsWith("SunPoints:")) {
            this.sunPoints = Integer.parseInt(line.split(":")[1]);
        } else if (line.startsWith("Brains:")) {
            this.brains = Integer.parseInt(line.split(":")[1]);
        } else if (line.startsWith("Plant:")) {
            String[] parts = line.split(",");
            String plantType = parts[0].split(":")[1];
            int row = Integer.parseInt(parts[1]);
            int col = Integer.parseInt(parts[2]);

            Plant plant = createPlant(plantType,row,col);
            if (plant != null) {
                plants[row][col] = plant;
            }
        } else if (line.startsWith("Zombie:")) {
            String[] parts = line.split(",");
            String zombieType = parts[0].split(":")[1];
            int row = Integer.parseInt(parts[1]);
            int col = Integer.parseInt(parts[2]);

            Zombie zombie = createZombie(zombieType,row,col);
            if (zombie != null) {
                zombies[row][col] = zombie;
            }
        }
    }

    private Plant createPlant(String plantType, int row, int col) {
        switch (plantType) {
            case "Peashooter":
                return new Peashooter(this,row,col);
            case "Sunflower":
                return new Sunflower(this,row,col);
            case "WallNut":
                return new WallNut(this,row,col);
            case "TallNut":
                return new TallNut(this,row,col);
            case "ECIPlant":
                return new ECIPlant(this,row,col);
            case "PotatoMine":
                return new PotatoMine(this,row,col);
            default:
                return null;
        }
    }

    private Zombie createZombie(String zombieType,int row,int col) {
        switch (zombieType) {
            case "BasicZombie":
                return new BasicZombie(this,row,col);
            case "ConeheadZombie":
                return new ConeheadZombie(this,row,col);
            case "BucketheadZombie":
                return new BucketheadZombie(this,row,col);
            case "ECIZombie":
                return new ECIZombie(this,row,col);
            case "BrainsteinZombie":
                return new BrainsteinZombie(this,row,col);
            default:
                return null;
        }
    }
}
