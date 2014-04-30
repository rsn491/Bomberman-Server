package ist.meic.cm.server;

import java.util.ArrayList;

import ist.meic.cm.bomberman.controller.MapController;

public class Game {

	private MapController mapController;
	private ArrayList<String> players;

	public Game(MapController mapController) {
		this.mapController = mapController;
		players = new ArrayList<String>();
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
	}

}
