package arbol;
import java.util.LinkedList;

public class SubIf implements Instruccion{
    ////#############VALORES DE SUBIF-------------------------
    private Boolean valorCondicion;
    private final boolean isElse;
    private final Instruccion condicion;
    ////#############VALORES DE SUBIF-------------------------
    
    private final LinkedList<Instruccion> listaInstrucciones;
    
    //IF O ELSE IF
    public SubIf(Instruccion ins, LinkedList<Instruccion> lins) {
        condicion=ins;
        listaInstrucciones=lins;
        isElse=false;
    }
    
    //ELSE
    public SubIf(LinkedList<Instruccion> lins) {
        condicion=null;
        listaInstrucciones=lins;
        isElse=true;
    }
    
    //Obtener condicion
    public Boolean getValorCondicion() {
        try{
            return valorCondicion || isElse;
        }catch(Exception e){
            return null;
        }
    }
    
    @Override
    public Object ejecutar(TablaDeSimbolos ts,Arbol ar) {
        Object res=null;
        if(condicion!=null){
            res= condicion.ejecutar(ts, ar);
            if(!(res instanceof Boolean)){
                System.out.println("LA CONDICION DEBE SER BOOLEANA");
                return null;
            }
        }
        valorCondicion=((res==null)?false:(Boolean)res);
        if(valorCondicion || isElse){
            TablaDeSimbolos tablaLocal=new TablaDeSimbolos();
            tablaLocal.addAll(ts);
            for(Instruccion in: listaInstrucciones){
                Object r;
                r=in.ejecutar(tablaLocal,ar);
                if(r!=null){
                    return r;
                }
            }
        }
        return null;
    }
}
