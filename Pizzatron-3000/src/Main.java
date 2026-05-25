
import model.*;
import controller.*;
import view.*;

public class Main {

    public static void main(String[] args) {
        Cocina cocina = new Cocina();
        OrdenController ordenCtrl = new OrdenController(cocina);
        PizzaController pizzaCtrl = new PizzaController();
        PuntuacionController puntosCtrl = new PuntuacionController();

        // Swing debe arrancar en el Event Dispatch Thread
        javax.swing.SwingUtilities.invokeLater(()
                -> new GameView(ordenCtrl, pizzaCtrl, puntosCtrl)
        );
    }
}
