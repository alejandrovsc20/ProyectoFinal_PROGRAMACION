package dao;

import db.ConexionDB;
import model.Empleado;
import model.RolUsuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAOimpl implements EmpleadoDAO {

    @Override
    public List<Empleado> listarTodos() {
        List<Empleado> lista = new ArrayList<>();
        // Hacemos INNER JOIN para traer los datos comunes de usuario y los específicos
        // de empleado
        String sql = "SELECT u.id_usuario, u.username, u.password, u.email, u.nombre, u.apellidos, u.dni, " +
                "e.fecha_contratacion, e.salario, e.turno " +
                "FROM usuarios u " +
                "INNER JOIN empleados e ON u.id_usuario = e.id_usuario";

        try (Connection conn = ConexionDB.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Empleado emp = new Empleado(
                        rs.getInt("id_usuario"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("dni"),
                        RolUsuario.EMPLEADO,
                        rs.getString("fecha_contratacion"), // Recuerda que lo dejamos como String
                        rs.getDouble("salario"),
                        rs.getString("turno"));
                lista.add(emp);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar empleados: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean eliminar(int idEmpleado) {
        // Al igual que con el cliente, borramos de 'usuarios' y el ON DELETE CASCADE se
        // encarga de 'empleados'
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (Connection conn = ConexionDB.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idEmpleado);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar empleado: " + e.getMessage());
            return false;
        }
    }
}