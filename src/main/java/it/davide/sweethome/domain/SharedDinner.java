package it.davide.sweethome.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A SharedDinner.
 */
@Entity
@Table(name = "shared_dinner")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "shareddinner")
public class SharedDinner implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "update_date")
    private Instant updateDate;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "slogan")
    private String slogan;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "dinner_date")
    private LocalDate dinnerDate;

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

    @NotNull
    @Column(name = "costmin", nullable = false)
    private Double costmin;

    @NotNull
    @Column(name = "costmax", nullable = false)
    private Double costmax;

    @ManyToOne
    @JsonIgnoreProperties(value = { "internalUser", "sharedDinners", "takeAways" }, allowSetters = true)
    private Innkeeper innkeeper;

    @ManyToMany(mappedBy = "sharedDinners")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "internalUser", "sharedDinners" }, allowSetters = true)
    private Set<Customer> customers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SharedDinner id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public SharedDinner createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getUpdateDate() {
        return this.updateDate;
    }

    public SharedDinner updateDate(Instant updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public void setUpdateDate(Instant updateDate) {
        this.updateDate = updateDate;
    }

    public String getTitle() {
        return this.title;
    }

    public SharedDinner title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlogan() {
        return this.slogan;
    }

    public SharedDinner slogan(String slogan) {
        this.slogan = slogan;
        return this;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getDescription() {
        return this.description;
    }

    public SharedDinner description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDinnerDate() {
        return this.dinnerDate;
    }

    public SharedDinner dinnerDate(LocalDate dinnerDate) {
        this.dinnerDate = dinnerDate;
        return this;
    }

    public void setDinnerDate(LocalDate dinnerDate) {
        this.dinnerDate = dinnerDate;
    }

    public String getHomePage() {
        return this.homePage;
    }

    public SharedDinner homePage(String homePage) {
        this.homePage = homePage;
        return this;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public SharedDinner latitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public SharedDinner longitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return this.address;
    }

    public SharedDinner address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getCostmin() {
        return this.costmin;
    }

    public SharedDinner costmin(Double costmin) {
        this.costmin = costmin;
        return this;
    }

    public void setCostmin(Double costmin) {
        this.costmin = costmin;
    }

    public Double getCostmax() {
        return this.costmax;
    }

    public SharedDinner costmax(Double costmax) {
        this.costmax = costmax;
        return this;
    }

    public void setCostmax(Double costmax) {
        this.costmax = costmax;
    }

    public Innkeeper getInnkeeper() {
        return this.innkeeper;
    }

    public SharedDinner innkeeper(Innkeeper innkeeper) {
        this.setInnkeeper(innkeeper);
        return this;
    }

    public void setInnkeeper(Innkeeper innkeeper) {
        this.innkeeper = innkeeper;
    }

    public Set<Customer> getCustomers() {
        return this.customers;
    }

    public SharedDinner customers(Set<Customer> customers) {
        this.setCustomers(customers);
        return this;
    }

    public SharedDinner addCustomer(Customer customer) {
        this.customers.add(customer);
        customer.getSharedDinners().add(this);
        return this;
    }

    public SharedDinner removeCustomer(Customer customer) {
        this.customers.remove(customer);
        customer.getSharedDinners().remove(this);
        return this;
    }

    public void setCustomers(Set<Customer> customers) {
        if (this.customers != null) {
            this.customers.forEach(i -> i.removeSharedDinner(this));
        }
        if (customers != null) {
            customers.forEach(i -> i.addSharedDinner(this));
        }
        this.customers = customers;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SharedDinner)) {
            return false;
        }
        return id != null && id.equals(((SharedDinner) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SharedDinner{" +
            "id=" + getId() +
            ", createDate='" + getCreateDate() + "'" +
            ", updateDate='" + getUpdateDate() + "'" +
            ", title='" + getTitle() + "'" +
            ", slogan='" + getSlogan() + "'" +
            ", description='" + getDescription() + "'" +
            ", dinnerDate='" + getDinnerDate() + "'" +
            ", homePage='" + getHomePage() + "'" +
            ", latitude='" + getLatitude() + "'" +
            ", longitude='" + getLongitude() + "'" +
            ", address='" + getAddress() + "'" +
            ", costmin=" + getCostmin() +
            ", costmax=" + getCostmax() +
            "}";
    }
}
