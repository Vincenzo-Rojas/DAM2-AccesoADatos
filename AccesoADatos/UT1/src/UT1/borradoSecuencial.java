/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UT1;

import java.nio.file.Files; // Clases con métodos estáticos para archivos y directorios
import java.nio.file.Path; // Representa rutas de archivos/directorios de forma portable
import java.io.IOException; // Para manejar errores de entrada/salida
import java.util.Comparator; // Para ordenar colecciones (aquí se usa para invertir orden)
import java.util.stream.Stream; // Para procesar secuencias de elementos, en este caso rutas de archivos

/**
 *
 * @author vince
 */
public class borradoSecuencial {
    
    /**
     * Método para borrar recursivamente un directorio completo
     * @param path Ruta del directorio a borrar
     */
    public static void borrarDirectorio(Path path) {
        // Si la ruta es null, no hacemos nada
        if (path == null) {
            return;
        }

        // Si la ruta no existe, no hay nada que borrar
        if (!Files.exists(path)) {
            return;
        }

        // Declaramos un Stream de Path fuera del try para poder cerrarlo en finally
        Stream<Path> stream = null;

        try {
            // Files.walk genera un Stream con todas las rutas dentro del directorio de forma recursiva
            stream = Files.walk(path);

            // Procesamos cada ruta
            stream
                // Ordenamos de manera inversa: primero archivos y subdirectorios internos, luego directorios padres
                .sorted(Comparator.reverseOrder())
                // Iteramos sobre cada elemento
                .forEach(p -> {
                    try {
                        // Borra el archivo o directorio si existe
                        Files.deleteIfExists(p);
                        // Muestra en consola qué se ha borrado
                        System.out.println("Borrado: " + p);
                    } catch (IOException e) {
                        // Captura errores individuales de cada elemento
                        System.err.println("No se pudo borrar: " + p + " -> " + e.getMessage());
                    }
                });

        } catch (IOException e) {
            // Captura errores generales al generar o procesar el Stream
            System.err.println("Error al recorrer el directorio: " + path);
            e.printStackTrace();
        } finally {
            // Asegura que el Stream se cierra para liberar recursos
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception ex) {
                    System.err.println("Error cerrando el stream: " + ex.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        // Definimos la ruta de la carpeta que queremos borrar
        Path raiz = Path.of("src", "main", "java", "ut1", "files", "CarpetaTest");

        // Comprobamos que la ruta exista y sea un directorio antes de borrarla
        if(Files.exists(raiz) && Files.isDirectory(raiz)){
            borrarDirectorio(raiz); // Llamamos al método para borrar todo el árbol de carpetas y archivos
        }
    }
    
}

