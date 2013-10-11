package fi.hut.soberit.agilefant.model;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonCreator;

@SuppressWarnings("serial")
@Embeddable
public class SignedExactEstimate extends Number implements Comparable<SignedExactEstimate> {

    public static final SignedExactEstimate ZERO = new SignedExactEstimate(0);
    
    private Long minorUnits;

    public SignedExactEstimate() {
    }

    @JsonCreator
    public SignedExactEstimate(long minorUnits) {
        this.minorUnits = minorUnits;
    }

    public void setMinorUnits(Long minorUnits) {
        this.minorUnits = minorUnits;
    }

    public Long getMinorUnits() {
        return minorUnits;
    }
    
    public int compareTo(SignedExactEstimate o) {
        long thisMinorUnits = this.getMinorUnits();
        long otherMinorUnits = o.getMinorUnits();
        if (otherMinorUnits < thisMinorUnits) {
            return -1;
        }
        else if (otherMinorUnits > thisMinorUnits) {
            return 1;
        }
        return 0;
    }

    @Override
    public double doubleValue() {
        return this.minorUnits;
    }

    @Override
    public float floatValue() {
        return this.minorUnits;
    }

    @Override
    public int intValue() {
        return ((Long)this.minorUnits).intValue();
    }

    @Override
    public long longValue() {
        return this.minorUnits;
    }
    
    public String toString() {
        return String.valueOf(this.minorUnits);
    }
    
    public void add(long addee) {
        this.minorUnits += addee;
    }
}
