package com.ecc.factory;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.ecc.exception.InsufficientHealthyHorsesException;
import com.ecc.model.Horse;
import com.ecc.utility.RandomGenerator;

public class HorseFactory {

	private static final Integer MINIMUM_SPEED = 1;
	private static final Integer MAXIMUM_SPEED = 10; 
	private static final Integer MINIMUM_NUMBER_OF_HEALTHY_HORSES = 2;

	public static List<Horse> getHealthyHorses(Integer numberOfHorses) throws InsufficientHealthyHorsesException {

		List<Horse> healthyHorses = new ArrayList<>(numberOfHorses);

		for (Integer i = 0; i < numberOfHorses; i++) {
			Horse horse = new Horse();
			horse.setIsHealthy(RandomGenerator.getBoolean());
			horse.setSpeed(RandomGenerator.getInteger(MINIMUM_SPEED, MAXIMUM_SPEED));
			if (horse.getIsHealthy()) {
				healthyHorses.add(horse);
			}
		}

		if (healthyHorses.size() < MINIMUM_NUMBER_OF_HEALTHY_HORSES)
			throw new InsufficientHealthyHorsesException(healthyHorses.size(), MINIMUM_NUMBER_OF_HEALTHY_HORSES);

		return healthyHorses;
	}
}