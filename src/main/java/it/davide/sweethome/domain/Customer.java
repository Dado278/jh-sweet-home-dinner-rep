package it.davide.sweethome.domain;

import io.swagger.annotations.ApiModelProperty;
import it.davide.sweethome.domain.enumeration.Gender;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Pattern(regexp = "^[a-z0-9_-]{3,16}$")
    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "freshman")
    private Long freshman;

    /**
     * matricola
     */
    @Pattern(regexp = "^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$")
    @ApiModelProperty(value = "matricola")
    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "update_date")
    private Instant updateDate;

    @OneToOne
    @JoinColumn(unique = true)
    private User internalUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer id(Long id) {
        this.id = id;
        return this;
    }

    public String getNickname() {
        return this.nickname;
    }

    public Customer nickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getFreshman() {
        return this.freshman;
    }

    public Customer freshman(Long freshman) {
        this.freshman = freshman;
        return this;
    }

    public void setFreshman(Long freshman) {
        this.freshman = freshman;
    }

    public String getEmail() {
        return this.email;
    }

    public Customer email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Customer phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Customer gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public Customer createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getUpdateDate() {
        return this.updateDate;
    }

    public Customer updateDate(Instant updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public void setUpdateDate(Instant updateDate) {
        this.updateDate = updateDate;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public Customer internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return id != null && id.equals(((Customer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", nickname='" + getNickname() + "'" +
            ", freshman=" + getFreshman() +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", gender='" + getGender() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", updateDate='" + getUpdateDate() + "'" +
            "}";
    }
}
