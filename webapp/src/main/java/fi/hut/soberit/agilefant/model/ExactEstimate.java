package fi.hut.soberit.agilefant.model;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonCreator;

import fi.hut.soberit.agilefant.util.XmlExactEstimateAdapter;

@SuppressWarnings("serial")
@Embeddable
@XmlAccessorType( XmlAccessType.NONE )
@XmlType
@XmlJavaTypeAdapter(XmlExactEstimateAdapter.class)
public class ExactEstimate extends Number implements Comparable<ExactEstimate> {

    public static final ExactEstimate ZERO = new ExactEstimate(0);
    
    private Long minorUnits;

    public ExactEstimate() {
    }

    @JsonCreator
    public ExactEstimate(long minorUnits) {
        this.minorUnits = minorUnits;
    }

    public void setMinorUnits(Long minorUnits) {
        this.minorUnits = minorUnits;
    }

    public Long getMinorUnits() {
        return minorUnits;
    }
    
    public int compareTo(ExactEstimate o) {
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
    
    public void add(Long minorUnits) {
        this.minorUnits += minorUnits;
    }
}
