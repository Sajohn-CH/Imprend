package gui;

import informationManagement.*;
import utilities.Imprend;
import utilities.Save;
import utilities.UTF8Control;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by samuel on 30.07.15.
 * panel to edit stacks
 */
public class JEditPanel extends JNavPanel{

    DefaultTableModel tblCardsModel;
    DefaultTableModel tblInfosModel;
    SimpleDateFormat dateFormat;
    ResourceBundle edit;
    InformationGroup lastInfoGroup;     //used in loadTableInfos(InformationGroup infoGroup) to detect changes in the table. It is the InformationGroup the table is currently showing
    Stack stack;


    public JEditPanel(Imprend imprend, final Stack stack) {
        this.stack = stack;
        edit = ResourceBundle.getBundle(imprend.settings.getResourceBundles()+".JEditPanelBundle", imprend.settings.getLocale(), new UTF8Control());
        dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        JLabel lblStack = new JLabel(stack.getName());

        JPanel pnlCards = new JPanel();
        JScrollPane scrlPnCards = new JScrollPane();
        final JTable tblCards = new JTable();
        tblCardsModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };

        JPanel pnlInfos = new JPanel();
        JScrollPane scrlPnInfos = new JScrollPane();
        final JTable tblInfos = new JTable();
        tblInfosModel = new DefaultTableModel();
        JComboBox<String> combo = new JComboBox<>();

        JPanel pnlTools = new JPanel();
        JButton btnDeleteDates = new JButton(edit.getString("deleteDates"));
        final JButton btnDeleteCard = new JButton(edit.getString("deleteCard"));
        JButton btnDeleteInfo = new JButton(edit.getString("deleteInfo"));
        final JButton btnAddInfo = new JButton(edit.getString("addInfo"));
        JButton btnAddCard = new JButton(edit.getString("addCard"));

