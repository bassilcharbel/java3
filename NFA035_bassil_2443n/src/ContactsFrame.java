import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable; // Contact and PhoneNumber are Serializable
import java.util.List; // For compatibility with old List format & Contact's phone list
import java.util.Set;
import java.util.TreeSet;
// Assuming Contact, PhoneNumber, AddNewContactFrame, UpdateContactFrame are in the default package.
// No explicit imports needed for them if they are in the same directory (default package) and compiled together.

public class ContactsFrame extends JFrame {
    // Shared data models
    Set<Contact> contactsSet = new TreeSet<>();
    DefaultListModel<String> contactsLSTMDL = new DefaultListModel<>();

    UpdateContactFrame ucf; // Should be initialized when needed
    AddNewContactFrame nwctf; // Should be initialized when needed
    JList<String> contactsInfoLST;
    JTextArea cyanTXT, searchTXT;
    JButton srtFnBTN, srtLnBTN, srtCyBTN, newCBTN, viewBTN, updateBTN, deleteBTN, readBTN;
    JLabel contactLBL, gestionCLBL, searchLBL;
    JScrollPane scrollPane; 

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
        scrollPane = new JScrollPane(contactsInfoLST);
        scrollPane.setBounds(230, 70, 200, 150);
        add(scrollPane);

        searchTXT = new JTextArea();
        searchTXT.setBounds(290, 35, 100, 15);
        add(searchTXT);
        
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
                    int i = 0;
                    // This assumes contactsSet is iterated in the same order as contactsLSTMDL is populated
                    // This is true for TreeSet if refreshContactsListModel always iterates contactsSet in its natural order
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
                        // This might happen if the set is modified and list not refreshed, or selection logic error
                        JOptionPane.showMessageDialog(ContactsFrame.this, "Could not retrieve selected contact details. The list might be out of sync.");
                    }
                } else {
                    JOptionPane.showMessageDialog(ContactsFrame.this, "Please select a contact to update.");
                }
            }
        });
    }

    private void refreshContactsListModel() {
        contactsLSTMDL.clear();
        for (Contact c : contactsSet) { 
            if (c != null && !c.getPhoneNumbers().isEmpty()) {
                PhoneNumber firstPn = c.getPhoneNumbers().get(0); // Assuming at least one PhoneNumber if list not empty
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

    @SuppressWarnings("unchecked") 
    private void loadContactsFromFile() {
        File f = new File("contact.dat");
        if (!f.exists()) {
            // Optionally inform the user or log that the file doesn't exist
            // System.out.println("contact.dat not found. Starting with an empty contact list.");
            return; 
        }
        try (FileInputStream fis = new FileInputStream(f);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object loadedData = ois.readObject();
            contactsSet.clear(); 
            if (loadedData instanceof Set) { 
                // Type safety check for each element would be ideal here
                for (Object obj : (Set<?>) loadedData) {
                    if (obj instanceof Contact) {
                        contactsSet.add((Contact) obj);
                    }
                }
            } else if (loadedData instanceof List) { // Compatibility for old List format
                for (Object obj : (List<?>) loadedData) {
                    if (obj instanceof Contact) {
                        contactsSet.add((Contact) obj);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading contacts: " + e.getMessage(), "Loading Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
