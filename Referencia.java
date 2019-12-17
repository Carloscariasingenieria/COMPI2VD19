package arbol;

public class Referencia implements Instruccion{
    ////#############VALORES DE REFERENCIA-------------------------
    private final String id;
    ////#############VALORES DE REFERENCIA-------------------------
    
    public Referencia(String identificador){
        this.id=identificador;
    }
    
    @Override
    public Object ejecutar(TablaDeSimbolos ts, Arbol ar) {
        return ts.getSimbol(id);
    }
    
}
