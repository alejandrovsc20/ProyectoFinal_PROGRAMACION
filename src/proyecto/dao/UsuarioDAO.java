package dao;

import model.Usuario;
import model.Cliente;
import model.Empleado;

public interface UsuarioDAO {
    Usuario validarLogin(String username, String password);

    boolean registrarCliente (Cliente cliente);

    boolean registrarEmpleado(Empleado empleado);

}
