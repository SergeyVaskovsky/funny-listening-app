package ru.funnylistening.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Story.
 */
@Entity
@Table(name = "story")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Story implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "story_name", nullable = false)
    private String storyName;

    @OneToMany(mappedBy = "entireStory")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "entireStory", "stories" }, allowSetters = true)
    private Set<Element> entireStoryElements = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_story__elements",
        joinColumns = @JoinColumn(name = "story_id"),
        inverseJoinColumns = @JoinColumn(name = "elements_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "entireStory", "stories" }, allowSetters = true)
    private Set<Element> elements = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Story id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStoryName() {
        return this.storyName;
    }

    public Story storyName(String storyName) {
        this.setStoryName(storyName);
        return this;
    }

    public void setStoryName(String storyName) {
        this.storyName = storyName;
    }

    public Set<Element> getEntireStoryElements() {
        return this.entireStoryElements;
    }

    public void setEntireStoryElements(Set<Element> elements) {
        if (this.entireStoryElements != null) {
            this.entireStoryElements.forEach(i -> i.setEntireStory(null));
        }
        if (elements != null) {
            elements.forEach(i -> i.setEntireStory(this));
        }
        this.entireStoryElements = elements;
    }

    public Story entireStoryElements(Set<Element> elements) {
        this.setEntireStoryElements(elements);
        return this;
    }

    public Story addEntireStoryElement(Element element) {
        this.entireStoryElements.add(element);
        element.setEntireStory(this);
        return this;
    }

    public Story removeEntireStoryElement(Element element) {
        this.entireStoryElements.remove(element);
        element.setEntireStory(null);
        return this;
    }

    public Set<Element> getElements() {
        return this.elements;
    }

    public void setElements(Set<Element> elements) {
        this.elements = elements;
    }

    public Story elements(Set<Element> elements) {
        this.setElements(elements);
        return this;
    }

    public Story addElements(Element element) {
        this.elements.add(element);
        element.getStories().add(this);
        return this;
    }

    public Story removeElements(Element element) {
        this.elements.remove(element);
        element.getStories().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Story)) {
            return false;
        }
        return id != null && id.equals(((Story) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Story{" +
            "id=" + getId() +
            ", storyName='" + getStoryName() + "'" +
            "}";
    }
}
