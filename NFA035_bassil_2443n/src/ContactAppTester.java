import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

// Assuming Contact, PhoneNumber are accessible (default package)

public class ContactAppTester {

    private static final String PRODUCTION_CONTACT_FILE = "contact.dat";
    // Use a distinct test file name to avoid conflict even with backup/restore logic
    private static final String TEST_CONTACT_FILE = "contact_test_suite.dat"; 

    public static void main(String[] args) {
        System.out.println("Starting ContactApp Data Logic Tests...");
        boolean allTestsPassed = true;

        try {
            System.out.println("\n--- Test 1: Save New Contact ---");
            testSaveNewContact();
            System.out.println("testSaveNewContact: PASSED");
        } catch (Exception e) {
            System.err.println("testSaveNewContact: FAILED - " + e.getMessage());
            e.printStackTrace();
            allTestsPassed = false;
        }

        try {
            System.out.println("\n--- Test 2: Load Contacts ---");
            testLoadContacts();
            System.out.println("testLoadContacts: PASSED");
        } catch (Exception e) {
            System.err.println("testLoadContacts: FAILED - " + e.getMessage());
            e.printStackTrace();
            allTestsPassed = false;
        }
        
        try {
            System.out.println("\n--- Test 3: Update Existing Contact ---");
            testUpdateContact();
            System.out.println("testUpdateContact: PASSED");
        } catch (Exception e) {
            System.err.println("testUpdateContact: FAILED - " + e.getMessage());
            e.printStackTrace();
            allTestsPassed = false;
        }

        cleanupTestFile(); // Clean up the specific test file

        System.out.println("\n-------------------------------------");
        if (allTestsPassed) {
            System.out.println("All ContactApp data logic tests PASSED!");
        } else {
            System.err.println("One or more ContactApp data logic tests FAILED.");
        }
        System.out.println("-------------------------------------");
    }

    private static void prepareTestFile() {
        File file = new File(TEST_CONTACT_FILE);
        if (file.exists()) {
            if (!file.delete()) {
                System.err.println("Warning: Could not delete existing test file: " + TEST_CONTACT_FILE);
            }
        }
    }

    private static void cleanupTestFile() {
        File file = new File(TEST_CONTACT_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    // Helper to write a Set<Contact> to a file (mimicking frame logic)
    private static void writeContactsToFileHelper(Set<Contact> contacts, String filename) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(contacts);
        }
    }

    // Helper to load Set<Contact> from a file (mimicking frame logic)
    @SuppressWarnings("unchecked")
    private static Set<Contact> loadContactsFromFileHelper(String filename) throws IOException, ClassNotFoundException {
        File f = new File(filename);
        if (!f.exists()) {
            return new TreeSet<>(); // Return empty set if file not found
        }
        try (FileInputStream fis = new FileInputStream(f);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object loadedData = ois.readObject();
            if (loadedData instanceof Set) {
                return (Set<Contact>) loadedData;
            } else if (loadedData instanceof List) { // For compatibility
                return new TreeSet<>((List<Contact>) loadedData);
            }
            throw new IOException("Unexpected data type in contact file: " + loadedData.getClass().getName());
        }
    }
    
    // Helper to populate a DefaultListModel from a Set<Contact> (mimicking frame logic)
    private static void refreshListModelFromSet(Set<Contact> contacts, DefaultListModel<String> listModel) {
        listModel.clear();
        for (Contact c : contacts) {
            if (!c.getPhoneNumbers().isEmpty()) {
                listModel.addElement(c.getFirstName() + " " + c.getLastName() + " - " + c.getCity());
            } else {
                listModel.addElement(c.getFirstName() + " " + c.getLastName() + " - " + c.getCity() + " (No Numbers)");
            }
        }
    }

    // --- Test Cases ---

