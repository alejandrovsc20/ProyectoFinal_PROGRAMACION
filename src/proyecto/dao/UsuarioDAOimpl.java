package dao;

import db.ConexionDB;
import model.Cliente;
import model.Empleado;
import model.RolUsuario;
import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsuarioDAOimpl implements UsuarioDAO {

    @Override
    public Usuario validarLogin(String username, String password) {
        // Usamos una consulta sencilla. Si quisiéramos todos los datos del cliente, haríamos un JOIN.
        String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ?";
        
        // El try-with-resources cierra automáticamente el Connection, PreparedStatement y ResultSet
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Creamos un objeto Cliente vacío solo para guardar los datos base por ahora
                    Cliente usuarioLogueado = new Cliente();
                    usuarioLogueado.setIdUsuario(rs.getInt("id_usuario"));
                    usuarioLogueado.setUsername(rs.getString("username"));
                    usuarioLogueado.setNombre(rs.getString("nombre"));
                    usuarioLogueado.setRol(RolUsuario.desdeString(rs.getString("rol")));
                    
                    return usuarioLogueado; // Login exitoso
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en el login: " + e.getMessage());
        }
        return null; // Login fallido
    }

    @Override
    public boolean registrarCliente(Cliente cliente) {
        String sqlUsuario = "INSERT INTO usuarios (username, password, email, nombre, apellidos, dni, rol) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlCliente = "INSERT INTO clientes (id_usuario, puntos_fidelidad, plataforma_preferida) VALUES (?, ?, ?)";
        
        Connection conn = null;
        
        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false);
            
            int idUsuarioGenerado = -1;
            
            // 2. INSERTAMOS EN LA TABLA PADRE (usuarios)
            // Usamos Statement.RETURN_GENERATED_KEYS para recuperar el ID que MySQL le asigne
            try (PreparedStatement pstmtUser = conn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {
                pstmtUser.setString(1, cliente.getUsername());
                pstmtUser.setString(2, cliente.getPassword());
                pstmtUser.setString(3, cliente.getEmail());
                pstmtUser.setString(4, cliente.getNombre());
                pstmtUser.setString(5, cliente.getApellidos());
                pstmtUser.setString(6, cliente.getDni());
                pstmtUser.setString(7, cliente.getRol().name().toLowerCase());
                
                pstmtUser.executeUpdate();
                
                try (ResultSet rs = pstmtUser.getGeneratedKeys()) {
                    if (rs.next()) {
                        idUsuarioGenerado = rs.getInt(1);
                    }
                }
            }
            
            // 3. INSERTAMOS EN LA TABLA HIJA (clientes) usando el ID recuperado
            if (idUsuarioGenerado != -1) {
                try (PreparedStatement pstmtCli = conn.prepareStatement(sqlCliente)) {
                    pstmtCli.setInt(1, idUsuarioGenerado);
                    pstmtCli.setInt(2, cliente.getPuntosFidelidad());
                    pstmtCli.setString(3, cliente.getPlataformaPreferida());
                    pstmtCli.executeUpdate();
                }
            } else {
                throw new SQLException("No se pudo obtener el ID del usuario generado.");
            }
            
            // 4. SI TODO FUE BIEN, CONFIRMAMOS LA TRANSACCIÓN
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error al registrar cliente. Haciendo ROLLBACK... " + e.getMessage());
            try {
                if (conn != null) {
                    // 5. SI ALGO FALLÓ, REVERTIMOS TODOS LOS CAMBIOS
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Error grave en el rollback: " + ex.getMessage());
            }
            return false;
        } finally {
            // 6. VOLVEMOS A DEJAR LA CONEXIÓN EN SU ESTADO ORIGINAL
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error cerrando la conexión: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean registrarEmpleado(Empleado empleado) {
        String sqlUsuario = "INSERT INTO usuarios (username, password, email, nombre, apellidos, dni, rol) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlEmpleado = "INSERT INTO empleados (id_usuario, fecha_contratacion, salario, turno) VALUES (?, ?, ?, ?)";
        
        Connection conn = null;
        
        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false);
            
            int idUsuarioGenerado = -1;
            
            try (PreparedStatement pstmtUser = conn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {
                pstmtUser.setString(1, empleado.getUsername());
                pstmtUser.setString(2, empleado.getPassword());
                pstmtUser.setString(3, empleado.getEmail());
                pstmtUser.setString(4, empleado.getNombre());
                pstmtUser.setString(5, empleado.getApellidos());
                pstmtUser.setString(6, empleado.getDni());
                pstmtUser.setString(7, empleado.getRol().name().toLowerCase());
                
                pstmtUser.executeUpdate();
                
                try (ResultSet rs = pstmtUser.getGeneratedKeys()) {
                    if (rs.next()) {
                        idUsuarioGenerado = rs.getInt(1);
                    }
                }
            }
            
            if (idUsuarioGenerado != -1) {
                try (PreparedStatement pstmtEmp = conn.prepareStatement(sqlEmpleado)) {
                    pstmtEmp.setInt(1, idUsuarioGenerado);
                    pstmtEmp.setString(2, empleado.getFechaContratacion());
                    pstmtEmp.setDouble(3, empleado.getSalario());
                    pstmtEmp.setString(4, empleado.getTurno());
                    pstmtEmp.executeUpdate();
                }
            } else {
                throw new SQLException("No se pudo obtener el ID del usuario generado.");
            }
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error al registrar empleado. Haciendo ROLLBACK... " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Error grave en el rollback: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error cerrando la conexión: " + e.getMessage());
            }
        }
    }
}