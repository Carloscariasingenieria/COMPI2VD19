package arbol;

public class Siga implements Instruccion{
    @Override
    public Object ejecutar(TablaDeSimbolos ts, Arbol ar) {
        return this;
    }
}
