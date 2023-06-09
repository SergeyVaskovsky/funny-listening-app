package ru.funnylistening.app.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.funnylistening.app.domain.Element;
import ru.funnylistening.app.repository.ElementRepository;
import ru.funnylistening.app.service.ElementService;
import ru.funnylistening.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.funnylistening.app.domain.Element}.
 */
@RestController
@RequestMapping("/api")
public class ElementResource {

    private final Logger log = LoggerFactory.getLogger(ElementResource.class);

    private static final String ENTITY_NAME = "element";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ElementService elementService;

    private final ElementRepository elementRepository;

    public ElementResource(ElementService elementService, ElementRepository elementRepository) {
        this.elementService = elementService;
        this.elementRepository = elementRepository;
    }

    /**
     * {@code POST  /elements} : Create a new element.
     *
     * @param element the element to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new element, or with status {@code 400 (Bad Request)} if the element has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/elements")
    public ResponseEntity<Element> createElement(@Valid @RequestBody Element element) throws URISyntaxException {
        log.debug("REST request to save Element : {}", element);
        if (element.getId() != null) {
            throw new BadRequestAlertException("A new element cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Element result = elementService.save(element);
        return ResponseEntity
            .created(new URI("/api/elements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /elements/:id} : Updates an existing element.
     *
     * @param id the id of the element to save.
     * @param element the element to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated element,
     * or with status {@code 400 (Bad Request)} if the element is not valid,
     * or with status {@code 500 (Internal Server Error)} if the element couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/elements/{id}")
    public ResponseEntity<Element> updateElement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Element element
    ) throws URISyntaxException {
        log.debug("REST request to update Element : {}, {}", id, element);
        if (element.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, element.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!elementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Element result = elementService.update(element);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, element.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /elements/:id} : Partial updates given fields of an existing element, field will ignore if it is null
     *
     * @param id the id of the element to save.
     * @param element the element to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated element,
     * or with status {@code 400 (Bad Request)} if the element is not valid,
     * or with status {@code 404 (Not Found)} if the element is not found,
     * or with status {@code 500 (Internal Server Error)} if the element couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/elements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Element> partialUpdateElement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Element element
    ) throws URISyntaxException {
        log.debug("REST request to partial update Element partially : {}, {}", id, element);
        if (element.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, element.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!elementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Element> result = elementService.partialUpdate(element);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, element.getId().toString())
        );
    }

    /**
     * {@code GET  /elements} : get all the elements.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of elements in body.
     */
    @GetMapping("/elements")
    public List<Element> getAllElements() {
        log.debug("REST request to get all Elements");
        return elementService.findAll();
    }

    /**
     * {@code GET  /elements/:id} : get the "id" element.
     *
     * @param id the id of the element to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the element, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/elements/{id}")
    public ResponseEntity<Element> getElement(@PathVariable Long id) {
        log.debug("REST request to get Element : {}", id);
        Optional<Element> element = elementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(element);
    }

    /**
     * {@code DELETE  /elements/:id} : delete the "id" element.
     *
     * @param id the id of the element to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/elements/{id}")
    public ResponseEntity<Void> deleteElement(@PathVariable Long id) {
        log.debug("REST request to delete Element : {}", id);
        elementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
