/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import java.io.PrintWriter;

/**
 *
 * @author carlos
 */
/**
 * Asumes codificación UTF-8. JDK 7+.
 */
public class CrearXMLScanner {

    /**
     * Constructor. @param aFileName nombre completo de un fichero existente y
     * legible.
     */
    public CrearXMLScanner(String aFileName) {
        fFilePath = Paths.get(aFileName);
        try {
            writer = new PrintWriter(aFileName + ".xml", "UTF-8");
            //writer = new PrintWriter(new FileWriter("/home/carlos/Escritorio/creado.xml", true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método plantilla que se llama {@link #processLine(String)}.
     */
    public final void processLineByLine() throws IOException {
        writer.println("<add>");
        try (Scanner scanner = new Scanner(fFilePath, ENCODING.name())) {
            while (scanner.hasNextLine()) {
                //processLine(scanner.nextLine());
                crearArchivo(scanner.nextLine());
            }
        }
        writer.println("</add>");
        writer.close();
    }

    /**
     * Método sobrecargado para procesar las lineas de texto de diferentes
     * maneras.
     * <P>
     * Esta implementación simple por defecto espera valores simples por pares,
     * separados por un signo '='. Ejemplos de entradas válidas serían:
     * <tt>altura = 167cm</tt> <tt>peso = 65kg</tt> <tt>caracter = "gruñon"</tt>
     * <tt>este es el nombre = este es el valor</tt>
     */
    protected void processLine(String aLine) {
        //usa un segundo scanner para parsear el contenido de cada línea
        Scanner scanner = new Scanner(aLine);
        scanner.useDelimiter("=");
        if (scanner.hasNext()) {
            //assumes the line has a certain structure       
            String name = scanner.next();
            String value = scanner.next();
            log("Name is : " + quote(name.trim()) + ", and Value is : " + quote(value.trim()));
        } else {
            log("Empty or invalid line. Unable to process.");
        }
    }

    public void crearArchivo(String aLine) {
        Scanner scanner = new Scanner(aLine);
        //scanner.useDelimiter("\\W");
        if (scanner.hasNext()) {
            //assumes the line has a certain structure       
            String name = scanner.nextLine();
            //String aux = "aux";
            //String value = scanner.next();
            if (name.indexOf("Document") != -1) {
                String subcadena = name.substring(8);
                writer.print("<doc>\n  <field name=\"id\">" + subcadena.replace(" ", "") + "</field>\n  <field name=\"title\">");
            } else if (name.equals("********************************************")) {
                writer.print("</field>\n</doc>\n");
            } else if (name.indexOf("&") != -1) {
                writer.print(name.replace("&", "&amp;"));
            } else {
                writer.print(name);// + "++++");// + "=" + value);
            }

            //log("Name is : " + quote(name.trim()) + ", and Value is : " + quote(value.trim()));
        } else {
            //log("Empty or invalid line. Unable to process.");
            writer.print("</field>\n  <field name=\"text\">");
        }
    }

    // PRIVATE   
    private final Path fFilePath;
    private final static Charset ENCODING = StandardCharsets.UTF_8;
    private PrintWriter writer;

    public static void log(Object aObject) {
        System.out.println(String.valueOf(aObject));
    }

    private String quote(String aText) {
        String QUOTE = "'";
        return QUOTE + aText + QUOTE;
    }

}
