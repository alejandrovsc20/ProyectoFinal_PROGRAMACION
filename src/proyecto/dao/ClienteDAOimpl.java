package dao;

import db.ConexionDB;
import model.Cliente;
import model.RolUsuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOimpl implements ClienteDAO {

    @Override
    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.username, u.password, u.email, u.nombre, u.apellidos, u.dni, c.puntos_fidelidad, c.plataforma_preferida "
                +
                "FROM usuarios u " +
                "INNER JOIN clientes c ON u.id_usuario = c.id_usuario";

        try (Connection conn = ConexionDB.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Cliente c = new Cliente(
                        rs.getInt("id_usuario"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("dni"),
                        RolUsuario.CLIENTE,
                        rs.getInt("puntos_fidelidad"),
                        rs.getString("plataforma_preferida"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean eliminar(int idCliente) {
        // Al eliminar de la tabla usuarios, el ON DELETE CASCADE borrará
        // automáticamente el registro en la tabla clientes
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (Connection conn = ConexionDB.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCliente);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }
}
