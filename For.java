package arbol;
import java.util.LinkedList;

public class For implements Instruccion{
    ////#############VALORES DE FOR-------------------------
    private final Instruccion inicializador;
    private final Instruccion condicion;
    private final Instruccion incrementador;
    private final LinkedList<Instruccion> listaInstrucciones;
    ////#############VALORES DE FOR-------------------------
    
    public For(Instruccion a, Instruccion b, Instruccion c, LinkedList<Instruccion> d) {
        inicializador=a;
        condicion=b;
        incrementador=c;
        listaInstrucciones=d;
    }
    
    @Override
    public Object ejecutar(TablaDeSimbolos ts,Arbol ar) {
        TablaDeSimbolos tablaLocal=new TablaDeSimbolos();
        tablaLocal.addAll(ts);
        inicializador.ejecutar(tablaLocal,ar);
        Object tst = condicion.ejecutar(tablaLocal, ar);
        if(!(tst instanceof Boolean)){
            ar.addError("ERROR EN CONDICION DE FOR, DEBE SER BOOLEANA");
            System.out.println("CONDICION DEBE SER BOOLEANA");
            return null;
        }
        LinkedList<Instruccion> list_instr;
        while((Boolean)tst){
            incrementador.ejecutar(tablaLocal,ar);
            list_instr=(LinkedList<Instruccion>)listaInstrucciones.clone();
            for(Instruccion ins:list_instr){
                Object r;
                r=ins.ejecutar(tablaLocal,ar);
                if(r!=null){
                    if(r instanceof Break){
                        return null;
                    }else if(r instanceof Siga){
                        break;
                    }else{
                        return r;
                    }
                }
            }
            tst = condicion.ejecutar(tablaLocal, ar);
            if(!(tst instanceof Boolean)){
                ar.addError("ERROR EN CONDICION DE FOR, DEBE SER BOOLEANA");
                System.out.println("CONDICION DEBE SER BOOLEANA");
                return null;
            }
        }
        return null;
    }   
}