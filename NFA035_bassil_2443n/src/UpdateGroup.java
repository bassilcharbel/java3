import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable; // Group, Contact, PhoneNumber are Serializable
import java.util.List;    // Used by Contact.getPhoneNumbers()
import java.util.Set;
// Assuming Contact, Group, PhoneNumber, GroupMain are in the default package.

public class UpdateGroup extends JFrame{
    private Set<Group> groupsSet_ref;
    private Group groupToUpdate;
    private int groupIndex_ref;
    private DefaultTableModel grpiModel_ref;
    private Set<Contact> allContactsSet_ref;
    private GroupMain groupMain_ref;

	JTable ctsiTBL; 
	JLabel headerLBL, grpLBL, descLBL; // updLBL was unused
	JTextField grpTXT, descTXT;
	JButton saveBTN, cancelBTN;
	DefaultTableModel ctsModel; 

	public UpdateGroup(Set<Group> groupsSet, Group groupToUpdate, int groupIndex, DefaultTableModel grpiModel, Set<Contact> allContactsSet, GroupMain groupMain) {
        this.groupsSet_ref = groupsSet;
        this.groupToUpdate = groupToUpdate;
        this.groupIndex_ref = groupIndex;
        this.grpiModel_ref = grpiModel;
        this.allContactsSet_ref = allContactsSet;
        this.groupMain_ref = groupMain;

		setTitle("Update Group: " + (this.groupToUpdate != null ? this.groupToUpdate.getGroupName() : "Error")); // Handle null groupToUpdate
		setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 450); 

		headerLBL=new JLabel("Update Group Details"); 
		headerLBL.setBounds(130,10,200,25); 
        headerLBL.setForeground(Color.BLUE);
        headerLBL.setFont(new Font("Arial", Font.BOLD, 14));
		add(headerLBL);
		
		grpLBL=new JLabel("Group name:");
		grpLBL.setBounds(30,50,100,25);
		add(grpLBL);
		
		grpTXT=new JTextField(190);
		grpTXT.setBounds(140,50,220,25);
		add(grpTXT);
		
		descLBL=new JLabel("Description:");
		descLBL.setBounds(30,80,100,25);
		add(descLBL);
		
		descTXT=new JTextField(150);
		descTXT.setBounds(140,80,220,25);
		add(descTXT);
		
        JLabel selectContactsLBL = new JLabel("Update contacts in this group:");
        selectContactsLBL.setBounds(30, 115, 300, 25);
        add(selectContactsLBL);
        
