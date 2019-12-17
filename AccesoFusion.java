package arbol;

import java.util.LinkedList;

public class AccesoFusion  implements Instruccion{
    ////#############VALORES DE ASIGNACION-------------------------
    private final LinkedList<ACCFUSION> lista_acc;
    private final String id;
    private final LinkedList<Instruccion> list_dim;
     protected final Instruccion auxfus;
    ////#############VALORES DE ASIGNACION-------------------------
    
    //CONSTRUCTOR ACCESO A FUSION POR ID
    public AccesoFusion(String id, LinkedList<ACCFUSION> lista_acc) {
        this.id=id;
        this.lista_acc=lista_acc;
        this.list_dim=null;
        this.auxfus=null;
    }
    
    //CONSTRUCTOR ACCESO FUSION CON LLAMADA A FUNCION
    public AccesoFusion(LinkedList<ACCFUSION> lista_acc, Instruccion func) {
        this.id="arrfusion";
        this.lista_acc=lista_acc;
        this.list_dim=null;
        this.auxfus=func;
    }
    
    //CONSTRUCTOR ACCESO FUSION POR ID CON DIMENSIONES
    public AccesoFusion(String id, LinkedList<Instruccion> list_dim, LinkedList<ACCFUSION> lista_acc) {
        this.id=id;
        this.lista_acc=lista_acc;
        this.list_dim=list_dim;
        this.auxfus=null;
    }
    
    //CONSTRUCTOR ACCESO FUSION POR LLAMADA A FUNCION CON DIMENSIONES
    public AccesoFusion(LinkedList<Instruccion> list_dim, LinkedList<ACCFUSION> lista_acc, Instruccion func) {
        this.id="arrfusion";
        this.lista_acc=lista_acc;
        this.list_dim=list_dim;
        this.auxfus=func;
    }
    
