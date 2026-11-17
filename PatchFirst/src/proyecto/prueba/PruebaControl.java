package proyecto.prueba;

import javax.swing.JOptionPane;
import proyecto.control.IncidenteControl;
import proyecto.modelo.Incidente;

public class PruebaControl {

    public static void main(String[] args) {
        IncidenteControl control = new IncidenteControl();
        int antes = control.getContador();
        Incidente nuevo = new Incidente(0, "2026-11-16", "10:00:00", "203.0.113.45", 1, 14, 1, 300, 5, 4, "Abierto", 20);
        boolean guardado = control.registrar(nuevo);
        int despues = control.getContador();
        Incidente primero = control.buscar(1);
        JOptionPane.showMessageDialog(null,
                "Incidentes antes: " + antes
                + "\nGuardado: " + guardado
                + "\nIncidentes despues: " + despues
                + "\n\nIncidente #1 -> IP: " + primero.getIpOrigen()
                + ", Criticidad: " + primero.getCriticidad());
    }
}