package arbol;

import java.util.LinkedList;

public class ACCFUSION {
    private  String id;
    private final LinkedList<Instruccion> list_dim;
    
    public ACCFUSION(String id){
        this.id=id;
        list_dim=null;
    }
    
    public ACCFUSION(String id,LinkedList<Instruccion>list_dim){
        this.id=id;
        this.list_dim=list_dim;
    }
    
    public String getId() {
        return id;
    }
    public void setId(String id){
        this.id=id;
    }
    
    public LinkedList<Instruccion> getList_dim() {
        return list_dim;
    }

}
