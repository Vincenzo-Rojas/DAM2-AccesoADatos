/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AccesoADatosEjercicios;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author vince
 */
public class PracticaAccesoADatosUT1 {

    public static Path ruta = Path.of("UT1","src", "AccesoADatosEjercicios", "archivo_aleatorio.txt");

    public static void main(String[] args) {
        //Path ruta = Paths.get("").toAbsolutePath();
        System.out.println("Ruta de ejecución: " + ruta);
        ej3();
    }

    public static void ej1() {
        /*
Crea un programa en Java que lea un fichero de texto línea a línea y cuente: El número total 
de líneas, El número de palabras, Y el número total de caracteres (sin contar espacios).
         */

        try {
            // --- LECTURA CON BUFFER ---
            // Path para archivo de texto con buffer
            BufferedReader br = new BufferedReader(new FileReader(ruta.toFile())); // BufferedReader permite leer líneas completas
            String linea; // Variable para almacenar cada línea
            int nLineas = 0;
            int nPalabras = 0;
            int nCaracteres = 0;

            System.out.println("Leyendo con BufferedReader:");

            while ((linea = br.readLine()) != null) { // Leemos hasta el final del archivo
                nLineas += 1;
                nCaracteres += linea.length();

                String[] palabras = linea.trim().split("\\s+");
                nPalabras += palabras.length;
            }

            System.out.println("El numero de lineas son: " + nLineas);
            System.out.println("El numero de palabras son: " + nPalabras);
            System.out.println("El numero de caracteres son: " + nCaracteres);

            br.close(); // Cerramos el BufferedReader

        } catch (IOException e) { // Captura errores de E/S
            e.printStackTrace(); // Muestra información detallada del error
        }

    }

    public static void ej2() {
        /*
Escribe un programa que guarde en un archivo binario los datos de varios alumnos 
(nombre, edad, nota media) y luego los lea y los muestre por consola.
         */

        // FICHEROS BINARIOS CON BUFFER
        try {
            // --- ESCRITURA CON BUFFER ---
            ruta = ruta.resolveSibling("alumnos.txt"); // Path para archivo con buffer

            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(ruta.toFile())));
            // BufferedOutputStream mejora la eficiencia al escribir

            dos.writeUTF("alumno 1");           // Escribe un String UTF-8, nombre alumno
            dos.writeInt(10);                   // Edad
            dos.writeDouble(6.28);              // Escribe un double, nota

            dos.close();                        // Cerramos el flujo

