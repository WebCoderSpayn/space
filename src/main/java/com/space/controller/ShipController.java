package com.space.controller;

import com.space.controller.exceptions.BadRequest;
import com.space.controller.exceptions.NotFound;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.Filter;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class ShipController {

    private ShipRepository repo;

    @Autowired
    public void setShipRepository(ShipRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/ships")
    public List<Ship> getShipsList(
            @RequestParam(required = false)
                    String name,
            @RequestParam(required = false)
                    String planet,
            @RequestParam(required = false)
                    ShipType shipType,
            @RequestParam(required = false)
                    Long after,
            @RequestParam(required = false)
                    Long before,
            @RequestParam(required = false)
                    Boolean isUsed,
            @RequestParam(required = false)
                    Double minSpeed,
            @RequestParam(required = false)
                    Double maxSpeed,
            @RequestParam(required = false)
                    Integer minCrewSize,
            @RequestParam(required = false)
                    Integer maxCrewSize,
            @RequestParam(required = false)
                    Double minRating,
            @RequestParam(required = false)
                    Double maxRating,
            @RequestParam(defaultValue = "ID", required = false)
                    ShipOrder order,
            @RequestParam(defaultValue = "0", required = false)
                    Integer pageNumber,
            @RequestParam(defaultValue = "3", required = false)
                    Integer pageSize
    ) {
        Pageable specificationSorted = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));
        Filter filter = new Filter()
            .hasName(name)
            .onPlanet(planet)
            .isShipType(shipType)
            .betweenDate(after, before)
            .isUsed(isUsed)
            .inSpeed(minSpeed, maxSpeed)
            .inCrewSize(minCrewSize, maxCrewSize)
            .inRating(minRating, maxRating);
        return repo.findAll(filter.build(), specificationSorted).getContent();
    }

    @GetMapping("/ships/{id}")
    public Ship editShip(@PathVariable String id) {
        return repo.findById(shipValidId(id)).orElseThrow(NotFound::new);
    }

    @PostMapping("/ships/{id}")
    public Ship updateShip( @RequestBody ShipParsed updateShip, @PathVariable String id) {
        boolean updatedField = false;
        Ship ship = repo.findById(shipValidId(id)).orElseThrow(NotFound::new);

        if (updateShip.getName() != null) {
            ship.setName(updateShip.getName());
            updatedField = true;
        }

        if (updateShip.getPlanet() != null) {
            ship.setPlanet(updateShip.getPlanet());
            updatedField = true;
        }

        if (updateShip.getShipType() != null) {
            ship.setShipType(updateShip.getShipType());
            updatedField = true;
        }

        if (updateShip.getProdDate() != null) {
            ship.setProdDate(updateShip.getProdDate());
            updatedField = true;
        }

        if (updateShip.getUsed() != null) {
            ship.setUsed(updateShip.getUsed());
            updatedField = true;
        }

        if (updateShip.getSpeed() != null) {
            ship.setSpeed(updateShip.getSpeed());
            updatedField = true;
        }

        if (updateShip.getCrewSize() != null) {
            ship.setCrewSize(updateShip.getCrewSize());
            updatedField = true;
        }

        if (updatedField) {
            ship.setRating(ship.calculateRating());
            repo.save(ship);
        }
        return ship;
    }

    @DeleteMapping("/ships/{id}")
    public void deleteShip(@PathVariable String id) {
        Ship ship = repo.findById(shipValidId(id)).orElseThrow(NotFound::new);
        repo.delete(ship);
    }

    @PostMapping("/ships")
    public Ship createShip(@RequestBody ShipParsed newShip) {
        Ship ship = new Ship();
        ship.setName(newShip.getNameNotNull());
        ship.setPlanet(newShip.getPlanetNotNull());
        ship.setShipType(newShip.getShipTypeNotNull());
        ship.setProdDate(newShip.getProdDateNotNull());
        ship.setSpeed(newShip.getSpeedNotNull());
        ship.setCrewSize(newShip.getCrewSizeNotNull());
        ship.setUsed(newShip.getUsedNotNull());
        ship.setRating(ship.calculateRating());
        return repo.save(ship);
    }

    @GetMapping("/ships/count")
    public long getCount(
            @RequestParam(required = false)
                    String name,
            @RequestParam(required = false)
                    String planet,
            @RequestParam(required = false)
                    ShipType shipType,
            @RequestParam(required = false)
                    Long after,
            @RequestParam(required = false)
                    Long before,
            @RequestParam(required = false)
                    Boolean isUsed,
            @RequestParam(required = false)
                    Double minSpeed,
            @RequestParam(required = false)
                    Double maxSpeed,
            @RequestParam(required = false)
                    Integer minCrewSize,
            @RequestParam(required = false)
                    Integer maxCrewSize,
            @RequestParam(required = false)
                    Double minRating,
            @RequestParam(required = false)
                    Double maxRating
    ) {
        Filter filter = new Filter()
            .hasName(name)
            .onPlanet(planet)
            .isShipType(shipType)
            .betweenDate(after, before)
            .isUsed(isUsed)
            .inSpeed(minSpeed, maxSpeed)
            .inCrewSize(minCrewSize, maxCrewSize)
            .inRating(minRating, maxRating);
        return repo.count(filter.build());
    }

    private long shipValidId(String id) {
        long parsedId;
        try {
            parsedId = Long.parseUnsignedLong(id);
            if (parsedId < 1) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            throw new BadRequest();
        }

        return parsedId;
    }
}