    public static void testSaveNewContact() throws Exception {
        prepareTestFile();

        Set<Contact> sharedContactsSet = new TreeSet<>();
        DefaultListModel<String> sharedContactsLSTMDL = new DefaultListModel<>();

        // Simulate data that would be collected by AddNewContactFrame
        String firstName = "John";
        String lastName = "Doe";
        String city = "TestCity";
        TreeSet<PhoneNumber> pnbrSet = new TreeSet<>();
        pnbrSet.add(new PhoneNumber("555-0101", "123"));
        
        Contact newContact = new Contact(firstName, lastName, city);
        for (PhoneNumber p : pnbrSet) {
            newContact.addPhoneNumber(p);
        }
        
        // Simulate AddNewContactFrame.saveContact() logic for a new contact
        if (sharedContactsSet.add(newContact)) {
            sharedContactsLSTMDL.addElement(firstName + " " + lastName + " - " + city);
            // In the app, clearFormForNew() would be called.
        } else {
            throw new RuntimeException("testSaveNewContact: Failed to add new contact to set (duplicate?).");
        }
        
        // Simulate AddNewContactFrame.writeContactsToFile()
        writeContactsToFileHelper(sharedContactsSet, TEST_CONTACT_FILE);

        // Verification
        if (sharedContactsSet.size() != 1) {
            throw new RuntimeException("testSaveNewContact: contactsSet size should be 1 after save, but was " + sharedContactsSet.size());
        }
        // After clearFormForNew, LSTMDL would be empty in the actual AddNewContactFrame,
        // but here we test the state of *shared* LSTMDL which should reflect the addition.
        if (sharedContactsLSTMDL.getSize() != 1) { 
             throw new RuntimeException("testSaveNewContact: sharedContactsLSTMDL size should be 1, but was " + sharedContactsLSTMDL.getSize());
        }
        Contact savedContact = sharedContactsSet.iterator().next();
        if (!savedContact.getFirstName().equals("John")) {
            throw new RuntimeException("testSaveNewContact: Saved contact first name mismatch.");
        }

        // Verify by reloading
        Set<Contact> reloadedContactsSet = loadContactsFromFileHelper(TEST_CONTACT_FILE);
        DefaultListModel<String> reloadedLSTMDL = new DefaultListModel<>();
        refreshListModelFromSet(reloadedContactsSet, reloadedLSTMDL);

        if (reloadedContactsSet.size() != 1) {
            throw new RuntimeException("testSaveNewContact: Reloaded contactsSet size should be 1, but was " + reloadedContactsSet.size());
        }
        if (reloadedLSTMDL.getSize() != 1) {
            throw new RuntimeException("testSaveNewContact: Reloaded contactsLSTMDL size should be 1, but was " + reloadedLSTMDL.getSize());
        }
        Contact reloadedContact = reloadedContactsSet.iterator().next();
        if (!reloadedContact.getFirstName().equals("John")) {
            throw new RuntimeException("testSaveNewContact: Reloaded contact first name mismatch.");
        }
        if (!reloadedLSTMDL.getElementAt(0).contains("John Doe - TestCity")) {
            throw new RuntimeException("testSaveNewContact: Reloaded LSTMDL content mismatch.");
        }
    }

    public static void testLoadContacts() throws Exception {
        prepareTestFile();

        Set<Contact> predefinedContacts = new TreeSet<>();
        Contact c1 = new Contact("Alice", "Smith", "CityA");
        c1.addPhoneNumber(new PhoneNumber("111-2222", "001"));
        predefinedContacts.add(c1);

        Contact c2 = new Contact("Bob", "Jones", "CityB");
        c2.addPhoneNumber(new PhoneNumber("333-4444", "002"));
        c2.addPhoneNumber(new PhoneNumber("555-6666", "003"));
        predefinedContacts.add(c2);

        writeContactsToFileHelper(predefinedContacts, TEST_CONTACT_FILE);

        Set<Contact> loadedContactsSet = loadContactsFromFileHelper(TEST_CONTACT_FILE);
        DefaultListModel<String> loadedLSTMDL = new DefaultListModel<>();
        refreshListModelFromSet(loadedContactsSet, loadedLSTMDL);

        if (loadedContactsSet.size() != 2) {
            throw new RuntimeException("testLoadContacts: Loaded contactsSet size should be 2, but was " + loadedContactsSet.size());
        }
        if (loadedLSTMDL.getSize() != 2) {
            throw new RuntimeException("testLoadContacts: Loaded contactsLSTMDL size should be 2, but was " + loadedLSTMDL.getSize());
        }

        boolean foundAlice = false, foundBob = false;
        for (Contact c : loadedContactsSet) {
            if (c.getFirstName().equals("Alice")) {
                foundAlice = true;
                if (c.getPhoneNumbers().size() != 1) throw new RuntimeException("testLoadContacts: Alice should have 1 number.");
            } else if (c.getFirstName().equals("Bob")) {
                foundBob = true;
                if (c.getPhoneNumbers().size() != 2) throw new RuntimeException("testLoadContacts: Bob should have 2 numbers.");
            }
        }
        if (!foundAlice || !foundBob) {
            throw new RuntimeException("testLoadContacts: Did not find Alice or Bob in loaded contacts.");
        }
    }
    
