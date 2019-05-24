package io.github.oliviercailloux.y2018.apartments.valuefunction;

import java.util.Map;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class which enables the user to get the subjective value of a Boolean given in argument.
 * 
 */
public class BooleanValueFunction implements PartialValueFunction<Boolean>{
	private Map<Boolean, Double> subjective;
	private final static Logger LOGGER = LoggerFactory.getLogger(BooleanValueFunction.class);


	/**
	 * Create a map which associate a subjective value to true and a subjective value to false. This subjective value will take the value of 1 for the boolean that represent the "good value" and 0 for the other.
	 * @param preference a boolean that indicates whether true or false represent the "good" value for the corresponding attribute
	 */
	public BooleanValueFunction (boolean preference) {
		subjective = new HashMap<>();
		if (preference) {			
			subjective.put(true, (double) 1);
			subjective.put(false, (double) 0);
		}
		else {
			subjective.put(false, (double) 1);
			subjective.put(true, (double) 0);
		}

		LOGGER.info("The Map with the subjective values for true and false has been set with success in the BooleanValueFunction class.");
	}



	@Override
	public Double apply(Boolean objectiveData) {
		return subjective.get(objectiveData);
	}

	@Override
	public double getSubjectiveValue(Boolean objectiveData) throws IllegalArgumentException {
		return subjective.get(objectiveData);
	}

}