            // --- LECTURA CON BUFFER ---
            DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(ruta.toFile())));
            // BufferedInputStream mejora la eficiencia al leer

            String nombre = dis.readUTF();      // Lee un String
            int edad = dis.readInt();           // Lee un entero
            double nota = dis.readDouble();     // Lee un double

            dis.close();                        // Cerramos el flujo

            System.out.println("Leído desde buffer: nombre - " + nombre + ", edad - " + edad + ", nota - " + nota);

        } catch (IOException e) { // Captura errores de E/S
            e.printStackTrace(); // Muestra información detallada del error
        }
    }

    public static boolean ej3() {
        /*
Crea un fichero XML con información de una librería (libros, autor, precio). Implementa 
un programa Java que:
    -Lel fichero XML usando DOM.
    -Modifique el precio de un libro concreto.
    -Guarde los cambios en un nuevo archivo.    
         */

        Path xmlPath = Path.of("UT1","src", "AccesoADatosEjercicios", "libreria_dom.xml");

        // 🔹 2️⃣ Crear el documento XML en memoria
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); // Crea una fábrica de constructores DOM
        DocumentBuilder builder = null;
        Document doc = null;
        try {
            builder = factory.newDocumentBuilder(); // Obtiene un constructor concreto
            doc = builder.newDocument(); // Crea un nuevo documento XML vacío en memoria
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(PracticaAccesoADatosUT1.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Crea el elemento raíz <biblioteca>
        Element root = doc.createElement("libreria");
        // Añade el elemento raíz al documento
        doc.appendChild(root);

        // Crea el primer libro y lo añade al XML
        Element libro1 = crearLibro(doc, "20", "El Quijote", "Miguel de Cervantes");
        root.appendChild(libro1);

        // Crea el segundo libro y lo añade al XML
        Element libro2 = crearLibro(doc, "50", "Cien años de soledad", "Gabriel García Márquez");
        root.appendChild(libro2);

        // Mensaje informativo
        System.out.println("✅ XML creado en memoria.");

        // 🔹 5️⃣ Guardar el documento XML en disco
        TransformerFactory transformerFactory = TransformerFactory.newInstance(); // Fábrica de transformadores
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer(); // Crea un transformador
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // Formatear con sangrías
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            // Guardar en archivo
            transformer.transform(new DOMSource(doc), new StreamResult(xmlPath.toFile()));
            System.out.println("XML creado correctamente en: " + xmlPath.toAbsolutePath());

        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(PracticaAccesoADatosUT1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(PracticaAccesoADatosUT1.class.getName()).log(Level.SEVERE, null, ex);
        }

        //LEER EL XML
        // 🔹 6️⃣ Leer el XML y recorrerlo
        if (Files.exists(xmlPath)) { // Comprobamos si el archivo existe
            Document docLeido;
            try {
                docLeido = builder.parse(xmlPath.toFile()); // Carga el XML desde disco
                docLeido.getDocumentElement().normalize(); // Normaliza el documento (combina nodos de texto, elimina espacios)

                // Obtener todos los nodos <libro>
                NodeList listaLibros = docLeido.getElementsByTagName("libro");

                System.out.println("📖 Contenido leído del XML:");
                for (int i = 0; i < listaLibros.getLength(); i++) {
                    Node nodo = listaLibros.item(i); // Obtiene cada nodo <libro>
                    if (nodo.getNodeType() == Node.ELEMENT_NODE) { // Verifica que sea un elemento (no texto)
                        Element eLibro = (Element) nodo;

                        String precio = eLibro.getAttribute("precio"); // Lee el atributo id
                        String tituloTxt = eLibro.getElementsByTagName("titulo").item(0).getTextContent(); // Lee texto de <titulo>
                        String autorTxt = eLibro.getElementsByTagName("autor").item(0).getTextContent(); // Lee texto de <autor>

                        System.out.println("Precio: " + precio);
                        System.out.println("Título: " + tituloTxt);
                        System.out.println("Autor: " + autorTxt);
                    }
                }

                // 🔹 7️⃣ Modificar un valor en memoria (por ejemplo, cambiar el título)// cambiar el precio
                Element primerLibro = (Element) listaLibros.item(0);
                primerLibro.setAttribute("precio", "100");

                // Guardar los cambios nuevamente en el mismo archivo
                xmlPath = xmlPath.resolveSibling("libreria_revisada.xml");
                transformer.transform(new DOMSource(docLeido), new StreamResult(xmlPath.toFile()));
                System.out.println("XML actualizado correctamente.");
                
            } catch (SAXException ex) {
                Logger.getLogger(PracticaAccesoADatosUT1.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(PracticaAccesoADatosUT1.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TransformerException ex) {
                Logger.getLogger(PracticaAccesoADatosUT1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    // Método auxiliar para crear un elemento <libro>

    private static Element crearLibro(Document doc, String precio, String titulo, String autor) {
        // Crea un nuevo elemento <libro>
        Element libro = doc.createElement("libro");
        // Le asigna un atributo id
        libro.setAttribute("precio", precio);

        // Crea el subelemento <titulo> y le asigna su texto
        Element eTitulo = doc.createElement("titulo");
        eTitulo.setTextContent(titulo);
        // Crea el subelemento <autor> y le asigna su texto
        Element eAutor = doc.createElement("autor");
        eAutor.setTextContent(autor);

        // Añade los subelementos al libro
        libro.appendChild(eTitulo);
        libro.appendChild(eAutor);
        // Devuelve el elemento <libro> completo
        return libro;
    }
}
