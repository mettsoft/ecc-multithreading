package com.ecc.exception;

public class MinimumBoundedIntegerException extends Exception {
	public MinimumBoundedIntegerException(int minimumThreshold) {
		super(String.format("The minimum must be %d.", minimumThreshold));
	}
}