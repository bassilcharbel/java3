import java.util.List;
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
import java.util.HashSet; // Required for Group class
import java.util.Objects; // Required for Group and PhoneNumber equals/hashCode
import javax.swing.event.ListSelectionListener; // Added for GroupMain
import javax.swing.event.ListSelectionEvent; // Added for GroupMain
import java.awt.event.WindowListener; // Added for GroupMain
import java.awt.event.WindowAdapter; // Added for GroupMain
import java.awt.event.WindowEvent; // Added for GroupMain


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
        
       nwctf=new AddNewContactFrame();
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
        		// Placeholder for add new contact logic
        	}
        });
        readBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<Contact> contacts = readCts();
                DefaultListModel<String> listModel = new DefaultListModel<>();
                for (Contact c : contacts) {
                    if (c.getPnbrList() != null) {
                        for (PhoneNumber p : c.getPnbrList()) {
                            listModel.addElement(p.getFirstName() + " " + p.getLastName() + " " + p.getCity());
                        }
                    }
                }
                contactsInfoLST.setModel(listModel);
            }
        });

        newCBTN.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e ) {
        		nwctf.setVisible(true);
        		nwctf.setLocation(940,0);
        	}
        });
        updateBTN.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		ucf.setVisible(true);
        		ucf.setLocation(50,50);
        	}
        });
    }

    private List<Contact> readCts() {
        List<Contact> contactList = new ArrayList<>();
        File f = new File("contact.dat");
        if (!f.exists()) return contactList;

        try (FileInputStream fis = new FileInputStream(f);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object readObject = ois.readObject();
            if (readObject instanceof Set) {
                 contactList.addAll((Set<Contact>) readObject);
            } else if (readObject instanceof List) { // Handle if contact.dat stores a List
                 contactList.addAll((List<Contact>) readObject);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return contactList;
    }
}

class Contact  extends Observable implements Serializable{
	private static final long serialVersionUID = 1L; // Good practice
	List<PhoneNumber>pnbrList; // Changed from Set to List

	public Contact() { // Default constructor
		pnbrList=new ArrayList<PhoneNumber>();
	}
	
	public boolean addPhoneNumber(PhoneNumber phoneNumber) {
		if(pnbrList.add(phoneNumber)) {
			setChanged();
			notifyObservers();
			return true;
		}
		return false;
	}
	
	public boolean removePhoneNumber(PhoneNumber p) {
		if(pnbrList.remove(p)) {
			setChanged();
			notifyObservers();
			return true;
		}
		return false;
	}

    public List<PhoneNumber> getPnbrList() { // Getter for the list
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
        return Objects.equals(pnbrList, other.pnbrList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pnbrList);
    }
}

class PhoneNumber implements Comparable<PhoneNumber> ,Serializable{
	private static final long serialVersionUID = 2L; // Good practice
	String pnbr,city,firstName,lastName,region;
	public PhoneNumber(String pn,String c,String fn,String ln,String r) {
		pnbr=pn;city=c;firstName=fn;lastName=ln;region=r;
	}
	public int compareTo(PhoneNumber phoneNumber) {return pnbr.compareToIgnoreCase(phoneNumber.pnbr);
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
        return Objects.equals(pnbr.toLowerCase(), other.pnbr.toLowerCase()) &&
               Objects.equals(city.toLowerCase(), other.city.toLowerCase()) &&
               Objects.equals(firstName.toLowerCase(), other.firstName.toLowerCase()) &&
               Objects.equals(lastName.toLowerCase(), other.lastName.toLowerCase()) &&
               Objects.equals(region.toLowerCase(), other.region.toLowerCase());
    }
    @Override
    public int hashCode() {
        return Objects.hash(pnbr.toLowerCase(), city.toLowerCase(), firstName.toLowerCase(),
                lastName.toLowerCase(), region.toLowerCase());
    }
}

class AddNewContactFrame extends JFrame {
	private static final long serialVersionUID = 3L;
	PhoneNumber phoneNumber;
	Contact contactObject; 
	JLabel fnameLBL,lnameLBL,ctyLBL, gestionLBL ,newLBL,phnLBL,addLBL,nwLBL;
	JTextArea fnTXT,lnTXT,ctyTXT;
	JCheckBox grpCHK,famCHK,FrndCHK,workCHK;
	JButton saveBTN,cancelBTN,nwBTN,writeContactBTN,readBTN;
	JTable phonenbrTBL;
	DefaultTableModel ctsModel;
	boolean newRec=false;
	File f;
	InputStream is;
	FileInputStream fis;
	ObjectInputStream ois;
	OutputStream os;
	FileOutputStream fos;
	ObjectOutputStream oos;
	Iterator<PhoneNumber> contactIt;
	
	public AddNewContactFrame() {
		contactObject=new Contact();
		setTitle("Add New Contact");
		setLayout(null);
        setSize(400,500);
		gestionLBL=new JLabel("Gestion des Contacts");
		gestionLBL.setBounds(150,20,150,15);
		gestionLBL.setForeground(Color.blue);
		add(gestionLBL);
		
		writeContactBTN=new JButton("Write Contacts");
		writeContactBTN.setBounds(15,295,150,25);
		add(writeContactBTN);
		
		readBTN=new JButton("Read Contacts");
		readBTN.setBounds(10,10,120,25);
		add(readBTN);
		
		newLBL=new JLabel("New Contact");
		newLBL.setBounds(15,50,150,15);
		add(newLBL);
		
		fnameLBL=new JLabel("First Name:");
		fnameLBL.setFont(new Font("serif",Font.BOLD,12));
		fnameLBL.setBounds(20,80,80,20);
		add(fnameLBL);
		
		fnTXT=new JTextArea();
		fnTXT.setBounds(110,80,250,20);
		add(fnTXT);

		lnameLBL=new JLabel("Last Name:");
		lnameLBL.setFont(new Font("serif",Font.BOLD,12));
		lnameLBL.setBounds(20,110,80,20);
		add(lnameLBL);
		
		lnTXT=new JTextArea();
		lnTXT.setBounds(110,110,250,20);
		add(lnTXT);
		
		ctyLBL=new JLabel("City:");
        ctyLBL.setFont(new Font("serif",Font.BOLD,12));
		ctyLBL.setBounds(20,140,80,20);
		add(ctyLBL);
		
		ctyTXT=new JTextArea();
		ctyTXT.setBounds(110,140,250,20);
		add(ctyTXT);
		
		phnLBL=new JLabel("Phone Numbers:");
        phnLBL.setFont(new Font("serif",Font.BOLD,12));
		phnLBL.setBounds(20,170,120,20);
		add(phnLBL);
		
		ctsModel = new DefaultTableModel();
		ctsModel.addColumn("Region Code");
		ctsModel.addColumn("Phone Number");
		
		phonenbrTBL=new JTable(ctsModel);
		JScrollPane sp = new JScrollPane(phonenbrTBL);
		sp.setBounds(20,200,340,80);
		add(sp);
		
		addLBL=new JLabel("Add to Group:");
        addLBL.setFont(new Font("serif",Font.BOLD,12));
		addLBL.setBounds(20,290,150,20);
		add(addLBL);

		grpCHK=new JCheckBox("No Group");
		grpCHK.setSelected(true);
		grpCHK.setBounds(20,320,100,20);
		add(grpCHK);
		
		famCHK=new JCheckBox("Family");
		famCHK.setBounds(130,320,100,20);
		add(famCHK);
		
		FrndCHK=new JCheckBox("Friends");
		FrndCHK.setBounds(20,345,100,20);
		add(FrndCHK);
		
		workCHK=new JCheckBox("Co-Workers");
		workCHK.setBounds(130,345,120,20);
		add(workCHK);

		saveBTN=new JButton("Save");
		saveBTN.setBounds(50,380,75,25);
		add(saveBTN);
		
		nwBTN=new JButton("New Row");
		nwBTN.setBounds(150,380,100,25);
		add(nwBTN);
		
		cancelBTN=new JButton("Cancel");
		cancelBTN.setBounds(270,380,85,25);
		add(cancelBTN);

		writeContactBTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				writeCts();
			}
		});
		
		cancelBTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int choice = JOptionPane.showConfirmDialog(AddNewContactFrame.this,"Do you want to exit without saving?","Confirm Exit", JOptionPane.YES_NO_OPTION );
                if(choice == JOptionPane.YES_OPTION) {
                    dispose(); 
                }
			}
		});
		
		nwBTN.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ctsModel.addRow(new String[] { "", "" });
			}
		});
		
		saveBTN.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        String name = fnTXT.getText().trim();
		        String lastName = lnTXT.getText().trim();
		        String city = ctyTXT.getText().trim();

		        if (name.isEmpty() || lastName.isEmpty() || city.isEmpty()) {
		            JOptionPane.showMessageDialog(AddNewContactFrame.this, "First Name, Last Name, and City are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
		            return;
		        }
                
                if (contactObject == null || newRec) { 
                     contactObject = new Contact();
                     newRec = false; 
                }
                contactObject.getPnbrList().clear(); 

                for (int i = 0; i < ctsModel.getRowCount(); i++) {
                    String region = (String) ctsModel.getValueAt(i, 0);
                    String pnbrs = (String) ctsModel.getValueAt(i, 1);
                    if (pnbrs != null && !pnbrs.trim().isEmpty() && region != null && !region.trim().isEmpty() ) {
                        phoneNumber = new PhoneNumber(pnbrs, city, name, lastName, region);
                        contactObject.addPhoneNumber(phoneNumber);
                    }
                }
                
                if(contactObject.getPnbrList().isEmpty()){
                     JOptionPane.showMessageDialog(AddNewContactFrame.this, "At least one phone number must be entered with region and number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
		        writeCts(); 
                System.out.println("Contact saved/updated.");
		    }
		});
		
		readBTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				readCts();
			}
		});
	}
	
	private void readCts() {
	    f = new File("contact.dat");
	    if (!f.exists()) {
            JOptionPane.showMessageDialog(this, "contact.dat not found. Cannot read.", "File Error", JOptionPane.ERROR_MESSAGE);
	        return;
	    }
	    try (FileInputStream fin = new FileInputStream(f); 
             ObjectInputStream oin = new ObjectInputStream(fin)) {
	        Object obj = oin.readObject();
            if (obj instanceof Set || obj instanceof List) {
                Collection<Contact> contactsFromFile = (obj instanceof Set) ? (Set<Contact>)obj : (List<Contact>)obj;
                if (!contactsFromFile.isEmpty()) {
                    contactObject = contactsFromFile.iterator().next(); 
                } else {
                    contactObject = new Contact();
                }
            } else if (obj instanceof Contact) {
	            contactObject = (Contact) obj;
	        } else {
                 JOptionPane.showMessageDialog(this, "Unknown data structure in contact.dat.", "File Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

	        ctsModel.setRowCount(0); 
	        if (contactObject != null && contactObject.getPnbrList() != null && !contactObject.getPnbrList().isEmpty()) {
	            PhoneNumber firstPn = contactObject.getPnbrList().get(0);
	            fnTXT.setText(firstPn.getFirstName());
	            lnTXT.setText(firstPn.getLastName());
	            ctyTXT.setText(firstPn.getCity());
	            for (PhoneNumber pn : contactObject.getPnbrList()) {
	                ctsModel.addRow(new Object[]{pn.getRegion(), pn.getPnbr()});
	            }
	        } else { 
                fnTXT.setText("");
                lnTXT.setText("");
                ctyTXT.setText("");
            }
	    } catch (IOException | ClassNotFoundException ex) {
	        ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading from contact.dat: " + ex.getMessage(), "Read Error", JOptionPane.ERROR_MESSAGE);
	    }
	}

	private void writeCts() {
	    if (contactObject == null) {
             JOptionPane.showMessageDialog(this, "No contact data to save.", "Save Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // This should ideally save a List/Set of contacts, not just one.
        // For now, it overwrites with the single currently edited/created contact.
        Set<Contact> contactsToSave = new HashSet<>();
        contactsToSave.add(contactObject); 

	    try (FileOutputStream fout = new FileOutputStream("contact.dat");
	         ObjectOutputStream oout = new ObjectOutputStream(fout)) {
	        oout.writeObject(contactsToSave); 
	        JOptionPane.showMessageDialog(this, "Contact data saved to contact.dat.", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
	    } catch (IOException e) {
	        e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error writing to contact.dat: " + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
	    }
	}
}

class UpdateContactFrame extends JFrame{
	Contact contact;
	JLabel fnameLBL,lnameLBL,ctyLBL, updateLBL ,newLBL,phnLBL,addLBL;
	JTextArea fnTXT,lnTXT,ctyTXT;
	JCheckBox grpCHK,famCHK,FrndCHK,workCHK;
	JButton saveBTN,cancelBTN,readBTN;
	JTable phonenbrTBL;
	DefaultTableModel ctsModel;
	public UpdateContactFrame() {
		setTitle("Update Contact");
		setLayout(null);
        setSize(400,500);
		readBTN=new JButton("Load Contact");
		readBTN.setBounds(15,10,120,25);
		add(readBTN);
		
		updateLBL=new JLabel("Update Contact Details");
		updateLBL.setBounds(120,20,200,25);
		updateLBL.setForeground(Color.blue);
        updateLBL.setFont(new Font("Serif", Font.BOLD, 14));
		add(updateLBL);
		
		fnameLBL=new JLabel("First Name:");
		fnameLBL.setFont(new Font("serif",Font.BOLD,12));
		fnameLBL.setBounds(20,50,80,20);
		add(fnameLBL);
		
		fnTXT=new JTextArea();
		fnTXT.setBounds(110,50,250,20);
		add(fnTXT);
		
		lnameLBL=new JLabel("Last Name:");
		lnameLBL.setFont(new Font("serif",Font.BOLD,12));
		lnameLBL.setBounds(20,80,80,20);
		add(lnameLBL);
		
		lnTXT=new JTextArea();
		lnTXT.setBounds(110,80,250,20);
		add(lnTXT);
		
		ctyLBL=new JLabel("City:");
        ctyLBL.setFont(new Font("serif",Font.BOLD,12));
		ctyLBL.setBounds(20,110,80,20);
		add(ctyLBL);
		
		ctyTXT=new JTextArea();
		ctyTXT.setBounds(110,110,250,20);
		add(ctyTXT);
		
		phnLBL=new JLabel("Phone Numbers:");
        phnLBL.setFont(new Font("serif",Font.BOLD,12));
		phnLBL.setBounds(20,140,120,20);
		add(phnLBL);
		
		ctsModel = new DefaultTableModel();
		ctsModel.addColumn("Region Code");
		ctsModel.addColumn("Phone Number");
		
		phonenbrTBL=new JTable(ctsModel);
		JScrollPane sp = new JScrollPane(phonenbrTBL);
		sp.setBounds(20,170,340,100);
		add(sp);
		
		addLBL=new JLabel("Belongs to Group(s):");
        addLBL.setFont(new Font("serif",Font.BOLD,12));
		addLBL.setBounds(20,280,150,20);
		add(addLBL);
		
		grpCHK=new JCheckBox("No Groups");
		grpCHK.setBounds(20,310,100,20);
		add(grpCHK);
		
		famCHK=new JCheckBox("Family");
		famCHK.setBounds(130,310,100,20);
		add(famCHK);
		
		FrndCHK=new JCheckBox("Friends");
		FrndCHK.setBounds(20,335,100,20);
		add(FrndCHK);
		
		workCHK=new JCheckBox("Co-Workers");
		workCHK.setBounds(130,335,120,20);
		add(workCHK);
		
		saveBTN=new JButton("Save Update");
		saveBTN.setBounds(50,420,120,25);
		add(saveBTN);
		
		cancelBTN=new JButton("Cancel");
		cancelBTN.setBounds(200,420,100,25);
		add(cancelBTN);
	}
}

class GroupMain extends JFrame {
    AddNewGroup agrp;
    UpdateGroup ugrp;
    JButton addBTN, updateBTN, deleteBTN, viewContactsBTN; // viewContactsBTN is declared but not used in this subtask
    JTable groupInfoTBL, contactInfoTBL;
    JLabel titleLBL, tableLBL;
    DefaultTableModel grpiModel, ctiModel;
    private Set<Group> allGroupsSet = new HashSet<>();

    public GroupMain() {
        setTitle("Group Management");
        setLayout(null);
        setSize(450, 450);

        agrp = new AddNewGroup();
        agrp.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        ugrp = new UpdateGroup();
        ugrp.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        titleLBL = new JLabel("Gestion des Groupes et Contacts");
        titleLBL.setFont(new Font("Serif", Font.BOLD, 16));
        titleLBL.setBounds(100, 5, 250, 25);
        titleLBL.setForeground(Color.BLUE);
        add(titleLBL);

        grpiModel = new DefaultTableModel();
        grpiModel.addColumn("Group Name");
        grpiModel.addColumn("Number of Contacts");

        groupInfoTBL = new JTable(grpiModel);
        JScrollPane sp = new JScrollPane(groupInfoTBL);
        sp.setBounds(20, 70, 390, 120);
        add(sp);

        ctiModel = new DefaultTableModel();
        ctiModel.addColumn("Contact Name");
        ctiModel.addColumn("Contact City");

        contactInfoTBL = new JTable(ctiModel);
        JScrollPane s = new JScrollPane(contactInfoTBL);
        s.setBounds(20, 220, 390, 120);
        add(s);

        tableLBL = new JLabel("List of Groups");
        tableLBL.setFont(new Font("Serif", Font.BOLD, 12));
        tableLBL.setBounds(20, 45, 100, 20);
        add(tableLBL);

        JLabel contactsInGroupLBL = new JLabel("Contacts in Selected Group");
        contactsInGroupLBL.setFont(new Font("Serif", Font.BOLD, 12));
        contactsInGroupLBL.setBounds(20, 195, 200, 20);
        add(contactsInGroupLBL);

        addBTN = new JButton("Add New Group");
        addBTN.setBounds(20, 350, 130, 25);
        add(addBTN);

        deleteBTN = new JButton("Delete Group");
        deleteBTN.setBounds(160, 350, 120, 25);
        add(deleteBTN);

        updateBTN = new JButton("Update Group");
        updateBTN.setBounds(290, 350, 120, 25);
        add(updateBTN);

        viewContactsBTN = new JButton("View Contacts in Group"); // Declared but not implemented in this task
        viewContactsBTN.setBounds(20, 380, 200, 25);
        add(viewContactsBTN);

        groupInfoTBL.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && groupInfoTBL.getSelectedRow() != -1) {
                    String selectedGroupName = (String) grpiModel.getValueAt(groupInfoTBL.getSelectedRow(), 0);
                    Group selectedGroup = null;
                    for (Group group : allGroupsSet) {
                        if (group.getGroupName().equals(selectedGroupName)) {
                            selectedGroup = group;
                            break;
                        }
                    }
                    ctiModel.setRowCount(0); 
                    if (selectedGroup != null && selectedGroup.getContactsInGroup() != null) {
                        for (Contact contact : selectedGroup.getContactsInGroup()) {
                            String contactName = "N/A";
                            String city = "N/A";
                            if (contact.getPnbrList() != null && !contact.getPnbrList().isEmpty()) {
                                PhoneNumber pn = contact.getPnbrList().get(0);
                                contactName = pn.getFirstName() + " " + pn.getLastName();
                                city = pn.getCity();
                            }
                            ctiModel.addRow(new Object[]{contactName, city});
                        }
                    }
                }
            }
        });

        addBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                agrp.setVisible(true);
                // Remove existing listeners to avoid duplicates
                for(WindowListener wl : agrp.getWindowListeners()) {
                    if (wl.getClass().getName().contains("GroupMain$")) { 
                        agrp.removeWindowListener(wl);
                    }
                }
                agrp.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                        refreshGroupTable();
                    }
                     @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        refreshGroupTable(); // Also refresh if closed via 'X' if AddNewGroup hides
                    }
                });
            }
        });

        updateBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = groupInfoTBL.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(GroupMain.this, "Please select a group to update.", "No Group Selected", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String selectedGroupName = (String) grpiModel.getValueAt(selectedRow, 0);
                Group groupToUpdate = null;
                for (Group group : allGroupsSet) {
                    if (group.getGroupName().equals(selectedGroupName)) {
                        groupToUpdate = group;
                        break;
                    }
                }

                if (groupToUpdate != null) {
                    ugrp.loadGroupToUpdate(groupToUpdate); 
                    ugrp.setVisible(true);
                    for(WindowListener wl : ugrp.getWindowListeners()) {
                         if (wl.getClass().getName().contains("GroupMain$")) { 
                            ugrp.removeWindowListener(wl);
                        }
                    }
                    ugrp.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                            refreshGroupTable();
                        }
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                            refreshGroupTable();
                        }
                    });
                }
            }
        });
        
        deleteBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = groupInfoTBL.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(GroupMain.this, "Please select a group to delete.", "No Group Selected", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String selectedGroupName = (String) grpiModel.getValueAt(selectedRow, 0);
                
                int confirm = JOptionPane.showConfirmDialog(GroupMain.this, 
                                "Are you sure you want to delete the group '" + selectedGroupName + "'?", 
                                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean removed = allGroupsSet.removeIf(g -> g.getGroupName().equals(selectedGroupName));
                    if (removed) {
                        PrjctDemo.writeGroups(allGroupsSet);
                        refreshGroupTable();
                        JOptionPane.showMessageDialog(GroupMain.this, "Group '" + selectedGroupName + "' deleted successfully.", "Group Deleted", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(GroupMain.this, "Could not delete group '" + selectedGroupName + "'.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        refreshGroupTable(); // Initial population of the group table
    }

    private void refreshGroupTable() {
        allGroupsSet = PrjctDemo.readGroups(); 
        
        grpiModel.setRowCount(0); 
        contactInfoTBL.setModel(new DefaultTableModel(new Object[][]{}, new String[]{"Contact Name", "Contact City"})); // Clear contactInfoTBL
        
        if (allGroupsSet != null) {
            for (Group group : allGroupsSet) {
                grpiModel.addRow(new Object[]{
                    group.getGroupName(), 
                    group.getContactsInGroup() != null ? group.getContactsInGroup().size() : 0
                });
            }
        }
    }
}

class AddNewGroup extends JFrame {
    JTable ctsiTBL;
    JLabel headerLBL, grpLBL, descLBL;
    JTextField grpTXT, descTXT;
    JButton saveBTN, cancelBTN;
    DefaultTableModel ctsModel;
    private java.util.List<Contact> displayedContacts = new java.util.ArrayList<>();

    public AddNewGroup() {
        setTitle("Add New Group");
        setLayout(null);
        setSize(400, 400); 

        headerLBL = new JLabel("Gestion des Contacts - Add Group");
        headerLBL.setBounds(100, 10, 250, 25); 
        headerLBL.setForeground(Color.BLUE);
        add(headerLBL);

        grpLBL = new JLabel("Group Name:");
        grpLBL.setBounds(20, 50, 100, 25);
        add(grpLBL);

        grpTXT = new JTextField();
        grpTXT.setBounds(130, 50, 230, 25);
        add(grpTXT);

        descLBL = new JLabel("Description:");
        descLBL.setBounds(20, 80, 100, 25);
        add(descLBL);

        descTXT = new JTextField();
        descTXT.setBounds(130, 80, 230, 25);
        add(descTXT);

        ctsModel = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) { // "Add to group" column
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Only checkbox column is editable
            }
        };
        ctsModel.addColumn("contact name"); 
        ctsModel.addColumn("City");         
        ctsModel.addColumn("Add to group"); 
        
        ctsiTBL = new JTable(ctsModel);
        JScrollPane sp = new JScrollPane(ctsiTBL);
        sp.setBounds(20, 120, 340, 180); 
        add(sp);

        saveBTN = new JButton("Save");
        saveBTN.setBounds(100, 320, 75, 25); 
        add(saveBTN);

        cancelBTN = new JButton("Cancel");
        cancelBTN.setBounds(200, 320, 75, 25); 
        add(cancelBTN);

        saveBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String groupName = grpTXT.getText().trim();
                String groupDescription = descTXT.getText().trim();

                if (groupName.isEmpty()) {
                    JOptionPane.showMessageDialog(AddNewGroup.this, "Group name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Group newGroup = new Group(groupName, groupDescription);

                for (int i = 0; i < ctsModel.getRowCount(); i++) {
                    Boolean isSelected = (Boolean) ctsModel.getValueAt(i, 2);
                    if (isSelected != null && isSelected) {
                        if (i < displayedContacts.size()) { // Check bounds
                            newGroup.addContact(displayedContacts.get(i));
                        }
                    }
                }

                System.out.println("Group to save: " + newGroup.getGroupName() + 
                                   ", Description: " + newGroup.getGroupDescription() + 
                                   ", Contacts selected: " + newGroup.getContactsInGroup().size());
                for(Contact c : newGroup.getContactsInGroup()){
                    System.out.println("  - " + c.toString());
                }
                
                grpTXT.setText("");
                descTXT.setText("");
                populateContactTable(); 
            }
        });

        cancelBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                grpTXT.setText("");
                descTXT.setText("");
                populateContactTable();
            }
        });
        
        populateContactTable(); // Call at the end of the constructor
    }

    private void populateContactTable() {
        ctsModel.setRowCount(0); 
        displayedContacts.clear(); 

        Set<Contact> allContacts = new HashSet<>();
        File contactsFile = new File("contact.dat");
        if (contactsFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(contactsFile))) {
                Object readObject = ois.readObject();
                if (readObject instanceof Set) {
                     allContacts = (Set<Contact>) readObject;
                } else if (readObject instanceof List) { // Handle if accidentally saved as List
                    allContacts = new HashSet<>((List<Contact>) readObject);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace(); 
                JOptionPane.showMessageDialog(this, "Error loading contacts: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (allContacts != null) {
            for (Contact contact : allContacts) {
                String contactName = "N/A";
                String city = "N/A";

                if (contact.getPnbrList() != null && !contact.getPnbrList().isEmpty()) {
                    PhoneNumber pn = contact.getPnbrList().get(0); 
                    contactName = pn.getFirstName() + " " + pn.getLastName();
                    city = pn.getCity();
                }
                ctsModel.addRow(new Object[]{contactName, city, false}); 
                displayedContacts.add(contact); 
            }
        }
    }
}

