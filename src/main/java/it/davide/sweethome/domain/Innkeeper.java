package it.davide.sweethome.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import it.davide.sweethome.domain.enumeration.Gender;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Innkeeper.
 */
@Entity
@Table(name = "innkeeper")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "innkeeper")
public class Innkeeper implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Pattern(regexp = "^[a-z0-9_-]{3,16}$")
    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @Column(name = "freshman", unique = true)
    private Long freshman;

    /**
     * matricola
     */
    @NotNull
    @Pattern(regexp = "^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$")
    @ApiModelProperty(value = "matricola", required = true)
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Pattern(regexp = "^[0-9]{9,15}$")
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "slogan")
    private String slogan;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Pattern(regexp = "^(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*\\/?$")
    @Column(name = "home_page")
    private String homePage;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "services")
    private String services;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "update_date")
    private Instant updateDate;

    @OneToOne
    @JoinColumn(unique = true)
    private User internalUser;

    @OneToMany(mappedBy = "innkeeper")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "innkeeper", "customers" }, allowSetters = true)
    private Set<SharedDinner> sharedDinners = new HashSet<>();

    @OneToMany(mappedBy = "innkeeper")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "innkeeper" }, allowSetters = true)
    private Set<TakeAway> takeAways = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Innkeeper id(Long id) {
        this.id = id;
        return this;
    }

    public String getNickname() {
        return this.nickname;
    }

    public Innkeeper nickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getFreshman() {
        return this.freshman;
    }

    public Innkeeper freshman(Long freshman) {
        this.freshman = freshman;
        return this;
    }

    public void setFreshman(Long freshman) {
        this.freshman = freshman;
    }

    public String getEmail() {
        return this.email;
    }

    public Innkeeper email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Innkeeper phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Innkeeper gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getSlogan() {
        return this.slogan;
    }

    public Innkeeper slogan(String slogan) {
        this.slogan = slogan;
        return this;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getDescription() {
        return this.description;
    }

    public Innkeeper description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHomePage() {
        return this.homePage;
    }

    public Innkeeper homePage(String homePage) {
        this.homePage = homePage;
        return this;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public Innkeeper latitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public Innkeeper longitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return this.address;
    }

    public Innkeeper address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getServices() {
        return this.services;
    }

    public Innkeeper services(String services) {
        this.services = services;
        return this;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public Innkeeper createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getUpdateDate() {
        return this.updateDate;
    }

    public Innkeeper updateDate(Instant updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public void setUpdateDate(Instant updateDate) {
        this.updateDate = updateDate;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public Innkeeper internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public Set<SharedDinner> getSharedDinners() {
        return this.sharedDinners;
    }

    public Innkeeper sharedDinners(Set<SharedDinner> sharedDinners) {
        this.setSharedDinners(sharedDinners);
        return this;
    }

    public Innkeeper addSharedDinner(SharedDinner sharedDinner) {
        this.sharedDinners.add(sharedDinner);
        sharedDinner.setInnkeeper(this);
        return this;
    }

    public Innkeeper removeSharedDinner(SharedDinner sharedDinner) {
        this.sharedDinners.remove(sharedDinner);
        sharedDinner.setInnkeeper(null);
        return this;
    }

    public void setSharedDinners(Set<SharedDinner> sharedDinners) {
        if (this.sharedDinners != null) {
            this.sharedDinners.forEach(i -> i.setInnkeeper(null));
        }
        if (sharedDinners != null) {
            sharedDinners.forEach(i -> i.setInnkeeper(this));
        }
        this.sharedDinners = sharedDinners;
    }

    public Set<TakeAway> getTakeAways() {
        return this.takeAways;
    }

    public Innkeeper takeAways(Set<TakeAway> takeAways) {
        this.setTakeAways(takeAways);
        return this;
    }

    public Innkeeper addTakeAway(TakeAway takeAway) {
        this.takeAways.add(takeAway);
        takeAway.setInnkeeper(this);
        return this;
    }

    public Innkeeper removeTakeAway(TakeAway takeAway) {
        this.takeAways.remove(takeAway);
        takeAway.setInnkeeper(null);
        return this;
    }

    public void setTakeAways(Set<TakeAway> takeAways) {
        if (this.takeAways != null) {
            this.takeAways.forEach(i -> i.setInnkeeper(null));
        }
        if (takeAways != null) {
            takeAways.forEach(i -> i.setInnkeeper(this));
        }
        this.takeAways = takeAways;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Innkeeper)) {
            return false;
        }
        return id != null && id.equals(((Innkeeper) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Innkeeper{" +
            "id=" + getId() +
            ", nickname='" + getNickname() + "'" +
            ", freshman=" + getFreshman() +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", gender='" + getGender() + "'" +
            ", slogan='" + getSlogan() + "'" +
            ", description='" + getDescription() + "'" +
            ", homePage='" + getHomePage() + "'" +
            ", latitude='" + getLatitude() + "'" +
            ", longitude='" + getLongitude() + "'" +
            ", address='" + getAddress() + "'" +
            ", services='" + getServices() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", updateDate='" + getUpdateDate() + "'" +
            "}";
    }
}
