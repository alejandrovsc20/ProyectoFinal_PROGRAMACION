import view.Login;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        // Activar el diseño nativo del Sistema Operativo en lugar de Nimbus
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo aplicar el tema del sistema: " + e.getMessage());
        }

        // Lanzar la aplicación
        java.awt.EventQueue.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}