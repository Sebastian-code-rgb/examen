package ec.edu.taller;
import javax.swing.*;
import java.util.List;
public class Ventana extends JFrame {
    private JPanel panel1;
    private JTextField txtCodigo;
    private JComboBox<String> cmbTipo;
    private JTextArea txtDescripcion;
    private JSpinner spnPrioridad;
    private JLabel lblPendientes;
    private JLabel lblDisponibles;
    private JLabel lblCapacidad;
    private JLabel lblCodigoActual;
    private JLabel lblTipoActual;
    private JLabel lblPrioridadActual;
    private JLabel lblEstadoActual;
    private JTextArea txtDescripcionActual;
    private JTextArea txtLista;
    private JButton btnRegistrar;
    private JButton btnSiguiente;
    private JButton btnAtender;
    private JButton btnListar;
    private JPanel Gestionarincidentes;
    private GestorIncidentes gestor;
    public Ventana() {
        gestor = new GestorIncidentes(10);
        cargarTipos();
        setTitle("Gestionar Incidentes");
        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        btnRegistrar.addActionListener(e -> registrar());
        btnSiguiente.addActionListener(e -> siguiente());
        btnAtender.addActionListener(e -> atender());
        btnListar.addActionListener(e -> listar());
        cargarIncidentes();
        actualizarContadores();
        siguiente();
        listar();
        pack();
        setLocationRelativeTo(null);
    }
    private void cargarTipos() {
        cmbTipo.removeAllItems();
        cmbTipo.addItem("PHISHING");
        cmbTipo.addItem("MALWARE");
        cmbTipo.addItem("ACCESO_NO_AUTORIZADO");
        cmbTipo.addItem("FUGA_DATOS");
        cmbTipo.addItem("OTRO");
    }
    private void cargarIncidentes() {
        gestor.registrarIncidente(
                new IncidenteSeguridad("INC1", "PHISHING",
                        "Correo sospechoso recibido", 3));
        gestor.registrarIncidente(
                new IncidenteSeguridad("INC2", "MALWARE",
                        "Archivo malicioso detectado", 5));
        gestor.registrarIncidente(
                new IncidenteSeguridad("INC3", "ACCESO_NO_AUTORIZADO",
                        "Intento de acceso sin permiso", 4));
        gestor.registrarIncidente(
                new IncidenteSeguridad("INC4", "FUGA_DATOS",
                        "Posible filtracion de informacion", 5));
        gestor.registrarIncidente(
                new IncidenteSeguridad("INC5", "OTRO",
                        "Actividad inusual en el sistema", 2));
        gestor.registrarIncidente(
                new IncidenteSeguridad("INC6", "PHISHING",
                        "Enlace sospechoso detectado", 4));
        gestor.registrarIncidente(
                new IncidenteSeguridad("INC7", "MALWARE",
                        "Alerta de antivirus", 3));
        gestor.registrarIncidente(
                new IncidenteSeguridad("INC8", "OTRO",
                        "Intentos repetidos de inicio de sesion", 1));
    }
    private void registrar() {
        String codigo = txtCodigo.getText().trim();
        String tipo = (String) cmbTipo.getSelectedItem();
        String descripcion = txtDescripcion.getText().trim();
        int prioridad = (int) spnPrioridad.getValue();
        if (codigo.isEmpty() || descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Ingrese el codigo y la descripcion.");
            return;
        }
        if (tipo == null) {
            JOptionPane.showMessageDialog(
                    this, "Seleccione un tipo de incidente.");
            return;
        }
        IncidenteSeguridad incidente = new IncidenteSeguridad(
                codigo, tipo, descripcion, prioridad);
        if (gestor.registrarIncidente(incidente)) {
            JOptionPane.showMessageDialog(
                    this, "Incidente registrado correctamente.");
            txtCodigo.setText("");
            txtDescripcion.setText("");
            cmbTipo.setSelectedIndex(0);
            spnPrioridad.setValue(1);
            actualizarContadores();
            siguiente();
            listar();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo registrar. Revise el codigo, "
                            + "los duplicados y la capacidad de la cola.");
        }
    }
    private void siguiente() {
        IncidenteSeguridad incidente =
                gestor.consultarSiguienteIncidente();
        if (incidente == null) {
            lblCodigoActual.setText("CODIGO: -");
            lblTipoActual.setText("TIPO: -");
            lblPrioridadActual.setText("PRIORIDAD: -");
            lblEstadoActual.setText("ESTADO: -");
            txtDescripcionActual.setText("");
            return;
        }
        lblCodigoActual.setText(
                "CODIGO: " + incidente.getCodigo());
        lblTipoActual.setText(
                "TIPO: " + incidente.getTipo());
        lblPrioridadActual.setText(
                "PRIORIDAD: " + incidente.getPrioridad());
        lblEstadoActual.setText(
                "ESTADO: " + incidente.getEstado());
        txtDescripcionActual.setText(
                incidente.getDescripcion());
    }
    private void atender() {
        IncidenteSeguridad incidente =
                gestor.atenderSiguienteIncidente();
        if (incidente == null) {
            JOptionPane.showMessageDialog(
                    this, "No hay incidentes pendientes.");
        } else {
            JOptionPane.showMessageDialog(
                    this, "Atendido: " + incidente.getCodigo());
        }
        actualizarContadores();
        siguiente();
        listar();
    }
    private void listar() {
        List<IncidenteSeguridad> incidentes =
                gestor.listarIncidentes();
        txtLista.setText("");
        for (IncidenteSeguridad incidente : incidentes) {
            txtLista.append(
                    "Codigo: " + incidente.getCodigo()
                            + " | Tipo: " + incidente.getTipo()
                            + " | Prioridad: " + incidente.getPrioridad()
                            + " | Estado: " + incidente.getEstado()
                            + "\n");
        }
        if (incidentes.isEmpty()) {
            txtLista.setText("No hay incidentes pendientes.");
        }
    }
    private void actualizarContadores() {
        int pendientes = gestor.contarIncidentesPendientes();
        int disponibles = gestor.consultarEspaciosDisponibles();
        lblPendientes.setText("PENDIENTES: " + pendientes);
        lblDisponibles.setText("DISPONIBLES: " + disponibles);
        lblCapacidad.setText("CAPACIDAD: " + (pendientes + disponibles));
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Ventana().setVisible(true);
        });
    }
}