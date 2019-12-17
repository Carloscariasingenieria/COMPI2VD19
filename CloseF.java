package arbol;

public class CloseF implements Instruccion{
    @Override
    public Object ejecutar(TablaDeSimbolos ts, Arbol ar) {
        ar.CloseFichero();
        return null;
    }
    
}
