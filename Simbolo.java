package arbol;

import java.util.LinkedList;

public class Simbolo implements Cloneable{
    ////#############VALORES DE SIMBOLOS-------------------------
    private boolean parametro;
    private boolean parametroInicializado;
    private boolean arreglo;
    private int dim_arreglo;
    private final Tipo tipo;
    private final String id;
    private String nombre_fusion;
    private final LinkedList<Integer> dim_tam;
    private Object valor;    
    private boolean is_ref=false;
    private String ref;
    private Arbol ar;
    ////#############VALORES DE SIMBOLOS-------------------------

    //#############CONSTRUCTORES--------------------------------

    //identificador, tipo... DECLARACION SIMPLE
    public Simbolo(String id, Tipo tipo) {
        this.tipo = tipo;
        if(tipo==Tipo.CARACTER){
            this.valor='\0';
        }else if(tipo==Tipo.CADENA){
            this.valor="";
        }else if(tipo==Tipo.ENTERO){
            this.valor=0;
        }else if(tipo==Tipo.DECIMAL){
            this.valor=0.0;
        }else if(tipo==Tipo.BOOLEANO){
            this.valor=false;
        }
        this.id = id;
        this.dim_tam = null;
        this.parametro=false;
        this.parametroInicializado=false;
        this.arreglo=false;
    }
    //identificador, nombrefusion, tipo... DECLARACION DE FUSION
    public Simbolo(String id, String nombre_fusion,Tipo tipo) {
        this.tipo = tipo;
        this.id = id;
        this.dim_tam = null;
        this.parametro=false;
        this.parametroInicializado=false;
        this.nombre_fusion=nombre_fusion;
        this.arreglo=false;
    }
    //identificador, tipo,listadedimensiones... DECLARACION ARREGLO
    public Simbolo(String id, Tipo tipo, LinkedList<Integer> list_dim) {
        this.tipo = tipo;
        this.id = id;
        this.dim_tam = list_dim;
        NodoArreglo arr=new NodoArreglo();
        arr.setDims(dim_tam);
        arr.inicializarNodo(list_dim.size(), 1, list_dim,tipo);
        this.valor=arr;
        this.arreglo=true;
    }
    
    //CONSTRUCTOR SIMBOLO ARREGLO CON VALORES
    public Simbolo(String id, Tipo tipo, LinkedList<Integer> list_dim,NodoArreglo valor) {
        this.tipo = tipo;
        this.id = id;
        this.dim_tam = list_dim;
        this.valor=valor;
        this.arreglo=true;
    }

    //#############CONSTRUCTORES--------------------------------
    
    //OBTENER ID DEL SIMBOLO
    public String getId() {
        return id;
    }
    
    //OBTENER VALOR DEL SIMBOLO
    public Object getValor() {
        return valor;
    }
    
    //OBTENER ID DEL FUSION
    public String getFusion() {
        return nombre_fusion;
    }
    
    //ASIGNAR NUEVO ARREGLO
    public void setValor(NodoArreglo v){
        this.valor=v;
    }
    
    //ASIGNAR VALOR AL SIMBOLO
    public void setValor(Object v) {
        
        Tipo vTipo=null;
        if(v instanceof Double) {
            vTipo = Simbolo.Tipo.DECIMAL;
        }else if (v instanceof Integer) {
            vTipo = Simbolo.Tipo.ENTERO;
        } else if(v instanceof Character) {
            vTipo = Simbolo.Tipo.CARACTER;
        }else if(v instanceof String) {
            vTipo = Simbolo.Tipo.CADENA;
        } else if(v instanceof Boolean){
            vTipo = Simbolo.Tipo.BOOLEANO;
        }else if(v instanceof Fusion){
            vTipo = Simbolo.Tipo.FUSION;
        }
        
        if(vTipo == tipo){
            valor=v;
        }else{
            System.err.println("ERROR DE TIPO AL ASIGNAR A LA VARIABLE: "+id+" DE TIPO: "+tipo.name());
        }
    }
    public void setValor (ACCFUSION V){
        this.valor=V;
    }
    
