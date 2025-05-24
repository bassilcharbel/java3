import javax.swing.JFrame;
import javax.swing.SwingUtilities;

// Contact.java and Group.java are assumed to be in the same directory (default package)
// and compiled. The other UI classes (mainContact, ContactsFrame, etc.) are now
// in their own files and will be compiled as well.

public class PrjctDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Assuming mainContact.java is in the same directory and will be compiled.
                // No import needed for classes in the default package.
                mainContact c = new mainContact();
                c.setSize(400, 300); // Default size for the main window
                c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit application when this window closes
                c.setVisible(true);
            }
        });
    }
}
