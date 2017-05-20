/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.malikalamgirian.fyp;


import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 *
 * @author Wasif
 */
public class GetBlankXMLDocument {

    public static Document getDocument() {
        Document doc;
        
        try {
            //Create instance of DocumentBuilderFactory   
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            //Get the DocumentBuilder 
            DocumentBuilder parser = factory.newDocumentBuilder();

            //Create blank DOM  Document
            doc = parser.newDocument();
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return doc;
    }
}


