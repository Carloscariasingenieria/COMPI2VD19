package arbol;

public class Declaracion implements Instruccion{
    ////#############VALORES DE DECLARACION-------------------------
    protected boolean parametro;
    protected final String id;
    protected Simbolo.Tipo tipo;
    public final String nombre_fusion;
    private Instruccion asig;
    ////#############VALORES DE DECLARACION-------------------------
    
    //DECLARACION TIPO ID... PARAMETROS
    public Declaracion(Object dec_tipo, String dec_identificador) {
        String aux_tipo;
        if(dec_tipo instanceof String){
            aux_tipo= (String)dec_tipo;
            switch (aux_tipo.toLowerCase()) {
                case "dec": tipo=Simbolo.Tipo.DECIMAL;
                        nombre_fusion=null;
                         break;
                case "ent": tipo=Simbolo.Tipo.ENTERO;
                        nombre_fusion=null;
                         break;
                case "rstring":  tipo=Simbolo.Tipo.CADENA;
                        nombre_fusion=null;
                         break;
                case "chr":  tipo=Simbolo.Tipo.CARACTER;
                        nombre_fusion=null;
                         break;
                case "bul": tipo=Simbolo.Tipo.BOOLEANO;
                        nombre_fusion=null;
                         break;
                case "zro": tipo=null;
                        nombre_fusion=null;
                        System.out.println("ERROR, VOID NO ES ASIGNABLE A UNA VARIABLE");
                         break;
                default:
                    tipo=Simbolo.Tipo.FUSION;
                    nombre_fusion=aux_tipo;
            }
        }else{
            nombre_fusion=null;
            tipo=null;
            System.out.println("ERROR DECLRACION DE TIPO");
        }
        this.asig=null;
        id=dec_identificador;
        parametro=false;
    }
    
    //DECLARACION CON ASIGNACION VACIA O CON INSTRUCCION... VARIABLES NORMALES Y FUSION
    public Declaracion(Object dec_tipo, String dec_identificador,Instruccion asig) {
        String aux_tipo;
        if(dec_tipo instanceof String){
            aux_tipo= (String)dec_tipo;
            switch (aux_tipo.toLowerCase()) {
                case "dec": tipo=Simbolo.Tipo.DECIMAL;
                        nombre_fusion=null;
                         break;
                case "ent": tipo=Simbolo.Tipo.ENTERO;
                        nombre_fusion=null;
                         break;
                case "rstring":  tipo=Simbolo.Tipo.CADENA;
                        nombre_fusion=null;
                         break;
                case "chr":  tipo=Simbolo.Tipo.CARACTER;
                        nombre_fusion=null;
                         break;
                case "bul": tipo=Simbolo.Tipo.BOOLEANO;
                        nombre_fusion=null;
                         break;
                case "zro": tipo=null;
                        nombre_fusion=null;
                        System.out.println("ERROR, VOID NO ES ASIGNABLE A UNA VARIABLE");
                         break;
                default:
                    tipo=Simbolo.Tipo.FUSION;
                    nombre_fusion=aux_tipo;
            }
        }else{
            nombre_fusion=null;
            tipo=null;
            System.out.println("ERROR DECLRACION DE TIPO");
        }
        this.asig=asig;
        id=dec_identificador;
        parametro=false;
    }
    
    @Override
    public Object ejecutar(TablaDeSimbolos ts,Arbol ar) {
        if(tipo==null){
            ar.addError("ERROR EN DECLARACION DE: "+this.id+" ERROR AL DECLARAR EL TIPO");
            System.out.println("ERROR EN DECLARACION DE: "+this.id+" ERROR AL DECLARAR EL TIPO");
            return null;
        }
        Simbolo aux;
        if(nombre_fusion!=null){
            aux=new Simbolo(id,nombre_fusion,tipo);  
        }else{
            aux=new Simbolo(id,tipo);
        }
        aux.setParametro(this.parametro);
        boolean addc=ts.add(aux);
        if(addc==false && asig==null){
            return null;
        }
        if(asig!=null){
            if(this.parametro){
                ts.setParametroInicializado(id);
            }
            Asignacion a=new Asignacion(id,asig);
            a.ejecutar(ts, ar);
        }
        return null;
    }
    
    //OBTENER IDENTIFICADOR DE LA DECLARACION
    public String getIdentificador() {
        return id;
    }
    
    //OBTIENE SI ES PARAMETRO
    public boolean isParametro() {
        return parametro;
    }
    
    //SE ASIGNA EL PARAMETRO
    public void setParametro(boolean parametro) {
        this.parametro = parametro;
    }
    
    //OBTENER ASIGNACION
    public Instruccion getAsig(){
        return this.asig;
    }
    
    public void setAsig(Instruccion asig){
        this.asig=asig;
    }
}
