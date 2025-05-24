import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Assuming ContactsFrame and GroupMain are in the default package
// and will be compiled alongside.

public class mainContact extends JFrame {
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
            	// setVisible(false); // Retaining original logic, though might need review for UX
            	contactsFrame.setLocation(430,0);
                contactsFrame.setVisible(true);
            }
        });
    }
}
