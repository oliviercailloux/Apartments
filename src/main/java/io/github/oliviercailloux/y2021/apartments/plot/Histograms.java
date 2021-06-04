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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The public class Histograms enables to get histograms according a given criterion.
 */

public class Histograms {
  
    final static HashSet<Criterion> listOfNumericFeatures = new HashSet<>(Arrays.asList(Criterion.FLOOR_AREA,Criterion.FLOOR_AREA_TERRACE,Criterion.NB_BATHROOMS,Criterion.NB_BEDROOMS,Criterion.NB_MIN_NIGHT,Criterion.NB_SLEEPING,Criterion.PRICE_PER_NIGHT));
    static List<Apartment> listOfApartments = JsonConvert.getDefaultApartments();
    private static final Logger LOGGER = LoggerFactory.getLogger(Histograms.class);
    
    
    public static Histograms given(Criterion featureName) {
      return new Histograms(featureName);
    }
    
    private Histograms(Criterion featureName) {
      launchHistogram(getDataAsAList(featureName)) ;
    }
    
    /**
    * Get the data of all the apartments and return them as a map in which the key is the criterion given
    * as a parameter and the value is a list of data.
    * @param a criterion that we want to study.
    * @return a HashMap that stores the criterion name and its data.
    */
    public static HashMap<Criterion, List<Double>> getDataAsAList(Criterion featureName) {
        
        checkArgument(listOfNumericFeatures.contains(featureName));
        LOGGER.info("{} is part of our criteria",featureName);

        HashMap<Criterion, List<Double>> dataMap = new HashMap<>();

        switch(featureName) {
        
        case FLOOR_AREA : {
            LOGGER.info("Data about the floor area are available :");
            List <Double> floorAreaStats = new ArrayList<>();
            for (int i=0; i< listOfApartments.size(); i++) {
                floorAreaStats.add(Math.round(listOfApartments.get(i).getFloorArea()* 100.0) / 100.0);
            }
            dataMap.put(featureName, floorAreaStats);
            return dataMap;
        }
        
        case FLOOR_AREA_TERRACE : {
            LOGGER.info("Data about the floor area terrace are available :");
            List <Double> floorAreaTerraceStats = new ArrayList<>();
            for (int i=0; i< listOfApartments.size(); i++) {
                floorAreaTerraceStats.add(Math.round(listOfApartments.get(i).getFloorAreaTerrace()* 100.0) / 100.0);
            }
            dataMap.put(featureName, floorAreaTerraceStats);
            return dataMap;
        }
        case PRICE_PER_NIGHT : {
            LOGGER.info("Data about the price per night statistics are available :");
            List <Double> pricePerNightStats = new ArrayList<>();
            for (int i=0; i< listOfApartments.size(); i++) {
                pricePerNightStats.add(Math.round(listOfApartments.get(i).getPricePerNight()* 100.0) / 100.0);
            }
            dataMap.put(featureName, pricePerNightStats);
            return dataMap;
            
        }
        case NB_BATHROOMS : {
            LOGGER.info("Data about the number of bathrooms are available :");
            List <Double> pricePerNightStats = new ArrayList<>();
            for (int i=0; i< listOfApartments.size(); i++) {
                pricePerNightStats.add(Math.round(listOfApartments.get(i).getPricePerNight()* 100.0) / 100.0);
            }
            dataMap.put(featureName, pricePerNightStats);
            return dataMap; 
        }
        
        case NB_BEDROOMS : {
            LOGGER.info("Data about the number of bedrooms are available :");
            List <Double> pricePerNightStats = new ArrayList<>();
            for (int i=0; i< listOfApartments.size(); i++) {
                pricePerNightStats.add(Math.round(listOfApartments.get(i).getPricePerNight()* 100.0) / 100.0);
            }
            dataMap.put(featureName, pricePerNightStats);
            return dataMap;
            
        }
        case NB_MIN_NIGHT: {
            LOGGER.info("Data about the the minimum number of night are available :");
            List <Double> pricePerNightStats = new ArrayList<>();
            for (int i=0; i< listOfApartments.size(); i++) {
                pricePerNightStats.add(Math.round(listOfApartments.get(i).getPricePerNight()* 100.0) / 100.0);
            }
            dataMap.put(featureName, pricePerNightStats);
            return dataMap;
            
        }
        case NB_SLEEPING : {
            LOGGER.info("Data about the number of sleeping are available :");
            List <Double> pricePerNightStats = new ArrayList<>();
            for (int i=0; i< listOfApartments.size(); i++) {
                pricePerNightStats.add(Math.round(listOfApartments.get(i).getPricePerNight()* 100.0) / 100.0);
            }
            dataMap.put(featureName, pricePerNightStats);
            return dataMap;   
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
    * Create an histogram and save it.
    * @param  a HashMap that stores the criterion name and its data.
    */
    
    public static void launchHistogram(HashMap<Criterion, List<Double>>  dataMap) {
         Entry<Criterion, List<Double>> entry = dataMap.entrySet().iterator().next();
         Criterion feature = entry.getKey();
         List<Double> dataList = entry.getValue();

        double[] data = dataList.stream().mapToDouble(Double::doubleValue).toArray();
        var dataset = new HistogramDataset();
        dataset.addSeries("key", data, 50);
        JFreeChart histogram = ChartFactory.createHistogram(feature.toString()+" statistics",feature.toString(), "Effectif", dataset);
        
        try {          
            ChartUtils.saveChartAsPNG(new File(feature+".png"), histogram, 450, 400);
            LOGGER.info("Image successfully created.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
     }

}
