package io.github.oliviercailloux.y2021.apartments.plot;


import org.eclipse.core.runtime.Path;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * The public class Histograms enables to get histograms according a given criterion.
 */

public class Histograms {
  
    private static final Logger LOGGER = LoggerFactory.getLogger(Histograms.class);
    
    private static JFreeChart myHistogram;
    private static Criterion crit;
    private List<Apartment> myApartments; 

    public static Histograms given(Criterion featureName, List<Apartment> apartments) {
        return new Histograms(featureName, apartments);
    }
    
    private Histograms(Criterion featureName, List<Apartment> apartments) {
        this.myHistogram  = createHistogram(getDataAsAList(featureName, apartments));
        this.crit = featureName;
        this.myApartments = apartments;
    }
    
    /**
    * Get all data of the given criterion of the list of apartments
    * 
    * @param a criterion that we want to study.
    * @return a ArrayList that stores the criterion data.
    */
    
    public static ArrayList<Double> getDataAsAList(Criterion featureName, List<Apartment> apartments) {
        
        switch(featureName) {
        
        case FLOOR_AREA :
            LOGGER.info("{} is part of our criteria",featureName);
            ArrayList <Double> floorAreaStats = new ArrayList<>();
            for (int i=0; i< apartments.size(); i++) {
                floorAreaStats.add(Math.round(apartments.get(i).getFloorArea()* 100.0) / 100.0);
            }
            LOGGER.info("Data about the floor area are available");
            return floorAreaStats;
        
        case FLOOR_AREA_TERRACE : 
            LOGGER.info("{} is part of our criteria",featureName);
            ArrayList <Double> floorAreaTerraceStats = new ArrayList<>();
            for (int i=0; i< apartments.size(); i++) {
                floorAreaTerraceStats.add(Math.round(apartments.get(i).getFloorAreaTerrace()* 100.0) / 100.0);
            }
            LOGGER.info("Data about the floor area terrace are available");
            return floorAreaTerraceStats;
        
        case PRICE_PER_NIGHT :
            LOGGER.info("{} is part of our criteria",featureName);
            ArrayList <Double> pricePerNightStats = new ArrayList<>();
            for (int i=0; i< apartments.size(); i++) {
                pricePerNightStats.add(Math.round(apartments.get(i).getPricePerNight()* 100.0) / 100.0);
            }
            LOGGER.info("Data about the price per night statistics are available");
            return pricePerNightStats;
        
        case NB_BATHROOMS : 
            LOGGER.info("{} is part of our criteria",featureName);
            ArrayList <Double> nbBathroomsStats = new ArrayList<>();
            for (int i=0; i< apartments.size(); i++) {
              nbBathroomsStats.add(Math.round(apartments.get(i).getNbBathrooms()* 100.0) / 100.0);
            }
            LOGGER.info("Data about the number of bathrooms are available");
            return nbBathroomsStats; 
        
        case NB_BEDROOMS :
            LOGGER.info("{} is part of our criteria",featureName);   
            ArrayList <Double> nbBedroomsStats = new ArrayList<>();
            for (int i=0; i< apartments.size(); i++) {
              nbBedroomsStats.add(Math.round(apartments.get(i).getNbBedrooms()* 100.0) / 100.0);
            }
            LOGGER.info("Data about the number of bedrooms are available");
            return nbBedroomsStats;
            
        case NB_MIN_NIGHT:
            LOGGER.info("{} is part of our criteria",featureName);
            ArrayList <Double> nbMinNightStats = new ArrayList<>();
            for (int i=0; i< apartments.size(); i++) {
              nbMinNightStats.add(Math.round(apartments.get(i).getNbMinNight()* 100.0) / 100.0);
            }
            LOGGER.info("Data about the the minimum number of night are available");
            return nbMinNightStats;
            
        case NB_SLEEPING : 
            LOGGER.info("{} is part of our criteria",featureName);
            ArrayList <Double> nbSleepingStats = new ArrayList<>();
            for (int i=0; i< apartments.size(); i++) {
              nbSleepingStats.add(Math.round(apartments.get(i).getNbSleeping()* 100.0) / 100.0);
            }
            LOGGER.info("Data about the number of sleeping are available");
            return nbSleepingStats;   
        
        default:
        	throw new IllegalArgumentException(featureName+" isn't a numeric Apartment feature");
        }
    }
    
    /**
    * Create and returns an histogram of the given criterion.
    * @param an ArrayList that stores the criterion and its data.
    */
    
    public static JFreeChart createHistogram(ArrayList<Double> dataList) {

        double[] data = dataList.stream().mapToDouble(Double::doubleValue).toArray();
        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("key", data, 50);
        LOGGER.info("The {} histogram has been successfully created.", crit);
        JFreeChart histogram = ChartFactory.createHistogram(crit+" statistics",crit.toString(), "Number of apartments", dataset);
        return histogram;
    }
    
    /**
     * Enables to save the image of the histogram stored in the instance of Histograms
     * 
	 * @throws IOException
     */
    
    public void saveImage() throws IOException {
     
      java.nio.file.Path path = Paths.get("Doc/img/"+crit+".png");
      ChartUtils.saveChartAsPNG(new File("Doc/img/"+crit+".png"), myHistogram, 450, 400);
      if (!Files.exists(path)) {
			throw new IllegalArgumentException("Please put a correct pathName !");
      }
      LOGGER.info("Image successfully created.");

    }
    
    /**
     * Enables to change the criteria and its linked histogram with a method 
     * that works like the given (but more usable from the user point of the view).
     */
    
    public Histograms modifyCriterion(Criterion newCrit) {
      
    	Histograms newHist = new Histograms(newCrit, this.myApartments);
    	LOGGER.info("A new histogram has been created.");
    	return newHist;
    }
    
    public static JFreeChart getMyHistogram() {
		return myHistogram;
	}
	
	public List<Apartment> getMyApartments() {
		return myApartments;
	}

	public static Criterion getCrit() {
		return crit;
	}
  
}
