package proyecto.control;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {

    public Connection conectar() {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/patchfirst?serverTimezone=America/Bogota", "root", "TU_CLAVE");
        } catch (Exception e) {
            con = null;
        }
        return con;
    }
}