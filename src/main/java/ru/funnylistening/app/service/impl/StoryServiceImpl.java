package ru.funnylistening.app.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.funnylistening.app.domain.Story;
import ru.funnylistening.app.repository.StoryRepository;
import ru.funnylistening.app.service.StoryService;

/**
 * Service Implementation for managing {@link Story}.
 */
@Service
@Transactional
public class StoryServiceImpl implements StoryService {

    private final Logger log = LoggerFactory.getLogger(StoryServiceImpl.class);

    private final StoryRepository storyRepository;

    public StoryServiceImpl(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    @Override
    public Story save(Story story) {
        log.debug("Request to save Story : {}", story);
        return storyRepository.save(story);
    }

    @Override
    public Story update(Story story) {
        log.debug("Request to update Story : {}", story);
        return storyRepository.save(story);
    }

    @Override
    public Optional<Story> partialUpdate(Story story) {
        log.debug("Request to partially update Story : {}", story);

        return storyRepository
            .findById(story.getId())
            .map(existingStory -> {
                if (story.getStoryName() != null) {
                    existingStory.setStoryName(story.getStoryName());
                }

                return existingStory;
            })
            .map(storyRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Story> findAll() {
        log.debug("Request to get all Stories");
        return storyRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Story> findOne(Long id) {
        log.debug("Request to get Story : {}", id);
        return storyRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Story : {}", id);
        storyRepository.deleteById(id);
    }
}
