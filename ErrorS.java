package arbol;

import java.util.LinkedList;

public class ErrorS {
    ////#############VALORES DE ERRORES-------------------------
    private final String tipo;
    protected final String descripcion;
    protected final int fila;
    protected final int columna;
    protected final String archivo;
    ////#############VALORES DE ERRORES-------------------------    
    
    public ErrorS(String tipo,String descripcion,int fila,int columna,String archivo){
        this.tipo=tipo;
        this.descripcion=descripcion;
        this.fila=fila;
        this.columna=columna;
        this.archivo=archivo;
    }
    
    public String getTipo(){
        return this.tipo;
    }
    public String getDescripcion(){
        return this.descripcion;
    }
    public String getArchivo(){
        return this.archivo;
    }
    public int getFila(){
        return this.fila;
    }
    public int getColumna(){
        return this.columna;
    }
}
