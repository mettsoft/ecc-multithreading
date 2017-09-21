package com.ecc.service;

import java.util.concurrent.CyclicBarrier;
import java.util.function.Function;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.concurrent.BrokenBarrierException;

import com.ecc.model.Horse;
import com.ecc.utility.RandomGenerator;

public class HorseRaceService {

	private static final Integer COUNT_DOWN_FROM = 3;
	private static final Integer COUNT_DOWN_TO = 1;
	private static final Integer COUNT_DOWN_INTERVAL_IN_MILLISECONDS = 1000;

	private List<Horse> horses;
	private List<Horse> leaderboard;
	private CyclicBarrier barrier;
	private double startTime;

	public void acceptContenders(List<Horse> horses) {
		this.horses = horses;
		this.leaderboard = new ArrayList<Horse>(horses.size());
		this.barrier = new CyclicBarrier(horses.size(), this::countDown);
	}

	private void countDown() {
		System.out.println("------- Race starts in -------");
		for (Integer i = COUNT_DOWN_FROM; i >= COUNT_DOWN_TO; i--) {
			System.out.println(String.format("------- %d -------", i));
			try {
				Thread.sleep(COUNT_DOWN_INTERVAL_IN_MILLISECONDS);			
			}
			catch(InterruptedException exception) {
				throw new RuntimeException(exception);
			}
		}
		System.out.println("------- Go!!! -------");
		startTime = System.nanoTime();
	}

	public void printContenders() {
		System.out.println("------- Contenders -------");
		horses.stream().forEach(horse ->
			System.out.println(String.format("Horse: %s\nSpeed: %d\n", horse.getName(), horse.getSpeed())));
	}

	public Boolean hasContenders() {
		return horses != null;
	}

	public void start(Integer raceDistance) throws InterruptedException {
		List<Thread> threads = new ArrayList<Thread>(horses.size());
		leaderboard.clear();

		for (Integer i = 0; i < horses.size(); i++) {
			Horse horse = horses.get(i);
			horse.setDistance(0);
			horse.setGoal(raceDistance);
			Thread thread = new Thread(() -> race(horse));
			threads.add(thread);
			thread.start();
		}

		for (Thread thread : threads) {
			thread.join();			
		}
	}

	private void race(Horse horse) {
		try {		
			System.out.println(String.format("Horse: %s is ready to run!", horse.getName()));
			barrier.await();
			
			while (horse.getDistance() < horse.getGoal()) {
				move(horse);
				Thread.sleep(100);	
			}
		}
		catch(Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	private void move(Horse horse) {
		assignNewSpeed(horse);
		horse.setDistance(horse.getDistance() + horse.getSpeed());
		
		StringBuilder stringBuilder = new StringBuilder(generateStatistics(horse));	
		if (horse.getDistance() == horse.getGoal()) {
			stringBuilder.append(String.format("Horse: %s shouts %s!", horse.getName(), horse.getWarCry()));			
			leaderboard.add(horse);
		}
		System.out.println(stringBuilder);
	}

	private void assignNewSpeed(Horse horse) {
		if (isLastHorse(horse)) {
			horse.setSpeed(RandomGenerator.getInteger(11, 20));
		}
		else {
			horse.setSpeed(RandomGenerator.getInteger(1, 10));
		}
	}

	private String generateStatistics(Horse horse) {
		return String.format("------- Timestamp: %s ns -------\n%s", (System.nanoTime() - startTime), 
			horse);
	}

	private Boolean isLastHorse(Horse horse) {
		List<Horse> lastHorses = horses.stream()
			.filter(selectedHorse -> selectedHorse.getDistance() < selectedHorse.getGoal())
			.sorted((firstHorse, secondHorse) -> firstHorse.getDistance().compareTo(secondHorse.getDistance()))
			.limit(2)
			.collect(Collectors.toList());

		return lastHorses.size() == 2 && lastHorses.get(0) == horse && 
			lastHorses.get(0).getDistance() < lastHorses.get(1).getDistance();
	}

	public void printLeaderboard() {
		System.out.println("\n------ Leaderboard ------");
		for (Integer i = 0; i < leaderboard.size(); i++) {
			System.out.println(String.format("Place-%d: Horse %s!", i + 1, leaderboard.get(i).getName()));
		}
	}
}