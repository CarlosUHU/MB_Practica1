/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.solr.client.solrj.SolrServerException;
import org.xml.sax.SAXException;

/**
 *
 * @author carlos
 */
public class Menu {

    public void menu_principal(String rutaCorpus) throws IOException, SolrServerException, ParserConfigurationException, SAXException {
        Scanner sn = new Scanner(System.in);
        boolean salir = false;
        int opcion; //Guardaremos la opcion del usuario

        while (!salir) {

            System.out.println("\n\n\n1. Parsear Corpus");
            System.out.println("2. Indexar Corpus");
            System.out.println("3. Consultas");
            System.out.println("4. Salir");

            try {

                System.out.println("Escribe una de las opciones");
                opcion = sn.nextInt();

                switch (opcion) {
                    case 1:
                        ParsearCorpus PC = new ParsearCorpus(rutaCorpus);
                        PC.Parseo();
                        break;
                    case 2:
                        IndexarCorpus IC = new IndexarCorpus(rutaCorpus);
                        IC.Indexar();
                        break;
                    case 3:
                        Consultas C = new Consultas(rutaCorpus);
                        C.Consultar();
                        break;
                    case 4:
                        salir = true;
                        break;
                    default:
                        System.out.println("Solo números entre 1 y 4");
                }
            } catch (InputMismatchException e) {
                System.out.println("Debes insertar un número");
                sn.next();
            }
        }
    }
}
