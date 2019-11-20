/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1;

import java.io.File;
import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;

/**
 *
 * @author carlos
 */
public class CrearXML {

    public static void main(String[] args) throws IOException,
            SolrServerException {

        File folder = new File("/home/carlos/Escritorio/LISA/");
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().matches("^LISA\\d.*")) {
                //System.out.println(file.getName());
                CrearXMLScanner parser = new CrearXMLScanner("/home/carlos/Escritorio/LISA/" + file.getName());
                parser.processLineByLine();
                parser.log("Done: " + file.getName());
            }
        }

    }

}
