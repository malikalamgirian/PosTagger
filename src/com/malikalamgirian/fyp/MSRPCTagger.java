/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.malikalamgirian.fyp;

import edu.stanford.nlp.tagger.maxent.*;
import java.io.*;
import java.util.Date;
import org.w3c.dom.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.StreamResult;

/**
 *
 * @author Wasif
 */
public class MSRPCTagger {

    /* The MaxentTagger Reference variable */
    private MaxentTagger tagger;

    /* Filename fields declarations here */
    public String MSRPC_Input_File_URL;
    public String MSRPC_Output_XML_File_URL;
    public String MSRPC_Output_XML_Tagged_File_URL;

    /* private stats */
    Integer positives = 0,
            negatives = 0,
            localTotal = 0,
            grandTotal = 5801;
     
     Double positivesRatio = 0.0,
            negativesRatio = 0.0,
            totalLocalsRatio = 0.0;



    public MSRPCTagger() throws IOException, ClassNotFoundException {

        /* Initialize the tagger here */
        tagger = new MaxentTagger("../../stanford_Tagger_Parser/"
                + "stanford-postagger-full-2010-04-30/stanford-postagger-full-2010-04-30"
                + "/models/left3words-wsj-0-18.tagger");

    }

    public void Process() throws Exception {
        /* This method is to be used when the process button for MSRPC is pressed */
        /* Here we will
         * 1. Set the output File URL, with XML extension;
         * 2. Convert Input File into XML;
         * 3. Write the output File with tagged sentences, in XML format;
         * 4. Close the read file;
         * 6. Make user aware of the process at each step.
         * 7. Handle Exceptions properly, with proper messages.
         */

        /* 1 */
        /* 1.1 for file without tags */
        MSRPC_Output_XML_File_URL = MSRPC_Input_File_URL.substring(0, MSRPC_Input_File_URL.length() - 4);
        MSRPC_Output_XML_File_URL += "_xml";
        MSRPC_Output_XML_File_URL += ".xml";

        /* 1.2 for file with tags */
        MSRPC_Output_XML_Tagged_File_URL = MSRPC_Input_File_URL.substring(0, MSRPC_Input_File_URL.length() - 4);
        MSRPC_Output_XML_Tagged_File_URL += "_xml_tagged";
        MSRPC_Output_XML_Tagged_File_URL += ".xml";

        /* 2 */
        Convert(MSRPC_Input_File_URL, MSRPC_Output_XML_File_URL);

        /* 3 */
        ConvertAndTag(MSRPC_Input_File_URL, MSRPC_Output_XML_Tagged_File_URL);

        /*Basic statistics are */
        localTotal          =        negatives + positives  ;
        positivesRatio      =        positives.doubleValue() * 100 / localTotal.doubleValue() ;
        negativesRatio      =        negatives.doubleValue() * 100 / localTotal.doubleValue() ;
        totalLocalsRatio    =        localTotal.doubleValue() * 100 / grandTotal.doubleValue() ;
        
        System.out.println("\nCalculations are : ");
        System.out.println("\tPositive pairs : " + positives);
        System.out.println("\tNegatives pairs : " + negatives);
        System.out.println("\tTotal pairs : " + localTotal);
        System.out.println("\tPositive pairs ratio is : " + positivesRatio );
        System.out.println("\tNegative pairs ratio is : " + negativesRatio );
        System.out.println("\tThis Set is \'" + totalLocalsRatio + "\'% of 5801 pairs.");

    }

