package io.github.oliviercailloux.y2021.apartments.plot;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.HistogramDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.oliviercailloux.y2018.apartments.apartment.Apartment;
import io.github.oliviercailloux.y2018.apartments.apartment.json.JsonConvert;
import io.github.oliviercailloux.y2018.apartments.valuefunction.Criterion;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * The public class Histograms enables to get histograms according a given criterion.
 */

public class Histograms {
  
    private static List<Apartment> listOfApartments = JsonConvert.getDefaultApartments();
    private static final Logger LOGGER = LoggerFactory.getLogger(Histograms.class);
    
    private static JFreeChart myHistogram;
    private static Criterion crit;
    
    public static Histograms given(Criterion featureName) {
      return new Histograms(featureName);
    }
    
    private Histograms(Criterion featureName) {
      this.myHistogram  = launchHistogram(getDataAsAList(featureName));
      this.crit = featureName;
    }
    
    /**
    * Get all data of the given criterion of the list of apartments
    * 
    * @param a criterion that we want to study.
    * @return a ArrayList that stores the criterion data.
    */
    
    public static ArrayList<Double> getDataAsAList(Criterion featureName) {
        
        switch(featureName) {
        
        case FLOOR_AREA : {
            LOGGER.info("{} is part of our criteria",featureName);
            ArrayList <Double> floorAreaStats = new ArrayList<>();
            for (int i=0; i< listOfApartments.size(); i++) {
                floorAreaStats.add(Math.round(listOfApartments.get(i).getFloorArea()* 100.0) / 100.0);
            }
            LOGGER.info("Data about the floor area are available");
            return floorAreaStats;
        }
        
        case FLOOR_AREA_TERRACE : {
            LOGGER.info("{} is part of our criteria",featureName);
            ArrayList <Double> floorAreaTerraceStats = new ArrayList<>();
            for (int i=0; i< listOfApartments.size(); i++) {
                floorAreaTerraceStats.add(Math.round(listOfApartments.get(i).getFloorAreaTerrace()* 100.0) / 100.0);
            }
            LOGGER.info("Data about the floor area terrace are available");
            return floorAreaTerraceStats;
        }
        
        case PRICE_PER_NIGHT : {
            LOGGER.info("{} is part of our criteria",featureName);
            ArrayList <Double> pricePerNightStats = new ArrayList<>();
            for (int i=0; i< listOfApartments.size(); i++) {
                pricePerNightStats.add(Math.round(listOfApartments.get(i).getPricePerNight()* 100.0) / 100.0);
            }
            LOGGER.info("Data about the price per night statistics are available");
            return pricePerNightStats;
        }
        
        case NB_BATHROOMS : {
            LOGGER.info("{} is part of our criteria",featureName);
            ArrayList <Double> nbBathroomsStats = new ArrayList<>();
            for (int i=0; i< listOfApartments.size(); i++) {
              nbBathroomsStats.add(Math.round(listOfApartments.get(i).getNbBathrooms()* 100.0) / 100.0);
            }
            LOGGER.info("Data about the number of bathrooms are available");
            return nbBathroomsStats; 
            
        }
        
        case NB_BEDROOMS : {
            LOGGER.info("{} is part of our criteria",featureName);   
            ArrayList <Double> nbBedroomsStats = new ArrayList<>();
            for (int i=0; i< listOfApartments.size(); i++) {
              nbBedroomsStats.add(Math.round(listOfApartments.get(i).getNbBedrooms()* 100.0) / 100.0);
            }
            LOGGER.info("Data about the number of bedrooms are available");
            return nbBedroomsStats;
            
        }
        case NB_MIN_NIGHT: {
            LOGGER.info("{} is part of our criteria",featureName);
            ArrayList <Double> nbMinNightStats = new ArrayList<>();
            for (int i=0; i< listOfApartments.size(); i++) {
              nbMinNightStats.add(Math.round(listOfApartments.get(i).getNbMinNight()* 100.0) / 100.0);
            }
            LOGGER.info("Data about the the minimum number of night are available");
            return nbMinNightStats;
            
        }
        case NB_SLEEPING : {
            LOGGER.info("{} is part of our criteria",featureName);
            ArrayList <Double> nbSleepingStats = new ArrayList<>();
            for (int i=0; i< listOfApartments.size(); i++) {
              nbSleepingStats.add(Math.round(listOfApartments.get(i).getNbSleeping()* 100.0) / 100.0);
            }
            LOGGER.info("Data about the number of sleeping are available");
            return nbSleepingStats;   
            
        }
        
        case TELE:
        	throw new IllegalArgumentException(featureName+" isn't a numeric Apartment feature");

        case TERRACE:
        	throw new IllegalArgumentException(featureName+" isn't a numeric Apartment feature");

        case WIFI:
        	throw new IllegalArgumentException(featureName+" isn't a numeric Apartment feature");

        default:
        	throw new IllegalArgumentException(featureName+" isn't a numeric Apartment feature");
        }
    }
    
    /**
    * Create and returns an histogram of the given criterion.
    * @param an ArrayList that stores the criterion and its data.
    */
    
    public static JFreeChart launchHistogram(ArrayList<Double> dataList) {

        double[] data = dataList.stream().mapToDouble(Double::doubleValue).toArray();
        var dataset = new HistogramDataset();
        dataset.addSeries("key", data, 50);
        LOGGER.info("The {} histogram has been successfully created.", crit.toString());
        JFreeChart histogram = ChartFactory.createHistogram(crit.toString()+" statistics",crit.toString(), "Effectif", dataset);
        
        return histogram;
    }
    
    /**
     * Enables to save the image of the histogram stored in the instance of Histograms
     */
    
    public void saveImage() {
     
      try {          
            ChartUtils.saveChartAsPNG(new File("Doc/img/"+crit+".png"), myHistogram, 450, 400);
            LOGGER.info("Image successfully created.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Enables to change the criteria and its linked histogram with a method 
     * that works like the given (but more usable from the user point of the view).
     */
    
    public Histograms modifyCriterion(Criterion newCrit) {
      
      Histograms newHist = new Histograms(newCrit)  ;
      LOGGER.info("A new histogram has been created.");
      return newHist;
      
    }
  
}
