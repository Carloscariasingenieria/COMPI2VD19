package arbol;

import java.util.LinkedList;

public class Operacion implements Instruccion {

    public static enum Tipo_operacion {
        NEGATIVO,
        SUMA,
        RESTA,
        INCREMENTO,
        DECREMENTO,
        MULTIPLICACION,
        DIVISION,
        MAYOR_QUE,
        MENOR_QUE,
        MAYOR_IGUAL_QUE,
        MENOR_IGUAL_QUE,
        DIFERENTE_QUE,
        IGUAL_QUE,
        NOT,
        AND,
        OR,
        MODULO,
        POTENCIA,
        NUMERO,
        CADENA,
        CARACTER,
        TRUE,
        FALSE,
        IDENTIFICADOR,
        REFERENCIA,
        NODOARREGLO,
        NULO
    }
    ////#############VALORES DE OPERACION-------------------------
    private final Tipo_operacion tipo;
    private Instruccion operadorIzq;
    private Instruccion operadorDer;
    private Object valor;
    LinkedList<Instruccion> indices;
    ////#############VALORES DE OPERACION-------------------------

    //CONSTRUCTOR DE OPERACIONES BINARIAS
    public Operacion(Instruccion operadorIzq, Instruccion operadorDer, Tipo_operacion tipo) {
        this.tipo = tipo;
        this.operadorIzq = operadorIzq;
        this.operadorDer = operadorDer;
    }

    //CONSTRUCTOR OPERACIONES UNARIAS
    public Operacion(Instruccion operadorIzq, Tipo_operacion tipo) {
        this.tipo = tipo;
        this.operadorIzq = operadorIzq;
    }

    //CONSTRUCTOR STRING E IDENTIFICADOR
    public Operacion(String a, Tipo_operacion tipo) {
        this.valor = a;
        this.tipo=tipo;
    }
    
    //CONSTRUCTOR STRING E IDENTIFICADOR
    public Operacion(String a, Tipo_operacion tipo,LinkedList<Instruccion> indices){
        this.valor = a;
        this.tipo=tipo;
        this.indices=indices;
    }
    
    //CONSTRUCTOR CARACTER
    public Operacion(char a, Tipo_operacion tipo) {
        this.valor = a;
        this.tipo=tipo;
    }
    
    //CONSTRUCTOR NUMERO
    public Operacion(Double a) {
        this.valor = a;
        this.tipo = Tipo_operacion.NUMERO;
    }
    //CONSTRUCTOR NUMERO
    public Operacion(Integer a) {
        this.valor = a;
        this.tipo = Tipo_operacion.NUMERO;
    }
    
    //CONSTRUCTOR NODOARREGLO
    public Operacion(LinkedList<Object> nodo_arr,Tipo_operacion tipo) {
        this.valor = nodo_arr;
        this.tipo = tipo;
    }

