package ist.meic.cm.server;

import java.util.ArrayList;

import ist.meic.cm.bomberman.controller.MapController;
import ist.meic.cm.bomberman.controller.OperationCodes;

public class Game {

	private static final long INTERVAL = 1000;
	private MapController mapController;
	private ArrayList<String> players;
	private boolean[] ready;
	private int maxNumPlayers;
	private int duration;
	private Thread timer;
	private int tmp;

	public Game(MapController mapController, String duration) {
		this.mapController = mapController;
		players = new ArrayList<String>();
		ready = new boolean[4];
		this.duration = Integer.parseInt(duration);
		this.tmp = this.duration;
		timer = null;
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

	public void timerThread() {

		Runnable runnable = new Runnable() {
			boolean running = true;

			public void run() {
				while (running) {
					try {
						Thread.sleep(INTERVAL);
					} catch (InterruptedException e) {

					}

					if (running)
						tmp--;

					if (tmp <= 0)
						running = false;
				}
			}
		};
		timer = new Thread(runnable);
		timer.start();
	}

	public Thread getTimer() {
		return timer;
	}

	public int getTmp() {

		return tmp;
	}
}
