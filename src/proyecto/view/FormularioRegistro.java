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

    private JTextField txtUsername, txtEmail, txtNombre, txtApellidos, txtDni;
    private JPasswordField txtPassword;
    private JComboBox<RolUsuario> cmbRol;
    private JTextField txtPuntos, txtPlataforma;
    private JPanel panelCliente;
    private JTextField txtFecha, txtSalario, txtTurno;
    private JPanel panelEmpleado;
    private JButton btnRegistrar, btnCancelar;
    private UsuarioDAO usuarioDAO;

    public FormularioRegistro() {
        usuarioDAO = new UsuarioDAOimpl();
        initUI();
    }

    private void initUI() {
        setTitle("Nuevo Registro");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("CREAR CUENTA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        mainPanel.add(crearFila("Usuario:", txtUsername = new JTextField()));
        mainPanel.add(crearFila("Contraseña:", txtPassword = new JPasswordField()));
        mainPanel.add(crearFila("Email:", txtEmail = new JTextField()));
        mainPanel.add(crearFila("Nombre:", txtNombre = new JTextField()));
        mainPanel.add(crearFila("Apellidos:", txtApellidos = new JTextField()));
        mainPanel.add(crearFila("DNI:", txtDni = new JTextField()));

        JPanel pnlRol = new JPanel(new GridLayout(1, 2, 10, 10));
        JLabel lblRol = new JLabel("Tipo de Usuario:");
        lblRol.setFont(new Font("SansSerif", Font.BOLD, 12));
        pnlRol.add(lblRol);
        cmbRol = new JComboBox<>(RolUsuario.values());
        pnlRol.add(cmbRol);
        pnlRol.setMaximumSize(new Dimension(500, 35));
        mainPanel.add(pnlRol);
        mainPanel.add(Box.createVerticalStrut(20));

        panelCliente = new JPanel();
        panelCliente.setLayout(new BoxLayout(panelCliente, BoxLayout.Y_AXIS));
        panelCliente.add(crearFila("Puntos Iniciales:", txtPuntos = new JTextField("0")));
        panelCliente.add(crearFila("Plataforma Favorita:", txtPlataforma = new JTextField()));

        panelEmpleado = new JPanel();
        panelEmpleado.setLayout(new BoxLayout(panelEmpleado, BoxLayout.Y_AXIS));
        panelEmpleado.add(crearFila("Fecha Contrat. (YYYY-MM-DD):", txtFecha = new JTextField()));
        panelEmpleado.add(crearFila("Salario Mensual:", txtSalario = new JTextField()));
        panelEmpleado.add(crearFila("Turno (Mañana/Tarde):", txtTurno = new JTextField()));
        panelEmpleado.setVisible(false);

        mainPanel.add(panelCliente);
        mainPanel.add(panelEmpleado);

        cmbRol.addActionListener(e -> {
            boolean esCliente = cmbRol.getSelectedItem() == RolUsuario.CLIENTE;
            panelCliente.setVisible(esCliente);
            panelEmpleado.setVisible(!esCliente);
            this.revalidate();
            this.repaint();
        });

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        btnRegistrar = new JButton("Registrar Usuario");
        btnRegistrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnRegistrar.setBackground(new Color(39, 174, 96)); // Verde éxito
        btnRegistrar.setForeground(Color.WHITE);

        btnRegistrar.setContentAreaFilled(false);
        btnRegistrar.setOpaque(true);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("SansSerif", Font.PLAIN, 14));

        pnlBotones.add(btnRegistrar);
        pnlBotones.add(btnCancelar);

        add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        add(pnlBotones, BorderLayout.SOUTH);

        btnRegistrar.addActionListener(e -> accionRegistrar());
        btnCancelar.addActionListener(e -> dispose());
    }

    private JPanel crearFila(String etiqueta, JComponent comp) {
        JPanel p = new JPanel(new GridLayout(1, 2, 10, 10));
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        if (comp instanceof JTextField)
            ((JTextField) comp).setFont(new Font("SansSerif", Font.PLAIN, 13));
        p.add(lbl);
        p.add(comp);
        p.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        p.setMaximumSize(new Dimension(500, 40));
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
                JOptionPane.showMessageDialog(this, "¡Usuario registrado con éxito!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar.", "Error", JOptionPane.ERROR_MESSAGE);
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