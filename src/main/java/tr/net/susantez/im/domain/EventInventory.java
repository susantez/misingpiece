package tr.net.susantez.im.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A EventInventory.
 */
@Entity
@Table(name = "event_inventory")
public class EventInventory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "item_count", nullable = false)
    private Float itemCount;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Product product;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Event event;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getItemCount() {
        return itemCount;
    }

    public EventInventory itemCount(Float itemCount) {
        this.itemCount = itemCount;
        return this;
    }

    public void setItemCount(Float itemCount) {
        this.itemCount = itemCount;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public EventInventory creationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Product getProduct() {
        return product;
    }

    public EventInventory product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Event getEvent() {
        return event;
    }

    public EventInventory event(Event event) {
        this.event = event;
        return this;
    }

    public void setEvent(Event event) {
        this.event = event;
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
        EventInventory eventInventory = (EventInventory) o;
        if (eventInventory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), eventInventory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EventInventory{" +
            "id=" + getId() +
            ", itemCount=" + getItemCount() +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