class UpdateGroup extends JFrame {
    JTable ctsiTBL;
    JLabel headerLBL, grpLBL, descLBL; // updLBL was unused
    JTextField grpTXT, descTXT;
    JButton saveBTN, cancelBTN;
    DefaultTableModel ctsModel;
    private java.util.List<Contact> displayedContacts = new java.util.ArrayList<>();
    private Group currentGroup; // To store the group being edited

    public UpdateGroup() {
        setTitle("Update Group");
        setLayout(null);
        setSize(400, 400);

        headerLBL = new JLabel("Gestion des Contacts - Update Group");
        headerLBL.setBounds(100, 10, 250, 25); // Adjusted y for spacing
        headerLBL.setForeground(Color.BLUE);
        add(headerLBL);

        grpLBL = new JLabel("Group Name:");
        grpLBL.setBounds(20, 50, 100, 25);
        add(grpLBL);

        grpTXT = new JTextField();
        grpTXT.setBounds(130, 50, 230, 25);
        add(grpTXT);

        descLBL = new JLabel("Description:");
        descLBL.setBounds(20, 80, 100, 25);
        add(descLBL);

        descTXT = new JTextField();
        descTXT.setBounds(130, 80, 230, 25);
        add(descTXT);

        ctsModel = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) { // "Add to group" column
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Only checkbox column is editable
            }
        };
        ctsModel.addColumn("contact name");
        ctsModel.addColumn("City");
        ctsModel.addColumn("Add to group"); // For checkboxes

        ctsiTBL = new JTable(ctsModel);
        JScrollPane sp = new JScrollPane(ctsiTBL);
        sp.setBounds(20, 120, 340, 180);
        add(sp);

        saveBTN = new JButton("Save");
        saveBTN.setBounds(100, 320, 75, 25);
        add(saveBTN);

        cancelBTN = new JButton("Cancel");
        cancelBTN.setBounds(200, 320, 75, 25);
        add(cancelBTN);

        saveBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentGroup == null) {
                    JOptionPane.showMessageDialog(UpdateGroup.this, "No group selected to update.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String groupName = grpTXT.getText().trim();
                String groupDescription = descTXT.getText().trim();

                if (groupName.isEmpty()) {
                    JOptionPane.showMessageDialog(UpdateGroup.this, "Group name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                currentGroup.setGroupName(groupName);
                currentGroup.setGroupDescription(groupDescription);
                currentGroup.getContactsInGroup().clear(); // Clear existing members before re-adding

                for (int i = 0; i < ctsModel.getRowCount(); i++) {
                    Boolean isSelected = (Boolean) ctsModel.getValueAt(i, 2);
                    if (isSelected != null && isSelected) {
                        if (i < displayedContacts.size()) {
                            currentGroup.addContact(displayedContacts.get(i));
                        }
                    }
                }

                System.out.println("Group updated: " + currentGroup.getGroupName() +
                                   ", Description: " + currentGroup.getGroupDescription() +
                                   ", Contacts selected: " + currentGroup.getContactsInGroup().size());
                for(Contact c : currentGroup.getContactsInGroup()){
                    System.out.println("  - " + c.toString());
                }
                
                // Logic to save the updated currentGroup to a main list and persist
                // would go here in a more complete application.
                // JOptionPane.showMessageDialog(UpdateGroup.this, "Group updated (simulated).");
                // setVisible(false); 
                loadGroupToUpdate(null); // Clear the form as a visual cue
            }
        });
        
        cancelBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadGroupToUpdate(null); // Clear form and table
                // setVisible(false); // Optionally hide the window
            }
        });
    }

    public void loadGroupToUpdate(Group groupToUpdate) {
        this.currentGroup = groupToUpdate;
        if (this.currentGroup == null) {
            grpTXT.setText("");
            descTXT.setText("");
            ctsModel.setRowCount(0); 
            return;
        }

        grpTXT.setText(this.currentGroup.getGroupName());
        descTXT.setText(this.currentGroup.getGroupDescription());
        populateContactTable(); 
    }

    private void populateContactTable() {
        ctsModel.setRowCount(0);
        displayedContacts.clear();

        Set<Contact> allContacts = new HashSet<>();
        File contactsFile = new File("contact.dat");
        if (contactsFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(contactsFile))) {
                Object readObj = ois.readObject();
                if (readObj instanceof Set) {
                    allContacts = (Set<Contact>) readObj;
                } else if (readObj instanceof List) { // Handle if contact.dat was saved as List
                     allContacts = new HashSet<>((List<Contact>) readObj);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading contacts: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (allContacts != null) {
            for (Contact contact : allContacts) {
                String contactName = "N/A";
                String city = "N/A";
                if (contact.getPnbrList() != null && !contact.getPnbrList().isEmpty()) {
                    PhoneNumber pn = contact.getPnbrList().get(0);
                    contactName = pn.getFirstName() + " " + pn.getLastName();
                    city = pn.getCity();
                }

                boolean isMember = false;
                if (currentGroup != null && currentGroup.getContactsInGroup() != null) {
                    // Contact.equals() must be correctly implemented for contains() to work.
                    isMember = currentGroup.getContactsInGroup().contains(contact);
                }
                ctsModel.addRow(new Object[]{contactName, city, isMember});
                displayedContacts.add(contact);
            }
        }
    }
}

