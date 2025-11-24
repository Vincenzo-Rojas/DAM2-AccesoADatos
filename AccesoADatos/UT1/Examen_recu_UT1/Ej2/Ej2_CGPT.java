package Ej2;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
//import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Ej2_CGPT {

    /*
    * EJERCICIOS PROPUESTOS SOBRE TU XML
1. Listado y consulta básica (nivel fácil)
 1.1 Listar todas las categorías de las secciones

Recorrer <section> y mostrar el atributo category.
→ Ya lo tienes hecho.
* */
    public static void categoriasSeccion(String rutaArchivo){
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

    /*
 1.2 Cuenta cuantas secciones tipo <x(paragraph, section...)> hay con x(intro, consejo...) atributo category
*/
    public static void seccionesXCategoria(String rutaArchivo){
        try {
            // Crear un objeto Path moderno a partir de la ruta proporcionada como parámetro
            Path rutaXML = Path.of(rutaArchivo);

            // Comprobar si el archivo existe; si no, mostrar error y salir del método
            if (!Files.exists(rutaXML)) {
                System.err.println("No se encontró el archivo XML: " + rutaXML.toAbsolutePath());
                return; //Sale del método si el archivo no existe
            }

            // Crear DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Cargar el XML
            Document doc = builder.parse(rutaXML.toFile());
            doc.getDocumentElement().normalize();

            // Obtener todas las secciones
            NodeList secciones = doc.getElementsByTagName("paragraph");

            // Mapa categoría → contador
            Map<String, Integer> contadorCategorias = new HashMap<>();

            // Recorrer las secciones
            for (int i = 0; i < secciones.getLength(); i++) {
                Node nodo = secciones.item(i);

                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element eSeccion = (Element) nodo;

                    // Obtener el atributo category
                    String categoria = eSeccion.getAttribute("category");

                    // Si no existe, empieza en 1; si existe, suma 1
                    contadorCategorias.put(
                            categoria,
                            contadorCategorias.getOrDefault(categoria, 0) + 1
                    );
                }
            }

            // Mostrar resultados
            System.out.println("Conteo de secciones por categoría:");
            for (Map.Entry<String, Integer> entry : contadorCategorias.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }

        } catch (ParserConfigurationException e) {
            System.err.println("Error configurando el parser XML: " + e.getMessage());
            e.printStackTrace();
        } catch (SAXException e) {
            System.err.println("Error leyendo el XML: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error accediendo al archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /*
 1.3 Mostrar el texto de todos los <paragraph>

Recoger todos los nodos <paragraph> y mostrar su contenido.
*/
    public static void textoParagraph(String rutaArchivo){
        try {
            // Crear un objeto Path moderno a partir de la ruta proporcionada como parámetro
            Path rutaXML = Path.of(rutaArchivo);

            // Comprobar si el archivo existe; si no, mostrar error y salir del método
            if (!Files.exists(rutaXML)) {
                System.err.println("No se encontró el archivo XML: " + rutaXML.toAbsolutePath());
                return; //Sale del método si el archivo no existe
            }

            // Crear DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Cargar el XML
            Document doc = builder.parse(rutaXML.toFile());
            doc.getDocumentElement().normalize();

            // Obtener todas las secciones
            NodeList secciones = doc.getElementsByTagName("paragraph");

            // Recorrer las secciones
            for (int i = 0; i < secciones.getLength(); i++) {
                Node nodo = secciones.item(i);

                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element eSeccion = (Element) nodo;

                    // Obtener el texto
                    String texto = eSeccion.getTextContent();
                    System.out.println("<paragraph> : " + texto);
                }
            }

        } catch (ParserConfigurationException e) {
            System.err.println("Error configurando el parser XML: " + e.getMessage());
            e.printStackTrace();
        } catch (SAXException e) {
            System.err.println("Error leyendo el XML: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error accediendo al archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /*
2. Procesamiento de datos (nivel medio)
 2.1 Encontrar el año con mayor porcentaje de uso

Leer celdas <cell category="anyo">2020</cell> y <cell category="uso">35%</cell>.

Asociarlas (este ejercicio te viene perfecto para practicar claves/valores).

Encontrar el porcentaje máximo.

*/
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

            System.out.println("\nEjercicio de ayos y usos:");


            String claveTemporal = null; // guardar el ayo para clave - valor

            // Iterar por cada nodo <section> encontrado
            for (int i = 0; i < cell.getLength(); i++) {
                Node nodo = cell.item(i); // Obtener el nodo i
                // Comprobar que el nodo es de tipo ELEMENT_NODE (evitar nodos de texto u otros tipos)
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element eSeccion = (Element) nodo; // Convertir a Element para acceder a atributos
                    String cellcategory = eSeccion.getAttribute("category"); // Anio o uso
                    String contenido = eSeccion.getTextContent(); // Contenido de la cellCategory

                    switch (cellcategory) {
                        case "anio":
                            // Guardamos el año para asociarlo al próximo uso
                            claveTemporal = contenido;
                            break;

                        case "uso":
                            if (claveTemporal != null) {
                                try {
                                    //cambia x% a x, dejando solo el numero
                                    contenido = contenido.replace("%", "");
                                    int valor = Integer.parseInt(contenido);

                                    diccionario.put(claveTemporal, valor);

                                } catch (NumberFormatException ex) {
                                    System.err.println("Error convirtiendo porcentaje: " + contenido);
                                }
                            }
                            claveTemporal = null;
                            break;
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

        // Calcular el mayor porcentaje
        int maxUso = -1;
        String mejorAño = null;

        for (Map.Entry<String, Integer> entrada : diccionario.entrySet()) {
            int uso = entrada.getValue();
            if (uso > maxUso) {
                maxUso = uso;
                mejorAño = entrada.getKey();
            }
        }

        if (mejorAño != null) {
            System.out.println("El máximo porcentaje de uso es " + maxUso + "% en el año: " + mejorAño);
        } else {
            System.out.println("No se encontraron datos válidos.");
        }

    }

    /*

 2.3 Calcular la media de uso de todos los años
Trabaja con datos numéricos, ignorando %.
*/
    public static void mediaTotalUso(String rutaArchivo) {
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

            System.out.println("\nEjercicio de ayos y usos:");


            String claveTemporal = null; // guardar el ayo para clave - valor

            // Iterar por cada nodo <section> encontrado
            for (int i = 0; i < cell.getLength(); i++) {
                Node nodo = cell.item(i); // Obtener el nodo i
                // Comprobar que el nodo es de tipo ELEMENT_NODE (evitar nodos de texto u otros tipos)
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element eSeccion = (Element) nodo; // Convertir a Element para acceder a atributos
                    String cellcategory = eSeccion.getAttribute("category"); // Anio o uso
                    String contenido = eSeccion.getTextContent(); // Contenido de la cellCategory

                    switch (cellcategory) {
                        case "anio":
                            // Guardamos el año para asociarlo al próximo uso
                            claveTemporal = contenido;
                            break;

                        case "uso":
                            if (claveTemporal != null) {
                                try {
                                    //cambia x% a x, dejando solo el numero
                                    contenido = contenido.replace("%", "");
                                    int valor = Integer.parseInt(contenido);

                                    diccionario.put(claveTemporal, valor);

                                } catch (NumberFormatException ex) {
                                    System.err.println("Error convirtiendo porcentaje: " + contenido);
                                }
                            }
                            claveTemporal = null;
                            break;
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

        // Calcular el mayor porcentaje
        float media = 0;
        int contador = 0;

        for (Map.Entry<String, Integer> entrada : diccionario.entrySet()) {
            contador+=1;
            media+= entrada.getValue();
        }
        media = media/contador;

        System.out.println("La media de uso en todos los años es de: "+ media + "%");

    }

    /*

3. Modificación del XML (nivel medio-alto)
 3.1 Insertar un <paragraph> nuevo en cualquier sección

Como ya hiciste con <section id="sec3">.
Hazlo general: un metodo
*/
    public static void insertarParagraph(String rutaArchivo,String tipoEtiqueta, String idSeccion, String categoria, String texto){

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

            // Buscar el section con idSeccion="secX"
            NodeList sections = doc.getElementsByTagName(tipoEtiqueta);
            Element sec = null;

            for (int i = 0; i < sections.getLength(); i++) {
                Node nodo = sections.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) nodo;
                    if (e.getAttribute("id").equals(idSeccion)) {
                        sec = e;
                    }
                }
            }

            if (sec == null) {
                System.out.println("No se encontró <section id=\""+idSeccion+"\">");
                return;
            }

            // Crear el nuevo paragraph
            Element paragraph = doc.createElement("paragraph");
            paragraph.setAttribute("category", categoria);
            paragraph.setTextContent(texto);

            // Añadir el paragraph a la sección
            sec.appendChild(paragraph);

            // Guardar los cambios en el archivo
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();

            // Fuente y resultado
            DOMSource source = new DOMSource(doc);
            // Si no existe el archivo se crea
            StreamResult result = new StreamResult(Paths.get( "articulo_mod_ChGPT.xml").toFile());

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);

            System.out.println("Paragraph añadido correctamente en <section id=\""+idSeccion+"\">");

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
    /*


 3.2 Añadir un atributo nuevo

Ejemplo: añadir updated="true" a todas los <paragraph> add atributo textoenriquecido= "true".

*/
    public static void addAtributo(String rutaArchivo, String etiqueta,String atributoNew, String atributoText){

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

            // Editar: NodeList
            // NodeList con todos los nodos <paragraph> del documento
            NodeList lista = doc.getElementsByTagName(etiqueta);


            for(int i = 0; i < lista.getLength(); i++){
                // Cada nodo tenemos que investigar de qué tipo es
                Node node = lista.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;

                    if (element.getNodeName().equals(etiqueta)){
                        element.setAttribute(atributoNew, atributoText);
                    }
                }
            }
            // Escribimos de nuevo el fichero
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource src = new DOMSource(doc);
            StreamResult result = new StreamResult(Paths.get( "articulo_mod_ChGPT.xml").toFile());

            t.transform(src, result);

            System.out.println("El atributo "+atributoNew+" se ha añadido a las etiquetas: "+etiqueta+".");
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
    /*

 3.3 Eliminar nodos

Eliminar todos los <paragraph category="deprecated">.

*/
    public static void delAtributo(String rutaArchivo){

        try {
            // Crear un objeto Path moderno a partir de la ruta proporcionada como parámetro
            Path rutaXML = Path.of(rutaArchivo);

            // Comprobar si el archivo existe; si no, mostrar error y salir del método
            if (!Files.exists(rutaXML)) {
                System.err.println("No se encontró el archivo XML: " + rutaXML.toAbsolutePath());
                return; // Sale del método si el archivo no existe
            }

            // Creamos el builder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(rutaXML.toFile());

            // Normalizar el documento combina nodos de texto adyacentes y limpia espacios
            doc.getDocumentElement().normalize();

            //Acciones
            NodeList lista = doc.getElementsByTagName("paragraph");

            // Iterar por cada nodo <paragraph> encontrado
            for (int i = 0; i < lista.getLength(); i++) {
                Node nodo = lista.item(i); // Obtener el nodo i
                // Comprobar que el nodo es de tipo ELEMENT_NODE (evitar nodos de texto u otros tipos)
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element eSeccion = (Element) nodo; // Convertir a Element para acceder a atributos
                    String categoria = eSeccion.getAttribute("category"); // Leer atributo 'category'

                    if( categoria.equals("deprecated")){
                        Node parent = eSeccion.getParentNode();
                        parent.removeChild(eSeccion);
                    }
                }
            }

            // Escribimos de nuevo el fichero
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource src = new DOMSource(doc);
            StreamResult result = new StreamResult(Paths.get( "articulo_mod_ChGPT.xml").toFile());

            t.transform(src, result);

            //System.out.println("El atributo "+atributoNew+" se ha añadido a las etiquetas: "+etiqueta+".");
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
    /*

 3.4 Modificar atributos

Cambiar el atributo category="consejos" por category="tips".
*/
    public static void changeAtributo(String rutaArchivo){

        try {
            // Crear un objeto Path moderno a partir de la ruta proporcionada como parámetro
            Path rutaXML = Path.of(rutaArchivo);

            // Comprobar si el archivo existe; si no, mostrar error y salir del método
            if (!Files.exists(rutaXML)) {
                System.err.println("No se encontró el archivo XML: " + rutaXML.toAbsolutePath());
                return; // Sale del método si el archivo no existe
            }

            // Creamos el builder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(rutaXML.toFile());

            // Normalizar el documento combina nodos de texto adyacentes y limpia espacios
            doc.getDocumentElement().normalize();

            //Acciones
            NodeList lista = doc.getElementsByTagName("section");

            // Iterar por cada nodo <paragraph> encontrado
            for (int i = 0; i < lista.getLength(); i++) {
                Node nodo = lista.item(i); // Obtener el nodo i
                // Comprobar que el nodo es de tipo ELEMENT_NODE (evitar nodos de texto u otros tipos)
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element eSeccion = (Element) nodo; // Convertir a Element para acceder a atributos
                    String categoria = eSeccion.getAttribute("category"); // Leer atributo 'category'

                    if( categoria.equals("consejos")){
                        eSeccion.setAttribute("category", "tips");
                    }
                }
            }

            // Escribimos de nuevo el fichero
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource src = new DOMSource(doc);
            StreamResult result = new StreamResult(Paths.get( "articulo_mod_ChGPT.xml").toFile());

            t.transform(src, result);

            //System.out.println("El atributo "+atributoNew+" se ha añadido a las etiquetas: "+etiqueta+".");
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
    /*
4. Ejercicios avanzados (nivel experto)
 4.1 Reordenar secciones según un atributo

Ordenar <section> por id, category, etc., y reescribir el XML.
*/
    public static void ordenaroPorAtributo(String rutaArchivo, String categoriaOrdenar){

        try {
            // Crear un objeto Path moderno a partir de la ruta proporcionada como parámetro
            Path rutaXML = Path.of(rutaArchivo);

            // Comprobar si el archivo existe; si no, mostrar error y salir del método
            if (!Files.exists(rutaXML)) {
                System.err.println("No se encontró el archivo XML: " + rutaXML.toAbsolutePath());
                return; // Sale del método si el archivo no existe
            }

            // Creamos el builder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(rutaXML.toFile());

            // Normalizar el documento combina nodos de texto adyacentes y limpia espacios
            doc.getDocumentElement().normalize();

            //Acciones
            NodeList lista = doc.getElementsByTagName("section");

            // Convertir el NodeList en una lista normal
            List<Element> secciones = new ArrayList<>();

            for (int i = 0; i < lista.getLength(); i++) {
                Node nodo = lista.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    secciones.add((Element) nodo);
                }
            }

            // Bubble sort por atributo categoriaOrdenar
            for (int i = 0; i < secciones.size() - 1; i++) {
                for (int j = i + 1; j < secciones.size(); j++) {
                    String id1 = secciones.get(i).getAttribute(categoriaOrdenar);
                    String id2 = secciones.get(j).getAttribute(categoriaOrdenar);

                    //Cambia la posicion
                    if (id1.compareTo(id2) > 0) {
                        // Intercambiar elementos
                        Element temp = secciones.get(i);
                        secciones.set(i, secciones.get(j));
                        secciones.set(j, temp);
                    }
                }
            }

            // Borrar original, si no duplicas informacion
            Element root = doc.getDocumentElement();

            // eliminar todos los nodos <section>
            NodeList lista2 = doc.getElementsByTagName("section");

            while (lista2.getLength() > 0) {
                Node n = lista2.item(0); //Se usa item(0) siempre porque el NodeList se actualiza en tiempo real.
                root.removeChild(n);
            }

            // Añadir <section> con el nuevo orden
            for (Element s : secciones) {
                Node copia = doc.importNode(s, true); // copiar el nodo completo
                root.appendChild(copia);
            }


            // Escribimos de nuevo el fichero
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource src = new DOMSource(doc);
            StreamResult result = new StreamResult(Paths.get( "secciones_ordenadas.xml").toFile());

            t.transform(src, result);

            //System.out.println("El atributo "+atributoNew+" se ha añadido a las etiquetas: "+etiqueta+".");
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
    /*

 4.2 Generar un informe con estadísticas del XML

Crear un nuevo XML o JSON con:

nº de secciones por categoría

año de máximo uso

media de uso

nº total de párrafos

etc.

*/
    public static void generarInforme(String rutaArchivo){

        try {
            // Crear un objeto Path moderno a partir de la ruta proporcionada como parámetro
            Path rutaXML = Path.of(rutaArchivo);

            // Comprobar si el archivo existe; si no, mostrar error y salir del método
            if (!Files.exists(rutaXML)) {
                System.err.println("No se encontró el archivo XML: " + rutaXML.toAbsolutePath());
                return; // Sale del método si el archivo no existe
            }

            // Creamos el builder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(rutaXML.toFile());

            // Normalizar el documento combina nodos de texto adyacentes y limpia espacios
            doc.getDocumentElement().normalize();

            //Acciones
        // Recoger datos:
                //Número de secciones por categoría → usar Map<String, Integer>.
                //Año de máximo uso → recorrer <cell category="uso"> y <cell category="anio"> como antes con un Map.
                //Media de uso → sumar todos los porcentajes y dividir entre la cantidad.
                //Número total de párrafos → recorrer <paragraph> con getElementsByTagName("paragraph") y contar.

            NodeList secciones = doc.getElementsByTagName("section");
            Map<String, Integer> seccionesPorCategoria = new HashMap<>();
            for (int i = 0; i < secciones.getLength(); i++) {
                Element e = (Element) secciones.item(i);
                String cat = e.getAttribute("category");
                seccionesPorCategoria.put(cat, seccionesPorCategoria.getOrDefault(cat, 0) + 1);
            }

            // Para uso y año
            NodeList cells = doc.getElementsByTagName("cell");
            Map<String, Integer> usoPorAyo = new HashMap<>();
            String claveTemporal = null;
            for (int i = 0; i < cells.getLength(); i++) {
                Element c = (Element) cells.item(i);
                String cat = c.getAttribute("category");
                if (cat.equals("anio")) claveTemporal = c.getTextContent();
                else if (cat.equals("uso") && claveTemporal != null) {
                    String valor = c.getTextContent().replace("%", "");
                    usoPorAyo.put(claveTemporal, Integer.parseInt(valor));
                }
            }

            // Número total de párrafos
            NodeList parrafos = doc.getElementsByTagName("paragraph");
            int totalParrafos = parrafos.getLength();


        //CALCULAR
            int maxUso = 0;
            String anioMaxUso = null;
            int sumaUso = 0;

            for (Map.Entry<String, Integer> entry : usoPorAyo.entrySet()) {
                int val = entry.getValue();
                sumaUso += val;
                if (val > maxUso) {
                    maxUso = val;
                    anioMaxUso = entry.getKey();
                }
            }

            double mediaUso = sumaUso / (double) usoPorAyo.size();

        //Nuevo XML
            Document informe = builder.newDocument();
            Element root = informe.createElement("informe");
            informe.appendChild(root);

// Secciones por categoría
            Element seccionesElem = informe.createElement("secciones");
            root.appendChild(seccionesElem);
            for (Map.Entry<String, Integer> entry : seccionesPorCategoria.entrySet()) {
                Element catElem = informe.createElement("categoria");
                catElem.setAttribute("name", entry.getKey());
                catElem.setTextContent(entry.getValue().toString());
                seccionesElem.appendChild(catElem);
            }

// Uso
            Element usoElem = informe.createElement("uso");
            Element anioMax = informe.createElement("anioMaximo");
            anioMax.setTextContent(anioMaxUso);
            Element media = informe.createElement("mediaUso");
            media.setTextContent(String.format("%.2f", mediaUso));
            usoElem.appendChild(anioMax);
            usoElem.appendChild(media);
            root.appendChild(usoElem);

// Total párrafos
            Element parrafosElem = informe.createElement("parrafos");
            parrafosElem.setTextContent(String.valueOf(totalParrafos));
            root.appendChild(parrafosElem);

            // Escribimos de nuevo el fichero
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource src = new DOMSource(informe);
            StreamResult result = new StreamResult(Paths.get( "estadisticas.xml").toFile());

            t.transform(src, result);

            //System.out.println("El atributo "+atributoNew+" se ha añadido a las etiquetas: "+etiqueta+".");
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
    /*

 4.3 Fusionar dos XML

Dado un segundo archivo XML con más <section>, unirlos en uno solo si tienen el mismo id, actualizando contenidos.

*/
    public static void fusionarXML(String rutaArchivo, String rutaArchivoAFusionar){

        try {
            // Crear un objeto Path moderno a partir de la ruta proporcionada como parámetro
            Path rutaXML = Path.of(rutaArchivo);
            Path rutaAFusionar = Path.of(rutaArchivoAFusionar);

            // Comprobar si el archivo existe; si no, mostrar error y salir del método
            if (!Files.exists(rutaXML)) {
                System.err.println("No se encontró el archivo XML: " + rutaXML.toAbsolutePath());
                return; // Sale del método si el archivo no existe
            }

            // Comprobar si el archivo existe; si no, mostrar error y salir del método
            if (!Files.exists(rutaAFusionar)) {
                System.err.println("No se encontró el archivo XML: " + rutaXML.toAbsolutePath());
                return; // Sale del método si el archivo no existe
            }

            // Creamos el builder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document docPrincipal  = builder.parse(rutaXML.toFile());
            Document docAFusionar  = builder.parse(rutaAFusionar.toFile());

            // Normalizar el documento combina nodos de texto adyacentes y limpia espacios
            docPrincipal.getDocumentElement().normalize();
            docAFusionar.getDocumentElement().normalize();

            //Acciones
            // Obtenemos la raíz del documento principal
            Element raizPrincipal = docPrincipal.getDocumentElement();

            // Obtenemos la raíz del documento que vamos a fusionar
            Element raizAFusionar = docAFusionar.getDocumentElement();

            // Obtenemos todas las secciones (<section>) del documento a fusionar
            NodeList seccionesAFusionar = raizAFusionar.getElementsByTagName("section");

            // Recorremos cada sección del XML a fusionar
            for (int i = 0; i < seccionesAFusionar.getLength(); i++) {
                Element secFusion = (Element) seccionesAFusionar.item(i); // sección actual a fusionar
                String secId = secFusion.getAttribute("id"); // obtenemos el atributo "id" de la sección
                boolean encontrado = false; // bandera para indicar si encontramos una sección con el mismo id en el XML principal

                // Obtenemos los hijos directos de la raíz del XML principal
                NodeList hijosDirectos = raizPrincipal.getChildNodes();

                // Recorremos los hijos directos para buscar si ya existe una sección con el mismo id
                for (int j = 0; j < hijosDirectos.getLength(); j++) {
                    Node nodo = hijosDirectos.item(j); // nodo actual de la raíz principal

                    // Verificamos que sea un elemento y que sea una <section>
                    if (nodo.getNodeType() == Node.ELEMENT_NODE && nodo.getNodeName().equals("section")) {
                        Element secPrincipal = (Element) nodo;

                        // Si el id de la sección principal coincide con el id de la sección a fusionar
                        if (secPrincipal.getAttribute("id").equals(secId)) {
                            // Obtenemos el nodo padre de la sección a reemplazar
                            Node parent = secPrincipal.getParentNode();

                            // Eliminamos la sección antigua del documento principal
                            parent.removeChild(secPrincipal);

                            // Importamos la sección del documento a fusionar al documento principal
                            Node nuevaSec = docPrincipal.importNode(secFusion, true);

                            // Añadimos la nueva sección al mismo nivel que la anterior
                            parent.appendChild(nuevaSec);

                            // Marcamos que encontramos una sección con el mismo id
                            encontrado = true;
                        }
                    }
                }

                // Si no se encontró ninguna sección con el mismo id, simplemente añadimos la nueva sección al final
                if (!encontrado) {
                    Node nuevaSec = docPrincipal.importNode(secFusion, true); // importamos la sección completa
                    raizPrincipal.appendChild(nuevaSec); // la añadimos al final de la raíz del documento principal
                }
            }

            // Guardar el XML fusionado en el archivo de salida
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource src = new DOMSource(docPrincipal);
            StreamResult result = new StreamResult(Paths.get( "articulo_fusionado.xml").toFile());
            t.transform(src, result);

            System.out.println("Fusión completada. Archivo guardado en: " + "articulo_fusionado.xml");
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
5. Ejercicios realistas de examen

5.1 Crear un metodo que:

devuelva el texto del primer <paragraph category="consejo"> o indique que no existe

*/
    public static String texto1Paragraph(String rutaArchivo){
        String texto = null;

        try {
            // Crear un objeto Path moderno a partir de la ruta proporcionada como parámetro
            Path rutaXML = Path.of(rutaArchivo);


            // Comprobar si el archivo existe; si no, mostrar error y salir del metodo
            if (!Files.exists(rutaXML)) {
                System.err.println("No se encontró el archivo XML: " + rutaXML.toAbsolutePath());
                return null; // Sale del metodo si el archivo no existe
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(rutaXML.toFile());

            // Normalizar el documento combina nodos de texto adyacentes y limpia espacios
            doc.getDocumentElement().normalize();

            // OBTENER TODAS LOS paragraph
            // NodeList con todos los nodos <paragraph> del documento
            NodeList secciones = doc.getElementsByTagName("paragraph");

            Node nodo = secciones.item(0); // Obtener el nodo 0, el primero
            // Comprobar que el nodo es de tipo ELEMENT_NODE (evitar nodos de texto u otros tipos)
            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                Element eSeccion = (Element) nodo; // Convertir a Element para acceder a atributos'
                texto = eSeccion.getTextContent(); // Asigna el contenido de la etiqueta a texto
            }

            System.out.println("1er Paragraph texto: " + texto);

        } catch (ParserConfigurationException e) {
            System.err.println("Error configurando el parser XML: " + e.getMessage());
            e.printStackTrace();
        } catch (SAXException e) {
            System.err.println("Error al parsear el XML: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error de E/S con el archivo: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            return texto;
        }
    }
    /*

5.2 Validar el XML (sin XSD)
Comprobar manualmente que todas las secciones tienen id.

*/
    public static void validacion(String rutaArchivo){
        boolean todasID = true;
        try {
            // Crear un objeto Path moderno a partir de la ruta proporcionada como parámetro
            Path rutaXML = Path.of(rutaArchivo);

            // Comprobar si el archivo existe; si no, mostrar error y salir del metodo
            if (!Files.exists(rutaXML)) {
                System.err.println("No se encontró el archivo XML: " + rutaXML.toAbsolutePath());
                return ; // Sale del metodo si el archivo no existe
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(rutaXML.toFile());

            // Normalizar el documento combina nodos de texto adyacentes y limpia espacios
            doc.getDocumentElement().normalize();

            // Acciones

            // Obtener todas las secciones <section>
            NodeList secciones = doc.getElementsByTagName("section");

            // Recorrer las secciones
            for (int i = 0; i < secciones.getLength(); i++) {
                Node nodo = secciones.item(i);

                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element eSeccion = (Element) nodo;

                    // Comprobar si el atributo 'id' está vacío
                    if ((eSeccion.getAttribute("id")).isEmpty()){
                        System.out.println("Sección sin id encontrada en posición " + i);
                        todasID = false;
                    }
                }
            }

        } catch (ParserConfigurationException e) {
            System.err.println("Error configurando el parser XML: " + e.getMessage());
            e.printStackTrace();
        } catch (SAXException e) {
            System.err.println("Error al parsear el XML: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error de E/S con el archivo: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(todasID){
                System.out.println("Todas las <section> tienen id");
            }else{
                System.out.println("No todas las <section> tienen id");
            }
        }

    }
    /*

5.3 Crear un menú de consola
Con opciones como:

Listar secciones

Añadir párrafo

Mostrar año de más uso

Guardar XML con otro nombre
    *
    * */

    public static void menu(String rutaArchivo){
        Scanner sc = new Scanner(System.in);

        boolean salir = false;

        while (!salir) {
            System.out.println("\n--- MENÚ XML ---");
            System.out.println("1. Listar secciones");
            System.out.println("2. Añadir párrafo a una sección");
            System.out.println("3. Mostrar año de más uso");
            System.out.println("4. Guardar XML con otro nombre");
            System.out.println("5. Salir");
            System.out.print("Elige una opción: ");
            int opcion = sc.nextInt();
            sc.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1:
                    //listarSecciones(doc);
                    break;
                case 2:
                    System.out.print("Introduce el id de la sección: ");
                    String secId = sc.nextLine();
                    System.out.print("Introduce el texto del párrafo: ");
                    String texto = sc.nextLine();
                    //añadirParrafo(doc, secId, texto);
                    break;
                case 3:
                    //mostrarAyoMasUso(doc);
                    break;
                case 4:
                    System.out.print("Introduce el nombre del archivo de salida: ");
                    String nombreSalida = sc.nextLine();
                    //guardarXML(doc, nombreSalida);
                    break;
                case 5:
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
        sc.close();
    }

    public static void main(String[] args) {
        String rutaActual = System.getProperty("user.dir");
        System.out.println("Directorio de ejecucion: " + rutaActual);
        String ruta = "articulo.xml";

        //categoriasSeccion("articulo.xml");
        //seccionesXCategoria("articulo.xml");
        //textoParagraph("articulo.xml");
        //buscarAyo("articulo.xml");
        //mediaTotalUso("articulo.xml");
        //String texto = "EL nuevo texto add al articulo";
        //insertarParagraph("articulo.xml","section","sec2","texto",texto);
        //addAtributo("articulo.xml","paragraph","txtEnriquecido", "true");
        //delAtributo("articulo_mod_ChGPT.xml");
        //addConsejo("articulo.xml");
        //changeAtributo("articulo.xml");
        //ordenaroPorAtributo("articulo.xml", "category");
        //generarInforme("articulo.xml");
        //fusionarXML("articulo.xml","aFusionar.xml");

        //Ejercicio 5
        texto1Paragraph(ruta);
        validacion(ruta);
        menu(ruta);

    }

}
