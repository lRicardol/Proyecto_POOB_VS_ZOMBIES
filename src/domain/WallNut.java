package domain;

public class WallNut extends Plant {

    /**
     * Constructor para inicializar un WallNut.
     * @param board El tablero del juego.
     * @param row   La fila donde se coloca el WallNut.
     * @param col   La columna donde se coloca el WallNut.
     */
    public WallNut(Board board,int row,int col) {
        super(3000, 50, 0, 0.0,board,0,0);
    }

    /**
     * Realiza la acción del WallNut.
     * En este caso, no tiene acción activa inmediata.
     */
    @Override
    public void performAction() {
        System.out.println("WallNut is standing strong!");
    }

    /**
     * Indica si el WallNut es un TallNut.
     * @return false, ya que esta clase representa un WallNut.
     */
    @Override
    public boolean isTallNut(){
        return false;
    }

    /**
     * Obtiene el nombre de la imagen estática del WallNut.
     * @return La ruta de la imagen estática.
     */
    @Override
    public String getImageName() {
        return "src/resources/WallNut.png";
    }

    /**
     * Obtiene el nombre del GIF animado del WallNut.
     * @return La ruta del GIF animado.
     */
    @Override
    public String getGifName() {
        return "src/resources/WallNutA.gif";
    }
}
