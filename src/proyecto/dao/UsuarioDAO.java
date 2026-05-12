package dao;

import model.Usuario;
import model.Cliente;

public interface UsuarioDAO {
    Usuario validarLogin(String username, String password);

    boolean registrarCliente (Cliente cliente);
}
