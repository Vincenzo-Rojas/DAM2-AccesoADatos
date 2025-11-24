/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UT1;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;
import java.io.*;
import java.nio.file.*;

public class guiaXML { // Declaración de la clase pública llamada guiaXML

    // Ruta del archivo XML que se creará y manipulará
    private final Path xmlPath = Path.of("biblioteca_dom.xml");
    // Objeto que permitirá construir, leer y parsear documentos XML
    private DocumentBuilder builder;

    // Método principal del flujo de trabajo (llama a todos los pasos)
    public void iniciar() {
        inicializarParser();       // 1) Configura el parser DOM
        Document doc = crearXML(); // 2) Crea el documento XML en memoria
        escribirXML(doc);          // 3) Escribe el XML en un archivo físico
        Document docLeido = leerXML(); // 4) Lee el XML desde el archivo
        recorrerXML(docLeido);         // 5) Recorre y muestra el contenido del XML
        editarXML(docLeido);           // 6) Modifica el contenido del XML
        anadirNodo(docLeido);          // 7) Añade un nuevo nodo (libro)
        borrarNodo(docLeido);          // 8) Elimina un nodo existente
        guardarCambios(docLeido);      // 9) Guarda los cambios en disco
        mostrarFinal();                // 10) Muestra el contenido final del XML
        borrarArchivo();               // 11) Elimina el archivo del sistema
    }

    // ---------- 1) Inicializar parser DOM ----------
    private void inicializarParser() {
        try {
            // Crea una fábrica de constructores de documentos XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // Obtiene un DocumentBuilder a partir de la fábrica
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // Si hay error en la configuración del parser, se muestra mensaje de error
            System.err.println("Error al configurar parser DOM: " + e.getMessage());
        }
    }

    // ---------- 2) Crear XML desde cero ----------
    private Document crearXML() {
        // Crea un nuevo documento XML vacío
        Document doc = builder.newDocument();
        // Crea el elemento raíz <biblioteca>
        Element root = doc.createElement("biblioteca");
        // Añade el elemento raíz al documento
        doc.appendChild(root);

        // Crea el primer libro y lo añade al XML
        Element libro1 = crearLibro(doc, "1", "El Quijote", "Miguel de Cervantes");
        root.appendChild(libro1);

        // Crea el segundo libro y lo añade al XML
        Element libro2 = crearLibro(doc, "2", "Cien años de soledad", "Gabriel García Márquez");
        root.appendChild(libro2);

        // Mensaje informativo
        System.out.println("✅ XML creado en memoria.");
        // Devuelve el documento construido
        return doc;
    }

