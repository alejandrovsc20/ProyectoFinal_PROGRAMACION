package dto;

public class VentaDTO {
    private int idVenta;
    private String nombreCliente;
    private String tituloVideojuego;
    private String fechaCompra;
    private int cantidad;
    private double precioTotal; // Cantidad * precio histórico

    public VentaDTO() {
    }

    public VentaDTO(int idVenta, String nombreCliente, String tituloVideojuego, String fechaCompra, int cantidad,
            double precioTotal) {
        this.idVenta = idVenta;
        this.nombreCliente = nombreCliente;
        this.tituloVideojuego = tituloVideojuego;
        this.fechaCompra = fechaCompra;
        this.cantidad = cantidad;
        this.precioTotal = precioTotal;
    }

    // Getters y Setters
    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getTituloVideojuego() {
        return tituloVideojuego;
    }

    public void setTituloVideojuego(String tituloVideojuego) {
        this.tituloVideojuego = tituloVideojuego;
    }

    public String getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(String fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }
}
