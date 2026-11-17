package proyecto.prueba;

import javax.swing.JOptionPane;
import proyecto.modelo.Incidente;

public class PruebaIncidente {

    public static void main(String[] args) {
        Incidente inc = new Incidente(0, "2026-09-15", "08:30:00", "203.0.113.45", 1, 14, 1, 420, 5, 4, "Abierto", 95);
        JOptionPane.showMessageDialog(null,
                "Probabilidad: " + inc.getProbabilidad()
                + "\nImpacto: " + inc.getImpacto()
                + "\nNivel de riesgo: " + inc.getNivelRiesgo()
                + "\nCriticidad: " + inc.getCriticidad());
    }
}