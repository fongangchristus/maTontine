package com.it4innov.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.Adresse} entity. This class is used
 * in {@link com.it4innov.web.rest.AdresseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /adresses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdresseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter departmentName;

    private StringFilter streetAddress;

    private StringFilter postalCode;

    private StringFilter city;

    private StringFilter stateProvince;

    private StringFilter pays;

    private Boolean distinct;

    public AdresseCriteria() {}

    public AdresseCriteria(AdresseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.departmentName = other.departmentName == null ? null : other.departmentName.copy();
        this.streetAddress = other.streetAddress == null ? null : other.streetAddress.copy();
        this.postalCode = other.postalCode == null ? null : other.postalCode.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.stateProvince = other.stateProvince == null ? null : other.stateProvince.copy();
        this.pays = other.pays == null ? null : other.pays.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AdresseCriteria copy() {
        return new AdresseCriteria(this);
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

    public StringFilter getDepartmentName() {
        return departmentName;
    }

    public StringFilter departmentName() {
        if (departmentName == null) {
            departmentName = new StringFilter();
        }
        return departmentName;
    }

    public void setDepartmentName(StringFilter departmentName) {
        this.departmentName = departmentName;
    }

    public StringFilter getStreetAddress() {
        return streetAddress;
    }

    public StringFilter streetAddress() {
        if (streetAddress == null) {
            streetAddress = new StringFilter();
        }
        return streetAddress;
    }

    public void setStreetAddress(StringFilter streetAddress) {
        this.streetAddress = streetAddress;
    }

    public StringFilter getPostalCode() {
        return postalCode;
    }

    public StringFilter postalCode() {
        if (postalCode == null) {
            postalCode = new StringFilter();
        }
        return postalCode;
    }

    public void setPostalCode(StringFilter postalCode) {
        this.postalCode = postalCode;
    }

    public StringFilter getCity() {
        return city;
    }

    public StringFilter city() {
        if (city == null) {
            city = new StringFilter();
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getStateProvince() {
        return stateProvince;
    }

    public StringFilter stateProvince() {
        if (stateProvince == null) {
            stateProvince = new StringFilter();
        }
        return stateProvince;
    }

    public void setStateProvince(StringFilter stateProvince) {
        this.stateProvince = stateProvince;
    }

    public StringFilter getPays() {
        return pays;
    }

    public StringFilter pays() {
        if (pays == null) {
            pays = new StringFilter();
        }
        return pays;
    }

    public void setPays(StringFilter pays) {
        this.pays = pays;
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
        final AdresseCriteria that = (AdresseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(departmentName, that.departmentName) &&
            Objects.equals(streetAddress, that.streetAddress) &&
            Objects.equals(postalCode, that.postalCode) &&
            Objects.equals(city, that.city) &&
            Objects.equals(stateProvince, that.stateProvince) &&
            Objects.equals(pays, that.pays) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, departmentName, streetAddress, postalCode, city, stateProvince, pays, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdresseCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (departmentName != null ? "departmentName=" + departmentName + ", " : "") +
            (streetAddress != null ? "streetAddress=" + streetAddress + ", " : "") +
            (postalCode != null ? "postalCode=" + postalCode + ", " : "") +
            (city != null ? "city=" + city + ", " : "") +
            (stateProvince != null ? "stateProvince=" + stateProvince + ", " : "") +
            (pays != null ? "pays=" + pays + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
