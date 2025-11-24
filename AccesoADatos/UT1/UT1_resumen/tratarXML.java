/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
 /*
 * Ejemplo completo de manejo de ficheros XML usando DOM (Document Object Model)
 * ------------------------------------------------------------
 * Este ejemplo muestra cómo:
 *  1️⃣ Crear un documento XML en memoria.
 *  2️⃣ Añadir nodos (elementos y atributos).
 *  3️⃣ Guardarlo en disco.
 *  4️⃣ Leer un XML existente.
 *  5️⃣ Recorrer sus elementos y modificar contenido.
 * 
 * Se utilizan las clases del paquete javax.xml.parsers y org.w3c.dom,
 * que permiten representar y manipular un documento XML como un árbol.
 */
package UT1;

import org.w3c.dom.*;                 // Contiene las clases para manejar nodos XML (Document, Element, NodeList, etc.)
import javax.xml.parsers.*;          // Contiene DocumentBuilder y DocumentBuilderFactory para crear o leer documentos XML
import javax.xml.transform.*;        // Para transformar (guardar) el documento XML
import javax.xml.transform.dom.DOMSource;  // Fuente de datos basada en el árbol DOM
import javax.xml.transform.stream.StreamResult; // Destino (archivo o consola)
import java.io.File;                 // Para manejar rutas de archivo con java.io
import java.nio.file.Path;           // Para rutas modernas (java.nio)
import java.nio.file.Files;          // Para verificar existencia del archivo
import java.io.IOException;          // Para capturar errores de E/S
import org.xml.sax.SAXException;

public class tratarXML {

