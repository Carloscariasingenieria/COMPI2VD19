package arbol;

import java.util.LinkedList;

public class Nativa implements Instruccion{
    
    public static enum Tipo_funcion {
        PESODE,
        RESERVAR,
        ATXT,
        AENT,
        ADEC,
        EQLS,
        CONC
    }
     ////#############VALORES DE OPERACION-------------------------
    private final Tipo_funcion tipo;
    private Instruccion operadorIzq;
    private Instruccion operadorDer;
    private Object valor;
    private Object valor2;
    private int tipob;
    ////#############VALORES DE OPERACION-------------------------
    
    //CONSTRUCTOR DE OPERACIONES BINARIAS
    public Nativa(Instruccion operadorIzq, Instruccion operadorDer, Tipo_funcion tipo) {
        this.tipo = tipo;
        this.operadorIzq = operadorIzq;
        this.operadorDer = operadorDer;
    }

    //CONSTRUCTOR OPERADOR UNARIO
    public Nativa(Instruccion operadorIzq, Tipo_funcion tipo) {
        this.tipo = tipo;
        this.operadorIzq = operadorIzq;
    }
    
    //CONSTRUCTOR PESODE
    public Nativa(String valor, Tipo_funcion tipo) {
        this.tipo = tipo;
        this.valor=valor;
    }
    
    //CONSTRUCTOR CONCATENAR
    public Nativa(String valor,String valor2, Tipo_funcion tipo,int tipob) {
        this.tipo = tipo;
        this.valor=valor;
        this.valor2=valor2;
        this.tipob=tipob;
    }

