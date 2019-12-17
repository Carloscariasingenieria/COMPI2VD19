package arbol;
import java.util.LinkedList;

public class DoWhile implements Instruccion{
    ////#############VALORES DE REFERENCIA-------------------------
    private final Instruccion condicion;
    private final LinkedList<Instruccion> listaInstrucciones;
    ////#############VALORES DE REFERENCIA-------------------------
    
    //CONSTRUCTOR
    public DoWhile(Instruccion a, LinkedList<Instruccion> b) {
        this.condicion=a;
        this.listaInstrucciones=b;
    }
    
    @Override
    public Object ejecutar(TablaDeSimbolos ts,Arbol ar) {
        TablaDeSimbolos tablaLocal=new TablaDeSimbolos();
        tablaLocal.addAll(ts);
        Object tst;
        LinkedList<Instruccion> list_instr;
        do{
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
            tst= condicion.ejecutar(tablaLocal, ar);
            if(!(tst instanceof Boolean)){
                ar.addError("ERROR EN CONDICION DE REPEAT, DEBE SER BOOLEANA");
                System.out.println("CONDICION DEBE SER BOOLEANA DO WHILE");
                return null;
            }
        }while((Boolean)tst);
        return null;
    }   
}