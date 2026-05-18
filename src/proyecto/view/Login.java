package view;

import dao.UsuarioDAO;
import dao.UsuarioDAOimpl;
import model.Usuario;

import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnEntrar, btnIrRegistro;
    private UsuarioDAO usuarioDAO;

    public Login() {
        usuarioDAO = new UsuarioDAOimpl();
        initUI();
    }

    private void initUI() {
        setTitle("Acceso - Tienda de Videojuegos");
        setSize(400, 350); // Un poco más grande para respirar
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Cabecera Minimalista
        JLabel lblTitulo = new JLabel("GAME STORE", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // Panel de Formulario
        JPanel panelForm = new JPanel(new GridLayout(4, 1, 10, 15));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));

        txtUsername = new JTextField();
        txtUsername.setBorder(BorderFactory.createTitledBorder("Usuario"));
        txtUsername.setFont(new Font("SansSerif", Font.PLAIN, 14));

        txtPassword = new JPasswordField();
        txtPassword.setBorder(BorderFactory.createTitledBorder("Contraseña"));
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 14));

        btnEntrar = new JButton("Entrar");
        btnEntrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnEntrar.setBackground(new Color(41, 128, 185)); // Azul elegante
        btnEntrar.setForeground(Color.WHITE);

        btnIrRegistro = new JButton("¿No tienes cuenta? Regístrate");
        btnIrRegistro.setContentAreaFilled(false);
        btnIrRegistro.setBorderPainted(false);
        btnIrRegistro.setForeground(new Color(41, 128, 185));
        btnIrRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelForm.add(txtUsername);
        panelForm.add(txtPassword);
        panelForm.add(btnEntrar);
        panelForm.add(btnIrRegistro);

        add(panelForm, BorderLayout.CENTER);

        // Eventos
        btnEntrar.addActionListener(e -> accionLogin());
        btnIrRegistro.addActionListener(e -> new FormularioRegistro().setVisible(true));
    }

    private void accionLogin() {
        String user = txtUsername.getText();
        String pass = new String(txtPassword.getPassword());

        Usuario u = usuarioDAO.validarLogin(user, pass);

        if (u != null) {
            new Principal(u).setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error de Acceso",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}