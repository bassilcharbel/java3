import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Group implements Serializable, Comparable<Group> {

    private static final long serialVersionUID = 1L; // Recommended for Serializable classes

    private String groupName;
    private String description;
    private List<Contact> contacts;

    public Group(String groupName, String description) {
        this.groupName = groupName;
        this.description = description;
        this.contacts = new ArrayList<>();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void addContact(Contact contact) {
        if (contact != null) {
            this.contacts.add(contact);
        }
    }

    public void removeContact(Contact contact) {
        this.contacts.remove(contact);
    }

    @Override
    public String toString() {
        return "Group: " + groupName + ", Description: " + description + ", Contacts: " + contacts.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(groupName, group.groupName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupName);
    }

    @Override
    public int compareTo(Group other) {
        if (this.groupName == null && other.groupName == null) {
            return 0;
        }
        if (this.groupName == null) {
            return -1; // nulls first
        }
        if (other.groupName == null) {
            return 1; // nulls first
        }
        return this.groupName.compareToIgnoreCase(other.groupName);
    }
}

// Assuming Contact class exists, for example:
/*

import java.io.Serializable;
import java.util.Objects;

public class Contact implements Serializable {
    private String name;
    private String phoneNumber;

    public Contact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Contact{" +
               "name='" + name + '\'' +
               ", phoneNumber='" + phoneNumber + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(name, contact.name) &&
               Objects.equals(phoneNumber, contact.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phoneNumber);
    }
}
*/
