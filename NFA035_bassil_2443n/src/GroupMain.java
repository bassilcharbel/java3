import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
import java.io.FileNotFoundException;
import java.io.Serializable; 
import java.util.Set;
import java.util.TreeSet;
import java.util.List; 
import java.util.Iterator; // Not explicitly used but good for Set iteration understanding

// Assuming Contact, Group, AddNewGroup, UpdateGroup are in the default package.

public class GroupMain extends JFrame{
	private Set<Group> groupsSet = new TreeSet<>();
	private Set<Contact> allContactsSet = new TreeSet<>(); 
	private static final String GROUP_FILE_NAME = "group.dat";
	private static final String CONTACT_FILE_NAME = "contact.dat"; 

	AddNewGroup agrp; 
	UpdateGroup ugrp; 
	JButton addBTN,updateBTN,deleteBTN;
	JTable groupInfoTBL,contactInfoTBL;
	JLabel titleLBL,tableLBL; // grpLBL was unused
	DefaultTableModel grpiModel,ctiModel;
	
	public GroupMain() {
		setTitle("Group Management"); // More specific title
		setLayout(null);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); // Hide this frame when closed, doesn't exit app

		loadAllContacts();    
		loadGroupsFromFile(); 
		
		titleLBL=new JLabel("Group Management"); 
		titleLBL.setBounds(150,5,150,25); // Adjusted for new title
		titleLBL.setForeground(Color.BLUE); // Example color
        titleLBL.setFont(new Font("Arial", Font.BOLD, 14)); // Example font
		add(titleLBL);
		
		grpiModel=new DefaultTableModel();
		grpiModel.addColumn("Group Name"); // More standard column name
		grpiModel.addColumn("# Contacts"); // Shorter
		groupInfoTBL=new JTable(grpiModel);
		groupInfoTBL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane sp = new JScrollPane(groupInfoTBL);
		sp.setBounds(20,70,340,120); // Adjusted layout
		add(sp);
		
		ctiModel=new DefaultTableModel();
		ctiModel.addColumn("Contact Name");
		ctiModel.addColumn("Contact City"); 
		contactInfoTBL=new JTable(ctiModel);
		JScrollPane s = new JScrollPane(contactInfoTBL);
		s.setBounds(20,220,340,120); // Adjusted layout
		add(s);
		
		tableLBL=new JLabel("List of Groups");
		tableLBL.setBounds(20,40,150,25); // Adjusted layout
		add(tableLBL);

        JLabel contactsInGroupLBL = new JLabel("Contacts in Selected Group:");
        contactsInGroupLBL.setBounds(20, 195, 200, 25);
        add(contactsInGroupLBL);
		
		addBTN=new JButton("Add New Group"); // More descriptive
		addBTN.setBounds(20,350,140,25); // Adjusted layout
		add(addBTN);
		
		updateBTN=new JButton("Update Group"); 
		updateBTN.setBounds(170,350,120,25); 
		add(updateBTN);
		
		deleteBTN=new JButton("Delete Group");
		deleteBTN.setBounds(300,350,120,25); // Adjusted layout
		add(deleteBTN);

		refreshGroupsTable(); // Initial population of the group table

		deleteBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = groupInfoTBL.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(GroupMain.this, "Please select a group to delete.", "No Group Selected", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String groupNameToDelete = (String) grpiModel.getValueAt(selectedRow, 0);
                int confirmation = JOptionPane.showConfirmDialog(GroupMain.this, 
                                                               "Are you sure you want to delete the group '" + groupNameToDelete + "'?", 
                                                               "Confirm Deletion", 
                                                               JOptionPane.YES_NO_OPTION,
                                                               JOptionPane.QUESTION_MESSAGE);
                if (confirmation != JOptionPane.YES_OPTION) {
                    return;
                }
                Group objectToDelete = null;
                Iterator<Group> iterator = groupsSet.iterator(); // Use iterator for safe removal
                while(iterator.hasNext()){
                    Group group = iterator.next();
                    if (group.getGroupName().equals(groupNameToDelete)) {
                        objectToDelete = group; // Mark for removal
                        iterator.remove();      // Remove using iterator
                        break;
                    }
                }

                if (objectToDelete != null) {
                    writeGroupsToFile(); 
                    refreshGroupsTable(); 
                    populateContactsInGroupTable(null); 
                    JOptionPane.showMessageDialog(GroupMain.this, "Group '" + groupNameToDelete + "' deleted successfully.", "Deletion Successful", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(GroupMain.this, "Error: Could not find the selected group to delete.", "Deletion Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

		updateBTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                int selectedRow = groupInfoTBL.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(GroupMain.this, "Please select a group to update.", "No Group Selected", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String groupNameFromTable = (String) grpiModel.getValueAt(selectedRow, 0);
                Group objectToUpdate = null;
                for (Group group : groupsSet) {
                    if (group.getGroupName().equals(groupNameFromTable)) {
                        objectToUpdate = group;
                        break;
                    }
                }
                if (objectToUpdate == null) {
                    JOptionPane.showMessageDialog(GroupMain.this, "Could not find the selected group. Please refresh.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (ugrp == null || !ugrp.isVisible()) {
                    ugrp = new UpdateGroup(groupsSet, objectToUpdate, selectedRow, grpiModel, allContactsSet, GroupMain.this);
                    ugrp.setSize(400, 450); 
                    ugrp.setLocationRelativeTo(GroupMain.this);
                    ugrp.setVisible(true);
                } else {
                    ugrp.toFront();
                }
			}
		});

		addBTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (agrp == null || !agrp.isVisible()) {
				    agrp = new AddNewGroup(groupsSet, grpiModel, allContactsSet, GroupMain.this);
				    agrp.setSize(400, 450); 
                    agrp.setLocationRelativeTo(GroupMain.this); 
				    agrp.setVisible(true);
				} else {
                    agrp.toFront(); 
                }
			}
		});

