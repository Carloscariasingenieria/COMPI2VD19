package arbol;
import java.util.LinkedList;

public class Dim {
    protected LinkedList<Instruccion> lista_dim;
    protected String tipo;
    public Dim(LinkedList<Instruccion> lista, String tipo){
        this.lista_dim = lista;
        this.tipo=tipo;
    }
    LinkedList<Instruccion> getLista(){
        return this.lista_dim;
    }
    
    String getTipo(){
        return this.tipo;
    }
}
