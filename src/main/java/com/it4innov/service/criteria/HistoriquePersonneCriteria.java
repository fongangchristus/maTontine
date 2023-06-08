package com.it4innov.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.HistoriquePersonne} entity. This class is used
 * in {@link com.it4innov.web.rest.HistoriquePersonneResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /historique-personnes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriquePersonneCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter dateAction;

    private StringFilter matriculePersonne;

    private StringFilter action;

    private StringFilter result;

    private StringFilter description;

    private LongFilter personneId;

    private Boolean distinct;

    public HistoriquePersonneCriteria() {}

    public HistoriquePersonneCriteria(HistoriquePersonneCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dateAction = other.dateAction == null ? null : other.dateAction.copy();
        this.matriculePersonne = other.matriculePersonne == null ? null : other.matriculePersonne.copy();
        this.action = other.action == null ? null : other.action.copy();
        this.result = other.result == null ? null : other.result.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.personneId = other.personneId == null ? null : other.personneId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public HistoriquePersonneCriteria copy() {
        return new HistoriquePersonneCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getDateAction() {
        return dateAction;
    }

    public InstantFilter dateAction() {
        if (dateAction == null) {
            dateAction = new InstantFilter();
        }
        return dateAction;
    }

    public void setDateAction(InstantFilter dateAction) {
        this.dateAction = dateAction;
    }

    public StringFilter getMatriculePersonne() {
        return matriculePersonne;
    }

    public StringFilter matriculePersonne() {
        if (matriculePersonne == null) {
            matriculePersonne = new StringFilter();
        }
        return matriculePersonne;
    }

    public void setMatriculePersonne(StringFilter matriculePersonne) {
        this.matriculePersonne = matriculePersonne;
    }

    public StringFilter getAction() {
        return action;
    }

    public StringFilter action() {
        if (action == null) {
            action = new StringFilter();
        }
        return action;
    }

    public void setAction(StringFilter action) {
        this.action = action;
    }

    public StringFilter getResult() {
        return result;
    }

    public StringFilter result() {
        if (result == null) {
            result = new StringFilter();
        }
        return result;
    }

    public void setResult(StringFilter result) {
        this.result = result;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getPersonneId() {
        return personneId;
    }

    public LongFilter personneId() {
        if (personneId == null) {
            personneId = new LongFilter();
        }
        return personneId;
    }

    public void setPersonneId(LongFilter personneId) {
        this.personneId = personneId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HistoriquePersonneCriteria that = (HistoriquePersonneCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dateAction, that.dateAction) &&
            Objects.equals(matriculePersonne, that.matriculePersonne) &&
            Objects.equals(action, that.action) &&
            Objects.equals(result, that.result) &&
            Objects.equals(description, that.description) &&
            Objects.equals(personneId, that.personneId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateAction, matriculePersonne, action, result, description, personneId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriquePersonneCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dateAction != null ? "dateAction=" + dateAction + ", " : "") +
            (matriculePersonne != null ? "matriculePersonne=" + matriculePersonne + ", " : "") +
            (action != null ? "action=" + action + ", " : "") +
            (result != null ? "result=" + result + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (personneId != null ? "personneId=" + personneId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
