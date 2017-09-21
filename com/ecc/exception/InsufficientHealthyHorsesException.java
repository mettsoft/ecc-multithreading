package com.ecc.exception;

public class InsufficientHealthyHorsesException extends Exception {
	public InsufficientHealthyHorsesException(int actualCount, int minimumThreshold) {
		super(String.format("There are only %d healthy horses. There must be at least %d.", actualCount, minimumThreshold));
	}
}