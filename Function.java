package arbol;

import java.util.LinkedList;

public class Function implements Instruccion {
    
    ////#############VALORES DE NODO FUNCION-------------------------
    private final Simbolo.Tipo tipo;
    private final String identificador;
    private final String nombre_fusion;
    private final LinkedList<Declaracion> parametros;
    private final Dim arreglo_tipo;
    private boolean esArreglo;
    private LinkedList<Instruccion> valoresParametros;
    private final LinkedList<Instruccion> instrucciones;
    ////#############VALORES DE NODO FUNCION-------------------------

    ////#############CONSTRUCTORES-------------------------

    //CONSTRUCTOR DE FUNCION CON PARAMETROS
    public Function(Object func_tipo, String func_id, LinkedList<Declaracion> func_params, LinkedList<Instruccion> func_instr) {
        String aux_tipo;
        if(func_tipo instanceof String){
            aux_tipo= (String)func_tipo;
            arreglo_tipo=null;
            esArreglo=false;
        }else{
            Dim dim_tipo = (Dim)func_tipo;
            arreglo_tipo = dim_tipo;
            aux_tipo = dim_tipo.tipo;
            esArreglo=true;
        }
        
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
            case "zro": tipo=Simbolo.Tipo.VOID;
                    nombre_fusion=null;
                     break;
            default:
                tipo=Simbolo.Tipo.FUSION;
                nombre_fusion=aux_tipo;
        }
        identificador=func_id;
        parametros=func_params;
        instrucciones=func_instr;
    }
  
    //CONSTRUCTOR DE FUNCION SIN PARAMETROS
    public Function(Object func_tipo, String func_id, LinkedList<Instruccion> func_instr) {
        String aux_tipo;
        if(func_tipo instanceof String){
            aux_tipo= (String)func_tipo;
            arreglo_tipo=null;
            esArreglo=false;
        }else{
            Dim dim_tipo = (Dim)func_tipo;
            arreglo_tipo = dim_tipo;
            aux_tipo = dim_tipo.tipo;
            esArreglo=true;
        }
        
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
            case "zro": tipo=Simbolo.Tipo.VOID;
                    nombre_fusion=null;
                     break;
            default:
                tipo=Simbolo.Tipo.FUSION;
                nombre_fusion=aux_tipo;
        }
        identificador=func_id;
        parametros=new LinkedList<>();
        instrucciones=func_instr;
    }
    
    //EJECUTAR FUNCION
    @Override
    public Object ejecutar(TablaDeSimbolos ts,Arbol ar) {
        //LLENAR TABLA DE SIMBOLOS DE AMBITO LOCAL
        TablaDeSimbolos tablaLocal=new TablaDeSimbolos(); 
        tablaLocal.addAll(ts); 
        //LLENAR TABLA DE SIMBOLOS DE AMBITO LOCAL
        
        //si los parametros son iguales a los valores
        if(parametros.size()==valoresParametros.size()){
            //declaracion e inicializacion de cada parametro
            for(int i=0;i<parametros.size();i++){
                if(parametros.get(i) instanceof DeclaracionArreglo){
                    DeclaracionArreglo d = (DeclaracionArreglo) parametros.get(i);
                    d.setParametro(true);
                    d.setAsigArr(valoresParametros.get(i));
                    d.ejecutar(tablaLocal, ar);
                    tablaLocal.setParametroInicializado(d.getIdentificador());
                }else if(parametros.get(i) instanceof Declaracion){
                    Declaracion d=parametros.get(i); 
                    d.setParametro(true);
                    d.setAsig(valoresParametros.get(i));
                    d.ejecutar(tablaLocal, ar);
                    tablaLocal.setParametroInicializado(d.getIdentificador());
                }else{
                    ar.addError("ERROR EN DECLARACION DE PARAMETROS DE LA FUNCION: "+this.identificador);
                    System.out.println("ERROR EN DECLARACION DE PARAMETROS DE LA FUNCION: "+this.identificador);
                    return null;
                }
            }
            //EJECUTAR INSTRUCCIONES DE FUNCION
            for(Instruccion ins:instrucciones){
                Object r;
                r=ins.ejecutar(tablaLocal,ar);
                if(r!=null){
                    if(r instanceof Break || r instanceof Siga){
                        ar.addError("NO PUEDE VENIR UN ROMPER O SIGA FUERA DE UN CILCO EN LA FUNCION: "+this.identificador);
                        System.out.println("NO PUEDE VENIR UN ROMPER O SIGA FUERA DE UN CILCO EN LA FUNCION: "+this.identificador);
                        return null;
                    }
                    return r;
                }
            }            
        }else{
            ar.addError("ERROR EN NUMERO DE PARAMETROS PARA LA FUNCION: "+identificador);
            System.out.println("ERROR EN NUMERO DE PARAMETROS PARA LA FUNCION: "+identificador);
        }
        return null;
    }
    
    //OBTENER IDENTIFICADOR DE LA FUNCION
    public String getIdentificador() {
        
        //IDENTIFICADOR DE LA FUNCION
        String id = "_" + identificador + "(";
        if (parametros != null) {
            for(Declaracion parametro: parametros) {
                if(parametro instanceof DeclaracionArreglo){
                    if(parametro.tipo==Simbolo.Tipo.CARACTER){
                        id += "_" + Simbolo.Tipo.CADENA.name();
                    }else{
                        id += "_" + parametro.tipo.name();
                    }
                }else{
                    id += "_" + parametro.tipo.name();    
                }
                
            }
        }
        id += ")";
        
        return id.toLowerCase();
    }
    
    //LISTA DE PARAMETROS A ASIGNAR EN LA FUNCION
    public void setValoresParametros(LinkedList<Instruccion> a) {
        valoresParametros=a;
    }
    
    //OBTENER EL TIPO DE LA FUNCION
    public Simbolo.Tipo getTipo() {
        return tipo;
    }
    
    //OBTENER SI ES UN ARREGLO LO QUE DEVUELVE
    public boolean isArreglo() {
        return esArreglo;
    }
    
    public Dim getDim(){
        return arreglo_tipo;
    }
    public String getNombreFusion(){
        return nombre_fusion;
    }
}
