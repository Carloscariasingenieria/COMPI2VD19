package arbol;

import java.util.LinkedList;

public class AsignacionFusion extends Asignacion implements Instruccion{
    ////#############VALORES DE ASIGNACION-------------------------
    private final LinkedList<ACCFUSION> lista_acc;
    private final LinkedList<Instruccion> list_dim;
    ////#############VALORES DE ASIGNACION-------------------------
    
    //CONSTRUCTOR ASIGNACION ARREGLO
    public AsignacionFusion(String id, LinkedList<ACCFUSION> lista_acc, Instruccion valor) {
        super(id,valor);
        this.lista_acc=lista_acc;
        this.list_dim=null;
    }
    
    //CONSTRUCTOR ASIGNACION ARREGLO
    public AsignacionFusion(String id, LinkedList<Instruccion> list_dim, LinkedList<ACCFUSION> lista_acc, Instruccion valor) {
        super(id,valor);
        this.lista_acc=lista_acc;
        this.list_dim=list_dim;
    }
    
    
    @Override
    public Object ejecutar(TablaDeSimbolos ts,Arbol ar) {
        if(this.valor==null){
            ar.addError("ERROR ASIGNACION DE: "+this.id+" VIENE VACIA");
            System.out.println("ERROR ASIGNACION VACIA: "+this.id);
            return null;
        }
        Object res;
        if(list_dim!=null){
            LinkedList<Integer> valoresIndices=new LinkedList<>();
            if(list_dim.get(0)==null){
                System.out.println("ERROR AL DECLARAR DIMENSIONES DEL ARREGLO: "+this.id);
                return null;
            }
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
            res= ts.getValor(id,valoresIndices);    
        }else{
            res = ts.getValor(id);
        }
        if(res instanceof Fusion){
            Fusion f = (Fusion)res;
            ACCFUSION aux;
            Simbolo auxsim;
            for(int i=0;i<(lista_acc.size()-1);i++){
                aux = lista_acc.get(i);
                if(aux.getList_dim()==null){//SI SE ACCEDE A VARIABLE DE FUSION NORMAL
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
            if(auxsim==null){
                ar.addError("ERROR EN ACCESO A FUSION "+aux.getId()+" NO EXISTE");
                System.out.println("ERROR EN ACCESO A FUSION "+aux.getId()+" NO EXISTE");
                return null;
            }
            res=this.valor.ejecutar(ts, ar);
            if(aux.getList_dim()==null){
                if(res instanceof Simbolo){//SI DEVUELVE SIMBOLO ES REFERENCIA
                    Simbolo ref = (Simbolo)res;
                    auxsim.setIs_ref(true);
                    auxsim.setRef(ref.getId());
                }else{
                    if(auxsim.getTipo()==Simbolo.Tipo.FUSION){
                         if(res instanceof Fusion){//SI SE VA A ASIGNAR UN FUSION (RESERVAR)
                             Fusion asigfus = (Fusion)res;
                             Fusion varfus = ar.getFusion(auxsim.getFusion());
                             if(varfus!=null){
                                 if(asigfus.getPeso()==varfus.getPeso()){
                                     if(auxsim.isIs_ref()){
                                         ts.getSimbol(auxsim.getRef()).setValor(varfus);
                                     }else{
                                         auxsim.setValor(varfus);
                                     }
                                 }else{
                                     ar.addError("EL PESO DE LA FUSION: "+varfus.getNombre()+" NO COINCIDE;");
                                     System.out.println("EL PESO DE LA FUSION: "+varfus.getNombre()+" NO COINCIDE;");
                                 }
                             }else{
                                 ar.addError("ERROR NO SE HA DECLARADADO LA FUSION: "+auxsim.getFusion());
                                 System.out.println("ERROR NO SE HA DECLARADADO LA FUSION: "+auxsim.getFusion());
                             }
                         }else{
                             ar.addError("ERROR EN: "+id+" LOS TIPOS A ASIGNAR NO COINCIDEN");
                             System.out.println("ERROR EN: "+id+" LOS TIPOS A ASIGNAR NO COINCIDEN");
                         }
                     }else{
                         ts.setValor(id,res);         
                     }
                }
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
                if(auxsim.getTipo()==Simbolo.Tipo.FUSION){
                    if(res instanceof Fusion){
                        Fusion asigfus = (Fusion)res;
                        Fusion varfus = ar.getFusion(auxsim.getFusion());
                        if(varfus!=null){
                            if(asigfus.getPeso()==varfus.getPeso()){
                                auxsim.setValor(varfus, valoresIndices);
                            }else{
                                ar.addError("EL PESO DE LA FUSION: "+varfus.getNombre()+" NO COINCIDE;");
                                System.out.println("EL PESO DE LA FUSION: "+varfus.getNombre()+" NO COINCIDE;");
                            }
                        }else{
                            ar.addError("ERROR NO SE HA DECLARADADO LA FUSION: "+auxsim.getFusion());
                            System.out.println("ERROR NO SE HA DECLARADADO LA FUSION: "+auxsim.getFusion());
                        }
                    }else{
                        ar.addError("ERROR EN: "+id+" LOS TIPOS NO COINCIDEN");
                        System.out.println("ERROR EN: "+id+" LOS TIPOS NO COINCIDEN");
                    }
                }else{
                    auxsim.setValor(res,valoresIndices);
                }
            }
            
        }else{
            ar.addError("ERROR, LA VARIABLE: "+id+" NO ES DE TIPO FUSION");
            System.out.println("ERROR, LA VARIABLE: "+id+" NO ES DE TIPO FUSION");
        }
        return null;
    }
}


