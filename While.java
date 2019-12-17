package arbol;
import java.util.LinkedList;

public class While implements Instruccion{
    ////#############VALORES DE WHILE-------------------------
    private final Instruccion condicion;
    private final LinkedList<Instruccion> listaInstrucciones;
    ////#############VALORES DE WHILE-------------------------
    
    //SENTENCIA WHILE
    public While(Instruccion a, LinkedList<Instruccion> b) {
        this.condicion=a;
        this.listaInstrucciones=b;
    }
   
    @Override
    public Object ejecutar(TablaDeSimbolos ts,Arbol ar) {
        TablaDeSimbolos tablaLocal=new TablaDeSimbolos();
        tablaLocal.addAll(ts);
        Object tst = condicion.ejecutar(tablaLocal, ar);
        if(!(tst instanceof Boolean)){
            System.out.println("CONDICION DEBE SER BOOLEANA");
            return null;
        }
        LinkedList<Instruccion> list_instr;
        while((Boolean)tst){
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
                System.out.println("CONDICION DEBE SER BOOLEANA");
                return null;
            }
        }
        return null;
    }   
}