    @Override
    public Object ejecutar(TablaDeSimbolos ts,Arbol ar) {
        Object res=null;
        if(list_dim!=null){//SI TRAE DIMENSIONES QUE ES UN ARREGLO DE FUSIONES
            LinkedList<Integer> valoresIndices=new LinkedList<>();
            for (Instruccion dim : this.list_dim) {
                if(dim!=null){
                    Object er=dim.ejecutar(ts, ar);
                    //COMPROBAR QUE CADA INDICE SEA NUMERICO
                    if(!(er instanceof Integer)){
                        ar.addError("ERROR EN ACCESO A FUSION LOS INDICES DEBEN SER TIPO ENTERO");
                        System.err.println("ERROR EN ACCESO A FUSION LOS INDICES DEBEN SER TIPO ENTERO");
                        return null;
                    }
                    valoresIndices.add((Integer)er);
                }else{
                    //SI VIENE VACIO ES ERROR
                    ar.addError("ERROR EN ACCESO A FUSION LOS INDICES NO PUEDEN VENIR VACIOS");
                    System.err.println("ERROR EN ACCESO A FUSION LOS INDICES NO PUEDEN VENIR VACIOS");
                    return null;    
                }
            }
            if(auxfus!=null){//SI SE VA A ACCEDER MEDIANTE LLAMADA A FUNCION Y ES ARREGLO
                Object resfunc = auxfus.ejecutar(ts, ar);
                if(!(resfunc instanceof NodoArreglo)){
                    ar.addError("ERROR EN ACCESO FUSION LA FUNCION NO ES UN ARREGLO");
                    System.out.println("ERROR EN ACCESO FUSION LA FUNCION NO ES UN ARREGLO");
                    return null;
                }
                NodoArreglo arr = (NodoArreglo)resfunc;
                res =arr.getValor(valoresIndices.size(), 1, valoresIndices, id);
            }else{
                res= ts.getValor(id,valoresIndices);    
            }
            
        }else{//SI SE ACCEDE POR UN ID NORMAL SIN ARREGLO
            if(auxfus!=null){
                res =auxfus.ejecutar(ts, ar);
            }else{
                res = ts.getValor(id);   
            }
        }
        
        if(res instanceof Fusion){
            Fusion f = (Fusion)res;
            ACCFUSION aux;
            Simbolo auxsim;
            for(int i=0;i<(lista_acc.size()-1);i++){//PARA CADA ACCESO A FUSION SIN CONTAR EL ULTIMO
                aux = lista_acc.get(i);
                if(aux.getList_dim()==null){ //VARIABLE NORMAL DENTRO DE FUSION
                    auxsim=f.getSimbolo(aux.getId());
                    if(auxsim.getValor() instanceof Fusion){
                        f=(Fusion)auxsim.getValor();
                    }else{
                        ar.addError("ERROR EN ACCESO A FUSION "+aux.getId()+" NO TIENE UNA FUSION A LA QUE SE INTENTA ACCEDER");
                        System.out.println("ERROR EN ACCESO A FUSION "+aux.getId()+" NO TIENE UNA FUSION A LA QUE SE INTENTA ACCEDER");
                        return null;
                    }
                }else{//VARIABLE ARREGLO DENTRO DE FUSION
                    auxsim=f.getSimbolo(aux.getId());
                    if(auxsim.getFusion()!=null){
                        LinkedList<Integer> valoresIndices=new LinkedList<>();
                        for (Instruccion dim : aux.getList_dim()) {
                            if(dim!=null){
                                Object er=dim.ejecutar(ts, ar);
                                //COMPROBAR QUE CADA INDICE SEA NUMERICO
                                if(!(er instanceof Integer)){
                                    ar.addError("ERROR EN ACCESO A FUSION LOS INDICES DEBEN SER TIPO ENTERO");
                                    System.err.println("ERROR EN ACCESO A FUSION LOS INDICES DEBEN SER TIPO ENTERO");
                                    return null;
                                }
                                valoresIndices.add((Integer)er);
                            }else{
                                //SI VIENE VACIO ES ERROR
                                ar.addError("ERROR EN ACCESO A FUSION LOS INDICES NO PUEDEN VENIR VACIOS");
                                System.err.println("ERROR EN ACCESO A FUSION LOS INDICES NO PUEDEN VENIR VACIOS");
                                return null;       
                            }
                        }
                        Object testsim = auxsim.getValor(auxsim.getId(),valoresIndices);
                        if(testsim instanceof Fusion){
                            f=(Fusion)testsim;
                        }else{
                            ar.addError("ERROR EN ACCESO A FUSION "+aux.getId()+" NO TIENE UNA FUSION A LA QUE SE INTENTA ACCEDER");
                            System.out.println("ERROR EN ACCESO A FUSION "+aux.getId()+" NO TIENE UNA FUSION A LA QUE SE INTENTA ACCEDER");
                            return null;
                        }
                    }else{
                        ar.addError("ERROR EN ACCESO A FUSION "+aux.getId()+" NO TIENE UNA FUSION A LA QUE SE INTENTA ACCEDER");
                        System.out.println("ERROR EN ACCESO A FUSION "+aux.getId()+" NO TIENE UNA FUSION A LA QUE SE INTENTA ACCEDER");
                        return null;
                    }
                }
            }
            aux = lista_acc.get(lista_acc.size()-1);
            auxsim=f.getSimbolo(aux.getId());
            if(aux.getList_dim()==null){//RETORNA EL VALOR DE LA FUSION
                if(auxsim==null){
                    ar.addError("ERROR EN ACCESO A FUSION "+aux.getId()+" NO EXISTE");
                    System.out.println("ERROR EN ACCESO A FUSION "+aux.getId()+" NO EXISTE");
                    return null;
                }
                return auxsim.getValor();
            }else{
                LinkedList<Integer> valoresIndices=new LinkedList<>();
                for (Instruccion dim : aux.getList_dim()) {
                    if(dim!=null){
                        Object er=dim.ejecutar(ts, ar);
                        //COMPROBAR QUE CADA INDICE SEA NUMERICO
                        if(!(er instanceof Integer)){
                            ar.addError("ERROR EN ACCESO A FUSION LOS INDICES DEBEN SER TIPO ENTERO");
                            System.err.println("ERROR EN ACCESO A FUSION LOS INDICES DEBEN SER TIPO ENTERO");
                            return null;
                        }
                        valoresIndices.add((Integer)er);
                    }else{
                        //SI VIENE VACIO ES ERROR
                        ar.addError("ERROR EN ACCESO A FUSION LOS INDICES NO PUEDEN VENIR VACIOS");
                        System.err.println("ERROR EN ACCESO A FUSION LOS INDICES NO PUEDEN VENIR VACIOS");
                        return null;     
                    }
                }
                return auxsim.getValor(auxsim.getId(), valoresIndices);
            }
            
        }else if(res instanceof NodoArreglo){
            return (NodoArreglo)res;
        }else{
            ar.addError("ERROR, LA VARIABLE: "+id+" NO ES DE TIPO FUSION");
            System.out.println("ERROR, LA VARIABLE: "+id+" NO ES DE TIPO FUSION");
        }
        return null;
    }
}