        groupInfoTBL.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = groupInfoTBL.getSelectedRow();
                    if (selectedRow != -1) {
                        // Ensure row index is valid for the model
                        if (selectedRow < grpiModel.getRowCount()) {
                             String selectedGroupName = (String) grpiModel.getValueAt(selectedRow, 0);
                             Group foundGroup = null;
                             for (Group group : groupsSet) {
                                 if (group.getGroupName().equals(selectedGroupName)) {
                                     foundGroup = group;
                                     break;
                                 }
                             }
                             populateContactsInGroupTable(foundGroup);
                        } else {
                             // This can happen if table is refreshed and selection becomes invalid
                             populateContactsInGroupTable(null);
                        }
                    } else {
                        populateContactsInGroupTable(null);
                    }
                }
            }
        });
	}

    public void saveAllGroups() {
        writeGroupsToFile();
    }

	@SuppressWarnings("unchecked")
	private void loadAllContacts() {
		File contactsFile = new File(CONTACT_FILE_NAME);
		if (!contactsFile.exists()) {
			System.out.println(CONTACT_FILE_NAME + " not found. No contacts loaded for group assignment.");
			allContactsSet = new TreeSet<>(); 
			return;
		}
		try (FileInputStream fis = new FileInputStream(contactsFile);
			 ObjectInputStream ois = new ObjectInputStream(fis)) {
			Object loadedData = ois.readObject();
			allContactsSet.clear(); 
			if (loadedData instanceof Set) {
                for(Object obj : (Set<?>) loadedData) { 
                    if (obj instanceof Contact) allContactsSet.add((Contact)obj);
                }
			} else if (loadedData instanceof List) { 
                for(Object obj : (List<?>) loadedData) { 
                    if (obj instanceof Contact) allContactsSet.add((Contact)obj);
                }
            } else {
				System.err.println("Data in " + CONTACT_FILE_NAME + " is not of expected type Set<Contact> or List<Contact>.");
			}
			// System.out.println("All contacts loaded from " + CONTACT_FILE_NAME + ". Count: " + allContactsSet.size());
		} catch (FileNotFoundException e) {
			System.err.println("Error: " + CONTACT_FILE_NAME + " not found during load. " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Error reading from " + CONTACT_FILE_NAME + ": " + e.getMessage());
			// e.printStackTrace(); // Consider logging framework or less verbose error for user
		} catch (ClassNotFoundException e) {
			System.err.println("Error: Class not found during deserialization from " + CONTACT_FILE_NAME + ". " + e.getMessage());
			// e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void loadGroupsFromFile() {
		File groupFile = new File(GROUP_FILE_NAME);
		if (!groupFile.exists()) {
			System.out.println(GROUP_FILE_NAME + " not found. Starting with an empty set of groups.");
			return;
		}
		try (FileInputStream fis = new FileInputStream(groupFile);
			 ObjectInputStream ois = new ObjectInputStream(fis)) {
			Object loadedData = ois.readObject();
			if (loadedData instanceof Set) {
				groupsSet.clear();
                for(Object obj : (Set<?>) loadedData) { 
                    if (obj instanceof Group) groupsSet.add((Group)obj);
                }
				// System.out.println("Groups loaded from " + GROUP_FILE_NAME);
			} else {
				System.err.println("Data in " + GROUP_FILE_NAME + " is not of expected type Set<Group>.");
			}
		} catch (FileNotFoundException e) {
			System.err.println("Error: " + GROUP_FILE_NAME + " not found during load. " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Error reading from " + GROUP_FILE_NAME + ": " + e.getMessage());
			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println("Error: Class not found during deserialization from " + GROUP_FILE_NAME + ". " + e.getMessage());
			// e.printStackTrace();
		}
	}

	private void writeGroupsToFile() {
		try (FileOutputStream fos = new FileOutputStream(GROUP_FILE_NAME);
			 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(groupsSet);
			// System.out.println("Groups saved to " + GROUP_FILE_NAME);
		} catch (IOException e) {
			System.err.println("Error writing to " + GROUP_FILE_NAME + ": " + e.getMessage());
			// e.printStackTrace();
		}
	}

	public void refreshGroupsTable() { 
		if (grpiModel == null) {
			grpiModel = new DefaultTableModel();
			grpiModel.addColumn("Group Name");
			grpiModel.addColumn("# Contacts");
			if (groupInfoTBL != null) {
				groupInfoTBL.setModel(grpiModel);
			}
		}
		grpiModel.setRowCount(0); 
		if (groupsSet != null) {
			for (Group group : groupsSet) {
				if (group != null) {
					String groupName = group.getGroupName();
					int numberOfContacts = (group.getContacts() != null) ? group.getContacts().size() : 0;
					grpiModel.addRow(new Object[]{groupName, numberOfContacts});
				}
			}
		}
	}

	public void populateContactsInGroupTable(Group selectedGroup) { 
        if (ctiModel == null) { 
            ctiModel = new DefaultTableModel();
            ctiModel.addColumn("Contact Name");
            ctiModel.addColumn("Contact City");
            if (contactInfoTBL != null) {
                contactInfoTBL.setModel(ctiModel);
            }
        }
        ctiModel.setRowCount(0); 

        if (selectedGroup != null && selectedGroup.getContacts() != null) {
            for (Contact contact : selectedGroup.getContacts()) {
                if (contact != null) {
                    String contactName = "N/A";
                    String contactCity = "N/A";
                    List<PhoneNumber> phoneNumbers = contact.getPhoneNumbers();
                    if (phoneNumbers != null && !phoneNumbers.isEmpty()) {
                        PhoneNumber firstPn = phoneNumbers.get(0);
                        if (firstPn != null) {
                            contactName = (firstPn.getFirstName() != null ? firstPn.getFirstName() : "") + 
                                          " " + 
                                          (firstPn.getLastName() != null ? firstPn.getLastName() : "");
                            contactCity = firstPn.getCity() != null ? firstPn.getCity() : "N/A";
                        }
                    }
                    ctiModel.addRow(new Object[]{contactName.trim(), contactCity});
                }
            }
        }
    }
}
