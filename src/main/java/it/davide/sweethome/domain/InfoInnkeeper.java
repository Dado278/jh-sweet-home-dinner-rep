package it.davide.sweethome.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A InfoInnkeeper.
 */
@Entity
@Table(name = "info_innkeeper")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "infoinnkeeper")
public class InfoInnkeeper implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "slogan")
    private String slogan;

    @Column(name = "description")
    private String description;

    @Column(name = "home_page")
    private String homePage;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "address")
    private String address;

    @Column(name = "services")
    private String services;

    @OneToMany(mappedBy = "infoInnkeeper")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<SharedDinner> sharedDinners = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public InfoInnkeeper nickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSlogan() {
        return slogan;
    }

    public InfoInnkeeper slogan(String slogan) {
        this.slogan = slogan;
        return this;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getDescription() {
        return description;
    }

    public InfoInnkeeper description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHomePage() {
        return homePage;
    }

    public InfoInnkeeper homePage(String homePage) {
        this.homePage = homePage;
        return this;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public String getLatitude() {
        return latitude;
    }

    public InfoInnkeeper latitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public InfoInnkeeper longitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public InfoInnkeeper address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getServices() {
        return services;
    }

    public InfoInnkeeper services(String services) {
        this.services = services;
        return this;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public Set<SharedDinner> getSharedDinners() {
        return sharedDinners;
    }

    public InfoInnkeeper sharedDinners(Set<SharedDinner> sharedDinners) {
        this.sharedDinners = sharedDinners;
        return this;
    }

    public InfoInnkeeper addSharedDinner(SharedDinner sharedDinner) {
        this.sharedDinners.add(sharedDinner);
        sharedDinner.setInfoInnkeeper(this);
        return this;
    }

    public InfoInnkeeper removeSharedDinner(SharedDinner sharedDinner) {
        this.sharedDinners.remove(sharedDinner);
        sharedDinner.setInfoInnkeeper(null);
        return this;
    }

    public void setSharedDinners(Set<SharedDinner> sharedDinners) {
        this.sharedDinners = sharedDinners;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InfoInnkeeper)) {
            return false;
        }
        return id != null && id.equals(((InfoInnkeeper) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InfoInnkeeper{" +
            "id=" + getId() +
            ", nickname='" + getNickname() + "'" +
            ", slogan='" + getSlogan() + "'" +
            ", description='" + getDescription() + "'" +
            ", homePage='" + getHomePage() + "'" +
            ", latitude='" + getLatitude() + "'" +
            ", longitude='" + getLongitude() + "'" +
            ", address='" + getAddress() + "'" +
            ", services='" + getServices() + "'" +
            "}";
    }
}
