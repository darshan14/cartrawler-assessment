package com.cartrawler.assessment;

import com.cartrawler.assessment.car.CarResult;
import com.cartrawler.assessment.service.CarService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


public class CarResultTest {

    private static Set<CarResult> cars;

    @BeforeAll
    static void createCarList() {
        cars = new HashSet<>();
        cars.add(new CarResult("Mercedes Viano", "SIXT", "FDMR", 205.18d, CarResult.FuelPolicy.FULLFULL));
        cars.add(new CarResult("Peugeot 107","AVIS", "MCMR", 30.05d, CarResult.FuelPolicy.FULLFULL));
        cars.add(new CarResult("Toyota C-HR","NIZA", "CDMR", 82.58d, CarResult.FuelPolicy.FULLEMPTY));
        cars.add(new CarResult("Volkswagen Up","DELPASO", "MDMR", 134.47d, CarResult.FuelPolicy.FULLEMPTY));
        cars.add(new CarResult("Opel Astra","GOLDCAR", "CLMR", 180.34d, CarResult.FuelPolicy.FULLFULL));
        cars.add(new CarResult("Mahindra Scorpio", "NIZA", "CMMV", 450.5d, CarResult.FuelPolicy.FULLEMPTY));
        cars.add(new CarResult("Peugeot 107","SIXT", "MCMR", 22.50d, CarResult.FuelPolicy.FULLFULL));
        cars.add(new CarResult("Volkswagen Up","DELPASO", "MDMR", 87.64d, CarResult.FuelPolicy.FULLEMPTY));
        cars.add(new CarResult("Volkswagen Up","DELPASO", "MDMR", 134.47d, CarResult.FuelPolicy.FULLEMPTY));
        cars.add(new CarResult("Opel Astra","GOLDCAR", "CLMR", 180.34d, CarResult.FuelPolicy.FULLFULL));
        cars.add(new CarResult("Mazda 3", "BUDGET", "CDAR", 156.29d, CarResult.FuelPolicy.FULLEMPTY));
        cars.add(new CarResult("Suzuki Swift", "RHODIUM", "CMMV", 56.4d, CarResult.FuelPolicy.FULLEMPTY));
    }

    @Test
    void testRemoveDuplicates() {
        CarService carService = new CarService();

        List<CarResult> carList = cars.stream().collect(Collectors.toMap(
                CarResult::toString, car -> car,  (exist, replace) -> exist
        )).values().stream().toList();

        assertEquals(10, carList.size());
    }

    @Test
    void testCorporateCarSort() {
        CarService carService = new CarService();
        Set<CarResult> sortedCarList =  carService.sortByConditions(cars);

        int listSize = sortedCarList.size();

        CarResult topCar = sortedCarList.stream().toList().get(0);
        CarResult bottomCar = sortedCarList.stream().toList().get(listSize - 1);

        assertTrue(carService.isCorporateGroupCar(topCar));
        assertFalse(carService.isCorporateGroupCar(bottomCar));
    }

    @Test
    void testMedianCondition() {

        double median = 200d;
        CarService carService = new CarService();
        Set<CarResult> sortedCarList =  carService.sortByConditions(cars);

        //sortedCarList.forEach(System.out::println);

        assertTrue(sortedCarList.stream().anyMatch(car -> car.getSupplierName().equals("BUDGET") && car.getRentalCost() < median));
        assertTrue(sortedCarList.stream().noneMatch(car -> car.getSupplierName().equals("AVIS")));
    }

}