    private void Convert(String txt_Input_File_Absolute_URL,
            String XML_Output_File_Absolute_URL) throws Exception {

        System.out.println("Convert File : " + txt_Input_File_Absolute_URL + "\nOn : " + new Date().toString());
        /* Here we
         * 1. Open input file
         * 2. Get Blank XML Document
         * 3. Generate Document Element
         * 4. Read Lines & Using a method Populate them in XML file
         * 5. Apply transformation to XML
         * 6. close the open file
         */

        String lineRead;

        /* 1.1 Open input file */
        FileInputStream fInStream = new FileInputStream(txt_Input_File_Absolute_URL);

        /* 1.2 Get the object of DataInputStream */
        DataInputStream dInStream = new DataInputStream(fInStream);
        BufferedReader bReader = new BufferedReader(new InputStreamReader(dInStream));

        /* 2.Get Blank XML Document */
        Document doc = com.malikalamgirian.fyp.GetBlankXMLDocument.getDocument();

        /* 3. Generate Document Element */
        Element root = doc.createElement(txt_Input_File_Absolute_URL.substring(txt_Input_File_Absolute_URL.lastIndexOf("\\") + 1, txt_Input_File_Absolute_URL.lastIndexOf(".")) + "_xml");

        /* 3.1 add it to xml tree */
        doc.appendChild(root);

        /* 4. Read Lines & Using a method Populate them in XML file */

        /* 4.1 Waste away the titles line */
        lineRead = bReader.readLine();

        /* 4.2 Populate all other lines */
        while ((lineRead = bReader.readLine()) != null) {
            try {
                PopulateLine(lineRead, doc, root);
            } catch (Exception ex) {
                System.out.println("PopulateLine: for Conversion from txt to XML : " + ex);
                System.out.println(ex.getMessage());
                System.out.println("Line Read: " + lineRead);
            }
        }

        /* 5. Transform */
        root.normalize();

        TransformerFactory tranFactory = TransformerFactory.newInstance();
        Transformer aTransformer = tranFactory.newTransformer();

        aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");

        /* 5.1 Describes the domain of the indent-amount parameter (apache xst specific, non JAXP generic). */
        aTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        /* 5.2 Set source and destination */
        Source src = new DOMSource(doc);
        //Result dest = new StreamResult(System.out);
        //aTransformer.transform(src, dest);
        Result dest = new StreamResult(new File(XML_Output_File_Absolute_URL));

        aTransformer.transform(src, dest);

        /* 6 */
        bReader.close();


    }

    private void PopulateLine(String lineToPopulate, Document doc, Element root) throws Exception {


        /* Here we
         * 1. Preprocess the line into proper storage units
         * 2. Create proper element hierarchy and add them to root node
         */
        String String1,
                String2,
                StringID1,
                StringID2,
                Quality,
                lineToPopulateCopy = lineToPopulate;

        int indexOfParticularCharacter;
        String[] arrayForRegex;
        String strings, replacedStrings;
        String numbers;


        /* 1. Preprocess the line into proper storage units
         * 1.1 Here we divide the string into Strings and Numbers
         * 1.2 Extract numbers
         * 1.3 Extract strings
         */

        /*
         * New testing, worked well, Success
         * this regex is the proper one, which uses
         * Tab "\t" for split operation,
         * because this change was lately made so some comments might be improper
         * the explicit commenting Has been done for implementing this new Regex
         */
        arrayForRegex = lineToPopulate.split("\t");
        
        for( int i = 0; i < arrayForRegex.length; i++){
            System.out.println( arrayForRegex[i] );
        }
        System.out.println("\n\n");
        /*
         * New testing ends here
         */

//        /* 1.1 Divide the string into Strings and Numbers */
//        arrayForRegex = lineToPopulate.split("[a-zA-Z]|[\"]");
//        int indexOfFirstCharacter = arrayForRegex[0].length();
//
//        numbers = lineToPopulate.substring(0, indexOfFirstCharacter);
//        strings = lineToPopulate.substring(indexOfFirstCharacter, lineToPopulate.length());
//
//
//        /* 1.2 Extract numbers */
//        numbers = numbers.replaceAll("[^0-9]", "_");
//        arrayForRegex = numbers.split("_");

        Quality = arrayForRegex[0];
        StringID1 = arrayForRegex[1];
        StringID2 = arrayForRegex[2];

//        /* 1.3 Extract strings */
//        replacedStrings = strings.replaceAll("[[.]|[\"]|[?]][\\s&&[^ ]]", "]");
//        arrayForRegex = replacedStrings.split("]");
//
//        /* 1.3.1 We have to check for consistency that whether String1
//         * ends with . 0r ? or "
//         * In simple words fetch the next word to arrayForRegex[0] substring in strings
//         */

//        indexOfParticularCharacter = arrayForRegex[0].length();
//        String1 = arrayForRegex[0] + strings.charAt(indexOfParticularCharacter);
//        String2 = arrayForRegex[1];
        String1 = arrayForRegex[3];
        String2 = arrayForRegex[4];

        /* 2 Create proper element hierarchy and add them to root node */
        Element pair = doc.createElement("Pair");

        /* Add the atribute to the child */
        pair.setAttribute("Quality", Quality);

        Element string1 = doc.createElement("String1");
        Element string2 = doc.createElement("String2");

        string1.setAttribute("Id", StringID1);
        string2.setAttribute("Id", StringID2);

        string1.setTextContent(String1);
        string2.setTextContent(String2);

        pair.appendChild(string1);
        pair.appendChild(string2);


        root.appendChild(pair);

        root.normalize();

        /* Extra validation for Strings */
        if (lineToPopulateCopy.indexOf(String1) == -1 || lineToPopulateCopy.indexOf(String2) == -1) {
            System.out.println("Substring Validation failed : in line, " + lineToPopulateCopy);
        }

        /* Calaulate stats from Quality */
        
        if (Integer.parseInt(Quality) == 0) {
            ++negatives;
        } else if (Integer.parseInt(Quality) == 1) {
            ++positives;
        }

    }

