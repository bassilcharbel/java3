import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Objects;

public class Contact extends Observable implements Serializable, Comparable<Contact> {
    List<PhoneNumber> pnbrList;

    public Contact() {
        pnbrList = new ArrayList<>();
    }

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Contact with ").append(pnbrList.size()).append(" numbers:\n");
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
        return pnbrList.equals(other.pnbrList);
    }

    @Override
    public int hashCode() {
        return pnbrList.hashCode();
    }

    @Override
    public int compareTo(Contact other) {
        // Simple comparison based on toString(). 
        // A more robust implementation would compare specific fields.
        if (other == null) {
            return 1; // Non-null is greater than null
        }
        return this.toString().compareTo(other.toString());
    }
}
