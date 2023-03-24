package ru.funnylistening.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Element.
 */
@Entity
@Table(name = "element")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Element implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "element_text", nullable = false)
    private String elementText;

    @Column(name = "element_audio_path")
    private String elementAudioPath;

    @JsonIgnoreProperties(value = { "entireStoryElement", "elements" }, allowSetters = true)
    @OneToOne(mappedBy = "entireStoryElement")
    private Story entireStory;

    @ManyToOne
    @JsonIgnoreProperties(value = { "entireStoryElement", "elements" }, allowSetters = true)
    private Story story;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Element id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getElementText() {
        return this.elementText;
    }

    public Element elementText(String elementText) {
        this.setElementText(elementText);
        return this;
    }

    public void setElementText(String elementText) {
        this.elementText = elementText;
    }

    public String getElementAudioPath() {
        return this.elementAudioPath;
    }

    public Element elementAudioPath(String elementAudioPath) {
        this.setElementAudioPath(elementAudioPath);
        return this;
    }

    public void setElementAudioPath(String elementAudioPath) {
        this.elementAudioPath = elementAudioPath;
    }

    public Story getEntireStory() {
        return this.entireStory;
    }

    public void setEntireStory(Story story) {
        if (this.entireStory != null) {
            this.entireStory.setEntireStoryElement(null);
        }
        if (story != null) {
            story.setEntireStoryElement(this);
        }
        this.entireStory = story;
    }

    public Element entireStory(Story story) {
        this.setEntireStory(story);
        return this;
    }

    public Story getStory() {
        return this.story;
    }

    public void setStory(Story story) {
        this.story = story;
    }

    public Element story(Story story) {
        this.setStory(story);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Element)) {
            return false;
        }
        return id != null && id.equals(((Element) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Element{" +
            "id=" + getId() +
            ", elementText='" + getElementText() + "'" +
            ", elementAudioPath='" + getElementAudioPath() + "'" +
            "}";
    }
}
