package io.github.oliviercailloux.y2018.apartments.apartment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.oliviercailloux.y2018.apartments.apartment.ApartmentStatistics;
import io.github.oliviercailloux.y2018.apartments.apartment.json.JsonConvert;
import io.github.oliviercailloux.y2018.apartments.valuefunction.Criterion;


 class ApartmentStatisticsTests {
 
 List<Apartment> listOfApartments = JsonConvert.getDefaultApartments();
 
   void getApartmentsSample() {
     ArrayList<Apartment> sampleApartments = new ArrayList<>(Arrays.asList(listOfApartments.get(0),listOfApartments.get(1),listOfApartments.get(2)));
     ApartmentStatistics listOfAparts = ApartmentStatistics.given(listOfApartments, 3);
     assertEquals(listOfAparts.myApartments,sampleApartments);
     
   }
 @Test
   void testGetNumericStatistics() {
     double pricePerNight = (listOfApartments.get(0).getPricePerNight()+listOfApartments.get(1).getPricePerNight() )/2;
     double nbMinNight = listOfApartments.get(0).getNbMinNight();
     ApartmentStatistics listOfAparts = ApartmentStatistics.given(listOfApartments, 2);
     assertEquals(listOfAparts.getNumericStatistics(Criterion.NB_MIN_NIGHT).max(), Math.round(nbMinNight * 100.0) / 100.0);
     assertEquals(listOfAparts.getNumericStatistics(Criterion.PRICE_PER_NIGHT).mean(), Math.round(pricePerNight * 100.0) / 100.0);
   
   }
 
   void testGetBooleanStatistics() {
     int tele = 3;
     int terrace = 2;
     ApartmentStatistics listOfAparts = ApartmentStatistics.given(listOfApartments, 2);
     assertEquals(listOfAparts.getBooleanStatistics(Criterion.TELE).get(true),tele);
     assertEquals(listOfAparts.getBooleanStatistics(Criterion.TERRACE).get(true),terrace);

   }
 
 }

  

