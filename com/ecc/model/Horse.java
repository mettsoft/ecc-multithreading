package com.ecc.model;

public class Horse {
	private String name;
	private String warCry;
	private Boolean isHealthy;
	private Integer speed;
	private Integer distance;
	private Integer goal;

	public Horse() {
		name = "";
		warCry = "";
		isHealthy = true;
		speed = 0;
		distance = 0;
		goal = 0;
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

	public Integer getDistance() {
		return distance;
	}

	public Integer getGoal() {
		return goal;
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

	public void setDistance(Integer distance) {
		this.distance = goal - distance < 0? goal: distance;
	}

	public void setGoal(Integer goal) {
		this.goal = goal;
	}

	@Override 
	public String toString() {
		return String.format("Horse: %s\nSpeed: %d\nDistance Travelled: %d\nRemaining Distance to goal: %d\n",
			name, speed, distance, goal - distance);
	}
}