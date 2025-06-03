package domain;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;

public class MachineVSMachine extends Gamemode{
    private PlantMachine plantMachine;
    private ZombieMachine zombieMachine;
    private static Board board;
    private static final long UPDATE_INTERVAL = 5000; // 5 segundos
    private List<Plant> selectedPlants;
    private List<Zombie> selectedZombies;
    private Timer gameLoopTimer;
    private long accumulatedTime;

    /**
     * Constructor para inicializar el modo Machine vs Machine.
     * Este constructor configura las máquinas de plantas y zombis, así como los puntos iniciales de sol y cerebros.
     * También inicializa el tablero del juego.
     * @param plantMachine Máquina encargada de la lógica para las plantas.
     * @param zombieMachine Máquina encargada de la lógica para los zombis.
     * @param initialSunPoints Cantidad inicial de puntos de sol disponibles.
     * @param initialBrains Cantidad inicial de cerebros disponibles.
     */
    public MachineVSMachine(PlantMachine plantMachine, ZombieMachine zombieMachine, int initialSunPoints, int initialBrains) {
        super(initialSunPoints, initialBrains);
        this.plantMachine = plantMachine;
        this.zombieMachine = zombieMachine;
        board = new Board(initialSunPoints, initialBrains);
        accumulatedTime = 0;
    }

    /**
     * Constructor alternativo para inicializar el modo Machine vs Machine con un tablero existente.
     * Este constructor parece ser un placeholder y no realiza acciones significativas.
     * @param board Tablero de juego ya inicializado.
     * @param initialSunPoints Cantidad inicial de puntos de sol disponibles.
     * @param initialBrains Cantidad inicial de cerebros disponibles.
     */
    public MachineVSMachine(Board board, int initialSunPoints, int initialBrains) {
        super();
    }

    /**
     * Establece las plantas seleccionadas para la máquina de plantas.
     * @param plants Lista de plantas seleccionadas.
     */
    public void setSelectedPlants(List<Plant> plants) {
        this.selectedPlants = plants;
    }

    /**
     * Establece los zombis seleccionados para la máquina de zombis.
     * @param zombies Lista de zombis seleccionados.
     */
    public void setSelectedZombies(List<Zombie> zombies) {
        this.selectedZombies = zombies;
    }

    /**
     * Inicia la partida en el modo Machine vs Machine.
     * - Configura un temporizador que actualiza los turnos del juego cada 100 ms.
     * - En cada iteración:
     * - Se ejecutan los turnos de las máquinas de plantas y zombis.
     * - Se mueven los zombis en el tablero.
     * - El temporizador se detiene automáticamente cuando el juego termina.
     */
    @Override
    public void startGame() {
        System.out.println("Iniciando partida Machine vs Machine...");
        gameLoopTimer = new Timer(100, new ActionListener() {
            private long lastUpdate = System.currentTimeMillis();

            @Override
            public void actionPerformed(ActionEvent e) {
                long currentTime = System.currentTimeMillis();
                long deltaTime = currentTime - lastUpdate;
                lastUpdate = currentTime;

                accumulatedTime += deltaTime;

                if (accumulatedTime >= UPDATE_INTERVAL) {
                    System.out.println("Actualizando turno...");
                    plantTurn();
                    zombieTurn();
                    board.moveZombies();
                    accumulatedTime = 0;
                }
                if (gameOver) {
                    System.out.println("Juego terminado.");
                    gameLoopTimer.stop();
                }
            }
        });
        gameLoopTimer.start();
    }

    /**
     * Agrega una planta al tablero en una posición específica.
     * @param row Fila donde se colocará la planta.
     * @param col Columna donde se colocará la planta.
     * @param plant Planta que se desea agregar.
     * @return true si la planta fue colocada exitosamente, false en caso contrario.
     */
    @Override
    public boolean addPlant(int row, int col, Plant plant) {
        return board.addPlant(row, col, plant);
    }

    /**
     * Añade un zombi a la fila especificada en el tablero.
     * @param row Fila donde se colocará el zombi.
     * @param zombie Instancia del zombi a agregar.
     * @return true si el zombi fue añadido exitosamente, false en caso contrario.
     */
    @Override
    public boolean addZombie(int row, Zombie zombie) {
        boolean added = board.addZombie(row, 9, zombie);
        if (added) {
            System.out.println("Zombie añadido a la fila " + row);
        } else {
            System.out.println("No se pudo añadir el zombie a la fila " + row);
        }
        return added;
    }

    /**
     * Verifica si se cumple la condición de victoria.
     * La condición de victoria se alcanza cuando no quedan zombis en el tablero.
     * @return true si no quedan zombis en el tablero, false en caso contrario.
     */
    @Override
    public boolean checkWinCondition() {
        return board.noZombiesLeft();
    }

    /**
     * Verifica si se cumple la condición de derrota.
     * La condición de derrota se alcanza cuando un zombi llega a la primera columna en cualquier fila.
     * @return true si un zombi alcanza la primera columna, false en caso contrario.
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
     * Ejecuta el turno de las plantas utilizando la estrategia definida en la máquina de plantas.
     * - La máquina de plantas debe estar correctamente inicializada.
     * - Invoca la lógica de estrategia definida para las plantas.
     */
    public void plantTurn() {
        if (plantMachine == null) {
            System.err.println("plantMachine es null. Asegúrate de inicializarlo correctamente.");
            return;
        }
        System.out.println("Turno de las plantas...");
        plantMachine.executeStrategy();
    }

    /**
     * Ejecuta el turno de los zombis utilizando la estrategia definida en la máquina de zombis.
     * - La máquina de zombis debe estar correctamente inicializada.
     * - Invoca la lógica de estrategia definida para los zombis.
     */
    public void zombieTurn() {
        if (zombieMachine == null) {
            System.err.println("zombieMachine es null. Asegúrate de inicializarlo correctamente.");
            return;
        }
        System.out.println("Turno de los zombies...");
        zombieMachine.executeStrategy();
    }

    /**
     * Guarda el estado actual del juego en un archivo.
     * El estado del juego incluye los puntos de sol, cerebros y el estado del tablero.
     * @param file Archivo en el que se guardará el estado del juego.
     * @throws IOException Si ocurre un error al escribir en el archivo.
     */
    public void saveGame(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            board.saveState(writer);
        }
    }

    /**
     * Carga un estado de juego desde un archivo.
     * El archivo debe contener información sobre puntos de sol, cerebros y el estado del tablero.
     * @param file Archivo desde el cual se cargará el estado del juego.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    public void openGame(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("SunPoints:")) {
                    sunPoints = Integer.parseInt(line.split(":")[1]);
                } else if (line.startsWith("Brains:")) {
                    brains = Integer.parseInt(line.split(":")[1]);
                } else {
                    board.loadState(line);
                }
            }
        }
    }
}
