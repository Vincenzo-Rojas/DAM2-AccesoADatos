/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Ej2;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.xml.sax.SAXException;

/**
 *
 * @author DAM2B-11
 */
public class XML {

    /**
     *
     */
    // lista las diferentes categorias de las secciones del documento 
    public static void listarCategoriasSecciones(String rutaArchivo) {
        try {
            // Crear un objeto Path moderno a partir de la ruta proporcionada como parámetro
            Path rutaXML = Path.of(rutaArchivo);

            // Comprobar si el archivo existe; si no, mostrar error y salir del método
            if (!Files.exists(rutaXML)) {
                System.err.println("No se encontró el archivo XML: " + rutaXML.toAbsolutePath());
                return; // Sale del método si el archivo no existe
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(rutaXML.toFile());

            // Normalizar el documento combina nodos de texto adyacentes y limpia espacios
            doc.getDocumentElement().normalize();

            // OBTENER TODAS LAS SECCIONES
            // NodeList con todos los nodos <section> del documento
            NodeList secciones = doc.getElementsByTagName("section");

            System.out.println("Categorias de las secciones del documento:");

            // Iterar por cada nodo <section> encontrado
            for (int i = 0; i < secciones.getLength(); i++) {
                Node nodo = secciones.item(i); // Obtener el nodo i
                // Comprobar que el nodo es de tipo ELEMENT_NODE (evitar nodos de texto u otros tipos)
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element eSeccion = (Element) nodo; // Convertir a Element para acceder a atributos
                    String id = eSeccion.getAttribute("id"); // Leer atributo 'id' de la sección
                    String categoria = eSeccion.getAttribute("category"); // Leer atributo 'category'
                    // Mostrar por consola la información de la sección
                    System.out.println("Seccion ID: " + id + " -> Categoria: " + categoria);
                }
            }

        } catch (ParserConfigurationException e) {
            // Se lanza si falla la configuración del parser DOM
            e.printStackTrace();
        } catch (SAXException e) {
            // Se lanza si el XML está mal formado o no se puede parsear
            e.printStackTrace();
        } catch (IOException e) {
            // Se lanza si hay errores de lectura/escritura de archivos
            e.printStackTrace();
        }
    }

    public static void buscarAyo(String rutaArchivo) {
        // Crear un diccionario (clave → valor)
        Map<String, Integer> diccionario = new HashMap<>();
        
        try {
            // Crear un objeto Path moderno a partir de la ruta proporcionada como parámetro
            Path rutaXML = Path.of(rutaArchivo);

            // Comprobar si el archivo existe; si no, mostrar error y salir del método
            if (!Files.exists(rutaXML)) {
                System.err.println("No se encontró el archivo XML: " + rutaXML.toAbsolutePath());
                return; // Sale del método si el archivo no existe
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(rutaXML.toFile());

            // Normalizar el documento combina nodos de texto adyacentes y limpia espacios
            doc.getDocumentElement().normalize();

            // OBTENER: ayo con mayor porcentaje de uso de la tajeta de credito - 2020
            // NodeList con todos los nodos <cell> del documento
            NodeList cell = doc.getElementsByTagName("cell");

            System.out.println("Ejercicio de ayos y usos:");

            
            String claveTemporal = null; // aquí guardas el ayo

            // Iterar por cada nodo <section> encontrado
            for (int i = 0; i < cell.getLength(); i++) {
                Node nodo = cell.item(i); // Obtener el nodo i
                // Comprobar que el nodo es de tipo ELEMENT_NODE (evitar nodos de texto u otros tipos)
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element eSeccion = (Element) nodo; // Convertir a Element para acceder a atributos
                    String cellcategory = eSeccion.getAttribute("category"); // Anio o uso
                    String contenido = eSeccion.getTextContent(); // Contenido de la cellCategory

                    if (cellcategory.equals("anio")) {
                        // guardamos la clave para la próxima celda
                        claveTemporal = contenido;
                    }
                    if (cellcategory.equals("uso")) {
                        // procesar porcentaje
                        contenido = contenido.replace("%", "");
                        int valor = Integer.parseInt(contenido);

                        // ahora sí podemos guardar clave → valor
                        diccionario.put(claveTemporal, valor);

                        //limpiar la clave temporal
                        claveTemporal = null;
                    }
                }
            }

        } catch (ParserConfigurationException e) {
            // Se lanza si falla la configuración del parser DOM
            e.printStackTrace();
        } catch (SAXException e) {
            // Se lanza si el XML está mal formado o no se puede parsear
            e.printStackTrace();
        } catch (IOException e) {
            // Se lanza si hay errores de lectura/escritura de archivos
            e.printStackTrace();
        } catch (NumberFormatException e){
            e.printStackTrace();
        }
        
        int max = 0;
        String ayo = null;
        
        for (Map.Entry<String, Integer> entrada : diccionario.entrySet()) {
            //System.out.println(entrada.getKey() + " -> " + entrada.getValue());
            if(max < entrada.getValue()){
                max = entrada.getValue();
            }
        }
        
        for (Map.Entry<String, Integer> entrada : diccionario.entrySet()) {
            //System.out.println(entrada.getKey() + " -> " + entrada.getValue());
            if(max == entrada.getValue()){
                ayo = entrada.getKey();
            }
        }
        
        System.out.println("El maximo porcentaje de uso es: "+ max +"% en el ayo: "+ ayo);
        
    }
    
    public static void addConsejo(String rutaArchivo){
        try {
            // Crear DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Cargar el XML
            Document doc = builder.parse(new File(rutaArchivo));
            doc.getDocumentElement().normalize();

            // Buscar el section con id="sec3"
            NodeList sections = doc.getElementsByTagName("section");
            Element sec3 = null;

            for (int i = 0; i < sections.getLength(); i++) {
                Node nodo = sections.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) nodo;
                    if (e.getAttribute("id").equals("sec3")) {
                        sec3 = e;
                    }
                }
            }

            if (sec3 == null) {
                System.out.println("No se encontró <section id=\"sec3\">");
                return;
            }

            // Crear el nuevo paragraph
            Element paragraph = doc.createElement("paragraph");
            paragraph.setAttribute("category", "consejo");
            paragraph.setTextContent("recuerda no dejar al descubierto tu cuenta al pagar, para evitar intereses excesivos y situaciones no deseadas.");

            // Añadir el paragraph a la sección
            sec3.appendChild(paragraph);

            // Guardar los cambios en el archivo
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();

            // Fuente y resultado
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(Paths.get( "articulo_mod.xml").toFile());

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);

            System.out.println("Paragraph añadido correctamente en <section id=\"sec3\">");

        } catch (ParserConfigurationException e) {
            System.err.println("Error configurando el parser XML: " + e.getMessage());
            e.printStackTrace();
        } catch (SAXException e) {
            System.err.println("Error al parsear el XML: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error de E/S con el archivo: " + e.getMessage());
            e.printStackTrace();
        } catch (TransformerException e) {
            System.err.println("Error guardando el XML modificado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String rutaActual = System.getProperty("user.dir");
        System.out.println("Directorio de ejecucion: " + rutaActual);

        listarCategoriasSecciones("articulo.xml");
        buscarAyo("articulo.xml");
        addConsejo("articulo.xml");

    }

}
