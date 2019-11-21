/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 *
 * @author carlos
 */
//PARSEO DEL CORPUS CON LA CLASE SCANNER Y CREACION DE DOCUMENTOS XML QUE SERAN LEIDOS POR SOLR
public class ParsearCorpus {

    private String rutaCorpus;
    private PrintWriter writer;
    private final Path fFilePath;
    private final static Charset ENCODING = StandardCharsets.UTF_8;

    public ParsearCorpus(String rutaCorpus) {
        this.rutaCorpus = rutaCorpus;
        fFilePath = Paths.get(rutaCorpus);
        try {
            writer = new PrintWriter(rutaCorpus + ".xml", "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Parseo() throws IOException {
        File folder = new File(rutaCorpus);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().matches("^LISA\\d.*")) {
                //System.out.println(file.getName());
                ParsearCorpus parser = new ParsearCorpus(rutaCorpus + file.getName());
                parser.processLineByLine(rutaCorpus);
                parser.log("Done: " + file.getName());
            }
        }
    }

    public final void processLineByLine(String rutaCorpus) throws IOException {
        writer.println("<add>");
        try (Scanner scanner = new Scanner(fFilePath, ENCODING.name())) {
            while (scanner.hasNextLine()) {
                processLine(scanner.nextLine());
            }
        }
        writer.println("</add>");
        writer.close();
    }

    protected void processLine(String aLine) {
        Scanner scanner = new Scanner(aLine);
        if (scanner.hasNext()) {
            String name = scanner.nextLine();
            if (name.indexOf("Document") != -1) {//Linea Documento id
                String subcadena = name.substring(8);
                writer.print("<doc>\n  <field name=\"id\">" + subcadena.replace(" ", "") + "</field>\n  <field name=\"title\">");
            } else if (name.equals("********************************************")) {//Linea *************
                writer.print("</field>\n</doc>\n");
            } else if (name.indexOf("&") != -1) {//Transforma el caracter & al formato de solr
                writer.print(name.replace("&", "&amp;"));
            } else {//Linea de texto
                writer.print(name);
            }
            //log("Name is : " + quote(name.trim()));
        } else {//Linea vacia
            writer.print("</field>\n  <field name=\"text\">");
        }
    }

    private static void log(Object aObject) {
        System.out.println(String.valueOf(aObject));
    }

    private String quote(String aText) {
        String QUOTE = "'";
        return QUOTE + aText + QUOTE;
    }

}
