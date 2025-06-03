package domain;

public abstract class Gamemode {
    protected int sunPoints;
    protected int brains;
    protected boolean gameOver;

    /**
     * Constructor con parámetros iniciales
     * @param sunPoints Puntos de sol iniciales.
     * @param brains    Cerebros iniciales.
     */
    public Gamemode(int sunPoints, int brains) {
        this.sunPoints = sunPoints;
        this.brains = brains;
        this.gameOver = false;
    }

    /**
     * Constructor por defecto
     */
    public Gamemode() {
    }

    /**
     * Inicia el juego.
     */
    public abstract void startGame();

    /**
     * Agrega una planta al tablero.
     * @param row   Fila donde se colocará la planta.
     * @param col   Columna donde se colocará la planta.
     * @param plant Objeto planta que se agregará.
     * @return true si la planta se agregó exitosamente, false en caso contrario.
     */
    public abstract boolean addPlant(int row, int col, Plant plant);

    /**
     * Agrega un zombi al tablero.
     * @param row   Fila donde se colocará el zombi.
     * @param zombie Objeto zombi que se agregará.
     * @return true si el zombi se agregó exitosamente, false en caso contrario.
     */
    public abstract boolean addZombie(int row, Zombie zombie);

    /**
     * Verifica la condición de victoria del juego.
     * @return true si se cumple la condición de victoria, false en caso contrario.
     */
    public abstract boolean checkWinCondition();

    /**
     * Verifica la condición de derrota del juego.
     * @return true si se cumple la condición de derrota, false en caso contrario.
     */
    public abstract boolean checkLoseCondition();

    /**
     * Método llamado cuando un zombi alcanza la casa del jugador.
     * Marca el juego como terminado.
     */
    public void zombieReachedHouse() {
        System.out.println("A zombie has reached the house! Game Over.");
        this.gameOver = true;
    }

    /**
     * Obtiene los puntos de sol actuales.
     * @return Puntos de sol disponibles.
     */
    public int getSunPoints() {
        return sunPoints;
    }

    /**
     * Establece los puntos de sol.
     * @param sunPoints Nueva cantidad de puntos de sol.
     */
    public void setSunPoints(int sunPoints) {
        this.sunPoints = sunPoints;
    }

    /**
     * Obtiene la cantidad actual de cerebros.
     * @return Cantidad de cerebros disponibles.
     */
    public int getBrains() {
        return brains;
    }

    /**
     * Establece la cantidad de cerebros.
     * @param brains Nueva cantidad de cerebros.
     */
    public void setBrains(int brains) {
        this.brains = brains;
    }

    /**
     * Verifica si el juego ha terminado.
     * @return true si el juego ha finalizado, false en caso contrario.
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Crea un zombi basado en el nombre proporcionado.
     * @param randomZombie Nombre del tipo de zombi a crear.
     * @param randomRow Fila donde se colocará el zombi.
     * @param lastCol Última columna del tablero (posición inicial del zombi).
     * @return Objeto Zombie creado o null si no se pudo crear.
     */
    public Zombie createZombie(String randomZombie, int randomRow, int lastCol) {
        return null;
    }
}
