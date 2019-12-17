package arbol;
import java.util.LinkedList;

public class If implements Instruccion{
    ////#############VALORES DE IF-------------------------
    private final LinkedList<Instruccion> subIfs;
    ////#############VALORES DE IF-------------------------

    //IF NORMAL DE TODA LA VIDA  SIN ELSE
    public If(SubIf subif) {
        subIfs=new LinkedList<>();
        subIfs.add(subif);
    }
    
    //IF CON LISTA DE ELSE IF SIN ELSE
    public If(SubIf subif, LinkedList<SubIf> lelseif) {
        subIfs=new LinkedList<>();
        subIfs.add(subif);
        subIfs.addAll(lelseif);
    }
    
    //IF CON LISTA DE ELSE IF CON ELSE
    public If(SubIf subif, LinkedList<SubIf> lelseif, SubIf subelse) {
        subIfs=new LinkedList<>();
        subIfs.add(subif);
        subIfs.addAll(lelseif);
        subIfs.add(subelse);
    }
    
    //IF DE TODA LA VIDA CON ELSE
    public If(SubIf subif, SubIf subelse) {
        subIfs=new LinkedList<>();
        subIfs.add(subif);
        subIfs.add(subelse);
    }
    
    @Override
    public Object ejecutar(TablaDeSimbolos ts,Arbol ar) {
        //Ejecutar SubIfs
        Object r;
        for(Instruccion in: subIfs){
            r=in.ejecutar(ts,ar);
            try{
            if(((SubIf)in).getValorCondicion())
                return r;
            }catch(Exception e){
                return null;
            }
        }
        return null;
    }
}
