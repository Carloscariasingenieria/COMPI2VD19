package arbol;
public class Asignacion implements Instruccion{
    
    ////#############VALORES DE ASIGNACION-------------------------
    protected TablaDeSimbolos tablaPadre;
    protected final String id;
    protected final Instruccion valor;
    ////#############VALORES DE ASIGNACION-------------------------
    
    //CONSTRUCTOR ASIGNACION
    public Asignacion(String identificador, Instruccion valor) {
        this.id=identificador;
        this.valor=valor;
    }
    
    //ASIGNAR TABLA DE SIMBOLOS PADRE
    public void setTablaDeSimbolosPadre(TablaDeSimbolos ts) {
        this.tablaPadre = ts;
    }
    
    //EJECUTAR ASIGNACION
    @Override
    public Object ejecutar(TablaDeSimbolos ts, Arbol ar) {
        if(this.valor==null){
            ar.addError("ERROR ASIGNACION DE: "+this.id+" VIENE VACIA");
            System.out.println("ERROR ASIGNACION VACIA: "+this.id);
            return null;
        }
        if(tablaPadre != null){ //SETAR VALOR PARA UN PARAMETRO
            ts.setValor(id,valor.ejecutar(tablaPadre,ar));
        }else{
            Object res = valor.ejecutar(ts, ar);
            if(res instanceof Simbolo){//SI DEVUELVE SIMBOLO ES REFERENCIA
                Simbolo ref = (Simbolo)res;
                ts.getSimbol(id).setIs_ref(true);
                ts.getSimbol(id).setRef(ref.getId());
            }else{
                Simbolo auxsim=ts.getSimbol(id);//SIMBOLO DE LA VARIABLE
                if(auxsim==null){
                    ar.addError("ERROR: "+this.id+" NO EXISTE EN LA TABLA DE SIMBOLOS");
                    System.out.println("ERROR: "+this.id+" NO EXISTE EN LA TABLA DE SIMBOLOS");
                    return null;
                }
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
                        ar.addError("ERROR EN: "+id+" LOS TIPOS NO COINCIDEN");
                        System.out.println("ERROR EN: "+id+" LOS TIPOS NO COINCIDEN");
                    }
                }else{
                    ts.setValor(id,res);         
                }
            }
        }
        
        return null;
    }
    
}
