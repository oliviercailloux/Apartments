package io.github.oliviercailloux.y2018.apartments.utils;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;

/** @author Amioplk, alexisperdereau Manages random ranges of doubles */
public class RandomRange {

  /**
   * @param s the total sum of the list
   * @param size the size of the list
   * @return range of size values with sum s
   */
  public static List<Double> weightRangeOfSum(double s, int size) {

    List<Double> weightRange = new ArrayList<>();

    for (int i = 0; i < size; ++i)
      weightRange.add(Math.random());

    /**
     * Found on
     * https://stackoverflow.com/questions/30125296/how-to-sum-a-list-of-integers-with-java-streams
     */
    double sum = weightRange.stream().reduce(0d, Double::sum);
    weightRange =
        weightRange.stream().map(d -> (d / sum) * s).collect(ImmutableList.toImmutableList());

    return weightRange;
  }
}