    public static void main(String[] args) {
        try {
            // 🔹 1️⃣ Crear la ruta donde se guardará el XML
            Path ruta = Path.of("ejemplo_dom.xml"); // Creamos un Path moderno con java.nio

            // 🔹 2️⃣ Crear el documento XML en memoria
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); // Crea una fábrica de constructores DOM
            DocumentBuilder builder = factory.newDocumentBuilder(); // Obtiene un constructor concreto
            Document doc = builder.newDocument(); // Crea un nuevo documento XML vacío en memoria

            // 🔹 3️⃣ Crear el nodo raíz (por ejemplo, <libros>)
            Element root = doc.createElement("libros"); // Creamos el elemento raíz
            doc.appendChild(root); // Lo añadimos al documento

            // 🔹 4️⃣ Añadir un elemento hijo (<libro>) con atributos y texto
            Element libro = doc.createElement("libro"); // Creamos el nodo <libro>
            libro.setAttribute("id", "1"); // Añadimos atributo id="1"

            Element titulo = doc.createElement("titulo"); // Nodo <titulo>
            titulo.setTextContent("El Quijote"); // Texto interno del nodo

            Element autor = doc.createElement("autor"); // Nodo <autor>
            autor.setTextContent("Miguel de Cervantes"); // Texto interno del nodo

            // Insertamos los hijos dentro de <libro>
            libro.appendChild(titulo);
            libro.appendChild(autor);

            // Finalmente, añadimos <libro> dentro de <libros>
            root.appendChild(libro);

            // 🔹 5️⃣ Guardar el documento XML en disco
            TransformerFactory transformerFactory = TransformerFactory.newInstance(); // Fábrica de transformadores
            Transformer transformer = transformerFactory.newTransformer(); // Crea un transformador
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // Formatear con sangrías
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            // Guardar en archivo
            transformer.transform(new DOMSource(doc), new StreamResult(ruta.toFile()));
            System.out.println("XML creado correctamente en: " + ruta.toAbsolutePath());

            // 🔹 6️⃣ Leer el XML y recorrerlo
            if (Files.exists(ruta)) { // Comprobamos si el archivo existe
                Document docLeido = builder.parse(ruta.toFile()); // Carga el XML desde disco
                docLeido.getDocumentElement().normalize(); // Normaliza el documento (combina nodos de texto, elimina espacios)

                // Obtener todos los nodos <libro>
                NodeList listaLibros = docLeido.getElementsByTagName("libro");

                System.out.println("📖 Contenido leído del XML:");
                for (int i = 0; i < listaLibros.getLength(); i++) {
                    Node nodo = listaLibros.item(i); // Obtiene cada nodo <libro>
                    if (nodo.getNodeType() == Node.ELEMENT_NODE) { // Verifica que sea un elemento (no texto)
                        Element eLibro = (Element) nodo;

                        String id = eLibro.getAttribute("id"); // Lee el atributo id
                        String tituloTxt = eLibro.getElementsByTagName("titulo").item(0).getTextContent(); // Lee texto de <titulo>
                        String autorTxt = eLibro.getElementsByTagName("autor").item(0).getTextContent(); // Lee texto de <autor>

                        System.out.println("Libro ID: " + id);
                        System.out.println("Título: " + tituloTxt);
                        System.out.println("Autor: " + autorTxt);
                    }
                }

                // 🔹 7️⃣ Modificar un valor en memoria (por ejemplo, cambiar el título)
                Element primerLibro = (Element) listaLibros.item(0);
                primerLibro.getElementsByTagName("titulo").item(0).setTextContent("El Quijote (Edición Revisada)");

                // Guardar los cambios nuevamente en el mismo archivo
                transformer.transform(new DOMSource(docLeido), new StreamResult(ruta.toFile()));
                System.out.println("XML actualizado correctamente.");
            }

        } catch (ParserConfigurationException e) {
            System.err.println("Error configurando el parser XML.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error de E/S al acceder al archivo.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error procesando el XML.");
            e.printStackTrace();
        }
    }

    // OPERACIONES XML

    /*
 * Ejemplo completo: crear, escribir, leer, recorrer, editar, añadir,
 * borrar nodos y borrar el archivo XML en disco usando DOM.
     */
    public void xmlDom() {
        // Definimos la ruta del archivo XML que vamos a crear y manipular

        Path xmlPath = Path.of("ejemplo_dom_full.xml"); // Path moderno que apunta a ejemplo_dom_full.xml

        // Creamos la fábrica de parsers DOM
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); // Fábrica para constructores DOM
        DocumentBuilder builder = null; // Declaramos el DocumentBuilder (se inicializa abajo)

        try {
            builder = factory.newDocumentBuilder(); // Obtenemos un DocumentBuilder a partir de la fábrica
        } catch (ParserConfigurationException e) {
            // Si la configuración del parser falla, no podemos continuar
            System.err.println("Error configurando el parser DOM: " + e.getMessage());
            e.printStackTrace();
            return; // Salimos porque no es posible continuar
        }

        // ---------------------------
        // 1) CREAR Y ESCRIBIR (GENERAR) XML
        // ---------------------------
        Document doc = builder.newDocument(); // Creamos un documento DOM vacío en memoria
        Element root = doc.createElement("library"); // Creamos elemento raíz <library>

        doc.appendChild(root); // Añadimos la raíz al documento

        // Creamos el primer <book id="1"> con título y autor
        Element book1 = doc.createElement("book"); // <book>

        book1.setAttribute(
                "id", "1"); // atributo id="1"
        Element title1 = doc.createElement("title"); // <title>

        title1.setTextContent(
                "El Quijote"); // texto dentro de <title>
        Element author1 = doc.createElement("author"); // <author>

        author1.setTextContent(
                "Miguel de Cervantes"); // texto dentro de <author>
        book1.appendChild(title1); // <book> <- <title>

        book1.appendChild(author1); // <book> <- <author>

        root.appendChild(book1); // <library> <- <book>

        // Creamos el segundo <book id="2"> con título y autor
        Element book2 = doc.createElement("book"); // <book>

        book2.setAttribute(
                "id", "2"); // id="2"
        Element title2 = doc.createElement("title"); // <title>

        title2.setTextContent(
                "Cien años de soledad"); // texto
        Element author2 = doc.createElement("author"); // <author>

        author2.setTextContent(
                "Gabriel García Márquez"); // texto
        book2.appendChild(title2); // <book> <- <title>

        book2.appendChild(author2); // <book> <- <author>

        root.appendChild(book2); // <library> <- <book>

        // Guardamos el documento en disco (escritura del archivo XML)
        try {
            TransformerFactory tf = TransformerFactory.newInstance(); // Fábrica de transformers
            Transformer transformer = tf.newTransformer(); // Crear transformer para serializar DOM a archivo
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // Pide indentado para legibilidad

            // No esta en los apuntes del profesor
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2"); // Cantidad de espacios

            // Transformamos DOM -> archivo físico usando la ruta Path convertida a File
            transformer.transform(new DOMSource(doc), new StreamResult(xmlPath.toFile()));

            //otra forma de hacerlo
            /*
             DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(Path.of("xml", "dom", "juegos.xml").toFile());
            
            t.transform(source, result);
            
             */
            System.out.println("XML creado y escrito en: " + xmlPath.toAbsolutePath());
        } catch (TransformerException e) {
            // Si falla la transformación (escritura), se muestra el error
            System.err.println("Error escribiendo XML: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // ---------------------------
        // 2) LEER Y RECORRER (PARSING Y TRAVERSAL)
        // ---------------------------
        Document docLeido = null; // Documento que cargaremos desde disco

        try {
            // Parseamos (leemos) el archivo XML desde disco al DOM en memoria
            docLeido = builder.parse(xmlPath.toFile()); // builder.parse(File) carga y construye el DOM
            docLeido.getDocumentElement().normalize(); // Normalizamos (quita nodos de texto redundantes)
        } catch (SAXException e) {
            // Error al parsear el XML (documento mal formado)
            System.err.println("XML mal formado: " + e.getMessage());
            e.printStackTrace();
            return;
        } catch (IOException e) {
            // Error de E/S (archivo no encontrado, permisos, etc.)
            System.err.println("Error de E/S al leer el XML: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Recorremos todos los nodos <book> y mostramos sus datos
        NodeList listaBooks = docLeido.getElementsByTagName("book"); // Obtenemos lista de <book>

        System.out.println(
                "Recorrido inicial del XML:");
        for (int i = 0; i < listaBooks.getLength(); i++) { // Iteramos por cada <book>
            Node nodo = listaBooks.item(i); // Obtenemos el nodo i-ésimo
            if (nodo.getNodeType() == Node.ELEMENT_NODE) { // Comprobamos que es un elemento
                Element eBook = (Element) nodo; // Convertimos a Element para acceder a métodos específicos
                // Leemos atributo id y los subelementos <title> y <author>
                String id = eBook.getAttribute("id"); // atributo id
                String titulo = eBook.getElementsByTagName("title").item(0).getTextContent(); // texto de <title>
                String autor = eBook.getElementsByTagName("author").item(0).getTextContent(); // texto de <author>
                // Mostramos en consola
                System.out.println("Libro id=" + id + " -> Título: " + titulo + " | Autor: " + autor);
            }
        }

        //CON getChield
        /*
        
         // Obtenemos el nodo raíz
Element raiz = builder.parse(new File(nombreArchivo)).getDocumentElement(); // <biblioteca>

   - Se utiliza el objeto DocumentBuilder (`builder`) para parsear (analizar y cargar) el archivo XML cuyo nombre está en la variable `nombreArchivo`.
   - `parse()` devuelve un objeto Document, que representa el XML completo.
   - `getDocumentElement()` devuelve el nodo raíz del documento, en este caso <biblioteca>.

System.out.println("\nNodo raíz: " + raiz.getNodeName());
// Muestra por consola el nombre del nodo raíz, normalmente "biblioteca".

// Obtenemos los hijos directos del nodo raíz
NodeList hijosRaiz = raiz.getChildNodes();

   - `getChildNodes()` obtiene todos los nodos hijos de la raíz.
   - Estos pueden incluir elementos (como <libro>) y nodos de texto (espacios o saltos de línea).

System.out.println("Número de nodos hijos del raíz: " + hijosRaiz.getLength());
// Muestra cuántos nodos hijos tiene el nodo raíz (incluyendo texto y elementos).

// Recorremos los hijos de <biblioteca>
for (int i = 0; i < hijosRaiz.getLength(); i++) {
    Node hijo = hijosRaiz.item(i); // Puede ser <libro> o un nodo de texto (espacio)
    
       - Con `item(i)` accedemos a cada hijo del nodo raíz.
       - Algunos serán elementos (<libro>), otros nodos de texto (por los espacios o saltos de línea en el XML).
    
    if (hijo.getNodeType() == Node.ELEMENT_NODE) { // Ignoramos texto o saltos de línea
        // Solo procesamos los nodos que sean elementos XML (no texto).
        System.out.println("Elemento hijo: " + hijo.getNodeName());
        // Muestra el nombre del nodo hijo, por ejemplo "libro".

        // Obtenemos sus hijos (por ejemplo, <titulo> y <autor>)
        NodeList subnodos = hijo.getChildNodes();
        
           - Cada <libro> puede tener varios subnodos: <titulo>, <autor>, <anio>, etc.
           - También puede incluir nodos de texto (espacios, saltos de línea).
        
        for (int j = 0; j < subnodos.getLength(); j++) {
            Node subnodo = subnodos.item(j);
            // Accedemos a cada nodo dentro de <libro>.
            if (subnodo.getNodeType() == Node.ELEMENT_NODE) {
                
                   - Solo mostramos los subnodos que sean elementos (no texto).
                   - `getTextContent()` devuelve el texto contenido dentro del nodo,
                     por ejemplo el título del libro.
                
                System.out.println("  Subnodo: " + subnodo.getNodeName()
                        + " -> " + subnodo.getTextContent());
            }
        }

        // Ejemplo de uso de getFirstChild() y getLastChild()
        Node primerHijo = hijo.getFirstChild();
        Node ultimoHijo = hijo.getLastChild();
        
           - `getFirstChild()` obtiene el primer nodo hijo del elemento <libro>.
           - `getLastChild()` obtiene el último nodo hijo.
           - Estos pueden ser nodos de texto o elementos.
        
        if (primerHijo != null && primerHijo.getNodeType() == Node.ELEMENT_NODE) {
            // Comprueba que el primer hijo exista y sea un elemento XML.
            System.out.println("  Primer hijo: " + primerHijo.getNodeName());
        }
        if (ultimoHijo != null && ultimoHijo.getNodeType() == Node.ELEMENT_NODE) {
            // Comprueba que el último hijo exista y sea un elemento XML.
            System.out.println("  Último hijo: " + ultimoHijo.getNodeName());
        }
    }
}

         */
        // ---------------------------
        // 3) EDITAR NODOS (MODIFICAR CONTENIDO Y ATRIBUTOS)
        // ---------------------------
        // Ejemplo: cambiar el título del primer libro (id="1")
        if (listaBooks.getLength()
                > 0) { // Comprobamos que exista al menos un <book>
            Element primerLibro = (Element) listaBooks.item(0); // Primer <book>
            // Accedemos al nodo <title> y cambiamos su contenido
            Node titleNode = primerLibro.getElementsByTagName("title").item(0); // nodo <title>
            if (titleNode != null) {
                titleNode.setTextContent("El Quijote (Edición Comentada)"); // Modificamos el texto
                System.out.println("Título del primer libro modificado en memoria.");
            }
            // También podemos modificar atributos: por ejemplo añadimos un atributo 'lang'
            primerLibro.setAttribute("lang", "es"); // Añadimos atributo lang="es"
            System.out.println("Atributo 'lang' añadido al primer libro.");
        }

        // CON getCHIELD
        /* Obtiene todos los nodos hijos directos del nodo raíz. NodeList incluye tanto elementos () como nodos de texto (espacios, saltos de línea).
NodeList libros = raiz.getChildNodes();

// Itera por cada nodo presente en la NodeList 'libros'
for (int i = 0; i < libros.getLength(); i++) {
// Recupera el nodo en la posición i del NodeList
Node nodoLibro = libros.item(i);

/* Comprueba dos cosas a la vez:
   1) que el nodo sea un ELEMENT_NODE (evita procesar nodos de texto u otros tipos),
   2) que el nombre del elemento sea exactamente "libro".
   Esto asegura que solo procesamos nodos <libro>. 
if (nodoLibro.getNodeType() == Node.ELEMENT_NODE && nodoLibro.getNodeName().equals("libro")) {

    /* Obtiene la lista de nodos hijos del elemento <libro>.
       Estos hijos suelen ser elementos como <titulo>, <autor> y también pueden incluir nodos de texto (por ejemplo, espacios). 
    NodeList hijosLibro = nodoLibro.getChildNodes();

    // Recorre todos los subnodos del <libro>
    for (int j = 0; j < hijosLibro.getLength(); j++) {
        // Recupera el subnodo en la posición j
        Node subnodo = hijosLibro.item(j);

        /* Comprueba si el nombre del subnodo es "titulo".
           Observaciones:
           - Si el subnodo es un nodo de texto su getNodeName() devolverá "#text", por lo que esta comparación fallará y ese subnodo será ignorado.
           - Alternativamente podrías comprobar primero subnodo.getNodeType() == Node.ELEMENT_NODE para evitar llamadas innecesarias a getNodeName(). 
        if (subnodo.getNodeName().equals("titulo")) {
            // Modifica el texto contenido en el nodo <titulo>
            subnodo.setTextContent("1984 (Edición Revisada)");

            // Mensaje informativo por consola indicando que se realizó la edición
            System.out.println("\nSe ha editado el título del libro usando getChildNodes()");
        }
    }
}
         */
        // ---------------------------
        // 4) AÑADIR NODOS (CREAR NUEVOS ELEMENTOS)
        // ---------------------------
        // Creamos un nuevo <book id="3"> y lo añadimos a la raíz
        Element book3 = docLeido.createElement("book"); // Nuevo elemento <book> en el documento leído

        book3.setAttribute("id", "3"); // id="3"
        Element title3 = docLeido.createElement("title"); // <title>

        title3.setTextContent("La sombra del viento"); // texto
        Element author3 = docLeido.createElement("author"); // <author>

        author3.setTextContent(
                "Carlos Ruiz Zafón"); // texto
        book3.appendChild(title3); // <book> <- <title>

        book3.appendChild(author3); // <book> <- <author>

        docLeido.getDocumentElement().appendChild(book3); // <library> <- <book id=3>
        System.out.println("Nuevo libro (id=3) añadido en memoria.");

        // ---------------------------
        // 5) BORRAR NODOS (ELIMINAR ELEMENTOS DEL DOM)
        // ---------------------------
        // Ejemplo: eliminar el <book> con id="2" si existe
        Node nodoAEliminar = null; // Referencia al nodo que eliminaremos
        NodeList booksParaBorrar = docLeido.getElementsByTagName("book"); // Re-obtenemos la lista actualizada
        // Recorremos de atrás hacia delante para poder eliminar sin comprometer el NodeList
        for (int i = booksParaBorrar.getLength() - 1; i >= 0; i--) {
            Element e = (Element) booksParaBorrar.item(i); // Element <book> actual
            String id = e.getAttribute("id"); // Leemos su atributo id
            if ("2".equals(id)) { // Si coincide con "2"
                nodoAEliminar = e; // Marcamos para eliminar
                break; // Salimos del bucle (hemos encontrado el que queremos borrar)
            }
        }
        if (nodoAEliminar != null) { // Si encontramos el nodo a eliminar
            Node padre = nodoAEliminar.getParentNode(); // Obtenemos el nodo padre (la raíz)
            padre.removeChild(nodoAEliminar); // Eliminamos el nodo del árbol DOM
            System.out.println("Se ha eliminado el <book id=\"2\"> del DOM en memoria.");
        } else {
            System.out.println("No se encontró <book id=\"2\"> para eliminar.");
        }

        // ---------------------------
        // 6) GUARDAR (SERIALIZAR) LOS CAMBIOS EN EL ARCHIVO XML
        // ---------------------------
        try {
            TransformerFactory tf2 = TransformerFactory.newInstance(); // Nueva fábrica de transformers
            Transformer transformer2 = tf2.newTransformer(); // Nuevo transformer
            transformer2.setOutputProperty(OutputKeys.INDENT, "yes"); // Indentado para legibilidad
            transformer2.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2"); // Espaciado de indentado

            // Serializamos el DOM modificado al mismo archivo xmlPath (sobrescribe el archivo existente)
            transformer2.transform(new DOMSource(docLeido), new StreamResult(xmlPath.toFile()));
            System.out.println("Cambios guardados en el archivo XML: " + xmlPath.toAbsolutePath());
        } catch (TransformerException e) {
            System.err.println("Error guardando cambios en el XML: " + e.getMessage());
            e.printStackTrace();
        }

        // ---------------------------
        // 7) (OPCIONAL) VOLVER A LEER Y MOSTRAR EL RESULTADO FINAL
        // ---------------------------
        try {
            Document docFinal = builder.parse(xmlPath.toFile()); // Parseamos el archivo actualizado
            docFinal.getDocumentElement().normalize(); // Normalizamos
            NodeList finalBooks = docFinal.getElementsByTagName("book"); // Obtenemos lista actualizada

            System.out.println("Estado final del XML tras modificaciones:");
            for (int i = 0; i < finalBooks.getLength(); i++) { // Recorremos y mostramos cada libro
                Element e = (Element) finalBooks.item(i);
                String id = e.getAttribute("id");
                String t = e.getElementsByTagName("title").item(0).getTextContent();
                // El autor puede no existir si lo eliminamos; comprobamos su existencia
                NodeList autores = e.getElementsByTagName("author");
                String a = (autores.getLength() > 0) ? autores.item(0).getTextContent() : "(sin autor)";
                System.out.println("Libro id=" + id + " -> Título: " + t + " | Autor: " + a);
            }
        } catch (SAXException e) {
            System.err.println("XML final mal formado al re-parsear: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error de E/S al re-leer el XML final: " + e.getMessage());
            e.printStackTrace();
        }

        // ---------------------------
        // 8) BORRAR EL ARCHIVO XML DEL SISTEMA DE FICHEROS
        // ---------------------------
        try {
            boolean borrado = Files.deleteIfExists(xmlPath); // Borra el archivo si existe; devuelve true si lo borró
            if (borrado) {
                System.out.println("Archivo XML borrado del disco: " + xmlPath.toAbsolutePath());
            } else {
                System.out.println("No se encontró el archivo para borrar (o ya fue borrado): " + xmlPath.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Error borrando el archivo XML: " + e.getMessage());
            e.printStackTrace();
        }

        // Fin del ejemplo
    }
}