    //EJECUTAR OPERACION
    @Override
    public Object ejecutar(TablaDeSimbolos ts, Arbol ar) {
        Object a = (operadorIzq == null) ? null : operadorIzq.ejecutar(ts, ar);
        Object b = (operadorDer == null) ? null : operadorDer.ejecutar(ts, ar);
        if(tipo == Tipo_operacion.SUMA) 
        {//OPERACION SUMA
            if (a instanceof Double && (b instanceof Double||b instanceof Integer)) { //NUM Y NUM
                Double vald = Double.parseDouble(b.toString());
                return (Double) a + vald;
            }else if (b instanceof Double && (a instanceof Double||a instanceof Integer)) { //NUM Y NUM
                Double vald = Double.parseDouble(a.toString());
                return (Double)b + vald;
            }else if (a instanceof Integer && b instanceof Integer) { //NUM Y NUM
                return (Integer)a + (Integer)b;
            }else if (a instanceof Double && b instanceof Character) { //NUM Y CHAR
                return (Double) a + (char) b;
            }else if (a instanceof Integer && b instanceof Character) { //NUM Y CHAR
                return (Integer) a + (char) b;
            }else if (a instanceof Character && b instanceof Double) { //CHAR Y NUM
                return (char) a + (Double) b;
            }else if (a instanceof Character && b instanceof Integer) { //CHAR Y NUM
                return (char) a + (Integer) b;
            }else if (a instanceof Character && b instanceof Character) { //CHAR Y CHAR
                int res = (char) a + (char) b;
                return res; 
            }else {
                System.err.println("ERROR DE TIPOS EN SUMA");
                return null;
            }
        }else if (tipo == Tipo_operacion.RESTA) 
        {//OPERACION RESTA
            if (a instanceof Double && (b instanceof Double||b instanceof Integer)) { //NUM Y NUM
                Double vald = Double.parseDouble(b.toString());
                return (Double) a - vald;
            }else if (b instanceof Double && (a instanceof Double||a instanceof Integer)) { //NUM Y NUM
                Double vald = Double.parseDouble(a.toString());
                return (Double)b - vald;
            }else if (a instanceof Integer && b instanceof Integer) { //NUM Y NUM
                return (Integer)a - (Integer)b;
            }else if (a instanceof Double && b instanceof Character) { //NUM Y CHAR
                return (Double) a - (char) b;
            }else if (a instanceof Integer && b instanceof Character) { //NUM Y CHAR
                return (Integer) a - (char) b;
            }else if (a instanceof Character && b instanceof Double) { //CHAR Y NUM
                return (char) a - (Double) b;
            }else if (a instanceof Character && b instanceof Integer) { //CHAR Y NUM
                return (char) a - (Integer) b;
            }else if (a instanceof Character && b instanceof Character) { //CHAR Y CHAR
                int res = (char) a - (char) b;
                return res; 
            }else {
                System.err.println("ERROR DE TIPOS EN RESTA");
                return null;
            }
        }else if(tipo == Tipo_operacion.INCREMENTO) 
        {//OPERACION INCREMENTO
            if(a!=null){
                if (a instanceof Double ) { //NUM
                    return (Double) a +1;
                }else {
                    System.err.println("ERROR DE TIPOS EN INCREMENTO");
                    return null;
                }
            }else{
                if (valor instanceof String ) { 
                    if(indices==null){
                        Object res = ts.getValor(valor.toString());
                        if(res instanceof Double || res instanceof Integer){
                            double resultado;
                            if(res instanceof Double){
                                resultado=  (Double) res +1;    
                            }else{
                                resultado =  (Integer)res +1;
                            }
                            
                            ts.setValor(valor.toString(),resultado);
                            return null;
                        }else{
                            System.out.println("ERROR DE INCREMENTO, VARIABLE NO ES NUMERO");
                            return null;
                        }    
                    }else{
                        Object res = ts.getValor(valor.toString());
                        if(res instanceof NodoArreglo){
                            NodoArreglo resarr = (NodoArreglo)res;
                            LinkedList<Integer> valoresIndices=new LinkedList<>();
                            for (Instruccion dim : indices) {
                                Object er=dim.ejecutar(ts, ar);
                                //Se comprueba que cada indice para acceder al arreglo sea de tipo numerico
                                if(!(er instanceof Double)){
                                    System.err.println("Los indices para acceder a un arreglo deben ser de tipo numérico. El indice ["+String.valueOf(er)+"] no es numérico.");
                                    return null;
                                }
                                if(er instanceof Double){
                                    Double aux1=(Double)er;
                                    int aux = aux1.intValue();
                                    if((aux1%aux)!=0){
                                        System.err.println("LOS INDICES NO PUEDEN SER DECIMALES");
                                        return null;    
                                    }
                                }
                                valoresIndices.add(((Double)er).intValue());
                            }
                            Object resnum = resarr.getValor(valoresIndices.size(),1, valoresIndices,valor.toString());
                            if(resnum instanceof Double){
                                double resultado =  (Double) resnum + 1;
                                ts.setValor(valor.toString(),resultado,valoresIndices);
                                return null;
                            }else{
                                System.out.println("ERROR EL ARREGLO NO ES DE NUMEROS");
                                return null;
                            }
                        }
                    }
                }else {
                    System.err.println("ERROR DE TIPOS EN INCREMENTO");
                    return null;
                }
            }
        }else if(tipo == Tipo_operacion.DECREMENTO) 
        {//OPERACION DECREMENTO
            if(a!=null){
                if (a instanceof Double || a instanceof Integer) { //NUM
                    if(a instanceof Double){
                     return (Double) a -1;   
                    }else{
                     return (Integer)a -1;   
                    }
                }else {
                    System.err.println("ERROR DE TIPOS EN DECREMENTO");
                    return null;
                }
            }else{
                if (valor instanceof String ) { 
                    if(indices==null){
                        Object res = ts.getValor(valor.toString());
                        if(res instanceof Double){
                            double resultado =  (Double) res - 1;
                            ts.setValor(valor.toString(),resultado);
                            return null;
                        }else{
                            System.out.println("ERROR DE DECREMENTO, VARIABLE NO ES NUMERO");
                            return null;
                        }    
                    }else{
                        Object res = ts.getValor(valor.toString());
                        if(res instanceof NodoArreglo){
                            NodoArreglo resarr = (NodoArreglo)res;
                            LinkedList<Integer> valoresIndices=new LinkedList<>();
                            for (Instruccion dim : indices) {
                                Object er=dim.ejecutar(ts, ar);
                                //Se comprueba que cada indice para acceder al arreglo sea de tipo numerico
                                if(!(er instanceof Double)){
                                    System.err.println("Los indices para acceder a un arreglo deben ser de tipo numérico. El indice ["+String.valueOf(er)+"] no es numérico.");
                                    return null;
                                }
                                if(er instanceof Double){
                                    Double aux1=(Double)er;
                                    int aux = aux1.intValue();
                                    if((aux1%aux)!=0){
                                        System.err.println("LOS INDICES NO PUEDEN SER DECIMALES");
                                        return null;    
                                    }
                                }
                                valoresIndices.add(((Double)er).intValue());
                            }
                            Object resnum = resarr.getValor(valoresIndices.size(),1, valoresIndices,valor.toString());
                            if(resnum instanceof Double){
                                double resultado =  (Double) resnum - 1;
                                ts.setValor(valor.toString(),resultado,valoresIndices);
                                return null;
                            }else{
                                System.out.println("ERROR EL ARREGLO NO ES DE NUMEROS");
                            }
                        }else{
                            System.out.println("ERROR DE DECREMENTO, VARIABLE NO ES NUMERO");
                        }
                    }
                    return null;
                }else {
                    System.err.println("ERROR DE TIPOS EN DECREMENTO");
                    return null;
                }
            }
        }else if (tipo == Tipo_operacion.NEGATIVO) 
        {//OPERACION NEGACION
            if (a instanceof Double) {
                return (Double) a * -1;
            } else {
                System.err.println("ERROR DE TIPOS EN NEGATIVO");
                return null;
            }
        }else if (tipo == Tipo_operacion.MULTIPLICACION) 
        {//OPERACION MULTIPLICACION
             if (a instanceof Double && b instanceof Double) { //NUM Y NUM
                return (Double) a * (Double) b;
            }else if (a instanceof Double && b instanceof Character) { //NUM Y CHAR
                return (Double) a * (char) b;
            }else if (a instanceof Character && b instanceof Double) { //CHAR Y NUM
                return (char) a * (Double) b;
            }else if (a instanceof Character && b instanceof Character) { //CHAR Y CHAR
                double res = (char) a * (char) b;
                return res; 
            }else {
                System.err.println("ERROR DE TIPOS EN MULTIPLICACION");
                return null;
            }
        }else if (tipo == Tipo_operacion.POTENCIA) 
        {//OPERACION POTENCIA
            if(a instanceof Double){
                Double aux1=(Double)a;
                int aux = aux1.intValue();
                if((aux1%aux)!=0){
                    System.err.println("ERROR DE TIPOS EN POTENCIA");
                    return null;    
                }
            }
            if(b instanceof Double){
                Double aux1=(Double)b;
                int aux = aux1.intValue();
                if((aux1%aux)!=0){
                    System.err.println("ERROR DE TIPOS EN POTENCIA");
                    return null;    
                }
            }
         
            if (a instanceof Double && b instanceof Double) { //NUM Y NUM
                Double aux1=(Double)a;
                Double aux2=(Double)b;
                return Math.pow(aux1,aux2);
            }else if (a instanceof Double && b instanceof Character) { //NUM Y CHAR
                Double aux1=(Double)a;
                char aux2=(char)b;
                return Math.pow(aux1,aux2);
            }else if (a instanceof Character && b instanceof Double) { //CHAR Y NUM
                Double aux1=(Double)b;
                char aux2=(char)a;
                return Math.pow(aux2,aux1);
            }else if (a instanceof Character && b instanceof Character) { //CHAR Y CHAR
                char aux1=(char)a;
                char aux2=(char)b;
                return Math.pow(aux1,aux2);
            }else {
                System.err.println("ERROR DE TIPOS EN POTENCIA");
                return null;
            }
        }else if (tipo == Tipo_operacion.MODULO) 
        {//OPERACION MODULO
            if (a instanceof Double && b instanceof Double) { //NUM Y NUM
                return (Double) a % (Double) b;
            }else if (a instanceof Double && b instanceof Character) { //NUM Y CHAR
                return (Double) a % (char) b;
            }else if (a instanceof Character && b instanceof Double) { //CHAR Y NUM
                return (char) a % (Double) b;
            }else if (a instanceof Character && b instanceof Character) { //CHAR Y CHAR
                double res = (char) a % (char) b;
                return res; 
            }else {
                System.err.println("ERROR DE TIPOS EN MODULO");
                return null;
            }
        }else if (tipo == Tipo_operacion.DIVISION) 
        {//OPERACION DIVISION
            if (a instanceof Double && b instanceof Double) { //NUM Y NUM
                return (Double) a / (Double) b;
            }else if (a instanceof Double && b instanceof Character) { //NUM Y CHAR
                return (Double) a / (char) b;
            }else if (a instanceof Character && b instanceof Double) { //CHAR Y NUM
                return (char) a / (Double) b;
            }else if (a instanceof Character && b instanceof Character) { //CHAR Y CHAR
                double res = (char) a / (char) b;
                return res; 
            }else {
                System.err.println("ERROR DE TIPOS EN DIVISION");
                return null;
            }
        }else if (tipo == Tipo_operacion.MAYOR_QUE) 
        {//OPERACION MAYORQUE
            if (a != null && b != null) {
                if (a instanceof Double && b instanceof Double) { //NUM Y NUM
                    return (Boolean)((Double) a > (Double) b);
                }else if (a instanceof Double && b instanceof Character) { //NUM Y CHAR
                    return (Boolean)((Double) a > (char) b);
                }else if (a instanceof Character && b instanceof Double) { //CHAR Y NUM
                    return (Boolean)((char) a > (char) b);
                }else if (a instanceof Character && b instanceof Character) { //CHAR Y CHAR
                    return (Boolean)((char) a > (char) b);
                }else {
                    System.err.println("ERROR DE TIPOS EN COMPARACION MAYORQUE");
                    return null;
                }
            }
        }else if (tipo == Tipo_operacion.MENOR_QUE) 
        {//OPERACION MENORQUE
            if (a != null && b != null) {
                if (a instanceof Double && b instanceof Double) { //NUM Y NUM
                    return (Boolean)((Double) a < (Double) b);
                }else if (a instanceof Double && b instanceof Character) { //NUM Y CHAR
                    return (Boolean)((Double) a < (char) b);
                }else if (a instanceof Character && b instanceof Double) { //CHAR Y NUM
                    return (Boolean)((char) a < (char) b);
                }else if (a instanceof Character && b instanceof Character) { //CHAR Y CHAR
                    return (Boolean)((char) a < (char) b);
                }else {
                    System.err.println("ERROR DE TIPOS EN COMPARACION MENORQUE");
                    return null;
                }
            }
        }else if (tipo == Tipo_operacion.MAYOR_IGUAL_QUE) 
        {//OPERACION MAYORIGUAL
            if (a != null && b != null) {
                if (a instanceof Double && b instanceof Double) { //NUM Y NUM
                    return (Boolean)((Double) a >= (Double) b);
                }else if (a instanceof Double && b instanceof Character) { //NUM Y CHAR
                    return (Boolean)((Double) a >= (char) b);
                }else if (a instanceof Character && b instanceof Double) { //CHAR Y NUM
                    return (Boolean)((char) a >= (char) b);
                }else if (a instanceof Character && b instanceof Character) { //CHAR Y CHAR
                    return (Boolean)((char) a >= (char) b);
                }else {
                    System.err.println("ERROR DE TIPOS EN COMPARACION MAYORIGUALQUE");
                    return null;
                }
            }
        }else if (tipo == Tipo_operacion.MENOR_IGUAL_QUE) 
        {//OPERACION MENORIGUAL
            if (a != null && b != null) {
                if (a instanceof Double && b instanceof Double) { //NUM Y NUM
                    return (Boolean)((Double) a <= (Double) b);
                }else if (a instanceof Double && b instanceof Character) { //NUM Y CHAR
                    return (Boolean)((Double) a <= (char) b);
                }else if (a instanceof Character && b instanceof Double) { //CHAR Y NUM
                    return (Boolean)((char) a <= (char) b);
                }else if (a instanceof Character && b instanceof Character) { //CHAR Y CHAR
                    return (Boolean)((char) a <= (char) b);
                }else {
                    System.err.println("ERROR DE TIPOS EN COMPARACION MENORIGUALQUE");
                    return null;
                }
            }
        }else if (tipo == Tipo_operacion.IGUAL_QUE) 
        {//OPERACION IGUAL QUE
            if (a != null && b != null) {
                if (a instanceof Double && (b instanceof Double||b instanceof Integer)) { //NUM Y NUM
                    double vald = Double.parseDouble(b.toString());
                    return (Double) a == vald;
                }else if (b instanceof Double && (a instanceof Double||a instanceof Integer)) { //NUM Y NUM
                    double vald = Double.parseDouble(a.toString());
                    return (Double)b == vald;
                }else if (a instanceof Integer && b instanceof Integer) { //NUM Y NUM
                    int ax=(Integer)a;
                    int bx=(Integer)b;
                    return ax==bx;
                }else if (a instanceof Double && b instanceof Character) { //NUM Y CHAR
                    return (Double) a + (char) b;
                }else if (a instanceof Integer && b instanceof Character) { //NUM Y CHAR
                    return (Integer) a + (char) b;
                }else if (a instanceof Character && b instanceof Double) { //CHAR Y NUM
                    return (char) a + (Double) b;
                }else if (a instanceof Character && b instanceof Integer) { //CHAR Y NUM
                    return (char) a + (Integer) b;
                }else if (a instanceof Character && b instanceof Character) { //CHAR Y CHAR
                    int res = (char) a + (char) b;
                    return res; 
                }else {
                    System.err.println("ERROR DE TIPOS EN SUMA");
                    return null;
                }
                }else{
                if(a==null && b==null){
                    return true;
                }
            }
        }else if (tipo == Tipo_operacion.DIFERENTE_QUE) 
        {//OPERACION DIFERENTE QUE
            if (a != null && b != null) {
                if (a instanceof Double && b instanceof Double) { //NUM Y NUM
                    double aux = (Double)a;
                    double aux2 = (Double)b;
                    return (Boolean)(aux != aux2);
                }else if (a instanceof Double && b instanceof Character) { //NUM Y CHAR
                    return (Boolean)((Double) a != (char) b);
                }else if (a instanceof Character && b instanceof Double) { //CHAR Y NUM
                    return (Boolean)((char) a != (char) b);
                }else if (a instanceof Character && b instanceof Character) { //CHAR Y CHAR
                    return (Boolean)((char) a != (char) b);
                }else {
                    System.err.println("ERROR DE TIPOS EN COMPARACION DESIGUALDAD");
                    return null;
                }
            }
        } else if (tipo == Tipo_operacion.NOT) 
        {//OPERACION NOT
            if (a instanceof Boolean) {
                return !((Boolean) a);
            } else {
                System.err.println("ERROR DE TIPOS EN NOT");
                return null;
            }
        } else if (tipo == Tipo_operacion.AND) 
        {//OPERACION AND
            if (a instanceof Boolean && b instanceof Boolean) {
                return ((Boolean) a) && ((Boolean) b);
            } else {
                 System.err.println("ERROR DE TIPOS EN AND");
                return null;
            }
        } else if (tipo == Tipo_operacion.OR) 
        {//OPERACION OR
            if (a instanceof Boolean && b instanceof Boolean) {
                return ((Boolean) a) || ((Boolean) b);
            } else {
                System.err.println("ERROR DE TIPOS EN OR");
                return null;
            }
        }else if (tipo == Tipo_operacion.NUMERO) 
        {//OPERACION NUMERO
            if(this.valor instanceof Double){
                return new Double(valor.toString());
            }else{
                return new Integer(valor.toString());
            }
        }else if (tipo == Tipo_operacion.IDENTIFICADOR) 
        {//OPERADOR IDENTIFICADOR
            return ts.getValor(valor.toString());
        }else if (tipo == Tipo_operacion.CADENA) 
        {//OPERADOR CADENA
            return valor.toString();
        }else if (tipo == Tipo_operacion.CARACTER) 
        {//OPERADOR CARACTER
            return valor.toString().toCharArray()[0];
        }else if (tipo == Tipo_operacion.TRUE) 
        {//OPERADOR TRUE
            return true;
        }else if (tipo == Tipo_operacion.FALSE) 
        {//OPERADOR FALSE
            return false;
        }else if (tipo == Tipo_operacion.NULO) 
        {//OPERADOR NULO
            return null;
        }else if (tipo == Tipo_operacion.REFERENCIA) 
        {//OPERADOR REFERENCIA
            return ts.getSimbol((String)this.valor);
        }else if (tipo == Tipo_operacion.NODOARREGLO) 
        {//OPERADOR NODOARREGLO
            LinkedList<Object> larr = (LinkedList<Object>)this.valor;
            int res = verificartam(larr);
            NodoArreglo arr;
            if(res!=-1){
                //SI LOS TAMAÑOS SON SIMETRICOS
                LinkedList<Object> vals= new LinkedList<>();
                //CREAR UNA LISTA SIMPLE DE TODOS LOS VALORES
                getValLarr(larr,vals);
                LinkedList<Integer> arr_dim= new LinkedList<>();
                //CREAR UNA LISTA CON LAS DIMENSIONES DEL ARREGLO
                arr_dim.add(larr.size());
                getDims(larr, arr_dim);
                //CREAR UN NODO ARREGLO
                int [] contador={0};
                arr=new NodoArreglo();
                arr.setDims(arr_dim);
                arr.inicializarNodo(arr_dim.size(), 1, arr_dim, vals, contador);
            }else{
                System.out.println("ERROR DE ASIGNACION EN ARREGLO { } TAMAÑO INCORRECTO");
                return null;
            }
            return arr;
        }else {
            return null;
        }
        return null;
    }
    
