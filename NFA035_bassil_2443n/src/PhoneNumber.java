import java.io.Serializable;
import java.util.Objects;

public class PhoneNumber implements Comparable<PhoneNumber>, Serializable {
    String pnbr, city, firstName, lastName, region;

    public PhoneNumber(String pn, String c, String fn, String ln, String r) {
        pnbr = pn;
        city = c;
        firstName = fn;
        lastName = ln;
        region = r;
    }

    public int compareTo(PhoneNumber phoneNumber) {
        return pnbr.compareToIgnoreCase(phoneNumber.pnbr);
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPnbr() { return pnbr; }
    public String getCity() { return city; }
    public String getRegion() { return region; }

    @Override
    public String toString() {
        return String.format("PhoneNumber[pnbr=%s, city=%s, firstName=%s, lastName=%s, region=%s]",
                pnbr, city, firstName, lastName, region);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneNumber)) return false;
        PhoneNumber other = (PhoneNumber) o;
        return pnbr.equalsIgnoreCase(other.pnbr) &&
               city.equalsIgnoreCase(other.city) &&
               firstName.equalsIgnoreCase(other.firstName) &&
               lastName.equalsIgnoreCase(other.lastName) &&
               region.equalsIgnoreCase(other.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pnbr.toLowerCase(), city.toLowerCase(), firstName.toLowerCase(),
                lastName.toLowerCase(), region.toLowerCase());
    }
}
