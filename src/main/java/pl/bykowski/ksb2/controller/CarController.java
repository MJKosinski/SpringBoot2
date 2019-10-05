package pl.bykowski.ksb2.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bykowski.ksb2.entity.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController()
@RequestMapping(value = "/cars", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })  //postman akceptuje tylko pierwsza wartosc na liscie?
public class CarController {

    private List<Car> carList;

    public CarController() {
        carList = new ArrayList<Car>();
        carList.add(new Car(1L, "Fiat", "126P", "pink"));
        carList.add(new Car(2L, "Polonez", "Caro", "orange"));
        carList.add(new Car(3L, "Syrena", "105", "blue"));
    }

    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        return new ResponseEntity<>(carList, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable long id) {

        Optional<Car> carById = getFirstbyId(id);

        if (carById.isPresent()) {
            return new ResponseEntity<>(carById.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Car> deleteCarById(@PathVariable long id) {

        Optional<Car> carById = getFirstbyId(id);

        if (carById.isPresent()) {
            carList.remove(carById.get());
            return new ResponseEntity<>(carById.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping("/color")
    public ResponseEntity<List<Car>> getCarsByColor(@RequestParam String color) {

        List<Car> carsByColor = carList.stream().filter(car -> car.getColor().equals(color.toLowerCase())).collect(Collectors.toList());

        if (carsByColor.size() > 0) {
            return new ResponseEntity<>(carsByColor, HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping
    public ResponseEntity addCar(@RequestBody Car car) {

        boolean add = carList.add(car);

        if (add) {
            return new ResponseEntity(HttpStatus.CREATED);
        }

        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @PutMapping
    public ResponseEntity<Car> changeCar(@RequestBody Car carToChange) {
        Optional<Car> firstbyId = getFirstbyId(carToChange.getId());

        if (firstbyId.isPresent()) {
            carList.remove(firstbyId.get());
            carList.add(carToChange);
            return new ResponseEntity<>(carToChange, HttpStatus.OK);

        }


        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Car> modifyCar(@PathVariable long id,
                                         @RequestParam(required = false) Long newId,
                                         @RequestParam(required = false) String newMark,
                                         @RequestParam(required = false) String newModel,
                                         @RequestParam(required = false) String newColor) {


        Optional<Car> firstbyId = getFirstbyId(id);
        if (firstbyId.isPresent()) {

            if (newId != null) firstbyId.get().setId(newId);
            if (newMark != null) firstbyId.get().setMark(newMark);
            if (newModel != null) firstbyId.get().setMark(newModel);
            if (newColor != null) firstbyId.get().setMark(newColor);

            if (newId == null && newMark == null && newModel == null && newColor == null)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(firstbyId.get(), HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }






    private Optional<Car> getFirstbyId(long id) {
        return carList.stream().filter(car -> car.getId() == id).findFirst();
    }
}


/*
Zadanie podstawowe:
Napisz REST API dla listy pojazdów. Pojazd będzie miał pola: id, mark, model, color.
API które będzie obsługiwało metody webowe:

do pobierania wszystkich pozycji
do pobierania elementu po jego id
do pobierania elementów w określonym kolorze (query)
do dodawania pozycji
do modyfikowania pozycji
do modyfikowania jednego z pól pozycji
do usuwania jeden pozycji
Przy starcie aplikacji mają dodawać się 3 pozycje.
 */