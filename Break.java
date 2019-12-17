package arbol;

public class Break implements Instruccion{
    @Override
    public Object ejecutar(TablaDeSimbolos ts, Arbol ar) {
        return this;
    }
}
