package ru.funnylistening.app.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.funnylistening.app.domain.Link;
import ru.funnylistening.app.repository.LinkRepository;
import ru.funnylistening.app.service.LinkService;

/**
 * Service Implementation for managing {@link Link}.
 */
@Service
@Transactional
public class LinkServiceImpl implements LinkService {

    private final Logger log = LoggerFactory.getLogger(LinkServiceImpl.class);

    private final LinkRepository linkRepository;

    public LinkServiceImpl(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Override
    public Link save(Link link) {
        log.debug("Request to save Link : {}", link);
        return linkRepository.save(link);
    }

    @Override
    public Link update(Link link) {
        log.debug("Request to update Link : {}", link);
        return linkRepository.save(link);
    }

    @Override
    public Optional<Link> partialUpdate(Link link) {
        log.debug("Request to partially update Link : {}", link);

        return linkRepository
            .findById(link.getId())
            .map(existingLink -> {
                if (link.getLinkText() != null) {
                    existingLink.setLinkText(link.getLinkText());
                }

                return existingLink;
            })
            .map(linkRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Link> findAll() {
        log.debug("Request to get all Links");
        return linkRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Link> findOne(Long id) {
        log.debug("Request to get Link : {}", id);
        return linkRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Link : {}", id);
        linkRepository.deleteById(id);
    }
}
