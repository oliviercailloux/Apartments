package io.github.oliviercailloux.y2018.apartments.apartment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.math.Stats;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import io.github.oliviercailloux.y2018.apartments.apartment.Apartment.Builder;
import io.github.oliviercailloux.y2018.apartments.apartment.json.JsonConvert;
import io.github.oliviercailloux.y2018.apartments.valuefunction.Criterion;


public class ApartmentStatistics {

  private static final Logger LOGGER = LoggerFactory.getLogger(Apartment.class);
  
  private List<Apartment> myApartments; 
  
  public static ApartmentStatistics given(List<Apartment> listApartments, Integer number) {
    return new ApartmentStatistics(listApartments, number);
  }
  
  private ApartmentStatistics(List<Apartment> listApartments, Integer number) {
    this.myApartments = getApartmentsSample(listApartments, number);
  }

  /**
   * Give a list of apartments on which we will work, his length is given by the user.
   * In the future, we would want to sort all apartments according to their value function of each apartment. 
   * So that, the number would take the best apartments of the sorted list.
   * 
   * @param a larger list of apartments and the number that we want to study 
   * @return a sample of apartments
   */
  
  private List<Apartment> getApartmentsSample(List<Apartment> listApartments, Integer number) {
    
	  List<Apartment> apartments = JsonConvert.getDefaultApartments();
	  checkArgument(number<=apartments.size() && number>0);
	    
	  List<Apartment> newApartments = new ArrayList<>();
	  newApartments.addAll(listApartments.subList(0, number));
	    
	  LOGGER.info("A list of {} apartments is available", number);
	  return newApartments;
  }
  
  /**
   * Load numeric data of the given criteria and create a Stats object in order to get statistics of our apartments.
   * 
   * More information on Stats : https://guava.dev/releases/23.0/api/docs/com/google/common/math/Stats.html
   * 
   * @param the criterion that we want to extract statistics.
   * @return an object of Stats type.
   */
  
  public Stats getNumericStatistics(Criterion featureName) {
    
    HashMap<Criterion, ArrayList<Double>> criteriaStats = new HashMap<>();
    checkNotNull(featureName);
    
    switch (featureName) {

      case FLOOR_AREA: 
        ArrayList<Double> floorAreaStats = new ArrayList<>();
        for (int i = 0; i < myApartments.size(); i++) {
          floorAreaStats.add(Math.round(myApartments.get(i).getFloorArea() * 100.0) / 100.0);
        }
        LOGGER.info("Floor area statistics are available");
        return Stats.of(floorAreaStats);
        
      case FLOOR_AREA_TERRACE: 
        ArrayList<Double> floorAreaTerraceStats = new ArrayList<>();
        for (int i = 0; i < myApartments.size(); i++) {
          floorAreaTerraceStats.add(Math.round(myApartments.get(i).getFloorAreaTerrace() * 100.0) / 100.0);
        }
        LOGGER.info("Floor area terrace statistics are available");
        return Stats.of(floorAreaTerraceStats);
      
      case NB_BATHROOMS: {
        ArrayList<Double> nbBathroomsStats = new ArrayList<>();
        for (int i = 0; i < myApartments.size(); i++) {
          nbBathroomsStats.add(Double.valueOf(myApartments.get(i).getNbBathrooms()));
        }
        LOGGER.info("Number of bathrooms statistics are available");
        return Stats.of(nbBathroomsStats);
      }

      case NB_BEDROOMS: {
        ArrayList<Double> nbBedroomsStats = new ArrayList<>();
        for (int i = 0; i < myApartments.size(); i++) {
          nbBedroomsStats.add(Double.valueOf(myApartments.get(i).getNbBedrooms()));
        }
        criteriaStats.put(featureName, nbBedroomsStats);
        LOGGER.info("Number of bedrooms statistics are available");
        return Stats.of(nbBedroomsStats);
      }

      case NB_MIN_NIGHT : {
        ArrayList<Double> nbMinNightStats = new ArrayList<>();
        for (int i = 0; i < myApartments.size(); i++) {
          nbMinNightStats.add(Double.valueOf(myApartments.get(i).getNbMinNight()));
        }
        LOGGER.info("The requested minimum number of night statistics are available");
        return Stats.of(nbMinNightStats);
      }

      case NB_SLEEPING : {
        ArrayList<Double> nbSleepingStats = new ArrayList<>();
        for (int i = 0; i < myApartments.size(); i++) {
          nbSleepingStats.add(Double.valueOf(myApartments.get(i).getNbSleeping()));
        }
        LOGGER.info("The number of sleepings statistics are available");
        return Stats.of(nbSleepingStats);
      }

      case PRICE_PER_NIGHT : {
        ArrayList<Double> pricePerNightStats = new ArrayList<>();
        for (int i = 0; i < myApartments.size(); i++) {
          pricePerNightStats.add(Math.round(myApartments.get(i).getPricePerNight() * 100.0) / 100.0);
        }
        LOGGER.info("The price per night statistics are available");
        return Stats.of(pricePerNightStats);
     
      }
      default:
        throw new IllegalArgumentException(featureName+" isn't a numeric Apartment feature");
  }
    
}
 
  /**
   * Load boolean data of the given criteria and create an HashMap (size 2) that has as keys true and false 
   * and as values their respective occurrences for the given apartments.
   * 
   * @param the criterion that we want to extract statistics.
   * @return an HashMap with the occurrences of each boolean.
   */
  
public Map<Boolean,Integer> getBooleanStatistics(Criterion featureName) {
  
  checkNotNull(featureName);
  Map<Boolean, Integer> results = new HashMap<>();
  int yes = 0;
  int no = 0;
  
  switch (featureName) {

    case TELE: 
      for (int i = 0; i < myApartments.size(); i++) {
        if (myApartments.get(i).getTele()) {
          yes++;
        } else {
          no++;
        }
      }
      results.put(true, yes);
      results.put(false, no);
      LOGGER.info("The {} statistics are available", featureName);
      return results;
    
    case TERRACE : 
      for (int i = 0; i < myApartments.size(); i++) {
        if (myApartments.get(i).getTerrace()) {
          yes++;
        } else {
          no++;
        }
      }
      results.put(true, yes);
      results.put(false, no);
      LOGGER.info("The {} presence statistics are available", featureName);
      return results;
    
    case WIFI : 
      for (int i = 0; i < myApartments.size(); i++) {
        if (myApartments.get(i).getTerrace()) {
          yes++;
        } else {
          no++;
        }
      }
      results.put(true, yes);
      results.put(false, no);
      LOGGER.info("The {} presence statistics are available", featureName);
      return results;
      
    default:
      throw new IllegalArgumentException(featureName+" isn't a boolean Apartment feature");
    }
  
  }

	public List<Apartment> getMyApartments() {
		return myApartments;
	}


}
