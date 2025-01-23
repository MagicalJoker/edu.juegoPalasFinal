package controladores;

import javax.swing.*;

import menu.MenuPrincipal;
/**
 * @author jmormez
 * 23-01-2025
 * Puerta de entrada a mi aplicación
 */
public class Inicio {
	
	/**
	 * jmormez | 23-01-2025
	 * Clase controladora de mi aplicación
	 * @param args
	 */
    public static void main(String[] args) {
    	
    	//llamada a MenuPrincipal
        SwingUtilities.invokeLater(() -> new MenuPrincipal());
    }
}

