import java.io.Serializable;
import java.util.Objects;

public class PhoneNumber implements Comparable<PhoneNumber>, Serializable {
    String pnbr, region;

    public PhoneNumber(String pn, String r) {
        pnbr = pn;
        region = r;
    }

    public int compareTo(PhoneNumber phoneNumber) {
        return pnbr.compareToIgnoreCase(phoneNumber.pnbr);
    }

    public String getPnbr() { return pnbr; }
    public String getRegion() { return region; }

    @Override
    public String toString() {
        return String.format("PhoneNumber[pnbr=%s, region=%s]",
                pnbr, region);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneNumber)) return false;
        PhoneNumber other = (PhoneNumber) o;
        return pnbr.equalsIgnoreCase(other.pnbr) &&
               region.equalsIgnoreCase(other.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pnbr.toLowerCase(), region.toLowerCase());
    }
}
