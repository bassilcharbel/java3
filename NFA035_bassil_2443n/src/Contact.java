import java.io.Serializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Objects;
import java.util.Set;

public class Contact extends Observable implements Serializable, Comparable<Contact> {
    private String firstName;
    private String lastName;
    private String city;
    List<PhoneNumber> pnbrList;
    private Set<String> groups = new HashSet<>();

    public Contact() {
        this.firstName = "";
        this.lastName = "";
        this.city = "";
        pnbrList = new ArrayList<>();
    }

    public Contact(String firstName, String lastName, String city) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.pnbrList = new ArrayList<>();
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getCity() { return city; }

    public boolean addPhoneNumber(PhoneNumber phoneNumber) {
        pnbrList.add(phoneNumber);
        setChanged();
        notifyObservers();
        return true;
    }

    public boolean removePhoneNumber(PhoneNumber p) {
        boolean removed = pnbrList.remove(p);
        if (removed) {
            setChanged();
            notifyObservers();
        }
        return removed;
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return pnbrList;
    }

    public Set<String> getGroups() {
        return this.groups;
    }

    public boolean addGroup(String groupName) {
        if (groupName != null && !groupName.trim().isEmpty()) {
            boolean added = this.groups.add(groupName.trim());
            if (added) { // only notify if the group was actually added
                setChanged();
                notifyObservers();
            }
            return added;
        }
        return false; // If groupName is null or empty, nothing was added.
    }

    public void removeGroup(String group) {
        if (group != null) {
            if (this.groups.remove(group)) {
                setChanged();
                notifyObservers();
            }
        }
    }

    public boolean isInGroup(String group) {
        return group != null && this.groups.contains(group);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Contact: ").append(firstName).append(" ").append(lastName).append(", City: ").append(city).append(", Groups: ").append(groups).append(", with ").append(pnbrList.size()).append(" numbers:\n");
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
        return Objects.equals(firstName, other.firstName) &&
               Objects.equals(lastName, other.lastName) &&
               Objects.equals(city, other.city) &&
               Objects.equals(pnbrList, other.pnbrList) &&
               Objects.equals(groups, other.groups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, city, pnbrList, groups);
    }

    @Override
    public int compareTo(Contact other) {
        if (other == null) {
            return 1; // Non-null is greater than null
        }
        int lastNameComp = (this.lastName == null && other.lastName == null) ? 0 :
                           (this.lastName == null) ? -1 :
                           (other.lastName == null) ? 1 :
                           this.lastName.compareToIgnoreCase(other.lastName);
        if (lastNameComp != 0) {
            return lastNameComp;
        }
        return (this.firstName == null && other.firstName == null) ? 0 :
               (this.firstName == null) ? -1 :
               (other.firstName == null) ? 1 :
               this.firstName.compareToIgnoreCase(other.firstName);
    }

    public static final Comparator<Contact> FirstNameComparator = new Comparator<Contact>() {
        @Override
        public int compare(Contact c1, Contact c2) {
            if (c1 == null && c2 == null) return 0;
            if (c1 == null) return -1;
            if (c2 == null) return 1;
            if (c1.getFirstName() == null && c2.getFirstName() == null) return 0;
            if (c1.getFirstName() == null) return -1;
            if (c2.getFirstName() == null) return 1;
            return c1.getFirstName().compareToIgnoreCase(c2.getFirstName());
        }
    };

    public static final Comparator<Contact> LastNameComparator = new Comparator<Contact>() {
        @Override
        public int compare(Contact c1, Contact c2) {
            if (c1 == null && c2 == null) return 0;
            if (c1 == null) return -1;
            if (c2 == null) return 1;
            if (c1.getLastName() == null && c2.getLastName() == null) return 0;
            if (c1.getLastName() == null) return -1;
            if (c2.getLastName() == null) return 1;
            return c1.getLastName().compareToIgnoreCase(c2.getLastName());
        }
    };

    public static final Comparator<Contact> CityComparator = new Comparator<Contact>() {
        @Override
        public int compare(Contact c1, Contact c2) {
            if (c1 == null && c2 == null) return 0;
            if (c1 == null) return -1;
            if (c2 == null) return 1;
            if (c1.getCity() == null && c2.getCity() == null) return 0;
            if (c1.getCity() == null) return -1;
            if (c2.getCity() == null) return 1;
            return c1.getCity().compareToIgnoreCase(c2.getCity());
        }
    };
}
