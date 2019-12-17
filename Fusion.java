package arbol;

import java.util.LinkedList;

public class Fusion implements Instruccion{
    ////#############VALORES DE FUSION-------------------------
    private final String nombre_fusion;
    private final TablaDeSimbolos tsFusion;
    private final int pesoFusion;
    private final LinkedList<Instruccion> list_atributos;
    ////#############VALORES DE SIMBOLOS-------------------------
    
    public Fusion(String id, LinkedList<Instruccion> lista_atributos){
        this.nombre_fusion=id;
        this.pesoFusion=lista_atributos.size();
        tsFusion=new TablaDeSimbolos();
        this.list_atributos=lista_atributos;
    }
    
    public Fusion(String id, int peso){
        this.nombre_fusion=id;
        this.pesoFusion=peso;
        tsFusion=new TablaDeSimbolos();
        this.list_atributos=null;
    }
    
    @Override
    public Object ejecutar(TablaDeSimbolos ts, Arbol ar) {
        if(this.list_atributos==null){
            System.out.println("FUSION SIN DECLARACIONES");
            return null;
        }
        for(Instruccion ins : list_atributos){
            if(ins instanceof Declaracion){
                Declaracion aux = (Declaracion)ins;
                Instruccion asig = aux.getAsig();
                if(asig==null){
                    aux.ejecutar(tsFusion, ar);
                }else{
                    ar.addError("NO SE PUEDE ASIGNAR EN UNA FUSION: "+this.nombre_fusion);
                    System.out.println("NO SE PUEDE ASIGNAR EN UNA FUSION: "+this.nombre_fusion);
                }
            }else if (ins instanceof DeclaracionArreglo){
                DeclaracionArreglo aux = (DeclaracionArreglo)ins;
                Instruccion asig = aux.getAsigArr();
                if(asig==null){
                    aux.ejecutar(tsFusion, ar);
                }else{
                    ar.addError("NO SE PUEDE ASIGNAR EN UNA FUSION: "+this.nombre_fusion);
                    System.out.println("NO SE PUEDE ASIGNAR EN UNA FUSION: "+this.nombre_fusion);
                }
            }else{
                ar.addError("ERROR EN ALGUNA DECLARACION DE LA FUSION: "+this.nombre_fusion);
                    System.out.println("ERROR EN ALGUNA DECLARACION DE LA FUSION: "+this.nombre_fusion);
            }
        }
        return null;
    }
    
    public int getPeso(){
        return this.pesoFusion;
    }
      
    public String getNombre(){
        return this.nombre_fusion;
    }
    public Simbolo getSimbolo(String id){
        return tsFusion.getSimbol(id);
    }
    public Object getAtributo(String id){
        return tsFusion.getValor(id);
    }
    
    public Object getAtributo(String id,LinkedList<Integer> list_dim){
        return tsFusion.getValor(id,list_dim);
    }
    
    public void setAtributo(String id, Object valor){
        tsFusion.setValor(id, valor);
    }
    
    public void setAtributo(String id, Object valor,LinkedList<Integer> list_dim){
        tsFusion.setValor(id, valor, list_dim);
    }
    public TablaDeSimbolos getAtributos(){
        return this.tsFusion;
    }
    public void setAtributos(TablaDeSimbolos ts){
        for(Simbolo s : ts){
            Simbolo nuevo = (Simbolo)s.clone();
            this.tsFusion.add(nuevo);
        }
    }
}
