package model;

public class Cliente extends Usuario {
    private int puntosFidelidad;
    private String plataformaPreferida;

    public Cliente() {
        super();
        this.rol = RolUsuario.CLIENTE;
    }

    public Cliente(int idUsuario, String username, String password, String email, String nombre, String apellidos,
            String dni, RolUsuario rol, int puntosFidelidad, String plataformaPreferida) {
        super(idUsuario, username, password, email, nombre, apellidos, dni, rol);
        this.puntosFidelidad = puntosFidelidad;
        this.plataformaPreferida = plataformaPreferida;
    }

    

    public int getPuntosFidelidad() {
        return puntosFidelidad;
    }

    public void setPuntosFidelidad(int puntosFidelidad) {
        this.puntosFidelidad = puntosFidelidad;
    }

    public String getPlataformaPreferida() {
        return plataformaPreferida;
    }

    public void setPlataformaPreferida(String plataformaPreferida) {
        this.plataformaPreferida = plataformaPreferida;
    }

}
