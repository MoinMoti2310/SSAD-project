package ourfood.form;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.core.env.Environment;

import ourfood.domain.RoleType;
import ourfood.domain.enums.UserType;

public class Register {
    // to check which profile is active
    Environment env;

    @NotNull
    @NotEmpty
    // TODO custom @UniqueEmail validation is failing for some reason.
    String primaryEmail;
    @NotNull
    @NotEmpty
    String password;

    String firstName;

    String lastName;

    Long org;

    RoleType role;

    UserType userType;

    @AssertTrue
    public boolean isPasswordStrong() {
        if (null == this.password)
            return false;
        // TODO the env variable is always null. Check it
        // if (null == env || env.acceptsProfiles("development")) {
        // return true;
        // }
        // TODO: Do the actual validation here
        return true;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Long getOrg() {
        return org;
    }

    public void setOrg(Long org) {
        this.org = org;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public RoleType getRole() {
        return role;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public UserType getUserType() {
        return userType;
    }
}
