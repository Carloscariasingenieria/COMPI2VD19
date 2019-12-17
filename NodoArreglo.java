package arbol;

import java.util.LinkedList;

public class NodoArreglo {
    ////#############VALORES DE NODOS-------------------------
    private final LinkedList<NodoArreglo> hijos;
    private LinkedList<Integer> dims;
    private Object valor;
    ////#############VALORES DE NODOS-------------------------

    //#############CONSTRUCTORES--------------------------------

    //Constructor NODOARREGLO
    public NodoArreglo() {
        this.hijos = new LinkedList<>();
        this.valor = null;
        this.dims=new LinkedList<>();
    }
    
    //INICIALIZAR NODOS SIGUIENTES POR CADA DIMENSION
    public void inicializarNodo(int cant_dim, int actual_dim, LinkedList<Integer> list_dim,Simbolo.Tipo tipo){
        if(actual_dim>cant_dim){
            return;
        }
        for (int i = 0; i < list_dim.get(actual_dim-1) ; i++) {
            NodoArreglo arr=new NodoArreglo();
            if(actual_dim==cant_dim){
                 if(tipo==Simbolo.Tipo.CARACTER){
                    arr.valor='\0';
                }else if(tipo==Simbolo.Tipo.CADENA){
                    arr.valor="";
                }else if(tipo==Simbolo.Tipo.ENTERO){
                    arr.valor=0;
                }else if(tipo==Simbolo.Tipo.DECIMAL){
                    arr.valor=0.0;
                }else if(tipo==Simbolo.Tipo.BOOLEANO){
                    arr.valor=false;
                }
            }
            LinkedList<Integer> dimsaux=new LinkedList<>();
            dimsaux.addAll(dims);
            dimsaux.removeFirst();
            arr.setDims(dimsaux);
            hijos.add(arr);
            arr.inicializarNodo(cant_dim, actual_dim+1, list_dim,tipo);            
        }
    }
    //INICIALIZAR NODOS CON VALORES
    public void inicializarNodo(int cant_dim, int actual_dim, LinkedList<Integer> list_dim,LinkedList<Object> valores,int[] contador){
        if(actual_dim>cant_dim){
            return;
        }
        for (int i = 0; i < list_dim.get(actual_dim-1) ; i++) {
            NodoArreglo arr=new NodoArreglo();
            if(actual_dim==cant_dim){
                arr.valor=valores.get(contador[0]);
                contador[0]++;
            }
            LinkedList<Integer> dimsaux=new LinkedList<>();
            dimsaux.addAll(dims);
            dimsaux.removeFirst();
            arr.setDims(dimsaux);
            hijos.add(arr);
            arr.inicializarNodo(cant_dim, actual_dim+1, list_dim,valores,contador);            
        }
    }
    
    //ASIGNAR VALOR A CASILLA ESPECIFICA EN EL ARREGLO
    public void setValor(int cantIndices, int indiceActual, LinkedList<Integer> indices, Object val, String id){
        int valIndiceActual=indices.get(indiceActual-1);
        if(valIndiceActual<hijos.size() && valIndiceActual>=0){
            NodoArreglo arr=hijos.get(valIndiceActual);
            if(indiceActual==cantIndices){
                arr.valor=val;
            }else{
                arr.setValor(cantIndices, indiceActual+1, indices, val, id);
            }
        }else{
            System.err.println("LIMITES DEL ARREGLO: "+id+" EXCEDIDOS");
        }
    }
    
    //OBTENER VALOR DE CASILLA ESPECIFICA EN EL ARREGLO
    public Object getValor(int cantIndices, int indiceActual, LinkedList<Integer> indices, String id) {
        return getValor(cantIndices, indiceActual, indices, id,this.dims);
    }
    public Object getValor(int cantIndices, int indiceActual, LinkedList<Integer> indices, String id,LinkedList<Integer> list_dim) {
        int valIndiceActual=indices.get(indiceActual-1);
        if(valIndiceActual<hijos.size() && valIndiceActual>=0){
            NodoArreglo arr=hijos.get(valIndiceActual);
            if(indiceActual==cantIndices){
                if(cantIndices<list_dim.size()){
                    return arr;
                }else{
                    return arr.valor;
                }
            }else{
                return arr.getValor(cantIndices, indiceActual+1, indices, id,list_dim);
            }
        }else{
            System.err.println("LIMITES DEL ARREGLO: "+id+" EXCEDIDOS");
        }
        return null;
    }
    
    //OBTENER VALOR SIMPLE
    public Object getValor(){
        return this.valor;
    }
    //SET ARRDIMS
    public void setDims(LinkedList<Integer> dims){
        this.dims=dims;
    }
    
    //GET ARRDIMS
    public LinkedList<Integer> getDims(){
        return this.dims;
    }
    public Object getFirst(){
        LinkedList<Integer> auxdim = new LinkedList<>();
        for(int i=0;i<dims.size();i++){
            auxdim.add(0);
        }
        return getValor(auxdim.size(),1,auxdim,"arr");
    }
}
