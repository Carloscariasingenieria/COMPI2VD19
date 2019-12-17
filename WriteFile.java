package arbol;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedList;

public class WriteFile implements Instruccion{
    ////#############VALORES DE IMPRESION-------------------------
    LinkedList<Instruccion> instrucciones;
    ////#############VALORES DE IMPRESION-------------------------
    
    public WriteFile(LinkedList<Instruccion> instrucciones){
        this.instrucciones=instrucciones;
    }

    @Override
    public Object ejecutar(TablaDeSimbolos ts, Arbol ar) {
        FileWriter fichero = ar.getFichero();
        if(fichero==null){
            System.out.println("ERROR NO HAY FICHERO ABIERTO PARA ESCRIBIR");
        }
        Object res = instrucciones.get(0).ejecutar(ts, ar);
        String impr ="";
        if(res instanceof String){
            impr=(String)res;
        }else if(res instanceof Character){
            impr=(Character)res+"";
            System.out.println("####"+impr+"#####");
            return null;
        }else{
            System.out.println("ERROR AL ESCRIBIR EN ARCHIVO, FORMATO NO VALIDO");
            return null;
        }
        LinkedList<Object> valores = new LinkedList<>();
        for(Instruccion ins:instrucciones){
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
            for(int i=0;i<impstr.size()-1;i++){
                impr+=impstr.pop();
                if((valores.get(i) instanceof Double) && ((impvals.get(i)=='d')||(impvals.get(i)=='e'))){
                    impr+=(Double)valores.get(i);
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
                        System.out.println("ERROR EL ARREGLO NO ES DE CARACTERES");
                        return null;
                    }
                }else{
                    System.out.println("ERROR DE FORMATO");
                    return null;
                }
            }
            try{
                impr+=impstr.pop();
            }catch(Exception e){}
        }else{
            System.out.println("ERROR LOS VALORES A IMPRIMIR NO COINCIDEN EN TAMAÑO");
            return null;
        }
        PrintWriter pw = null;
        try
        {
            pw = new PrintWriter(impr);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

