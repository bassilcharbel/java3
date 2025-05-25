package NFA035_bassil_2443n.src; // Assuming classes like Contact are in this package based on file structure

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

public class Group implements Serializable {
    private static final long serialVersionUID = 1L; // Good practice for Serializable classes

    private String nom; // Name of the group
    private String description;
    private Set<Contact> contacts;

    public Group(String nom, String description) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Group name cannot be null or empty.");
        }
        this.nom = nom;
        this.description = description;
        this.contacts = new HashSet<>(); // Initialize the set
    }

    // Getters
    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public Set<Contact> getContacts() {
        return contacts; // Consider returning a copy if direct modification is not desired: new HashSet<>(contacts)
    }

    // Setters (optional, but can be useful)
    public void setNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Group name cannot be null or empty.");
        }
        this.nom = nom;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Contact management
    public boolean addContact(Contact contact) {
        if (contact == null) {
            return false;
        }
        return this.contacts.add(contact);
    }

    public boolean removeContact(Contact contact) {
        if (contact == null) {
            return false;
        }
        return this.contacts.remove(contact);
    }

    @Override
    public String toString() {
        return this.nom; // Simple representation, often used in UI lists
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(nom.toLowerCase(), group.nom.toLowerCase()); // Case-insensitive comparison for name
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom.toLowerCase()); // Case-insensitive for hash
    }
}
