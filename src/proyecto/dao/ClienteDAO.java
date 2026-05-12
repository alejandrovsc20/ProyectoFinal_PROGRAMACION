package dao;

import model.Cliente;
import java.util.List;

public interface ClienteDAO {
    List<Cliente> listarTodos();

    boolean eliminar(int idCliente);
}