    private int verificartam(Object val){
        if(!(val instanceof LinkedList)){
            return 0;
        }
        LinkedList<Object> larr =(LinkedList<Object>)val;
        LinkedList<Object> aux,aux1;
        int pivot,sig,res;
        res=0;
        for(int i=0;i<larr.size()-1;i++){
            //PRIMER HIJO
            res=verificartam(larr.get(i));
            if(res==-1){
                return -1;
            }
            aux =(LinkedList<Object>)larr.get(i);
            pivot=aux.size();
            //HIJO SIGUIENTE
            res=verificartam(larr.get(i+1));
            if(res==-1){
                return -1;
            }
            aux1 =(LinkedList<Object>)larr.get(i+1);
            sig=aux1.size();
            //COMPARAR
            if(pivot!=sig){
                res= -1;
                return res;
            }else{
                if((aux.size()==1)&&(aux1.size()==1)){
                    if(!((aux.get(0) instanceof LinkedList)&&(aux1.get(0) instanceof LinkedList))){
                        if(!(!(aux.get(0) instanceof LinkedList)&&!(aux1.get(0) instanceof LinkedList))){
                            res=-1;
                            return res;
                        }
                    }
                }
            }
        }
        if(larr.size()==1){
            res=verificartam(larr.get(0));
        }
        return res;
    }
    private void getValLarr(Object val,LinkedList<Object> vals){
        if(!(val instanceof LinkedList)){
            vals.add(val);
            return;
        }
        LinkedList<Object> larr =(LinkedList<Object>)val;
        for(int i=0;i<larr.size();i++){
            //PRIMER HIJO
            getValLarr(larr.get(i),vals);
        }
    }
    private void getDims(Object val,LinkedList<Integer> dims){
        if(!(val instanceof LinkedList)){
            return;
        }
        LinkedList<Object> larr =(LinkedList<Object>)val;
        if(larr.get(0) instanceof LinkedList){
            LinkedList<Object> hijo =(LinkedList<Object>)larr.get(0);
            if(hijo.get(0) instanceof LinkedList){
                dims.add(hijo.size());   
            }
        }
        getDims(larr.get(0),dims);
    }
    
}
