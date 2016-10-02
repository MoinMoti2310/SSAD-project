package ourfood.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ourfood.domain.enums.UserType;
import reactor.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Domain object to represent an user
 * 
 * @author raghu.mulukoju
 */
@Entity
public class User implements Serializable, UserDetails {
    @Column(unique = true)
    private String registeredMobile;



    /**
     * Default serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;

    /**
     * encrypted password of the user. Not available via json
     */
    @JsonIgnore
    private String password;

    /**
     * primary email address of the user. This is the identity of the user
     */
    @Column(unique = true)
    @NotBlank
    private String primaryEmail;
    @Column(unique = true)
    private String registeredEmail;

    /**
     * Verification status of the user. This is same as verification of primary email
     */
    private Boolean isVerified = false;

    /**
     * User can be suspended by any of the admins or the user themselves can deactivate their account
     */
    private Boolean isActive = true;

    /**
     * facebook id of the user
     */
    private String facebookId;

    /**
     * Google Id of the user
     */
    private String googleId;

    /**
     * First Name of the user
     */
    private String firstName;

    /**
     * Last Name of the user
     */
    private String lastName;

    /**
     * GCM Toke for Google push notifications
     */
    private String gcmToken;

    /**
     * Created date of the user
     */
    private Date createdDate;

    /**
     * UserType (MOBILE/WEB/MOBILEWEB/API)
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "INT DEFAULT 1")
    @NotNull
    private UserType userType;

    /**
     * Organization in which the user is present
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;

    /**
     * Roles assigned to the user
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;

    /**
     * Authorized action sets
     */
    private String authorizedActionSetIds;

    /**
     * profile of the user
     */
    @OneToOne(fetch = FetchType.LAZY)
    private Profile profile;

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return primaryEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public String getRegisteredMobile() {
        return registeredMobile;
    }

    public void setRegisteredMobile(String registeredMobile) {
        this.registeredMobile = registeredMobile;
    }

    public String getRegisteredEmail() {
        return registeredEmail;
    }

    public void setRegisteredEmail(String registeredEmail) {
        this.registeredEmail = registeredEmail;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<SimpleGrantedAuthority> authorities = new HashSet<SimpleGrantedAuthority>();

        // Add both roles and permissions
        if (roles != null) {
            for (Role role : roles) {
                authorities.add(new SimpleGrantedAuthority(role.getCode()));

                for (Permission permission : role.getPermissions()) {
                    authorities.add(new SimpleGrantedAuthority(permission.getCode()));
                }
            }
        }

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        boolean ret = isActive;
        if (organization != null) {
            ret = ret && organization.getIsActive();
        }
        return ret;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO implement this feature if required
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isVerified;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setRole(Role role) {

        if (this.roles == null) {
            this.roles = new ArrayList<Role>();
        }

        this.roles.add(role);
    }

    public void setOnlyRole(Role role) {

        if (role != null) {

            this.roles = new ArrayList<Role>();
            this.roles.add(role);
        }
    }

    public boolean hasRole(RoleType role) {

        Assert.notNull(role, "Undefined Role");

        if (roles != null) {

            for (Role role1 : roles) {

                if (role1.getCode().equals(role.toString())) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean hasRole(Permissions permission) {

        Assert.notNull(permission, "Undefined Permission");

        // Add both roles and permissions
        if (roles != null) {

            for (Role role : roles) {

                for (Permission permission1 : role.getPermissions()) {
                    if (permission1.getCode().equals(permission.toString())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Verifies if the user has either role OR permission
     * 
     * @param role
     * @return
     */
    public boolean hasRole(String role) {

        // Platform admins bypass all authorizations
        if (hasRole(RoleType.ROLE_PLATFORM_POWER_ADMIN) || hasRole(RoleType.ROLE_PLATFORM_PRI_ADMIN)) {
            return true;
        }

        boolean flag = false;

        /**
         * Checks if it starts with PERM and enters respective try block
         * 
         */
        if (role.startsWith("PERM")) {
            try {

                flag = hasRole(Permissions.valueOf(role));

                if (flag) {
                    return flag;
                }

            } catch (IllegalArgumentException e) {
            }
        } else {
            try {

                flag = hasRole(RoleType.valueOf(role));

                if (flag) {
                    return flag;
                }

            } catch (IllegalArgumentException e) {
                
            }
        }

        return flag;
    }
    
    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getGcmToken() {
        return gcmToken;
    }

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }

    public String getAuthorizedActionSetIds() {
        return authorizedActionSetIds;
    }

    public void setAuthorizedActionSetIds(String authorizedActionSetIds) {
        this.authorizedActionSetIds = authorizedActionSetIds;
    }
}