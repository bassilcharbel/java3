import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

// mainContact class (same as before)
class mainContact extends JFrame {
    JButton contactBTN,groupBTN;
    JTextArea cyanTXT;
    JLabel titleLBL;
    ContactsFrame contactsFrame;
    GroupMain grpm;

    public mainContact() {
        super("contact managment");
        setLayout(null);
        titleLBL = new JLabel("Gestion des contacts");
        titleLBL.setBounds(150, 0, 130, 50);
        titleLBL.setForeground(Color.blue);
        add(titleLBL);
        groupBTN=new JButton("groupe");
        groupBTN.setBounds(10, 110, 100, 30);
        add(groupBTN);
        contactBTN = new JButton("contacts");
        contactBTN.setBounds(10, 75, 100, 30);
        add(contactBTN);
        cyanTXT = new JTextArea();
        cyanTXT.setBounds(150, 50, 200, 200);
        cyanTXT.setEditable(false);
        cyanTXT.setBackground(Color.CYAN);
        add(cyanTXT);
        grpm=new GroupMain();
        grpm.setSize(400,400);
        grpm.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        contactsFrame = new ContactsFrame();
        contactsFrame.setSize(500, 300);
        contactsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        groupBTN.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		grpm.setVisible(true);
        	}
        });
        contactBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	setVisible(false);
            	contactsFrame.setLocation(430,0);
                contactsFrame.setVisible(true);
            }
        });
    }
}


// ContactsFrame class (Refactored)
class ContactsFrame extends JFrame {
    // Shared data models
    Set<Contact> contactsSet = new TreeSet<>();
    DefaultListModel<String> contactsLSTMDL = new DefaultListModel<>();

    UpdateContactFrame ucf; // Should be initialized when needed
    AddNewContactFrame nwctf; // Should be initialized when needed
    JList<String> contactsInfoLST;
    JTextArea cyanTXT, searchTXT;
    JButton srtFnBTN, srtLnBTN, srtCyBTN, newCBTN, viewBTN, updateBTN, deleteBTN, readBTN, searchBTN; 
    JLabel contactLBL, gestionCLBL, searchLBL;
    JScrollPane scrollPane; // This was declared but not added to the frame in the original code.

