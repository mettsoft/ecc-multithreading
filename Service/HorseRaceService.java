package Service;

import java.io.PrintStream;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorService;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Consumer;
import java.util.Optional;
import java.util.NoSuchElementException;

import Model.Horse;

public class HorseRaceService {

	private static final Integer MINIMUM_HEALTHY_HORSES_TO_CONTINUE = 2;
	private static final Integer DISTANCE_TO_GATE = 10;
	private static final Integer TIME_SLICE_IN_MILLISECONDS = 100;

	private Set<Horse> horses;
	private Set<Horse> healthyHorses;
	private Integer distanceToFinishLine;
	private PrintStream printStream;
	private ExecutorService executorService;
	private CompletionService<Horse> completionService;
	private CyclicBarrier barrier;
	private Double startTimeStamp;
	private SamplingService samplingService;
	private Thread samplingThread;

	public HorseRaceService(Set<Horse> horses, Integer distanceToFinishLine, PrintStream printStream) {
		this.horses = horses;
		this.healthyHorses = horses.stream()
			.filter(horse -> horse.getIsHealthy())
			.collect(Collectors.toSet());
		this.distanceToFinishLine = distanceToFinishLine;
		this.printStream = printStream;
	}

	public Boolean canContinue() {
		return this.healthyHorses.size() >= MINIMUM_HEALTHY_HORSES_TO_CONTINUE;
	}

	public void start() throws InterruptedException, ExecutionException  {

		barrier = new CyclicBarrier(healthyHorses.size(), () -> {
				printStream.println(String.format("------- Race starts now! -------"));
				startTimeStamp = System.nanoTime() * 1e-9;
				samplingService = new SamplingService(() -> {
					try {
						healthyHorses.stream().forEach(horse -> horse.setIsLastHorse(false));
						healthyHorses.stream()
							.filter(horse -> horse.getGoalDistance() > 0 && horse.getDistance() > 0)
							.min((horse, otherHorse) -> horse.getDistance().compareTo(otherHorse.getDistance()))
							.get().setIsLastHorse(true);					
					}
					catch(NoSuchElementException exception){

					}
					finally {
						this.printStatistics();
					}
				});
				samplingThread = new Thread(samplingService);
				samplingThread.start();
			});

		executorService = Executors.newFixedThreadPool(healthyHorses.size());
		completionService = new ExecutorCompletionService<>(executorService);

		printStream.println(String.format("------- Race to gate: %d meters -------", DISTANCE_TO_GATE));
		race(
			DISTANCE_TO_GATE,
			horse -> printStream.println(String.format("Horse: %s is ready at the barn!", horse.getName())),
			horse -> {},
			horse -> printStream.println(String.format("Horse: %s has arrived at the gate!", horse.getName()))
		);

		printStream.println(String.format("------- Race to finish line: %d meters -------", distanceToFinishLine));
		race(
			distanceToFinishLine,
			horse -> printStream.println(String.format("Horse: %s is ready at the gate!", horse.getName())),
			horse -> {
				if (horse.getIsLastHorse())
					horse.setSpeed(getRandomSpeed(11, 20));
				else 
					horse.setSpeed(getRandomSpeed(1, 10));
			},
			horse -> printStream.println(String.format("Horse: %s shouts %s!", horse.getName(), horse.getWarCry()))
		);
		executorService.shutdown();
	}

	private void race(Integer raceDistance, Consumer<Horse> onReady, Consumer<Horse> onAfterMove, Consumer<Horse> onGoal) throws InterruptedException, ExecutionException {

		for (final Horse horse : healthyHorses) {
			horse.setGoalDistance(raceDistance + 0d);
			completionService.submit(() -> {
				onReady.accept(horse);
				barrier.await();

				while (horse.getDistance() < raceDistance) {
					Long startTime = System.nanoTime();
					Thread.sleep(TIME_SLICE_IN_MILLISECONDS);
					Double deltaTime = (System.nanoTime() - startTime) * 1e-9;
					horse.setDistance(horse.getDistance() + deltaTime * horse.getSpeed());
					onAfterMove.accept(horse);
				}
				return horse;
			});			
		}

		for (int i = 0; i < healthyHorses.size(); i++) {
			Future<Horse> future = completionService.take();
			Horse horse = future.get();
			horse.setDistance(0d);
			horse.setGoalDistance(0d);
			onGoal.accept(horse);
		}

		samplingService.done();
		samplingThread.join();
	}

	private static Integer getRandomSpeed(Integer lowerBound, Integer upperBound) {
		return new Random().nextInt(upperBound - lowerBound + 1) + lowerBound;
	}

	public void printContenders() {
		printStream.println("------- Contenders -------");
		horses.stream().forEach(printStream::println);
	}

	public void printStatistics() {
		printStream.println("------- Statistics -------");
		printStream.println(String.format("Elapsed Time: %.4f", System.nanoTime() * 1e-9 - startTimeStamp));
		healthyHorses.stream().forEach(printStream::println);
	}

	public void printRaceStatus() {
		if (healthyHorses.size() >= MINIMUM_HEALTHY_HORSES_TO_CONTINUE)
			printStream.println(String.format("Number of healthy horses: %d\nRace can continue!", healthyHorses.size()));
		else
			printStream.println(String.format("Number of healthy horses: %d\nRace cannot continue!", healthyHorses.size()));
	}
}