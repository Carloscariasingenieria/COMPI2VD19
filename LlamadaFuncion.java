package arbol;
import java.util.LinkedList;

public class LlamadaFuncion implements Instruccion{
    
    ////#############VALORES DE LLAMADAFUNCION-------------------------
    private final String identificador;
    private final LinkedList<Instruccion> parametros;
    ////#############VALORES DE LLAMADAFUNCION-------------------------
    
    public LlamadaFuncion(String id, LinkedList<Instruccion> params) {
        identificador=id;
        parametros=params;
    }
    
    @Override
    public Object ejecutar(TablaDeSimbolos ts,Arbol ar) {
 
        // para llamar a la función es necesario construir su identificador único
        String id = "_" + identificador + "(";
        for(Instruccion parametro: parametros) {
            Object resultado = parametro.ejecutar(ts, ar);
            
            if (resultado instanceof Double) {
                id += "_" + Simbolo.Tipo.DECIMAL;
            } else if(resultado instanceof Integer) {
                id += "_" + Simbolo.Tipo.ENTERO; 
            }else if(resultado instanceof String) {
                id += "_" + Simbolo.Tipo.CADENA;
            } else if(resultado instanceof Boolean){
                id += "_" + Simbolo.Tipo.BOOLEANO;
            }else if(resultado instanceof Character){
                id += "_" + Simbolo.Tipo.CARACTER;
            }else if(resultado instanceof Fusion){
                id += "_" + Simbolo.Tipo.FUSION;
            }
        }
        id += ")";
        
        Function f=ar.getFunction(id.toLowerCase());
        if(f!=null){
            f.setValoresParametros(parametros);
            Object rFuncion=f.ejecutar(ts, ar); //Objeto que almacena el resultado de la ejecución del proceso
            if(!(rFuncion instanceof NodoArreglo)){
                if(f.getTipo()==Simbolo.Tipo.VOID && !(rFuncion == null)){
                    ar.addError("Una función de tipo Void no puede retornar valores");
                    System.err.println("Una función de tipo Void no puede retornar valores");
                    return null;
                } else if (f.getTipo() != Simbolo.Tipo.VOID && rFuncion == null) {
                    ar.addError("Hace falta una sentencia de retorno en la función");
                    System.err.println("Hace falta una sentencia de retorno en la función");
                    return null;
                }else if((f.getTipo()==Simbolo.Tipo.DECIMAL) && !(rFuncion instanceof Double)){
                    ar.addError("Una función de tipo Numerico no puede retornar un valor que no sea numérico.");
                    System.err.println("Una función de tipo Numerico no puede retornar un valor que no sea numérico.");
                    return null;
                }else if(f.getTipo()==Simbolo.Tipo.ENTERO && !(rFuncion instanceof Integer)){
                    ar.addError("Una función de tipo Numerico no puede retornar un valor que no sea numérico.");
                    System.err.println("Una función de tipo Numerico no puede retornar un valor que no sea numérico.");
                    return null;
                }else if(f.getTipo()==Simbolo.Tipo.BOOLEANO && !(rFuncion instanceof Boolean)){
                    ar.addError("Una función de tipo Boolean no puede retornar un valor que no sea verdadero o falso.");
                    System.err.println("Una función de tipo Boolean no puede retornar un valor que no sea verdadero o falso.");
                    return null;
                }else if(f.getTipo()==Simbolo.Tipo.CADENA && !(rFuncion instanceof String)){
                    ar.addError("Una función de tipo Cadena no puede retornar un valor que no sea una cadena de caracteres.");
                    System.err.println("Una función de tipo Cadena no puede retornar un valor que no sea una cadena de caracteres.");
                    return null;
                }else if(f.getTipo()==Simbolo.Tipo.CARACTER && !(rFuncion instanceof Character)){
                    ar.addError("Una función de tipo Cadena no puede retornar un valor que no sea una cadena de caracteres.");
                    System.err.println("Una función de tipo Cadena no puede retornar un valor que no sea una cadena de caracteres.");
                    return null;
                }else if(f.getTipo()==Simbolo.Tipo.FUSION && !(rFuncion instanceof Fusion)){
                    ar.addError("Una función de tipo Cadena no puede retornar un valor que no sea una cadena de caracteres.");
                    System.err.println("Una función de tipo Cadena no puede retornar un valor que no sea una cadena de caracteres.");
                    return null;
                }
            }else{
                if(!f.isArreglo()){
                    ar.addError("Error en el retorno la funcion:"+this.identificador+" es de tipo arreglo");
                    System.out.println("Error en el retorno la funcion "+this.identificador+"es de tipo arreglo");
                }else{
                    Dim aux = f.getDim();
                    LinkedList<Instruccion> auxlist=aux.lista_dim;
                    NodoArreglo auxarr= (NodoArreglo)rFuncion;
                    if(!(auxlist.size()==auxarr.getDims().size())){
                        ar.addError("El tamaño del arreglo devuelto y el de la funcion: "+this.identificador+" no son iguales");
                        System.out.println("El tamaño del arreglo devuelto y el de la funcion "+this.identificador+" no son iguales");
                    }
                }
            }
            return rFuncion;
        } else {
            ar.addError("La función " + identificador + " no existe."); 
            System.err.println("La función " + identificador + " no existe.");    
        }
        return null;
    }
}
