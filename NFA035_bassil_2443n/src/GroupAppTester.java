import java.io.*;
import java.util.*;
// No Swing models directly needed for these tests unless we use UI helper methods,
// which we are not planning to for this data-centric test suite.

// Assuming Contact.java, Group.java, PhoneNumber.java are in the same default package and compiled.

public class GroupAppTester {

    private static final String TEST_GROUP_FILE = "group_test_suite.dat";
    private static final String TEST_CONTACT_FILE_FOR_GROUPS = "contact_test_suite_for_groups.dat";
    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("Starting Group Functionality Tests...");
        System.out.println("=====================================");

        runTest("testSaveNewGroup", GroupAppTester::testSaveNewGroup);
        runTest("testLoadGroups", GroupAppTester::testLoadGroups);
        runTest("testUpdateGroup", GroupAppTester::testUpdateGroup);
        runTest("testDeleteGroup", GroupAppTester::testDeleteGroup);
        runTest("testAddContactToGroup", GroupAppTester::testAddContactToGroup);
        runTest("testRemoveContactFromGroup", GroupAppTester::testRemoveContactFromGroup);

        System.out.println("\n=====================================");
        System.out.println("Group Test Suite Summary:");
        System.out.println("Tests Passed: " + testsPassed);
        System.out.println("Tests Failed: " + testsFailed);
        System.out.println("=====================================");

