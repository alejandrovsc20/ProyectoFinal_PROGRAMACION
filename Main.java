import view.Login;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        // Opcional: Poner el diseño del sistema operativo (Look & Feel)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Lanzar la aplicación en el hilo de despacho de eventos de Swing
        java.awt.EventQueue.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}