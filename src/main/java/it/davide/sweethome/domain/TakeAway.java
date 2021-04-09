package it.davide.sweethome.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A TakeAway.
 */
@Entity
@Table(name = "take_away")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "takeaway")
public class TakeAway implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "update_date")
    private Instant updateDate;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "dish", length = 50, nullable = false)
    private String dish;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "ingredients", nullable = false)
    private String ingredients;

    @NotNull
    @Column(name = "allergens", nullable = false)
    private String allergens;

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

    @Column(name = "tags")
    private String tags;

    @ManyToOne
    @JsonIgnoreProperties(value = { "internalUser", "sharedDinners", "takeAways" }, allowSetters = true)
    private Innkeeper innkeeper;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TakeAway id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public TakeAway createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getUpdateDate() {
        return this.updateDate;
    }

    public TakeAway updateDate(Instant updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public void setUpdateDate(Instant updateDate) {
        this.updateDate = updateDate;
    }

    public String getDish() {
        return this.dish;
    }

    public TakeAway dish(String dish) {
        this.dish = dish;
        return this;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }

    public String getDescription() {
        return this.description;
    }

    public TakeAway description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredients() {
        return this.ingredients;
    }

    public TakeAway ingredients(String ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getAllergens() {
        return this.allergens;
    }

    public TakeAway allergens(String allergens) {
        this.allergens = allergens;
        return this;
    }

    public void setAllergens(String allergens) {
        this.allergens = allergens;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public TakeAway latitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public TakeAway longitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return this.address;
    }

    public TakeAway address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getCostmin() {
        return this.costmin;
    }

    public TakeAway costmin(Double costmin) {
        this.costmin = costmin;
        return this;
    }

    public void setCostmin(Double costmin) {
        this.costmin = costmin;
    }

    public Double getCostmax() {
        return this.costmax;
    }

    public TakeAway costmax(Double costmax) {
        this.costmax = costmax;
        return this;
    }

    public void setCostmax(Double costmax) {
        this.costmax = costmax;
    }

    public String getTags() {
        return this.tags;
    }

    public TakeAway tags(String tags) {
        this.tags = tags;
        return this;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Innkeeper getInnkeeper() {
        return this.innkeeper;
    }

    public TakeAway innkeeper(Innkeeper innkeeper) {
        this.setInnkeeper(innkeeper);
        return this;
    }

    public void setInnkeeper(Innkeeper innkeeper) {
        this.innkeeper = innkeeper;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TakeAway)) {
            return false;
        }
        return id != null && id.equals(((TakeAway) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TakeAway{" +
            "id=" + getId() +
            ", createDate='" + getCreateDate() + "'" +
            ", updateDate='" + getUpdateDate() + "'" +
            ", dish='" + getDish() + "'" +
            ", description='" + getDescription() + "'" +
            ", ingredients='" + getIngredients() + "'" +
            ", allergens='" + getAllergens() + "'" +
            ", latitude='" + getLatitude() + "'" +
            ", longitude='" + getLongitude() + "'" +
            ", address='" + getAddress() + "'" +
            ", costmin=" + getCostmin() +
            ", costmax=" + getCostmax() +
            ", tags='" + getTags() + "'" +
            "}";
    }
}
