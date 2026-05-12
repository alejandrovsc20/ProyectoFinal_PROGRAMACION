package dao;

import db.ConexionDB;
import model.Videojuego;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VideojuegoDAOimpl implements VideojuegoDAO {

    @Override
    public boolean insertar(Videojuego v) {
        String sql = "INSERT INTO videojuegos (titulo, genero, plataforma, precio, stock, multijugador) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, v.getTitulo());
            pstmt.setString(2, v.getGenero());
            pstmt.setString(3, v.getPlataforma());
            pstmt.setDouble(4, v.getPrecio());
            pstmt.setInt(5, v.getStock());
            pstmt.setBoolean(6, v.isMultijugador());

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar videojuego: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Videojuego> listarTodos() {
        List<Videojuego> lista = new ArrayList<>();
        String sql = "SELECT * FROM videojuegos";

        try (Connection conn = ConexionDB.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Videojuego v = new Videojuego(
                        rs.getInt("id_videojuego"),
                        rs.getString("titulo"),
                        rs.getString("genero"),
                        rs.getString("plataforma"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        rs.getBoolean("multijugador"));
                lista.add(v);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar videojuegos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Videojuego v) {
        String sql = "UPDATE videojuegos SET titulo=?, genero=?, plataforma=?, precio=?, stock=?, multijugador=? WHERE id_videojuego=?";
        try (Connection conn = ConexionDB.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, v.getTitulo());
            pstmt.setString(2, v.getGenero());
            pstmt.setString(3, v.getPlataforma());
            pstmt.setDouble(4, v.getPrecio());
            pstmt.setInt(5, v.getStock());
            pstmt.setBoolean(6, v.isMultijugador());
            pstmt.setInt(7, v.getIdVideojuego());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar videojuego: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int idVideojuego) {
        String sql = "DELETE FROM videojuegos WHERE id_videojuego = ?";
        try (Connection conn = ConexionDB.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idVideojuego);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar videojuego: " + e.getMessage());
            return false;
        }
    }
}