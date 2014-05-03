package ist.meic.cm.server;

import java.util.ArrayList;

import ist.meic.cm.bomberman.controller.MapController;

public class Game {

	private MapController mapController;
	private ArrayList<String> players;
	private boolean[] ready;
	private int maxNumPlayers;

	public Game(MapController mapController) {
		this.mapController = mapController;
		players = new ArrayList<String>();
		ready = new boolean[3];
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
}
