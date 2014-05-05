package ist.meic.cm.server;

import java.util.ArrayList;

import ist.meic.cm.bomberman.controller.MapController;

public class Game {

	private MapController mapController;
	private ArrayList<String> players;
	private boolean[] ready;
	private int maxNumPlayers;
	private int duration;

	public Game(MapController mapController, String duration) {
		this.mapController = mapController;
		players = new ArrayList<String>();
		ready = new boolean[3];
		this.duration=Integer.parseInt(duration);
	}

	public void setReady(int playerId) {
		ready[playerId] = true;
	}

	public MapController getMapController() {
		return mapController;
	}

	public void setMapController(MapController mapController) {
		this.mapController = mapController;
	}

	public ArrayList<String> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<String> players) {
		this.players = players;
		maxNumPlayers++;
	}

	public int getMaxNumPlayers() {
		return maxNumPlayers;
	}

	public boolean[] getReady() {
		return ready;
	}

	public int removePlayer() {
		return --maxNumPlayers;
	}

	public int getDuration() {
		return duration;
	}
}
