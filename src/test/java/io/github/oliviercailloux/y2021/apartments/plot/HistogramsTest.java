package io.github.oliviercailloux.y2021.apartments.plot;

import static org.junit.jupiter.api.Assertions.*;

import io.github.oliviercailloux.y2018.apartments.apartment.Apartment;
import io.github.oliviercailloux.y2018.apartments.apartment.Apartment.Builder;
import io.github.oliviercailloux.y2018.apartments.apartment.json.JsonConvert;
import io.github.oliviercailloux.y2018.apartments.valuefunction.Criterion;
import io.github.oliviercailloux.y2021.apartments.plot.Histograms;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class HistogramsTest {

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
	
  @Test
  void getDataAsAListTest() {
	  
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
	  ArrayList<Double> nbSleepingsStat = new ArrayList<>(Arrays.asList((double)apart1.getNbSleeping(),(double)apart2.getNbSleeping()));   
	  ArrayList<Double> dataList = Histograms.getDataAsAList(Criterion.NB_SLEEPING, aparts);
	  
	  assertEquals(dataList,nbSleepingsStat);
  }
  
  void saveImageTest() throws IOException {
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
	    Histograms floorAreaHist = Histograms.given(Criterion.FLOOR_AREA, aparts);
	    File file = new File("Doc/img/"+Histograms.getCrit()+".png");
	    floorAreaHist.saveImage();
	    assertTrue(file.exists());
  }
  
  void modifyCriterionTest() {
	  Builder apartBuilder = new Builder();
		Apartment apart1 = apartBuilder.setFloorArea(floorArea).setAddress(address)
			        .setNbBedrooms(nbBedrooms).setNbSleeping(nbSleeping).setNbBathrooms(nbBathrooms)
			        .setTerrace(hasTerrace).setFloorAreaTerrace(floorAreaTerrace).setDescription(description)
			        .setTitle(title).setWifi(wifi).setPricePerNight(pricePerNight).setNbMinNight(nbMinNight)
			        .setTele(tele).build();
			   apartBuilder = new Builder();
		Apartment apart2 = apartBuilder.setFloorArea(floorArea).setAddress(address )
			        .setNbBedrooms(nbBedrooms).setNbSleeping(nbSleeping).setNbBathrooms(nbBathrooms)
			        .setTerrace(hasTerrace).setFloorAreaTerrace(floorAreaTerrace).setDescription(description)
			        .setTitle(title).setWifi(wifi).setPricePerNight(pricePerNight).setNbMinNight(nbMinNight)
			        .setTele(tele).build();
			
		List<Apartment> aparts = new ArrayList<>(Arrays.asList(apart1,apart2));
	    Histograms teleToWifi = Histograms.given(Criterion.TELE, aparts);
	    Histograms wifi = Histograms.given(Criterion.WIFI, aparts);
	    teleToWifi.modifyCriterion(Criterion.TELE);
	    assertEquals(wifi,teleToWifi);
  }

}