    private void ConvertAndTag(String txt_Input_File_Absolute_URL,
            String XML_Output__Tagged_File_Absolute_URL) throws Exception {

        System.out.println("Convert and Tag File.");
        /* Here we
         * 1. Open input file
         * 2. Get Blank XML Document
         * 3. Generate Document Element
         * 4. Read Lines & Using a method Populate them in XML file
         * 5. Apply transformation to XML
         */

        String lineRead;

        /* 1.1 Open input file */
        FileInputStream fInStream = new FileInputStream(txt_Input_File_Absolute_URL);

        /* 1.2 Get the object of DataInputStream */
        DataInputStream dInStream = new DataInputStream(fInStream);
        BufferedReader bReader = new BufferedReader(new InputStreamReader(dInStream));

        /* 2.Get Blank XML Document */
        Document doc = com.malikalamgirian.fyp.GetBlankXMLDocument.getDocument();

        /* 3. Generate Document Element */
        Element root = doc.createElement(txt_Input_File_Absolute_URL.substring(txt_Input_File_Absolute_URL.lastIndexOf("\\") + 1, txt_Input_File_Absolute_URL.lastIndexOf(".")) + "_xml_tagged");

        /* 3.1 add it to xml tree */
        doc.appendChild(root);

        /* 4. Read Lines & Using a method Populate them in XML file */

        /* 4.1 Waste away the titles line */
        lineRead = bReader.readLine();

        /* 4.2 Populate all other lines */
        while ((lineRead = bReader.readLine()) != null) {
            try {
                PopulateTaggedLine(lineRead, doc, root);

            } catch (Exception ex) {
                System.out.println("PopulateLine: for Conversion from txt to XML : " + ex);
                System.out.println(ex.getMessage());
                System.out.println("Line Read: " + lineRead);
            }
        }

        /* 5. Transform */
        root.normalize();

        TransformerFactory tranFactory = TransformerFactory.newInstance();
        Transformer aTransformer = tranFactory.newTransformer();

        aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");

        /* 5.1 Describes the domain of the indent-amount parameter (apache xst specific, non JAXP generic). */
        aTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        /* 5.2 Set source and destination */
        Source src = new DOMSource(doc);
        //Result dest = new StreamResult(System.out);
        //aTransformer.transform(src, dest);
        Result dest = new StreamResult(new File(XML_Output__Tagged_File_Absolute_URL));

        aTransformer.transform(src, dest);

    }

