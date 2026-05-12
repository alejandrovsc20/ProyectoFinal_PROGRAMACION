package view;

import dao.UsuarioDAO;
import dao.UsuarioDAOimpl;
import model.Cliente;
import model.Empleado;
import model.RolUsuario;
import model.Usuario;

import javax.swing.*;
import java.awt.*;

public class FormularioRegistro extends JFrame {

    // Componentes comunes
    private JTextField txtUsername, txtEmail, txtNombre, txtApellidos, txtDni;
    private JPasswordField txtPassword;
    private JComboBox<RolUsuario> cmbRol;

    // Componentes específicos de Cliente
    private JTextField txtPuntos, txtPlataforma;
    private JPanel panelCliente;

    // Componentes específicos de Empleado
    private JTextField txtFecha, txtSalario, txtTurno;
    private JPanel panelEmpleado;

    private JButton btnRegistrar, btnCancelar;

    // Capa de datos (DAO)
    private UsuarioDAO usuarioDAO;

    public FormularioRegistro() {
        usuarioDAO = new UsuarioDAOimpl(); // Instanciamos el DAO
        initUI();
    }

    private void initUI() {
        setTitle("Registro de Usuario - Tienda de Videojuegos");
        setSize(450, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // PANEL PRINCIPAL (Scrollable por si hay muchos campos)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- SECCIÓN COMÚN ---
        mainPanel.add(crearFila("Nombre de Usuario:", txtUsername = new JTextField()));
        mainPanel.add(crearFila("Contraseña:", txtPassword = new JPasswordField()));
        mainPanel.add(crearFila("Email:", txtEmail = new JTextField()));
        mainPanel.add(crearFila("Nombre:", txtNombre = new JTextField()));
        mainPanel.add(crearFila("Apellidos:", txtApellidos = new JTextField()));
        mainPanel.add(crearFila("DNI:", txtDni = new JTextField()));

        // --- SELECCIÓN DE ROL (Dinámico) ---
        JPanel pnlRol = new JPanel(new GridLayout(1, 2));
        pnlRol.add(new JLabel("Tipo de Usuario:"));
        cmbRol = new JComboBox<>(RolUsuario.values());
        pnlRol.add(cmbRol);
        mainPanel.add(pnlRol);
        mainPanel.add(Box.createVerticalStrut(15));

        // --- PANEL ESPECÍFICO CLIENTE ---
        panelCliente = new JPanel();
        panelCliente.setLayout(new BoxLayout(panelCliente, BoxLayout.Y_AXIS));
        panelCliente.add(crearFila("Puntos Iniciales:", txtPuntos = new JTextField("0")));
        panelCliente.add(crearFila("Plataforma Favorita:", txtPlataforma = new JTextField()));

        // --- PANEL ESPECÍFICO EMPLEADO ---
        panelEmpleado = new JPanel();
        panelEmpleado.setLayout(new BoxLayout(panelEmpleado, BoxLayout.Y_AXIS));
        panelEmpleado.add(crearFila("Fecha Contrat. (YYYY-MM-DD):", txtFecha = new JTextField()));
        panelEmpleado.add(crearFila("Salario Mensual:", txtSalario = new JTextField()));
        panelEmpleado.add(crearFila("Turno (Mañana/Tarde):", txtTurno = new JTextField()));
        panelEmpleado.setVisible(false); // Oculto al inicio

        mainPanel.add(panelCliente);
        mainPanel.add(panelEmpleado);

        // LÓGICA DINÁMICA: Mostrar/Ocultar campos según el rol
        cmbRol.addActionListener(e -> {
            boolean esCliente = cmbRol.getSelectedItem() == RolUsuario.CLIENTE;
            panelCliente.setVisible(esCliente);
            panelEmpleado.setVisible(!esCliente);
            this.revalidate();
            this.repaint();
        });

        // --- BOTONES ---
        JPanel pnlBotones = new JPanel();
        btnRegistrar = new JButton("Registrar Usuario");
        btnCancelar = new JButton("Cancelar");
        pnlBotones.add(btnRegistrar);
        pnlBotones.add(btnCancelar);

        add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        add(pnlBotones, BorderLayout.SOUTH);

        // EVENTO DEL BOTÓN REGISTRAR
        btnRegistrar.addActionListener(e -> accionRegistrar());
        btnCancelar.addActionListener(e -> dispose());
    }

    private JPanel crearFila(String etiqueta, JComponent comp) {
        JPanel p = new JPanel(new GridLayout(1, 2, 5, 5));
        p.add(new JLabel(etiqueta));
        p.add(comp);
        p.setMaximumSize(new Dimension(400, 30));
        return p;
    }

    private void accionRegistrar() {
        RolUsuario rol = (RolUsuario) cmbRol.getSelectedItem();
        boolean exito = false;

        try {
            if (rol == RolUsuario.CLIENTE) {
                Cliente c = new Cliente();
                rellenarDatosComunes(c);
                c.setPuntosFidelidad(Integer.parseInt(txtPuntos.getText()));
                c.setPlataformaPreferida(txtPlataforma.getText());
                exito = usuarioDAO.registrarCliente(c);
            } else {
                Empleado emp = new Empleado();
                rellenarDatosComunes(emp);
                emp.setFechaContratacion(txtFecha.getText());
                emp.setSalario(Double.parseDouble(txtSalario.getText()));
                emp.setTurno(txtTurno.getText());
                exito = usuarioDAO.registrarEmpleado(emp);
            }

            if (exito) {
                JOptionPane.showMessageDialog(this, "¡Usuario registrado con éxito!", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar. Revisa el log o los datos.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error en los datos: " + ex.getMessage());
        }
    }

    private void rellenarDatosComunes(Usuario u) {
        u.setUsername(txtUsername.getText());
        u.setPassword(new String(txtPassword.getPassword()));
        u.setEmail(txtEmail.getText());
        u.setNombre(txtNombre.getText());
        u.setApellidos(txtApellidos.getText());
        u.setDni(txtDni.getText());
        u.setRol((RolUsuario) cmbRol.getSelectedItem());
    }
}