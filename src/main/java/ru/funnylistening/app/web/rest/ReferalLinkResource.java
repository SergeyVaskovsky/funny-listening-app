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
import ru.funnylistening.app.domain.ReferalLink;
import ru.funnylistening.app.repository.ReferalLinkRepository;
import ru.funnylistening.app.service.ReferalLinkService;
import ru.funnylistening.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.funnylistening.app.domain.ReferalLink}.
 */
@RestController
@RequestMapping("/api")
public class ReferalLinkResource {

    private final Logger log = LoggerFactory.getLogger(ReferalLinkResource.class);

    private static final String ENTITY_NAME = "referalLink";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReferalLinkService referalLinkService;

    private final ReferalLinkRepository referalLinkRepository;

    public ReferalLinkResource(ReferalLinkService referalLinkService, ReferalLinkRepository referalLinkRepository) {
        this.referalLinkService = referalLinkService;
        this.referalLinkRepository = referalLinkRepository;
    }

    /**
     * {@code POST  /referal-links} : Create a new referalLink.
     *
     * @param referalLink the referalLink to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new referalLink, or with status {@code 400 (Bad Request)} if the referalLink has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/referal-links")
    public ResponseEntity<ReferalLink> createReferalLink(@Valid @RequestBody ReferalLink referalLink) throws URISyntaxException {
        log.debug("REST request to save ReferalLink : {}", referalLink);
        if (referalLink.getId() != null) {
            throw new BadRequestAlertException("A new referalLink cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReferalLink result = referalLinkService.save(referalLink);
        return ResponseEntity
            .created(new URI("/api/referal-links/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /referal-links/:id} : Updates an existing referalLink.
     *
     * @param id the id of the referalLink to save.
     * @param referalLink the referalLink to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated referalLink,
     * or with status {@code 400 (Bad Request)} if the referalLink is not valid,
     * or with status {@code 500 (Internal Server Error)} if the referalLink couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/referal-links/{id}")
    public ResponseEntity<ReferalLink> updateReferalLink(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReferalLink referalLink
    ) throws URISyntaxException {
        log.debug("REST request to update ReferalLink : {}, {}", id, referalLink);
        if (referalLink.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, referalLink.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!referalLinkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReferalLink result = referalLinkService.update(referalLink);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, referalLink.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /referal-links/:id} : Partial updates given fields of an existing referalLink, field will ignore if it is null
     *
     * @param id the id of the referalLink to save.
     * @param referalLink the referalLink to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated referalLink,
     * or with status {@code 400 (Bad Request)} if the referalLink is not valid,
     * or with status {@code 404 (Not Found)} if the referalLink is not found,
     * or with status {@code 500 (Internal Server Error)} if the referalLink couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/referal-links/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReferalLink> partialUpdateReferalLink(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReferalLink referalLink
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReferalLink partially : {}, {}", id, referalLink);
        if (referalLink.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, referalLink.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!referalLinkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReferalLink> result = referalLinkService.partialUpdate(referalLink);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, referalLink.getId().toString())
        );
    }

    /**
     * {@code GET  /referal-links} : get all the referalLinks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of referalLinks in body.
     */
    @GetMapping("/referal-links")
    public List<ReferalLink> getAllReferalLinks() {
        log.debug("REST request to get all ReferalLinks");
        return referalLinkService.findAll();
    }

    /**
     * {@code GET  /referal-links/:id} : get the "id" referalLink.
     *
     * @param id the id of the referalLink to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the referalLink, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/referal-links/{id}")
    public ResponseEntity<ReferalLink> getReferalLink(@PathVariable Long id) {
        log.debug("REST request to get ReferalLink : {}", id);
        Optional<ReferalLink> referalLink = referalLinkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(referalLink);
    }

    /**
     * {@code DELETE  /referal-links/:id} : delete the "id" referalLink.
     *
     * @param id the id of the referalLink to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/referal-links/{id}")
    public ResponseEntity<Void> deleteReferalLink(@PathVariable Long id) {
        log.debug("REST request to delete ReferalLink : {}", id);
        referalLinkService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
