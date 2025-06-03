package domain;

public class TallNut extends Plant{

    /**
     * Constructor para inicializar un TallNut.
     * @param board El tablero del juego.
     * @param row   La fila donde se coloca el TallNut.
     * @param col   La columna donde se coloca el TallNut.
     */
    public TallNut(Board board,int row,int col) {
        super(6000, 50, 0, 0.0,board,0,0);
    }

    /**
     * Realiza la acción del TallNut.
     * En este caso, no tiene acción activa inmediata.
     */
    @Override
    public void performAction() {
        System.out.println("TallNut is standing strong!");
    }

    /**
     * Indica si el TallNut es un TallNut.
     * @return true, ya que esta clase representa un TallNut.
     */
    @Override
    public boolean isTallNut(){
        return true;
    }

    /**
     * Obtiene el nombre de la imagen estática del TallNut.
     * @return La ruta de la imagen estática.
     */
    @Override
    public String getImageName() {
        return "src/resources/TallNut.png";
    }

    /**
     * Obtiene el nombre del GIF animado del TallNut.
     * @return La ruta del GIF animado.
     */
    @Override
    public String getGifName() {
        return "src/resources/TallNutA.gif";
    }
}

