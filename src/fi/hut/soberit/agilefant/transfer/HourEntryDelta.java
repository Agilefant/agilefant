package fi.hut.soberit.agilefant.transfer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import flexjson.JSON;

@XmlType
@XmlAccessorType( XmlAccessType.NONE )
public class HourEntryDelta {

    private long delta;

    @JSON
    @XmlAttribute
    public long getDelta() {
        return delta;
    }

    public void setDelta(long delta) {
        this.delta = delta;
    }
}

