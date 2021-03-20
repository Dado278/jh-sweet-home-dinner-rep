package it.davide.sweethome.domain;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

import it.davide.sweethome.domain.enumeration.PersonType;

import it.davide.sweethome.domain.enumeration.Gender;

/**
 * A People.
 */
@Entity
@Table(name = "people")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "people")
public class People implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "freshman")
    private Long freshman;

    /**
     * matricola
     */
    @ApiModelProperty(value = "matricola")
    @Enumerated(EnumType.STRING)
    @Column(name = "person_type")
    private PersonType personType;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "token")
    private String token;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "update_date")
    private Instant updateDate;

    @OneToOne
    @JoinColumn(unique = true)
    private InfoInnkeeper infoInnkeeper;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public People username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public People password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getFreshman() {
        return freshman;
    }

    public People freshman(Long freshman) {
        this.freshman = freshman;
        return this;
    }

    public void setFreshman(Long freshman) {
        this.freshman = freshman;
    }

    public PersonType getPersonType() {
        return personType;
    }

    public People personType(PersonType personType) {
        this.personType = personType;
        return this;
    }

    public void setPersonType(PersonType personType) {
        this.personType = personType;
    }

    public String getEmail() {
        return email;
    }

    public People email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public People phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Gender getGender() {
        return gender;
    }

    public People gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getToken() {
        return token;
    }

    public People token(String token) {
        this.token = token;
        return this;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public People createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getUpdateDate() {
        return updateDate;
    }

    public People updateDate(Instant updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public void setUpdateDate(Instant updateDate) {
        this.updateDate = updateDate;
    }

    public InfoInnkeeper getInfoInnkeeper() {
        return infoInnkeeper;
    }

    public People infoInnkeeper(InfoInnkeeper infoInnkeeper) {
        this.infoInnkeeper = infoInnkeeper;
        return this;
    }

    public void setInfoInnkeeper(InfoInnkeeper infoInnkeeper) {
        this.infoInnkeeper = infoInnkeeper;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof People)) {
            return false;
        }
        return id != null && id.equals(((People) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "People{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", freshman=" + getFreshman() +
            ", personType='" + getPersonType() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", gender='" + getGender() + "'" +
            ", token='" + getToken() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", updateDate='" + getUpdateDate() + "'" +
            "}";
    }
}
