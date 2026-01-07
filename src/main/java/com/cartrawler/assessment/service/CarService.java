package com.cartrawler.assessment.service;

import com.cartrawler.assessment.car.CarResult;
import java.util.*;
import java.util.stream.Collectors;

public class CarService {

    private static final List<String> CORPORATE_SUPPLIERS_LIST = List.of("AVIS", "BUDGET", "ENTERPRISE", "FIREFLY", "HERTZ", "SIXT", "THRIFTY");

    public Set<CarResult> sortByConditions(Set<CarResult> carList) {

        if (carList == null || carList.isEmpty()) {
            return Collections.emptySet();
        }

        // Removed the duplicated records from the list
        List<CarResult> distinctCarList = carList.stream().collect(Collectors.toMap(
                CarResult::toString, car -> car,  (exist, replace) -> exist
        )).values().stream().toList();


        // Created Group of cars on the basis of Corporate/ Non-Corporate and SIPP Code Rank (M, E, C, Other)
        Map<String, List<CarResult>> carGroups = distinctCarList.stream().collect(Collectors
                .groupingBy(car -> (isCorporateGroupCar(car) ? "Corporate" : "Non Corporate") +" - "+ getSIPPCodeRank(car.getSippCode())));


        // Removed the cars whose FuelType.FULLFULL and Rental Cost > Median of teh Group
        List<CarResult> finalBeforeSortedList = new ArrayList<CarResult>();

        carGroups.values().forEach((cars) -> {
            var median = calculateMedian(cars);
            for(CarResult car: cars) {
                if(!(car.getFuelPolicy() == CarResult.FuelPolicy.FULLFULL && car.getRentalCost() > median))
                    finalBeforeSortedList.add(car);
            }
        });


        /* Created a Comparator to sort on the basis of below conditions:
            1. Sort Corporate Group of Cars first
            2. Then sort on the basis of SIPP Code Rank
            3. Finally, Sorted on teh basis of low to high price
        */

        Comparator<CarResult> sortCarComparator = Comparator.comparing((CarResult car) -> !isCorporateGroupCar(car))
                .thenComparing(car -> getSIPPCodeRank((car.getSippCode())))
                .thenComparing(CarResult :: getRentalCost);


        List<CarResult> sortedByCorporateList = finalBeforeSortedList.stream().sorted(sortCarComparator).toList();

        return new LinkedHashSet<>(sortedByCorporateList);

    }


    public boolean isCorporateGroupCar(CarResult car) {
        return CORPORATE_SUPPLIERS_LIST.contains(car.getSupplierName().toUpperCase()) ;
    }

    public int getSIPPCodeRank(String sippCode) {

        int rank = 4;
        if(sippCode == null || sippCode.isEmpty())
            return rank;

        rank = switch (sippCode.charAt(0)) {
            case 'M' -> 1;
            case 'E' -> 2;
            case 'C' -> 3;
            default -> 4;
        };

        return rank;
    }

    public double calculateMedian(List<CarResult> cars) {

        int totalCars = cars.size();
        double median;

        List<CarResult> carList = cars.stream().sorted(Comparator.comparing(CarResult:: getRentalCost)).toList();

        if(totalCars % 2 == 0) {
            median = (carList.get(totalCars/2).getRentalCost() + carList.get(totalCars/2 - 1).getRentalCost()) /2;
        } else {
            median = carList.get(totalCars/2).getRentalCost();
        }

        return median;
    }

}
