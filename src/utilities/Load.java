package utilities;

import informationManagement.Information;
import informationManagement.InformationGroup;
import informationManagement.Question;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by samuel on 30.06.15.
 * Static class with methods to load different files, which the program needs to run. E.g. the files containing the cards.
 */
public class Load {

    public static String[] getAllObjectPathsIn(File dir) {
        //returns all the paths of all the objects in the given directory
        if(!dir.isDirectory()) {
            return null;
        }
        File[] files = dir.listFiles();
        String[] paths = new String[files.length];
        for(int i = 0; i < files.length; i++) {
            paths[i] = files[i].getPath();
        }
        return paths;
    }

    public static ArrayList<InformationGroup> loadStack(File file) {
        if(file.getPath().split("\\.")[1].equals("xml")){
            //if xml-File
            return loadXMLStack(file);
        }
        return null;
    }

    private static ArrayList<InformationGroup> loadXMLStack(File file) {
        //Loads all the InformationGroups of an XML-File, which are makung up a stack.
        ArrayList<InformationGroup> infoGroups = new ArrayList<>();

        try {
            //start reading the xml-File
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            dBuilder = dbFactory.newDocumentBuilder();
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("InformationGroup");

            for(int i = 0; i < nList.getLength(); i++) {
                Element element1 = (Element) nList.item(i);
                InformationGroup infoGroup = new InformationGroup();
                infoGroup.setComment(element1.getAttribute("comment"));
                infoGroup.setId(Integer.valueOf(element1.getAttribute("id")));
                //Create for every Object in the xml-File a corresponding Object (Information or Question) and add it to the InformationGroup infoGroup
                for(int j = 0; j < element1.getElementsByTagName("Information").getLength(); j++) {
                    Element element2 = (Element) element1.getElementsByTagName("Information").item(j);
                    if(element2.getAttribute("type").equals(Imprend.strInfoObjectInfo)) {
                        //it is an Information-Object
                        Information info = new Information();
                        info.setInformation(element2.getTextContent());
                        info.setDate(new Date(Long.valueOf(element2.getAttribute("date"))));
                        info.setEase(Double.valueOf(element2.getAttribute("ease")));
                        info.setAmountRepetition(Integer.valueOf(element2.getAttribute("amountRepetition")));
                        Date oldDate = new Date(Long.valueOf(element2.getAttribute("oldDate")));
                        info.setOldDate(oldDate);
                        info.setId(Integer.valueOf(element2.getAttribute("id")));
                        info.setGroup(element2.getAttribute("group"));
                        infoGroup.addInformation(info);
                    } else if(element2.getAttribute("type").equals(Imprend.strInfoObjectQuest)) {
                        //it is an Question-Object
                        Question question = new Question();
                        question.setInformation(element2.getTextContent());
                        question.setId(Integer.valueOf(element2.getAttribute("id")));
                        question.setGroup(element2.getAttribute("group"));
                        infoGroup.addInformation(question);
                    }
                }
                infoGroups.add(infoGroup);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return infoGroups;

    }

    private static int getStackIdXML(File file) {
        //reads the ID of a stack out of the xml-File
        try {
            //start reading the xml-File
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            dBuilder = dbFactory.newDocumentBuilder();
            doc.getDocumentElement().normalize();

            return Integer.valueOf(doc.getDocumentElement().getAttribute("id"));

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }
}
