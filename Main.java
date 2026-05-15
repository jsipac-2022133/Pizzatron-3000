import model.*;
import controller.*;
import view.*;


public class Main {

    public static void main(String[] args) {
        Cocina cocina = new Cocina();
        OrdenController ordenCtrl = new OrdenController(cocina);
        PizzaController pizzaCtrl = new PizzaController();
        PuntuacionController puntosCtrl = new PuntuacionController();
        GameView view = new GameView();

        view.mostrarBienvenida();

        boolean corriendo = true;

        while (corriendo) {
            view.mostrarPuntuacion(puntosCtrl.getPuntos(), puntosCtrl.getErrores());
            int opcion = view.mostrarMenuPrincipal();

            switch (opcion) {

                // 1. Generar ordenes
                case 1:
                    int n = view.pedirCantidadOrdenes();
                    view.mostrarMensaje("Generando " + n + " orden(es)...");
                    ordenCtrl.generarOrdenes(n);
                    view.mostrarMensaje("Ordenes en cola: " + cocina.getTamano());
                    break;

                // 2. Tomar siguiente orden de la cola
                case 2:
                    if (ordenCtrl.hayOrdenActual()) {
                        view.mostrarError("Ya hay una orden activa. Enviala primero.");
                        view.mostrarOrdenActual(ordenCtrl.getOrdenActual());
                    } else {
                        boolean tomada = ordenCtrl.tomarSiguienteOrden();
                        if (tomada) {
                            view.mostrarMensaje("Orden tomada!");
                            view.mostrarOrdenActual(ordenCtrl.getOrdenActual());
                        } else {
                            view.mostrarError("Cola vacia. Genera ordenes primero (opcion 1).");
                        }
                    }
                    break;

                // 3. Armar pizza
                case 3:
                    if (!ordenCtrl.hayOrdenActual()) {
                        view.mostrarError("Toma una orden primero (opcion 2).");
                        break;
                    }
                    menuArmarPizza(pizzaCtrl, ordenCtrl, puntosCtrl, view);
                    break;

                // 4. Ver orden actual
                case 4:
                    view.mostrarOrdenActual(ordenCtrl.getOrdenActual());
                    break;

                // 5. Ver cola
                case 5:
                    view.mostrarCola(cocina);
                    break;

                // 6. Historial de resultados
                case 6:
                    view.mostrarMensaje("--- HISTORIAL DE RESULTADOS ---");
                    puntosCtrl.mostrarHistorial();
                    break;

                // 7. Resetear
                case 7:
                    puntosCtrl.resetear();
                    pizzaCtrl.iniciarNuevaPizza();
                    view.mostrarMensaje("Puntuacion y pizza reseteadas.");
                    break;

                // 8. Salir
                case 8:
                    corriendo = false;
                    view.mostrarMensaje("Hasta luego, pinguino chef!");
                    break;

                default:
                    view.mostrarError("Opcion invalida. Ingresa un numero del 1 al 8.");
            }
        }

        view.cerrar();
    }

    // ── Sub-menu: armar pizza ─────────────────────────────────────────────
    private static void menuArmarPizza(
            PizzaController pizzaCtrl,
            OrdenController ordenCtrl,
            PuntuacionController puntosCtrl,
            GameView view) {

        boolean armando = true;

        while (armando) {
            view.mostrarPizzaActual(pizzaCtrl.getPizzaActual());
            int op = view.mostrarMenuIngredientes();

            switch (op) {

                // Bases
                case 1:
                    pizzaCtrl.agregarIngrediente(new Base(TipoBase.DELGADA));
                    view.mostrarMensaje("  [PUSH] Base Delgada agregada.");
                    break;
                case 2:
                    pizzaCtrl.agregarIngrediente(new Base(TipoBase.GRUESA));
                    view.mostrarMensaje("  [PUSH] Base Gruesa agregada.");
                    break;

                // Salsas
                case 3:
                    pizzaCtrl.agregarIngrediente(new Salsa(false));
                    view.mostrarMensaje("  [PUSH] Salsa Normal agregada.");
                    break;
                case 4:
                    pizzaCtrl.agregarIngrediente(new Salsa(true));
                    view.mostrarMensaje("  [PUSH] Salsa Picante agregada.");
                    break;

                // Toppings
                case 5:
                    pizzaCtrl.agregarIngrediente(new Topping(TipoTopping.PESCADO));
                    view.mostrarMensaje("  [PUSH] Pescado agregado.");
                    break;
                case 6:
                    pizzaCtrl.agregarIngrediente(new Topping(TipoTopping.CAMARONES));
                    view.mostrarMensaje("  [PUSH] Camarones agregados.");
                    break;
                case 7:
                    pizzaCtrl.agregarIngrediente(new Topping(TipoTopping.SQUIDS));
                    view.mostrarMensaje("  [PUSH] Squids agregados.");
                    break;
                case 8:
                    pizzaCtrl.agregarIngrediente(new Topping(TipoTopping.ALGA));
                    view.mostrarMensaje("  [PUSH] Alga agregada.");
                    break;
                case 9:
                    pizzaCtrl.agregarIngrediente(new Topping(TipoTopping.HIELO));
                    view.mostrarMensaje("  [PUSH] Hielo agregado.");
                    break;

                // Deshacer (POP)
                case 10:
                    Ingrediente quitado = pizzaCtrl.deshacerUltimo();
                    if (quitado == null)
                        view.mostrarError("La pila esta vacia, nada que deshacer.");
                    else
                        view.mostrarMensaje("  [POP] Se quito: " + quitado.getNombre());
                    break;

                // Ver pizza actual
                case 11:
                    view.mostrarPizzaActual(pizzaCtrl.getPizzaActual());
                    break;

                // Enviar pizza
                case 12:
                    if (pizzaCtrl.pizzaEstaVacia()) {
                        view.mostrarError("No puedes enviar una pizza vacia.");
                        break;
                    }
                    Pizza enviada = pizzaCtrl.enviarPizza();
                    String resultado = puntosCtrl.compararPizzas(
                            enviada,
                            ordenCtrl.getOrdenActual().getPizzaObjetivo());
                    view.mostrarResultado(resultado);
                    ordenCtrl.completarOrdenActual();
                    armando = false;
                    break;

                // Volver
                case 0:
                    armando = false;
                    break;

                default:
                    view.mostrarError("Opcion invalida.");
            }
        }
    }
}