    public static void testUpdateContact() throws Exception {
        prepareTestFile();

        Set<Contact> initialContacts = new TreeSet<>();
        Contact contactToEdit = new Contact("Charlie", "Brown", "OldCity");
        contactToEdit.addPhoneNumber(new PhoneNumber("777-8888", "007"));
        initialContacts.add(contactToEdit);
        writeContactsToFileHelper(initialContacts, TEST_CONTACT_FILE);

        Set<Contact> sharedContactsSet = loadContactsFromFileHelper(TEST_CONTACT_FILE);
        DefaultListModel<String> sharedContactsLSTMDL = new DefaultListModel<>();
        refreshListModelFromSet(sharedContactsSet, sharedContactsLSTMDL);

        // Get the contact to update (assuming it's the first/only one for simplicity)
        Contact originalContact = null;
        for(Contact c : sharedContactsSet) { // Find Charlie Brown
            if(c.getFirstName().equals("Charlie")) {
                originalContact = c;
                break;
            }
        }
        if (originalContact == null) throw new RuntimeException("testUpdateContact: Could not find Charlie Brown to update.");
        
        // Find index in list model (this is a bit fragile but necessary for setElementAt)
        int originalContactIndex = -1;
        for(int i=0; i<sharedContactsLSTMDL.size(); i++) {
            if(sharedContactsLSTMDL.getElementAt(i).contains("Charlie Brown - OldCity")) {
                originalContactIndex = i;
                break;
            }
        }
        if (originalContactIndex == -1) throw new RuntimeException("testUpdateContact: Could not find Charlie Brown in list model.");


        // Simulate data that would be collected by UpdateContactFrame after user edits
        String updatedFirstName = "Charles";
        String updatedLastName = "Brown"; // Assuming last name remains same
        String updatedCity = "NewCity";
        TreeSet<PhoneNumber> updatedPnbrSet = new TreeSet<>();
        updatedPnbrSet.add(new PhoneNumber("777-8888", "007")); // Phone number string same, city/name updated

        Contact updatedContact = new Contact(updatedFirstName, updatedLastName, updatedCity);
        for (PhoneNumber p : updatedPnbrSet) {
            updatedContact.addPhoneNumber(p);
        }

        // Simulate UpdateContactFrame.saveUpdatedContact() logic
        if (sharedContactsSet.remove(originalContact)) {
            if (sharedContactsSet.add(updatedContact)) {
                sharedContactsLSTMDL.setElementAt(updatedFirstName + " " + updatedLastName + " - " + updatedCity, originalContactIndex);
            } else {
                sharedContactsSet.add(originalContact); // Rollback
                throw new RuntimeException("testUpdateContact: Failed to add updated contact to set (duplicate?).");
            }
        } else {
            throw new RuntimeException("testUpdateContact: Failed to remove original contact from set.");
        }
        
        writeContactsToFileHelper(sharedContactsSet, TEST_CONTACT_FILE);

        // Verification
        if (sharedContactsSet.size() != 1) {
            throw new RuntimeException("testUpdateContact: contactsSet size should still be 1 after update, but was " + sharedContactsSet.size());
        }
        Contact updatedContactInSet = sharedContactsSet.iterator().next(); // Assuming only one contact
        if (!updatedContactInSet.getFirstName().equals("Charles")) {
            throw new RuntimeException("testUpdateContact: Updated contact first name mismatch in set. Expected Charles, got " + updatedContactInSet.getFirstName());
        }
        if (!updatedContactInSet.getCity().equals("NewCity")) {
            throw new RuntimeException("testUpdateContact: Updated contact city mismatch in set.");
        }
        if (!sharedContactsLSTMDL.getElementAt(originalContactIndex).contains("Charles Brown - NewCity")) {
             throw new RuntimeException("testUpdateContact: Updated LSTMDL content mismatch. Got: " + sharedContactsLSTMDL.getElementAt(originalContactIndex));
        }

        Set<Contact> reloadedContactsSet = loadContactsFromFileHelper(TEST_CONTACT_FILE);
        if (reloadedContactsSet.size() != 1) {
            throw new RuntimeException("testUpdateContact: Reloaded contactsSet size should be 1, but was " + reloadedContactsSet.size());
        }
        Contact reloadedContact = reloadedContactsSet.iterator().next();
        if (!reloadedContact.getFirstName().equals("Charles")) {
            throw new RuntimeException("testUpdateContact: Reloaded contact first name mismatch.");
        }
    }
}
