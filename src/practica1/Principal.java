/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.solr.client.solrj.SolrServerException;
import org.xml.sax.SAXException;

/**
 *
 * @author carlos
 */
public class Principal {

    public static void main(String[] args) throws IOException, SolrServerException, ParserConfigurationException, SAXException {
        String rutaCorpus = "/home/carlos/Escritorio/LISA/";//ESCRIBIR LA RUTA DONDE SE ENCUENTRA EL CORPUS
        Menu menu = new Menu();
        menu.menu_principal(rutaCorpus);
    }
}
