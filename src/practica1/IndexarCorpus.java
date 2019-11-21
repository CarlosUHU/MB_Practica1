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
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author carlos
 */
//LECTURA DE LOS DOCUMENTOS XML Y SU INDEXACION EN SOLR
//LA LECTURA DE LOS DOCUMENTOS XML SE HA HECHO CON LA LIBRERIA DE JAVA SAX
//SAX AYUDA A LEER GRANDES VOLUMENES DE DATOS USANDO POCA MEMORIA
public class IndexarCorpus {

    private String rutaCorpus;

    public IndexarCorpus(String rutaCorpus) {
        this.rutaCorpus = rutaCorpus;
    }

    public void Indexar() throws ParserConfigurationException, SAXException, IOException, SolrServerException {
        HttpSolrClient solr = new HttpSolrClient.Builder("http://localhost:8983/solr/gettingstarted").build();

        File folder = new File(rutaCorpus);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().matches("^LISA\\d.*.xml")) {
                //System.out.println(file.getName());
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

    //HANDLER NECESARIO PARA LA LECTURA XML CON SAX
    public class LeerXMLHandler extends DefaultHandler {

        private ArrayList<Documento> documentos = new ArrayList();
        private Documento documento;
        private StringBuilder buffer = new StringBuilder();
        private String nombreAtributo;

        public ArrayList<Documento> getDocumentos() {
            return documentos;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            buffer.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            switch (qName) {
                case "add":
                    break;
                case "doc":
                    break;
                case "field":
                    if (nombreAtributo.equals("id")) {
                        documento.setId(Integer.parseInt(buffer.toString()));
                    } else if (nombreAtributo.equals("title")) {
                        documento.setTitle(buffer.toString());
                    } else {
                        documento.setText(buffer.toString());
                    }
                    break;
            }
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            switch (qName) {
                case "add":
                    break;
                case "doc":
                    documento = new Documento();
                    documentos.add(documento);
                    break;
                case "field":
                    buffer.delete(0, buffer.length());
                    nombreAtributo = attributes.getValue("name");
                    break;
            }
        }
    }
}
