package tr.net.susantez.im.web.rest;

import com.codahale.metrics.annotation.Timed;
import tr.net.susantez.im.domain.EventInventory;
import tr.net.susantez.im.repository.EventInventoryRepository;
import tr.net.susantez.im.web.rest.errors.BadRequestAlertException;
import tr.net.susantez.im.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing EventInventory.
 */
@RestController
@RequestMapping("/api")
public class EventInventoryResource {

    private final Logger log = LoggerFactory.getLogger(EventInventoryResource.class);

    private static final String ENTITY_NAME = "eventInventory";

    private final EventInventoryRepository eventInventoryRepository;

    public EventInventoryResource(EventInventoryRepository eventInventoryRepository) {
        this.eventInventoryRepository = eventInventoryRepository;
    }

    /**
     * POST  /event-inventories : Create a new eventInventory.
     *
     * @param eventInventory the eventInventory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new eventInventory, or with status 400 (Bad Request) if the eventInventory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/event-inventories")
    @Timed
    public ResponseEntity<EventInventory> createEventInventory(@Valid @RequestBody EventInventory eventInventory) throws URISyntaxException {
        log.debug("REST request to save EventInventory : {}", eventInventory);
        if (eventInventory.getId() != null) {
            throw new BadRequestAlertException("A new eventInventory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventInventory result = eventInventoryRepository.save(eventInventory);
        return ResponseEntity.created(new URI("/api/event-inventories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /event-inventories : Updates an existing eventInventory.
     *
     * @param eventInventory the eventInventory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated eventInventory,
     * or with status 400 (Bad Request) if the eventInventory is not valid,
     * or with status 500 (Internal Server Error) if the eventInventory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/event-inventories")
    @Timed
    public ResponseEntity<EventInventory> updateEventInventory(@Valid @RequestBody EventInventory eventInventory) throws URISyntaxException {
        log.debug("REST request to update EventInventory : {}", eventInventory);
        if (eventInventory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EventInventory result = eventInventoryRepository.save(eventInventory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, eventInventory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /event-inventories : get all the eventInventories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of eventInventories in body
     */
    @GetMapping("/event-inventories")
    @Timed
    public List<EventInventory> getAllEventInventories() {
        log.debug("REST request to get all EventInventories");
        return eventInventoryRepository.findAll();
    }

    /**
     * GET  /event-inventories/:id : get the "id" eventInventory.
     *
     * @param id the id of the eventInventory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the eventInventory, or with status 404 (Not Found)
     */
    @GetMapping("/event-inventories/{id}")
    @Timed
    public ResponseEntity<EventInventory> getEventInventory(@PathVariable Long id) {
        log.debug("REST request to get EventInventory : {}", id);
        Optional<EventInventory> eventInventory = eventInventoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(eventInventory);
    }

    /**
     * DELETE  /event-inventories/:id : delete the "id" eventInventory.
     *
     * @param id the id of the eventInventory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/event-inventories/{id}")
    @Timed
    public ResponseEntity<Void> deleteEventInventory(@PathVariable Long id) {
        log.debug("REST request to delete EventInventory : {}", id);

        eventInventoryRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