        ctsModel = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) { 
                    return Boolean.class;
                }
                return String.class;
            }
             @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; 
            }
        };
		ctsModel.addColumn("Contact Name");
		ctsModel.addColumn("City");
		ctsModel.addColumn("In Group");

		ctsiTBL=new JTable(ctsModel);
		ctsiTBL.getColumnModel().getColumn(0).setPreferredWidth(120);
        ctsiTBL.getColumnModel().getColumn(1).setPreferredWidth(100);
        ctsiTBL.getColumnModel().getColumn(2).setPreferredWidth(70); 


		JScrollPane sp = new JScrollPane(ctsiTBL);
		sp.setBounds(30,145,330,180); 
		add(sp);

		saveBTN=new JButton("Save Changes"); 
		saveBTN.setBounds(70,340,120,30);  
		add(saveBTN);

		cancelBTN=new JButton("Cancel");
		cancelBTN.setBounds(210,340,120,30); 
		add(cancelBTN);

        if (this.groupToUpdate != null) { // Ensure groupToUpdate is not null before prefilling
            prefillForm();
        } else {
            // Handle the error case, maybe disable the form or show an error
            JOptionPane.showMessageDialog(this, "Error: Group data is not available for update.", "Initialization Error", JOptionPane.ERROR_MESSAGE);
            // Consider disposing the frame if it's unusable: dispose();
            grpTXT.setEnabled(false);
            descTXT.setEnabled(false);
            saveBTN.setEnabled(false);
        }


        saveBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (groupToUpdate == null) { // Should not happen if form is disabled
                     JOptionPane.showMessageDialog(UpdateGroup.this, "Error: Group data is missing.", "Update Error", JOptionPane.ERROR_MESSAGE);
                     return;
                }
                String newGroupName = grpTXT.getText().trim();
                String newDescription = descTXT.getText().trim();

                if (newGroupName.isEmpty()) {
                    JOptionPane.showMessageDialog(UpdateGroup.this, "Group name cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String oldGroupName = groupToUpdate.getGroupName();
                boolean nameChanged = !oldGroupName.equalsIgnoreCase(newGroupName);

                if (nameChanged) {
                    for (Group existingGroup : groupsSet_ref) {
                        if (existingGroup != groupToUpdate && existingGroup.getGroupName().equalsIgnoreCase(newGroupName)) {
                            JOptionPane.showMessageDialog(UpdateGroup.this, "Another group with this name already exists.", "Duplicate Group Name", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    groupsSet_ref.remove(groupToUpdate); 
                }
                
                groupToUpdate.setGroupName(newGroupName);
                groupToUpdate.setDescription(newDescription);
                groupToUpdate.getContacts().clear(); 

                for (int i = 0; i < ctsModel.getRowCount(); i++) {
                    Boolean isInGroup = (Boolean) ctsModel.getValueAt(i, 2);
                    if (Boolean.TRUE.equals(isInGroup)) {
                        String contactNameInTable = (String) ctsModel.getValueAt(i, 0);
                        String contactCityInTable = (String) ctsModel.getValueAt(i, 1);
                        
                        for (Contact actualContact : allContactsSet_ref) {
                            if (actualContact != null && !actualContact.getPhoneNumbers().isEmpty()) {
                                PhoneNumber p = actualContact.getPhoneNumbers().get(0);
                                if (p != null) {
                                    String nameInSet = (p.getFirstName() + " " + p.getLastName()).trim();
                                    String cityInSet = p.getCity();
                                    if (nameInSet.equals(contactNameInTable) && cityInSet.equals(contactCityInTable)) {
                                        groupToUpdate.addContact(actualContact);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                if (nameChanged) {
                    groupsSet_ref.add(groupToUpdate); 
                }
                
                if (groupMain_ref != null) {
                    groupMain_ref.saveAllGroups(); 
                    groupMain_ref.refreshGroupsTable(); 
                    groupMain_ref.populateContactsInGroupTable(groupToUpdate); 
                }

                if (grpiModel_ref != null) { 
                    grpiModel_ref.setValueAt(groupToUpdate.getGroupName(), groupIndex_ref, 0);
                    grpiModel_ref.setValueAt(groupToUpdate.getContacts().size(), groupIndex_ref, 1);
                }
                
                JOptionPane.showMessageDialog(UpdateGroup.this, "Group updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        });

        cancelBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
	}

    private void prefillForm() {
        if (groupToUpdate == null) return; 
        grpTXT.setText(groupToUpdate.getGroupName());
        descTXT.setText(groupToUpdate.getDescription());
        populateContactsTable();
    }

    private void populateContactsTable() {
        ctsModel.setRowCount(0); 
        if (allContactsSet_ref != null) {
            for (Contact contact : allContactsSet_ref) {
                if (contact != null) {
                    String displayName = "N/A";
                    String city = "N/A";
                    if (!contact.getPhoneNumbers().isEmpty()) {
                        PhoneNumber firstPn = contact.getPhoneNumbers().get(0);
                        if (firstPn != null) {
                             displayName = (firstPn.getFirstName() + " " + firstPn.getLastName()).trim();
                             city = firstPn.getCity() != null ? firstPn.getCity() : "N/A";
                        }
                    }
                    boolean isInGroup = groupToUpdate != null && groupToUpdate.getContacts() != null && groupToUpdate.getContacts().contains(contact);
                    ctsModel.addRow(new Object[]{displayName, city, isInGroup});
                }
            }
        }
    }
}
