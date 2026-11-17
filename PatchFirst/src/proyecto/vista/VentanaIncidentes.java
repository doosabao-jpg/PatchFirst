package proyecto.vista;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import proyecto.control.IncidenteControl;
import proyecto.modelo.Incidente;

public class VentanaIncidentes extends JFrame implements ActionListener {
    private IncidenteControl control = new IncidenteControl();
    private JTextField txtId = new JTextField();
    private JTextField txtFecha = new JTextField();
    private JTextField txtHora = new JTextField();
    private JTextField txtIp = new JTextField();
    private JTextField txtDispositivo = new JTextField();
    private JTextField txtAtaque = new JTextField();
    private JTextField txtTecnico = new JTextField();
    private JTextField txtValor = new JTextField();
    private JTextField txtProbabilidad = new JTextField();
    private JTextField txtImpacto = new JTextField();
    private JTextField txtTiempo = new JTextField();
    private String[] estados = {"Abierto", "En proceso", "Cerrado"};
    private JComboBox<String> cbEstado = new JComboBox<String>(estados);
    private JButton btnAgregar = new JButton("Agregar");
    private JButton btnBuscar = new JButton("Buscar");
    private JButton btnEditar = new JButton("Editar");
    private JButton btnEliminar = new JButton("Eliminar");
    private JButton btnLimpiar = new JButton("Limpiar");
    private JLabel lblTotal = new JLabel();
    private DefaultTableModel modelo = new DefaultTableModel();
    private JTable tabla = new JTable(modelo);

    public VentanaIncidentes() {
        setTitle("PatchFirst: Sistema de Priorizacion de Incidentes de Ciberseguridad");
        setSize(1250, 720);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ponerCampo("ID incidente:", txtId, 20, 20);
        ponerCampo("Fecha (AAAA-MM-DD):", txtFecha, 20, 55);
        ponerCampo("Hora (HH:MM:SS):", txtHora, 20, 90);
        ponerCampo("IP origen:", txtIp, 20, 125);
        ponerCampo("ID dispositivo (1-16):", txtDispositivo, 420, 20);
        ponerCampo("ID tipo ataque (1-27):", txtAtaque, 420, 55);
        ponerCampo("ID tecnico (1-5):", txtTecnico, 420, 90);
        ponerCampo("Valor indicador:", txtValor, 420, 125);
        ponerCampo("Probabilidad (1-5):", txtProbabilidad, 820, 20);
        ponerCampo("Impacto (1-5):", txtImpacto, 820, 55);
        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setBounds(820, 90, 160, 25);
        add(lblEstado);
        
        cbEstado.setBounds(990, 90, 200, 25);
        add(cbEstado);
        
        ponerCampo("Tiempo respuesta (min):", txtTiempo, 820, 125);
        ponerBoton(btnAgregar, 20);
        ponerBoton(btnBuscar, 140);
        ponerBoton(btnEditar, 260);
        ponerBoton(btnEliminar, 380);
        ponerBoton(btnLimpiar, 500);
        
        lblTotal.setBounds(650, 175, 300, 30);
        add(lblTotal);

        modelo.addColumn("ID");
        modelo.addColumn("Fecha");
        modelo.addColumn("Hora");
        modelo.addColumn("IP origen");
        modelo.addColumn("Dispositivo");
        modelo.addColumn("Ataque");
        modelo.addColumn("Tecnico");
        modelo.addColumn("Valor");
        modelo.addColumn("Prob.");
        modelo.addColumn("Impacto");
        modelo.addColumn("Nivel riesgo");
        modelo.addColumn("Criticidad");
        modelo.addColumn("Estado");
        modelo.addColumn("Tiempo (min)");

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(20, 225, 1195, 440);
        add(scroll);

        cargarTabla();
    }

