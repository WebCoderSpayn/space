package com.space.repository;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Filter {
    private static List<Specification<Ship>> listSpecifications;

    public Filter() {
        listSpecifications = new ArrayList<>();
    }

    public Filter hasName(String name) {
        if (name != null) {
            listSpecifications.add((ship, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(ship.get("name"), "%" + name + "%"));
        }
        return this;
    }

    public Filter onPlanet(String planet) {
        if (planet != null) {
            listSpecifications.add((ship, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(ship.get("planet"), "%" + planet + "%"));
        }
        return this;
    }

    public Filter isShipType(ShipType shipType) {
        if (shipType != null) {
            listSpecifications.add((ship, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(ship.get("shipType"), shipType));
        }
        return this;
    }

    public Filter betweenDate(Long after, Long before) {
        if (after != null) {
            listSpecifications.add((ship, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(ship.get("prodDate"), new Date(after)));
        }
        if (before != null) {
            listSpecifications.add((ship, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(ship.get("prodDate"), new Date(before)));
        }
        return this;
    }

    public Filter isUsed(Boolean isUsed) {
        if (isUsed != null) {
            listSpecifications.add((ship, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(ship.get("isUsed"), isUsed));
        }
        return this;
    }

    public Filter inSpeed(Double minSpeed, Double maxSpeed) {
        if (minSpeed != null && minSpeed > 0.01) {
            listSpecifications.add((ship, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(ship.get("speed"), minSpeed));
        }

        if (maxSpeed != null && maxSpeed < 0.99) {
            listSpecifications.add((ship, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(ship.get("speed"), maxSpeed));
        }
        return this;
    }

    public Filter inCrewSize(Integer minCrewSize, Integer maxCrewSize) {
        if (minCrewSize != null && minCrewSize > 1 && minCrewSize < 9999) {
            listSpecifications.add((ship, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(ship.get("crewSize"), minCrewSize));
        }

        if (maxCrewSize != null && maxCrewSize < 9999 && maxCrewSize > 1) {
            listSpecifications.add((ship, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(ship.get("crewSize"), maxCrewSize));
        }
        return this;
    }

    public Filter inRating(Double minRating, Double maxRating) {
        if (minRating != null) {
            listSpecifications.add((ship, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(ship.get("rating"), minRating));
        }

        if (maxRating != null) {
            listSpecifications.add((ship, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(ship.get("rating"), maxRating));
        }
        return this;
    }

    public Specification<Ship> build() {
        Specification<Ship> specifications = null;
        if (listSpecifications.size() != 0) {
            specifications = Specification.where(listSpecifications.get(0));
            for (int i = 1; i < listSpecifications.size(); i++) {
                specifications = specifications.and(listSpecifications.get(i));
            }
        }
        return specifications;
    }
}
