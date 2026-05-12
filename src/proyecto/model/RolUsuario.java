package model;

public enum RolUsuario {
    CLIENTE,
    EMPLEADO;

    // Método de utilidad útil para cuando leamos el texto de la base de datos
    public static RolUsuario desdeString(String rolDB) {
        if (rolDB == null)
            return null;
        return RolUsuario.valueOf(rolDB.toUpperCase());
    }
}
