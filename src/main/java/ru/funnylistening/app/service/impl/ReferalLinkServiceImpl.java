package ru.funnylistening.app.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.funnylistening.app.domain.ReferalLink;
import ru.funnylistening.app.repository.ReferalLinkRepository;
import ru.funnylistening.app.service.ReferalLinkService;

/**
 * Service Implementation for managing {@link ReferalLink}.
 */
@Service
@Transactional
public class ReferalLinkServiceImpl implements ReferalLinkService {

    private final Logger log = LoggerFactory.getLogger(ReferalLinkServiceImpl.class);

    private final ReferalLinkRepository referalLinkRepository;

    public ReferalLinkServiceImpl(ReferalLinkRepository referalLinkRepository) {
        this.referalLinkRepository = referalLinkRepository;
    }

    @Override
    public ReferalLink save(ReferalLink referalLink) {
        log.debug("Request to save ReferalLink : {}", referalLink);
        return referalLinkRepository.save(referalLink);
    }

    @Override
    public ReferalLink update(ReferalLink referalLink) {
        log.debug("Request to update ReferalLink : {}", referalLink);
        return referalLinkRepository.save(referalLink);
    }

    @Override
    public Optional<ReferalLink> partialUpdate(ReferalLink referalLink) {
        log.debug("Request to partially update ReferalLink : {}", referalLink);

        return referalLinkRepository
            .findById(referalLink.getId())
            .map(existingReferalLink -> {
                if (referalLink.getLinkText() != null) {
                    existingReferalLink.setLinkText(referalLink.getLinkText());
                }

                return existingReferalLink;
            })
            .map(referalLinkRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReferalLink> findAll() {
        log.debug("Request to get all ReferalLinks");
        return referalLinkRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReferalLink> findOne(Long id) {
        log.debug("Request to get ReferalLink : {}", id);
        return referalLinkRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReferalLink : {}", id);
        referalLinkRepository.deleteById(id);
    }
}