    public ContactsFrame() {
        setTitle("contacts interface");
        setLayout(null);

        readBTN = new JButton("read");
        readBTN.setBounds(10, 10, 100, 25);
        add(readBTN);

        updateBTN = new JButton("update");
        updateBTN.setBounds(320, 225, 75, 25);
        add(updateBTN);

        deleteBTN = new JButton("delete");
        deleteBTN.setBounds(415, 225, 75, 25);
        add(deleteBTN);

        viewBTN = new JButton("view");
        viewBTN.setBounds(225, 225, 75, 25);
        add(viewBTN);

        contactLBL = new JLabel("contacts");
        contactLBL.setBounds(5, 25, 150, 30);
        contactLBL.setForeground(Color.red);
        add(contactLBL);

        srtFnBTN = new JButton("Sort by first name");
        srtFnBTN.setBounds(5, 50, 150, 30);
        add(srtFnBTN);

        srtLnBTN = new JButton("Sort by last name");
        srtLnBTN.setBounds(5, 100, 150, 30);
        add(srtLnBTN);

        srtCyBTN = new JButton("Sort by city name");
        srtCyBTN.setBounds(5, 150, 150, 30);
        add(srtCyBTN);

        newCBTN = new JButton("add new contact");
        newCBTN.setBounds(5, 200, 150, 30);
        add(newCBTN);

        cyanTXT = new JTextArea();
        cyanTXT.setEditable(false);
        cyanTXT.setBackground(Color.CYAN);
        cyanTXT.setBounds(175, 25, 25, 300);
        add(cyanTXT);

        gestionCLBL = new JLabel("gestion des contacts");
        gestionCLBL.setForeground(Color.blue);
        gestionCLBL.setBounds(175, 5, 150, 25);
        add(gestionCLBL);

        searchLBL = new JLabel("search");
        searchLBL.setBounds(230, 30, 50, 25);
        add(searchLBL);

        contactsInfoLST = new JList<>(contactsLSTMDL); 
        contactsInfoLST.setVisibleRowCount(5);
        contactsInfoLST.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // JScrollPane for contactsInfoLST is good practice
        scrollPane = new JScrollPane(contactsInfoLST);
        scrollPane.setBounds(230, 70, 200, 150);
        add(scrollPane);

        searchTXT = new JTextArea();
        searchTXT.setBounds(290, 35, 100, 15);
        add(searchTXT);

        searchBTN = new JButton("Search");
        searchBTN.setBounds(400, 33, 80, 20); // Position it next to searchTXT
        add(searchBTN);

        // ActionListener for Sort by First Name
        srtFnBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (contactsSet == null || contactsSet.isEmpty()) {
                    return;
                }
                List<Contact> sortedList = new ArrayList<>(contactsSet);
                Collections.sort(sortedList, Contact.FirstNameComparator);
                contactsSet.clear();
                contactsSet.addAll(sortedList);
                refreshContactsListModel();
            }
        });

        // ActionListener for Sort by Last Name
        srtLnBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (contactsSet == null || contactsSet.isEmpty()) {
                    return;
                }
                List<Contact> sortedList = new ArrayList<>(contactsSet);
                Collections.sort(sortedList, Contact.LastNameComparator);
                contactsSet.clear();
                contactsSet.addAll(sortedList);
                refreshContactsListModel();
            }
        });

        // ActionListener for Sort by City
        srtCyBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (contactsSet == null || contactsSet.isEmpty()) {
                    return;
                }
                List<Contact> sortedList = new ArrayList<>(contactsSet);
                Collections.sort(sortedList, Contact.CityComparator);
                contactsSet.clear();
                contactsSet.addAll(sortedList);
                refreshContactsListModel();
            }
        });

        searchBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchTerm = searchTXT.getText().trim().toLowerCase();
                contactsLSTMDL.clear(); // Clear the JList model

                if (searchTerm.isEmpty()) {
                    // If search term is empty, refresh with all contacts from the original set
                    // The existing refreshContactsListModel() iterates contactsSet, which is fine.
                    // However, refreshContactsListModel itself clears and repopulates from contactsSet.
                    // To avoid double clearing or issues if refreshContactsListModel changes,
                    // it's safer to just call it.
                    refreshContactsListModel(); // This will show all contacts in their current sort order from contactsSet
                } else {
                    List<Contact> filteredContacts = new ArrayList<>();
                    for (Contact c : contactsSet) {
                        if (c.getFirstName() != null && c.getFirstName().toLowerCase().contains(searchTerm)) {
                            filteredContacts.add(c);
                        }
                    }
                    // Optional: Sort filteredContacts if a specific order is desired for search results
                    // For now, they will appear in the order they are found in contactsSet (which is a TreeSet)
                    // Collections.sort(filteredContacts); // Example if you want natural sort of filtered results

                    for (Contact c : filteredContacts) {
                        if (!c.getPhoneNumbers().isEmpty()) {
                            contactsLSTMDL.addElement(c.getFirstName() + " " + c.getLastName() + " - " + c.getCity());
                        } else {
                            contactsLSTMDL.addElement(c.getFirstName() + " " + c.getLastName() + " - " + c.getCity() + " (No Numbers)");
                        }
                    }
                }
            }
        });
        
        // Load initial data when ContactsFrame is created
        loadContactsFromFile();
        refreshContactsListModel();

        readBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadContactsFromFile();
                refreshContactsListModel();
            }
        });

        newCBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nwctf = new AddNewContactFrame(contactsSet, contactsLSTMDL, null, -1, true);
                nwctf.setSize(450, 480); 
                nwctf.setLocationRelativeTo(ContactsFrame.this); 
                nwctf.setVisible(true);
            }
        });
        
        updateBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIdx = contactsInfoLST.getSelectedIndex();
                if (selectedIdx != -1) {
                    Contact selectedContact = null;
                    // This assumes contactsSet is sorted in the same way contactsLSTMDL is populated
                    // For TreeSet, iteration order is guaranteed.
                    int i = 0;
                    for (Contact c : contactsSet) { 
                        if (i == selectedIdx) {
                            selectedContact = c;
                            break;
                        }
                        i++;
                    }

                    if (selectedContact != null) {
                        ucf = new UpdateContactFrame(contactsSet, contactsLSTMDL, selectedContact, selectedIdx);
                        ucf.setSize(450, 480); 
                        ucf.setLocationRelativeTo(ContactsFrame.this); 
                        ucf.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(ContactsFrame.this, "Could not retrieve selected contact details. The list might be out of sync or the selection logic needs adjustment.");
                    }
                } else {
                    JOptionPane.showMessageDialog(ContactsFrame.this, "Please select a contact to update.");
                }
            }
        });
    } // End of ContactsFrame Constructor

    private void refreshContactsListModel() {
        contactsLSTMDL.clear();
        for (Contact c : contactsSet) { // Iteration order from TreeSet is sorted
            if (!c.getPhoneNumbers().isEmpty()) {
                contactsLSTMDL.addElement(c.getFirstName() + " " + c.getLastName() + " - " + c.getCity());
            } else {
                contactsLSTMDL.addElement(c.getFirstName() + " " + c.getLastName() + " - " + c.getCity() + " (No Numbers)");
            }
        }
    }

    @SuppressWarnings("unchecked") // Suppress warning for cast from Object to Set/List
    private void loadContactsFromFile() {
        File f = new File("contact.dat");
        if (!f.exists()) {
            return; 
        }
        try (FileInputStream fis = new FileInputStream(f);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object loadedData = ois.readObject();
            contactsSet.clear(); // Clear existing data first
            if (loadedData instanceof Set) { 
                contactsSet.addAll((Set<Contact>) loadedData);
            } else if (loadedData instanceof List) { // Compatibility for old List format
                contactsSet.addAll((List<Contact>) loadedData);
                // Optionally, could re-save in Set format here if desired
                // writeCurrentContactsToFile(); 
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading contacts: " + e.getMessage());
        }
    }
} // End of ContactsFrame Class

