package ourfood.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Domain object to represent an user
 * 
 \* @author raghu.mulukoju
 */
@Entity
public class Profile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;

    /**
     * user level variables and their values
     */
    @JsonIgnore
    @ElementCollection(fetch = FetchType.LAZY)
    @MapKeyColumn(name = "variable")
    @Column(name = "value")
    private Map<String, String> properties;

    private String profilePic;

    public Profile() {

    }

    public Map<String, String> getProperties() {
        if (properties == null) {
            properties = new HashMap<String, String>();
        }
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}