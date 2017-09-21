import java.util.Random;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Scanner;

import Utility.PromptBuilder;
import Service.HorseRaceService;
import Model.Horse;
import Exception.MinimumBoundedIntegerException;

public class Main {
	private static final Integer MINIMUM_NUMBER_OF_HORSES_REQUIRED = 3;
	private static final Integer MINIMUM_DISTANCE_TO_FINISH_LINE = 20;

	private static final String PROMPT_NUMBER_OF_HORSES = String.format("Enter the number of horses (min of %d): ", MINIMUM_NUMBER_OF_HORSES_REQUIRED);
	private static final String PROMPT_DISTANCE_TO_FINISH_LINE = String.format("Enter the distance to finish line (min of %d): ", MINIMUM_DISTANCE_TO_FINISH_LINE);
	private static final String PROMPT_HORSE_NAME = "Enter horse-%d name: ";
	private static final String PROMPT_HORSE_WAR_CRY = "Enter horse-%d war cry: ";

	public static void main(String[] args) {

		PromptBuilder promptBuilder = new PromptBuilder(System.in, System.out);
		HorseRaceService race = null;

		do {
			try {
				Integer numberOfHorses = null;
				Integer distanceToFinishLine = null;

				numberOfHorses = promptBuilder.prompt(PROMPT_NUMBER_OF_HORSES)
					.getNextLine(input -> { 
						Integer numericalInput = Integer.valueOf(input);
						if (numericalInput < MINIMUM_NUMBER_OF_HORSES_REQUIRED)
							throw new MinimumBoundedIntegerException(MINIMUM_NUMBER_OF_HORSES_REQUIRED);
						return numericalInput;
					});

				distanceToFinishLine = promptBuilder.prompt(PROMPT_DISTANCE_TO_FINISH_LINE)
					.getNextLine(input -> { 
						Integer numericalInput = Integer.valueOf(input);
						if (numericalInput < MINIMUM_DISTANCE_TO_FINISH_LINE)
							throw new MinimumBoundedIntegerException(MINIMUM_DISTANCE_TO_FINISH_LINE);
						return numericalInput;
					});

				Set<Horse> horses = new LinkedHashSet<>(numberOfHorses);

				Random random = new Random();
				for (int i = 0; i < numberOfHorses; i++) {
					horses.add(new Horse(
							promptBuilder.prompt(String.format(PROMPT_HORSE_NAME, i + 1)).getNextLine(),
							promptBuilder.prompt(String.format(PROMPT_HORSE_WAR_CRY, i + 1)).getNextLine(),
							random.nextBoolean(),
							random.nextInt(10) + 1
						)
					);
				}
				race = new HorseRaceService(horses, distanceToFinishLine, System.out);
				race.printContenders();
				race.printRaceStatus();
			}
			catch(Exception exception) {
				printException(exception);
			}
		} while(race == null || !race.canContinue());

		try {
			race.start();
		}
		catch(Exception exception) {
			printException(exception);
		}
	}

	private static void printException(Exception exception) {
		System.err.println("EXCEPTION: " + exception);
		// exception.printStackTrace();

		// Throwable cause = exception.getCause();
		// if (cause != null) {
		// 	System.err.println("INNER EXCEPTION: " + cause);
		// 	cause.printStackTrace();
		// }
	}
}