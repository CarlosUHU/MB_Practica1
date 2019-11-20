/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1;

import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author carlos
 */
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
