package arbol;
import java.util.LinkedList;

public class AsignacionArreglo extends Asignacion implements Instruccion{
    ////#############VALORES DE ASIGNACION-------------------------
    private final LinkedList<Instruccion> indices;
    ////#############VALORES DE ASIGNACION-------------------------
    
    //CONSTRUCTOR ASIGNACION ARREGLO
    public AsignacionArreglo(String id, LinkedList<Instruccion> dim, Instruccion valor ) {
        super(id,valor);
        indices=dim;
    }
    
    @Override
    public Object ejecutar(TablaDeSimbolos ts,Arbol ar) {
        if(this.valor==null){
            ar.addError("ERROR ASIGNACION DE: "+this.id+" VIENE VACIA");
            System.out.println("ERROR ASIGNACION VACIA: "+this.id);
            return null;
        }
        LinkedList<Integer> valoresIndices=new LinkedList<>();
        if(indices.get(0)!=null){
            for (Instruccion dim : indices) {
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
                valoresIndices.add((Integer)er);
            }
             Simbolo auxsim=ts.getSimbol(id);
                if(auxsim.getTipo()==Simbolo.Tipo.FUSION){//si el tipo es fusion
                    Object valej= valor.ejecutar(ts, ar);
                    if(valej instanceof Fusion){
                        Fusion asigfus = (Fusion)valej;
                        Fusion varfus = ar.getFusion(auxsim.getFusion());
                        if(varfus!=null){
                            if(asigfus.getPeso()==varfus.getPeso()){
                                ts.setValor(id,varfus,valoresIndices);
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
                    ts.setValor(id,valor.ejecutar(ts,ar),valoresIndices);      
                }
        }else{
            ar.addError("ERROR AL ASIGNAR EL ARREGLO: "+this.id+" ALGUN INDICE VIENE VACIO");
            System.out.println("Error en AsignacionArreglo: indice vacio "+this.id);
        }
        return null;
    }
}
