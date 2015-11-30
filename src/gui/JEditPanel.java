package gui;

import informationManagement.*;
import utilities.Imprend;
import utilities.Save;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Kindklasse von JNavPanel. Enthält alle Elemmente, die es braucht um einen Stapel zu bearbeiten. Es beinhaltet zwei Tabellen. Die eine zeigt alle InformationGroups eines Stapels.
 * Die andere zeigt die InfoObjects der InformationGroup, die in der anderen Tabelle ausgewählt wurde. Die InformationGroups können bearbeitet werden. Bei jeder Änderung wird in der Tabelle
 * eine Methode aufgerufen, die alles auf Änderungen überprüft.
 * Es können können InformationGroups und InfoObjects gelöscht und hinzugefügt werden.
 * Auch kann der Lernfortschritt eines Stapels gelöscht werden (hauptsächlich zum Testen gedacht). Es besitzt den Stapel, den es bearbeitet als Attribut, welches
 * dem Konstruktor übergeben wird.<br>
 * Erstellt am 30.07.15.
 * @author Samuel Martin
 * {@inheritDoc}
 */
public class JEditPanel extends JNavPanel{

    private DefaultTableModel tblCardsModel;
    private JTable tblCards;
    private DefaultTableModel tblInfosModel;
    private JTable tblInfos;
    private SimpleDateFormat dateFormat;
    private ResourceBundle resource;
    private InformationGroup lastInfoGroup;     //used in loadTableInfos(InformationGroup infoGroup) to detect changes in the table. It is the InformationGroup the table is currently showing
    private Stack stack;

    /**
     * Konstruktor. Initialisiert alle Elemente und ordnet diese an.
     * @param imprend  Imprend als Schnittstelle um z.B. Einstellungen zu erhalten
     * @param stack  Stapel, der bearbeitet werden soll.
     */
    public JEditPanel(final Imprend imprend, final Stack stack) {
        this.stack = stack;
        resource = imprend.settings.getResourceBundle();
        dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        JLabel lblStack = new JLabel(stack.getName());

        JPanel pnlCards = new JPanel();
        JScrollPane scrlPnCards = new JScrollPane();
        tblCards = new JTable();
        tblCardsModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };

        JPanel pnlInfos = new JPanel();
        JScrollPane scrlPnInfos = new JScrollPane();
        tblInfos = new JTable();
        tblInfosModel = new DefaultTableModel();
        JComboBox<String> combo = new JComboBox<>();

        JPanel pnlTools = new JPanel();
        JButton btnDeleteDates = new JButton(resource.getString("deleteDates"));
        final JButton btnDeleteCard = new JButton(resource.getString("deleteCard"));
        JButton btnDeleteInfo = new JButton(resource.getString("deleteInfo"));
        final JButton btnAddInfo = new JButton(resource.getString("addInfo"));
        JButton btnAddCard = new JButton(resource.getString("addCard"));

