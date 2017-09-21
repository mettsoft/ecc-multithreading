package com.ecc.utility;

import java.util.Random;

public class RandomGenerator {
	private static final Random RANDOM = new Random();

	public static Integer getInteger(Integer lowerBound, Integer upperBound) {
		return RANDOM.nextInt(upperBound - lowerBound + 1) + lowerBound;
	}

	public static Boolean getBoolean() {
		return RANDOM.nextBoolean();
	}
}