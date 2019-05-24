package io.github.oliviercailloux.y2018.apartments.piecewise;

import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Range;


import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Iterator;

/**
 * 
 * The public class PricewiseLinearValueFunction enables the creation of an object with a criteria 
 * which represents a property of an apartment, and two numbers which represent first the value 
 * of the criteria ( for example the number of rooms ) and the utility associated to this
 * criteria.
 * The class enables the user to know for any value of the criteria, the utility associated.
 */
public class PiecewiseLinearValueFunction implements IPiecewiseLinearValueFunction {

	private String criteria;
	private NavigableMap<Integer, Double> breakPoints;
	private final static Logger LOGGER = LoggerFactory.getLogger(PiecewiseLinearValueFunction.class);

	/**
	 * Constructor of the object {@link PricewiseLinearValueFunction}
	 * @param crit
	 * 			Label of the criteria
	 */
	public PiecewiseLinearValueFunction(String crit)
	{
		breakPoints = new ConcurrentSkipListMap<>();
		criteria = crit;
	}
	

	@Override
	public void setUtility(int key, double value) {
		
		if (breakPoints.containsKey(key)) {
			LOGGER.error("The key "+key+" is already in the map.");
			throw new IllegalArgumentException("The key is already in the Map.");
		}
		
		if (key<0 || value<0 || value>1) {
			if (key<0)
				LOGGER.error("The key has not been set because the value of the key is strictly negative.");
			if (value<0 || value>1)
				LOGGER.error("The key has not been set because the value of the utility doesn't respect the constraints.");
			throw new IllegalArgumentException("The key or the utility in parameter is not in keeping with the rules.");
		}
		
		breakPoints.put(key, value);
		LOGGER.info("Utility "+key+" with the value "+value+" set with sucess.");
			
	
	}

	@Override
	public String toString()
	{
		return  "This object is defining the following criteria :  "+ criteria + 
				". It contains " + this.breakPoints.size() + " values";
	}

	/**
	 * This method calculates the linear value of a function. 
	 * If the point A and the point B are not different, the method returns
	 * an IllegalArgumentException.
	 * @param a the Point2D A
	 * @param b the Point2D B
	 * @return a double number which corresponds to the linear value.
	 */
	private double getLinearValue(Point2D a, Point2D b) {
		
		if ( a.getX() == b.getX()) {
			LOGGER.error("The linear value cannot be calculated.The abscisse of the point A "+a.getX()+" can't be the same than the abscisse of the point B "+b.getX());
			throw new IllegalArgumentException("The points are the same");
		}

		return ( b.getY() - a.getY()) / (b.getX() - a.getX());
		
	}
	
	/**
	 * This method calculates the ordinate value of a function.
	 * @param a the Point2D A
	 * @param b the Point2D B
	 * @return a double number which corresponds to the ordinate value.
	 */
	private double getOrdinateValue(Point2D a, Point2D b) {
		double linearValue = getLinearValue(a,b);
		return a.getY() - (a.getX()* linearValue);
	}
	

	@Override
	public int getMaxKey() {
		Iterator<Integer> k = breakPoints.keySet().iterator();
		int max = 0;
		int tmp = 0;
		while (k.hasNext()) {
			tmp = k.next();
			if (max< tmp)
				max = tmp;
		}
		return max;
	}
	

	@Override
	public int getMinKey() {
		Iterator<Integer> k = breakPoints.keySet().iterator();
		int min = -1;
		int tmp = 0;
		while (k.hasNext()) {
			tmp = k.next();
			if (min> tmp || min == -1)
				min = tmp;
		}
		return min;
	}
	

	@Override
	public Range<Integer> getInterval(int key) throws IOException {
		
		if (key>getMaxKey()) {
			LOGGER.error("The key value "+key+" is more than "+getMaxKey()+ ". No coherent value to return for the range.");
			throw new IllegalArgumentException("No coherent value to return for the range");
		}
		
		if (key<getMinKey()) {
			LOGGER.error("The key value "+key+" is less than "+getMinKey()+ ". No coherent value to return for the range.");
			throw new IllegalArgumentException("No coherent value to return for the range");
		}
		
		Iterator<Integer> k = breakPoints.keySet().iterator();
		int delta1= -1;
		int delta2 = -1;
		int key1 = 0;
		int key2 = 0;
		int tmp = 0;
		
		while (k.hasNext()) {		
			tmp = k.next();
			if (tmp < key) {
				if (delta1  == -1) {
					delta1 = key - tmp;
					key1 = tmp;
				}
				else if (delta1> (key - tmp)) {
					delta1 = (key - tmp);
					key1 = tmp;
				}
			}
			else if (tmp>key) {
				if (delta2 == -1) {
					delta2 = tmp - key;
					key2 = tmp;
				}
				else if (delta2> (tmp - key)) {
					delta2 = (tmp - key);
					key2 = tmp;
				} 
			}
		}
		
		Range<Integer> interval = Range.closed(key1, key2);
		LOGGER.info("The range for the utility has been set with success.");
		
		return interval;
		
		
	}
	

	@Override
	public double getUtility(int key) throws IOException {
		
		if (breakPoints.containsKey(key))
			return breakPoints.get(key);
		
		if (breakPoints.size()<2) {
			LOGGER.error("The utility map needs more couples.");
			throw new IllegalStateException("Need more couples (minimum of 2) to identify the linear value");
		}
		
		
		Range<Integer> intervalKey = getInterval(key);
		int lowerBound = intervalKey.lowerEndpoint();
		int upperBound = intervalKey.upperEndpoint();
		
		Point2D lowerBoundPoint = new Point2D.Double(lowerBound,breakPoints.get(lowerBound));
		Point2D upperBoundPoint = new Point2D.Double(upperBound,breakPoints.get(upperBound));
		
		double value = getOrdinateValue(lowerBoundPoint,upperBoundPoint);
		
		LOGGER.info("The utility has been returned with success.");
		
		return key*getLinearValue(lowerBoundPoint,upperBoundPoint) + value;
	} 
		

}
