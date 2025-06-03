package domain;

public class PlantsStrategic extends PlantMachine {
    private int productionPlantsPlanted;
    private int attackPlantsPlanted;
    private int defensePlantsPlanted;

    /**
     * Constructor de la clase PlantsStrategic.
     * @param board el tablero donde las plantas serán colocadas.
     */
    public PlantsStrategic(Board board) {
        super(board);
        this.productionPlantsPlanted = 0;
        this.attackPlantsPlanted = 0;
        this.defensePlantsPlanted = 0;
    }

    /**
     * Ejecuta la estrategia para colocar plantas en el tablero.
     * Coloca plantas de producción, ataque y defensa de manera estratégica para maximizar su efectividad.
     */
    @Override
    public void executeStrategy() {
        System.out.println("Ejecutando estrategia PlantsStrategic...");
        if (productionPlantsPlanted < 5) {
            int productionPlantRow = board.getRowWithLessPlants();
            if (productionPlantRow < 5) {
                boolean added = board.addPlant(productionPlantRow, 0, new Sunflower(board, productionPlantRow, 0));
                if (added) {
                    productionPlantsPlanted++;
                    board.notifyCellUpdated(productionPlantRow, 1);
                    System.out.println("Sunflower colocado en (" + productionPlantRow + ", 0)");
                }
            }
        }

        if (attackPlantsPlanted < 15) {
            int rowWithMostZombies = board.getRowWithMostZombies();
            int colForAttack = findOptimalAttackColumn(rowWithMostZombies);

            if (colForAttack >= 0) {
                boolean added = board.addPlant(rowWithMostZombies, colForAttack, new Peashooter(board, rowWithMostZombies, colForAttack));
                if (added) {
                    attackPlantsPlanted++;
                    board.notifyCellUpdated(rowWithMostZombies, colForAttack);
                    System.out.println("Peashooter colocado en (" + rowWithMostZombies + ", " + colForAttack + ")");
                }
            }
        }
        if (defensePlantsPlanted < 10) {
            int rowToDefend = board.getRowWithMostZombies();
            int colForDefense = findOptimalDefenseColumn(rowToDefend);
            if (colForDefense >= 0) {
                boolean added;
                if (defensePlantsPlanted % 2 == 0) {
                    added = board.addPlant(rowToDefend, colForDefense, new WallNut(board, rowToDefend, colForDefense));
                } else {
                    added = board.addPlant(rowToDefend, colForDefense, new TallNut(board, rowToDefend, colForDefense));
                }

                if (added) {
                    defensePlantsPlanted++;
                    board.notifyCellUpdated(rowToDefend, colForDefense);
                    System.out.println("Planta defensiva colocada en (" + rowToDefend + ", " + colForDefense + ")");
                }
            }
        }
    }

    /**
     * Encuentra la columna óptima para colocar una planta de ataque en una fila específica.
     * @param row la fila donde se busca colocar la planta de ataque.
     * @return la columna óptima para el ataque, o -1 si no hay espacio disponible.
     */
    private int findOptimalAttackColumn(int row) {
        for (int col = 1; col < board.getColumns(); col++) {
            if (board.isCellEmpty(row, col)) {
                return col;
            }
        }
        return -1;
    }

    /**
     * Encuentra la columna óptima para colocar una planta de defensa en una fila específica.
     * @param row la fila donde se busca colocar la planta de defensa.
     * @return la columna óptima para la defensa, o -1 si no hay espacio disponible.
     */
    private int findOptimalDefenseColumn(int row) {
        for (int col = board.getColumns() - 1; col >= 1; col--) {
            if (board.isCellEmpty(row, col)) {
                return col;
            }
        }
        return -1;
    }
}
