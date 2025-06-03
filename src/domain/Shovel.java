package domain;

public class Shovel {

    /**
     * Elimina una planta en una posición específica del tablero.
     * @param board El tablero del juego.
     * @param row   La fila donde se encuentra la planta a eliminar.
     * @param col   La columna donde se encuentra la planta a eliminar.
     */
    public void removePlant(Board board, int row, int col) {
        if (!board.isValidPosition(row, col)) {
            logMessage("Invalid position: (" + row + ", " + col + ")");
            return;
        }

        if (board.getPlantAt(row, col) == null) {
            logMessage("No plant at position (" + row + ", " + col + ") to remove.");
            return;
        }

        board.removePlantAt(row, col);
        logMessage("Plant at position (" + row + ", " + col + ") has been removed.");
    }

    /**
     * Registra un mensaje en la consola.
     * @param message El mensaje a registrar.
     */
    private void logMessage(String message) {
        System.out.println(message);
    }
}
