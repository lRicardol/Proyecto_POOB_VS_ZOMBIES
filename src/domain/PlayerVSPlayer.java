package domain;

import java.io.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PlayerVSPlayer extends Gamemode {
    private Board board;
    private List<Plant> selectedPlants;
    private List<Zombie> selectedZombies;
    private boolean roundFinished;
    private int currentRound;
    private long roundStartTime;
    private long totalGameTime;
    private long strategyPlanningTime;
    private Timer resourceTimer;
    private static final int STRATEGY_TIME_LIMIT = 120000; // 2 minutos en millisegundos
    private static final int RESOURCE_UPDATE_INTERVAL = 10000; // 10 segundos
    private static final int SUN_INCREMENT = 25;
    private static final int BRAIN_INCREMENT = 50;
    private int plantScore;
    private int zombieScore;

    /**
     * Constructor para inicializar el modo de juego Player vs Player.
     * @param sunPoints Puntos iniciales de sol.
     * @param brains    Puntos iniciales de cerebros.
     * @param gameTime  Tiempo total de la partida en milisegundos.
     */
    public PlayerVSPlayer(int sunPoints, int brains, long gameTime) {
        super(sunPoints, brains);
        this.board = new Board(sunPoints, brains);
        this.roundFinished = false;
        this.currentRound = 1;
        this.totalGameTime = gameTime;
        this.strategyPlanningTime = STRATEGY_TIME_LIMIT;
        this.plantScore = 0;
        this.zombieScore = 0;
        initializeResourceTimer();
    }

    /**
     * Inicializa un temporizador para incrementar recursos de forma periódica.
     */
    private void initializeResourceTimer() {
        resourceTimer = new Timer();
        resourceTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                board.addSunPoints(SUN_INCREMENT);
                board.addBrainPoints(BRAIN_INCREMENT);
            }
        }, RESOURCE_UPDATE_INTERVAL, RESOURCE_UPDATE_INTERVAL);
    }

    /**
     * Inicia el modo de juego Player vs Player, gestionando rondas y el bucle principal del juego.
     */
    @Override
    public void startGame() {
        System.out.println("Starting Player vs Player mode...");
        roundStartTime = System.currentTimeMillis();
        long lastUpdate = roundStartTime;
        plantPlayerStrategyBeforeStarting();
        startRound();

        while (!gameOver) {
            long currentTime = System.currentTimeMillis();
            long deltaTime = currentTime - lastUpdate;
            if (currentTime - roundStartTime >= totalGameTime / 2 && currentRound == 1) {
                currentRound = 2;
                roundFinished = true;
                plantPlayerStrategyBeforeStarting();
                roundStartTime = System.currentTimeMillis();
                startRound();
            }
            if (currentTime - roundStartTime >= totalGameTime) {
                determineWinnerByScore();
                gameOver = true;
            }

            updateGameState();
            lastUpdate = currentTime;

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Game interrupted.");
            }
        }

        resourceTimer.cancel();
    }

    /**
     * Inicia una nueva ronda en el modo Player vs Player, limpiando el tablero y reiniciando el estado.
     */
    private void startRound() {
        roundFinished = false;
        board.clearBoard();
    }

    /**
     * Maneja la fase de planificación de estrategias para el jugador de plantas antes de iniciar la ronda.
     */
    public void plantPlayerStrategyBeforeStarting() {
        System.out.println("Plant player strategy planning phase starting...");
        long strategyStartTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - strategyStartTime < strategyPlanningTime) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Strategy planning interrupted.");
            }
        }

        System.out.println("Strategy planning phase ended.");
    }

    /**
     * Agrega una planta en una posición válida del tablero si hay suficientes puntos de sol.
     * @param row   Fila donde se colocará la planta.
     * @param col   Columna donde se colocará la planta.
     * @param plant Planta que se desea agregar.
     * @return Verdadero si la planta fue agregada con éxito.
     */
    @Override
    public boolean addPlant(int row, int col, Plant plant) {
        if (col < 1 || col > 8) {
            return false;
        }

        if (board.getSunPoints() < plant.getCost()) {
            System.out.println("Not enough sun points to place this plant!");
            return false;
        }

        board.addSunPoints(-plant.getCost());
        return board.addPlant(row, col, plant);
    }

    /**
     * Agrega un zombi si hay suficientes puntos de cerebro.
     * @param row    Fila donde se colocará el zombi.
     * @param zombie Zombi que se desea agregar.
     * @return Verdadero si el zombi fue agregado con éxito.
     */
    @Override
    public boolean addZombie(int row, Zombie zombie) {
        if (board.getBrainPoints() < zombie.getCost()) {
            System.out.println("Not enough brain points to place this zombie!");
            return false;
        }
        board.addBrainPoints(-zombie.getCost());
        return board.addZombie(row, zombie);
    }

    /**
     * Actualiza el estado general del juego, incluyendo el movimiento de zombis, acciones de plantas, y evaluación de condiciones de finalización.
     */
    private void updateGameState() {
        board.updatePlants();
        board.moveZombies();
        checkGameOver();
        updateScores();
    }

    /**
     * Verifica si el juego ha terminado debido a condiciones de victoria o derrota.
     */
    private void checkGameOver() {
        if (checkLoseCondition()) {
            System.out.println("Zombies reached the house! Zombies win!");
            gameOver = true;
            return;
        }

        if (board.noZombiesLeft()) {
            System.out.println("All zombies have been eliminated! Plants win!");
            gameOver = true;
            return;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - roundStartTime >= totalGameTime) {
            determineWinnerByScore();
            gameOver = true;
        }
    }

    /**
     * Actualiza las puntuaciones de ambos jugadores (plantas y zombis) basado en los recursos y costos actuales.
     */
    private void updateScores() {
        plantScore = calculatePlantScore();
        zombieScore = calculateZombieScore();
    }

    /**
     * Calcula la puntuación del jugador de plantas basada en los puntos de sol y el costo total de las plantas colocadas.
     * @return Puntuación calculada para el jugador de plantas.
     */
    private int calculatePlantScore() {
        int sunPoints = board.getSunPoints();
        int plantCostSum = 0;
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                Plant plant = board.getPlantAt(row, col);
                if (plant != null) {
                    plantCostSum += plant.getCost();
                }
            }
        }
        return (int) ((sunPoints + plantCostSum) * 1.5);
    }

    /**
     * Calcula la puntuación del jugador de zombis basada en los puntos de cerebro y el costo total de los zombis colocados.
     * @return Puntuación calculada para el jugador de zombis.
     */
    private int calculateZombieScore() {
        int brainPoints = board.getBrainPoints();
        int zombieCostSum = 0;
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                Zombie zombie = board.getZombieAt(row, col);
                if (zombie != null) {
                    zombieCostSum += zombie.getCost();
                }
            }
        }
        return brainPoints + zombieCostSum;
    }

    /**
     * Determina el ganador basándose en las puntuaciones finales de ambos jugadores.
     */
    private void determineWinnerByScore() {
        if (plantScore > zombieScore) {
            System.out.println("Plants win by score! Final score - Plants: " + plantScore + " Zombies: " + zombieScore);
        } else if (zombieScore > plantScore) {
            System.out.println("Zombies win by score! Final score - Plants: " + plantScore + " Zombies: " + zombieScore);
        } else {
            System.out.println("It's a tie! Final score - Plants: " + plantScore + " Zombies: " + zombieScore);
        }
    }

    /**
     * Verifica si las plantas han ganado eliminando todos los zombis del tablero.
     * @return Verdadero si no quedan zombis en el tablero.
     */
    @Override
    public boolean checkWinCondition() {
        return board.noZombiesLeft();
    }

    /**
     * Verifica si los zombis han ganado al alcanzar la primera columna del tablero.
     * @return Verdadero si un zombi alcanza la primera columna.
     */
    @Override
    public boolean checkLoseCondition() {
        for (int row = 0; row < 5; row++) {
            if (board.getZombieAt(row, 0) != null) {
                return true;
            }
        }
        return false;
    }


}