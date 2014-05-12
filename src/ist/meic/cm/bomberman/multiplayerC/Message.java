package ist.meic.cm.bomberman.multiplayerC;

import ist.meic.cm.bomberman.controller.MapController;
import ist.meic.cm.bomberman.controller.OperationCodes;
import ist.meic.cm.bomberman.status.BombermanStatus;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {

	private static final long serialVersionUID = -833977691375894906L;

	public static final int JOIN = 20;
	public static final int REQUEST = 10;
	public static final int SUCCESS = 1;
	public static final int FAIL = 0;

	public static final int END = 100;

	public static final int REFRESH = 15;

	public static final int READY = 25;

	private int code;
	private String details;

	private MapController gameMap;

	private OperationCodes operationCode;

	private int playerID;

	private BombermanStatus bombermanStatus;

	private ArrayList<String> players;

	private int duration;

	private String prefs;

	public Message() {
		this(Message.SUCCESS);
	}

	public Message(int code) {
		this.code = code;
	}

	public Message(int code, OperationCodes operationCode) {
		this.code = code;
		this.operationCode = operationCode;
	}

	public Message(int code, String details) {
		this.code = code;
		this.details = details;
	}

	public Message(int code, MapController gameMap) {
		this.code = code;
		this.gameMap = gameMap;
	}

	public Message(int code, int playerID, MapController currentMap) {
		this(code, currentMap);
		this.playerID = playerID;
	}

	public Message(int code, OperationCodes operationCode,
			BombermanStatus bombermanStatus) {
		this(code, operationCode);
		this.bombermanStatus = bombermanStatus;
	}

	public Message(int code, int playerID, MapController currentMap,
			ArrayList<String> players) {
		this(code, playerID, currentMap);
		this.players = players;
	}

	public Message(int code, ArrayList<String> players) {
		this(code);
		this.players = players;
	}

	public Message(int code, int playerID, MapController currentMap,
			ArrayList<String> players, int duration) {
		this(code, playerID, currentMap, players);
		this.duration = duration;
	}

	public Message(int code, int playerID, MapController currentMap,
			ArrayList<String> players, String prefs) {
		this(code, playerID, currentMap, players);
		this.prefs = prefs;
	}

	public Message(int code, int playerID) {
		this(code);
		this.playerID = playerID;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getSimpleDetails() {
		return details;
	}

	public String[] getDetails() {
		return details.split("\\s*[ ]\\s*");
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public MapController getGameMap() {
		return gameMap;
	}

	public void setGameMap(MapController gameMap) {
		this.gameMap = gameMap;
	}

	public OperationCodes getRequest() {

		return operationCode;
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	public BombermanStatus getBombermanStatus() {
		return bombermanStatus;
	}

	public void setBombermanStatus(BombermanStatus bombermanStatus) {
		this.bombermanStatus = bombermanStatus;
	}

	public ArrayList<String> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<String> players) {
		this.players = players;
	}

	public int getDuration() {

		return duration;
	}

	public String getPrefs() {
		return prefs;
	}

}
