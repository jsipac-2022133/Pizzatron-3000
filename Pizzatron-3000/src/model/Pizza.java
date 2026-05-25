package model;

import java.util.Stack;

/**
 * Usa Stack<Ingrediente> (LIFO):
 * push() -> agregar ingrediente
 * pop() -> deshacer el ultimo ingrediente agregado
 */
public class Pizza {
    private Stack<Ingrediente> pilaIngredientes;

    public Pizza() {
        this.pilaIngredientes = new Stack<>();
    }

    /** PUSH: agrega un ingrediente al tope de la pila */
    public void pushIngrediente(Ingrediente i) {
        pilaIngredientes.push(i);
    }

    /** POP: quita y retorna el ultimo ingrediente (deshacer) */
    public Ingrediente popIngrediente() {
        if (pilaIngredientes.isEmpty())
            return null;
        return pilaIngredientes.pop();
    }

    public Ingrediente peekIngrediente() {
        if (pilaIngredientes.isEmpty())
            return null;
        return pilaIngredientes.peek();
    }

    public Stack<Ingrediente> getPilaIngredientes() {
        return pilaIngredientes;
    }

    public boolean estaVacia() {
        return pilaIngredientes.isEmpty();
    }

    public void armar() {
        Stack<Ingrediente> temp = new Stack<>();
        while (!pilaIngredientes.isEmpty())
            temp.push(pilaIngredientes.pop());
        while (!temp.isEmpty()) {
            Ingrediente ing = temp.pop();
            ing.preparar();
            pilaIngredientes.push(ing);
        }
    }

    @Override
    public String toString() {
        if (pilaIngredientes.isEmpty())
            return "  (pizza vacia)\n";
        StringBuilder sb = new StringBuilder();
        Object[] items = pilaIngredientes.toArray();
        for (int i = items.length - 1; i >= 0; i--) {
            sb.append("  ").append(items.length - i).append(". ")
                    .append(items[i].toString());
            if (i == items.length - 1)
                sb.append("  <-- TOPE");
            sb.append("\n");
        }
        return sb.toString();
    }
}