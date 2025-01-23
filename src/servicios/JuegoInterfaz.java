package servicios;

public interface JuegoInterfaz {
    void iniciarJuego(String dificultadJuego);
    void startTimer();
    void generarLadrillosNormal();
    void generarLadrillosFacil();
    void setupKeyListener();
    void actualizarJuego();
    void comprobarVictoria();
    void reiniciarPelota();
    void terminarJuego(boolean haGanado);
    String formatTime(int segundos);
	void setupMouseListener();
}