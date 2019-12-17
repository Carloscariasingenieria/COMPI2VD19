package arbol;
import java.util.LinkedList;

public class TablaDeSimbolos extends LinkedList<Simbolo>{
    private Arbol ar;
    //CONSTRUCTOR DE UNA LISTA ENLAZADA
    public TablaDeSimbolos() {
        super();
    }
    
    //OBTENER UN SIMBOLO
    public Simbolo getSimbol(String id){
        for (int i = this.size()-1; i >= 0; i--) {
            Simbolo s=this.get(i);
            if(s.isParametro() && s.isParametroInicializado() || !s.isParametro()){
                if(s.getId().equals(id)){
                    return s;
                }
            }
        }
        System.out.println("ERROR NO EXISTE: "+id+" EN ESTE AMBITO");
        return null;
    }
    //VERIFICAR SIMBOLO
    public Boolean testSimbol(String id){
        for (int i = this.size()-1; i >= 0; i--) {
            Simbolo s=this.get(i);
            if(s.getId().equals(id)){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean add(Simbolo e) {
        Boolean test = testSimbol(e.getId());
        if(test){
            //System.out.println("ERROR SIMBOLO YA DEFINIDO EN LA TABLA DE SIMBOLOS LOCAL");
            return false;
        }else{
            return super.add(e);
        }
    }
    //OBTENER VALOR DE UN SIMBOLO
    Object getValor(String id){
        for (int i = this.size()-1; i >= 0; i--) {
            Simbolo s=this.get(i);
            s.setArbol(ar);
            if(s.isParametro() && s.isParametroInicializado() || !s.isParametro()){
                if(s.getId().equals(id)){
                    if(s.isIs_ref()){
                        return this.getSimbol(s.getRef()).getValor();
                    }else{
                        return s.getValor();   
                    }
                }
            }
        }
        System.out.println("ERROR NO EXISTE: "+id+" EN ESTE AMBITO");
        return null;
    }
    
    //OBTENER VALOR DE UN ARREGLO
    Object getValor(String id, LinkedList<Integer> indices) {
        for(Simbolo s:this){
            if(s.getId().equals(id)){
                if(s.isIs_ref()){
                    Simbolo saux=this.getSimbol(s.getRef());
                    saux.setArbol(ar);
                    return saux.getValor(id,indices);
                }else{
                    s.setArbol(ar);
                    return s.getValor(id, indices);   
                }
            }
        }
        System.out.println("ERROR NO EXISTE: "+id+" EN ESTE AMBITO");
        return null;
    }
    
    //ASIGNAR NUEVO ARREGLO
    void setValor(String id, NodoArreglo valor){
        for (int i = this.size()-1; i >= 0; i--) {
            Simbolo s=this.get(i);
            if(s.getId().equals(id)){
                if(s.isIs_ref()){
                    Simbolo saux=this.getSimbol(s.getRef());
                    saux.setArbol(ar);
                    saux.setValor(valor);
                }else{
                    s.setArbol(ar);
                    s.setValor(valor);   
                }
                return;
            }
        }
        System.out.println("ERROR NO EXISTE: "+id+" EN ESTE AMBITO");
    }
    
    //ASIGNAR VALOR SIMPLE
    void setValor(String id, Object valor) {
        for (int i = this.size()-1; i >= 0; i--) {
            Simbolo s=this.get(i);
            if(s.getId().equals(id)){
                if(s.isIs_ref()){
                    Simbolo saux=this.getSimbol(s.getRef());
                    saux.setArbol(ar);
                    saux.setValor(valor);
                }else{
                    s.setArbol(ar);
                    s.setValor(valor);
                }
                return;
            }
        }
        System.out.println("ERROR NO EXISTE: "+id+" EN ESTE AMBITO");
    }
    //ASIGNAR VALOR A ARREGLO
    void setValor(String id, Object valor, LinkedList<Integer> indices) {
        for(Simbolo s:this){
            if(s.getId().equals(id)){
                if(s.isIs_ref()){
                    Simbolo saux=this.getSimbol(s.getRef());
                    saux.setArbol(ar);
                    saux.setValor(valor,indices);
                }else{
                    s.setArbol(ar);
                    s.setValor(valor,indices);   
                }
                return;
            }
        }
        System.out.println("ERROR NO EXISTE: "+id+" EN ESTE AMBITO");
    }
    
    //INICIALIZA PARAMETRO
    void setParametroInicializado(String id) {
        for (int i = this.size()-1; i >= 0; i--) {
            Simbolo s=this.get(i);
            if(s.getId().equals(id)){
                s.setArbol(ar);
                s.setParametroInicializado(true);
                return;
            }
        }
        System.out.println("El parámtro "+id+" que quiere marcar como inicializado no existe en este ámbito, por lo "
                + "que no puede marcar.");
    }
   
    public void setArbol(Arbol ar){
        this.ar=ar;
    }
}