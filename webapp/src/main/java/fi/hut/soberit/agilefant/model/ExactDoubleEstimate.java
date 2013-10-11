package fi.hut.soberit.agilefant.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import fi.hut.soberit.agilefant.util.XmlExactEstimateAdapter;

@SuppressWarnings("serial")
@Embeddable
@XmlAccessorType( XmlAccessType.NONE )
@XmlType
@XmlJavaTypeAdapter(XmlExactEstimateAdapter.class)
public class ExactDoubleEstimate extends Number implements Comparable<ExactDoubleEstimate> {

    public static final ExactDoubleEstimate ZERO = new ExactDoubleEstimate(0);
    
    private Double minorUnits;

    public ExactDoubleEstimate() {
    }

    public ExactDoubleEstimate(double minorUnits) {
        this.minorUnits = minorUnits;
    }

    public void setMinorUnits(Double minorUnits) {
        this.minorUnits = minorUnits;
    }

    public Double getMinorUnits() {
        DecimalFormat df = new DecimalFormat("#.0");
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        return Double.valueOf((df.format(minorUnits)));
    }
    
    public int compareTo(ExactDoubleEstimate o) {
        double thisMinorUnits = this.getMinorUnits();
        double otherMinorUnits = o.getMinorUnits();
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
        return this.minorUnits.floatValue();
    }

    @Override
    public int intValue() {
        return this.minorUnits.intValue();
    }

    @Override
    public long longValue() {
        return this.minorUnits.longValue();
    }
    
    public String toString() {
        return String.valueOf(this.minorUnits);
    }
    
    public void add(Double minorUnits) {
        this.minorUnits += minorUnits;
    }
}
