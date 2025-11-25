/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UT1;

import java.io.*;       // Importa todas las clases de java.io (FileReader, FileWriter, Streams, etc.)
import java.io.IOException; // Importa IOException para manejar errores de E/S
import java.nio.file.Path;  // Importa Path de NIO para manejar rutas de archivos

/**
 * Clase que contiene métodos para manejar ficheros binarios y de texto,
 * tanto con buffer como sin buffer, usando Path de NIO.
 */
public class tratarDatos {

    // FICHEROS BINARIOS SIN BUFFER
    public void BinariosSinBuffer() {
        try {
            // --- ESCRITURA BINARIA SIN BUFFER ---
            Path pathBin = Path.of("datos.bin"); // Creamos un Path para el archivo binario
            FileOutputStream fos = new FileOutputStream(pathBin.toFile()); // Convertimos Path a File y creamos FileOutputStream
            DataOutputStream dos = new DataOutputStream(fos); // DataOutputStream permite escribir tipos primitivos

            // Escribimos datos primitivos en el archivo
            dos.writeInt(42);           // Escribe un entero
            dos.writeDouble(3.1416);    // Escribe un double
            dos.writeBoolean(true);     // Escribe un booleano
            dos.writeUTF("Hola Mundo"); // Escribe un String en UTF-8
            dos.flush();                // Fuerza a escribir los datos en el archivo

            System.out.println("Datos escritos correctamente.");
            dos.close(); // Cerramos el DataOutputStream

            // --- LECTURA BINARIA SIN BUFFER ---
            FileInputStream fis = new FileInputStream(pathBin.toFile()); // Abrimos el archivo para lectura
            DataInputStream dis = new DataInputStream(fis); // DataInputStream permite leer datos primitivos

            // Leemos los datos en el mismo orden que se escribieron
            int num = dis.readInt();         // Lee un entero
            double pi = dis.readDouble();    // Lee un double
            boolean flag = dis.readBoolean();// Lee un booleano
            String texto = dis.readUTF();    // Lee un String UTF-8

            System.out.println("Leído: " + num + ", " + pi + ", " + flag + ", " + texto);
            dis.close(); // Cerramos el DataInputStream

        } catch (IOException e) { // Captura cualquier error de E/S
            e.printStackTrace(); // Muestra información detallada del error
        }
    }

    // FICHEROS BINARIOS CON BUFFER
    public void BinariosConBuffer() {
        try {
            // --- ESCRITURA CON BUFFER ---
            Path pathBuf = Path.of("datos_buffer.bin"); // Path para archivo con buffer
            DataOutputStream dos = new DataOutputStream(
                    new BufferedOutputStream(new FileOutputStream(pathBuf.toFile()))
            ); // BufferedOutputStream mejora la eficiencia al escribir

            dos.writeByte(10);                 // Escribe un byte
            dos.writeDouble(6.28);             // Escribe un double
            dos.writeUTF("Buffered OutputStream"); // Escribe un String UTF-8
            dos.flush();                        // Guarda los datos del buffer
            dos.close();                        // Cerramos el flujo

            // --- LECTURA CON BUFFER ---
            DataInputStream dis = new DataInputStream(
                    new BufferedInputStream(new FileInputStream(pathBuf.toFile()))
            ); // BufferedInputStream mejora la eficiencia al leer

            byte b = dis.readByte();           // Lee un byte
            double d = dis.readDouble();       // Lee un double
            String s = dis.readUTF();          // Lee un String UTF-8
            dis.close();                        // Cerramos el flujo

            System.out.println("Leído desde buffer: " + b + ", " + d + ", " + s);

        } catch (IOException e) { // Captura errores de E/S
            e.printStackTrace(); // Muestra información detallada del error
        }
    }

    // FICHEROS DE TEXTO (sin buffer)
    public void TextoSinBuffer() {
        try {
            // --- ESCRITURA ---
            Path pathTxt = Path.of("texto.txt"); // Path para archivo de texto
            FileWriter fw = new FileWriter(pathTxt.toFile()); // FileWriter permite escribir caracteres
            fw.write("Primera línea\n");       // Escribimos la primera línea
            fw.append("Segunda línea\n");      // Añadimos la segunda línea
            fw.flush();                         // Guardamos los cambios
            fw.close();                         // Cerramos el FileWriter

            // --- LECTURA ---
            FileReader fr = new FileReader(pathTxt.toFile()); // FileReader permite leer caracteres
            int c; // Variable para almacenar cada carácter
            System.out.println("Contenido del archivo:");

            // Leemos caracter a caracter hasta el final del archivo (-1)
            while ((c = fr.read()) != -1) {
                System.out.print((char) c); // Convertimos el int a char y mostramos
            }
            fr.close(); // Cerramos el FileReader

        } catch (IOException e) { // Captura errores de E/S
            e.printStackTrace(); // Muestra información detallada del error
        }
    }

    // FICHEROS DE TEXTO (con buffer)
    public void TextoConBuffer() {
        try {
            // --- ESCRITURA CON BUFFER ---
            Path pathTxtBuf = Path.of("texto_buffer.txt"); // Path para archivo de texto con buffer
            BufferedWriter bw = new BufferedWriter(new FileWriter(pathTxtBuf.toFile())); // BufferedWriter permite escribir líneas

            bw.write("Línea 1"); // Escribimos primera línea
            bw.newLine();        // Salto de línea
            bw.write("Línea 2"); // Escribimos segunda línea
            bw.flush();          // Guardamos los cambios
            bw.close();          // Cerramos el BufferedWriter

            // --- LECTURA CON BUFFER ---
            BufferedReader br = new BufferedReader(new FileReader(pathTxtBuf.toFile())); // BufferedReader permite leer líneas completas
            String linea; // Variable para almacenar cada línea
            System.out.println("Leyendo con BufferedReader:");
            while ((linea = br.readLine()) != null) { // Leemos hasta el final del archivo
                System.out.println(linea);           // Mostramos cada línea
            }
            br.close(); // Cerramos el BufferedReader

        } catch (IOException e) { // Captura errores de E/S
            e.printStackTrace(); // Muestra información detallada del error
        }

    }

}
