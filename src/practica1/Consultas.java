/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.solr.client.solrj.SolrQuery;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

/**
 *
 * @author usuario
 */
public class Consultas {

    private String rutaCorpus;
    private final Path fFilePath;
    private final static Charset ENCODING = StandardCharsets.UTF_8;

    public Consultas(String rutaCorpus) {
        this.rutaCorpus = rutaCorpus;
        fFilePath = Paths.get(rutaCorpus + "LISA.QUE");
    }

    public void Consultar() throws IOException, SolrServerException {

        HttpSolrClient solr = new HttpSolrClient.Builder("http://localhost:8983/solr/gettingstarted").build();

        ArrayList<SolrDocumentList> respuestas = new ArrayList();
        String busqueda = "";
        int a = 1;

        //BUCLE Y PARSEO DE QUERIES.PARA CADA QUERY:
        try (Scanner scanner = new Scanner(fFilePath, ENCODING.name())) {
            while (scanner.hasNextLine()) {
                Scanner scanner2 = new Scanner(scanner.nextLine());
                if (scanner2.hasNext()) {
                    //assumes the line has a certain structure       
                    String name = scanner2.nextLine();
                    if (!name.matches("[0-9]+")) {
                        busqueda = busqueda + name + " ";
                        if (name.contains("#")) {
                            SolrQuery query = new SolrQuery();
                            query.setFields("id", "score");
                            //query.setRows(200);
                            String aux = busqueda.replace(":", "");
                            query.setQuery("title:" + aux + " OR text:" + aux);
                            QueryResponse rsp = solr.query(query);
                            SolrDocumentList docs = rsp.getResults();
                            respuestas.add(docs);
                            System.out.println("QUERY " + a + ": " + busqueda);
                            a++;
                            busqueda = "";
                        }
                    }
                }
            }
        }

        a = 1;

        for (int i = 0; i < respuestas.size(); i++) {
            System.out.println("RESPUESTA " + a + ": " + respuestas.get(i));
            a++;
        }
    }
}
