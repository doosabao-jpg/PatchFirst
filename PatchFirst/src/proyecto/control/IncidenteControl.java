
package proyecto.control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import proyecto.modelo.Incidente;

public class IncidenteControl {

    private Conexion conexion = new Conexion();
    public boolean registrar(Incidente inc) {
        boolean resultado = false;
        try {
            Connection con = conexion.conectar();
            String sql = "INSERT INTO incidente (fecha, hora, ip_origen, id_dispositivo, id_tipo_ataque, " + "id_tecnico, valor_indicador, probabilidad, impacto, nivel_riesgo, criticidad, " + "estado, tiempo_respuesta_min) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, inc.getFecha());
            ps.setString(2, inc.getHora());
            ps.setString(3, inc.getIpOrigen());
            ps.setInt(4, inc.getIdDispositivo());
            ps.setInt(5, inc.getIdTipoAtaque());
            ps.setInt(6, inc.getIdTecnico());
            ps.setInt(7, inc.getValorIndicador());
            ps.setInt(8, inc.getProbabilidad());
            ps.setInt(9, inc.getImpacto());
            ps.setInt(10, inc.getNivelRiesgo());
            ps.setString(11, inc.getCriticidad());
            ps.setString(12, inc.getEstado());
            ps.setInt(13, inc.getTiempoRespuestaMin());
            ps.executeUpdate();
            con.close();
            resultado = true;
        } catch (Exception e) {
            resultado = false;
        }
        return resultado;
    }

    public Incidente buscar(int idIncidente) {
        Incidente inc = null;
        try {
            Connection con = conexion.conectar();
            String sql = "SELECT * FROM incidente WHERE id_incidente = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idIncidente);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                inc = new Incidente(rs.getInt("id_incidente"), rs.getString("fecha"),
                        rs.getString("hora"), rs.getString("ip_origen"),
                        rs.getInt("id_dispositivo"), rs.getInt("id_tipo_ataque"),
                        rs.getInt("id_tecnico"), rs.getInt("valor_indicador"),
                        rs.getInt("probabilidad"), rs.getInt("impacto"),
                        rs.getString("estado"), rs.getInt("tiempo_respuesta_min"));
            }
            con.close();
        } catch (Exception e) {
            inc = null;
        }
        return inc;
    }

    public ArrayList<Incidente> listar() {
        ArrayList<Incidente> lista = new ArrayList<Incidente>();
        try {
            Connection con = conexion.conectar();
            String sql = "SELECT * FROM incidente ORDER BY id_incidente DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Incidente inc = new Incidente(rs.getInt("id_incidente"), rs.getString("fecha"),
                        rs.getString("hora"), rs.getString("ip_origen"),
                        rs.getInt("id_dispositivo"), rs.getInt("id_tipo_ataque"),
                        rs.getInt("id_tecnico"), rs.getInt("valor_indicador"),
                        rs.getInt("probabilidad"), rs.getInt("impacto"),
                        rs.getString("estado"), rs.getInt("tiempo_respuesta_min"));
                lista.add(inc);
            }
            con.close();
        } catch (Exception e) {
            lista = new ArrayList<Incidente>();
        }
        return lista;
    }

    public boolean actualizar(Incidente inc) {
        boolean resultado = false;
        try {
            Connection con = conexion.conectar();
            String sql = "UPDATE incidente SET fecha = ?, hora = ?, ip_origen = ?, id_dispositivo = ?, " + "id_tipo_ataque = ?, id_tecnico = ?, valor_indicador = ?, probabilidad = ?, " + "impacto = ?, nivel_riesgo = ?, criticidad = ?, estado = ?, " + "tiempo_respuesta_min = ? WHERE id_incidente = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, inc.getFecha());
            ps.setString(2, inc.getHora());
            ps.setString(3, inc.getIpOrigen());
            ps.setInt(4, inc.getIdDispositivo());
            ps.setInt(5, inc.getIdTipoAtaque());
            ps.setInt(6, inc.getIdTecnico());
            ps.setInt(7, inc.getValorIndicador());
            ps.setInt(8, inc.getProbabilidad());
            ps.setInt(9, inc.getImpacto());
            ps.setInt(10, inc.getNivelRiesgo());
            ps.setString(11, inc.getCriticidad());
            ps.setString(12, inc.getEstado());
            ps.setInt(13, inc.getTiempoRespuestaMin());
            ps.setInt(14, inc.getIdIncidente());
            int filas = ps.executeUpdate();
            con.close();
            if (filas > 0) {
                resultado = true;
            }
        } catch (Exception e) {
            resultado = false;
        }
        return resultado;
    }

    public boolean eliminar(int idIncidente) {
        boolean resultado = false;
        try {
            Connection con = conexion.conectar();
            String sql = "DELETE FROM incidente WHERE id_incidente = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idIncidente);
            int filas = ps.executeUpdate();
            con.close();
            if (filas > 0) {
                resultado = true;
            }
        } catch (Exception e) {
            resultado = false;
        }
        return resultado;
    }

    public int getContador() {
        int total = 0;
        try {
            Connection con = conexion.conectar();
            String sql = "SELECT COUNT(*) FROM incidente";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
            con.close();
        } catch (Exception e) {
            total = 0;
        }
        return total;
    }
}