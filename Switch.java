package arbol;

import java.util.LinkedList;


public class Switch implements Instruccion{
     ////#############VALORES DE SWITCH-------------------------
    private final LinkedList<Instruccion> subCases;
    private final Instruccion comparacion;
    ////#############VALORES DE SWITCH-------------------------

    //SWITCH CON DEFAULT
    public Switch(LinkedList<SubCase> lsubcase,SubCase subdef,Instruccion ins) {
        subCases=new LinkedList<>();
        subCases.addAll(lsubcase);
        subCases.add(subdef);
        this.comparacion=ins;
    }
    //SWITCH SIN DEFAULT
    public Switch(LinkedList<SubCase> lsubcase,Instruccion ins) {
        subCases=new LinkedList<>();
        subCases.addAll(lsubcase);
        this.comparacion=ins;
    }
    
    @Override
    public Object ejecutar(TablaDeSimbolos ts,Arbol ar) {
        //Ejecutar SubCases
        Object r,r2;
        r2=comparacion.ejecutar(ts, ar);
        boolean nocomparar=false;
        SubCase actual;
        for(Instruccion in: subCases){
            actual =(SubCase)in;
            r=actual.ejecutar(ts,ar);
            if(r.equals(r2) || nocomparar || actual.getisDefault() ){
                nocomparar=true;
                LinkedList<Instruccion> ins  = actual.getInstrucciones();
                for(Instruccion sin: ins){
                    r=sin.ejecutar(ts, ar);
                    if(r instanceof Return || r instanceof Break){
                        return r;
                    }
                }
            }
        }
        return null;
    }
}
