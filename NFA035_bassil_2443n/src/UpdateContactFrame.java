import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream; 
import java.io.IOException;     
import java.io.ObjectOutputStream; 
import java.io.Serializable; 
import java.util.List;    
import java.util.Set;
import java.util.TreeSet; 

// Assuming Contact and PhoneNumber are in the default package.

public class UpdateContactFrame extends JFrame {
    // Set<Contact> contactsSet; // Shared from ContactsFrame
    // DefaultListModel<String> contactsLSTMDL; // Shared from ContactsFrame
    // Contact contactToUpdate; // The specific contact to update
    // int contactIndex; // Index of the contact in contactsLSTMDL

    // Direct references to shared data:
    private Set<Contact> contactsSet_ref; 
    private DefaultListModel<String> contactsLSTMDL_ref; 
    private Contact contactToUpdate_orig; // Keep original for removal
    private int contactIndex_ref;


    JLabel fnameLBL, lnameLBL, ctyLBL, updateLBL, newLBL, phnLBL, addLBL;
    JTextArea fnTXT, lnTXT, ctyTXT;
    JCheckBox grpCHK, famCHK, FrndCHK, workCHK;
    JButton saveBTN, cancelBTN, addPhoneRowBTN; 
    JTable phonenbrTBL;
    DefaultTableModel ctsModel;

    public UpdateContactFrame(Set<Contact> sharedContactsSet, DefaultListModel<String> sharedContactsLSTMDL, Contact contactToUpdate, int contactIndex) {
        this.contactsSet_ref = sharedContactsSet;
        this.contactsLSTMDL_ref = sharedContactsLSTMDL;
        this.contactToUpdate_orig = contactToUpdate; // Store the original reference
        this.contactIndex_ref = contactIndex;

        setTitle("Update Contact");
        setLayout(null);
        setSize(450, 480); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        updateLBL = new JLabel("Update Contact Details");
        updateLBL.setBounds(150, 20, 200, 15);
        updateLBL.setForeground(Color.blue);
        add(updateLBL);

        String displayName = "Contact";
        if (this.contactToUpdate_orig != null && !this.contactToUpdate_orig.getPhoneNumbers().isEmpty()) {
            PhoneNumber firstPn = this.contactToUpdate_orig.getPhoneNumbers().get(0);
             if (firstPn != null) { 
                displayName = (firstPn.getFirstName() != null ? firstPn.getFirstName() : "") + 
                              " " + 
                              (firstPn.getLastName() != null ? firstPn.getLastName() : "");
            }
        }
        newLBL = new JLabel("Editing: " + displayName.trim());
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
        if (contactToUpdate_orig == null) return;
        List<PhoneNumber> pns = contactToUpdate_orig.getPhoneNumbers();
        ctsModel.setRowCount(0); 

        if (pns != null && !pns.isEmpty()) { 
            PhoneNumber firstPn = pns.get(0);
            if (firstPn != null) { 
                 fnTXT.setText(firstPn.getFirstName());
                 lnTXT.setText(firstPn.getLastName());
                 ctyTXT.setText(firstPn.getCity());
            }
            for (PhoneNumber pn : pns) {
                 if (pn != null) { 
                    ctsModel.addRow(new Object[]{pn.getRegion(), pn.getPnbr()});
                }
            }
        } else { 
             fnTXT.setText(""); 
             lnTXT.setText("");
             ctyTXT.setText("");
        }
        // TODO: Implement group checkbox state loading from contactToUpdate_orig if groups are part of Contact
    }

    public void saveUpdatedContact() { 
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
        
        Contact updatedContact = new Contact(); 
        // TODO: If Contact has an ID or other immutable fields, copy them from contactToUpdate_orig
        // For example: updatedContact.setId(contactToUpdate_orig.getId());
        for (PhoneNumber p : pnbrSet) {
            updatedContact.addPhoneNumber(p);
        }
        // TODO: Copy group information if applicable from checkboxes to updatedContact

        // Critical: remove the original instance, then add the new one.
        if (contactsSet_ref.remove(contactToUpdate_orig)) {
            if (contactsSet_ref.add(updatedContact)) { 
                if (contactsLSTMDL_ref != null) {
                    contactsLSTMDL_ref.setElementAt(firstName + " " + lastName + " - " + city, contactIndex_ref);
                }
                JOptionPane.showMessageDialog(this, "Contact updated successfully.");
                writeContactsToFile(); 
                dispose();
            } else {
                contactsSet_ref.add(contactToUpdate_orig); // Rollback: re-add original if new one is duplicate or fails to add
                JOptionPane.showMessageDialog(this, "Update failed. The updated contact details might conflict with another existing contact.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error: Original contact could not be found in the set for update. It might have been modified or deleted by another part of the application.");
        }
    }

    private void writeContactsToFile() {
        try (FileOutputStream fos = new FileOutputStream("contact.dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(contactsSet_ref); // Save the shared set
        } catch (IOException e) {
            e.printStackTrace();
            // Avoid showing JOptionPane here if frame is disposing.
        }
    }
}