    private void ponerCampo(String texto, JTextField campo, int x, int y) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setBounds(x, y, 160, 25);
        add(etiqueta);
        campo.setBounds(x + 170, y, 200, 25);
        add(campo);
    }

    private void ponerBoton(JButton boton, int x) {
        boton.setBounds(x, 175, 110, 30);
        boton.addActionListener(this);
        add(boton);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAgregar) {
            agregar();
        } else if (e.getSource() == btnBuscar) {
            buscar();
        } else if (e.getSource() == btnEditar) {
            editar();
        } else if (e.getSource() == btnEliminar) {
            eliminar();
        } else if (e.getSource() == btnLimpiar) {
            limpiar();
        }
    }

    private Incidente leerFormulario() {
        Incidente inc = new Incidente(0,
                txtFecha.getText(),
                txtHora.getText(),
                txtIp.getText(),
                Integer.parseInt(txtDispositivo.getText()),
                Integer.parseInt(txtAtaque.getText()),
                Integer.parseInt(txtTecnico.getText()),
                Integer.parseInt(txtValor.getText()),
                Integer.parseInt(txtProbabilidad.getText()),
                Integer.parseInt(txtImpacto.getText()),
                (String) cbEstado.getSelectedItem(),
                Integer.parseInt(txtTiempo.getText()));
        return inc;
    }

    private void agregar() {
        try {
            Incidente inc = leerFormulario();
            if (control.registrar(inc)) {
                JOptionPane.showMessageDialog(this, "Incidente registrado. Nivel de riesgo: " + inc.getNivelRiesgo() + " (" + inc.getCriticidad() + ")");
                limpiar();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar. Revise la fecha, los rangos y los ID.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Llene todos los campos. Los numericos deben tener numeros.");
        }
    }

    private void buscar() {
        try {
            int id = Integer.parseInt(txtId.getText());
            Incidente inc = control.buscar(id);
            if (inc != null) {
                txtFecha.setText(inc.getFecha());
                txtHora.setText(inc.getHora());
                txtIp.setText(inc.getIpOrigen());
                txtDispositivo.setText("" + inc.getIdDispositivo());
                txtAtaque.setText("" + inc.getIdTipoAtaque());
                txtTecnico.setText("" + inc.getIdTecnico());
                txtValor.setText("" + inc.getValorIndicador());
                txtProbabilidad.setText("" + inc.getProbabilidad());
                txtImpacto.setText("" + inc.getImpacto());
                cbEstado.setSelectedItem(inc.getEstado());
                txtTiempo.setText("" + inc.getTiempoRespuestaMin());
            } else {
                JOptionPane.showMessageDialog(this, "No existe un incidente con ID " + id);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Escriba un ID valido (solo numeros).");
        }
    }

    private void editar() {
        try {
            Incidente inc = leerFormulario();
            inc.setIdIncidente(Integer.parseInt(txtId.getText()));
            if (control.actualizar(inc)) {
                JOptionPane.showMessageDialog(this, "Incidente actualizado.");
                limpiar();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar. Revise el ID y los datos.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Primero busque el incidente y llene todos los campos.");
        }
    }

    private void eliminar() {
        try {
            int id = Integer.parseInt(txtId.getText());
            int respuesta = JOptionPane.showConfirmDialog(this, "Seguro que desea eliminar el incidente " + id + "?");
            if (respuesta == JOptionPane.YES_OPTION) {
                if (control.eliminar(id)) {
                    JOptionPane.showMessageDialog(this, "Incidente eliminado.");
                    limpiar();
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "No existe un incidente con ID " + id);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Escriba un ID valido (solo numeros).");
        }
    }

    private void limpiar() {
        txtId.setText("");
        txtFecha.setText("");
        txtHora.setText("");
        txtIp.setText("");
        txtDispositivo.setText("");
        txtAtaque.setText("");
        txtTecnico.setText("");
        txtValor.setText("");
        txtProbabilidad.setText("");
        txtImpacto.setText("");
        cbEstado.setSelectedIndex(0);
        txtTiempo.setText("");
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        ArrayList<Incidente> lista = control.listar();
        for (int i = 0; i < lista.size(); i++) {
            Incidente inc = lista.get(i);
            Object[] fila = {inc.getIdIncidente(), inc.getFecha(), inc.getHora(), inc.getIpOrigen(),
                inc.getIdDispositivo(), inc.getIdTipoAtaque(), inc.getIdTecnico(), inc.getValorIndicador(),
                inc.getProbabilidad(), inc.getImpacto(), inc.getNivelRiesgo(), inc.getCriticidad(),
                inc.getEstado(), inc.getTiempoRespuestaMin()};
            modelo.addRow(fila);
        }
        lblTotal.setText("Total de incidentes: " + control.getContador());
    }
}