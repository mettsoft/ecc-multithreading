package com.ecc;

import java.util.List;
import java.util.ArrayList;

import com.ecc.utility.InputHandler;
import com.ecc.model.Horse;
import com.ecc.factory.HorseFactory;
import com.ecc.service.HorseRaceService;

public class Main {
	private static final Integer MINIMUM_NUMBER_OF_HORSES_REQUIRED = 3;
	private static final Integer MINIMUM_RACE_DISTANCE = 20;

	private static final String PROMPT_NUMBER_OF_HORSES = String.format("Enter the number of horses (min of %d): ", MINIMUM_NUMBER_OF_HORSES_REQUIRED);
	private static final String PROMPT_DISTANCE_TO_FINISH_LINE = String.format("Enter the distance to finish line (min of %d): ", MINIMUM_RACE_DISTANCE);
	private static final String PROMPT_HORSE_NAME = "Enter horse-%d name: ";
	private static final String PROMPT_HORSE_WAR_CRY = "Enter horse-%d war cry: ";

	private static final Integer DISTANCE_TO_GATE = 10;

	/*
		11. Find other ways without synchronous. If there's no way, then use Thread.sleep.
	*/
	public static void main(String[] args) {
		HorseRaceService race = new HorseRaceService();
		Integer distanceToFinishLine = null;

		while(!race.hasContenders()) {
			try {
				Integer numberOfHorses = InputHandler.getNextLine(PROMPT_NUMBER_OF_HORSES, input -> InputHandler.parseInteger(input, MINIMUM_NUMBER_OF_HORSES_REQUIRED));
				List<Horse> horses = HorseFactory.getHealthyHorses(numberOfHorses);

				System.out.println(String.format("Name %d healthy horses.", horses.size()));
				for (Integer i = 0; i < horses.size(); i++) {
	 				horses.get(i).setName(InputHandler.getNextLine(String.format(PROMPT_HORSE_NAME, i + 1)));
					horses.get(i).setWarCry(InputHandler.getNextLine(String.format(PROMPT_HORSE_WAR_CRY, i + 1)));
				}

				distanceToFinishLine = InputHandler.getNextLine(PROMPT_DISTANCE_TO_FINISH_LINE, input -> InputHandler.parseInteger(input, MINIMUM_RACE_DISTANCE));
				race.acceptContenders(horses);
				race.printContenders();
			}
			catch (Exception exception) {
				System.err.println("Error: " + exception.getMessage());	
			}			
		}

		try {
			System.out.println(String.format("---- Race to gate: %d meters ----", DISTANCE_TO_GATE));
			race.start(DISTANCE_TO_GATE);

			System.out.println(String.format("-- Race to finish line: %d meters --", distanceToFinishLine));
			race.start(distanceToFinishLine);

			race.printLeaderboard();
		}
		catch(Exception exception) {
			System.err.println("Error: " + exception.getMessage());	
		}
	}
}