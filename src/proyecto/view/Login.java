package view;

import dao.UsuarioDAO;
import dao.UsuarioDAOimpl;
import model.Usuario;

import javax.swing.*;
import javax.swing.border.TitledBorder;
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
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Fondo principal en negro
        getContentPane().setBackground(Color.BLACK);

        // Cabecera Minimalista
        JLabel lblTitulo = new JLabel("GAME STORE", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE); // Texto en blanco
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // Panel de Formulario en negro
        JPanel panelForm = new JPanel(new GridLayout(4, 1, 10, 15));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));
        panelForm.setBackground(Color.BLACK);

        // Configuración de la caja de Usuario (Fondo gris oscuro, texto blanco)
        txtUsername = new JTextField();
        TitledBorder tbUser = BorderFactory.createTitledBorder("Usuario");
        tbUser.setTitleColor(Color.LIGHT_GRAY);
        txtUsername.setBorder(tbUser);
        txtUsername.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtUsername.setBackground(new Color(43, 48, 58));
        txtUsername.setForeground(Color.WHITE);
        txtUsername.setCaretColor(Color.WHITE); // El cursor parpadeante en blanco

        // Configuración de la caja de Contraseña (Fondo gris oscuro, texto blanco)
        txtPassword = new JPasswordField();
        TitledBorder tbPass = BorderFactory.createTitledBorder("Contraseña");
        tbPass.setTitleColor(Color.LIGHT_GRAY);
        txtPassword.setBorder(tbPass);
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtPassword.setBackground(new Color(43, 48, 58));
        txtPassword.setForeground(Color.WHITE);
        txtPassword.setCaretColor(Color.WHITE);

        // Botón Entrar (Gris oscuro con borde claro)
       // Botón Entrar (Gris oscuro con borde claro)
        btnEntrar = new JButton("Entrar");
        btnEntrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnEntrar.setBackground(Color.DARK_GRAY);
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        btnEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnEntrar.setContentAreaFilled(false);
        btnEntrar.setOpaque(true);

        // Enlace de Registro (Azul claro para que contraste con el fondo negro)
        btnIrRegistro = new JButton("¿No tienes cuenta? Regístrate");
        btnIrRegistro.setContentAreaFilled(false);
        btnIrRegistro.setBorderPainted(false);
        btnIrRegistro.setForeground(new Color(100, 180, 255));
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
            // Un JOptionPane personalizado para que también pegue con el estilo
            UIManager.put("OptionPane.background", Color.BLACK);
            UIManager.put("Panel.background", Color.BLACK);
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error de Acceso",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}