class Group implements Serializable {
    private static final long serialVersionUID = 1L; 
    private String groupName;
    private String groupDescription;
    private Set<Contact> contactsInGroup;

    public Group(String groupName, String groupDescription) {
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.contactsInGroup = new HashSet<>();
    }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }
    public String getGroupDescription() { return groupDescription; }
    public void setGroupDescription(String groupDescription) { this.groupDescription = groupDescription; }
    public Set<Contact> getContactsInGroup() { return contactsInGroup; }
    public void setContactsInGroup(Set<Contact> contactsInGroup) { this.contactsInGroup = contactsInGroup; }

    public void addContact(Contact contact) { contactsInGroup.add(contact); }
    public void removeContact(Contact contact) { contactsInGroup.remove(contact); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group)) return false;
        Group group = (Group) o;
        return Objects.equals(groupName.toLowerCase(), group.groupName.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupName.toLowerCase());
    }

    @Override
    public String toString() {
        return "Group Name: " + groupName + ", Number of Contacts: " + contactsInGroup.size();
    }
}

public class PrjctDemo { 
    private static final String GROUPS_FILE_PATH = "groups.dat";

   public static Set<Group> readGroups() {
       Set<Group> groups = new HashSet<>();
       File groupsFile = new File(GROUPS_FILE_PATH);
       if (groupsFile.exists()) {
           try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(groupsFile))) {
               Object data = ois.readObject();
               if (data instanceof Set) {
                   groups = (Set<Group>) data;
               } else if (data instanceof List) { // Handle if old data was List
                   groups = new HashSet<>((List<Group>) data);
               }
           } catch (IOException | ClassNotFoundException e) {
               e.printStackTrace(); // Log error or show a message
               // For robust error handling, could inform the user
               // JOptionPane.showMessageDialog(null, "Error reading groups data: " + e.getMessage(), "Read Error", JOptionPane.ERROR_MESSAGE);
           }
       }
       return groups;
   }

   public static void writeGroups(Set<Group> groups) {
       try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(GROUPS_FILE_PATH))) {
           oos.writeObject(groups);
       } catch (IOException e) {
           e.printStackTrace(); // Log error or show a message
           // JOptionPane.showMessageDialog(null, "Error writing groups data: " + e.getMessage(), "Write Error", JOptionPane.ERROR_MESSAGE);
       }
   }

    public static void main(String[] args) {
        mainContact c = new mainContact();
        c.setSize(400, 300);
        c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        c.setVisible(true);
    }
}
>>>>>>> REPLACE
