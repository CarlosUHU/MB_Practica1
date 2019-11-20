/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1;

import java.io.IOException;
import org.apache.solr.client.solrj.SolrQuery;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import org.apache.solr.common.SolrInputDocument;

/**
 *
 * @author usuario
 */
public class Consultas {

    public static void main(String[] args)
            throws IOException, SolrServerException {

        HttpSolrClient solr = new HttpSolrClient.Builder("http://localhost:8983/solr/gettingstarted").build();

        
        //BUCLE Y PARSEO DE QUERIES.PARA CADA QUERY:
        
        
        SolrQuery query = new SolrQuery();
        query.setQuery("*");
        

        //...
        
        //query.setFields("id","score");
        //query.setRows(500);
        QueryResponse rsp = solr.query(query);
        SolrDocumentList docs = rsp.getResults();
        for (int i = 0; i < docs.size(); ++i) {
            System.out.println(docs.get(i));
        }

    }

}