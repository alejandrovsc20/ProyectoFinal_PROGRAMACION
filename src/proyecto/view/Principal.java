package view;

import dao.ClienteDAO;
import dao.ClienteDAOimpl;
import dao.VideojuegoDAO;
import dao.VideojuegoDAOimpl;
import dao.VentaDAO;
import dao.VentaDAOimpl;
import dto.VentaDTO;
import model.Cliente;
import model.Usuario;
import model.Videojuego;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class Principal extends JFrame {

    private Usuario usuarioActual;
    private VideojuegoDAO videojuegoDAO;
    private VentaDAO ventaDAO;
    private ClienteDAO clienteDAO;

    private JTable tablaCentral;
    private DefaultTableModel modeloTabla;
    private JPanel panelOperaciones;
    private String moduloActivo = "Videojuegos";

    public Principal(Usuario usuario) {
        this.usuarioActual = usuario;
        this.videojuegoDAO = new VideojuegoDAOimpl();
        this.ventaDAO = new VentaDAOimpl();
        this.clienteDAO = new ClienteDAOimpl();
        initUI();
        cargarModuloVideojuegos();
    }

    private void initUI() {
        setTitle("GameStore Manager | Usuario: " + usuarioActual.getUsername() + " (" + usuarioActual.getRol() + ")");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. MENU BAR
        JMenuBar menuBar = new JMenuBar();
        JMenu menuOpciones = new JMenu("Opciones de Sesión");
        JMenuItem itemCerrarSesion = new JMenuItem("Cerrar Sesión");
        itemCerrarSesion.addActionListener(e -> {
            new Login().setVisible(true);
            this.dispose();
        });
        menuOpciones.add(itemCerrarSesion);
        menuBar.add(menuOpciones);
        setJMenuBar(menuBar);

        // 2. PANEL LATERAL DE NAVEGACIÓN (Negro Puro)
        JPanel panelNavegacion = new JPanel();
        panelNavegacion.setLayout(new BoxLayout(panelNavegacion, BoxLayout.Y_AXIS));
        panelNavegacion.setPreferredSize(new Dimension(200, 0));
        panelNavegacion.setBackground(Color.BLACK); // <-- FONDO NEGRO APLICADO AQUÍ
        panelNavegacion.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));

        JLabel lblLogo = new JLabel("GAME STORE", SwingConstants.CENTER);
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelNavegacion.add(lblLogo);
        panelNavegacion.add(Box.createVerticalStrut(40));

        JButton btnVideojuegos = crearBotonMenu("🎮  Juegos");
        JButton btnVentas = crearBotonMenu("🛒  Ventas");
        JButton btnClientes = crearBotonMenu("👥  Clientes");

        panelNavegacion.add(btnVideojuegos);
        panelNavegacion.add(Box.createVerticalStrut(15));
        panelNavegacion.add(btnVentas);
        panelNavegacion.add(Box.createVerticalStrut(15));
        panelNavegacion.add(btnClientes);

        btnVideojuegos.addActionListener(e -> cargarModuloVideojuegos());
        btnVentas.addActionListener(e -> cargarModuloVentas());
        btnClientes.addActionListener(e -> cargarModuloClientes());

        add(panelNavegacion, BorderLayout.WEST);

        // 3. JTABLE CENTRAL (Estilizada)
        modeloTabla = new DefaultTableModel();
        tablaCentral = new JTable(modeloTabla);
        tablaCentral.setDefaultEditor(Object.class, null);
        tablaCentral.setRowHeight(35);
        tablaCentral.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tablaCentral.setSelectionBackground(new Color(200, 220, 240));

        JTableHeader header = tablaCentral.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBackground(new Color(230, 230, 230));

        JScrollPane scrollTabla = new JScrollPane(tablaCentral);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder());

        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelCentral.add(scrollTabla, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);

        // 4. PANEL DE OPERACIONES (Derecha)
        panelOperaciones = new JPanel();
        panelOperaciones.setPreferredSize(new Dimension(280, 0));
        panelOperaciones.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        add(panelOperaciones, BorderLayout.EAST);
    }

    private JButton crearBotonMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(180, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setForeground(Color.WHITE);
        btn.setBackground(Color.BLACK);

        btn.setContentAreaFilled(false);
        btn.setOpaque(true);

        btn.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ==========================================
    // MÓDULO: VIDEOJUEGOS
    // ==========================================
    private void cargarModuloVideojuegos() {
        moduloActivo = "Videojuegos";
        modeloTabla.setColumnIdentifiers(new String[] { "ID", "Título", "Género", "Plataforma", "Precio", "Stock" });
        modeloTabla.setRowCount(0);

        List<Videojuego> lista = videojuegoDAO.listarTodos();
        for (Videojuego v : lista) {
            modeloTabla.addRow(new Object[] {
                    v.getIdVideojuego(), v.getTitulo(), v.getGenero(),
                    v.getPlataforma(), v.getPrecio() + " €", v.getStock()
            });
        }

        panelOperaciones.removeAll();
        panelOperaciones.setLayout(new BoxLayout(panelOperaciones, BoxLayout.Y_AXIS));

        JLabel lblTituloOp = new JLabel("Gestión de Juegos");
        lblTituloOp.setFont(new Font("SansSerif", Font.BOLD, 18));
        panelOperaciones.add(lblTituloOp);
        panelOperaciones.add(Box.createVerticalStrut(20));

        if (usuarioActual.getRol() == model.RolUsuario.EMPLEADO) {
            JTextField txtTitulo = new JTextField();
            // Definimos las opciones que queremos que salgan en los desplegables
            String[] opcionesGenero = { "Acción", "Aventura", "RPG", "Deportes", "Shooter", "Estrategia", "Lucha",
                    "Plataformas", "Terror" };
            String[] opcionesPlataforma = { "PC", "PS5", "PS4", "Xbox Series X/S", "Xbox One", "Nintendo Switch" };
            JComboBox<String> cmbGenero = new JComboBox<>(opcionesGenero);
            JComboBox<String> cmbPlataforma = new JComboBox<>(opcionesPlataforma);
            cmbGenero.setForeground(Color.BLACK);
            cmbPlataforma.setForeground(Color.BLACK);

            JTextField txtPrecio = new JTextField();
            JTextField txtStock = new JTextField();

            panelOperaciones.add(new JLabel("Título:"));
            panelOperaciones.add(txtTitulo);
            panelOperaciones.add(Box.createVerticalStrut(5));
            panelOperaciones.add(new JLabel("Género:"));
            panelOperaciones.add(cmbGenero);
            panelOperaciones.add(Box.createVerticalStrut(5));
            panelOperaciones.add(new JLabel("Plataforma:"));
            panelOperaciones.add(cmbPlataforma);
            panelOperaciones.add(Box.createVerticalStrut(5));
            panelOperaciones.add(new JLabel("Precio:"));
            panelOperaciones.add(txtPrecio);
            panelOperaciones.add(Box.createVerticalStrut(5));
            panelOperaciones.add(new JLabel("Stock:"));
            panelOperaciones.add(txtStock);

            JButton btnGuardar = new JButton("Guardar Nuevo");
            btnGuardar.setBackground(new Color(39, 174, 96));
            btnGuardar.setForeground(Color.WHITE);
            btnGuardar.setContentAreaFilled(false);
            btnGuardar.setOpaque(true);

            JButton btnEliminar = new JButton("Eliminar Seleccionado");
            btnEliminar.setBackground(new Color(231, 76, 60));
            btnEliminar.setForeground(Color.WHITE);
            btnEliminar.setContentAreaFilled(false);
            btnEliminar.setOpaque(true);

            btnGuardar.addActionListener(e -> {
                try {
                    Videojuego v = new Videojuego();
                    v.setTitulo(txtTitulo.getText());
                    v.setGenero(cmbGenero.getSelectedItem().toString());
                    v.setPlataforma(cmbPlataforma.getSelectedItem().toString());
                    v.setPrecio(Double.parseDouble(txtPrecio.getText()));
                    v.setStock(Integer.parseInt(txtStock.getText()));
                    v.setMultijugador(false);
                    if (videojuegoDAO.insertar(v))
                        cargarModuloVideojuegos();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error en los datos numéricos.");
                }
            });

            btnEliminar.addActionListener(e -> {
                int fila = tablaCentral.getSelectedRow();
                if (fila != -1) {
                    int id = (int) modeloTabla.getValueAt(fila, 0);
                    if (videojuegoDAO.eliminar(id))
                        cargarModuloVideojuegos();
                }
            });

            panelOperaciones.add(Box.createVerticalStrut(25));
            panelOperaciones.add(btnGuardar);
            panelOperaciones.add(Box.createVerticalStrut(10));
            panelOperaciones.add(btnEliminar);

        } else {
            JTextArea info = new JTextArea(
                    "Catálogo disponible.\nPara comprar un juego o \nconsultar disponibilidad, \nacude a un empleado.");
            info.setEditable(false);
            info.setOpaque(false);
            info.setFont(new Font("SansSerif", Font.PLAIN, 14));
            panelOperaciones.add(info);
        }
        panelOperaciones.revalidate();
        panelOperaciones.repaint();
    }

    // ==========================================
    // MÓDULO: VENTAS
    // ==========================================
    private void cargarModuloVentas() {
        moduloActivo = "Ventas";
        modeloTabla.setColumnIdentifiers(
                new String[] { "ID Venta", "Cliente", "Videojuego", "Fecha", "Cantidad", "Total" });
        modeloTabla.setRowCount(0);

        List<VentaDTO> lista = ventaDAO.listarTodasConDetalles();
        for (VentaDTO v : lista) {
            modeloTabla.addRow(new Object[] {
                    v.getIdVenta(), v.getNombreCliente(), v.getTituloVideojuego(),
                    v.getFechaCompra(), v.getCantidad(), String.format("%.2f €", v.getPrecioTotal())
            });
        }

        panelOperaciones.removeAll();
        panelOperaciones.setLayout(new BoxLayout(panelOperaciones, BoxLayout.Y_AXIS));

        JLabel lblTituloOp = new JLabel("Registro de Ventas");
        lblTituloOp.setFont(new Font("SansSerif", Font.BOLD, 18));
        panelOperaciones.add(lblTituloOp);
        panelOperaciones.add(Box.createVerticalStrut(20));

        if (usuarioActual.getRol() == model.RolUsuario.EMPLEADO) {
            JTextField txtIdCliente = new JTextField();
            JTextField txtIdJuego = new JTextField();
            JTextField txtCantidad = new JTextField();

            panelOperaciones.add(new JLabel("ID Cliente:"));
            panelOperaciones.add(txtIdCliente);
            panelOperaciones.add(Box.createVerticalStrut(5));
            panelOperaciones.add(new JLabel("ID Videojuego:"));
            panelOperaciones.add(txtIdJuego);
            panelOperaciones.add(Box.createVerticalStrut(5));
            panelOperaciones.add(new JLabel("Cantidad:"));
            panelOperaciones.add(txtCantidad);
            // --- BOTÓN DE NUEVA VENTA ---
            JButton btnNuevaVenta = new JButton("Completar Venta");
            btnNuevaVenta.setBackground(new Color(39, 174, 96));
            btnNuevaVenta.setForeground(Color.WHITE);
            btnNuevaVenta.setContentAreaFilled(false); // <-- Magia
            btnNuevaVenta.setOpaque(true); // <-- Magia

            btnNuevaVenta.addActionListener(e -> {
                try {
                    int idCliente = Integer.parseInt(txtIdCliente.getText());
                    int idJuego = Integer.parseInt(txtIdJuego.getText());
                    int cant = Integer.parseInt(txtCantidad.getText());

                    Videojuego v = videojuegoDAO.buscarPorId(idJuego);

                    if (v != null) {
                        model.Venta nuevaVenta = new model.Venta();
                        nuevaVenta.setIdCliente(idCliente);
                        nuevaVenta.setIdVideojuego(idJuego);
                        nuevaVenta.setCantidad(cant);
                        nuevaVenta.setPrecioHistorico(v.getPrecio());

                        if (ventaDAO.insertar(nuevaVenta)) {
                            double total = v.getPrecio() * cant;
                            JOptionPane.showMessageDialog(this, "Venta registrada.\nTotal a cobrar: " + total + "€");
                            cargarModuloVentas();
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "El ID del videojuego no existe.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Datos numéricos inválidos.");
                }
            });

            // --- BOTÓN DE ELIMINAR VENTA ---
            JButton btnEliminar = new JButton("Borrar Venta");
            btnEliminar.setBackground(new Color(231, 76, 60));
            btnEliminar.setForeground(Color.WHITE);
            btnEliminar.setContentAreaFilled(false); // <-- Magia
            btnEliminar.setOpaque(true); // <-- Magia

            btnEliminar.addActionListener(e -> {
                int fila = tablaCentral.getSelectedRow();
                if (fila != -1) {
                    int id = (int) modeloTabla.getValueAt(fila, 0);
                    int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres borrar esta venta?",
                            "Confirmar", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION && ventaDAO.eliminar(id)) {
                        cargarModuloVentas();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Selecciona una venta de la tabla.");
                }
            });

            panelOperaciones.add(Box.createVerticalStrut(25));
            panelOperaciones.add(btnNuevaVenta);
            panelOperaciones.add(Box.createVerticalStrut(10));
            panelOperaciones.add(btnEliminar);

        } else {
            JTextArea info = new JTextArea(
                    "Historial de compras.\nPara devoluciones, acuda a\nun empleado en mostrador.");
            info.setEditable(false);
            info.setOpaque(false);
            info.setFont(new Font("SansSerif", Font.PLAIN, 14));
            panelOperaciones.add(info);
        }
        panelOperaciones.revalidate();
        panelOperaciones.repaint();
    }

    // ==========================================
    // MÓDULO: CLIENTES
    // ==========================================
    private void cargarModuloClientes() {
        moduloActivo = "Clientes";
        modeloTabla.setColumnIdentifiers(new String[] { "ID", "Username", "Nombre", "Email", "Puntos", "Plataforma" });
        modeloTabla.setRowCount(0);

        List<Cliente> lista = clienteDAO.listarTodos();
        for (Cliente c : lista) {
            modeloTabla.addRow(new Object[] {
                    c.getIdUsuario(), c.getUsername(), c.getNombre() + " " + c.getApellidos(),
                    c.getEmail(), c.getPuntosFidelidad(), c.getPlataformaPreferida()
            });
        }

        panelOperaciones.removeAll();
        panelOperaciones.setLayout(new BoxLayout(panelOperaciones, BoxLayout.Y_AXIS));

        JLabel lblTituloOp = new JLabel("Red de Jugadores");
        lblTituloOp.setFont(new Font("SansSerif", Font.BOLD, 18));
        panelOperaciones.add(lblTituloOp);
        panelOperaciones.add(Box.createVerticalStrut(20));

        if (usuarioActual.getRol() == model.RolUsuario.EMPLEADO) {
            JTextArea info = new JTextArea("Registrar nuevos clientes\ndesde la ventana de Login.");
            info.setEditable(false);
            info.setOpaque(false);
            info.setFont(new Font("SansSerif", Font.PLAIN, 14));
            panelOperaciones.add(info);
            panelOperaciones.add(Box.createVerticalStrut(25));

            // --- BOTÓN DE ELIMINAR CLIENTE ---
            JButton btnEliminar = new JButton("Dar de baja (Borrar)");
            btnEliminar.setBackground(new Color(231, 76, 60));
            btnEliminar.setForeground(Color.WHITE);
            btnEliminar.setContentAreaFilled(false); // <-- Magia
            btnEliminar.setOpaque(true); // <-- Magia

            btnEliminar.addActionListener(e -> {
                int fila = tablaCentral.getSelectedRow();
                if (fila != -1) {
                    int id = (int) modeloTabla.getValueAt(fila, 0);
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "¿Seguro que quieres borrar al cliente con ID " + id
                                    + "?\nSe borrarán también sus ventas asociadas.",
                            "Confirmar baja", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                    if (confirm == JOptionPane.YES_OPTION && clienteDAO.eliminar(id)) {
                        JOptionPane.showMessageDialog(this, "Cliente eliminado correctamente.");
                        cargarModuloClientes();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Selecciona un cliente de la tabla.");
                }
            });
            panelOperaciones.add(btnEliminar);

        } else {
            JTextArea info = new JTextArea("Explora los perfiles de\notros jugadores.");
            info.setEditable(false);
            info.setOpaque(false);
            info.setFont(new Font("SansSerif", Font.PLAIN, 14));
            panelOperaciones.add(info);
        }
        panelOperaciones.revalidate();
        panelOperaciones.repaint();
    }
}