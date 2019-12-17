package arbol;
import java.util.LinkedList;

public class IMPRESION implements Instruccion{
    ////#############VALORES DE IMPRESION-------------------------
    LinkedList<Instruccion> instrucciones;
    ////#############VALORES DE IMPRESION-------------------------
    
    public IMPRESION(LinkedList<Instruccion> instrucciones){
        this.instrucciones=instrucciones;
    }

    @Override
    public Object ejecutar(TablaDeSimbolos ts, Arbol ar) {
        Object res = instrucciones.get(0).ejecutar(ts, ar);
        String impr;
        if(res instanceof String){
            impr=(String)res;
        }else if(res instanceof Character){
            impr=""+(Character)res;
            System.out.println("####"+impr+"#####");
            return null;
        }else{
            ar.addError("ERROR AL IMPRIMIR, FORMATO NO VALIDO: "+res);
            System.out.println("ERROR AL IMPRIMIR, FORMATO NO VALIDO "+res);
            return null;
        }
        LinkedList<Instruccion> ins_aux = ( LinkedList<Instruccion>)instrucciones.clone();
        ins_aux.pop();
        
        LinkedList<Object> valores = new LinkedList<>();
        for(Instruccion ins:ins_aux){
            valores.add(ins.ejecutar(ts, ar));
        }
        LinkedList<Character> impvals = new LinkedList<>();
        LinkedList<String> impstr = new LinkedList<>();
        String vals[] =impr.split("%");
        impstr.add(vals[0]);
        for(int i=1;i<vals.length;i++){
            impvals.add(vals[i].charAt(0));
            impstr.add(vals[i].substring(1, vals[i].length()));
        }
        impr="";
        if(valores.size()==impvals.size()){
            int tam=impstr.size();
            for(int i=0;i<tam-1;i++){
                impr+=impstr.pop();
                if((valores.get(i) instanceof Double) && (impvals.get(i)=='d')){
                    Double aux1=(Double)valores.get(i);
                    impr+=aux1;
                }else if((valores.get(i) instanceof Integer) && (impvals.get(i)=='e')){
                    impr+=(Integer)valores.get(i);
                }else if((valores.get(i) instanceof Boolean) && (impvals.get(i)=='b')){
                    impr+=(Boolean)valores.get(i);
                }else if((valores.get(i) instanceof Character) && (impvals.get(i)=='c')){
                    impr+=(Character)valores.get(i);
                }else if((valores.get(i) instanceof NodoArreglo) && (impvals.get(i)=='s')){
                    NodoArreglo arr = (NodoArreglo)valores.get(i);
                    if(validarNodo(arr)){
                        String arrstr =getString(arr);
                        impr+=arrstr;
                    }else{
                        ar.addError("ERROR EL ARREGLO NO ES DE CARACTERES");
                        System.out.println("ERROR EL ARREGLO NO ES DE CARACTERES");
                        return null;
                    }
                }else{
                    ar.addError("ERROR DE FORMATO AL IMPRIMIR");
                    System.out.println("ERROR DE FORMATO AL IMPRIMIR");
                    return null;
                }
            }
            try{
                impr+=impstr.pop();
            }catch(Exception e){}
        }else{
            ar.addError("ERROR LOS VALORES A IMPRIMIR NO COINCIDEN EN TAMAÑO");
            System.out.println("ERROR LOS VALORES A IMPRIMIR NO COINCIDEN EN TAMAÑO");
            return null;
        }
        System.out.println("####"+impr+"#####");
        return null;
    }
    
     private boolean validarNodo(NodoArreglo arr){
        LinkedList<Integer> indices = new LinkedList<>();
        indices.add(0);
        int auxsize=arr.getDims().size();
        //VALIDAR SI A ES UN ARREGLO DE CARACTERES
        if(auxsize==1){
            Object valaux = arr.getValor(arr.getDims().size(), 1, indices, "");
            if(valaux instanceof Character){
                return true;
            }
        }           
        return false;
    }
    
    private String getString(NodoArreglo arr){
        String res="";
        LinkedList<Integer> indices = new LinkedList<>();
        indices.add(0);
        int cont=arr.getDims().get(0);
        for(int i=0;i<cont;i++){
            indices.set(0, i);
            Object val =arr.getValor(1, 1, indices, "");
            if(val!=null){
             res+=(Character)val;   
            }
        }    
        return res;
    }
}
