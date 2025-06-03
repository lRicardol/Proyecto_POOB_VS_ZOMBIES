package domain;

public class PlantsIntelligent extends PlantMachine {
    private int productionPlantsPlanted;
    private int attackPlantsPlanted;
    private int defensePlantsPlanted;

    /**
     * Constructor de la clase PlantsIntelligent.
     * @param board el tablero donde las plantas serán colocadas.
     */
    public PlantsIntelligent(Board board) {
        super(board);
        this.productionPlantsPlanted = 0;
        this.attackPlantsPlanted = 0;
        this.defensePlantsPlanted = 0;
    }

    /**
     * Ejecuta la estrategia para colocar plantas en el tablero.
     * Coloca plantas de producción (Sunflowers), ataque (Peashooters) y defensa (WallNuts/TallNuts)
     * según los criterios definidos.
     */
    @Override
    public void executeStrategy() {
        System.out.println("Ejecutando estrategia PlantsIntelligent...");

        // Producción: colocar girasoles
        if (productionPlantsPlanted < 5) {
            int productionPlantRow = board.getFirstRowWithoutPlant(1);
            if (productionPlantRow < 5) {
                boolean added = board.addPlant(productionPlantRow, 1, new Sunflower(board, productionPlantRow, 1));
                if (added) {
                    productionPlantsPlanted++;
                    board.notifyCellUpdated(productionPlantRow, 1);
                    System.out.println("Girasol colocado en (" + productionPlantRow + ", 1)");
                }
            }
        }

        
        if (attackPlantsPlanted < 20) {
            int attackPlantRow_col2 = board.getFirstRowWithoutPlant(2);
            if (attackPlantRow_col2 < 5) {
                boolean added = board.addPlant(attackPlantRow_col2, 2, new Peashooter(board, attackPlantRow_col2, 2));
                if (added) {
                    board.notifyCellUpdated(attackPlantRow_col2, 2);
                    System.out.println("Peashooter colocado en (" + attackPlantRow_col2 + ", 2)");
                }
            }

            int attackPlantRow_col3 = board.getFirstRowWithoutPlant(3);
            if (attackPlantRow_col3 < 5) {
                boolean added = board.addPlant(attackPlantRow_col3, 3, new Peashooter(board, attackPlantRow_col3, 3));
                if (added) {
                    board.notifyCellUpdated(attackPlantRow_col3, 3);
                    System.out.println("Peashooter colocado en (" + attackPlantRow_col3 + ", 3)");
                }
            }

            attackPlantsPlanted++;
        }

        if (defensePlantsPlanted < 10) {
            int defensePlantRow_col4 = board.getFirstRowWithoutPlant(4);
            if (defensePlantRow_col4 < 5) {
                boolean added = board.addPlant(defensePlantRow_col4, 4, new WallNut(board, defensePlantRow_col4, 4));
                if (added) {
                    board.notifyCellUpdated(defensePlantRow_col4, 4);
                    System.out.println("WallNut colocada en (" + defensePlantRow_col4 + ", 4)");
                }
            }

            int defensePlantRow_col5 = board.getFirstRowWithoutPlant(5);
            if (defensePlantRow_col5 < 5) {
                boolean added = board.addPlant(defensePlantRow_col5, 5, new TallNut(board, defensePlantRow_col5, 5));
                if (added) {
                    board.notifyCellUpdated(defensePlantRow_col5, 5);
                    System.out.println("TallNut colocada en (" + defensePlantRow_col5 + ", 5)");
                }
            }

            defensePlantsPlanted++;
        }
    }
}
