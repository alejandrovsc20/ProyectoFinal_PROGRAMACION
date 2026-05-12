package model;

public class Empleado extends Usuario {

    private String fechaContratacion;
    private double salario;
    private String turno;

    public Empleado() {
        super();
        this.rol = RolUsuario.EMPLEADO;
    }

    public Empleado(int idUsuario, String username, String password, String email, String nombre, String apellidos,
            String dni, RolUsuario rol, String fechaContratacion, double salario, String turno) {
        super(idUsuario, username, password, email, nombre, apellidos, dni, rol);
        this.fechaContratacion = fechaContratacion;
        this.salario = salario;
        this.turno = turno;
    }

    public String getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(String fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    

    

}
