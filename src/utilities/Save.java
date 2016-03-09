package utilities;

import informationManagement.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

/**
 * Sammlung von statischen Methode die benötigt werden um Dateien von Stapeln zu speichern. <br>
 * Erstellt am 30.06.15.
 * @author Samuel Martin
 */
public class Save {

    /**
     * Speichert den gegeben Stapel in seiner Datei ab. Momentan werden nur .xml-Dateien unterstützt.
     * @param stack  Zu speichernder Stapel
     */
    public static void saveStack(Stack stack) {
        String[] filePath = stack.getStackFile().getPath().split("\\.");
        if(filePath[filePath.length-1].equals("xml")){
            //if xml-File
            saveXMLStack(stack);
        }
    }

    private static void saveXMLStack(Stack stack) {
        //saves the content of a Stack info a xml-File

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

            ArrayList<InformationGroup> infoGroups = stack.copyAllInformationGroups();
            for(int i = 0; i < infoGroups.size(); i++) {
                //InformationGroup elements
                Element infoGroup = doc.createElement("InformationGroup");
                rootElement.appendChild(infoGroup);
                infoGroup.setAttribute("comment", infoGroups.get(i).getComment());
                infoGroup.setAttribute("id", String.valueOf(infoGroups.get(i).getId()));
                rootElement.appendChild(infoGroup);

                //Information elements
                ArrayList<InfoObject> infos = infoGroups.get(i).copyAllInformations();
                for(int j = 0; j < infos.size(); j++) {
                    Element info = doc.createElement("Information");
                    info.setAttribute("type", infos.get(j).getType());
                    info.setAttribute("id", String.valueOf(infos.get(j).getId()));
                    info.setAttribute("group", String.valueOf(infos.get(j).getGroup()));
                    if(infos.get(j).getType().equals(Imprend.strInfoObjectInfo)) {
                        //attributes, which only an Information-Object has.
                        Information information = (Information) infos.get(j);
                        info.setAttribute("date", String.valueOf(information.getDate().getTime()));
                        info.setAttribute("oldDate", String.valueOf(information.getOldDate().getTime()));
                        info.setAttribute("ease", String.valueOf(information.getEase()));
                        info.setAttribute("amountRepetition", String.valueOf(information.getAmountRepetition()));
                    }
                    info.appendChild(doc.createTextNode(infos.get(j).getInformation()));
                    infoGroup.appendChild(info);
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

    /**
     * Speichert Statistik in xml-Datei.
     * @param stats Statistik zum Speichern.
     */
    public static void saveStatistics(Statistic stats) {
        File statsFile = new File("resources" + File.separator + "stats.xml");

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            //root element
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Stats");
            doc.appendChild(rootElement);

            for(Map.Entry<String, Integer> entry : Imprend.statistic.getLearned().entrySet()) {
                Element learned = doc.createElement("Learned");
                learned.setAttribute("date", entry.getKey());
                learned.appendChild(doc.createTextNode(String.valueOf(entry.getValue())));
                rootElement.appendChild(learned);
            }


            writeContentIntoXML(doc, statsFile);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static void writeContentIntoXML(Document doc, File file) throws TransformerException {
        //write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }
}