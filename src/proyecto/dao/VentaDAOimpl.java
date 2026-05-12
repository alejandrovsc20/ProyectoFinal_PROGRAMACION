package dao;

import db.ConexionDB;
import model.Venta;
import dto.VentaDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VentaDAOimpl implements VentaDAO {

    @Override
    public boolean insertar(Venta venta) {
        String sql = "INSERT INTO ventas (id_cliente, id_videojuego, fecha_compra, cantidad, precio_historico) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, venta.getIdCliente());
            pstmt.setInt(2, venta.getIdVideojuego());
            pstmt.setString(3, venta.getFechaCompra());
            pstmt.setInt(4, venta.getCantidad());
            pstmt.setDouble(5, venta.getPrecioHistorico());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar venta: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<VentaDTO> listarTodasConDetalles() {
        List<VentaDTO> lista = new ArrayList<>();
        // Hacemos JOIN entre ventas, usuarios (para el nombre) y videojuegos (para el
        // título)
        String sql = "SELECT v.id_venta, u.username, vid.titulo, v.fecha_compra, v.cantidad, v.precio_historico " +
                "FROM ventas v " +
                "INNER JOIN usuarios u ON v.id_cliente = u.id_usuario " +
                "INNER JOIN videojuegos vid ON v.id_videojuego = vid.id_videojuego " +
                "ORDER BY v.fecha_compra DESC";

        try (Connection conn = ConexionDB.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                VentaDTO dto = new VentaDTO();
                dto.setIdVenta(rs.getInt("id_venta"));
                dto.setNombreCliente(rs.getString("username"));
                dto.setTituloVideojuego(rs.getString("titulo"));
                dto.setFechaCompra(rs.getString("fecha_compra"));
                dto.setCantidad(rs.getInt("cantidad"));

                // Calculamos el total (Cantidad * Precio)
                double precioUnitario = rs.getDouble("precio_historico");
                dto.setPrecioTotal(dto.getCantidad() * precioUnitario);

                lista.add(dto);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar ventas con JOIN: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean eliminar(int idVenta) {
        String sql = "DELETE FROM ventas WHERE id_venta = ?";
        try (Connection conn = ConexionDB.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idVenta);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar venta: " + e.getMessage());
            return false;
        }
    }
}