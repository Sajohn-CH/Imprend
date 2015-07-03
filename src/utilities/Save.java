package utilities;

import informationManagement.Information;
import informationManagement.InformationGroup;
import informationManagement.Question;
import informationManagement.Stack;
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
 * Created by samuel on 30.06.15.
 * Static class with methods to save different files, which the program needs to run. E.g. the files containing the cards.
 */
public class Save {

    public static void saveStack(Stack stack) {
        if(stack.getStackFile().getPath().split("\\.")[1].equals("xml")){
            //if xml-File
            saveXMLStack(stack);
        }
    }

    private static void saveXMLStack(Stack stack) {

        if(stack.getStackFile().exists()) {
            //if file exits it will be remove so it can be replaced (maybe i will later change this, so the file will be ony changed instead of replaced)
            stack.getStackFile().delete();
        }
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            //root element
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Stack");
            doc.appendChild(rootElement);

            ArrayList<InformationGroup> infoGroups = stack.getAllInfoGroups();
            for(int i = 0; i < infoGroups.size(); i++) {
                //InformationGroup elements
                Element infoGroup = doc.createElement("InformationGroup");
                rootElement.appendChild(infoGroup);
                infoGroup.setAttribute("comment", infoGroups.get(i).getComment());
                rootElement.appendChild(infoGroup);

                //Information elements
                ArrayList<Information> infos = infoGroups.get(i).getInformations();
                for(int j = 0; j < infos.size(); j++) {
                    Element info = doc.createElement("Information");
                    info.setAttribute("type", "Information");
                    info.setAttribute("date", String.valueOf(infos.get(j).getDate().getTime()));
                    info.setAttribute("ease", String.valueOf(infos.get(j).getEase()));
                    info.setAttribute("amountRepetition", String.valueOf(infos.get(j).getAmountRepetition()));
                    info.setAttribute("oldDate", String.valueOf(infos.get(j).getOldDate().getTime()));
                    info.appendChild(doc.createTextNode(infos.get(j).getInformation()));
                    infoGroup.appendChild(info);
                }

                //Question-Objects
                ArrayList<Question> questions = infoGroups.get(i).getQuestions();
                for(int j = 0; j < questions.size(); j++) {
                    Element question = doc.createElement("Information");
                    question.setAttribute("type", "Question");
                    question.appendChild(doc.createTextNode(questions.get(j).getQuestion()));
                    infoGroup.appendChild(question);
                }
            }

            //write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(stack.getStackFile());
            transformer.transform(source, result);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }
}