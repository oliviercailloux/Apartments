package io.github.oliviercailloux.y2018.apartments.valuefunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.oliviercailloux.y2018.apartments.apartment.Apartment;
import io.github.oliviercailloux.y2018.apartments.apartment.Apartment.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApartmentValueFunctionTests {

  ApartmentValueFunction valueFunction = new ApartmentValueFunction();

  Apartment a;

  /** Inits the apartmentValueFunction object and the apartment before each tests */
  @BeforeEach
  void initEach() {
    a = new Builder().setFloorArea(250).setAddress("108 rue de chat-ville Ville-d'Avray 92410")
        .setNbBedrooms(1).setNbSleeping(4).setNbBathrooms(1).setTerrace(true)
        .setFloorAreaTerrace(40)
        .setDescription("Une ferme rustique en compagnie de Dwight Schrute, interdit à Jim Halpert")
        .setTitle("Une ferme").setWifi(false).setPricePerNight(3.3).setNbMinNight(3).setTele(false)
        .build();

    LinearValueFunction floorAreaV = new LinearValueFunction(0d, 200d);
    valueFunction.setDoubleValueFunction(Criterion.FLOOR_AREA, floorAreaV);

    LinearValueFunction nbSleepingV = new LinearValueFunction(3d, 5d);
    valueFunction.setDoubleValueFunction(Criterion.NB_SLEEPING, nbSleepingV);

    ReversedLinearValueFunction nbMinNightV = new ReversedLinearValueFunction(7d, 30d);
    valueFunction.setDoubleValueFunction(Criterion.NB_MIN_NIGHT, nbMinNightV);

    BooleanValueFunction terraceV = new BooleanValueFunction(true);
    valueFunction.setBooleanValueFunction(Criterion.TERRACE, terraceV);

    LinearValueFunction nbBedroomsV = new LinearValueFunction(3d, 4d);
    valueFunction.setDoubleValueFunction(Criterion.NB_BEDROOMS, nbBedroomsV);

    ReversedLinearValueFunction pricePerNightV = new ReversedLinearValueFunction(20d, 40d);
    valueFunction.setDoubleValueFunction(Criterion.PRICE_PER_NIGHT, pricePerNightV);

    BooleanValueFunction teleV = new BooleanValueFunction(true);
    valueFunction.setBooleanValueFunction(Criterion.TELE, teleV);

    BooleanValueFunction wifiV = new BooleanValueFunction(true);
    valueFunction.setBooleanValueFunction(Criterion.WIFI, wifiV);

    LinearValueFunction nbBathroomsV = new LinearValueFunction(2d, 3d);
    valueFunction.setDoubleValueFunction(Criterion.NB_BATHROOMS, nbBathroomsV);

    LinearValueFunction floorAreaTerraceV = new LinearValueFunction(30d, 50d);
    valueFunction.setDoubleValueFunction(Criterion.FLOOR_AREA_TERRACE, floorAreaTerraceV);
  }

  /** Function to test the some setters */
  @Test
  void testCheckValue() {

    LinearValueFunction lvf =
        (LinearValueFunction) valueFunction.getDoubleValueFunction(Criterion.NB_SLEEPING);
    assertEquals(5d, lvf.getInterval().upperEndpoint());
    lvf = (LinearValueFunction) valueFunction.getDoubleValueFunction(Criterion.NB_BEDROOMS);
    assertEquals(4d, lvf.getInterval().upperEndpoint());
    lvf = (LinearValueFunction) valueFunction.getDoubleValueFunction(Criterion.NB_BATHROOMS);
    assertEquals(3d, lvf.getInterval().upperEndpoint());
  }

  /**
   * Function to test the computing of the subjective value of an apartment To compute the
   * subjective value of an apartment, here is the formula : sum(attributeSubjectiveValue *
   * attributeWeight)/sum(weight)
   */
  @Test
  void testApartmentValueFunction() {

    double subjectiveValueTele =
        valueFunction.getBooleanValueFunction(Criterion.TELE).getSubjectiveValue(a.getTele())
            * valueFunction.getWeightSubjectiveValue(Criterion.TELE);
    assertEquals(0, subjectiveValueTele);
    double subjectiveValueFloorArea =
        valueFunction.getDoubleValueFunction(Criterion.FLOOR_AREA).getSubjectiveValue(
            a.getFloorArea()) * valueFunction.getWeightSubjectiveValue(Criterion.FLOOR_AREA);
    assertEquals(0.1, subjectiveValueFloorArea, 0.0001);
    double subjectiveValuePrice = valueFunction.getDoubleValueFunction(Criterion.PRICE_PER_NIGHT)
        .getSubjectiveValue(a.getPricePerNight())
        * valueFunction.getWeightSubjectiveValue(Criterion.PRICE_PER_NIGHT);
    assertEquals(0.1, subjectiveValuePrice, 0.0001);

    double sumWeight = valueFunction.getWeightSubjectiveValue(Criterion.FLOOR_AREA)
        + valueFunction.getWeightSubjectiveValue(Criterion.FLOOR_AREA_TERRACE)
        + valueFunction.getWeightSubjectiveValue(Criterion.NB_BEDROOMS)
        + valueFunction.getWeightSubjectiveValue(Criterion.NB_BATHROOMS)
        + valueFunction.getWeightSubjectiveValue(Criterion.NB_MIN_NIGHT)
        + valueFunction.getWeightSubjectiveValue(Criterion.PRICE_PER_NIGHT)
        + valueFunction.getWeightSubjectiveValue(Criterion.NB_SLEEPING)
        + valueFunction.getWeightSubjectiveValue(Criterion.TELE)
        + valueFunction.getWeightSubjectiveValue(Criterion.TERRACE)
        + valueFunction.getWeightSubjectiveValue(Criterion.WIFI);
    assertEquals(1, sumWeight, 0.0001);

    assertEquals(0.5, valueFunction.getSubjectiveValue(a), 0.0001);

    sumWeight = sumWeight - valueFunction.getWeightSubjectiveValue(Criterion.TELE)
        - valueFunction.getWeightSubjectiveValue(Criterion.FLOOR_AREA)
        - valueFunction.getWeightSubjectiveValue(Criterion.PRICE_PER_NIGHT);
    valueFunction.setCriterionSubjectiveValueWeight(Criterion.TELE, 4.3);
    valueFunction.setCriterionSubjectiveValueWeight(Criterion.FLOOR_AREA, 2);
    valueFunction.setCriterionSubjectiveValueWeight(Criterion.PRICE_PER_NIGHT, 3);
    sumWeight = sumWeight + valueFunction.getWeightSubjectiveValue(Criterion.TELE)
        + valueFunction.getWeightSubjectiveValue(Criterion.FLOOR_AREA)
        + valueFunction.getWeightSubjectiveValue(Criterion.PRICE_PER_NIGHT);
    assertEquals(10, sumWeight, 0.0001);

    subjectiveValueTele =
        valueFunction.getBooleanValueFunction(Criterion.TELE).getSubjectiveValue(a.getTele())
            * valueFunction.getWeightSubjectiveValue(Criterion.TELE);
    assertEquals(0, subjectiveValueTele);
    subjectiveValueFloorArea =
        valueFunction.getDoubleValueFunction(Criterion.FLOOR_AREA).getSubjectiveValue(
            a.getFloorArea()) * valueFunction.getWeightSubjectiveValue(Criterion.FLOOR_AREA);
    assertEquals(2, subjectiveValueFloorArea, 0.0001);
    subjectiveValuePrice = valueFunction.getDoubleValueFunction(Criterion.PRICE_PER_NIGHT)
        .getSubjectiveValue(a.getPricePerNight())
        * valueFunction.getWeightSubjectiveValue(Criterion.PRICE_PER_NIGHT);
    assertEquals(3, subjectiveValuePrice, 0.0001);

    assertEquals(0.53, valueFunction.getSubjectiveValue(a), 0.0001);
  }

  /** Test if the weight setter throw a Illegal Argument Exception when needed */
  @Test
  void testExceptionIllegalArgWeightSetter() {

    assertThrows(IllegalArgumentException.class,
        () -> valueFunction.setCriterionSubjectiveValueWeight(Criterion.FLOOR_AREA, -1d));
    assertThrows(IllegalArgumentException.class,
        () -> valueFunction.setCriterionSubjectiveValueWeight(Criterion.NB_BEDROOMS, -1d));
    assertThrows(IllegalArgumentException.class,
        () -> valueFunction.setCriterionSubjectiveValueWeight(Criterion.NB_SLEEPING, -1d));
    assertThrows(IllegalArgumentException.class,
        () -> valueFunction.setCriterionSubjectiveValueWeight(Criterion.NB_BATHROOMS, -1d));
    assertThrows(IllegalArgumentException.class,
        () -> valueFunction.setCriterionSubjectiveValueWeight(Criterion.TERRACE, -1d));
    assertThrows(IllegalArgumentException.class,
        () -> valueFunction.setCriterionSubjectiveValueWeight(Criterion.FLOOR_AREA_TERRACE, -1d));
    assertThrows(IllegalArgumentException.class,
        () -> valueFunction.setCriterionSubjectiveValueWeight(Criterion.WIFI, -1d));
    assertThrows(IllegalArgumentException.class,
        () -> valueFunction.setCriterionSubjectiveValueWeight(Criterion.PRICE_PER_NIGHT, -1d));
    assertThrows(IllegalArgumentException.class,
        () -> valueFunction.setCriterionSubjectiveValueWeight(Criterion.NB_MIN_NIGHT, -1d));
    assertThrows(IllegalArgumentException.class,
        () -> valueFunction.setCriterionSubjectiveValueWeight(Criterion.TELE, -1d));
  }

  /** Function to test the adaptation to the subjective value weight of a criteria */
  @Test
  void testAdaptWeight() {

    assertThrows(IllegalArgumentException.class,
        () -> valueFunction.adaptWeight(Criterion.TELE, Criterion.TELE));
    valueFunction.setCriterionSubjectiveValueWeight(Criterion.TELE, 7d);
    valueFunction.setCriterionSubjectiveValueWeight(Criterion.TERRACE, 3d);
    assertEquals(7d, valueFunction.getWeightSubjectiveValue(Criterion.TELE));
    assertEquals(3d, valueFunction.getWeightSubjectiveValue(Criterion.TERRACE));
    valueFunction = valueFunction.adaptWeight(Criterion.TERRACE, Criterion.TELE);
    assertEquals(9d, valueFunction.getWeightSubjectiveValue(Criterion.TERRACE));
    assertEquals(1d, valueFunction.getWeightSubjectiveValue(Criterion.TELE));
  }

  /** Function to test if the bounds of an interval adapt well when needed */
  @Test
  void testAdaptBounds() {

    assertThrows(IllegalArgumentException.class,
        () -> valueFunction.adaptBounds(Criterion.TELE, 0d, true));
    valueFunction = valueFunction.adaptBounds(Criterion.FLOOR_AREA_TERRACE, 25d, true);
    LinearValueFunction lvf =
        (LinearValueFunction) valueFunction.getDoubleValueFunction(Criterion.FLOOR_AREA_TERRACE);
    assertEquals(25d, lvf.getInterval().lowerEndpoint());
    assertEquals(0.6, valueFunction.getDoubleValueFunction(Criterion.FLOOR_AREA_TERRACE)
        .getSubjectiveValue(a.getFloorAreaTerrace()));
    valueFunction = valueFunction.adaptBounds(Criterion.NB_BEDROOMS, 8, false);
    double nbRooms = a.getNbBedrooms();
    assertEquals(0d,
        valueFunction.getDoubleValueFunction(Criterion.NB_BEDROOMS).getSubjectiveValue(nbRooms));
    lvf = (LinearValueFunction) valueFunction.getDoubleValueFunction(Criterion.NB_BEDROOMS);
    assertEquals(8, lvf.getInterval().upperEndpoint());
  }

  /** Function to test if a random apartment generated respects some criteria */
  @Test
  void testGetRandomApartmentValueFunction() {

    ApartmentValueFunction apart = ApartmentValueFunction.getRandomApartmentValueFunction();
    assertEquals(1d,
        apart.getDoubleValueFunction(Criterion.FLOOR_AREA).getSubjectiveValue(a.getFloorArea()));
    LinearValueFunction lvf =
        (LinearValueFunction) apart.getDoubleValueFunction(Criterion.FLOOR_AREA_TERRACE);
    assertTrue(lvf.getInterval().upperEndpoint() <= 101d);
    assertTrue(apart.getWeightSubjectiveValue(Criterion.TELE) <= 1d);
    double sum = apart.getWeightSubjectiveValue(Criterion.TELE)
        + apart.getWeightSubjectiveValue(Criterion.FLOOR_AREA)
        + apart.getWeightSubjectiveValue(Criterion.FLOOR_AREA_TERRACE)
        + apart.getWeightSubjectiveValue(Criterion.NB_BATHROOMS)
        + apart.getWeightSubjectiveValue(Criterion.NB_BEDROOMS)
        + apart.getWeightSubjectiveValue(Criterion.NB_SLEEPING)
        + apart.getWeightSubjectiveValue(Criterion.NB_MIN_NIGHT)
        + apart.getWeightSubjectiveValue(Criterion.PRICE_PER_NIGHT)
        + apart.getWeightSubjectiveValue(Criterion.TERRACE)
        + apart.getWeightSubjectiveValue(Criterion.WIFI);
    assertEquals(1d, sum, 0.00001);
  }
}
