package arbol;

import java.util.LinkedList;

public class Write implements Instruccion{
    ////#############VALORES DE IMPRESION-------------------------
    Instruccion ruta;
    boolean ap;
    ////#############VALORES DE IMPRESION-------------------------
    
    public Write(Instruccion ruta,boolean ap){
        this.ruta=ruta;
        this.ap=ap;
    }

    @Override
    public Object ejecutar(TablaDeSimbolos ts, Arbol ar) {
        Object res = this.ruta.ejecutar(ts, ar);
        if(!(res instanceof String)){
            System.out.println("ERROR LA RUTA DEBE VENIR EN UN STRING");
        }
        String valruta=(String)res;
        ar.SetFichero(valruta,ap);
        return null;
    }
}

