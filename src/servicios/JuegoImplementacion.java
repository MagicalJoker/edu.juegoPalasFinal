package servicios;

import dto.Pelota;
import dto.Pala;
import dto.Ladrillo;

// Importaciones existentes
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * NO REFACTORIZADO.
 * jmormez | 23-01-2025
 * Clase que contenedra todo el funcionamiento del juego.
 */
public class JuegoImplementacion extends JPanel implements JuegoInterfaz {
    public int tiempoRestante; // Tiempo restante en segundos
    public Timer temporizador;   // Temporizador para la cuenta regresiva
    public Timer generarLadrillosDificultadMedia;  // Temporizador para generar ladrillos en dificultad media
    public Timer temporizadorGenerarLadrillo; // Temporizador para dificultad baja

    public final int anchoVentana = 800, altoVentana = 600; // Dimensiones de la ventana del juego
    public final int tamanoPelota = 20, anchoPala = 100, altoPala = 10, anchoLadrillo = 60, altoLadrillo = 20; // Dimensiones de los elementos del juego
    public int velocidadPelotaX, velocidadPelotaY, velocidadPala, puntuacion = 0, vidas = 3; // Velocidades, puntuación y vidas
    public Pelota pelota; // Objeto pelota
    public Pala palaInferior, palaSuperior; // Objetos pala
    public List<Ladrillo> ladrillos; // Lista de ladrillos
    public Timer temporizadorJuego; // Temporizador principal del juego
    public boolean juegoEjecutandose = true; // Estado del juego

    /**
     * jmormez | 23-01-2025
     * Constructor de la clase Juego.
     * Inicializa la ventana del juego y configura los elementos iniciales.
     *
     * @param dificultad Dificultad del juego (baja o media).
     */
    public JuegoImplementacion(String dificultad) {
        JFrame frame = new JFrame("Juego Palas - Dificultad: " + dificultad);
        frame.setSize(anchoVentana, altoVentana); // Establece el tamaño de la ventana
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra la aplicación al cerrar la ventana
        frame.add(this); // Añade el panel del juego a la ventana
        frame.setLocationRelativeTo(null); // Centra la ventana en la pantalla

        setFocusable(true); // Permite que el panel reciba eventos de teclado
        setBackground(Color.DARK_GRAY); // Establece el color de fondo del panel

        iniciarJuego(dificultad); // Inicializa el juego según la dificultad
        setupMouseListener(); // Configura el listener del ratón
        startTimer(); // Inicia el temporizador del juego

        // Temporizador para actualizar el juego y repintar la pantalla
        temporizadorJuego = new Timer(1000 / 60, e -> {
            actualizarJuego(); // Actualiza la lógica del juego
            repaint(); // Repinta el panel
        });
        temporizadorJuego.start(); // Inicia el temporizador

        frame.setVisible(true); // Hace visible la ventana
    }

    @Override
    public void iniciarJuego(String dificultadJuego) {
        // Inicializa la pelota en el centro de la pantalla
        pelota = new Pelota(anchoVentana / 2 - tamanoPelota / 2, altoVentana / 2 - tamanoPelota / 2, tamanoPelota, tamanoPelota);
        // Inicializa la pala inferior
        palaInferior = new Pala(anchoVentana / 2 - anchoPala / 2, altoVentana - 40, anchoPala, altoPala);

        // Configura la dificultad del juego
        if (dificultadJuego.equals("media")) {
            // Dificultad media: añade pala superior y establece velocidades
            palaSuperior = new Pala(anchoVentana / 2 - anchoPala / 2, 30, anchoPala, altoPala);
            velocidadPelotaX = 6; // Velocidad de la pelota en el eje X
            velocidadPelotaY = 6; // Velocidad de la pelota en el eje Y
            tiempoRestante = 300; // 5 minutos en segundos
            generarLadrillosNormal(); // Inicia la generación de ladrillos
        } else {
            // Dificultad baja: establece velocidades más lentas
            velocidadPelotaX = 4; // Velocidad de la pelota en el eje X
            velocidadPelotaY = 4; // Velocidad de la pelota en el eje Y
            tiempoRestante = 600; // 10 minutos en segundos
            generarLadrillosFacil(); // Inicia la generación de ladrillos para dificultad fácil
        }

        velocidadPala = 15; // Velocidad de movimiento de la pala
        ladrillos = new ArrayList<>(); // Inicializa la lista de ladrillos
        // Genera ladrillos en la parte superior de la pantalla
        for (int i = 0; i < 20; i++) {
            ladrillos.add(new Ladrillo(i % 10 * anchoLadrillo + 50, i / 10 * altoLadrillo + 100, anchoLadrillo, altoLadrillo));
        }
    }

