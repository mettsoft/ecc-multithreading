package com.ecc.utility;

import java.util.Scanner;

import com.ecc.exception.MinimumBoundedIntegerException;
import com.ecc.utility.CheckedFunction;

public class InputHandler {
	private static final Scanner SCANNER = new Scanner(System.in);

	public static String getNextLine(String message) {
		System.out.print(message);
		return SCANNER.nextLine();	
	}

	public static <R> R getNextLine(String message, CheckedFunction<String, R> function) {
		while(true) {
			try {
				System.out.print(message);
				return function.apply(SCANNER.nextLine());
			}
			catch(Exception exception) {
				System.err.println("Error: " + exception.getMessage());
			}
		}
	}

	public static Integer parseInteger(String input, Integer minimumThreshold) throws MinimumBoundedIntegerException {
		Integer numericalInput = Integer.valueOf(input);
		if (numericalInput < minimumThreshold)
			throw new MinimumBoundedIntegerException(minimumThreshold);
		return numericalInput;
	}
}