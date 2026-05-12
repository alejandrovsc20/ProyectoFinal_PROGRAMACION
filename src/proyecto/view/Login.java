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
        setTitle("Login - Tienda de Videojuegos");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        txtUsername = new JTextField();
        txtUsername.setBorder(BorderFactory.createTitledBorder("Usuario"));

        txtPassword = new JPasswordField();
        txtPassword.setBorder(BorderFactory.createTitledBorder("Contraseña"));

        btnEntrar = new JButton("Entrar");
        btnIrRegistro = new JButton("¿No tienes cuenta? Regístrate");
        btnIrRegistro.setContentAreaFilled(false);
        btnIrRegistro.setBorderPainted(false);
        btnIrRegistro.setForeground(Color.BLUE);

        panel.add(txtUsername);
        panel.add(txtPassword);
        panel.add(btnEntrar);
        panel.add(btnIrRegistro);

        add(panel);

        // Eventos
        btnEntrar.addActionListener(e -> accionLogin());

        btnIrRegistro.addActionListener(e -> {
            new FormularioRegistro().setVisible(true);
        });
    }

    private void accionLogin() {
        String user = txtUsername.getText();
        String pass = new String(txtPassword.getPassword());

        Usuario u = usuarioDAO.validarLogin(user, pass);

        if (u != null) {
            JOptionPane.showMessageDialog(this, "Bienvenido " + u.getNombre());
            // DESCOMENTAR ESTAS DOS LÍNEAS:
            new Principal(u).setVisible(true);
            this.dispose(); // Cierra el login
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}