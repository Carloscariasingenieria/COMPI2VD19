package arbol;

public class ReadF implements Instruccion{
    ////#############VALORES DE IMPRESION-------------------------
    Instruccion ruta;
    String id;
    ////#############VALORES DE IMPRESION-------------------------
    
    public ReadF(Instruccion ruta,String id){
        this.ruta=ruta;
        this.id=id;
    }

    @Override
    public Object ejecutar(TablaDeSimbolos ts, Arbol ar) {
        Object res = this.ruta.ejecutar(ts, ar);
        if(!(res instanceof String)){
            System.out.println("ERROR LA RUTA DEBE VENIR EN UN STRING");
        }
        String valruta=(String)res;
        String resfile = ar.readFichero(valruta);
        ts.setValor(id, resfile);
        return null;
    }
}
