package arbol;

import java.util.LinkedList;

public class SubCase implements Instruccion{
    ////#############VALORES DE SUBIF-------------------------
    private final boolean isDefault;
    private final Instruccion comparador;
    ////#############VALORES DE SUBIF-------------------------
    
    private final LinkedList<Instruccion> listaInstrucciones;
    
    //CASE
    public SubCase(Instruccion ins, LinkedList<Instruccion> lins) {
        comparador=ins;
        listaInstrucciones=lins;
        isDefault=false;
    }
    
    //DEFAULT
    public SubCase(LinkedList<Instruccion> lins) {
        comparador=null;
        listaInstrucciones=lins;
        isDefault=true;
    }
    
    //Obtener condicion
    public Boolean getisDefault() {
        return isDefault;
    }
    
    //Obtener Lista de Instrucciones
    public LinkedList<Instruccion> getInstrucciones() {
        return listaInstrucciones;
    }
    
    @Override
    public Object ejecutar(TablaDeSimbolos ts,Arbol ar) {
        Object res=null;
        if(comparador!=null){
            res= comparador.ejecutar(ts, ar);
        }
        return res;
    }
    
}
