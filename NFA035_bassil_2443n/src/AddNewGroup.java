import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable; // Group, Contact, PhoneNumber are Serializable
import java.util.ArrayList; // Though not directly used, List is its parent.
import java.util.List;    // Used by Contact.getPhoneNumbers()
import java.util.Set;

// Assuming Contact, Group, PhoneNumber, GroupMain are in the default package.

public class AddNewGroup extends JFrame{
	private Set<Group> groupsSet_ref; 
    private DefaultTableModel grpiModel_ref; 
    private Set<Contact> allContactsSet_ref; 
    private GroupMain groupMain_ref; 

	JTable ctsiTBL; 
	JLabel headerLBL, grpLBL, descLBL; // updLBL was unused
	JTextField grpTXT, descTXT;
	JButton saveBTN, cancelBTN;
	DefaultTableModel ctsModel; 

	public AddNewGroup(Set<Group> groupsSet, DefaultTableModel grpiModel, Set<Contact> allContactsSet, GroupMain groupMainRef) {
		this.groupsSet_ref = groupsSet;
        this.grpiModel_ref = grpiModel;
        this.allContactsSet_ref = allContactsSet;
        this.groupMain_ref = groupMainRef;

		setTitle("Add New Group"); 
		setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setSize(400, 450); 

		headerLBL=new JLabel("Group Details"); 
		headerLBL.setBounds(150,10,150,25); // Adjusted y for spacing
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
		
        JLabel selectContactsLBL = new JLabel("Select contacts for this group:");
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
                return column == 2; // Only checkbox column is editable
            }
        };
		ctsModel.addColumn("Contact Name");
		ctsModel.addColumn("City");
		ctsModel.addColumn("Add to Group");

		ctsiTBL=new JTable(ctsModel);
		ctsiTBL.getColumnModel().getColumn(0).setPreferredWidth(120);
        ctsiTBL.getColumnModel().getColumn(1).setPreferredWidth(100);
        ctsiTBL.getColumnModel().getColumn(2).setPreferredWidth(80); 

		JScrollPane sp = new JScrollPane(ctsiTBL);
		sp.setBounds(30,145,330,180); 
		add(sp);

		saveBTN=new JButton("Save Group"); 
		saveBTN.setBounds(70,340,120,30); 
		add(saveBTN);

		cancelBTN=new JButton("Cancel"); 
		cancelBTN.setBounds(210,340,120,30); 
		add(cancelBTN);

		populateContactsTable(); 

		saveBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String groupName = grpTXT.getText().trim();
                String description = descTXT.getText().trim();

                if (groupName.isEmpty()) {
                    JOptionPane.showMessageDialog(AddNewGroup.this, "Group name cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                for (Group existingGroup : groupsSet_ref) {
                    if (existingGroup.getGroupName().equalsIgnoreCase(groupName)) {
                        JOptionPane.showMessageDialog(AddNewGroup.this, "A group with this name already exists.", "Duplicate Group", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                Group newGroup = new Group(groupName, description);
                
                for (int i = 0; i < ctsModel.getRowCount(); i++) {
                    Boolean isSelected = (Boolean) ctsModel.getValueAt(i, 2); 
                    if (Boolean.TRUE.equals(isSelected)) {
                        String contactNameInTable = (String) ctsModel.getValueAt(i, 0);
                        String contactCityInTable = (String) ctsModel.getValueAt(i, 1);
                        
                        for (Contact c : allContactsSet_ref) {
                            if (c != null && !c.getPhoneNumbers().isEmpty()) {
                                PhoneNumber p = c.getPhoneNumbers().get(0); 
                                if (p!=null) {
                                    String nameInSet = (p.getFirstName() + " " + p.getLastName()).trim();
                                    String cityInSet = p.getCity();
                                    if (nameInSet.equals(contactNameInTable) && cityInSet.equals(contactCityInTable)) {
                                        newGroup.addContact(c);
                                        break; 
                                    }
                                }
                            }
                        }
                    }
                }

                if (groupsSet_ref.add(newGroup)) {
                    if (grpiModel_ref != null) {
                         grpiModel_ref.addRow(new Object[]{newGroup.getGroupName(), newGroup.getContacts().size()});
                    }
                    
                    if (groupMain_ref != null) {
                        groupMain_ref.saveAllGroups();
                    }

                    JOptionPane.showMessageDialog(AddNewGroup.this, "Group added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFormAndClose();
                } else {
                    JOptionPane.showMessageDialog(AddNewGroup.this, "Failed to add group (possibly a duplicate or other error).", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); 
            }
        });
	}

	private void populateContactsTable() {
        ctsModel.setRowCount(0); 
        if (allContactsSet_ref != null) {
            for (Contact contact : allContactsSet_ref) {
                if (contact != null && !contact.getPhoneNumbers().isEmpty()) {
                    PhoneNumber firstPn = contact.getPhoneNumbers().get(0); 
                    if (firstPn != null) {
                        String displayName = (firstPn.getFirstName() + " " + firstPn.getLastName()).trim();
                        String city = firstPn.getCity() != null ? firstPn.getCity() : "N/A";
                        ctsModel.addRow(new Object[]{displayName, city, Boolean.FALSE}); 
                    }
                }
            }
        }
    }

    private void clearFormAndClose() {
        grpTXT.setText("");
        descTXT.setText("");
        populateContactsTable(); 
        dispose(); 
    }
}
