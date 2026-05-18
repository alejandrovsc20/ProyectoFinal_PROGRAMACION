Proyecto Final Programación: GameStore Management

Centro: IES Francisco Ayala - Granada

1. Descripción general del proyecto
Este proyecto consiste en una aplicación de escritorio desarrollada en Java para la gestión integral de una Tienda de Videojuegos.

Objetivo de la aplicación:
Proporcionar una herramienta visual, segura y robusta que permita a los empleados administrar el catálogo de videojuegos y registrar las ventas, mientras que los clientes pueden consultar el inventario y su historial de compras. El sistema cuenta con control de acceso basado en roles (RBAC) para garantizar que los usuarios cliente no puedan alterar el inventario ni dar de baja a otros usuarios.

2. Arquitectura y estructura del proyecto
El proyecto sigue estrictamente el Patrón de Arquitectura MVC (Modelo-Vista-Controlador) de manera pragmática y el patrón de persistencia DAO (Data Access Object), garantizando que la interfaz gráfica esté totalmente desacoplada de la base de datos (0% de código SQL en las vistas).

Estructura de paquetes implementada:

db/: Contiene la clase ConexionDB.java que gestiona la conexión a MySQL mediante el patrón Singleton.

model/: Contiene las entidades del dominio representadas como POJOs (Usuario, Cliente, Empleado, Videojuego, Venta). Cabe destacar que los atributos de fecha (como la fecha de contratación o de compra) se han tipado como String en el modelo de Java para agilizar el flujo de datos desde los componentes JTextField de Swing, asegurando su correcto parseo antes de la persistencia.

dao/: Contiene las interfaces y sus respectivas implementaciones (ej. UsuarioDAOImpl), donde se ubican las sentencias PreparedStatement y el control manual de transacciones (Commit/Rollback).

dto/: Contiene VentaDTO.java, utilizado para transferir datos complejos generados a partir de sentencias INNER JOIN (cruzando Ventas, Usuarios y Videojuegos) hacia las tablas de la interfaz.

view/: Contiene las interfaces gráficas desarrolladas con Java Swing (Login, FormularioRegistro, Principal), diseñadas con un aspecto moderno ("Dark Mode") y con componentes dinámicos.

3. Modelo de base de datos
El modelo relacional se ha diseñado garantizando la integridad de los datos mediante claves foráneas y la restricción ON DELETE CASCADE. Destaca la implementación del patrón Joined Table Inheritance para la gestión de usuarios.

Tabla raíz (usuarios): Almacena las credenciales y datos personales comunes (ID, username, password, email, nombre, apellidos, dni, rol).

Tablas hijas (clientes y empleados): Extienden de usuarios utilizando el id_usuario como Clave Primaria y Foránea simultáneamente. La inserción de un empleado o cliente se realiza en una única transacción atómica con rollback en caso de fallo.

Entidad principal (videojuegos): Almacena el catálogo (ID, título, género, plataforma, precio, stock, multijugador).

Tabla N:M (ventas): Relaciona clientes y videojuegos, guardando datos históricos de la transacción (cantidad y precio en el momento de la venta).

Script SQL de la Base de Datos
SQL
DROP DATABASE IF EXISTS tienda_videojuegos;
CREATE DATABASE tienda_videojuegos;
USE tienda_videojuegos;

CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    nombre VARCHAR(50) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    dni VARCHAR(9) NOT NULL UNIQUE,
    rol ENUM('cliente', 'empleado') NOT NULL
);

CREATE TABLE clientes (
    id_usuario INT PRIMARY KEY,
    puntos_fidelidad INT DEFAULT 0,
    plataforma_preferida VARCHAR(50),
    CONSTRAINT fk_cliente_usuario FOREIGN KEY (id_usuario) 
        REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

CREATE TABLE empleados (
    id_usuario INT PRIMARY KEY,
    fecha_contratacion DATE NOT NULL,
    salario DECIMAL(8,2) NOT NULL,
    turno VARCHAR(20),
    CONSTRAINT fk_empleado_usuario FOREIGN KEY (id_usuario) 
        REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

CREATE TABLE videojuegos (
    id_videojuego INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    genero VARCHAR(50) NOT NULL,
    plataforma VARCHAR(50) NOT NULL,
    precio DECIMAL(6,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    multijugador BOOLEAN DEFAULT FALSE
);

CREATE TABLE ventas (
    id_venta INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_videojuego INT NOT NULL,
    fecha_compra DATETIME DEFAULT CURRENT_TIMESTAMP,
    cantidad INT NOT NULL DEFAULT 1,
    precio_historico DECIMAL(6,2) NOT NULL,
    CONSTRAINT fk_venta_cliente FOREIGN KEY (id_cliente) 
        REFERENCES clientes(id_usuario) ON DELETE CASCADE,
    CONSTRAINT fk_venta_videojuego FOREIGN KEY (id_videojuego) 
        REFERENCES videojuegos(id_videojuego) ON DELETE CASCADE
);

-- Inserción del administrador por defecto
INSERT INTO usuarios (username, password, email, nombre, apellidos, dni, rol) 
VALUES ('admin', '1234', 'admin@tienda.com', 'Admin', 'Root', '00000000A', 'empleado');
INSERT INTO empleados (id_usuario, fecha_contratacion, salario, turno) 
VALUES (1, '2026-01-01', 1500.00, 'Mañana');
4. Instrucciones de instalación y ejecución
Despliegue de la Base de Datos: Desplegar un contenedor Docker de MySQL en el puerto 3306 (o utilizar un servidor local como XAMPP/Workbench) y ejecutar el script SQL adjunto para generar las tablas y el usuario administrador.

Configuración de Credenciales: En el código fuente, dirigirse al paquete db y abrir la clase ConexionDB.java. Modificar las constantes USER y PASSWORD para que coincidan con las credenciales de tu servidor MySQL.

Dependencias: Asegurarse de tener importada en el Build Path / librerías del IDE la dependencia JDBC (mysql-connector-j.jar).

Ejecución: Ejecutar el archivo Main.java ubicado en la raíz de src/.

Acceso: Utilizar las credenciales por defecto (Usuario: admin, Contraseña: 1234) para acceder con rol de empleado.

5. Control de Versiones (GitHub)
El desarrollo del proyecto se ha realizado mediante un control de versiones continuo, con commits descriptivos que reflejan la evolución de las distintas capas de la arquitectura.

🔗 Enlace al repositorio público: [Sustituye este texto por el enlace a tu repositorio de GitHub]

6. Registro de Tiempo (WakaTime)
Se acredita la dedicación y el esfuerzo en el desarrollo de la aplicación superando el mínimo de 10 horas de programación requeridas, bajo el nombre de proyecto "ProyectoFinal-Programacion".

📷 Dashboard Público / Captura de horas:
[Pega aquí la imagen (captura de pantalla) de tu gráfica de WakaTime]

7. Extensiones Implementadas
Gestión dinámica e inteligente de UX/UI: La aplicación implementa un diseño oscuro (Dark Mode) utilizando el Look & Feel nativo del sistema, alterando la paleta de colores de los componentes Swing para una visualización más ergonómica.

Asignación de precios automatizada (Lógica de Negocio Automática): En el módulo de ventas, el empleado no necesita teclear el importe a cobrar; el sistema realiza una consulta interna cruzada a la entidad Videojuego para extraer el precio real en ese instante temporal, multiplicarlo por la cantidad y guardarlo como precio_historico, previniendo errores humanos de facturación.