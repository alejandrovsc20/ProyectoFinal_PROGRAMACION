package dao;

import model.Venta;
import dto.VentaDTO;
import java.util.List;

public interface VentaDAO {
    boolean insertar(Venta venta);

    List<VentaDTO> listarTodasConDetalles(); // Este es el método clave para el JOIN

    boolean eliminar(int idVenta);
}