    @Override
    public void startTimer() {
        // Temporizador que decrementa el tiempo restante cada segundo
        temporizador = new Timer(1000, e -> {
            tiempoRestante--; // Decrementa el tiempo restante
            if (tiempoRestante <= 0) {
                temporizador.stop(); // Detiene el temporizador
                terminarJuego(false); // Termina el juego si el tiempo llega a 0
            }
            repaint(); // Actualiza la pantalla para mostrar el tiempo restante
        });
        temporizador.start(); // Inicia el temporizador
    }

    @Override
    public void generarLadrillosNormal() {
        // Temporizador que genera ladrillos cada 15 segundos
        generarLadrillosDificultadMedia = new Timer(15000, e -> {
            if (ladrillos.size() < 50) { // Limita la cantidad de ladrillos
                for (int i = 0; i < 5; i++) {
                    // Añade nuevos ladrillos a la lista
                    ladrillos.add(new Ladrillo(i % 10 * anchoLadrillo + 50, (ladrillos.size() / 10) * altoLadrillo + 100, anchoLadrillo, altoLadrillo));
                }
            }
        });
        generarLadrillosDificultadMedia.start(); // Inicia el temporizador de ladrillos
    }

    @Override
    public void generarLadrillosFacil() {
        // Temporizador que genera ladrillos cada 2 minutos
        temporizadorGenerarLadrillo = new Timer(120000, e -> { // 2 minutos en milisegundos
            if (ladrillos.size() < 30) { // Limita la cantidad de ladrillos
                for (int i = 0; i < 5; i++) {
                    // Añade nuevos ladrillos a la lista
                    ladrillos.add(new Ladrillo(i % 10 * anchoLadrillo + 50, (ladrillos.size() / 10) * altoLadrillo + 100, anchoLadrillo, altoLadrillo));
                }
            }
        });
        temporizadorGenerarLadrillo.start(); // Inicia el temporizador de generación de ladrillos
    }

