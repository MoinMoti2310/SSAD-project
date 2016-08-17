package ourfood.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Domain object to represent an Organization. Ever user is bound to an organization
 * 
 * @author raghu.mulukoju
 * 
 */
@Entity
@JsonInclude(Include.NON_EMPTY)
public class Organization implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * organization id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    /**
     * organization name
     */
    String name;
    /**
     * organization description
     */
    String description;
    /**
     * organization type
     */
    String type;
    /**
     * organization size
     */
    String size;
    /**
     * organization url
     */
    String url;
    /**
     * This has to be explicitly set to active by power user
     */
    Boolean isActive = false;
    /**
     * This decides if the org is pending approval or not
     */
    Boolean isPending = true;

    /**
     * parent organization
     */
    @ManyToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    Organization parent;

    /**
     * Direct children of this org
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
    @JsonManagedReference
    Set<Organization> children;

    public Organization getParent() {
        return parent;
    }

    public void setParent(Organization parent) {
        this.parent = parent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Organization> getChildren() {
        return children;
    }

    public void setChildren(Set<Organization> children) {
        this.children = children;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getIsPending() {
        return isPending;
    }

    public void setIsPending(Boolean isPending) {
        this.isPending = isPending;
    }
}
