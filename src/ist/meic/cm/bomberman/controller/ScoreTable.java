package ist.meic.cm.bomberman.controller;

import java.io.Serializable;
import java.util.HashMap;

public class ScoreTable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6020673959591592054L;
	private HashMap<Integer, Integer> scoreMap;
	private int pointsRobot;
	private int pointsOpon;

	public ScoreTable(String pointsRobot, String pointsOpon) {
		this.scoreMap = new HashMap<Integer, Integer>();

		this.pointsRobot = Integer.parseInt(pointsRobot);
		this.pointsOpon = Integer.parseInt(pointsOpon);
	}

	public void addPlayer(int playerId) {
		scoreMap.put(playerId, 0);
	}

	public int getScore(int playerId) {
		if (scoreMap.containsKey(playerId))
			return scoreMap.get(playerId);
		return 0;
	}

	public void killedGhost(int playerId) {
		int oldscore;

		if (scoreMap.containsKey(playerId)) {
			oldscore = scoreMap.get(playerId);
			scoreMap.put(playerId, oldscore + pointsRobot);
		}
	}

	public void killedBomberman(int playerId) {
		int oldscore;

		if (scoreMap.containsKey(playerId)) {
			oldscore = scoreMap.get(playerId);
			scoreMap.put(playerId, oldscore + pointsOpon);
		}
	}

}
