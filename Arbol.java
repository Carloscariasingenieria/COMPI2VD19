package arbol;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedList;

public class Arbol implements Instruccion{
    
    ////#############VALORES DE ARBOL-------------------------
    private final LinkedList<Instruccion> instrucciones;
    private final LinkedList<ErrorS> errores;
    public TablaDeSimbolos tablaDeSimbolosGlobal;
    public TablaDeSimbolos tablaReporte;
    FileWriter fichero = null;
    PrintWriter pw = null;
    private String archivo;
    ////#############VALORES DE ARBOL-------------------------

    //CONSTRUCTOR
    public Arbol(LinkedList<Instruccion> sup_ins) {
        instrucciones=sup_ins;
        this.tablaReporte= new TablaDeSimbolos();
        this.errores=new LinkedList<>();
    }
      
    @Override
    public Object ejecutar(TablaDeSimbolos ts,Arbol ar) {
        
        tablaDeSimbolosGlobal = ts;
        tablaDeSimbolosGlobal.setArbol(this);
        /*IMPORTAR 
        for(Instruccion ins:instrucciones){
            if(ins instanceof Importar){
                Importar imp = (Importar)ins;
                imp.ejecutar(ts, ar);
            }
        }*/
        
        for(Instruccion ins:instrucciones){
            if(ins instanceof Fusion){
                Fusion f = (Fusion)ins;
                f.ejecutar(ts, ar);
            }
            if(ins instanceof Declaracion){
                Declaracion d=(Declaracion)ins;
                d.ejecutar(ts, ar);
            }
            if(ins instanceof Definir){
                Definir d=(Definir)ins;
                d.ejecutar(ts, ar);
            }
        }
        
        int ejecutado=0;
        for(Instruccion ins:instrucciones){
            if(ins instanceof Function){
                Function f=(Function)ins;
                String id=f.getIdentificador(); 
                //LA FUNCION MAIN TIENE QUE VENIR COMO _MAIN()
                if("_main()".equals(id)){
                    f.setValoresParametros(new LinkedList<>());
                    f.ejecutar(ts, ar);
                    ejecutado=1;
                    break;
                }
            }
        }
        if(ejecutado==0){
            this.addError("NO SE HA DECLARADO UNA FUNCION main()");
            System.out.println("NO SE HA DECLARADO UNA FUNCION main()");
        }
        return null;
    }
   
    //DEVUELVE LA FUNCION
    public Function getFunction(String identificador){
        for(Instruccion ins:instrucciones){
            if(ins instanceof Function){
                Function f=(Function)ins;
                String id=f.getIdentificador();
                if(identificador.equals(id)){
                    return f;
                }
            }
        }
        return null;
    }
    
    public Fusion getFusion(String identificador){
        for(Instruccion ins:instrucciones){
            if(ins instanceof Fusion){
                Fusion f=(Fusion)ins; 
                String id=f.getNombre();
                if(identificador.equals(id)){
                    Fusion nueva = new Fusion(f.getNombre(), f.getPeso());
                    nueva.setAtributos(f.getAtributos());
                    return nueva;
                }
            }
        }
        return null;
    }
    
    public int getPesoFusion(String identificador){
        for(Instruccion ins:instrucciones){
            if(ins instanceof Fusion){
                Fusion f=(Fusion)ins; 
                if(f.getNombre().equals(identificador)){
                    return f.getPeso();   
                }
            }
        }
        return -1;
    }
    public String getRutaMain(){
        //CODIGO DEL CONFIG
        return "C:/Users/carlo/Desktop";
    }
    
    public void SetFichero(String ruta,boolean ap){
        try{
            if(ap){
                File archivo = new File (getRutaMain()+ruta);
                FileReader fr = new FileReader (archivo);
                fichero = new FileWriter(getRutaMain()+ruta,true);   
            }else{
                fichero = new FileWriter(getRutaMain()+ruta);   
            }
        }catch (Exception e) {
            System.out.println("ERROR AL INTENTAR ABRIR EL ARCHIVO");
            try {
             if (null != fichero){
                fichero.close();
                fichero=null;
             }
            } catch (Exception e2) {
               e2.printStackTrace();
            }
        }
    }
    
    public FileWriter getFichero(){
        return fichero;
    }
    
    public String readFichero(String ruta){
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
         archivo = new File (getRutaMain()+ruta);
         fr = new FileReader (archivo);
         br = new BufferedReader(fr);
         String res="";
         String linea;
         while((linea=br.readLine())!=null)
            res+=linea;
         return res;
      }catch(Exception e){
         e.printStackTrace();
         return null;
      }
    }
    
    public void CloseFichero(){
        try {
            if (null != fichero){
                fichero.close();   
                fichero=null;
            }
        }catch(Exception e2) {
           e2.printStackTrace();
        }
    }
    
    public void setArchivo(String arch){
        this.archivo=arch;
    }
    
    public void addError(String descripcion){
        errores.add(new ErrorS("Error Semantico",descripcion,0,0,this.archivo));
    }

    public LinkedList<Instruccion> getInstrucciones(){
        return this.instrucciones;
    }
}