// AddNewContactFrame class (Refactored)
class AddNewContactFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    JLabel fnameLBL, lnameLBL, ctyLBL, gestionLBL, newLBL, phnLBL, addLBL;
    JTextArea fnTXT, lnTXT, ctyTXT;
    JCheckBox grpCHK, famCHK, FrndCHK, workCHK;
    JButton saveBTN, cancelBTN, nwBTN, writeContactBTN, readBTN;
    JTable phonenbrTBL;
    DefaultTableModel ctsModel;
    Set<Contact> contactsSet; // Shared
    DefaultListModel<String> contactsLSTMDL; // Shared
    Contact oldContact; // Used if editing
    int selectedIndex;  // Used if editing
    boolean newRec;     // True if adding new, false if editing

    public AddNewContactFrame(Set<Contact> sharedContactsSet, DefaultListModel<String> sharedContactsLSTMDL,
                              Contact oldContact, int index, boolean newRec) {
        this.contactsSet = sharedContactsSet;
        this.contactsLSTMDL = sharedContactsLSTMDL;
        this.oldContact = oldContact;
        this.selectedIndex = index;
        this.newRec = newRec;

        setTitle(newRec ? "Add New Contact" : "Edit Contact");
        setLayout(null);
        setSize(450, 480); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        gestionLBL = new JLabel("Gestion des contacts");
        gestionLBL.setBounds(150, 20, 150, 15);
        gestionLBL.setForeground(Color.blue);
        add(gestionLBL);
        
        // These buttons might be re-thought; primary persistence should be via ContactsFrame.
        // For now, they write the shared set.
        writeContactBTN = new JButton("Save All to File"); 
        writeContactBTN.setBounds(15, 295, 150, 25);
        // add(writeContactBTN); // Optionally remove if centralizing save
        
        readBTN = new JButton("Reload All from File"); 
        readBTN.setBounds(15, 325, 150, 25);
        // add(readBTN); // Optionally remove

        newLBL = new JLabel(newRec ? "New Contact Details" : "Edit Contact Details");
        newLBL.setBounds(15, 50, 150, 15);
        add(newLBL);
        fnameLBL = new JLabel("First Name");
        fnameLBL.setFont(new Font("Serif", Font.BOLD, 12));
        fnameLBL.setBounds(175, 40, 80, 20);
        add(fnameLBL);
        fnTXT = new JTextArea(); // Corrected: fnTXT here
        fnTXT.setBounds(265, 40, 150, 20);
        add(fnTXT); // Add fnTXT

        lnameLBL = new JLabel("Last Name");
        lnameLBL.setFont(new Font("Serif", Font.BOLD, 12));
        lnameLBL.setBounds(175, 70, 80, 20);
        add(lnameLBL);
        lnTXT = new JTextArea(); // Corrected: lnTXT here
        lnTXT.setBounds(265, 70, 150, 20);
        add(lnTXT); // Add lnTXT
        ctyLBL = new JLabel("City");
        ctyLBL.setBounds(175, 100, 80, 20);
        add(ctyLBL);
        ctyTXT = new JTextArea();
        ctyTXT.setBounds(265, 100, 150, 20);
        add(ctyTXT);
        phnLBL = new JLabel("Phone Numbers");
        phnLBL.setBounds(175, 130, 150, 20);
        add(phnLBL);
        ctsModel = new DefaultTableModel();
        ctsModel.addColumn("Region Code");
        ctsModel.addColumn("Phone Number");
        phonenbrTBL = new JTable(ctsModel);
        JScrollPane sp = new JScrollPane(phonenbrTBL);
        sp.setBounds(175, 160, 240, 120);
        add(sp);
        addLBL = new JLabel("Add the contact to group");
        addLBL.setBounds(175, 290, 200, 20);
        add(addLBL);
        grpCHK = new JCheckBox("No Group");
        grpCHK.setSelected(true);
        grpCHK.setBounds(175, 320, 100, 20);
        add(grpCHK);
        famCHK = new JCheckBox("Family");
        famCHK.setBounds(280, 320, 100, 20);
        add(famCHK);
        FrndCHK = new JCheckBox("Friends");
        FrndCHK.setBounds(175, 345, 100, 20);
        add(FrndCHK);
        workCHK = new JCheckBox("Co-Workers");
        workCHK.setBounds(280, 345, 100, 20);
        add(workCHK);
        saveBTN = new JButton(newRec ? "Save New" : "Save Changes");
        saveBTN.setBounds(175, 380, 120, 25);
        add(saveBTN);
        nwBTN = new JButton("Add Phone Row"); 
        nwBTN.setBounds(305, 380, 130, 25); // Adjusted width
        add(nwBTN);
        cancelBTN = new JButton("Close"); 
        cancelBTN.setBounds(175, 410, 120, 25); 
        add(cancelBTN);

        if (!newRec && this.oldContact != null) {
            prefillForm();
        } else { // For new contact
             ctsModel.addRow(new String[] { "", "" }); 
        }

        saveBTN.addActionListener(e -> saveContact());
        cancelBTN.addActionListener(e -> dispose());
        nwBTN.addActionListener(e -> { 
            ctsModel.addRow(new String[] { "", "" });
        });
        // Action listeners for writeContactBTN and readBTN (if kept)
        writeContactBTN.addActionListener(e -> writeContactsToFile()); 
        readBTN.addActionListener(e -> { 
            readContactsFromFile(); 
            if (!newRec && oldContact != null) prefillForm(); 
            else clearFormForNew(); // If newRec, ensure form is for new entry
        });
    }

    private void prefillForm() {
        if (oldContact == null) return;
        List<PhoneNumber> pns = oldContact.getPhoneNumbers();
        
        ctsModel.setRowCount(0);
        // fnTXT.setText(""); lnTXT.setText(""); ctyTXT.setText(""); // Cleared before setting
        fnTXT.setText(oldContact.getFirstName());
        lnTXT.setText(oldContact.getLastName());
        ctyTXT.setText(oldContact.getCity());

        for (PhoneNumber pn : pns) {
            ctsModel.addRow(new Object[]{pn.getRegion(), pn.getPnbr()});
        }
        
        // Pre-select group checkboxes
        if (oldContact != null) { // Ensure oldContact is not null
            famCHK.setSelected(oldContact.isInGroup("Family"));
            FrndCHK.setSelected(oldContact.isInGroup("Friends"));
            workCHK.setSelected(oldContact.isInGroup("Co-Workers"));
            grpCHK.setSelected(oldContact.getGroups().isEmpty()); // Simpler logic for "No Group"
        } else {
            // Default state if oldContact is somehow null
            grpCHK.setSelected(true);
            famCHK.setSelected(false);
            FrndCHK.setSelected(false);
            workCHK.setSelected(false);
        }
    }

    private void clearFormForNew() { 
        fnTXT.setText(""); lnTXT.setText(""); ctyTXT.setText("");
        ctsModel.setRowCount(0);
        ctsModel.addRow(new String[] { "", "" }); 
        grpCHK.setSelected(true); famCHK.setSelected(false);
        FrndCHK.setSelected(false); workCHK.setSelected(false);
        this.oldContact = null; 
        this.newRec = true; // Ensure it's marked as new
        setTitle("Add New Contact");
        saveBTN.setText("Save New");
    }

    public void saveContact() { // Changed to public for testing
        String firstName = fnTXT.getText().trim();
        String lastName = lnTXT.getText().trim();
        String city = ctyTXT.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || city.isEmpty()) {
            JOptionPane.showMessageDialog(this, "First Name, Last Name, and City are required.");
            return;
        }
        TreeSet<PhoneNumber> pnbrSet = new TreeSet<>();
        for (int i = 0; i < ctsModel.getRowCount(); i++) {
            String region = (String) ctsModel.getValueAt(i, 0);
            String pnbr = (String) ctsModel.getValueAt(i, 1);
            if (pnbr != null && !pnbr.trim().isEmpty()) {
                pnbrSet.add(new PhoneNumber(pnbr, region));
            }
        }
        if (pnbrSet.isEmpty()) {
            JOptionPane.showMessageDialog(this, "At least one phone number is required.");
            return;
        }
        Contact currentContact = new Contact(firstName, lastName, city);
        for (PhoneNumber p : pnbrSet) {
            currentContact.addPhoneNumber(p);
        }

        // Add selected groups
        if (famCHK.isSelected()) {
            currentContact.addGroup("Family");
        }
        if (FrndCHK.isSelected()) {
            currentContact.addGroup("Friends");
        }
        if (workCHK.isSelected()) {
            currentContact.addGroup("Co-Workers");
        }
        // grpCHK (No Group) is handled by the absence of other selections.

        boolean success = false;
        String listDisplayString = firstName + " " + lastName + " - " + city;

        if (newRec) { 
            if (contactsSet.add(currentContact)) {
                // Use the consistent display string: firstName + " " + lastName + " - " + city
                contactsLSTMDL.addElement(listDisplayString); 
                JOptionPane.showMessageDialog(this, "Contact saved successfully.");
                success = true;
                clearFormForNew(); 
            } else {
                JOptionPane.showMessageDialog(this, "Duplicate contact. Not added.");
            }
        } else { // Editing existing contact
            if (oldContact != null && contactsSet.remove(oldContact)) {
                if (contactsSet.add(currentContact)) {
                    // Use the consistent display string: firstName + " " + lastName + " - " + city
                    contactsLSTMDL.setElementAt(listDisplayString, selectedIndex);
                    JOptionPane.showMessageDialog(this, "Contact updated successfully.");
                    success = true;
                } else { 
                    contactsSet.add(oldContact); // Rollback
                    JOptionPane.showMessageDialog(this, "Duplicate contact. Update failed.");
                }
            } else {
                 JOptionPane.showMessageDialog(this, "Original contact not found or error removing it.");
            }
        }
        if (success) {
            writeContactsToFile(); // Persist the entire shared set
            if (!newRec) dispose(); // Close window if it was an update
        }
    }

    private void writeContactsToFile() { // Operates on the shared contactsSet
        try (FileOutputStream fos = new FileOutputStream("contact.dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(contactsSet);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving contacts: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void readContactsFromFile() { 
        File f = new File("contact.dat");
        if (!f.exists()) {
            JOptionPane.showMessageDialog(this, "No contacts file found.");
            return;
        }
        try (FileInputStream fis = new FileInputStream(f);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object loadedData = ois.readObject();
            contactsSet.clear(); 
            if (loadedData instanceof Set) {
                contactsSet.addAll((Set<Contact>) loadedData);
            } else if (loadedData instanceof List) { // Compatibility
                contactsSet.addAll((List<Contact>) loadedData);
            }
            // Refresh the shared list model to reflect changes
            refreshSharedContactsListModel();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading contacts: " + e.getMessage());
        }
    }

    // Utility method to refresh the shared contactsLSTMDL from the shared contactsSet
    private void refreshSharedContactsListModel() {
        contactsLSTMDL.clear();
        for (Contact c : contactsSet) {
            if (!c.getPhoneNumbers().isEmpty()) {
                contactsLSTMDL.addElement(c.getFirstName() + " " + c.getLastName() + " - " + c.getCity());
            } else {
                contactsLSTMDL.addElement(c.getFirstName() + " " + c.getLastName() + " - " + c.getCity() + " (No Numbers)");
            }
        }
    }
}


// UpdateContactFrame class (Refactored)
class UpdateContactFrame extends JFrame {
    Set<Contact> contactsSet; 
    DefaultListModel<String> contactsLSTMDL; 
    Contact contactToUpdate;
    int contactIndex;
    JLabel fnameLBL, lnameLBL, ctyLBL, updateLBL, newLBL, phnLBL, addLBL;
    JTextArea fnTXT, lnTXT, ctyTXT;
    JCheckBox grpCHK, famCHK, FrndCHK, workCHK;
    JButton saveBTN, cancelBTN, addPhoneRowBTN; // Added addPhoneRowBTN
    JTable phonenbrTBL;
    DefaultTableModel ctsModel;

    public UpdateContactFrame(Set<Contact> sharedContactsSet, DefaultListModel<String> sharedContactsLSTMDL, Contact contactToUpdate, int contactIndex) {
        this.contactsSet = sharedContactsSet;
        this.contactsLSTMDL = sharedContactsLSTMDL;
        this.contactToUpdate = contactToUpdate;
        this.contactIndex = contactIndex;

        setTitle("Update Contact");
        setLayout(null);
        setSize(450, 480); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        updateLBL = new JLabel("Update Contact Details");
        updateLBL.setBounds(150, 20, 200, 15);
        updateLBL.setForeground(Color.blue);
        add(updateLBL);

        String displayName = "Contact";
        if (contactToUpdate != null) {
            displayName = contactToUpdate.getFirstName() + " " + contactToUpdate.getLastName();
        }
        newLBL = new JLabel("Editing: " + displayName);
        newLBL.setBounds(15, 50, 250, 15);
        add(newLBL);

        fnameLBL = new JLabel("First Name");
        fnameLBL.setFont(new Font("Serif", Font.BOLD, 12));
        fnameLBL.setBounds(175, 80, 80, 20); 
        add(fnameLBL);
        fnTXT = new JTextArea();
        fnTXT.setBounds(265, 80, 150, 20); 
        add(fnTXT);

        lnameLBL = new JLabel("Last Name");
        lnameLBL.setFont(new Font("Serif", Font.BOLD, 12));
        lnameLBL.setBounds(175, 110, 80, 20); 
        add(lnameLBL);
        lnTXT = new JTextArea();
        lnTXT.setBounds(265, 110, 150, 20); 
        add(lnTXT);

        ctyLBL = new JLabel("City");
        ctyLBL.setBounds(175, 140, 80, 20); 
        add(ctyLBL);
        ctyTXT = new JTextArea();
        ctyTXT.setBounds(265, 140, 150, 20); 
        add(ctyTXT);

        phnLBL = new JLabel("Phone Numbers");
        phnLBL.setBounds(175, 170, 150, 20); 
        add(phnLBL);

        ctsModel = new DefaultTableModel();
        ctsModel.addColumn("Region Code");
        ctsModel.addColumn("Phone Number");
        phonenbrTBL = new JTable(ctsModel);
        JScrollPane sp = new JScrollPane(phonenbrTBL);
        sp.setBounds(175, 200, 240, 100); 
        add(sp);
        
        addPhoneRowBTN = new JButton("Add Phone Row");
        addPhoneRowBTN.setBounds(175, 305, 130, 25);
        add(addPhoneRowBTN);

        addLBL = new JLabel("Contact Groups:");
        addLBL.setBounds(175, 335, 200, 20);
        add(addLBL);
        grpCHK = new JCheckBox("No Group");
        grpCHK.setBounds(175, 360, 100, 20);
        add(grpCHK);
        famCHK = new JCheckBox("Family");
        famCHK.setBounds(280, 360, 100, 20);
        add(famCHK);
        FrndCHK = new JCheckBox("Friends");
        FrndCHK.setBounds(175, 385, 100, 20);
        add(FrndCHK);
        workCHK = new JCheckBox("Co-Workers");
        workCHK.setBounds(280, 385, 100, 20);
        add(workCHK);
        
        saveBTN = new JButton("Save Changes");
        saveBTN.setBounds(175, 415, 120, 25);
        add(saveBTN);
        cancelBTN = new JButton("Cancel");
        cancelBTN.setBounds(305, 415, 110, 25);
        add(cancelBTN);

        prefillUpdateForm();

        saveBTN.addActionListener(e -> saveUpdatedContact());
        cancelBTN.addActionListener(e -> dispose());
        addPhoneRowBTN.addActionListener(e -> ctsModel.addRow(new String[]{"", ""}));
    }

    private void prefillUpdateForm() {
        if (contactToUpdate == null) return;
        List<PhoneNumber> pns = contactToUpdate.getPhoneNumbers();
        ctsModel.setRowCount(0);

        fnTXT.setText(contactToUpdate.getFirstName());
        lnTXT.setText(contactToUpdate.getLastName());
        ctyTXT.setText(contactToUpdate.getCity());

        if (!pns.isEmpty()) {
            for (PhoneNumber pn : pns) {
                ctsModel.addRow(new Object[]{pn.getRegion(), pn.getPnbr()});
            }
        }
        // No need for the 'else' block to set names/city from phone numbers as it's done directly from contactToUpdate above
        
        // Pre-select group checkboxes
        if (contactToUpdate != null) { // Ensure contactToUpdate is not null
            famCHK.setSelected(contactToUpdate.isInGroup("Family"));
            FrndCHK.setSelected(contactToUpdate.isInGroup("Friends"));
            workCHK.setSelected(contactToUpdate.isInGroup("Co-Workers"));
            grpCHK.setSelected(contactToUpdate.getGroups().isEmpty()); // "No Group" if no specific groups
        } else {
            // Default state if contactToUpdate is somehow null (should not happen here)
            grpCHK.setSelected(true);
            famCHK.setSelected(false);
            FrndCHK.setSelected(false);
            workCHK.setSelected(false);
        }
    }

    public void saveUpdatedContact() { // Changed to public for testing
        String firstName = fnTXT.getText().trim();
        String lastName = lnTXT.getText().trim();
        String city = ctyTXT.getText().trim();
        if (firstName.isEmpty() || lastName.isEmpty() || city.isEmpty()) {
            JOptionPane.showMessageDialog(this, "First Name, Last Name, and City are required.");
            return;
        }
        TreeSet<PhoneNumber> pnbrSet = new TreeSet<>();
        for (int i = 0; i < ctsModel.getRowCount(); i++) {
            String region = (String) ctsModel.getValueAt(i, 0);
            String pnbr = (String) ctsModel.getValueAt(i, 1);
            if (pnbr != null && !pnbr.trim().isEmpty()) {
                pnbrSet.add(new PhoneNumber(pnbr, region));
            }
        }
        if (pnbrSet.isEmpty()) {
            JOptionPane.showMessageDialog(this, "At least one phone number is required.");
            return;
        }
        Contact updatedContact = new Contact(firstName, lastName, city);
        for (PhoneNumber p : pnbrSet) {
            updatedContact.addPhoneNumber(p);
        }

        // Add selected groups
        // Since updatedContact is a new Contact object, its 'groups' field is a new HashSet.
        // No need to clear groups explicitly here.
        if (famCHK.isSelected()) {
            updatedContact.addGroup("Family");
        }
        if (FrndCHK.isSelected()) {
            updatedContact.addGroup("Friends");
        }
        if (workCHK.isSelected()) {
            updatedContact.addGroup("Co-Workers");
        }
        // grpCHK (No Group) is handled by the absence of other selections.

        // Critical: remove the original instance, then add the new one.
        if (contactsSet.remove(contactToUpdate)) {
            if (contactsSet.add(updatedContact)) {
                contactsLSTMDL.setElementAt(firstName + " " + lastName + " - " + city, contactIndex);
                JOptionPane.showMessageDialog(this, "Contact updated successfully.");
                writeContactsToFile(); // Persist the entire shared set
                dispose();
            } else {
                contactsSet.add(contactToUpdate); // Rollback: re-add original if new one is duplicate
                JOptionPane.showMessageDialog(this, "Update failed. The updated contact details might conflict with another existing contact.");
            }
        } else {
            // This case means contactToUpdate (the original object) was not found in contactsSet.
            // This could happen if its hashCode changed or if it wasn't in the set to begin with.
            // Or if equals method has issues.
            JOptionPane.showMessageDialog(this, "Error: Original contact could not be found in the set for update. Please ensure the contact list is current.");
        }
    }

    private void writeContactsToFile() {
        try (FileOutputStream fos = new FileOutputStream("contact.dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(contactsSet);
        } catch (IOException e) {
            e.printStackTrace();
            // Avoid showing JOptionPane here if frame is disposing, could lead to issues.
            // Log error or handle more gracefully.
        }
    }
}

// GroupMain, AddNewGroup, UpdateGroup classes (assuming no changes for this subtask)
class GroupMain extends JFrame{
	AddNewGroup agrp;
	UpdateGroup ugrp;
	JButton addBTN,updateBTN,deleteBTN;
	JTable groupInfoTBL,contactInfoTBL;
	JLabel titleLBL,tableLBL,grpLBL;
	DefaultTableModel grpiModel,ctiModel;
	
	public GroupMain() {
		setTitle("group main");
		setLayout(null);
		agrp=new AddNewGroup();
		ugrp=new UpdateGroup();
		titleLBL=new JLabel("Gestion des contact");
		titleLBL.setBounds(150,5,150,25);
		add(titleLBL);
		grpiModel=new DefaultTableModel();
		grpiModel.addColumn("Group name ");
		grpiModel.addColumn("Number of contacts");
		groupInfoTBL=new JTable(grpiModel);
		add(groupInfoTBL);
		JScrollPane sp = new JScrollPane(groupInfoTBL);
		sp.setBounds(150,70,220,120);
		add(sp);
		ctiModel=new DefaultTableModel();
		ctiModel.addColumn("Contact name");
		ctiModel.addColumn("contact city");
		contactInfoTBL=new JTable(ctiModel);
		add(contactInfoTBL);
		JScrollPane s = new JScrollPane(contactInfoTBL);
		s.setBounds(150,220,220,120);
		add(s);
		tableLBL=new JLabel("List of groups");
		tableLBL.setBounds(170,40,100,20);
		add(tableLBL);
		addBTN=new JButton("add new group");
		addBTN.setBounds(10,50,130,25);
		add(addBTN);
		deleteBTN=new JButton("delete");
		deleteBTN.setBounds(150,350,75,25);
		add(deleteBTN);
		updateBTN=new JButton("update");
		updateBTN.setBounds(245,350,75,25);
		add(updateBTN);
		updateBTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ugrp.setVisible(true);
				ugrp.setSize(400,400);
			}
		});
		addBTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				agrp.setVisible(true);
				agrp.setSize(400,400);
			}
		});
	}
}

