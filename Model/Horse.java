package Model;

public class Horse {
	String name;
	String warCry;
	Boolean isHealthy;
	Integer speed;
	Double distance;
	Double goalDistance;
	Boolean isLastHorse;

	public Horse(String name, String warCry, Boolean isHealthy, Integer speed) {
		this.name = name;
		this.warCry = warCry;
		this.isHealthy = isHealthy;
		this.speed = speed;
		this.distance = 0d;
		this.goalDistance = 0d;
		this.isLastHorse = false;
	}

	public String getName() {
		return name;
	}

	public String getWarCry() {
		return warCry;
	}

	public boolean getIsHealthy() {
		return isHealthy;
	}

	public Integer getSpeed() {
		return speed;
	}

	public Double getDistance() {
		return distance;
	}

	public Double getGoalDistance() {
		return goalDistance;
	}

	public Boolean getIsLastHorse() {
		return isLastHorse;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setWarCry(String warCry) {
		this.warCry = warCry;
	}

	public void setIsHealthy(Boolean isHealthy) {
		this.isHealthy = isHealthy;
	}

	public void setSpeed(Integer speed) {
		this.speed = speed;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public void setGoalDistance(Double goalDistance) {
		this.goalDistance = goalDistance;
	}

	public void setIsLastHorse(Boolean isLastHorse) {
		this.isLastHorse = isLastHorse;
	}

	@Override 
	public boolean equals(Object object) {
		Horse otherHorse = (Horse) object;
		return otherHorse != null && otherHorse instanceof Horse && otherHorse.name.equals(name) && 
			otherHorse.warCry.equals(warCry) && otherHorse.isHealthy.equals(isHealthy);
	}

	@Override
	public int hashCode() {
		return name.hashCode() * 100 + warCry.hashCode() * 10 + isHealthy.hashCode();
	}

	@Override 
	public String toString() {
		return String.format("\nHorse: %s\nWar Cry: %s\nHealth: %s\nSpeed: %d\nDistance: %.4f\nRemaining Distance to goal: %.4f", 
			name, warCry, isHealthy? "Healthy": "Sick", speed, distance, goalDistance - distance);
	}
}