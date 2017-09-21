package Service;

public class SamplingService implements Runnable {

	private static final int SAMPLING_FREQUENCY_IN_MILLISECONDS = 500;
	private volatile Boolean isFinished;
	private Runnable onSample;

	public SamplingService(Runnable onSample) {
		this.isFinished = false;
		this.onSample = onSample;
	}

	public void done() {
		isFinished = true;
	}

	@Override
	public void run() {
		try {
			while (!isFinished) {
				Thread.sleep(SAMPLING_FREQUENCY_IN_MILLISECONDS);
				onSample.run();
			}			
		}
		catch(InterruptedException exception) {
			System.err.println("Exception: " + exception);
		}
	}
}