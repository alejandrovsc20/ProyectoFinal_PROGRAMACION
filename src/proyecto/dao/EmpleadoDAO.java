package dao;

import model.Empleado;
import java.util.List;

public interface EmpleadoDAO {
    List<Empleado> listarTodos();

    boolean eliminar(int idempleado);
}
