# CarTrawlerAssessment

Built an application to display the final the sorted list based on the given conditions.

The following operations are performed as a part of the assessment:
* Removed the duplicated records from the list.
* Created the Groups of cars based on below criteria:
    1. Corporate/ Non-Corporate Groups - (AVIS, BUDGET, ENTERPRISE, FIREFLY, HERTZ, SIXT, THRIFTY)
    2. SIPP Code Ranks starts with M, E, C
* Removed the cars whose FuelType.FULLFULL and Rental Cost is above the Median of the Group.
* Created a Comparator to sort on the basis of below conditions:
  1. Sort Corporate Group of Cars first
  2. Then sort on the basis of SIPP Code Rank
  3. Finally, Sorted on teh basis of low to high price
* Finally, written few test cases to check the written business logic.