    // Método auxiliar para crear un elemento <libro>
    private Element crearLibro(Document doc, String id, String titulo, String autor) {
        // Crea un nuevo elemento <libro>
        Element libro = doc.createElement("libro");
        // Le asigna un atributo id
        libro.setAttribute("id", id);

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

    // ---------- 3) Guardar XML en disco ----------
    private void escribirXML(Document doc) {
        try {
            // Crea un objeto Transformer para convertir el DOM en archivo XML
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            // Configura la salida con indentación (legible)
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            // Transforma el DOM y lo guarda en la ruta indicada
            transformer.transform(new DOMSource(doc), new StreamResult(xmlPath.toFile()));
            // Mensaje de confirmación
            System.out.println("💾 Archivo XML guardado en: " + xmlPath.toAbsolutePath());
        } catch (TransformerException e) {
            // Captura errores de transformación o guardado
            System.err.println("Error al guardar XML: " + e.getMessage());
        }
    }

    // ---------- 4) Leer XML desde disco ----------
    private Document leerXML() {
        try {
            // Usa el builder para parsear (leer y convertir a DOM) el archivo XML
            Document doc = builder.parse(xmlPath.toFile());
            // Normaliza el documento (combina nodos de texto contiguos)
            doc.getDocumentElement().normalize();
            // Mensaje de éxito
            System.out.println("\n📖 XML leído correctamente.");
            // Devuelve el documento cargado
            return doc;
        } catch (IOException | SAXException e) {
            // En caso de error de lectura o sintaxis XML, se informa
            System.err.println("Error al leer XML: " + e.getMessage());
            return null;
        }
    }

    // ---------- 5) Recorrer XML ----------
    private void recorrerXML(Document doc) {
        // Si el documento no se pudo cargar, se detiene el método
        if (doc == null) return;

        // Obtiene el nodo raíz del XML (<biblioteca>)
        Element raiz = doc.getDocumentElement();
        System.out.println("\nNodo raíz: " + raiz.getNodeName());

        // Obtiene todos los hijos del nodo raíz (pueden ser <libro> o nodos de texto)
        NodeList hijosRaiz = raiz.getChildNodes();
        System.out.println("Número de nodos hijos del raíz: " + hijosRaiz.getLength());

        // Recorre cada nodo hijo del elemento raíz
        for (int i = 0; i < hijosRaiz.getLength(); i++) {
            Node hijo = hijosRaiz.item(i);
            // Solo procesa los nodos de tipo ELEMENT_NODE (ignora saltos o texto)
            if (hijo.getNodeType() == Node.ELEMENT_NODE) {
                System.out.println("Elemento hijo: " + hijo.getNodeName());

                // Obtiene los subnodos de cada libro (<titulo> y <autor>)
                NodeList subnodos = hijo.getChildNodes();

                // Recorre los subnodos del libro
                for (int j = 0; j < subnodos.getLength(); j++) {
                    Node subnodo = subnodos.item(j);
                    if (subnodo.getNodeType() == Node.ELEMENT_NODE) {
                        System.out.println("  Subnodo: " + subnodo.getNodeName() +
                                           " -> " + subnodo.getTextContent());
                    }
                }

                // Obtiene el primer y último hijo del elemento <libro>
                Node primerHijo = hijo.getFirstChild();
                Node ultimoHijo = hijo.getLastChild();

                // Muestra el primer hijo si es un elemento
                if (primerHijo != null && primerHijo.getNodeType() == Node.ELEMENT_NODE)
                    System.out.println("  Primer hijo: " + primerHijo.getNodeName());

                // Muestra el último hijo si es un elemento
                if (ultimoHijo != null && ultimoHijo.getNodeType() == Node.ELEMENT_NODE)
                    System.out.println("  Último hijo: " + ultimoHijo.getNodeName());
            }
        }
    }

    // ---------- 6) Editar XML ----------
    private void editarXML(Document doc) {
        // Obtiene todos los hijos del nodo raíz (libros)
        NodeList libros = doc.getDocumentElement().getChildNodes();
        for (int i = 0; i < libros.getLength(); i++) {
            Node nodoLibro = libros.item(i);
            // Verifica que sea un elemento <libro>
            if (nodoLibro.getNodeType() == Node.ELEMENT_NODE && nodoLibro.getNodeName().equals("libro")) {
                NodeList hijosLibro = nodoLibro.getChildNodes();
                for (int j = 0; j < hijosLibro.getLength(); j++) {
                    Node subnodo = hijosLibro.item(j);
                    // Si el subnodo es <titulo>, se modifica su texto
                    if (subnodo.getNodeName().equals("titulo")) {
                        subnodo.setTextContent("El Quijote (Edición Revisada)");
                        // Añade atributo 'lang' al libro
                        ((Element) nodoLibro).setAttribute("lang", "es");
                        System.out.println("\n✏️ Se ha editado el primer libro.");
                        return; // Termina tras editar el primero
                    }
                }
            }
        }
    }

    // ---------- 7) Añadir nuevo nodo ----------
    private void anadirNodo(Document doc) {
        // Obtiene la raíz <biblioteca>
        Element raiz = doc.getDocumentElement();
        // Crea un nuevo libro con datos y lo añade a la raíz
        Element nuevoLibro = crearLibro(doc, "3", "La sombra del viento", "Carlos Ruiz Zafón");
        raiz.appendChild(nuevoLibro);
        System.out.println("➕ Nuevo libro añadido (id=3).");
    }

    // ---------- 8) Borrar nodo ----------
    private void borrarNodo(Document doc) {
        // Obtiene la raíz
        Element raiz = doc.getDocumentElement();
        // Lista de libros actuales
        NodeList libros = raiz.getChildNodes();

        // Recorre los nodos para buscar el libro con id=2
        for (int i = 0; i < libros.getLength(); i++) {
            Node nodo = libros.item(i);
            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) nodo;
                // Si el atributo id es "2", lo elimina
                if ("2".equals(e.getAttribute("id"))) {
                    raiz.removeChild(e);
                    System.out.println("🗑️ Libro con id=2 eliminado.");
                    return;
                }
            }
        }
    }

    // ---------- 9) Guardar cambios ----------
    private void guardarCambios(Document doc) {
        try {
            // Usa Transformer para sobrescribir el archivo con los nuevos datos
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(new DOMSource(doc), new StreamResult(xmlPath.toFile()));
            System.out.println("💾 Cambios guardados en el archivo XML.");
        } catch (TransformerException e) {
            System.err.println("Error guardando cambios: " + e.getMessage());
        }
    }

    // ---------- 10) Mostrar XML final ----------
    private void mostrarFinal() {
        // Vuelve a leer el archivo actualizado
        Document docFinal = leerXML();
        if (docFinal == null) return;

        // Obtiene todos los elementos <libro>
        NodeList lista = docFinal.getElementsByTagName("libro");
        System.out.println("\n📚 Contenido final del XML:");

        // Recorre los libros y muestra título y autor
        for (int i = 0; i < lista.getLength(); i++) {
            Element e = (Element) lista.item(i);
            System.out.println("Libro id=" + e.getAttribute("id")
                    + " → " + e.getElementsByTagName("titulo").item(0).getTextContent()
                    + " | " + e.getElementsByTagName("autor").item(0).getTextContent());
        }
    }

    // ---------- 11) Borrar archivo XML ----------
    private void borrarArchivo() {
        try {
            // Intenta eliminar el archivo XML del disco
            boolean borrado = Files.deleteIfExists(xmlPath);
            if (borrado)
                System.out.println("\n🧹 Archivo XML eliminado.");
            else
                System.out.println("\n⚠️ No se encontró el archivo para borrar.");
        } catch (IOException e) {
            System.err.println("Error borrando archivo: " + e.getMessage());
        }
    }
}

