package test;

import informationManagement.Information;
import informationManagement.InformationGroup;
import informationManagement.Question;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by samuel on 03.07.15.
 */
public class testSaveXMLs {

    public static void main(String[] args) {
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            //root element
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Stack");
            doc.appendChild(rootElement);

            //InformationGroup elements
            Element infoGroup = doc.createElement("InformationGroup");
            rootElement.appendChild(infoGroup);
            infoGroup.setAttribute("comment", "hallo");
            rootElement.appendChild(infoGroup);

            //Information elements
            Element info = doc.createElement("Information");
            info.setAttribute("type", "Information");
            info.setAttribute("date", "100");
            info.setAttribute("ease", "2.5");
            info.setAttribute("amountRepetition", "2");
            info.setAttribute("oldDate", "50");
            info.appendChild(doc.createTextNode("Info1"));
            infoGroup.appendChild(info);


            //Question-Objects
            Element question = doc.createElement("Information");
            question.setAttribute("type", "Question");
            question.appendChild(doc.createTextNode("quest1"));
            infoGroup.appendChild(question);


            //write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult("textFile.xml");
            transformer.transform(source, result);

            /*DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("stack");
            doc.appendChild(rootElement);

            // InformatinGroup elements
            Element infoGroup = doc.createElement("InformationGroup");
            rootElement.appendChild(infoGroup);

            // set attribute to infoGroup element
             infoGroup.setAttribute("comment", "hallo");

            // Information elements
            Element firstname = doc.createElement("Information");
            firstname.appendChild(doc.createTextNode("yong"));
            infoGroup.appendChild(firstname);

            // lastname elements
            Element lastname = doc.createElement("lastname");
            lastname.appendChild(doc.createTextNode("mook kim"));
            infoGroup.appendChild(lastname);

            // nickname elements
            Element nickname = doc.createElement("nickname");
            nickname.appendChild(doc.createTextNode("mkyong"));
            infoGroup.appendChild(nickname);

            // salary elements
            Element salary = doc.createElement("salary");
            salary.appendChild(doc.createTextNode("100000"));
            infoGroup.appendChild(salary);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("testFile.xml"));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);

            System.out.println("File saved!");*/

            } catch (ParserConfigurationException pce) {
                pce.printStackTrace();
            } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }
}
