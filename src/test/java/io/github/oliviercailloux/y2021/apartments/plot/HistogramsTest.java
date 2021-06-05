package io.github.oliviercailloux.y2021.apartments.plot;

import static org.junit.jupiter.api.Assertions.*;

import io.github.oliviercailloux.y2018.apartments.apartment.Apartment;
import io.github.oliviercailloux.y2018.apartments.apartment.json.JsonConvert;
import io.github.oliviercailloux.y2018.apartments.valuefunction.Criterion;
import io.github.oliviercailloux.y2021.apartments.plot.Histograms;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class HistogramsTest {

  @Test
  void getDataAsAListTest() {
    List<Apartment> listOfApartments = JsonConvert.getDefaultApartments();

    ArrayList<Double> nbSleeping = new ArrayList<>(Arrays.asList((double)listOfApartments.get(0).getNbSleeping(),
                                                                 (double)listOfApartments.get(1).getNbSleeping(),
                                                                 (double)listOfApartments.get(2).getNbSleeping()
                                                                 )); 
    ArrayList<Double> dataList = Histograms.getDataAsAList(Criterion.NB_SLEEPING);
    assertEquals(nbSleeping.get(0),dataList.get(0));
    assertEquals(nbSleeping.get(1),dataList.get(1));
    assertEquals(nbSleeping.get(2),dataList.get(2));
  }
  
  void saveImageTest() {
    Histograms floorAreaHist = Histograms.given(Criterion.FLOOR_AREA);
    File file = new File("Doc/img/"+Histograms.crit+".png");
    floorAreaHist.saveImage();
    assertTrue(file.exists());
  }
  
  void modifyCriterionTest() {
    Histograms teleToWifi = Histograms.given(Criterion.TELE);
    Histograms wifi = Histograms.given(Criterion.WIFI);
    teleToWifi.modifyCriterion(Criterion.TELE);
    assertEquals(wifi,teleToWifi);
  }

}
