package AccesoADatosEjercicios;

// Importa IOException para manejar errores de E/S
import java.io.*;       // Importa todas las clases de java.io (FileReader, FileWriter, Streams, etc.)
import java.nio.file.Files;
import java.nio.file.Path;  // Importa Path de NIO para manejar rutas de archivos


public class gestionarCSV {
    public static void main(String[] args) {
        char caracterReemplazo = ';'; // Puedes cambiarlo por cualquier otro carácter

        try {
            // Leer y procesar el archivo
            procesarArchivoCSV(caracterReemplazo);
            System.out.println("Archivo procesado correctamente.");
        } catch (IOException excepcion) {
            System.err.println("Error al procesar el archivo: " + excepcion.getMessage());
            excepcion.printStackTrace();
        }
    }

    /**
     * Lee un archivo CSV, reemplaza las comas dentro de comillas dobles,
     * y escribe el resultado en un nuevo archivo.
     */
    public static void procesarArchivoCSV(char caracterReemplazo) throws IOException {
        Path archivoEntrada = Path.of("UT1","listings.csv");
        Path archivoSalida = Path.of("UT1","listingModificado.csv");

        try (var lector = Files.newBufferedReader(archivoEntrada);
             var escritor = Files.newBufferedWriter(archivoSalida)) {

            String linea;
            while ((linea = lector.readLine()) != null) {
                String lineaModificada = reemplazarComasDentroDeComillas(linea, caracterReemplazo);
                escritor.write(lineaModificada);
                escritor.newLine();
            }
        }
    }

    /**
     * Reemplaza las comas que están dentro de comillas dobles en una línea.
     * No usa StringBuilder, sino un arreglo de caracteres.
     */
    public static String reemplazarComasDentroDeComillas(String linea, char caracterReemplazo) {
        char[] caracteres = linea.toCharArray();
        boolean dentroDeComillas = false;

        for (int indice = 0; indice < caracteres.length; indice++) {
            char caracterActual = caracteres[indice];

            if (caracterActual == '"') {
                dentroDeComillas = !dentroDeComillas;
            }

            if (caracterActual == ',' && dentroDeComillas) {
                caracteres[indice] = caracterReemplazo;
            }
        }

        return new String(caracteres);
    }
}