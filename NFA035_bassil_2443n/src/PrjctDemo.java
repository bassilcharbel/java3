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

class ContactsFrame extends JFrame {
	UpdateContactFrame ucf;
	AddNewContactFrame nwctf;
	JList contactsInfoLST;
//    JTable ctsTBL;
//    DefaultTableModel ctsTBLMDL;
    JTextArea cyanTXT, searchTXT;
    JButton srtFnBTN, srtLnBTN, srtCyBTN, newCBTN,viewBTN,updateBTN,deleteBTN,readBTN;
    JLabel contactLBL, gestionCLBL, searchLBL;
    JScrollPane scrollPane;

    public ContactsFrame() {
    	
        setTitle("contacts interface");
        setLayout(null);
        
        readBTN=new JButton("read");
        readBTN.setBounds(10,10,100,25);
        add(readBTN);
        
        ucf=new UpdateContactFrame();
        ucf.setSize(400,500);
        
        updateBTN=new JButton("update");
        updateBTN.setBounds(320,225,75,25);
        add(updateBTN);
        
        deleteBTN=new JButton("delete");
        deleteBTN.setBounds(415,225,75,25);
        add(deleteBTN);
        
        viewBTN=new JButton("view");
        viewBTN.setBounds(225,225,75,25);
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
        
       nwctf=new AddNewContactFrame(null, null, null, getDefaultCloseOperation(), rootPaneCheckingEnabled);
        nwctf.setSize(400,500);
        searchLBL = new JLabel("search");
        searchLBL.setBounds(230, 30, 50, 25);
        add(searchLBL);
        
        contactsInfoLST=new JList();
        contactsInfoLST.setVisibleRowCount(5);
        contactsInfoLST.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactsInfoLST.setBounds(230,70,200,150);
        add(contactsInfoLST);
        
        searchTXT = new JTextArea();
        searchTXT.setBounds(290, 35, 100, 15);
        add(searchTXT);
        
        
        newCBTN.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        	}
        });
        readBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<Contact> contacts = readCts();

                DefaultListModel<String> listModel = new DefaultListModel<>();
                for (Contact c : contacts) {
                    for (PhoneNumber p : c.pnbrList) {
                        // Assuming PhoneNumber has getFirstName(), getLastName(), getCity()
                        listModel.addElement(p.getFirstName() + " " + p.getLastName() + " " + p.getCity());
                    }
                }

                contactsInfoLST.setModel(listModel);
            }
        });


//        ctsTBLMDL = new DefaultTableModel(dataRows, columnNames);
//        ctsTBL = new JTable(ctsTBLMDL) {
//            public boolean editCellAt(int row, int column, java.util.EventObject e) {
//                return false;
//            }
//        };
        newCBTN.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e ) {
        		nwctf.setVisible(true);
        		nwctf.setLocation(940,0);
        	}
        });
//        ctsTBL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        scrollPane = new JScrollPane(ctsTBL);
//        scrollPane.setBounds(210, 70, 260, 150);
//        add(scrollPane);
        
//        deleteBTN.addActionListener(new ActionListener() {
//        	public void actionPerformed(ActionEvent e) {
//        		int selectedRow = ctsTBL.getSelectedRow();
//                if (selectedRow >= 0) {
//                	 ctsLST.remove(selectedRow);
//                     ctsTBLMDL.removeRow(selectedRow);
//                }
//        	}
//        });
        //to be used in the group thingggyyyy
        //XOXOXOXO
//        srtFnBTN.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                sortTableByColumn(0); // Column 0 = First Name
//            }
//        });
//
//        srtLnBTN.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                sortTableByColumn(1);
//            }
//        });
//
//        srtCyBTN.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                sortTableByColumn(2);
//            }
//        });
        updateBTN.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		ucf.setVisible(true);
        		ucf.setLocation(50,50);
        	}
        });
//    }

//    private void sortTableByColumn(final int colIndex) {
//        List<Object[]> rowDataList = new ArrayList<Object[]>();
//
//        for (int i = 0; i < ctsTBLMDL.getRowCount(); i++) {
//            Object[] row = new Object[ctsTBLMDL.getColumnCount()];
//            for (int j = 0; j < row.length; j++) {
//                row[j] = ctsTBLMDL.getValueAt(i, j);
//            }
//            rowDataList.add(row);
//        }
//
//        Collections.sort(rowDataList, new Comparator<Object[]>() {
//            public int compare(Object[] row1, Object[] row2) {
//                return row1[colIndex].toString().compareToIgnoreCase(row2[colIndex].toString());
//            }
//        });
//        ctsTBLMDL.setRowCount(0);
//        for (int i = 0; i < rowDataList.size(); i++) {
//            ctsTBLMDL.addRow(rowDataList.get(i));
//        }
    }
    private List<Contact> readCts() {
        List<Contact> contactList = new ArrayList<>();

        try {
            File f = new File("contact.dat");
            if (!f.exists()) return contactList;

            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);

            contactList = (List<Contact>) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return contactList;
    }

}

