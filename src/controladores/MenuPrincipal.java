package controladores;


import javax.swing.*;
import java.awt.*;
/**
 * jmormez | 23-01-2025
 * Menu principal del juego. Se elegirá entre las opciones Jugar o Salir
 */
public class MenuPrincipal extends JFrame {
    public MenuPrincipal() {
    	//titulo de la ventana
        setTitle("Menu Principal |Juego Palas |jmormez");
        //tamaño de la ventana
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));
        
        //botones de acciones. Se llamará a MenuDificultades
        JButton jugar = new JButton("Jugar");
        JButton salir = new JButton("Salir");
        
        //funciones de los botones
        jugar.addActionListener(e -> openMenuDificultades());
        salir.addActionListener(e -> System.exit(0));

        add(new JLabel("Seleccione una opcion", SwingConstants.CENTER));
        add(jugar);
        add(salir);
        
        //posicion de la ventana 
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void openMenuDificultades() {
        dispose();
        new MenuDificultades();
    }
}
