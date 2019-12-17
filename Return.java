package arbol;

public class Return implements Instruccion{
    ////#############VALORES DE RETURN-------------------------
    private final Instruccion valorRetorno;
    ////#############VALORES DE RETURN-------------------------
    
    public Return(Instruccion a) {
        valorRetorno=a;
    }
    
    @Override
    public Object ejecutar(TablaDeSimbolos ts, Arbol ar) {
        if(valorRetorno==null){
            return null;
        }else{
            return valorRetorno.ejecutar(ts, ar);
        }
    }
    
}
