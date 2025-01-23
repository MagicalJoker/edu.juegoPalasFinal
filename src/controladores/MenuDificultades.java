package controladores;


import javax.swing.*;

import servicios.JuegoImplementacion;

import java.awt.*;

/**
 * jmormez | 23-01-2025
 * Clase encargada de mostrar la ventana de selección de dificultades al usuario.
 */
public class MenuDificultades extends JFrame {
    public MenuDificultades() {
        setTitle("Seleccionar Dificultad");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        JButton facil = new JButton("Dificultad Baja");
        JButton media = new JButton("Dificultad Media");
        JButton atras = new JButton("Volver");

        facil.addActionListener(e -> startGame("facil"));
        media.addActionListener(e -> startGame("media"));
        atras.addActionListener(e -> {
            dispose();
            new MenuPrincipal();
        });

        add(new JLabel("Selecciona una dificultad", SwingConstants.CENTER));
        add(facil);
        add(media);
        add(atras);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void startGame(String dificultad) {
        dispose();
        new JuegoImplementacion(dificultad);
    }
}