    @Override
    public void setupMouseListener() {
        // Añade un MouseMotionListener para manejar los movimientos del ratón
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int mouseX = e.getX(); // Obtiene la posición X del ratón
                // Mueve la pala inferior
                palaInferior.x = mouseX - anchoPala / 2;
                if (palaSuperior != null) {
                    // Mueve la pala superior para reflejar los movimientos del ratón
                    palaSuperior.x = mouseX - anchoPala / 2;
                }
                // Asegura que las palas no salgan de los límites de la ventana
                if (palaInferior.x < 0) palaInferior.x = 0;
                if (palaInferior.x > anchoVentana - anchoPala) palaInferior.x = anchoVentana - anchoPala;
                if (palaSuperior != null) {
                    if (palaSuperior.x < 0) palaSuperior.x = 0;
                    if (palaSuperior.x > anchoVentana - anchoPala) palaSuperior.x = anchoVentana - anchoPala;
                }
            }
        });
    }

    @Override
    public void actualizarJuego() {
        if (!juegoEjecutandose) return; // Si el juego no está en ejecución, no hace nada

        // Actualiza la posición de la pelota
        pelota.x += velocidadPelotaX;
        pelota.y += velocidadPelotaY;

        // Rebote en paredes laterales
        if (pelota.x <= 0 || pelota.x >= anchoVentana - tamanoPelota) {
            velocidadPelotaX *= -1; // Invierte la dirección en el eje X
        }

        // Rebote en la pared superior solo en dificultad baja
        if (pelota.y <= 0) {
            if (palaSuperior == null) { // En dificultad baja, rebota
                velocidadPelotaY *= -1; // Invierte la dirección en el eje Y
            } else { // En dificultad media, se pierde una vida
                vidas--; // Resta una vida
                puntuacion = Math.max(0, puntuacion - 5); // Resta 5 puntos por cada vida perdida
                if (vidas == 0) {
                    terminarJuego(false); // Termina el juego si no quedan vidas
                } else {
                    reiniciarPelota(); // Reinicia la posición de la pelota
                }
            }
        }

        // Rebote en palas
        if (pelota.intersects(palaInferior) || (palaSuperior != null && pelota.intersects(palaSuperior))) {
            velocidadPelotaY *= -1; // Invierte la dirección en el eje Y
        }

        // Pierde una vida si toca la pared inferior (en ambas dificultades)
        if (pelota.y > altoVentana) {
            vidas--; // Resta una vida
            puntuacion = Math.max(0, puntuacion - 5); // Resta 5 puntos por cada vida perdida
            if (vidas == 0) {
                terminarJuego(false); // Termina el juego si no quedan vidas
            } else {
                reiniciarPelota(); // Reinicia la posición de la pelota
            }
        }

        // Colisión con ladrillos
        ladrillos.removeIf(ladrillo -> {
            if (pelota.intersects(ladrillo)) { // Si la pelota colisiona con un ladrillo
                velocidadPelotaY *= -1; // Invierte la dirección en el eje Y
                puntuacion += 10; // Aumenta la puntuación
                comprobarVictoria(); // Verifica si el jugador ha ganado
                return true; // El ladrillo se elimina
            }
            return false; // El ladrillo no se elimina
        });
    }

    @Override
    public void comprobarVictoria() {
        int objetivoDePuntos = (palaSuperior == null) ? 300 : 500; // Baja: 300, Media: 500
        if (puntuacion >= objetivoDePuntos) {
            terminarJuego(true); // Finaliza el juego con victoria
        }
    }

    @Override
    public void reiniciarPelota() {
        pelota.setLocation(anchoVentana / 2 - tamanoPelota / 2, altoVentana / 2 - tamanoPelota / 2); // Coloca la pelota en el centro
    }

    @Override
    public void terminarJuego(boolean haGanado) {
        juegoEjecutandose = false; // Cambia el estado del juego a no en ejecución
        temporizadorJuego.stop(); // Detiene el temporizador principal
        if (temporizador != null) temporizador.stop(); // Detiene el temporizador de juego
        if (generarLadrillosDificultadMedia != null) generarLadrillosDificultadMedia.stop(); // Detiene el temporizador de ladrillos
        if (temporizadorGenerarLadrillo != null) temporizadorGenerarLadrillo.stop(); // Detiene el temporizador de generación de ladrillos

        // Muestra un mensaje de finalización del juego
        String mensajeFinal = haGanado ? "¡Has ganado! Puntuación final: " + puntuacion : "Fin del juego. Puntuación final: " + puntuacion;
        JOptionPane.showMessageDialog(this, mensajeFinal); // Muestra el mensaje en un cuadro de diálogo
        System.exit(0); // Cierra la aplicación
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Llama al método de la superclase para limpiar el panel

        // Dibujo en un búfer para evitar parpadeos
        BufferedImage buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = buffer.createGraphics(); // Crea un objeto Graphics2D para dibujar en el búfer

        dibujarJuego(g2d); // Dibuja el estado del juego en el búfer
        g.drawImage(buffer, 0, 0, null); // Dibuja el búfer en el panel
        g2d.dispose(); // Libera los recursos del objeto Graphics2D
    }

    /**
     * jmormez | 23-01-2025
     * Dibuja todos los elementos del juego en el gráfico proporcionado.
     *
     * @param g Objeto Graphics en el que se dibujan los elementos del juego.
     */
    public void dibujarJuego(Graphics g) {
        g.setColor(Color.BLACK); // Establece el color del texto
        g.setFont(new Font("Arial", Font.BOLD , 16)); // Establece la fuente del texto
        g.drawString("Puntos: " + puntuacion, 20, 50); // Dibuja la puntuación en la pantalla
        g.drawString("Vidas: " + vidas, 20, 70); // Dibuja el número de vidas restantes
        g.drawString("Tiempo: " + formatTime(tiempoRestante), anchoVentana - 150, 50); // Dibuja el tiempo restante

        g.setColor(Color.WHITE); // Establece el color de la pelota
        g.fillOval(pelota.x, pelota.y, pelota.width, pelota.height); // Dibuja la pelota

        g.setColor(Color.BLUE); // Establece el color de la pala inferior
        g.fillRect(palaInferior.x, palaInferior.y, palaInferior.width, palaInferior.height); // Dibuja la pala inferior
        if (palaSuperior != null) { // Si existe la pala superior
            g.fillRect(palaSuperior.x, palaSuperior.y, palaSuperior.width, palaSuperior.height); // Dibuja la pala superior
        }

        g.setColor(Color.GREEN); // Color para el interior del ladrillo
        for (Ladrillo ladrillo : ladrillos) { // Itera sobre cada ladrillo
            g.fillRect(ladrillo.x, ladrillo.y, ladrillo.width, ladrillo.height); // Rellena el ladrillo

            g.setColor(Color.RED); // Color para el borde del ladrillo
            g.drawRect(ladrillo.x, ladrillo.y, ladrillo.width, ladrillo.height); // Dibuja el borde del ladrillo
            g.setColor(Color.GREEN); // Vuelve a poner el color del interior
        }
    }

    @Override
    public String formatTime(int segundos) {
        int minutos = segundos / 60; // Calcula los minutos
        int segundosRestantes = segundos % 60; // Calcula los segundos restantes
        return String.format("%02d:%02d", minutos, segundosRestantes); // Devuelve el tiempo formateado
    }

	@Override
	public void setupKeyListener() {
		// TODO Auto-generated method stub
		
	}
}