package arbol;
import java.util.LinkedList;

public class AccesoArreglo implements Instruccion{

    ////#############VALORES DE SIMBOLOS-------------------------
    private final LinkedList<Instruccion> indices;
    protected final String id;
    protected final Instruccion auxarr;
    ////#############VALORES DE SIMBOLOS-------------------------
    
    //CONSTRUCTOR ID, INDICES DEL VALOR
    public AccesoArreglo(String id, LinkedList<Instruccion> indices) {
        this.indices = indices;
        this.id = id;
        this.auxarr=null;
    }
    
    //CONSTRUCTOR ID, INDICES DEL VALOR
    public AccesoArreglo(Instruccion arr, LinkedList<Instruccion> indices) {
        this.indices = indices;
        this.auxarr=arr;
        this.id=null;
    }

    
    @Override
    public Object ejecutar(TablaDeSimbolos ts, Arbol ar) {
        if(indices.get(0)==null){
            ar.addError("ERROR AL ACCEDER AL ARREGLO: "+this.id+" ALGUN INDICE VIENE VACIO");
            System.out.println("Error en AccesoArreglo: indice vacio "+this.id);
            return null;
        }
        LinkedList<Integer> valoresIndices=new LinkedList<>();
        for (Instruccion dim : indices) {
            if(dim==null){
                ar.addError("ERROR AL ACCEDER AL ARREGLO: "+this.id+" ALGUN INDICE VIENE VACIO");
                System.out.println("Error en AccesoArreglo: indice vacio "+this.id);
                return null;
            }
            Object er=dim.ejecutar(ts, ar);
            //COMPROBAR SI ES ENTERO
            if(!(er instanceof Integer)){
                ar.addError("INDICE: "+String.valueOf(er)+" EN ARREGLO: "+this.id+" NO ES NUMERICO");
                System.out.println("Los indices para acceder a un arreglo deben ser de tipo numérico. El indice ["+String.valueOf(er)+"] no es numérico.");
                return null;
            }
            valoresIndices.add((Integer)er);
        }
        if(auxarr!=null){//SI VIENE ARREGLO EN LUGAR DE UN ID
            Object resfunc = auxarr.ejecutar(ts, ar);
            if(!(resfunc instanceof NodoArreglo)){
                ar.addError("ERROR LA FUNCION A ACCEDER NO ES UN ARREGLO");
                System.out.println("ERROR LA FUNCION A ASIGNAR A NO ES UN ARREGLO");
                return null;
            }
            NodoArreglo arr = (NodoArreglo)resfunc;
            try{
                return arr.getValor(valoresIndices.size(), 1, valoresIndices, id);
            }catch(Exception e){e.printStackTrace();}
        }else{
            return ts.getValor(id, valoresIndices); 
        }
        return null;
    }
    
}