        cleanupTestFiles(); // Clean up at the very end
    }

    private static void runTest(String testName, TestRunner testRunner) {
        System.out.println("\nRunning " + testName + "...");
        try {
            prepareTestFiles(); // Prepare fresh files for each test
            boolean result = testRunner.run();
            if (result) {
                System.out.println(testName + ": PASSED");
                testsPassed++;
            } else {
                System.err.println(testName + ": FAILED");
                testsFailed++;
            }
        } catch (Exception e) {
            System.err.println(testName + ": FAILED with exception - " + e.getMessage());
            e.printStackTrace(System.err);
            testsFailed++;
        }
    }

    @FunctionalInterface
    interface TestRunner {
        boolean run() throws Exception;
    }

    // --- Helper Methods ---

    private static void prepareTestFiles() {
        System.out.println("  Preparing test files...");
        File groupFile = new File(TEST_GROUP_FILE);
        File contactFile = new File(TEST_CONTACT_FILE_FOR_GROUPS);
        if (groupFile.exists()) {
            if(!groupFile.delete()){
                 System.err.println("  Warning: Could not delete existing test group file: " + TEST_GROUP_FILE);
            }
        }
        if (contactFile.exists()) {
             if(!contactFile.delete()){
                 System.err.println("  Warning: Could not delete existing test contact file: " + TEST_CONTACT_FILE_FOR_GROUPS);
            }
        }
    }

    private static void cleanupTestFiles() {
        System.out.println("\nCleaning up test files...");
        File groupFile = new File(TEST_GROUP_FILE);
        File contactFile = new File(TEST_CONTACT_FILE_FOR_GROUPS);
        if (groupFile.exists()) {
            if (groupFile.delete()) {
                System.out.println("  Deleted: " + TEST_GROUP_FILE);
            } else {
                System.err.println("  Error: Could not delete test file: " + TEST_GROUP_FILE);
            }
        }
        if (contactFile.exists()) {
            if (contactFile.delete()) {
                System.out.println("  Deleted: " + TEST_CONTACT_FILE_FOR_GROUPS);
            } else {
                System.err.println("  Error: Could not delete test file: " + TEST_CONTACT_FILE_FOR_GROUPS);
            }
        }
    }

    private static void writeGroupsToFileHelper(Set<Group> groups, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(groups);
        }
    }

    @SuppressWarnings("unchecked")
    private static Set<Group> loadGroupsFromFileHelper(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Object obj = ois.readObject();
            if (obj instanceof Set) {
                // Basic check, could be more robust by checking elements
                return (Set<Group>) obj;
            }
            throw new ClassCastException("File content is not a Set of Group objects.");
        }
    }
    
    private static void writeContactsToFileHelper(Set<Contact> contacts, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(contacts);
        }
    }

    @SuppressWarnings("unchecked")
    private static Set<Contact> loadContactsFromFileHelper(String filename) throws IOException, ClassNotFoundException {
         try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Object obj = ois.readObject();
            if (obj instanceof Set) {
                return (Set<Contact>) obj;
            }
            throw new ClassCastException("File content is not a Set of Contact objects.");
        }
    }

    private static Set<Contact> createSampleContacts() {
        Set<Contact> contacts = new TreeSet<>();
        Contact c1 = new Contact(); // Assuming default constructor exists
        c1.addPhoneNumber(new PhoneNumber("111-0001", "CityA", "Alice", "Wonder", "R1"));
        contacts.add(c1);

        Contact c2 = new Contact();
        c2.addPhoneNumber(new PhoneNumber("222-0002", "CityB", "Bob", "Builder", "R2"));
        contacts.add(c2);
        
        Contact c3 = new Contact();
        c3.addPhoneNumber(new PhoneNumber("333-0003", "CityA", "Charlie", "Chaplin", "R3"));
        contacts.add(c3);
        return contacts;
    }

    // --- Test Methods ---

    private static boolean testSaveNewGroup() throws Exception {
        System.out.println("  testSaveNewGroup: Setting up...");
        Set<Group> groupsSet = new TreeSet<>();
        Group newGroup = new Group("Test Group 1", "Description 1");
        
        Set<Contact> sampleContacts = createSampleContacts();
        Iterator<Contact> contactIterator = sampleContacts.iterator();
        if(contactIterator.hasNext()) newGroup.addContact(contactIterator.next());
        if(contactIterator.hasNext()) newGroup.addContact(contactIterator.next());

        groupsSet.add(newGroup);
        writeGroupsToFileHelper(groupsSet, TEST_GROUP_FILE);
        System.out.println("  testSaveNewGroup: Group saved.");

        if (groupsSet.size() != 1) {
            System.err.println("  testSaveNewGroup: Failure - Original groupsSet size is not 1 after adding.");
            return false;
        }

        Set<Group> loadedGroups = loadGroupsFromFileHelper(TEST_GROUP_FILE);
        System.out.println("  testSaveNewGroup: Groups loaded from file.");

        if (loadedGroups.size() != 1) {
            System.err.println("  testSaveNewGroup: Failure - Loaded groupsSet size is not 1. Got: " + loadedGroups.size());
            return false;
        }
        Group loadedGroup = loadedGroups.iterator().next();
        if (!"Test Group 1".equals(loadedGroup.getGroupName())) {
            System.err.println("  testSaveNewGroup: Failure - Group name mismatch. Expected 'Test Group 1', Got: " + loadedGroup.getGroupName());
            return false;
        }
        if (!"Description 1".equals(loadedGroup.getDescription())) {
            System.err.println("  testSaveNewGroup: Failure - Group description mismatch.");
            return false;
        }
        if (loadedGroup.getContacts().size() != 2) {
             System.err.println("  testSaveNewGroup: Failure - Group contacts count mismatch. Expected 2, Got: " + loadedGroup.getContacts().size());
            return false;
        }
        System.out.println("  testSaveNewGroup: Verification successful.");
        return true;
    }

    private static boolean testLoadGroups() throws Exception {
        System.out.println("  testLoadGroups: Setting up...");
        Set<Contact> sampleContacts = createSampleContacts();
        Iterator<Contact> contactIterator = sampleContacts.iterator();
        Contact c1 = contactIterator.hasNext() ? contactIterator.next() : null;

        Set<Group> predefinedGroups = new TreeSet<>();
        Group g1 = new Group("Alpha Group", "A desc");
        if (c1 != null) g1.addContact(c1);
        predefinedGroups.add(g1);
        predefinedGroups.add(new Group("Beta Group", "B desc (no contacts)"));
        
        writeGroupsToFileHelper(predefinedGroups, TEST_GROUP_FILE);
        System.out.println("  testLoadGroups: Groups saved.");

        Set<Group> loadedGroups = loadGroupsFromFileHelper(TEST_GROUP_FILE);
        System.out.println("  testLoadGroups: Groups loaded.");

        if (loadedGroups.size() != predefinedGroups.size()) {
            System.err.println("  testLoadGroups: Failure - Size mismatch. Expected " + predefinedGroups.size() + ", Got: " + loadedGroups.size());
            return false;
        }
        for (Group originalG : predefinedGroups) {
            boolean found = false;
            for (Group loadedG : loadedGroups) {
                if (originalG.equals(loadedG) && // Relies on Group.equals() comparing groupName
                    originalG.getDescription().equals(loadedG.getDescription()) &&
                    originalG.getContacts().size() == loadedG.getContacts().size() &&
                    new HashSet<>(originalG.getContacts()).equals(new HashSet<>(loadedG.getContacts()))) { // Order might not be guaranteed in List
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.err.println("  testLoadGroups: Failure - Group " + originalG.getGroupName() + " not found or content mismatch in loaded data.");
                return false;
            }
        }
        System.out.println("  testLoadGroups: Verification successful.");
        return true;
    }

    private static boolean testUpdateGroup() throws Exception {
        System.out.println("  testUpdateGroup: Setting up...");
        Set<Group> groups = new TreeSet<>();
        Group g1 = new Group("Original Name", "Original Desc");
        Set<Contact> contacts = createSampleContacts();
        Contact c1 = contacts.iterator().next();
        g1.addContact(c1);
        groups.add(g1);
        writeGroupsToFileHelper(groups, TEST_GROUP_FILE);
        System.out.println("  testUpdateGroup: Initial group saved.");

        // Load, modify, save again
        Set<Group> loadedForUpdate = loadGroupsFromFileHelper(TEST_GROUP_FILE);
        Group groupToUpdate = null;
        for(Group g : loadedForUpdate) { if("Original Name".equals(g.getGroupName())) groupToUpdate = g; }

        if (groupToUpdate == null) {
             System.err.println("  testUpdateGroup: Failure - Could not find 'Original Name' group for update.");
             return false;
        }
        
        // Critical: If Group's equals/hashCode depends on name, remove and re-add if name changes
        loadedForUpdate.remove(groupToUpdate); // Remove before changing name
        groupToUpdate.setGroupName("Updated Name");
        groupToUpdate.setDescription("Updated Desc");
        Contact c2 = null;
        Iterator<Contact> it = contacts.iterator(); it.next(); if(it.hasNext()) c2 = it.next();
        if (c2 != null) groupToUpdate.addContact(c2); // Add another contact
        groupToUpdate.removeContact(c1); // Remove original contact
        loadedForUpdate.add(groupToUpdate); // Re-add with new name
        
        writeGroupsToFileHelper(loadedForUpdate, TEST_GROUP_FILE);
        System.out.println("  testUpdateGroup: Updated group saved.");

        Set<Group> loadedAfterUpdate = loadGroupsFromFileHelper(TEST_GROUP_FILE);
        if (loadedAfterUpdate.size() != 1) {
            System.err.println("  testUpdateGroup: Failure - Expected 1 group after update, Got: " + loadedAfterUpdate.size());
            return false;
        }
        Group updatedGroup = loadedAfterUpdate.iterator().next();
        if (!"Updated Name".equals(updatedGroup.getGroupName())) {
            System.err.println("  testUpdateGroup: Failure - Group name not updated. Expected 'Updated Name', Got: " + updatedGroup.getGroupName());
            return false;
        }
         if (!"Updated Desc".equals(updatedGroup.getDescription())) {
            System.err.println("  testUpdateGroup: Failure - Group description not updated.");
            return false;
        }
        if (updatedGroup.getContacts().size() != 1) { // Was 1, removed 1, added 1. So should be 1.
            System.err.println("  testUpdateGroup: Failure - Contacts count incorrect. Expected 1, Got: " + updatedGroup.getContacts().size());
            return false;
        }
        if (updatedGroup.getContacts().contains(c1)) {
             System.err.println("  testUpdateGroup: Failure - Contact c1 was not removed.");
            return false;
        }
        if (c2 != null && !updatedGroup.getContacts().contains(c2)) {
             System.err.println("  testUpdateGroup: Failure - Contact c2 was not added.");
            return false;
        }
        System.out.println("  testUpdateGroup: Verification successful.");
        return true;
    }

    private static boolean testDeleteGroup() throws Exception {
        System.out.println("  testDeleteGroup: Setting up...");
        Set<Group> groups = new TreeSet<>();
        Group g1 = new Group("GroupToDelete", "Delete Me");
        Group g2 = new Group("GroupToKeep", "Keep Me");
        groups.add(g1);
        groups.add(g2);
        writeGroupsToFileHelper(groups, TEST_GROUP_FILE);
        System.out.println("  testDeleteGroup: Initial groups saved.");

        Set<Group> loadedForDelete = loadGroupsFromFileHelper(TEST_GROUP_FILE);
        Group groupToDelete = null;
        for(Group g : loadedForDelete) { if("GroupToDelete".equals(g.getGroupName())) groupToDelete = g; }

        if (groupToDelete == null) {
             System.err.println("  testDeleteGroup: Failure - Could not find 'GroupToDelete' for deletion.");
             return false;
        }
        loadedForDelete.remove(groupToDelete);
        writeGroupsToFileHelper(loadedForDelete, TEST_GROUP_FILE);
        System.out.println("  testDeleteGroup: Group deleted and list saved.");

        Set<Group> loadedAfterDelete = loadGroupsFromFileHelper(TEST_GROUP_FILE);
        if (loadedAfterDelete.size() != 1) {
            System.err.println("  testDeleteGroup: Failure - Expected 1 group after deletion, Got: " + loadedAfterDelete.size());
            return false;
        }
        Group keptGroup = loadedAfterDelete.iterator().next();
        if (!"GroupToKeep".equals(keptGroup.getGroupName())) {
            System.err.println("  testDeleteGroup: Failure - Incorrect group remaining. Expected 'GroupToKeep', Got: " + keptGroup.getGroupName());
            return false;
        }
        System.out.println("  testDeleteGroup: Verification successful.");
        return true;
    }

    private static boolean testAddContactToGroup() throws Exception {
        System.out.println("  testAddContactToGroup: Setting up...");
        Set<Contact> sampleContacts = createSampleContacts();
        // writeContactsToFileHelper(sampleContacts, TEST_CONTACT_FILE_FOR_GROUPS); // Not strictly necessary for this test

        Group group = new Group("Contacts Test Group", "Testing adding contacts");
        if (sampleContacts.isEmpty()) {
            System.err.println("  testAddContactToGroup: Failure - No sample contacts to add.");
            return false;
        }
        Contact contactToAdd = sampleContacts.iterator().next();
        
        group.addContact(contactToAdd);
        System.out.println("  testAddContactToGroup: Contact added to group object.");

        if (group.getContacts().size() != 1) {
            System.err.println("  testAddContactToGroup: Failure - Group contacts size not 1. Got: " + group.getContacts().size());
            return false;
        }
        if (!group.getContacts().contains(contactToAdd)) {
            System.err.println("  testAddContactToGroup: Failure - Group does not contain the added contact.");
            return false;
        }
        
        // Optional: Save and reload to verify persistence of contacts in group
        Set<Group> groupsForSave = new TreeSet<>();
        groupsForSave.add(group);
        writeGroupsToFileHelper(groupsForSave, TEST_GROUP_FILE);
        Set<Group> reloadedGroups = loadGroupsFromFileHelper(TEST_GROUP_FILE);
        if(reloadedGroups.isEmpty() || !reloadedGroups.iterator().next().getContacts().contains(contactToAdd)){
             System.err.println("  testAddContactToGroup: Failure - Contact not persisted in group after save/load.");
            return false;
        }

        System.out.println("  testAddContactToGroup: Verification successful.");
        return true;
    }

    private static boolean testRemoveContactFromGroup() throws Exception {
        System.out.println("  testRemoveContactFromGroup: Setting up...");
        Set<Contact> sampleContacts = createSampleContacts();
        Iterator<Contact> it = sampleContacts.iterator();
        Contact c1 = it.hasNext() ? it.next() : null;
        Contact c2 = it.hasNext() ? it.next() : null;

        if (c1 == null || c2 == null) {
            System.err.println("  testRemoveContactFromGroup: Failure - Need at least two sample contacts for this test.");
            return false;
        }

        Group group = new Group("Removal Test", "Testing removing contacts");
        group.addContact(c1);
        group.addContact(c2);
        System.out.println("  testRemoveContactFromGroup: Contacts c1, c2 added. Initial size: " + group.getContacts().size());

        if (group.getContacts().size() != 2) {
             System.err.println("  testRemoveContactFromGroup: Failure - Initial size not 2 after adding contacts.");
            return false;
        }

        group.removeContact(c1);
        System.out.println("  testRemoveContactFromGroup: Contact c1 removed. Size now: " + group.getContacts().size());

        if (group.getContacts().size() != 1) {
            System.err.println("  testRemoveContactFromGroup: Failure - Group contacts size not 1 after removal. Got: " + group.getContacts().size());
            return false;
        }
        if (group.getContacts().contains(c1)) {
            System.err.println("  testRemoveContactFromGroup: Failure - Group still contains removed contact c1.");
            return false;
        }
        if (!group.getContacts().contains(c2)) {
            System.err.println("  testRemoveContactFromGroup: Failure - Group does not contain contact c2 which should remain.");
            return false;
        }
        System.out.println("  testRemoveContactFromGroup: Verification successful.");
        return true;
    }
}