        tblCards.setModel(tblCardsModel);
        scrlPnCards.setViewportView(tblCards);
        tblCardsModel.addColumn(resource.getString("ColumnQuest"));
        tblCardsModel.addColumn(resource.getString("ColumnAnsw"));
        ListSelectionListener goTblCards = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                //is being called everytime the user selects something on the tblCards. Is used to change the tblInfos ancordingly to the tblCards, depending which InformationGroup the user
                //selects on the tblCards. This InformationGroup will be shown at the tblInfos.
                if(tblCards.getSelectedRow() != -1) {
                    InformationGroup infoGroup = stack.getInfoGroupById(tblCards.getSelectedRow());
                    loadTableInfos(infoGroup);
                }
            }
        };
        tblCards.getSelectionModel().addListSelectionListener(goTblCards);

        combo.addItem(resource.getString("information"));
        combo.addItem(resource.getString("question"));

        tblInfos.setModel(tblInfosModel);
        scrlPnInfos.setViewportView(tblInfos);
        tblInfosModel.addColumn(resource.getString("ColumnType"));
        tblInfosModel.addColumn(resource.getString("ColumnInfo"));
        tblInfosModel.addColumn(resource.getString("ColumnDate"));
        tblInfosModel.addColumn(resource.getString("ColumnGroup"));
        //add a JComboBox to the first column, so this will always be used, to edit this column
        TableColumn typeColumn = tblInfos.getColumnModel().getColumn(0);
        typeColumn.setCellEditor(new DefaultCellEditor(combo));
        //Listener to keep track of the changes in the Table of the InformationGroups. This will be activated, everytime somehting changes
        TableModelListener listener = new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent tableModelEvent) {
                //is being called everytime something in the tblInfos changes. Then it updates the two tables and search for changes to write them into the stack
                if(tableModelEvent.getType() == TableModelEvent.UPDATE) { //only when user changed something
                    //search for chanes, so they can be change in the stack too.
                    checkForChanges(tableModelEvent.getLastRow());
                    //update tables
                    loadTableInfos(lastInfoGroup);
                    loadTableCards(stack);
                }
            }
        };

        //Set sizes of the columns of the table with the InfoObjects
        tblInfos.getColumnModel().getColumn(0).setPreferredWidth(90);
        tblInfos.getColumnModel().getColumn(1).setMinWidth(200);
        tblInfos.getColumnModel().getColumn(2).setPreferredWidth(90);
        tblInfos.getColumnModel().getColumn(3).setPreferredWidth(50);

        tblInfos.getModel().addTableModelListener(listener);

        //ActionListeners
        ActionListener goDeleteDates = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int answer = JOptionPane.showConfirmDialog(imprend.getFrame(), resource.getString("MsgSureDeleteDates"), resource.getString("MsgSure"), JOptionPane.OK_CANCEL_OPTION);
                if(answer == 0) {
                    //set all dates to 0 (0 = never asked/learned)
                    for (int i = 0; i < stack.getAmountInformationGroups(); i++) {
                        for(int j = 0; j < stack.getInfoGroupById(i).getAmountInformations(); j++) {
                            if(stack.getInfoObjectById(i, j).getType().equals(Imprend.strInfoObjectInfo)) {
                                Information info = (Information) stack.getInfoObjectById(i, j);
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
                if(tblCards.getSelectedRow() == -1) {
                    //no row => no card was selected to delete
                    return;
                }
                int answer = JOptionPane.showConfirmDialog(imprend.getFrame(), resource.getString("MsgSureDeleteCard"), resource.getString("MsgSure"), JOptionPane.OK_CANCEL_OPTION);
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
                if(tblCards.getSelectedRow() == -1 || tblInfos.getSelectedRow() == -1) {
                    //no row => no InfoObject was selected to delete
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
                String[] rowVector = new String[4];
                rowVector[0] = resource.getString("information");
                rowVector[1] = "";
                rowVector[2] = resource.getString("neverAsked");
                rowVector[3] = "";
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

        //Fonts
        lblStack.setFont(imprend.settings.getTitleFont());

        //Layouts
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
        c.ipadx = 0;
        add(pnlTools, c);

        loadTableCards(stack);
    }

    private void loadTableCards(Stack stack) {
        //saving the position the selection was before deletion the content, to restore the position of the selection later
        int selectedRow = tblCards.getSelectedRow();
        //Removing the old content
        for(int i = tblCardsModel.getRowCount()-1; i >= 0 ; i--) {
            tblCardsModel.removeRow(i);
        }
        //loads all InformationGroups (=Cards) into the table
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
        //resore the selected row
        if(selectedRow == -1 || selectedRow >= tblCardsModel.getRowCount()) {
            //if no row was selected before, or the row doesn't exist anymore, the first row will be selected
            setSelection(0, 0);
        } else {
            setSelection(selectedRow, 0);
        }
    }

    private void loadTableInfos(InformationGroup infoGroup) {
        //Removing the old content
        for(int i = tblInfosModel.getRowCount()-1; i >= 0 ; i--) {
            tblInfosModel.removeRow(i);
        }
        //loads all the Information and Questions and other stuff form the given InformationGroup into the table
        String[] rowData = new String[4];
        for(int i = 0; i < infoGroup.getAmountInformations(); i++) {
            switch(infoGroup.getInfoObjectById(i).getType()) {
                case Imprend.strInfoObjectInfo:
                    rowData[0] = resource.getString("information");
                    Information info = (Information) infoGroup.getInfoObjectById(i);
                    if(info.getDate().getTime() == 0) {
                        rowData[2] = resource.getString("neverAsked");
                    } else {
                        rowData[2] = dateFormat.format(info.getDate());
                    }
                    break;
                case Imprend.strInfoObjectQuest:
                    rowData[0] = resource.getString("question");
                    rowData[2] = "-";
                    break;
            }
            rowData[1] = infoGroup.getInfoObjectById(i).getInformation();
            rowData[3] = infoGroup.getInfoObjectById(i).getGroup();
            tblInfosModel.addRow(rowData);
        }
        lastInfoGroup = infoGroup;

    }

    private void checkForChanges(int row) {
        //Checks for only one change made in the table at a specific row and changes them too in the InformationGroup in the stack, so they can later be save
        if(tblInfosModel.getRowCount() != 0 && lastInfoGroup != null) {
            //Detect if something has changed, if so, save the changes
            if(tblInfosModel.getValueAt(row, 0).equals(resource.getString("information")) && lastInfoGroup.getInfoObjectById(row).getType().equals(Imprend.strInfoObjectQuest)) {
                    //transfrom the Question info an Information
                    String question = lastInfoGroup.getInfoObjectById(row).getInformation();
                    int id = lastInfoGroup.getInfoObjectById(row).getId();
                    Information info = new Information();
                    info.setInformation(question);
                    info.setDate(new Date(0));
                    info.setEase(2.5);
                    info.setAmountRepetition(0);
                    info.setOldDate(new Date(0));
                    lastInfoGroup.replaceInfoObject(row, info);
                }else if(tblInfosModel.getValueAt(row, 0).equals(resource.getString("question")) && lastInfoGroup.getInfoObjectById(row).getType().equals(Imprend.strInfoObjectInfo)) {
                    //transform the Information into a Question
                    String information = lastInfoGroup.getInfoObjectById(row).getInformation();
                    int id = lastInfoGroup.getInfoObjectById(row).getId();
                    Question question = new Question();
                    question.setInformation(information);
                    question.setId(id);
                    lastInfoGroup.replaceInfoObject(row, question);
                } else if(!lastInfoGroup.getInfoObjectById(row).getInformation().equals(tblInfosModel.getValueAt(row, 1))) {
                    //Information has changed
                    lastInfoGroup.getInfoObjectById(row).setInformation(String.valueOf(tblInfosModel.getValueAt(row, 1)));
                }else if(lastInfoGroup.getInfoObjectById(row).getType().equals(Imprend.strInfoObjectInfo)) {
                    //Date can only be changed by an Information
                    Information info = (Information) lastInfoGroup.getInfoObjectById(row);
                    if(!dateFormat.format(info.getDate()).equals(tblInfosModel.getValueAt(row, 2))) {
                        //Catch the case, the date is 0 (which is being replace by a text in the table) and doesn't get changed.
                        if(!tblInfosModel.getValueAt(row, 2).equals(resource.getString("neverAsked")) && !info.equals("0")) {
                            try {
                                //to change also the oldDate field, get the difference between the date in the file and the one it was change to.
                                Date newDate = dateFormat.parse(String.valueOf(tblInfosModel.getValueAt(row, 2)));
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
                if(!lastInfoGroup.getInfoObjectById(row).getGroup().equals(tblInfosModel.getValueAt(row, 3))) {
                    lastInfoGroup.getInfoObjectById(row).setGroup(String.valueOf(tblInfosModel.getValueAt(row, 3)));
                }
        }
    }


    /**
     * Details siehe {@link JNavPanel#back(Imprend)}.
     * Gibt immer true zurück, da es nichts selber macht.
     * @param imprend  Imprend als Schnittstelle um z.B. Einstellungen zu erhalten
     * @return true
     */
    @Override
    public boolean back(Imprend imprend) {
        return true;
    }

    /**
     * Details siehe {@link JNavPanel#cleanUp(Imprend)}. Speichert alle Veränderungen im Stapel.
     * @param imprend   Imprend als Schnittstelle um z.B. Einstellungen zu erhalten
     */
    @Override
    public void cleanUp(Imprend imprend) {
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

    /**
     * Setzt die Auswahl schon auf ein bestimtes InfoObject, so das diese direkt in den zwei Tabellen angezeigt wird.
     * Dies wird vom {@link JAddPanel} gebraucht.
     * @param infoGroupId  ID der InformationGroup, die angezeigt werden soll.
     * @param infoObjectId  ID der InfoObjects, das direkt angezeigt werden soll.
     */
    public void setSelection(int infoGroupId, int infoObjectId) {
        tblCards.changeSelection(infoGroupId, 0, false, false);
        tblInfos.changeSelection(infoObjectId, 0, false, false);

    }
}