class AddNewGroup extends JFrame{
	JTable ctsiTBL;
	JLabel headerLBL,updLBL,grpLBL,descLBL;
	JTextField grpTXT,descTXT;
	JButton saveBTN,cancelBTN;
	DefaultTableModel ctsModel;
	public AddNewGroup() {
		setTitle("add new group");
		setLayout(null);
		headerLBL=new JLabel("Gestion des contact");
		headerLBL.setBounds(200,20,150,25);
		add(headerLBL);
		grpLBL=new JLabel("Group name");
		grpLBL.setBounds(150,50,75,25);
		add(grpLBL);
		descLBL=new JLabel("description");
		descLBL.setBounds(150,80,75,25);
		add(descLBL);
		grpTXT=new JTextField(190);
		grpTXT.setBounds(185,50,190,25);
		add(grpTXT);
		descTXT=new JTextField(150);
		descTXT.setBounds(220,80,155,25);
		add(descTXT);
		ctsModel=new DefaultTableModel();
		ctsModel.addColumn("contact name");
		ctsModel.addColumn("City");
		ctsModel.addColumn("Add to group");
		ctsiTBL=new JTable(ctsModel);
		add(ctsiTBL);
		JScrollPane sp = new JScrollPane(ctsiTBL);
		sp.setBounds(100,125,260,120);
		add(sp);
		saveBTN=new JButton("save");
		saveBTN.setBounds(150,320,75,20);
		add(saveBTN);
		cancelBTN=new JButton("cancel");
		cancelBTN.setBounds(250,320,75,20);
		add(cancelBTN);
	}
}

