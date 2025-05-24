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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable; // Contact and PhoneNumber are Serializable
import java.util.List;    // Used by Contact.getPhoneNumbers()
import java.util.Set;
import java.util.TreeSet; // Used by pnbrSet

// Assuming Contact and PhoneNumber are in the default package.

public class AddNewContactFrame extends JFrame {
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
        fnTXT = new JTextArea(); 
        fnTXT.setBounds(265, 40, 150, 20);
        add(fnTXT); 

        lnameLBL = new JLabel("Last Name");
        lnameLBL.setFont(new Font("Serif", Font.BOLD, 12));
        lnameLBL.setBounds(175, 70, 80, 20);
        add(lnameLBL);
        lnTXT = new JTextArea(); 
        lnTXT.setBounds(265, 70, 150, 20);
        add(lnTXT); 
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
        nwBTN.setBounds(305, 380, 130, 25); 
        add(nwBTN);
        cancelBTN = new JButton("Close"); 
        cancelBTN.setBounds(175, 410, 120, 25); 
        add(cancelBTN);

        if (!newRec && this.oldContact != null) {
            prefillForm();
        } else { 
             ctsModel.addRow(new String[] { "", "" }); 
        }

        saveBTN.addActionListener(e -> saveContact());
        cancelBTN.addActionListener(e -> dispose());
        nwBTN.addActionListener(e -> { 
            ctsModel.addRow(new String[] { "", "" });
        });
        
        // Action listeners for writeContactBTN and readBTN (if kept)
        // These are potentially problematic as they operate on shared state
        // without necessarily informing the main ContactsFrame to refresh its view.
        writeContactBTN.addActionListener(e -> writeContactsToFile()); 
        readBTN.addActionListener(e -> { 
            readContactsFromFile(); 
            if (!newRec && oldContact != null) prefillForm(); 
            else clearFormForNew(); 
        });
    }

    private void prefillForm() {
        if (oldContact == null) return;
        List<PhoneNumber> pns = oldContact.getPhoneNumbers();
        
        fnTXT.setText(""); lnTXT.setText(""); ctyTXT.setText(""); // Clear fields
        ctsModel.setRowCount(0); // Clear table
        if (pns != null && !pns.isEmpty()) { 
            PhoneNumber pn = pns.get(0); // Use first phone number for primary contact info
             if (pn != null) { 
                fnTXT.setText(pn.getFirstName());
                lnTXT.setText(pn.getLastName());
                ctyTXT.setText(pn.getCity());
            }
             for (PhoneNumber phoneNum : pns) { 
                if (phoneNum != null) { 
                    ctsModel.addRow(new Object[]{phoneNum.getRegion(), phoneNum.getPnbr()});
                }
            }
        }
        // TODO: Group checkbox prefill logic if groups are part of Contact object
    }

    private void clearFormForNew() { 
        fnTXT.setText(""); lnTXT.setText(""); ctyTXT.setText("");
        ctsModel.setRowCount(0);
        ctsModel.addRow(new String[] { "", "" }); 
        grpCHK.setSelected(true); famCHK.setSelected(false);
        FrndCHK.setSelected(false); workCHK.setSelected(false);
        this.oldContact = null; 
        this.newRec = true; 
        setTitle("Add New Contact");
        saveBTN.setText("Save New");
    }

    public void saveContact() { 
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
                pnbrSet.add(new PhoneNumber(pnbr, city, firstName, lastName, region == null ? "" : region));
            }
        }
        if (pnbrSet.isEmpty()) {
            JOptionPane.showMessageDialog(this, "At least one phone number is required.");
            return;
        }
        Contact currentContact = new Contact(); // Assuming Contact has a default constructor
        for (PhoneNumber p : pnbrSet) {
            currentContact.addPhoneNumber(p);
        }
        // TODO: Set group information on currentContact if applicable from checkboxes

        boolean success = false;
        String listDisplayString = firstName + " " + lastName + " - " + city;

        if (newRec) { 
            if (contactsSet.add(currentContact)) {
                if (contactsLSTMDL != null) { // Ensure list model is available
                    contactsLSTMDL.addElement(listDisplayString); 
                }
                JOptionPane.showMessageDialog(this, "Contact saved successfully.");
                success = true;
                clearFormForNew(); 
            } else {
                JOptionPane.showMessageDialog(this, "Duplicate contact. Not added.");
            }
        } else { 
            if (oldContact != null && contactsSet.remove(oldContact)) {
                if (contactsSet.add(currentContact)) {
                     if (contactsLSTMDL != null) { // Ensure list model is available
                        contactsLSTMDL.setElementAt(listDisplayString, selectedIndex);
                     }
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
            writeContactsToFile(); 
            if (!newRec) dispose(); 
        }
    }

    private void writeContactsToFile() { 
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
                 for (Object obj : (Set<?>) loadedData) { 
                    if (obj instanceof Contact) {
                        contactsSet.add((Contact) obj);
                    }
                }
            } else if (loadedData instanceof List) { 
                for (Object obj : (List<?>) loadedData) { 
                    if (obj instanceof Contact) {
                        contactsSet.add((Contact) obj);
                    }
                }
            }
            if (contactsLSTMDL != null) { // Ensure list model is available before refreshing
                 refreshSharedContactsListModel();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading contacts: " + e.getMessage());
        }
    }

    private void refreshSharedContactsListModel() {
        if (contactsLSTMDL == null) return; // Guard against null list model
        contactsLSTMDL.clear();
        for (Contact c : contactsSet) {
            if (c != null && !c.getPhoneNumbers().isEmpty()) { 
                PhoneNumber firstPn = c.getPhoneNumbers().get(0);
                if (firstPn != null) { 
                     contactsLSTMDL.addElement(firstPn.getFirstName() + " " + firstPn.getLastName() + " - " + firstPn.getCity());
                } else {
                     contactsLSTMDL.addElement("Contact (Invalid PhoneData)");
                }
            } else {
                contactsLSTMDL.addElement("Contact (No Numbers)");
            }
        }
    }
}
