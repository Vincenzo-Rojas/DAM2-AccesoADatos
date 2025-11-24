package Ej1;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;


/*
 * datos_ajedrez.csv
 */

public class ej1 {

    //Crear un Path
    Path ruta = Paths.get("datos_ajedrez.csv");
    ArrayList<String> lista = readWholeFile(ruta);

   
    // Devuelve un fichero línea a línea en un ArrayList<String>
    public ArrayList<String> readWholeFile(Path ruta){
        ArrayList<String> lista = new ArrayList<String>();

        // Comprobar si el archivo existe
        if (!Files.exists(ruta)) {
            System.out.println("El archivo no existe en la ruta: " + ruta);
            return lista;
        } else {
            File archivo = ruta.toFile(); // Convierte el Path a un objeto File
            BufferedReader br = null;
            try {
                FileReader fr = new FileReader(archivo); // FileReader para leer el archivo
                br = new BufferedReader(fr); // BufferedReader para leer línea a línea
                String linea; 
                while ((linea = br.readLine()) != null) {
                    lista.add(linea);
                }
            } catch (IOException e) {
                System.out.println("Error al leer el archivo: " + e.getMessage());
            } finally {
                try {
                    if (br != null) br.close();
                } catch (IOException e) {
                    System.out.println("Error cerrando el archivo: " + e.getMessage());
                }
            }
        }
        return lista;
    }

    // imprime las primeras n lineas del csv. Si el arcchivo tiene cabeceza, primera fila = nombres columnas
    // se considera que la primera fila es la segunda
    public void head(int lineas, ArrayList<String> archivo, boolean head){
        int inicio = 0; // inicio de la impresion

        //si tiene cabecera y el numero de lineas es distinto de 0
        if (head == true && lineas != 0) {
            // inicia en la 2da fila, se salta la fila de cabecera
            inicio = 1;
        }
        // continua hasta que imprimes las lineas pedidas
        for(int i = inicio; i <= lineas; i++){
            //Imprime el contenido del arraylist
            try {
                System.out.println(archivo.get(i));
            } catch (IndexOutOfBoundsException e) {
                System.out.println("No hay más líneas para imprimir. Última línea: " + (i-1));
                break; // salimos del bucle
            }
        } 
    }

    // devuelve un array de double de dos posiciones [minimo, maximo], calculado a partir de cada fila
    // de datos. 
    public ArrayList<Double> minMaxCol(int nColumna, ArrayList<String> archivo, boolean head){
        ArrayList<Double> listaPosiciones = new ArrayList<Double>();
        
        Double minimo = null;
        Double maximo = null;

        // si el archivo esta vacio o solo tiene cabecera        
        if (archivo.isEmpty() || archivo.size() <= 1){
            listaPosiciones.add(Double.NaN);
            listaPosiciones.add(Double.NaN);  
            return listaPosiciones;
        }

        //consigue el numero de columnas que tiene el archivo
        String columnas[] = archivo.get(0).split(",");
        int numColumnas = columnas.length;

        // Si el numero de columna es mayor que la cantidad de columnas, return NaN
        if (nColumna > numColumnas){
            listaPosiciones.add(Double.NaN);
            listaPosiciones.add(Double.NaN);  
            return listaPosiciones;
        }

        int inicio = 0; // inicio de la impresion

        //si tiene cabecera
        if (head == true ) {
            // inicia en la 2da fila, se salta la fila de cabecera
            inicio = 1;
        }
        
        // continua hasta que termine el archivo
        for(int i = inicio; i < archivo.size() ; i++){
            //separa el contenido de la fila en un arreglo
            columnas = archivo.get(i).split(",");

            //valores de la columna sean numericos
            double valor;
            try {
                valor = Double.parseDouble(columnas[nColumna]);
            } catch (NumberFormatException e) {
                continue; // saltar filas no numéricas
            }

            // Inicializamos mínimo y máximo en la primera fila válida
            if (minimo == null || maximo == null) {
                minimo = valor;
                maximo = valor;
            }

            if (valor < minimo) 
                minimo = valor;
            if (valor > maximo) 
                maximo = valor;
        } 

        // Si no se encontraron valores numéricos
        if (minimo == null || maximo == null) {
            listaPosiciones.add(Double.NaN);
            listaPosiciones.add(Double.NaN);
        } else {
            listaPosiciones.add(minimo);
            listaPosiciones.add(maximo);
        }

        return listaPosiciones;
    }
}