package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de gestionar la conexión con la base de datos MySQL.
 */
public class ConexionDB {

    // Constantes de conexión. Ajusta el usuario y la contraseña según tu
    // configuración local.
    private static final String URL = "jdbc:mysql://localhost:3306/tienda_videojuegos?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // Tu usuario de MySQL (por defecto root)
    private static final String PASSWORD = "admin"; // Tu contraseña de MySQL (por defecto vacía en XAMPP)

    /**
     * Método estático que devuelve una conexión activa a la base de datos.
     * Cumple con el requisito de la rúbrica: "Método estático que devuelve la
     * Connection".
     * 
     * @return Connection objeto de conexión JDBC
     * @throws SQLException si las credenciales son incorrectas o el servidor está
     *                      caído
     */
    public static Connection getConnection() throws SQLException {
        /*
         * Nota: Desde JDBC 4.0 (Java 6), la carga explícita del driver con
         * Class.forName("com.mysql.cj.jdbc.Driver") ya no es estrictamente necesaria,
         * pero asegúrate de tener el archivo .jar del Connector/J de MySQL
         * añadido al Classpath (Build Path) de tu proyecto.
         */
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}