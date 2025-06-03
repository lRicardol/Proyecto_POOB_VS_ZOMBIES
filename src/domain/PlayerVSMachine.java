package domain;

import java.io.*;
import java.util.List;
import java.util.Random;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerVSMachine extends Gamemode {
    private static Board board;
    private List<Plant> selectedPlants;
    private static final long UPDATE_INTERVAL = 20000;
    private Timer gameLoopTimer;
    private long accumulatedTime;
    private static boolean gamePaused = false;

    /**
     * Constructor para inicializar el modo de juego Player vs Machine.
     * @param board     El tablero del juego.
     * @param sunPoints Puntos iniciales de sol.
     * @param brains    Puntos iniciales de cerebros.
     */
    public PlayerVSMachine(Board board, int sunPoints, int brains) {
        super(sunPoints, brains);
        this.board = board;
        this.accumulatedTime = 0;
    }

    /**
     * Establece la lista de plantas seleccionadas por el jugador.
     * @param plants Lista de plantas seleccionadas.
     */
    public void setSelectedPlants(List<Plant> plants) {
        this.selectedPlants = plants;
    }

    /**
     * Inicia el modo de juego Player vs Machine, configurando un bucle principal para actualizar el estado del juego.
     */
    @Override
    public void startGame() {
        System.out.println("Starting Player vs Machine mode...");

        gameLoopTimer = new Timer(100, new ActionListener() {
            private long lastUpdate = System.currentTimeMillis();

            @Override
            public void actionPerformed(ActionEvent e) {
                long currentTime = System.currentTimeMillis();
                long deltaTime = currentTime - lastUpdate;
                lastUpdate = currentTime;
                accumulatedTime += deltaTime;
                if (accumulatedTime >= UPDATE_INTERVAL && !gamePaused) {
                    updateGameState();
                    accumulatedTime = 0;
                }
                if (gameOver) {
                    gameLoopTimer.stop();
                    System.out.println("Game Over");
                }
            }
        });
        gameLoopTimer.start();
    }

    /**
     * Pausa el modo de juego, deteniendo el bucle principal.
     */
    public void pauseGame(){
        gamePaused = true;
    }

    /**
     * Reanuda el modo de juego, permitiendo que el bucle principal continúe.
     */
    public void resumeGame(){
        gamePaused = false;
    }

    /**
     * Actualiza el estado general del juego, incluyendo recursos y generación automática de zombis.
     */
    private void updateGameState() {
        updateResources();
        autoGenerateZombies();
        board.updatePlants();
    }

    /**
     * Agrega una planta en una posición específica del tablero.
     * @param row   Fila donde se colocará la planta.
     * @param col   Columna donde se colocará la planta.
     * @param plant Planta que se desea agregar.
     * @return Verdadero si la planta fue agregada con éxito.
     */
    @Override
    public boolean addPlant(int row, int col, Plant plant) {
        return board.addPlant(row, col, plant);
    }

    /**
     * Agrega un zombi en una fila específica y la última columna del tablero.
     * @param row    Fila donde se colocará el zombi.
     * @param zombie Zombi que se desea agregar.
     * @return Verdadero si el zombi fue agregado con éxito.
     */
    @Override
    public boolean addZombie(int row, Zombie zombie) {
        int col = 9;
        return board.addZombie(row, col, zombie);
    }

    /**
     * Verifica si la condición de victoria se cumple, es decir, si no quedan zombis en el tablero.
     * @return Verdadero si no hay zombis restantes en el tablero.
     */
    @Override
    public boolean checkWinCondition() {
        return board.noZombiesLeft();
    }

    /**
     * Verifica si la condición de derrota se cumple, es decir, si un zombi llega a la primera columna.
     * @return Verdadero si un zombi alcanza la primera columna.
     */
    @Override
    public boolean checkLoseCondition() {
        for (int row = 0; row < 5; row++) {
            Zombie zombie = board.getZombieAt(row, 0);
            if (zombie != null) {
                System.out.println("Zombie reached the first column in row " + row);
                return true;
            }
        }
        return false;
    }

    /**
     * Actualiza los recursos del jugador (soles y cerebros) en cada intervalo.
     */
    private void updateResources() {
        sunPoints += 25;
        brains += 50;
    }

    /**
     * Genera zombis automáticamente en posiciones aleatorias de la última columna del tablero.
     */
    public void autoGenerateZombies() {
        Random random = new Random();
        for (int row = 0; row < 5; row++) {
            if (random.nextBoolean()) {
                int col = 9;
                Zombie zombie = new BasicZombie(board, row, col);
                board.addZombie(row, col, zombie);
            }
        }
    }

    /**
     * Crea un zombi basado en un tipo especificado y lo coloca en una posición del tablero.
     * @param zombieType Tipo de zombi que se desea crear.
     * @param row        Fila donde se colocará el zombi.
     * @param col        Columna donde se colocará el zombi.
     * @return La instancia del zombi creado.
     */
    public Zombie createZombie(String zombieType, int row, int col) {
        Zombie newZombie;
        switch (zombieType) {
            case "BasicZombie":
                newZombie = new BasicZombie(board, row, col);
                break;
            case "BrainsteinZombie":
                newZombie = new BrainsteinZombie(board, row, col);
                break;
            case "BucketheadZombie":
                newZombie = new BucketheadZombie(board, row, col);
                break;
            case "ConeheadZombie":
                newZombie = new ConeheadZombie(board, row, col);
                break;
            case "ECIZombie":
                newZombie = new ECIZombie(board, row, col);
                break;
            default:
                newZombie = new BasicZombie(board, row, col);
        }

        return newZombie;
    }

    /**
     * Guarda el estado actual del juego en un archivo.
     * @param file Archivo donde se guardará el estado.
     * @throws IOException Si ocurre un error al escribir en el archivo.
     */
    public void saveGame(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            board.saveState(writer);
        }
    }

    /**
     * Carga el estado del juego desde un archivo.
     * @param file Archivo desde donde se cargará el estado.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    public void openGame(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                board.loadState(line);
            }
        }
        System.out.println("Partida cargada con éxito.");
    }

    /**
     * Obtiene los puntos actuales de cerebros en el juego.
     * @return Una cadena con los puntos de cerebros.
     */
    public String getBrainsPoints() {
        return "";
    }
}
