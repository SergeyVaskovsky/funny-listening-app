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

    @NotNull
    @Column(name = "element_audio_path", nullable = false)
    private String elementAudioPath;

    @ManyToOne
    @JsonIgnoreProperties(value = { "entireStories", "elements" }, allowSetters = true)
    private Story entireStoryElement;

    @ManyToOne
    @JsonIgnoreProperties(value = { "entireStories", "elements" }, allowSetters = true)
    private Story element;

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

    public Story getEntireStoryElement() {
        return this.entireStoryElement;
    }

    public void setEntireStoryElement(Story story) {
        this.entireStoryElement = story;
    }

    public Element entireStoryElement(Story story) {
        this.setEntireStoryElement(story);
        return this;
    }

    public Story getElement() {
        return this.element;
    }

    public void setElement(Story story) {
        this.element = story;
    }

    public Element element(Story story) {
        this.setElement(story);
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
