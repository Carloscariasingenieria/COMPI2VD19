package arbol;

public class Definir implements Instruccion{
    ////#############VALORES DE DEFINICION-------------------------
    protected final String id;
    protected final Simbolo.Tipo tipo;
    public final String nombre_fusion;
    private final Instruccion asig;
    ////#############VALORES DE DEFINICION-------------------------
    
    //DECLARACION CON ASIGNACION VACIA O CON INSTRUCCION... VARIABLES NORMALES Y FUSION
    public Definir(String dec_identificador,Instruccion asig) {
        this.asig=asig;
        id=dec_identificador;
        tipo=null;
        nombre_fusion=null;
    }

    @Override
    public Object ejecutar(TablaDeSimbolos ts, Arbol ar) {
        if(asig!=null){
            Object v= asig.ejecutar(ts, ar);
            NodoArreglo arr=null;
            if(v instanceof NodoArreglo){
                arr=(NodoArreglo)v;
                v=arr.getFirst();
            }
            Simbolo.Tipo vTipo;
            if (v instanceof Double) {
                vTipo = Simbolo.Tipo.DECIMAL;
            }else if(v instanceof Integer){
                vTipo = Simbolo.Tipo.ENTERO;   
            }else if(v instanceof Character) {
                vTipo = Simbolo.Tipo.CARACTER;
            }else if(v instanceof String) {
                vTipo = Simbolo.Tipo.CADENA;
            } else if(v instanceof Boolean){
                vTipo = Simbolo.Tipo.BOOLEANO;
            }else if(v instanceof Fusion){
                vTipo = Simbolo.Tipo.FUSION;
            }else{
                ar.addError("ERROR DE TIPO AL DECLARAR: "+this.id);
                System.out.println("ERROR DE TIPO AL DECLARAR");
                return null;
            }
            Simbolo aux;
            aux=new Simbolo(id,vTipo);
            aux.setParametro(false);
            if(arr!=null){
                aux.setValor(arr);    
            }else{
                aux.setValor(v);
            }
            ts.add(aux);
        }
        
        return null;
    }
}
