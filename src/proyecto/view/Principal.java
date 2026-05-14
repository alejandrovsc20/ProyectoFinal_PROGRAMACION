package view;

import dao.VideojuegoDAO;
import dao.VideojuegoDAOimpl;
import dao.VentaDAO;
import dao.VentaDAOimpl;
import dto.VentaDTO;
import model.Usuario;
import model.Videojuego;
import model.RolUsuario;
import dao.ClienteDAO;
import dao.ClienteDAOimpl;
import model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Principal extends JFrame {

    private Usuario usuarioActual;

    // DAOs
    private VideojuegoDAO videojuegoDAO;
    private VentaDAO ventaDAO;
    private ClienteDAO clienteDAO;

    // Componentes de la interfaz
    private JTable tablaCentral;
    private DefaultTableModel modeloTabla;
    private JPanel panelOperaciones; // Panel lateral derecho adaptable
    private String moduloActivo = "Videojuegos"; // Controla en qué sección estamos

    public Principal(Usuario usuario) {
        this.usuarioActual = usuario;
        this.videojuegoDAO = new VideojuegoDAOimpl();
        this.ventaDAO = new VentaDAOimpl();
        this.clienteDAO = new ClienteDAOimpl();
        initUI();
        cargarModuloVideojuegos(); // Cargamos por defecto el catálogo
    }

    private void initUI() {
        setTitle("Dashboard - Tienda de Videojuegos | Usuario: " + usuarioActual.getUsername());
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. MENU BAR (JMenuBar)
        JMenuBar menuBar = new JMenuBar();
        JMenu menuOpciones = new JMenu("Opciones");
        JMenuItem itemCerrarSesion = new JMenuItem("Cerrar Sesión");
        itemCerrarSesion.addActionListener(e -> {
            new Login().setVisible(true);
            this.dispose();
        });
        menuOpciones.add(itemCerrarSesion);
        menuBar.add(menuOpciones);
        setJMenuBar(menuBar);

        // 2. PANEL LATERAL DE NAVEGACIÓN (Izquierda)
        JPanel panelNavegacion = new JPanel();
        panelNavegacion.setLayout(new BoxLayout(panelNavegacion, BoxLayout.Y_AXIS));
        panelNavegacion.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        panelNavegacion.setBackground(Color.DARK_GRAY);

        JButton btnVideojuegos = crearBotonMenu("Juegos");
        JButton btnVentas = crearBotonMenu("Ventas");
        JButton btnClientes = crearBotonMenu("Clientes");

        panelNavegacion.add(btnVideojuegos);
        panelNavegacion.add(Box.createVerticalStrut(10));
        panelNavegacion.add(btnVentas);
        panelNavegacion.add(Box.createVerticalStrut(10));
        panelNavegacion.add(btnClientes);

        // Eventos del menú de navegación
        btnVideojuegos.addActionListener(e -> cargarModuloVideojuegos());
        btnVentas.addActionListener(e -> cargarModuloVentas());
        btnClientes.addActionListener(e -> cargarModuloClientes());

        add(panelNavegacion, BorderLayout.WEST);

        // 3. JTABLE CENTRAL
        modeloTabla = new DefaultTableModel();
        tablaCentral = new JTable(modeloTabla);
        // Evitar que editen las celdas directamente (se edita por el panel lateral)
        tablaCentral.setDefaultEditor(Object.class, null);
        JScrollPane scrollTabla = new JScrollPane(tablaCentral);
        add(scrollTabla, BorderLayout.CENTER);

        // 4. PANEL DE OPERACIONES ADAPTATIVO (Derecha)
        panelOperaciones = new JPanel();
        panelOperaciones.setPreferredSize(new Dimension(250, 0));
        panelOperaciones.setBorder(BorderFactory.createTitledBorder("Operaciones"));
        add(panelOperaciones, BorderLayout.EAST);
    }

    private JButton crearBotonMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(150, 40));
        btn.setForeground(Color.BLACK);
        return btn;
    }

    // ==========================================
    // MÓDULO: VIDEOJUEGOS (Con seguridad por Rol)
    // ==========================================
    private void cargarModuloVideojuegos() {
        moduloActivo = "Videojuegos";

        // 1. Configurar columnas de la tabla
        modeloTabla.setColumnIdentifiers(new String[] { "ID", "Título", "Género", "Plataforma", "Precio", "Stock" });
        modeloTabla.setRowCount(0); // Limpiar datos

        // 2. Traer datos del DAO
        List<Videojuego> lista = videojuegoDAO.listarTodos();
        for (Videojuego v : lista) {
            modeloTabla.addRow(new Object[] {
                    v.getIdVideojuego(), v.getTitulo(), v.getGenero(),
                    v.getPlataforma(), v.getPrecio(), v.getStock()
            });
        }

        // 3. Adaptar el panel de operaciones
        panelOperaciones.removeAll();
        panelOperaciones.setLayout(new BoxLayout(panelOperaciones, BoxLayout.Y_AXIS));

        // --- LÓGICA DE ROL: Solo empleados pueden añadir o borrar ---
        if (usuarioActual.getRol() == model.RolUsuario.EMPLEADO) {
            JTextField txtTitulo = new JTextField();
            JTextField txtGenero = new JTextField();
            JTextField txtPlat = new JTextField();
            JTextField txtPrecio = new JTextField();
            JTextField txtStock = new JTextField();

            panelOperaciones.add(new JLabel("Título:"));
            panelOperaciones.add(txtTitulo);
            panelOperaciones.add(new JLabel("Género:"));
            panelOperaciones.add(txtGenero);
            panelOperaciones.add(new JLabel("Plataforma:"));
            panelOperaciones.add(txtPlat);
            panelOperaciones.add(new JLabel("Precio:"));
            panelOperaciones.add(txtPrecio);
            panelOperaciones.add(new JLabel("Stock:"));
            panelOperaciones.add(txtStock);

            JButton btnGuardar = new JButton("Guardar Nuevo");
            JButton btnEliminar = new JButton("Eliminar Seleccionado");

            // Acción Guardar (Insert)
            btnGuardar.addActionListener(e -> {
                try {
                    Videojuego v = new Videojuego();
                    v.setTitulo(txtTitulo.getText());
                    v.setGenero(txtGenero.getText());
                    v.setPlataforma(txtPlat.getText());
                    v.setPrecio(Double.parseDouble(txtPrecio.getText()));
                    v.setStock(Integer.parseInt(txtStock.getText()));
                    v.setMultijugador(false);

                    if (videojuegoDAO.insertar(v)) {
                        JOptionPane.showMessageDialog(this, "Videojuego guardado correctamente.");
                        cargarModuloVideojuegos(); // Refrescar tabla
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error en los datos numéricos.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            });

            // Acción Eliminar (Delete)
            btnEliminar.addActionListener(e -> {
                int fila = tablaCentral.getSelectedRow();
                if (fila == -1) {
                    JOptionPane.showMessageDialog(this, "Selecciona un juego en la tabla primero.");
                    return;
                }
                int id = (int) modeloTabla.getValueAt(fila, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "¿Borrar juego con ID " + id + "?", "Confirmar",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION && videojuegoDAO.eliminar(id)) {
                    JOptionPane.showMessageDialog(this, "Juego eliminado.");
                    cargarModuloVideojuegos(); // Refrescar
                }
            });

            panelOperaciones.add(Box.createVerticalStrut(20));
            panelOperaciones.add(btnGuardar);
            panelOperaciones.add(Box.createVerticalStrut(10));
            panelOperaciones.add(btnEliminar);

        } else {
            // --- VISTA DEL CLIENTE ---
            panelOperaciones.add(new JLabel("Catálogo de Videojuegos"));
            panelOperaciones.add(Box.createVerticalStrut(10));
            JTextArea info = new JTextArea(
                    "Explora todo nuestro catálogo.\n\nPara comprar un juego o \nconsultar disponibilidad, \nacude a un empleado.");
            info.setEditable(false);
            info.setOpaque(false);
            panelOperaciones.add(info);
        }

        panelOperaciones.revalidate();
        panelOperaciones.repaint();
    }

    // ==========================================
    // MÓDULO: VENTAS (Con cálculo automático y seguridad por Rol)
    // ==========================================
    private void cargarModuloVentas() {
        moduloActivo = "Ventas";

        // 1. Configurar columnas de la tabla (JOIN con DTO)
        modeloTabla.setColumnIdentifiers(
                new String[] { "ID Venta", "Cliente", "Videojuego", "Fecha", "Cantidad", "Total €" });
        modeloTabla.setRowCount(0);

        // 2. Traer datos del DAO
        List<VentaDTO> lista = ventaDAO.listarTodasConDetalles();
        for (VentaDTO v : lista) {
            modeloTabla.addRow(new Object[] {
                    v.getIdVenta(), v.getNombreCliente(), v.getTituloVideojuego(),
                    v.getFechaCompra(), v.getCantidad(), v.getPrecioTotal()
            });
        }

        // 3. Adaptar el panel de operaciones
        panelOperaciones.removeAll();
        panelOperaciones.setLayout(new BoxLayout(panelOperaciones, BoxLayout.Y_AXIS));

        // --- LÓGICA DE ROL: Solo empleados pueden añadir y borrar ventas ---
        if (usuarioActual.getRol() == model.RolUsuario.EMPLEADO) {
            panelOperaciones.add(new JLabel("Añadir Nueva Venta"));
            panelOperaciones.add(Box.createVerticalStrut(10));

            // Ya no pedimos el precio unitario, lo sacaremos de la BD
            JTextField txtIdCliente = new JTextField();
            JTextField txtIdJuego = new JTextField();
            JTextField txtCantidad = new JTextField();

            panelOperaciones.add(new JLabel("ID Cliente:"));
            panelOperaciones.add(txtIdCliente);
            panelOperaciones.add(new JLabel("ID Videojuego:"));
            panelOperaciones.add(txtIdJuego);
            panelOperaciones.add(new JLabel("Cantidad:"));
            panelOperaciones.add(txtCantidad);

            JButton btnNuevaVenta = new JButton("Registrar Venta");
            btnNuevaVenta.addActionListener(e -> {
                try {
                    int idCliente = Integer.parseInt(txtIdCliente.getText());
                    int idJuego = Integer.parseInt(txtIdJuego.getText());
                    int cant = Integer.parseInt(txtCantidad.getText());

                    // 1. Buscamos el videojuego para obtener su precio actual
                    Videojuego v = videojuegoDAO.buscarPorId(idJuego);

                    if (v != null) {
                        // 2. Creamos la venta con el precio extraído
                        model.Venta nuevaVenta = new model.Venta();
                        nuevaVenta.setIdCliente(idCliente);
                        nuevaVenta.setIdVideojuego(idJuego);
                        nuevaVenta.setCantidad(cant);
                        nuevaVenta.setPrecioHistorico(v.getPrecio()); // Precio automático

                        // 3. Insertamos en BD
                        if (ventaDAO.insertar(nuevaVenta)) {
                            double total = v.getPrecio() * cant;
                            JOptionPane.showMessageDialog(this,
                                    "Venta registrada con éxito.\nTotal a cobrar: " + total + "€");
                            cargarModuloVentas(); // Refrescar tabla
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "El ID del videojuego no existe en la tienda.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Error: Introduce números válidos en todos los campos.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al registrar la venta: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            });

            // BOTÓN DE ELIMINAR (Protegido para empleados)
            JButton btnEliminar = new JButton("Borrar Venta Seleccionada");
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

            panelOperaciones.add(Box.createVerticalStrut(15));
            panelOperaciones.add(btnNuevaVenta);
            panelOperaciones.add(Box.createVerticalStrut(10));
            panelOperaciones.add(btnEliminar);

        } else {
            // --- VISTA DEL CLIENTE ---
            panelOperaciones.add(new JLabel("Módulo de Ventas"));
            panelOperaciones.add(Box.createVerticalStrut(10));
            JTextArea info = new JTextArea(
                    "Historial de compras disponible.\n\nPara realizar una devolución\no nueva compra, acuda a un\nempleado en mostrador.");
            info.setEditable(false);
            info.setOpaque(false);
            panelOperaciones.add(info);
        }

        panelOperaciones.revalidate();
        panelOperaciones.repaint();
    }

    // ==========================================
    // MÓDULO: CLIENTES (Con seguridad por Rol)
    // ==========================================
    private void cargarModuloClientes() {
        moduloActivo = "Clientes";

        // 1. Configurar columnas de la tabla
        modeloTabla.setColumnIdentifiers(
                new String[] { "ID", "Username", "Nombre Completo", "Email", "Puntos", "Plataforma" });
        modeloTabla.setRowCount(0); // Limpiar datos de la tabla anterior

        // 2. Traer datos del DAO
        List<Cliente> lista = clienteDAO.listarTodos();
        for (Cliente c : lista) {
            modeloTabla.addRow(new Object[] {
                    c.getIdUsuario(),
                    c.getUsername(),
                    c.getNombre() + " " + c.getApellidos(),
                    c.getEmail(),
                    c.getPuntosFidelidad(),
                    c.getPlataformaPreferida()
            });
        }

        // 3. Adaptar el panel de operaciones
        panelOperaciones.removeAll();
        panelOperaciones.setLayout(new BoxLayout(panelOperaciones, BoxLayout.Y_AXIS));

        panelOperaciones.add(new JLabel("Comunidad de Clientes"));
        panelOperaciones.add(Box.createVerticalStrut(10));

        // --- LÓGICA DE ROL: Solo empleados pueden borrar clientes ---
        if (usuarioActual.getRol() == model.RolUsuario.EMPLEADO) {
            JTextArea info = new JTextArea("Para añadir nuevos clientes, \nutiliza la ventana de Registro.");
            info.setEditable(false);
            info.setOpaque(false);
            panelOperaciones.add(info);

            panelOperaciones.add(Box.createVerticalStrut(20));

            JButton btnEliminar = new JButton("Dar de baja (Borrar)");

            // Acción Eliminar Cliente
            btnEliminar.addActionListener(e -> {
                int fila = tablaCentral.getSelectedRow();
                if (fila == -1) {
                    JOptionPane.showMessageDialog(this, "Selecciona un cliente de la tabla.");
                    return;
                }
                int id = (int) modeloTabla.getValueAt(fila, 0);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "¿Seguro que quieres borrar al cliente con ID " + id
                                + "?\nSe borrarán también sus ventas asociadas.",
                        "Confirmar baja", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION && clienteDAO.eliminar(id)) {
                    JOptionPane.showMessageDialog(this, "Cliente eliminado correctamente.");
                    cargarModuloClientes(); // Refrescar tabla
                }
            });

            panelOperaciones.add(btnEliminar);

        } else {
            // --- VISTA DEL CLIENTE ---
            JTextArea info = new JTextArea(
                    "Aquí puedes ver a otros \njugadores de la tienda y sus \nplataformas preferidas.");
            info.setEditable(false);
            info.setOpaque(false);
            panelOperaciones.add(info);
        }

        panelOperaciones.revalidate();
        panelOperaciones.repaint();
    }
}
