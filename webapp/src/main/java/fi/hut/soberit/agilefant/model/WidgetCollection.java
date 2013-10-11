package fi.hut.soberit.agilefant.model;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Entity
@Table(name = "widgetcollections")
@XmlAccessorType( XmlAccessType.NONE )
public class WidgetCollection {

    @Id
    @GeneratedValue
    private int id;
    
    @ManyToOne
    private User user;
    
    @Column(nullable = false)
    private String name;
    
    @OneToMany(
            cascade = CascadeType.REMOVE,
            targetEntity = fi.hut.soberit.agilefant.model.AgilefantWidget.class,
            mappedBy = "widgetCollection"
    )
    private Collection<AgilefantWidget> widgets = new HashSet<AgilefantWidget>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<AgilefantWidget> getWidgets() {
        return widgets;
    }

    public void setWidgets(Collection<AgilefantWidget> agilefantWidgets) {
        this.widgets = agilefantWidgets;
    }
}