    @Override
    public Object ejecutar(TablaDeSimbolos ts, Arbol ar) {
        Object a = (operadorIzq == null) ? null : operadorIzq.ejecutar(ts, ar);
        Object b = (operadorDer == null) ? null : operadorDer.ejecutar(ts, ar);
        
        if(tipo == Nativa.Tipo_funcion.PESODE) 
        {//OPERACION PESODE
            int auxfus = ar.getPesoFusion(valor.toString());
            if(auxfus==-1){
                Object auxobj = ts.getValor(valor.toString());
                if(auxobj instanceof NodoArreglo){
                    NodoArreglo auxarr = (NodoArreglo)auxobj;
                    if(auxarr.getDims().size()==1){
                        auxfus=auxarr.getDims().get(0);
                    }else{
                        auxfus=auxarr.getDims().size();
                    }
                }else if(auxobj instanceof Fusion){
                    Fusion auxfuss = (Fusion)auxobj;
                    auxfus=auxfuss.getPeso();
                }else if(auxobj instanceof Double || auxobj instanceof Integer || auxobj instanceof Character || auxobj instanceof String || auxobj instanceof Boolean){
                    auxfus=1;
                }else{
                    ar.addError("ERROR EN TIPO DE PESO");
                    System.out.println("ERROR EN TIPO DE PESO..");
                    return null;
                }
            }
            return auxfus;
        }else if(tipo == Nativa.Tipo_funcion.RESERVAR)
        {//OPERACION RESERVAR
            if(a instanceof Double){
                Double val=(Double)a;
                Fusion reserva = new Fusion("reserva", val.intValue());
                return reserva;
            }else if( a instanceof Integer){
                Integer val=(Integer)a;
                Fusion reserva = new Fusion("reserva", val);
                return reserva;
            }
        }else if(tipo == Nativa.Tipo_funcion.ATXT)
        {//OPERACION ATEXTO
            String valatxt;
            if (a instanceof Double)
            {
                valatxt=Double.toString((Double)a);
            }else if(a instanceof Integer){
                valatxt=Integer.toString((Integer)a);
            }else if(a instanceof String){
                valatxt=(String)a;
            }else {
                ar.addError("ERROR DE TIPOS EN CONVERTIR A ARREGLO DE CARACTERES");
                System.err.println("ERROR DE TIPOS EN CONVERTIR A ARREGLO");
                return null;
            }
            LinkedList<Object> vals= new LinkedList<>();
            //CREAR UNA LISTA SIMPLE DE TODOS LOS VALORES
            for(int i=0;i<valatxt.length();i++){
                vals.add(valatxt.toCharArray()[i]);
            }
            LinkedList<Integer> arr_dim= new LinkedList<>();
            arr_dim.add(valatxt.length());
            //CREAR UN NODO ARREGLO
            int [] contador={0};
            NodoArreglo arr=new NodoArreglo();
            arr.setDims(arr_dim);
            arr.inicializarNodo(arr_dim.size(), 1, arr_dim, vals, contador);
            return arr;
        }else if(tipo == Nativa.Tipo_funcion.AENT)
        {//OPERACION AENTERO
            int valent=0;
            if (a instanceof NodoArreglo)
            {
                NodoArreglo aux;
                aux=(NodoArreglo)a;
                if(validarNodo(aux)){
                    try{
                        String izq=getString(aux);
                        valent=Integer.parseInt(izq);
                    }catch(Exception e){
                        ar.addError("NO SE PUEDE PARSEAR ESO A NUMERO");
                        System.out.println("NO SE PUEDE PARSEAR ESO A NUMERO");
                    }
                }else{
                    ar.addError("SEGUNDO ARREGLO NO ES DE CARACTERES");
                    System.out.println("SEGUNDO ARREGLO NO ES DE CARACTERES");
                    return null;
                }
            }else if(a instanceof String){
                try{
                    valent=Integer.parseInt((String)a);
                }catch(Exception e){
                    ar.addError("NO SE PUEDE PARSEAR ESO A NUMERO");
                    System.out.println("NO SE PUEDE PARSEAR ESO A NUMERO");
                }
            }else {
                ar.addError("ERROR DE TIPOS EN CONVERTIR A ARREGLO");
                System.err.println("ERROR DE TIPOS EN CONVERTIR A ARREGLO");
                return null;
            }
            return valent;
        }else if(tipo == Nativa.Tipo_funcion.ADEC)
        {//OPERACION ADECIMAL
            double valdec=0;
             if (a instanceof NodoArreglo)
            {
                NodoArreglo aux;
                aux=(NodoArreglo)a;
                if(validarNodo(aux)){
                    try{
                        String izq=getString(aux);
                        valdec=Double.parseDouble(izq);
                    }catch(Exception e){
                        ar.addError("NO SE PUEDE PARSEAR ESO A NUMERO");
                        System.out.println("NO SE PUEDE PARSEAR ESO A NUMERO");
                    }
                }else{
                    ar.addError("SEGUNDO ARREGLO NO ES DE CARACTERES");
                    System.out.println("SEGUNDO ARREGLO NO ES DE CARACTERES");
                    return null;
                }
            }else if(a instanceof String){
                try{
                    valdec=Double.parseDouble((String)a);
                }catch(Exception e){
                    ar.addError("NO SE PUEDE PARSEAR ESO A NUMERO");
                    System.out.println("NO SE PUEDE PARSEAR ESO A NUMERO");
                }
            }else {
                ar.addError("ERROR DE TIPOS EN CONVERTIR A DECIMAL");
                System.err.println("ERROR DE TIPOS EN CONVERTIR A DECIMAL");
                return null;
            }
            return valdec;
        }else if(tipo == Nativa.Tipo_funcion.EQLS)
        {//OPERACION IGUAL
            if(a instanceof String && b instanceof String){
                String izq=(String)a;
                String der =(String)b;
                return izq.equals(der);
            }else if(a instanceof NodoArreglo && b instanceof String){
                if(validarNodo((NodoArreglo)a)){
                    String izq=getString((NodoArreglo)a);
                    String der =(String)b;
                    return izq.equals(der);
                }else{
                    ar.addError("PRIMER ARREGLO NO ES DE CARACTERES");
                    System.out.println("PRIMER ARREGLO NO ES DE CARACTERES");
                    return null;
                }
            }else if(a instanceof String && b instanceof NodoArreglo){
                if(validarNodo((NodoArreglo)b)){
                    String izq=getString((NodoArreglo)b);
                    String der =(String)a;
                    return izq.equals(der);
                }else{
                    ar.addError("SEGUNDO ARREGLO NO ES DE CARACTERES");
                    System.out.println("SEGUNDO ARREGLO NO ES DE CARACTERES");
                    return null;
                }
            }else if(a instanceof NodoArreglo && b instanceof NodoArreglo){
                if(validarNodo((NodoArreglo)a) && validarNodo((NodoArreglo)b)){
                    String izq=getString((NodoArreglo)a);
                    String der=getString((NodoArreglo)b);
                    return izq.equals(der);
                }else{
                    ar.addError("LOS ARREGLOS NO SON DE CARACTERES");
                    System.out.println("LOS ARREGLOS NO SON DE CARACTERES");
                    return null;
                }
            }else {
                ar.addError("ERROR DE TIPOS EN CONVERTIR A ARREGLO");
                System.err.println("ERROR DE TIPOS EN CONVERTIR A ARREGLO");
                return null;
            }
        }else if(tipo == Nativa.Tipo_funcion.CONC)
        {//OPERACION CONCATENAR
            a= ts.getValor(valor.toString());
            if(tipob==0){
                b= ts.getValor(valor2.toString());
            }else{
                b= this.valor2.toString();
            }
            if(a instanceof NodoArreglo && (b instanceof NodoArreglo | b instanceof String)){
                if(validarNodo((NodoArreglo)a)){
                    if(b instanceof NodoArreglo){
                        if(validarNodo((NodoArreglo)b)){
                            LinkedList<Integer> indices = new LinkedList<>();
                            indices.add(0);
                            NodoArreglo izq = (NodoArreglo)a;
                            NodoArreglo der = (NodoArreglo)b;
                            int cont=izq.getDims().get(0);
                            int contaux = der.getDims().get(0);
                            int ultimo = getUltimo(izq);
                            if(ultimo==-1){
                                ar.addError("ERROR YA NO HAY ESPACIO EN EL ARREGLO");
                                System.out.println("ERROR YA NO HAY ESPACION EN EL ARREGLO");
                            }
                            if(cont<(ultimo+contaux)){
                                ar.addError("ERROR DE ESPACIO EN EL ARREGLO");
                                System.out.println("ERROR DE ESPACIO EN EL ARREGLO");
                            }
                            String conc = getString(der);
                            int contador=0;
                            for(int i=ultimo;i<(ultimo+contaux);i++){
                                indices.set(0, i);
                                izq.setValor(1, 1, indices,conc.toCharArray()[contador], null);
                                contador++;
                            }
                            ts.setValor(valor.toString(),izq);
                        }else{
                            ar.addError("ERROR EL SEGUNDO ARREGLO NO ES CADENA");
                            System.out.println("ERROR EL SEGUNDO ARREGLO NO ES CADENA");
                            return null;        
                        }
                    }else if(b instanceof String){
                        LinkedList<Integer> indices = new LinkedList<>();
                        indices.add(0);
                        NodoArreglo izq = (NodoArreglo)a;
                        String conc = (String)b;
                        int cont=izq.getDims().get(0);
                        int contaux = conc.length();
                        int ultimo = getUltimo(izq);
                        if(ultimo==-1){
                            ar.addError("ERROR YA NO HAY ESPACIO EN EL ARREGLO");
                            System.out.println("ERROR YA NO HAY ESPACION EN EL ARREGLO");
                        }
                        if(cont<(ultimo+contaux)){
                            ar.addError("ERROR DE ESPACIO EN EL ARREGLO");
                            System.out.println("ERROR DE ESPACIO EN EL ARREGLO");
                        }
                        int contador=0;
                        for(int i=ultimo;i<(ultimo+contaux);i++){
                            indices.set(0, i);
                            izq.setValor(1, 1, indices,conc.toCharArray()[contador], null);
                            contador++;
                        }
                        ts.setValor(valor.toString(),izq);
                    }
                }else{
                    ar.addError("ERROR EL PRIMER ARREGLO NO ES CADENA");
                    System.out.println("ERROR EL PRIMER ARREGLO NO ES CADENA");
                    return null;
                }
            }else if(a instanceof String && (b instanceof NodoArreglo | b instanceof String)){
                 if(b instanceof NodoArreglo){
                        if(validarNodo((NodoArreglo)b)){
                            String izq =(String)a;
                            NodoArreglo der = (NodoArreglo)b;
                            String conc = getString(der);
                            izq = izq+conc;
                            ts.setValor(valor.toString(),izq);
                        }else{
                            ar.addError("ERROR EL SEGUNDO ARREGLO NO ES CADENA");
                            System.out.println("ERROR EL SEGUNDO ARREGLO NO ES CADENA");
                            return null;        
                        }
                    }else if(b instanceof String){
                        String izq =(String)a;
                        String conc = (String)b;
                        izq = izq+conc;
                        ts.setValor(valor.toString(),izq);
                    }
            }
        }else{
            return null;
        }
        return null;
    }
    
    private boolean validarNodo(NodoArreglo arr){
        LinkedList<Integer> indices = new LinkedList<>();
        indices.add(0);
        int auxsize=arr.getDims().size();
        //VALIDAR SI A ES UN ARREGLO DE CARACTERES
        if(auxsize==1){
            Object valaux = arr.getValor(arr.getDims().size(), 1, indices, "");
            if(valaux instanceof Character){
                return true;
            }
        }           
        return false;
    }
    
    private String getString(NodoArreglo arr){
        String res="";
        LinkedList<Integer> indices = new LinkedList<>();
        indices.add(0);
        int cont=arr.getDims().get(0);
        for(int i=0;i<cont;i++){
            indices.set(0, i);
            Object val =arr.getValor(1, 1, indices, "");
            if(val!=null){
             res+=(Character)val;   
            }
        }    
        return res;
    }
    
    private int getUltimo(NodoArreglo arr){
        LinkedList<Integer> indices = new LinkedList<>();
        int cont=arr.getDims().get(0);
        Object res;
        for(int i=0;i<cont;i++){
            indices.set(0, i);
            res=arr.getValor(1, 1, indices, "");
            if(res==null){
                return i;
            }
        }    
        return -1;
    }
    
}

