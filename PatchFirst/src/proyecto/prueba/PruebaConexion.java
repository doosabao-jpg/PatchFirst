package proyecto.prueba;

import java.sql.Connection;
import javax.swing.JOptionPane;
import proyecto.control.Conexion;

public class PruebaConexion {

    public static void main(String[] args) {
        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
        if (con != null) {
            JOptionPane.showMessageDialog(null, "Conexion exitosa a PatchFirst");
        } else {
            JOptionPane.showMessageDialog(null, "Error: no se pudo conectar");
        }
    }
}