class UpdateGroup extends JFrame{
	JTable ctsiTBL;
	JLabel headerLBL,updLBL,grpLBL,descLBL;
	JTextField grpTXT,descTXT;
	JButton saveBTN,cancelBTN;
	DefaultTableModel ctsModel;
	public UpdateGroup() {
		setTitle("Update Group"); // Corrected Title
		setLayout(null);
		headerLBL=new JLabel("Gestion des contact");
		headerLBL.setBounds(200,20,150,25);
		add(headerLBL);
		grpLBL=new JLabel("Group name");
		grpLBL.setBounds(150,50,75,25);
		add(grpLBL);
		descLBL=new JLabel("description");
		descLBL.setBounds(150,80,75,25);
		add(descLBL);
		grpTXT=new JTextField(190);
		grpTXT.setBounds(185,50,190,25);
		add(grpTXT);
		descTXT=new JTextField(150);
		descTXT.setBounds(220,80,155,25);
		add(descTXT);
		ctsModel=new DefaultTableModel();
		ctsModel.addColumn("contact name");
		ctsModel.addColumn("City");
		ctsModel.addColumn("Add to group");
		ctsiTBL=new JTable(ctsModel);
		add(ctsiTBL);
		JScrollPane sp = new JScrollPane(ctsiTBL);
		sp.setBounds(100,125,260,120);
		add(sp);
		saveBTN=new JButton("save");
		saveBTN.setBounds(150,320,75,20);
		add(saveBTN);
		cancelBTN=new JButton("cancel");
		cancelBTN.setBounds(250,320,75,20);
		add(cancelBTN);
	}
}

// PrjctDemo class (main method)
public class PrjctDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainContact c = new mainContact();
                c.setSize(400, 300);
                c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                c.setVisible(true);
            }
        });
    }
}
