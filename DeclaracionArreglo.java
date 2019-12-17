package arbol;
import java.util.LinkedList;
import java.util.Objects;

public class DeclaracionArreglo extends Declaracion implements Instruccion{
    ////#############VALORES DE DECLARACION ARREGLO-------------------------
    private final LinkedList<Instruccion> dimensiones;
    private Instruccion asigarr;
    ////#############VALORES DE DECLARACION ARREGLO-------------------------
    
    //CONSTRUCTOR ARREGLO SIN VALORES
    public DeclaracionArreglo(Object tipo, String identificador, LinkedList<Instruccion> dim) {
        super(tipo,identificador);
        dimensiones=dim;
        this.asigarr=null;
    }
    
    //CONSTRUCTOR ARREGLO CON VALORES E INDICES
    public DeclaracionArreglo(Object tipo, String identificador, LinkedList<Instruccion> dim,Instruccion asig){
        super(tipo,identificador);
        dimensiones=dim;
        this.asigarr=asig;
    }
    
    //EJECUTAR
    @Override
    public Object ejecutar(TablaDeSimbolos ts,Arbol ar) {
        if(tipo==Simbolo.Tipo.CADENA){
            ar.addError("ERROR AL DECLARAR: "+this.id+" NO SE PUEDE CREAR UN ARREGLO DE CADENAS");
            System.out.println("ERROR AL DECLARAR: "+this.id+" NO SE PUEDE CREAR UN ARREGLO DE CADENAS");
            return null;
        }
        LinkedList<Integer> tamaniosDimensiones=new LinkedList<>();
        //SI LAS DIMENSIONES TIENEN INDICES DECLARADOS
        if(dimensiones.get(0)!=null){
            for (Instruccion dim : dimensiones) {
                if(dim==null){
                    ar.addError("ERROR AL ASIGNAR EL ARREGLO: "+this.id+" ALGUN INDICE VIENE VACIO");
                    System.out.println("Error en AsignacionArreglo: indice vacio "+this.id);
                    return null;
                }
                Object er=dim.ejecutar(ts, ar);
                //COMPROBAR SI ES ENTERO
                if(!(er instanceof Integer)){
                    ar.addError("INDICE: "+this.id+" EN ARREGLO: "+this.id+" NO ES NUMERICO");
                    System.out.println("INDICE: "+this.id+" EN ARREGLO: "+this.id+" NO ES NUMERICO");
                    return null;
                }
                tamaniosDimensiones.add((Integer)er);
            }
            //VERIFICAR SI TRAE ASIGNACION DE NODO ARREGLO O NO;
            if(asigarr!=null){
                Object res = asigarr.ejecutar(ts, ar);
                //si se asigna un nodo arreglo {{},{},{}}
                if(res instanceof NodoArreglo){
                    NodoArreglo arr_asig=(NodoArreglo)res;
                    LinkedList<Integer> tam_aux=arr_asig.getDims();
                    if(tamaniosDimensiones.size() == tam_aux.size()){
                        for(int i=0;i<tamaniosDimensiones.size();i++){
                            if(!Objects.equals(tamaniosDimensiones.get(i), tam_aux.get(i))){
                                ar.addError("ERROR EN :"+this.id+"TAMAÑO DE DIMENSIONES NO COINCIDE");
                                System.out.println("ERROR EN TAMAÑO DE DIMENSIONES Y ASIGNACION");
                                return null;
                            }
                        }
                        Simbolo auxsim = new Simbolo(id,tipo,tamaniosDimensiones,arr_asig);
                        auxsim.setFusion(this.nombre_fusion);
                        ts.add(auxsim); 
                    }else{
                        ar.addError("ERROR EN :"+this.id+"TAMAÑO DE DIMENSIONES NO COINCIDE");
                        System.out.println("ERROR EN TAMAÑO DE DIMENSIONES Y ASIGNACION");
                        return null;
                    }
                }else if(res instanceof String){
                //si se asigna una cadena de caracteres
                    if(tamaniosDimensiones.size()==1){
                        String val = (String)res;
                        if(tamaniosDimensiones.get(0)<val.length()){
                            ar.addError("ERROR EN :"+this.id+"TAMAÑO DE DIMENSIONES NO COINCIDE");
                            System.out.println("ERROR EN :"+this.id+"TAMAÑO DE DIMENSIONES NO COINCIDE");    
                            return null;
                        }
                        LinkedList<Object> vals= new LinkedList<>();
                        //CREAR UNA LISTA SIMPLE DE TODOS LOS VALORES
                         for(int i=0;i<tamaniosDimensiones.get(0);i++){
                            if(i<val.length()){
                                vals.add(val.toCharArray()[i]);   
                            }else{
                                vals.add(null);
                            }
                        }
                        //CREAR UN NODO ARREGLO
                        int [] contador={0};
                        NodoArreglo arr=new NodoArreglo();
                        arr.setDims(tamaniosDimensiones);
                        arr.inicializarNodo(tamaniosDimensiones.size(), 1, tamaniosDimensiones, vals, contador);
                        Simbolo arrchar = new Simbolo(id,tipo,tamaniosDimensiones,arr);
                        ts.add(arrchar);
                    }else{
                        ar.addError("ERROR EN :"+this.id+"ARREGLO DE CARACTERES SOLO PUEDE SER DE UNA DIMENSION");
                        System.out.println("ERROR NO SE PUEDE ASIGNAR ASI UN ARREGLO DE CARACTERES");
                        return null;    
                    }
                }else if(res instanceof Simbolo){
                    Simbolo ref = (Simbolo)res;
                    if(tipo==ref.getTipo()){
                        if(ref.getValor() instanceof NodoArreglo){
                            Simbolo refhijo = new Simbolo(id,tipo,tamaniosDimensiones);
                            refhijo.setIs_ref(true);
                            refhijo.setRef(ref.getId());
                            ts.add(refhijo);    
                        }else{
                            ar.addError("ERROR EN :"+this.id+" LA VARIABLE QUE SE VA A REFERENCIAR NO ES ARREGLO");
                            System.out.println("ERROR EN :"+this.id+" LA VARIABLE QUE SE VA A REFERENCIAR NO ES ARREGLO");
                        }
                    }else{
                        ar.addError("ERROR EN :"+this.id+" NO SON DEL MISMO TIPO");
                        System.out.println("ERROR EN :"+this.id+" NO SON DEL MISMO TIPO");
                    }
                }else{
                    ar.addError("ERROR EN TIPO DE ASIGNACION EN ARREGLO: "+this.id);
                    System.out.println("ERROR EN TIPO DE ASIGNACION EN ARREGLO: "+this.id);
                    return null;    
                }
            }else{
                ts.add(new Simbolo(id,tipo,tamaniosDimensiones));   
            }
        }else{//SI VIENE CON DIMENSIONES VACIAS [][]
            if(asigarr!=null){
                Object res = asigarr.ejecutar(ts, ar);
                //si se asigna un nodo arreglo {{},{},{}}
                if(res instanceof NodoArreglo){
                    NodoArreglo arr_asig=(NodoArreglo)res;
                    tamaniosDimensiones=arr_asig.getDims();
                    Simbolo auxsim = new Simbolo(id,tipo,tamaniosDimensiones,arr_asig);
                    auxsim.setFusion(this.nombre_fusion);
                    ts.add(auxsim);
                }else if(res instanceof String){
                //si se asigna una cadena de caracteres
                    if(dimensiones.size()==1){
                        String val = (String)res;
                        tamaniosDimensiones.add(val.length());
                        LinkedList<Object> vals= new LinkedList<>();
                        //CREAR UNA LISTA SIMPLE DE TODOS LOS VALORES
                        for(int i=0;i<val.length();i++){
                            vals.add(val.toCharArray()[i]);
                        }
                        //CREAR UN NODO ARREGLO
                        int [] contador={0};
                        NodoArreglo arr=new NodoArreglo();
                        arr.setDims(tamaniosDimensiones);
                        arr.inicializarNodo(tamaniosDimensiones.size(), 1, tamaniosDimensiones, vals, contador);
                        Simbolo arrchar = new Simbolo(id,tipo,tamaniosDimensiones,arr);
                        ts.add(arrchar);
                    }else{
                        ar.addError("ERROR EN TIPO DE ASIGNACION EN ARREGLO: "+this.id);
                        System.out.println("ERROR NO SE PUEDE ASIGNAR ASI UN ARREGLO DE CARACTERES");
                        return null;
                    }
                }else if(res instanceof Simbolo){//SI ES UNA REFERENCIA DE ARREGLO
                    Simbolo ref = (Simbolo)res;
                    if(tipo==ref.getTipo()){
                        if(ref.getValor() instanceof NodoArreglo){
                            NodoArreglo arr_asig=(NodoArreglo)ref.getValor();
                            tamaniosDimensiones=arr_asig.getDims();
                            Simbolo refhijo = new Simbolo(id,tipo,tamaniosDimensiones);
                            refhijo.setIs_ref(true);
                            refhijo.setRef(ref.getId());
                            ts.add(refhijo);
                        }else{
                            ar.addError("ERROR LA VARIABLE QUE SE VA A REFERENCIAR NO ES ARREGLO: "+this.id);
                            System.out.println("ERROR LA VARIABLE QUE SE VA A REFERENCIAR NO ES ARREGLO");
                        }
                    }else{
                        ar.addError("ERROR DE VARIABLE REFERENCIADA NO SON DEL MISMO TIPO: "+this.id);
                        System.out.println("ERROR NO SON DEL MISMO TIPO");
                    }
                }else{
                    ar.addError("ERROR EN TIPO DE ASIGNACION EN ARREGLO: "+this.id);
                    System.out.println("ERROR EN TIPO DE ASIGNACION EN ARREGLO: "+this.id);
                    return null;
                }
            }else{
                ar.addError("NO SE PUEDE DECLARAR UN ARREGLO SIN INDICES NI VALORES: "+this.id);
                System.out.println("NO SE PUEDE DECLARAR UN ARREGLO SIN INDICES NI VALORES"+this.id);
                return null;
            }
        }
        return null;
    }
    
    //OBTENER ASIGNACION
    public Instruccion getAsigArr(){
        return this.asigarr;
    }
    
    public void setAsigArr(Instruccion asigarr){
        this.asigarr=asigarr;
    }
}