class Contact extends Observable implements Serializable {
    List<PhoneNumber> pnbrList;

    public Contact() {
        pnbrList = new ArrayList<>();
    }

    public boolean addPhoneNumber(PhoneNumber phoneNumber) {
        pnbrList.add(phoneNumber);
        setChanged();
        notifyObservers();
        return true;
    }

    public boolean removePhoneNumber(PhoneNumber p) {
        boolean removed = pnbrList.remove(p);
        if (removed) {
            setChanged();
            notifyObservers();
        }
        return removed;
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return pnbrList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Contact with ").append(pnbrList.size()).append(" numbers:\n");
        for (PhoneNumber pn : pnbrList) {
            sb.append(pn.toString()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact)) return false;
        Contact other = (Contact) o;
        return pnbrList.equals(other.pnbrList);
    }

    @Override
    public int hashCode() {
        return pnbrList.hashCode();
    }
}

class PhoneNumber implements Comparable<PhoneNumber>, Serializable {
    String pnbr, city, firstName, lastName, region;

    public PhoneNumber(String pn, String c, String fn, String ln, String r) {
        pnbr = pn;
        city = c;
        firstName = fn;
        lastName = ln;
        region = r;
    }

    public int compareTo(PhoneNumber phoneNumber) {
        return pnbr.compareToIgnoreCase(phoneNumber.pnbr);
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPnbr() { return pnbr; }
    public String getCity() { return city; }
    public String getRegion() { return region; }

    @Override
    public String toString() {
        return String.format("PhoneNumber[pnbr=%s, city=%s, firstName=%s, lastName=%s, region=%s]",
                pnbr, city, firstName, lastName, region);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneNumber)) return false;
        PhoneNumber other = (PhoneNumber) o;
        return pnbr.equalsIgnoreCase(other.pnbr) &&
               city.equalsIgnoreCase(other.city) &&
               firstName.equalsIgnoreCase(other.firstName) &&
               lastName.equalsIgnoreCase(other.lastName) &&
               region.equalsIgnoreCase(other.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pnbr.toLowerCase(), city.toLowerCase(), firstName.toLowerCase(),
                lastName.toLowerCase(), region.toLowerCase());
    }
}

class AddNewContactFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    // UI Components
    JLabel fnameLBL, lnameLBL, ctyLBL, gestionLBL, newLBL, phnLBL, addLBL;
    JTextArea fnTXT, lnTXT, ctyTXT;
    JCheckBox grpCHK, famCHK, FrndCHK, workCHK;
    JButton saveBTN, cancelBTN, nwBTN, writeContactBTN, readBTN;
    JTable phonenbrTBL;
    DefaultTableModel ctsModel;

    // Data
    Set<Contact> contactsSet;
    DefaultListModel<String> contactsLSTMDL;
    Contact oldContact;
    int selectedIndex;
    boolean newRec = false;

    public AddNewContactFrame(Set<Contact> contactsSet, DefaultListModel<String> model,
                              Contact oldContact, int index, boolean newRec) {
        this.contactsSet = contactsSet;
        this.contactsLSTMDL = model;
        this.oldContact = oldContact;
        this.selectedIndex = index;
        this.newRec = newRec;

        setTitle("Contact Management");
        setLayout(null);
        setSize(450, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // UI Setup
        gestionLBL = new JLabel("Gestion des contacts");
        gestionLBL.setBounds(150, 20, 150, 15);
        gestionLBL.setForeground(Color.blue);
        add(gestionLBL);

        writeContactBTN = new JButton("Write Contacts");
        writeContactBTN.setBounds(15, 295, 150, 25);
        add(writeContactBTN);

        readBTN = new JButton("Read");
        readBTN.setBounds(10, 10, 100, 25);
        add(readBTN);

        newLBL = new JLabel("New Contact");
        newLBL.setBounds(15, 50, 150, 15);
        add(newLBL);

        fnameLBL = new JLabel("First Name");
        fnameLBL.setFont(new Font("Serif", Font.BOLD, 12));
        fnameLBL.setBounds(175, 40, 80, 20);
        add(fnameLBL);

        lnameLBL = new JLabel("Last Name");
        lnameLBL.setFont(new Font("Serif", Font.BOLD, 12));
        lnameLBL.setBounds(175, 70, 80, 20);
        add(lnameLBL);

        fnTXT = new JTextArea();
        fnTXT.setBounds(265, 40, 150, 20);
        add(fnTXT);

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

        saveBTN = new JButton("Save");
        saveBTN.setBounds(175, 380, 75, 25);
        add(saveBTN);

        nwBTN = new JButton("New");
        nwBTN.setBounds(260, 380, 75, 25);
        add(nwBTN);

        cancelBTN = new JButton("Cancel");
        cancelBTN.setBounds(345, 380, 75, 25);
        add(cancelBTN);

        // Prefill form if editing an existing contact
        if (this.oldContact != null) {
            prefillForm();
            this.newRec = false;
        } else {
            this.newRec = true;
        }

        // Button listeners
        saveBTN.addActionListener(e -> saveContact());

        cancelBTN.addActionListener(e -> {
            int res = JOptionPane.showConfirmDialog(this, "Do you want to exit?", "Confirm Exit",
                    JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                dispose();
            }
        });

        nwBTN.addActionListener(e -> {
            clearForm();
            ctsModel.addRow(new String[] { "", "" });
            this.newRec = true;
        });

        writeContactBTN.addActionListener(e -> writeContactsToFile());

        readBTN.addActionListener(e -> readContactsFromFile());
    }

    private void prefillForm() {
        List<PhoneNumber> pns = oldContact.getPhoneNumbers();
        fnTXT.setText("");
        lnTXT.setText("");
        ctyTXT.setText("");
        ctsModel.setRowCount(0);

        if (!pns.isEmpty()) {
            PhoneNumber pn = pns.get(0);
            fnTXT.setText(pn.getFirstName());
            lnTXT.setText(pn.getLastName());
            ctyTXT.setText(pn.getCity());
        }

        for (PhoneNumber pn : pns) {
            ctsModel.addRow(new Object[]{pn.getRegion(), pn.getPnbr()});
        }

        // Uncomment if group management is implemented
        // grpCHK.setSelected(oldContact.hasGroup("No Group"));
        // famCHK.setSelected(oldContact.hasGroup("Family"));
        // FrndCHK.setSelected(oldContact.hasGroup("Friends"));
        // workCHK.setSelected(oldContact.hasGroup("Co-Workers"));
    }

    private void clearForm() {
        fnTXT.setText("");
        lnTXT.setText("");
        ctyTXT.setText("");
        ctsModel.setRowCount(0);
        grpCHK.setSelected(true);
        famCHK.setSelected(false);
        FrndCHK.setSelected(false);
        workCHK.setSelected(false);
    }

    private void saveContact() {
        String firstName = fnTXT.getText().trim();
        String lastName = lnTXT.getText().trim();
        String city = ctyTXT.getText().trim();

        System.out.println("Saving contact with: ");
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("City: " + city);
        if (firstName.isEmpty() || lastName.isEmpty() || city.isEmpty()) {
            JOptionPane.showMessageDialog(this, "First Name, Last Name, and City are required.");
            return;
        }
        System.out.println("Row count in ctsModel: " + ctsModel.getRowCount());
        TreeSet<PhoneNumber> pnbrSet = new TreeSet<>();
        for (int i = 0; i < ctsModel.getRowCount(); i++) {
            String region = (String) ctsModel.getValueAt(i, 0);
            String pnbr = (String) ctsModel.getValueAt(i, 1);
            System.out.println("region "+ region+" phoneNUmber"+pnbr);
            if (pnbr != null && !pnbr.trim().isEmpty()) {
                PhoneNumber phoneNumber = new PhoneNumber(pnbr, city, firstName, lastName, region);
                pnbrSet.add(phoneNumber);
                System.out.println("Added phone number: " + phoneNumber);
            }
        }

        if (pnbrSet.isEmpty()) {
            JOptionPane.showMessageDialog(this, "At least one phone number is required.");
            return;
        }

        Contact newContact = new Contact();
        for (PhoneNumber p : pnbrSet) {
            newContact.addPhoneNumber(p);
        }

        System.out.println("New contact created: " + newContact);

        // Uncomment if group management is implemented
        // if (grpCHK.isSelected()) newContact.addGroup("No Group");
        // if (famCHK.isSelected()) newContact.addGroup("Family");
        // if (FrndCHK.isSelected()) newContact.addGroup("Friends");
        // if (workCHK.isSelected()) newContact.addGroup("Co-Workers");

        if (this.newRec) {
            if (contactsSet.add(newContact)) {
                contactsLSTMDL.addElement(newContact.toString());
                JOptionPane.showMessageDialog(this, "Contact saved successfully.");
                this.newRec = false;
                System.out.println("Contact added to set.");
            } else {
                JOptionPane.showMessageDialog(this, "Duplicate contact. Not added.");
                System.out.println("Duplicate contact detected. Not added.");
            }
        } else {
            contactsSet.remove(oldContact);
            if (contactsSet.add(newContact)) {
                contactsLSTMDL.setElementAt(newContact.toString(), selectedIndex);
                JOptionPane.showMessageDialog(this, "Contact updated successfully.");
                this.newRec = false;
                System.out.println("Contact updated in set.");
            } else {
                contactsSet.add(oldContact); // rollback
                JOptionPane.showMessageDialog(this, "Duplicate contact. Update failed.");
                System.out.println("Duplicate on update. Rolled back to old contact.");
            }
        }

        writeContactsToFile();
    }


    private void writeContactsToFile() {
        try (FileOutputStream fos = new FileOutputStream("contact.dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(contactsSet);
            JOptionPane.showMessageDialog(this, "Contacts saved to file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving contacts: " + e.getMessage());
        }
    }

    private void readContactsFromFile() {
        File f = new File("contact.dat");
        if (!f.exists()) {
            JOptionPane.showMessageDialog(this, "No contacts file found.");
            return;
        }
        try (FileInputStream fis = new FileInputStream(f);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Set<Contact> loadedContacts = (Set<Contact>) ois.readObject();
            contactsSet.clear();
            contactsSet.addAll(loadedContacts);
            contactsLSTMDL.clear();
            for (Contact c : contactsSet) {
                contactsLSTMDL.addElement(c.toString());
            }
            JOptionPane.showMessageDialog(this, "Contacts loaded from file.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading contacts: " + e.getMessage());
        }
    }
}
//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
class UpdateContactFrame extends JFrame{
	
	Contact contact;
	JLabel fnameLBL,lnameLBL,ctyLBL, updateLBL ,newLBL,phnLBL,addLBL;
	JTextArea fnTXT,lnTXT,ctyTXT;
	JCheckBox grpCHK,famCHK,FrndCHK,workCHK;
	JButton saveBTN,cancelBTN,readBTN;
	JTable phonenbrTBL;
	DefaultTableModel ctsModel;
	public UpdateContactFrame() {
		setTitle("update");
		setLayout(null);
		readBTN=new JButton("read contacts");
		readBTN.setBounds(15,100,150,15);
		add(readBTN);
		
		updateLBL=new JLabel("update contact");
		updateLBL.setBounds(150,20,150,15);
		updateLBL.setForeground(Color.blue);
		add(updateLBL);
		
		newLBL=new JLabel("update");
		newLBL.setBounds(15,50,150,15);
		add(newLBL);
		
		fnameLBL=new JLabel("first name");
		fnameLBL.setFont(new Font("serif",Font.BOLD,10));
		fnameLBL.setBounds(175,40,50,15);
		add(fnameLBL);
		
		lnameLBL=new JLabel("last name");
		lnameLBL.setBounds(175,60,50,15);
		lnameLBL.setFont(new Font("serif",Font.BOLD,10));
		add(lnameLBL);
		
		fnTXT=new JTextArea(15,30);
		fnTXT.setBounds(225,40,100,15);
		add(fnTXT);
		
		lnTXT=new JTextArea(15,30);
		lnTXT.setBounds(225,60,100,15);
		add(lnTXT);
		
		ctyLBL=new JLabel("city");
		ctyLBL.setBounds(175,80,50,15);
		add(ctyLBL);
		
		ctyTXT=new JTextArea(15,30);
		ctyTXT.setBounds(205,80,120,15);
		add(ctyTXT);
		
		phnLBL=new JLabel("phone numbers");
		phnLBL.setBounds(240,100,100,20);
		add(phnLBL);
		
		ctsModel = new DefaultTableModel();
		ctsModel.addColumn("Region code");
		ctsModel.addColumn("phone number");
		
		addLBL=new JLabel("add the contacts to group");
		addLBL.setBounds(190,240,150,20);
		add(addLBL);
		
		saveBTN=new JButton("save");
		saveBTN.setBounds(200,400,75,20);
		add(saveBTN);
		
		cancelBTN=new JButton("cancel");
		cancelBTN.setBounds(280,400,75,20);
		add(cancelBTN);
		
		phonenbrTBL=new JTable(ctsModel);
		add(phonenbrTBL);
		JScrollPane sp = new JScrollPane(phonenbrTBL);
		sp.setBounds(100,125,260,120);
		add(sp);
		
		grpCHK=new JCheckBox("No Groups");
		grpCHK.setBounds(250,280,100,20);
		add(grpCHK);
		
		famCHK=new JCheckBox("Family");
		famCHK.setBounds(250,300,100,20);
		add(famCHK);
		
		FrndCHK=new JCheckBox("Friends");
		FrndCHK.setBounds(250,320,100,20);
		add(FrndCHK);
		
		workCHK=new JCheckBox("Co-Workors");
		workCHK.setBounds(250,340,100,20);
		add(workCHK);
		
		readBTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		
	}
}

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

public class PrjctDemo {
    public static void main(String[] args) {
        mainContact c = new mainContact();
        c.setSize(400, 300);
        c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        c.setVisible(true);
    }
}
