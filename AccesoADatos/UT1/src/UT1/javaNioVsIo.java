/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UT1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.UserPrincipal;
import java.util.stream.Stream;

/**
 *
 * @author vince
 */
public class javaNioVsIo {

    //JAVA.NIO.FILE (Path, Files, Paths)
    /*  IMPORTS NECESARIOS
    import java.nio.file.*; // Clases para trabajar con rutas y archivos (Path, Files, etc.)
    import java.io.IOException; // Para capturar excepciones de entrada/salida
    import java.nio.file.attribute.FileTime; // Para obtener fechas de archivos
    import java.nio.file.attribute.UserPrincipal; // Para obtener el propietario del archivo
    import java.util.stream.Stream; // Para procesar colecciones de rutas (listar archivos)
     */
    public void NIOEjemplo() {
        try {
            Path ruta = Paths.get("ejemplo_nio.txt"); // Ruta de archivo
            //Path rutaFiles = Path.of("src", "main", "java", "ut1", "files");
            

            // Crear archivo si no existe
            if (!Files.exists(ruta)) {
                Files.createFile(ruta);
                System.out.println("Archivo creado.");
            }

            // Comprobar propiedades
            System.out.println("¿Existe? " + Files.exists(ruta));
            System.out.println("¿Es archivo regular? " + Files.isRegularFile(ruta));
            System.out.println("¿Es directorio? " + Files.isDirectory(ruta));
            System.out.println("¿Se puede leer? " + Files.isReadable(ruta));
            System.out.println("¿Se puede escribir? " + Files.isWritable(ruta));

            // Escribir y leer contenido
            Files.writeString(ruta, "Contenido de prueba con NIO.\n");
            String contenido = Files.readString(ruta);
            System.out.println("Contenido leído: " + contenido);

            // Información adicional
            FileTime modTime = Files.getLastModifiedTime(ruta);
            UserPrincipal owner = Files.getOwner(ruta);
            System.out.println("Última modificación: " + modTime);
            System.out.println("Propietario: " + owner.getName());
            System.out.println("Tamaño: " + Files.size(ruta) + " bytes");

            // Copiar y mover archivos
            Path copia = Paths.get("copia_nio.txt");
            Files.copy(ruta, copia, StandardCopyOption.REPLACE_EXISTING);
            Path destino = Paths.get("mover_nio.txt");
            Files.move(copia, destino, StandardCopyOption.REPLACE_EXISTING);

            // Listar archivos del directorio actual
            System.out.println("Archivos en el directorio actual:");
            try (Stream<Path> stream = Files.list(Paths.get("."))) {
                stream.forEach(System.out::println);
            }

            // Borrar archivo
            Files.deleteIfExists(destino);

            // -----------------------------
            // EJEMPLO CON resolve Y resolvesibling
            Path base = Paths.get("carpeta_base"); // Directorio base
            //Path pTest = Path.of("src", "main", "java", "ut1", "files","CarpetaTest");

            // Crear directorio base si no existe
            if (!Files.exists(base)) {
                try {
                    Files.createDirectory(base);
                    System.out.println("Directorio base creado.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("La carpeta ya existe!");
            }

            // resolve: añadir subarchivo al path base
            Path archivo = base.resolve("archivo.txt"); // carpeta_base/archivo.txt
            if (!Files.exists(archivo)) {
                Files.createFile(archivo);
                System.out.println("Archivo creado usando resolve: " + archivo);
            }

            // resolveSibling: crear un archivo en el mismo directorio que otro
            Path archivoHermano = archivo.resolveSibling("archivo_hermano.txt"); // carpeta_base/archivo_hermano.txt
            if (!Files.exists(archivoHermano)) {
                Files.createFile(archivoHermano);
                System.out.println("Archivo creado usando resolveSibling: " + archivoHermano);
            }

            // Mostrar contenido del directorio base
            System.out.println("Archivos en " + base + ":");
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(base)) {
                for (Path p : stream) {
                    System.out.println(p.getFileName());
                }
            }

            // Limpiar archivos y directorio creados
            Files.deleteIfExists(archivoHermano);
            Files.deleteIfExists(archivo);
            Files.deleteIfExists(base);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //PATHS
    /* imports necesarios
    import java.nio.file.Path;        // Importa la clase Path de NIO, para representar rutas de archivos de forma portable
    import java.nio.file.Paths;       // Importa la clase Paths para crear objetos Path fácilmente
    import java.io.File;              // Importa la clase File para manejar archivos usando la API clásica de java.io
    import java.io.IOException;       // Importa IOException para manejar errores de entrada/salida
    import java.io.FileWriter;        // Importa FileWriter para escribir texto en archivos
    import java.io.FileReader;        // Importa FileReader para leer archivos de texto
    import java.io.BufferedReader;    // Importa BufferedReader para leer líneas completas de un archivo
     */
    public void PathToFileEjemplo() {  //Contiene el ejemplo de uso de Path y File
        try {  // Bloque try para capturar posibles errores de E/S
            //1️⃣ Crear un Path
            Path ruta = Paths.get("archivo.txt"); // Crea un Path moderno apuntando al archivo "archivo.txt"
            //Path pTest = Path.of("src", "main", "java", "ut1", "files","CarpetaTest");

            // 2️⃣ Convertir Path a File
            File archivo = ruta.toFile(); // Convierte el Path a un objeto File para usar métodos clásicos de java.io

            // 3️⃣ Crear archivo si no existe
            if (!archivo.exists()) { // Comprueba si el archivo ya existe
                archivo.createNewFile(); // Crea un nuevo archivo en la ruta especificada
                System.out.println("Archivo creado usando File: " + archivo.getName()); // Mensaje confirmando la creación
            }

            // 4️⃣ Escribir texto con FileWriter
            try (FileWriter fw = new FileWriter(archivo)) { // try-with-resources para que el FileWriter se cierre automáticamente
                fw.write("Hola desde FileWriter usando Path.toFile()\n"); // Escribe una línea de texto en el archivo
            } // FileWriter se cierra automáticamente al salir del try

            // 5️⃣ Leer texto con FileReader y BufferedReader
            try (FileReader fr = new FileReader(archivo); // FileReader para leer el archivo
                     BufferedReader br = new BufferedReader(fr)) { // BufferedReader para leer línea por línea de manera eficiente
                String linea; // Variable para almacenar cada línea leída
                System.out.println("Contenido leído:"); // Mensaje indicando que se va a mostrar el contenido
                while ((linea = br.readLine()) != null) { // Lee línea por línea hasta llegar al final del archivo (null)
                    System.out.println(linea); // Muestra la línea leída en la consola
                }
            } // BufferedReader y FileReader se cierran automáticamente al salir del try-with-resources

            // 6️⃣ Borrar archivo
            if (archivo.delete()) { // Intenta borrar el archivo
                System.out.println("Archivo borrado correctamente."); // Mensaje si se borró con éxito
            } else {
                System.out.println("No se pudo borrar el archivo."); // Mensaje si no se pudo borrar
            }

        } catch (IOException e) { // Captura cualquier excepción de entrada/salida que ocurra dentro del try
            e.printStackTrace(); // Muestra el stack trace del error en la consola
        }
    }
}
