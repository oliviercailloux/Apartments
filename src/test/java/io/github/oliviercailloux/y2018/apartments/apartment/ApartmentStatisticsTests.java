package io.github.oliviercailloux.y2018.apartments.apartment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.oliviercailloux.y2018.apartments.apartment.ApartmentStatistics;
import io.github.oliviercailloux.y2018.apartments.apartment.Apartment.Builder;
import io.github.oliviercailloux.y2018.apartments.apartment.json.JsonConvert;
import io.github.oliviercailloux.y2018.apartments.valuefunction.Criterion;


 class ApartmentStatisticsTests {
 
	 private double floorArea = 456.56;
	 private String address = "A Random Address";
	 private int nbBedrooms = 5;
	 private int nbSleeping = 9;
	 private int nbBathrooms = 3;
	 private boolean hasTerrace = true;
	 private double floorAreaTerrace = 25.32;
	 private String description = "A Random Description for A Random House in A Random Address";
	 private String title = "A Random House";
	 private boolean wifi = true;
	 private double pricePerNight = 45.95;
	 private int nbMinNight = 60;
	 private boolean tele = false;
	 
   void getApartmentsSample() {
	   Builder apartBuilder = new Builder();
	   Apartment apart1 = apartBuilder.setFloorArea(floorArea).setAddress(address)
	        .setNbBedrooms(nbBedrooms).setNbSleeping(nbSleeping).setNbBathrooms(nbBathrooms)
	        .setTerrace(hasTerrace).setFloorAreaTerrace(floorAreaTerrace).setDescription(description)
	        .setTitle(title).setWifi(wifi).setPricePerNight(pricePerNight).setNbMinNight(nbMinNight)
	        .setTele(tele).build();
	   apartBuilder = new Builder();
	   Apartment apart2 = apartBuilder.setFloorArea(floorArea).setAddress(address)
	        .setNbBedrooms(nbBedrooms).setNbSleeping(nbSleeping).setNbBathrooms(nbBathrooms)
	        .setTerrace(hasTerrace).setFloorAreaTerrace(floorAreaTerrace).setDescription(description)
	        .setTitle(title).setWifi(wifi).setPricePerNight(pricePerNight).setNbMinNight(nbMinNight)
	        .setTele(tele).build();
	   
	   List<Apartment> aparts = new ArrayList<>(Arrays.asList(apart1,apart2));
	   ApartmentStatistics apartsBis = ApartmentStatistics.given(aparts, 2);
	   assertEquals(apartsBis.getMyApartments(),aparts);
     
   }
 @Test
   void testGetNumericStatistics() {
	 Builder apartBuilder = new Builder();
	 Apartment apart1 = apartBuilder.setFloorArea(floorArea).setAddress(address)
	        .setNbBedrooms(nbBedrooms).setNbSleeping(nbSleeping).setNbBathrooms(nbBathrooms)
	        .setTerrace(hasTerrace).setFloorAreaTerrace(floorAreaTerrace).setDescription(description)
	        .setTitle(title).setWifi(wifi).setPricePerNight(pricePerNight).setNbMinNight(nbMinNight)
	        .setTele(tele).build();
	   
	 List<Apartment> aparts = new ArrayList<>(Arrays.asList(apart1));
	 ApartmentStatistics listAparts = ApartmentStatistics.given(aparts, 1);
	 assertTrue(listAparts.getNumericStatistics(Criterion.NB_MIN_NIGHT).max() == (double)nbMinNight);
   
   }
 
   void testGetBooleanStatistics() {
	   Builder apartBuilder = new Builder();
	   Apartment apart1 = apartBuilder.setFloorArea(floorArea).setAddress(address)
		        .setNbBedrooms(nbBedrooms).setNbSleeping(nbSleeping).setNbBathrooms(nbBathrooms)
		        .setTerrace(hasTerrace).setFloorAreaTerrace(floorAreaTerrace).setDescription(description)
		        .setTitle(title).setWifi(wifi).setPricePerNight(pricePerNight).setNbMinNight(nbMinNight)
		        .setTele(tele).build();
	   
	   List<Apartment> aparts = new ArrayList<>(Arrays.asList(apart1));
	   ApartmentStatistics listAparts = ApartmentStatistics.given(aparts, 1);
	   assertEquals(listAparts.getBooleanStatistics(Criterion.TELE).get(true),tele);

   }
 
 }

  

