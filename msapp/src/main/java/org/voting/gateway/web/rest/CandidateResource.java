package org.voting.gateway.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.voting.gateway.domain.Candidate;

import org.voting.gateway.repository.CandidateRepository;
import org.voting.gateway.repository.PageWithTotalCount;
import org.voting.gateway.web.rest.errors.BadRequestAlertException;
import org.voting.gateway.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.datastax.driver.core.utils.UUIDs;
import org.voting.gateway.web.rest.util.PaginationUtil;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing Candidate.
 */
@RestController
@RequestMapping("/api")
public class CandidateResource {

    private final Logger log = LoggerFactory.getLogger(CandidateResource.class);

    private static final String ENTITY_NAME = "candidate";

    private final CandidateRepository candidateRepository;
    private final ElectoralPeriodsResource electoralPeriodsResource;

    public CandidateResource(CandidateRepository candidateRepository, ElectoralPeriodsResource electoralPeriodsResource) {
        this.candidateRepository = candidateRepository;

        this.electoralPeriodsResource = electoralPeriodsResource;
    }

    /**
     * POST  /candidates : Create a new candidate.
     *
     * @param candidate the candidate to create
     * @return the ResponseEntity with status 201 (Created) and with body the new candidate, or with status 400 (Bad Request) if the candidate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PreAuthorize("hasAnyAuthority('ROLE_GKW_MEMBER', 'ROLE_ADMIN')")
    @PostMapping("/candidates")
    @Timed
    public ResponseEntity<Candidate> createCandidate(@Valid @RequestBody Candidate candidate) throws URISyntaxException {
        electoralPeriodsResource.isInPeriod("PreElectionPeriod");
        log.debug("REST request to save Candidate : {}", candidate);
        if (candidate.getCandidate_id() != null) {
            throw new BadRequestAlertException("A new candidate cannot already have an ID", ENTITY_NAME, "idexists");
        }

        candidate.setCandidate_id(UUIDs.timeBased());
        Candidate result = candidateRepository.save(candidate);
        return ResponseEntity.created(new URI("/api/candidates/" + result.getCandidate_id()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getCandidate_id().toString()))
            .body(result);
    }

    /**
     * PUT  /candidates : Updates an existing candidate.
     *
     * @param candidate the candidate to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated candidate,
     * or with status 400 (Bad Request) if the candidate is not valid,
     * or with status 500 (Internal Server Error) if the candidate couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PreAuthorize("hasAnyAuthority('ROLE_GKW_MEMBER', 'ROLE_ADMIN')")
    @PutMapping("/candidates")
    @Timed
    public ResponseEntity<Candidate> updateCandidate(@Valid @RequestBody Candidate candidate) throws URISyntaxException {
        electoralPeriodsResource.isInPeriod("PreElectionPeriod");
        log.debug("REST request to update Candidate : {}", candidate);
        if (candidate.getCandidate_id() == null) {
            return createCandidate(candidate);
        }
        Candidate result = candidateRepository.save(candidate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, candidate.getCandidate_id().toString()))
            .body(result);
    }

    /**
     * GET  /candidates : get all the candidates.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of candidates in body
     */
    @GetMapping("/candidates")
    @Timed
    public ResponseEntity<List<Candidate>> getAllCandidates(Pageable pageRequest) {
        log.debug("REST request to get all Candidates");
        PageWithTotalCount<Candidate> page = candidateRepository.findAllPaged(pageRequest);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page.getPage(), "/api/candidates");
        return new ResponseEntity<>(page.getPage().getContent(), headers, HttpStatus.OK);
    }
    //PageImpl<Candidate>

/*
    public ResponseEntity<List<Candidate>> getAllCandidates(Pageable pageable) {
        log.debug("REST request to get all Candidates");
        Page<Candidate> page = candidateRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/candidates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
*/


    /**
     * GET  /candidates/:id : get the "id" candidate.
     *
     * @param id the id of the candidate to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the candidate, or with status 404 (Not Found)
     */
    @GetMapping("/candidates/{id}")
    @Timed
    public ResponseEntity<Candidate> getCandidate(@PathVariable UUID id) {
        log.debug("REST request to get Candidate : {}", id);
        Candidate candidate = candidateRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(candidate));
    }

    /**
     * DELETE  /candidates/:id : delete the "id" candidate.
     *
     * @param id the id of the candidate to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @PreAuthorize("hasAnyAuthority('ROLE_GKW_MEMBER', 'ROLE_ADMIN')")
    @DeleteMapping("/candidates/{id}")
    @Timed
    public ResponseEntity<Void> deleteCandidate(@PathVariable UUID id) {
        electoralPeriodsResource.isInPeriod("PreElectionPeriod");
        log.debug("REST request to delete Candidate : {}", id);
        candidateRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET
     * @param municipalityId the id of the municipalityId
     * @return the list of candidates
     */
    @GetMapping("/municipalities/{municipalityId}/candidates")
    @Timed
    public List<Candidate> getElectoralDistrictsByMunicipalityId(@PathVariable UUID municipalityId){
        return candidateRepository.findInMunicipality(municipalityId);
    }

    /**
     *
     * @param municipalityId id of municipality
     * @param turn a given turn
     * @return a list of candidates from muncipality
     */
    @GetMapping("/municipalities/{municipalityId}/{turn}/candidates")
    @Timed
    public List<Candidate> getElectoralDistrictsByMunicipalityId(@PathVariable UUID municipalityId, @PathVariable
                                                                 Integer turn){
        return candidateRepository.findInMunicipalityByTurn(municipalityId,turn);
    }
}