    //ASIGNAR VALOR A UN SIMBOLO ARREGLO
   public void setValor(Object v, LinkedList<Integer> indices) {
        Tipo vTipo=null;
        if(v instanceof Double) {
            vTipo = Simbolo.Tipo.DECIMAL;
        }else if (v instanceof Integer) {
            vTipo = Simbolo.Tipo.ENTERO;
        }else if(v instanceof Character) {
            vTipo = Simbolo.Tipo.CARACTER;
        }else if(v instanceof String) {
            vTipo = Simbolo.Tipo.CADENA;
        } else if(v instanceof Boolean){
            vTipo = Simbolo.Tipo.BOOLEANO;
        }else if(v instanceof Fusion){
            vTipo = Simbolo.Tipo.FUSION;
        }
        if(vTipo != tipo){
            System.err.println("ERROR DE TIPO AL ASIGNAR A EL ARREGLO: "+id+" DE TIPO: "+tipo.name());
        }
        if(this.valor instanceof NodoArreglo){
            if(this.dim_tam.size()==indices.size()){
                NodoArreglo arr=(NodoArreglo)this.valor;
                arr.setValor(indices.size(), 1, indices, v, id);
            }else{
                System.out.println("CANTIDAD DE INDICES INCORRECTOS AL ASIGNAR AL ARREGLO: "+id);            
            }
        }else{
            System.out.println("LA VARIABLE "+id+" NO ES UN ARREGLO");
        }
    }
    
    //OBTENER VALOR DE UN ARREGLO
   public Object getValor(String id, LinkedList<Integer> indices) {
        if(this.valor instanceof NodoArreglo){
            if(this.dim_tam.size()>=indices.size()){
                NodoArreglo arr=(NodoArreglo)this.valor;
                if(this.dim_tam.size()>indices.size()){
                    return arr.getValor(indices.size(), 1, indices, id);   
                }else if(this.dim_tam.size()==indices.size()){
                    try{
                        return arr.getValor(indices.size(), 1, indices, id);
                    }catch(Exception e){
                        System.out.println("Error en nodo arreglo");
                    }
                }
            }else{
                System.out.println("CANTIDAD DE INDICES INCORRECTOS AL OBTENER DEL ARREGLO: "+id);            
            }
        }else{
            System.out.println("LA VARIABLE "+id+" NO ES UN ARREGLO");
        }
        return null;
    }
   
    public boolean isIs_ref() {
        return is_ref;
    }
    
    public void setIs_ref(boolean is_ref) {
        this.is_ref = is_ref;
    }
    
    public String getRef() {
        return ref;
    }
    public Simbolo.Tipo getTipo(){
        return this.tipo;
    }
    public void setRef(String ref) {
        this.ref = ref;
    }
    
   //TIPOS DE SIMBOLO
    public static enum Tipo{
        CARACTER,
        CADENA,
        ENTERO,
        DECIMAL,
        BOOLEANO,
        FUSION,
        VOID
    }
    
    //OBTENER SI DEVUELVE UN ARREGLO
    public boolean isArreglo() {
        return this.arreglo;
    }
    //ASIGNAR SI ES ARREGLO
    public void setArreglo(boolean arreglo) {
        this.arreglo = arreglo;
    }
    
    //ASIGNAR DIMENSIONES SI ES UN ARREGLO
    public void setArrDim(int cantidad) {
        this.dim_arreglo=cantidad;
    }
    
    //OBTENER DIMENSIONES SI ES UN ARREGLO
    public int getArrDim(int cantidad) {
        return this.dim_arreglo;
    }
    
    //OBTENER SI ES PARAMETRO
    public boolean isParametro() {
        return parametro;
    }
    
    //ASIGNAR SI ES PARAMETRO
    public void setParametro(boolean parametro) {
        this.parametro = parametro;
    }
    
    //OBTENER SI EL PARAMETRO ESTA INICIALIZADO
    public boolean isParametroInicializado() {
        return parametroInicializado;
    }
    
    //ASIGNAR PARAMETRO INICIALIZADO
    public void setParametroInicializado(boolean parametroInicializado) {
        this.parametroInicializado = parametroInicializado;
    }
    
    public void setFusion(String fusion){
        this.nombre_fusion=fusion;
    }
    
    @Override
     public Object clone(){
        Object obj=null;
        try{
            obj=super.clone();
        }catch(CloneNotSupportedException ex){
            ex.printStackTrace();
        }
        return obj;
    }
     
    public void setArbol(Arbol ar){
        this.ar=ar;
    }
}
