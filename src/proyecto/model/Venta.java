package model;

public class Venta {
    private int idVenta;
    private int idCliente;
    private int idVideojuego;
    private String fechaCompra;
    private int cantidad;
    private double precioHistorico;

    public Venta() {
    }

    public Venta(int idVenta, int idCliente, int idVideojuego, String fechaCompra, int cantidad,
            double precioHistorico) {
        this.idVenta = idVenta;
        this.idCliente = idCliente;
        this.idVideojuego = idVideojuego;
        this.fechaCompra = fechaCompra;
        this.cantidad = cantidad;
        this.precioHistorico = precioHistorico;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdVideojuego() {
        return idVideojuego;
    }

    public void setIdVideojuego(int idVideojuego) {
        this.idVideojuego = idVideojuego;
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

    public double getPrecioHistorico() {
        return precioHistorico;
    }

    public void setPrecioHistorico(double precioHistorico) {
        this.precioHistorico = precioHistorico;
    }

    


    
}
