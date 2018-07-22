package tr.net.susantez.im.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import tr.net.susantez.im.domain.enumeration.SalesType;

import tr.net.susantez.im.domain.enumeration.Status;

/**
 * A Sales.
 */
@Entity
@Table(name = "sales")
public class Sales implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private SalesType type;

    @NotNull
    @Column(name = "unit_price", nullable = false)
    private Float unitPrice;

    @NotNull
    @Column(name = "item_count", nullable = false)
    private Float itemCount;

    @NotNull
    @Column(name = "total_price", nullable = false)
    private Float totalPrice;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "update_date")
    private LocalDate updateDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Product product;

    @ManyToMany
    @JoinTable(name = "sales_event",
               joinColumns = @JoinColumn(name = "sales_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "events_id", referencedColumnName = "id"))
    private Set<Event> events = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SalesType getType() {
        return type;
    }

    public Sales type(SalesType type) {
        this.type = type;
        return this;
    }

    public void setType(SalesType type) {
        this.type = type;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public Sales unitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Float getItemCount() {
        return itemCount;
    }

    public Sales itemCount(Float itemCount) {
        this.itemCount = itemCount;
        return this;
    }

    public void setItemCount(Float itemCount) {
        this.itemCount = itemCount;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public Sales totalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Sales creationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public Sales updateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }

    public Status getStatus() {
        return status;
    }

    public Sales status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Product getProduct() {
        return product;
    }

    public Sales product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public Sales events(Set<Event> events) {
        this.events = events;
        return this;
    }

    public Sales addEvent(Event event) {
        this.events.add(event);
        return this;
    }

    public Sales removeEvent(Event event) {
        this.events.remove(event);
        return this;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sales sales = (Sales) o;
        if (sales.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sales.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Sales{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", unitPrice=" + getUnitPrice() +
            ", itemCount=" + getItemCount() +
            ", totalPrice=" + getTotalPrice() +
            ", creationDate='" + getCreationDate() + "'" +
            ", updateDate='" + getUpdateDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