        tblCards.setModel(tblCardsModel);
        scrlPnCards.setViewportView(tblCards);
        tblCardsModel.addColumn(edit.getString("ColumnQuest"));
        tblCardsModel.addColumn(edit.getString("ColumnAnsw"));
        ListSelectionListener goTblCards = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if(tblCards.getSelectedRow() != -1) {
                    InformationGroup infoGroup = stack.getInfoGroupById(tblCards.getSelectedRow());
                    checkForChanges();
                    loadTableInfos(infoGroup);
                }
            }
        };
        tblCards.getSelectionModel().addListSelectionListener(goTblCards);

        combo.addItem(edit.getString("information"));
        combo.addItem(edit.getString("question"));

        tblInfos.setModel(tblInfosModel);
        scrlPnInfos.setViewportView(tblInfos);
        tblInfosModel.addColumn(edit.getString("ColumnType"));
        tblInfosModel.addColumn(edit.getString("ColumnInfo"));
        tblInfosModel.addColumn(edit.getString("ColumnDate"));
        TableColumn typeColumn = tblInfos.getColumnModel().getColumn(0);
        typeColumn.setCellEditor(new DefaultCellEditor(combo));
        ListSelectionListener goTblInfos = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                checkForChanges();
                //loadTableInfos(lastInfoGroup);
            }
        };
        //tblInfos.getSelectionModel().addListSelectionListener(goTblInfos);

        //ActionListeners
        ActionListener goDeleteDates = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int answer = JOptionPane.showConfirmDialog(null, edit.getString("MsgSureDeleteDates"), edit.getString("MsgSure"), JOptionPane.OK_CANCEL_OPTION);
                if(answer == 0) {
                    //set all dates to 0 (0 = never asked/learned)
                    for (int i = 0; i < stack.getAmountInformationGroups(); i++) {
                        for(int j = 0; j < stack.getInfoGroupById(i).getAmountInformations(); j++) {
                            if(stack.getInfoObjectById(j, i).getType().equals(Imprend.strInfoObjectInfo)) {
                                Information info = (Information) stack.getInfoObjectById(j, i);
                                info.setDate(new Date(0));
                                info.setEase(2.5);
                                info.setAmountRepetition(0);
                                info.setOldDate(new Date(0));
                            }
                        }
                    }
                    Save.saveStack(stack);
                    //reload the list
                    InformationGroup infoGroup = stack.getInfoGroupById(tblCards.getSelectedRow());
                    loadTableInfos(infoGroup);
                }
            }
        };

        ActionListener goDeleteCard = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int answer = JOptionPane.showConfirmDialog(null, edit.getString("MsgSureDeleteCard"), edit.getString("MsgSure"), JOptionPane.OK_CANCEL_OPTION);
                if(answer == 0) {
                    int index = tblCards.getSelectedRow();
                    tblCards.changeSelection(0, 0, false, false);
                    stack.removeInformationGroup(index);
                    tblCardsModel.removeRow(index);
                    tblCards.changeSelection(0, 0, false, false);
                }
            }
        };

        ActionListener goDeleteInfo = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(tblCards.getSelectedRow() == -1) {
                    //no row was selected
                    return;
                }
                InformationGroup infoGroup = stack.getInfoGroupById(tblCards.getSelectedRow());
                infoGroup.removeInformation(tblInfos.getSelectedRow());
                tblInfosModel.removeRow(tblInfos.getSelectedRow());
                tblInfos.changeSelection(0, 0, false, false);
            }
        };

        ActionListener goAddInfo = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Information info = new Information();
                lastInfoGroup.addInformation(info);
                //fill table with default values
                String[] rowVector = new String[3];
                rowVector[0] = edit.getString("information");
                rowVector[1] = "";
                rowVector[2] = edit.getString("neverAsked");
                tblInfosModel.addRow(rowVector);
                tblInfos.changeSelection(tblInfosModel.getRowCount()-1, 1, false, false);
            }
        };

        ActionListener goAddCard = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                tblCardsModel.addRow(new String[]{});
                InformationGroup infoGroup = new InformationGroup();
                stack.addInformationGroup(infoGroup);
                tblInfos.changeSelection(0, 0, false, false);
                tblCards.changeSelection(tblCardsModel.getRowCount()-1, 0, false, false);
                btnAddInfo.doClick();
            }
        };

        btnDeleteDates.addActionListener(goDeleteDates);
        btnDeleteCard.addActionListener(goDeleteCard);
        btnDeleteInfo.addActionListener(goDeleteInfo);
        btnAddInfo.addActionListener(goAddInfo);
        btnAddCard.addActionListener(goAddCard);

        pnlCards.add(scrlPnCards);

        pnlInfos.add(scrlPnInfos);

        pnlTools.add(btnDeleteDates);
        pnlTools.add(btnDeleteCard);
        pnlTools.add(btnDeleteInfo);
        pnlTools.add(btnAddInfo);
        pnlTools.add(btnAddCard);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;

        add(lblStack, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        add(pnlCards, c);

        c.gridx = 1;
        c.gridy = 1;
        add(pnlInfos, c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 3;
        add(pnlTools, c);

        loadTableCards(stack);

    }

    private void loadTableCards(Stack stack) {
        String[] rowData = new String[2];
        for(int i = 0; i < stack.getAmountInformationGroups(); i++) {
            //decide whether there is a question or not (not = at least 2 informations)
            rowData[0] = stack.getInfoObjectById(i, 0).getInformation();
            if(stack.getInfoGroupById(i).getAmountInformations() > 1) {
                rowData[1] = stack.getInfoObjectById(i, 1).getInformation();
            } else {
                rowData[1] = "";
            }
            tblCardsModel.addRow(rowData);
        }
    }

    private void loadTableInfos(InformationGroup infoGroup) {
        //loads all the Information and Questions and other stuff form the given InformationGroup into the table
        //Removing the old content
        for(int i = tblInfosModel.getRowCount()-1; i >= 0 ; i--) {
            tblInfosModel.removeRow(i);
        }

        String[] rowData = new String[3];
        for(int i = 0; i < infoGroup.getAmountInformations(); i++) {
            switch(infoGroup.getInfoObjectById(i).getType()) {
                case Imprend.strInfoObjectInfo:
                    rowData[0] = edit.getString("information");
                    Information info = (Information) infoGroup.getInfoObjectById(i);
                    if(info.getDate().getTime() == 0) {
                        rowData[2] = edit.getString("neverAsked");
                    } else {
                        rowData[2] = dateFormat.format(info.getDate());
                    }
                    break;
                case Imprend.strInfoObjectQuest:
                    rowData[0] = edit.getString("question");
                    rowData[2] = "-";
                    break;
            }
            rowData[1] = infoGroup.getInfoObjectById(i).getInformation();
            tblInfosModel.addRow(rowData);
        }
        lastInfoGroup = infoGroup;

    }

    private void checkForChanges() {
        //Checks for changes made in the table and changes them too in the InformationGroup in the stack, so they can later be save
        if(tblInfosModel.getRowCount() != 0 && lastInfoGroup != null) {
            for(int i = 0; i < lastInfoGroup.getAmountInformations(); i++) {
                //Detect if something has changed, if so, save the changes
                if(tblInfosModel.getValueAt(i, 0).equals(edit.getString("information")) && lastInfoGroup.getInfoObjectById(i).getType().equals(Imprend.strInfoObjectQuest)) {
                    //transfrom the Question info an Information
                    String question = lastInfoGroup.getInfoObjectById(i).getInformation();
                    int id = lastInfoGroup.getInfoObjectById(i).getId();
                    Information info = new Information();
                    info.setInformation(question);
                    info.setDate(new Date(0));
                    info.setEase(2.5);
                    info.setAmountRepetition(0);
                    info.setOldDate(new Date(0));
                    lastInfoGroup.replaceInfoObject(i, info);
                }else if(tblInfosModel.getValueAt(i, 0).equals(edit.getString("question")) && lastInfoGroup.getInfoObjectById(i).getType().equals(Imprend.strInfoObjectInfo)) {
                    //transform the Information into a Question
                    String information = lastInfoGroup.getInfoObjectById(i).getInformation();
                    int id = lastInfoGroup.getInfoObjectById(i).getId();
                    Question question = new Question();
                    question.setInformation(information);
                    question.setId(id);
                    lastInfoGroup.replaceInfoObject(i, question);
                }
                if(!lastInfoGroup.getInfoObjectById(i).getInformation().equals(tblInfosModel.getValueAt(i, 1))) {
                    //Information has changed
                    lastInfoGroup.getInfoObjectById(i).setInformation(String.valueOf(tblInfosModel.getValueAt(i, 1)));
                }
                if(lastInfoGroup.getInfoObjectById(i).getType().equals(Imprend.strInfoObjectInfo)) {
                    //Date can only be changed by an Information
                    Information info = (Information) lastInfoGroup.getInfoObjectById(i);
                    if(!dateFormat.format(info.getDate()).equals(tblInfosModel.getValueAt(i, 2))) {
                        //Catch the case, the date is 0 (which is being replace by a text in the table) and doesn't get changed.
                        if(!tblInfosModel.getValueAt(i, 2).equals(edit.getString("neverAsked")) && !info.equals("0")) {
                            try {
                                //to change also the oldDate field, get the difference between the date in the file and the one it was change to.
                                Date newDate = dateFormat.parse(String.valueOf(tblInfosModel.getValueAt(i, 2)));
                                Date fileDate = info.getDate();
                                Date newOldDate;
                                if(newDate.before(fileDate)) {
                                    long difference = fileDate.getTime() - newDate.getTime();
                                    newOldDate = new Date(info.getOldDate().getTime()-difference);
                                } else {
                                    long difference = newDate.getTime() - fileDate.getTime();
                                    newOldDate = new Date(info.getOldDate().getTime()+difference);
                                }
                                info.setDate(newDate);
                                info.setOldDate(newOldDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                }
            }
            /*
            //Check for added InformationGroups (Cards) or InfoObjects
            if(tblCardsModel.getRowCount() > stack.getAmountInformationGroups()) {
                //add InformationGroup
                //for every added InformationGroup
                for(int i = 0; i < tblCardsModel.getRowCount()-stack.getAmountInformationGroups(); i++) {
                    //InformationGroup infoGroup = new InformationGroup();
                    //stack.addInformationGroup(infoGroup);
                }
            }
            if(tblInfosModel.getRowCount() > lastInfoGroup.getAmountInformations()) {
                //add InfoObject
                //for every added InfoObject
                int amountNewObjects = tblInfosModel.getRowCount()-lastInfoGroup.getAmountInformations();
                int amountObjectOld = lastInfoGroup.getAmountInformations();
                for(int i = 0; i < amountNewObjects; i++) {
                    //check if there is an information
                    if(tblInfosModel.getValueAt(i+amountObjectOld, 1) == (null) || tblInfosModel.getValueAt(i+amountObjectOld, 1).equals("")) {
                        return;
                    }
                    if(tblInfosModel.getValueAt(i+amountObjectOld, 0).equals(edit.getString("information"))) {
                        //Information Object
                        Information info = new Information();
                        info.setInformation(String.valueOf(tblInfosModel.getValueAt(i+amountObjectOld, 1)));
                        if(tblInfosModel.getValueAt(i+amountObjectOld, 2) != (null)) {
                            try {
                                info.setDate(dateFormat.parse(String.valueOf(tblInfosModel.getValueAt(i+amountObjectOld, 2))));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        info.setOldDate(new Date(0));
                        info.setEase(2.5);
                        info.setAmountRepetition(0);
                        lastInfoGroup.addInformation(info);
                    } else if(tblInfosModel.getValueAt(i+amountObjectOld, 0).equals(edit.getString("question"))) {
                        //check if there is an information
                        if(tblInfosModel.getValueAt(i+amountObjectOld, 1) == (null) || tblInfosModel.getValueAt(i+amountObjectOld, 1).equals("")) {
                            return;
                        }
                        Question question = new Question();
                        question.setInformation(String.valueOf(tblInfosModel.getValueAt(i+amountObjectOld, 1)));
                        lastInfoGroup.addInformation(question);
                    }
                }
            }*/
        }
    }

    @Override
    public void back(Imprend imprend) {
        cleanUp(imprend);
        imprend.switchPanel(imprend.strPnlMenu);
    }

    @Override
    public void cleanUp(Imprend imprend) {
        checkForChanges();
        //check for empty InformationGroups and empty InfoObjects
        for(int i = stack.getAmountInformationGroups()-1; i > 0; i--) {
            for(int j = 0; j < stack.getInfoGroupById(i).getAmountInformations(); j++) {
                if(stack.getInfoObjectById(i, j).getInformation().equals("")) {
                    stack.getInfoGroupById(i).removeInformation(j);
                }
            }
            if(stack.getInfoGroupById(i).getAmountInformations() == 0) {
                //emtpy InformationGroup
                stack.removeInformationGroup(i);
            }

        }
        Save.saveStack(stack);
    }
}

