package io.github.oliviercailloux.y2018.apartments.valuefunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.oliviercailloux.y2018.apartments.apartment.Apartment;
import io.github.oliviercailloux.y2018.apartments.apartment.Apartment.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApartementVFTests {

  ApartementVF linearAVF;
  Apartment a;

  /** Inits the apartmentValueFunction object and the apartment before each tests */
  @BeforeEach
  void initEach() {
    ApartementVF.Builder builderLinearAVF = new ApartementVF.Builder();
    a = new Builder().setFloorArea(250).setAddress("108 rue de chat-ville Ville-d'Avray 92410")
        .setNbBedrooms(1).setNbSleeping(4).setNbBathrooms(1).setTerrace(true)
        .setFloorAreaTerrace(40)
        .setDescription("Une ferme rustique en compagnie de Dwight Schrute, interdit à Jim Halpert")
        .setTitle("Une ferme").setWifi(false).setPricePerNight(3.3).setNbMinNight(3).setTele(false)
        .build();

    LinearValueFunction floorAreaV = new LinearValueFunction(0d, 200d);
    builderLinearAVF.setFloorAreaValueFunction(floorAreaV);

    LinearValueFunction nbSleepingV = new LinearValueFunction(3d, 5d);
    builderLinearAVF.setNbSleepingValueFunction(nbSleepingV);

    ReversedLinearValueFunction nbMinNightV = new ReversedLinearValueFunction(7d, 30d);
    builderLinearAVF.setNbMinNightValueFunction(nbMinNightV);

    BooleanValueFunction terraceV = new BooleanValueFunction(true);
    builderLinearAVF.setTerraceValueFunction(terraceV);

    LinearValueFunction nbBedroomsV = new LinearValueFunction(3d, 4d);
    builderLinearAVF.setNbBedroomsValueFunction(nbBedroomsV);

    ReversedLinearValueFunction pricePerNightV = new ReversedLinearValueFunction(20d, 40d);
    builderLinearAVF.setPricePerNightValueFunction(pricePerNightV);

    BooleanValueFunction teleV = new BooleanValueFunction(true);
    builderLinearAVF.setTeleValueFunction(teleV);

    BooleanValueFunction wifiV = new BooleanValueFunction(true);
    builderLinearAVF.setWifiValueFunction(wifiV);

    LinearValueFunction nbBathroomsV = new LinearValueFunction(2d, 3d);
    builderLinearAVF.setNbBathroomsValueFunction(nbBathroomsV);

    LinearValueFunction floorAreaTerraceV = new LinearValueFunction(30d, 50d);
    builderLinearAVF.setFloorAreaTerraceValueFunction(floorAreaTerraceV);
    for (Criterion c : Criterion.values()) {
      builderLinearAVF.setWeight(c, 10d);
    }
    linearAVF = builderLinearAVF.build();
  }

  /** Function to test the computing of the subjective value of an apartment */
  @Test
  void testLinearAVF() {
    assertEquals(0.5, linearAVF.getSubjectiveValue(a), 0.0001);
    linearAVF.withWeight(Criterion.TELE, 5d);
    assertEquals(10d, linearAVF.getWeight(Criterion.TELE));
    assertEquals(0.5, linearAVF.getSubjectiveValue(a), 0.00001);
  }

  /** Function to test the adaptation to the subjective value weight of a criteria */
  @Test
  void testAdaptWeight() {
    assertThrows(IllegalArgumentException.class, () -> linearAVF.withWeight(Criterion.TELE, -1d));

    assertEquals(0.5506, linearAVF.withWeight(Criterion.TELE, 0.8).getSubjectiveValue(a), 0.0001);
    assertEquals(0.5524, linearAVF.withWeight(Criterion.TELE, 0.5).getSubjectiveValue(a), 0.0001);
  }
}