    private void PopulateTaggedLine(String lineToPopulate, Document doc, Element root) throws Exception {

        System.out.println("Populate Tagged Line");

        /* Here we
         * 1. Preprocess the line into proper storage units
         * 2. Tag Strings
         * 3. Create proper element hierarchy and add them to root node
         */
        String String1,
                String2,
                StringID1,
                StringID2,
                Quality,
                taggedString1,
                taggedString2;

        int indexOfParticularCharacter;
        String[] arrayForRegex;
        String strings, replacedStrings;
        String numbers;


        /* 1. Preprocess the line into proper storage units
         * 1.1 Here we divide the string into Strings and Numbers
         * 1.2 Extract numbers
         * 1.3 Extract strings
         */
         /*
         * New testing, worked well, Success
         * this regex is the proper one, which uses
         * Tab "\t" for split operation,
         * because this change was lately made so some comments might be improper
         * the explicit commenting Has been done for implementing this new Regex
         */
        arrayForRegex = lineToPopulate.split("\t");

        for( int i = 0; i < arrayForRegex.length; i++){
            System.out.println( arrayForRegex[i] );
        }
        System.out.println("\n\n");

//        /* 1.1 Divide the string into Strings and Numbers */
//        arrayForRegex = lineToPopulate.split("[a-zA-Z]|[\"]");
//        int indexOfFirstCharacter = arrayForRegex[0].length();
//
//        numbers = lineToPopulate.substring(0, indexOfFirstCharacter);
//        strings = lineToPopulate.substring(indexOfFirstCharacter, lineToPopulate.length());
//
//
//        /* 1.2 Extract numbers */
//        numbers = numbers.replaceAll("[^0-9]", "_");
//        arrayForRegex = numbers.split("_");

          Quality   = arrayForRegex[0];
          StringID1 = arrayForRegex[1];
          StringID2 = arrayForRegex[2];

//        /* 1.3 Extract strings */
//        replacedStrings = strings.replaceAll("[[.]|[\"]|[?]][\\s&&[^ ]]", "]");
//        arrayForRegex = replacedStrings.split("]");
//
//        /* 1.3.1 We have to check for consistency that whether String1
//         * ends with . 0r ? or "
//         * In simple words fetch the next word to arrayForRegex[0] substring in strings
//         */
//
//        indexOfParticularCharacter = arrayForRegex[0].length();
//        String1 = arrayForRegex[0] + strings.charAt(indexOfParticularCharacter);
//        String2 = arrayForRegex[1];
          
          String1 = arrayForRegex[3];
          String2 = arrayForRegex[4];


        System.out.println(lineToPopulate);
        
        /* 2. tag here */
        taggedString1 = tagger.tagString(String1);
        System.out.println(taggedString1);

        taggedString2 = tagger.tagString(String2);
        System.out.println(taggedString2 + "\n");


        /* 3. Create proper element hierarchy and add them to root node */
        Element pair = doc.createElement("Pair");

        //Add the atribute to the child
        pair.setAttribute("Quality", Quality);

        Element string1 = doc.createElement("String1");
        Element string2 = doc.createElement("String2");

        string1.setAttribute("Id", StringID1);
        string2.setAttribute("Id", StringID2);

        string1.setTextContent(taggedString1);
        string2.setTextContent(taggedString2);

        pair.appendChild(string1);
        pair.appendChild(string2);


        root.appendChild(pair);

    }

//    private void PrintStatistics(Element root) {
//
//        int positives = 0,
//                negatives = 0;
//
//        NodeList pair = root.getChildNodes();
//
//        System.out.println("Total number of string pairs is :  " + pair.getLength());
//
//        for (int i = 0; i <= pair.getLength(); i++) {
//
//            System.out.println( pair.item(i).getAttributes().item(0).getNodeValue() );
//            /* count positives and negatives */
////          if( Integer.parseInt( pair.item(i).getAttributes().item(0).getNodeValue()) == 0){
////               ++negatives;
////          }
////          else if(Integer.parseInt( pair.item(i).getAttributes().item(0).getNodeValue()) == 1){
////                ++positives;
////          }
//        }
//
//        System.out.println("\n Calculations are : ");
//        System.out.println("\t Positive pairs : " + positives);
//        System.out.println("\t Negatives pairs : " + negatives);
//        System.out.println("\t Total pairs : " + negatives + positives);
//
//    }
}
