/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.apache.solr.client.solrj.SolrQuery;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

/**
 *
 * @author carlos
 */
public class Consultas {

    private String rutaCorpus;
    private final Path fFilePath;
    private final static Charset ENCODING = StandardCharsets.UTF_8;
    private PrintWriter writer;

    public Consultas(String rutaCorpus) {
        this.rutaCorpus = rutaCorpus;
        fFilePath = Paths.get(rutaCorpus + "LISA.QUE");
        try {
            writer = new PrintWriter(rutaCorpus + "trec_top_file.txt", "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                            //query.setRows(10);
                            String aux = busqueda.replace(":", ""); //Elimina los dos puntos : que dan problemas en las consultas de solr

                            aux = aux.replace(".", ""); //Elimina los signos de puntuacion
                            aux = aux.replace(",", "");
                            aux = aux.replace("(", "");
                            aux = aux.replace(")", "");

                            aux = Arrays.stream(aux.split("\\s+")).distinct().collect(Collectors.joining(" ")); //Elimina las palabras repetidas

                            List<String> allWords = new ArrayList<>(Arrays.asList(aux.toLowerCase().split(" "))); //Convierte el string en un arraylist para despues borrar las stopwords con removeAll
                            List<String> stopWords = Arrays.asList("a", "about", "above", "across", "after", "again",
                                    "against", "all", "almost", "alone", "along", "already", "also",
                                    "although", "always", "among", "an", "and", "another", "any",
                                    "anybody", "anyone", "anything", "anywhere", "are", "area",
                                    "areas", "around", "as", "ask", "asked", "asking", "asks", "at",
                                    "away", "b", "back", "backed", "backing", "backs", "be", "became",
                                    "because", "become", "becomes", "been", "before", "began",
                                    "behind", "being", "beings", "best", "better", "between", "big",
                                    "both", "but", "by", "c", "came", "can", "cannot", "case", "cases",
                                    "certain", "certainly", "clear", "clearly", "come", "could", "d",
                                    "did", "differ", "different", "differently", "do", "does", "done",
                                    "down", "down", "downed", "downing", "downs", "during", "e",
                                    "each", "early", "either", "end", "ended", "ending", "ends",
                                    "enough", "even", "evenly", "ever", "every", "everybody",
                                    "everyone", "everything", "everywhere", "f", "face", "faces",
                                    "fact", "facts", "far", "felt", "few", "find", "finds", "first",
                                    "for", "four", "from", "full", "fully", "further", "furthered",
                                    "furthering", "furthers", "g", "gave", "general", "generally",
                                    "get", "gets", "give", "given", "gives", "go", "going", "good",
                                    "goods", "got", "great", "greater", "greatest", "group", "grouped",
                                    "grouping", "groups", "h", "had", "has", "have", "having", "he",
                                    "her", "here", "herself", "high", "high", "high", "higher",
                                    "highest", "him", "himself", "his", "how", "however", "i", "if",
                                    "important", "in", "interest", "interested", "interesting",
                                    "interests", "into", "is", "it", "its", "itself", "j", "just", "k",
                                    "keep", "keeps", "kind", "knew", "know", "known", "knows", "l",
                                    "large", "largely", "last", "later", "latest", "least", "less",
                                    "let", "lets", "like", "likely", "long", "longer", "longest", "m",
                                    "made", "make", "making", "man", "many", "may", "me", "member",
                                    "members", "men", "might", "more", "most", "mostly", "mr", "mrs",
                                    "much", "must", "my", "myself", "n", "necessary", "need", "needed",
                                    "needing", "needs", "never", "new", "new", "newer", "newest",
                                    "next", "no", "nobody", "non", "noone", "not", "nothing", "now",
                                    "nowhere", "number", "numbers", "o", "of", "off", "often", "old",
                                    "older", "oldest", "on", "once", "one", "only", "open", "opened",
                                    "opening", "opens", "or", "order", "ordered", "ordering", "orders",
                                    "other", "others", "our", "out", "over", "p", "part", "parted",
                                    "parting", "parts", "per", "perhaps", "place", "places", "point",
                                    "pointed", "pointing", "points", "possible", "present",
                                    "presented", "presenting", "presents", "problem", "problems",
                                    "put", "puts", "q", "quite", "r", "rather", "really", "right",
                                    "right", "room", "rooms", "s", "said", "same", "saw", "say",
                                    "says", "second", "seconds", "see", "seem", "seemed", "seeming",
                                    "seems", "sees", "several", "shall", "she", "should", "show",
                                    "showed", "showing", "shows", "side", "sides", "since", "small",
                                    "smaller", "smallest", "so", "some", "somebody", "someone",
                                    "something", "somewhere", "state", "states", "still", "still",
                                    "such", "sure", "t", "take", "taken", "than", "that", "the",
                                    "their", "them", "then", "there", "therefore", "these", "they",
                                    "thing", "things", "think", "thinks", "this", "those", "though",
                                    "thought", "thoughts", "three", "through", "thus", "to", "today",
                                    "together", "too", "took", "toward", "turn", "turned", "turning",
                                    "turns", "two", "u", "under", "until", "up", "upon", "us", "use",
                                    "used", "uses", "v", "very", "w", "want", "wanted", "wanting",
                                    "wants", "was", "way", "ways", "we", "well", "wells", "went",
                                    "were", "what", "when", "where", "whether", "which", "while",
                                    "who", "whole", "whose", "why", "will", "with", "within",
                                    "without", "work", "worked", "working", "works", "would", "x", "y",
                                    "year", "years", "yet", "you", "young", "younger", "youngest",
                                    "your", "yours", "z", ".", ",", "#", "am", "pleased", "receive", "information",
                                    "identification", "evaluation", "instance", "increased", "dissertation", "novel");

                            allWords.removeAll(stopWords);

                            String result = String.join(" ", allWords); //String final con la query
                            //System.out.println(result);

                            query.setQuery("title:" + result + " OR text:" + result);
                            QueryResponse rsp = solr.query(query);
                            SolrDocumentList docs = rsp.getResults();
                            respuestas.add(docs);
                            System.out.println("QUERY " + a + ": " + result);
                            a++;
                            busqueda = "";
                        }
                    }
                }
            }
        }

        a = 1;

        for (int i = 0; i < respuestas.size(); i++) {
            if (respuestas.get(i).getNumFound() > 0) {//SI HAY RESPUESTAS A LA QUERY ENTONCES
                //System.out.println("numfound: " + respuestas.get(i).getNumFound());
                for (int j = 0; j < 8; j++) {
                    writer.println(a + " " + "Q" + 0 + " " + respuestas.get(i).get(j).getFieldValue("id") + " " + j + " " + respuestas.get(i).get(j).getFieldValue("score") + " ETSI");
                }
            }
            System.out.println("RESPUESTA " + a + ": " + respuestas.get(i));
            a++;
        }
        writer.close();
        System.out.println("\n\nCreado el fichero trec_top_file.txt");
    }
}
