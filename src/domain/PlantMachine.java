package domain;

public abstract class PlantMachine {
    protected Board board;
    protected String[] plants = {"Sunflower", "Peashooter", "WallNut", "TallNut", "ECIPlant", "PotatoMine"};

    /**
     * Constructor de la clase PlantMachine.
     * @param board el tablero donde las plantas serán colocadas.
     */
    public PlantMachine(Board board) {
        this.board = board;
    }

    /**
     * Método abstracto para ejecutar la estrategia de colocación de plantas.
     * Este método debe ser implementado por las subclases con una lógica específica.
     */
    public abstract void executeStrategy();

    /**
     * Crea una instancia de una planta en una posición específica del tablero.
     * @param plantType el tipo de planta a crear (por ejemplo, "Sunflower", "Peashooter").
     * @param row la fila en la que se colocará la planta.
     * @param col la columna en la que se colocará la planta.
     * @return una instancia de la planta creada.
     */
    public Plant createPlant(String plantType, int row, int col) {
        Plant newPlant;
        switch (plantType) {
            case "Sunflower":
                newPlant = new Sunflower(board, row, col);
                break;
            case "Peashooter":
                newPlant = new Peashooter(board, row, col);
                break;
            case "WallNut":
                newPlant = new WallNut(board, row, col);
                break;
            case "TallNut":
                newPlant = new TallNut(board, row, col);
                break;
            case "ECIPlant":
                newPlant = new ECIPlant(board, row, col);
                break;
            case "PotatoMine":
                newPlant = new PotatoMine(board,row,col);
                break;
            default:
                newPlant = new Peashooter(board, row, col);
        }

        return newPlant;
    }
}
