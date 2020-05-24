package com.space.controller;

import com.space.controller.exceptions.BadRequest;
import com.space.model.ShipType;

import java.util.Calendar;
import java.util.Date;

public class ShipParsed {
    private String name;
    private String planet;
    private ShipType shipType;
    private Long prodDate;
    private Boolean isUsed;
    private Double speed;
    private Integer crewSize;

    public String getName() {
        if (name != null && (name.isEmpty() || name.length() > 50)) {
            throw new BadRequest();
        }
        return name;
    }

    public String getNameNotNull() {
        String name = getName();
        if (name == null) {
            throw new BadRequest();
        }
        return name;
    }

    public String getPlanet() {
        if (planet != null && (planet.isEmpty() || planet.length() > 50)) {
            throw new BadRequest();
        }
        return planet;
    }

    public String getPlanetNotNull() {
        String planet = getPlanet();
        if (planet == null) {
            throw new BadRequest();
        }
        return planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public ShipType getShipTypeNotNull() {
        if (shipType == null) throw new BadRequest();
        return shipType;
    }

    public Date getProdDate() {
        if (prodDate == null) return null;

        if (prodDate < 0) throw new BadRequest();
        Calendar calendar = Calendar.getInstance();
        Date updateDate = new Date(prodDate);
        calendar.setTime(updateDate);
        int year = calendar.get(Calendar.YEAR);
        if (year < 2800 || year > 3019) throw new BadRequest();

        return new Date(prodDate);
    }

    public Date getProdDateNotNull() {
        Date prodDate = getProdDate();
        if (prodDate == null) throw new BadRequest();
        return prodDate;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public Boolean getUsedNotNull() {
        return isUsed == null ? false : isUsed;
    }

    public Double getSpeed() {
        if (speed != null && (speed < 0.01 || speed > 0.99)) {
            throw new BadRequest();
        }
        return speed;
    }

    public Double getSpeedNotNull() {
        Double speed = getSpeed();
        if (speed == null) {
            throw new BadRequest();
        }
        return speed;
    }

    public Integer getCrewSize() {
        if (crewSize != null && (crewSize < 1 || crewSize > 9999)) {
            throw new BadRequest();
        }
        return crewSize;
    }

    public Integer getCrewSizeNotNull() {
        Integer crewSize = getCrewSize();
        if (crewSize == null) {
            throw new BadRequest();
        }
        return crewSize;
    }
}
