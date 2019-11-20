/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

/**
 *
 * @author carlos
 */
public class LeerXMLSax {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, SolrServerException {

        HttpSolrClient solr = new HttpSolrClient.Builder("http://localhost:8983/solr/gettingstarted").build();

        File folder = new File("/home/carlos/Escritorio/LISA/");
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().matches("^LISA\\d.*.xml")) {
                //System.out.println(file.getName());
                /*
                CrearXMLScanner parser = new CrearXMLScanner("/home/carlos/Escritorio/LISA/" + file.getName());
                parser.processLineByLine();
                parser.log("Done.");*/
                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                SAXParser saxParser = saxParserFactory.newSAXParser();
                LeerXMLHandler handler = new LeerXMLHandler();
                saxParser.parse(file, handler);

                ArrayList<Documento> documentos = handler.getDocumentos();

                for (int i = 0; i < documentos.size(); i++) {
                    //System.out.println(documentos.get(i));
                    //Create solr document
                    SolrInputDocument doc = new SolrInputDocument();
                    doc.addField("id", documentos.get(i).getId());
                    doc.addField("title", documentos.get(i).getTitle());
                    doc.addField("text", documentos.get(i).getText());
                    solr.add(doc);
                    solr.commit();
                }
                
                System.out.println("*******TERMINADO " + file.getName() + "*************");

            }
        }
    }

}
