package dao;

import model.Videojuego;
import java.util.List;

public interface VideojuegoDAO {
    boolean insertar(Videojuego videojuego);

    List<Videojuego> listarTodos();

    boolean actualizar(Videojuego videojuego);

    boolean eliminar(int idVideojuego);

    Videojuego buscarPorId(int id);
}