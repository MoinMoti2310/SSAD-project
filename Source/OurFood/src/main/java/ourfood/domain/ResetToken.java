package ourfood.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity to hold the reset token of a user. The token can be used for any purpose like validation or forgot password
 * 
 \* @author raghu.mulukoju
 */
// TODO validate email and mobile also
@Entity
public class ResetToken implements Serializable {
    /**
     * id of the reset token. Not available via JSON
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    Long id;
    /**
     * token is valid till this time
     */
    @Column
    Date validTru;
    /**
     * the actual token
     */
    @Column(unique = true)
    String token;
    /**
     * the user this token pertains to
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getValidTru() {
        return validTru;
    }

    public void setValidTru(Date validTru) {
        this.validTru = validTru;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getUserId() {
        return this.user.getId();
    }
}
