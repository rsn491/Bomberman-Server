package ist.meic.cm.server;

import ist.meic.cm.bomberman.controller.MapController;
import ist.meic.cm.bomberman.controller.OperationCodes;
import ist.meic.cm.bomberman.multiplayerC.Message;
import ist.meic.cm.bomberman.status.BombermanStatus;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class Threads implements Runnable {

	private ObjectOutputStream output;
	private ObjectInputStream input;
	private int playerID;
	private ArrayList<Game> games;
	private MapController currentMap;
	private Socket clientSocket;
	private Game game;

	Threads(Socket clientSocket, ArrayList<Game> games) {

		this.clientSocket = clientSocket;

		this.games = games;

		try {
			this.output = new ObjectOutputStream(clientSocket.getOutputStream());

			this.input = new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {

		boolean running = true;
		Message fromClient = null;

		while (running) {
			try {
				fromClient = (Message) input.readObject();
			} catch (ClassNotFoundException e) {
				running = false;
				e.printStackTrace();
			} catch (IOException e) {
				running = false;
				e.printStackTrace();
			}

			if (fromClient == null)
				running = false;

			int request = fromClient.getCode();

			if (running)
				if (request == Message.JOIN) {
					if (!join(fromClient))
						running = false;
				} else if (request == Message.REFRESH) {
					sendUpdatedList();
				} else if (request == Message.READY) {
					checkToStart();
				} else if (request == Message.REQUEST) {
					if (!request(fromClient))
						running = false;
				} else if (request == Message.END) {
					// TO DO
					try {
						// TO DO
						clientSocket.close();
						running = false;
					} catch (IOException e) {
						running = false;
						e.printStackTrace();
					}
				}

		}

	}

	private void checkToStart() {
System.out.println(playerID);
		game.setReady(playerID);

		int max = game.getMaxNumPlayers();

		if (max == 1)
			sendToPlayer(new Message(Message.FAIL));
		else {

			boolean running = true;
			while (running) {
				for (int i = 0; i < max; i++)
					running = !game.getReady()[i];

				max = game.getMaxNumPlayers();
			}
			sendToPlayer(new Message(Message.SUCCESS));
		}
	}

	private void sendUpdatedList() {
		sendToPlayer(new Message(Message.SUCCESS, game.getPlayers()));

	}

	private boolean request(Message fromClient) {

		OperationCodes request = fromClient.getRequest();

		if (request.equals(OperationCodes.BOMB))
			currentMap.newBomb(playerID);
		else if (request.equals(OperationCodes.MOVE)) {
			LinkedList<BombermanStatus> bombermansStatus = currentMap
					.getBombermansStatus();
			bombermansStatus.set(playerID, fromClient.getBombermanStatus());
			currentMap.setBombermansStatus(bombermansStatus);
		} /*
		 * else if (!request.equals(OperationCodes.MAP))
		 * currentMap.bombermanMove(playerID, request);
		 */
		else if (request.equals(OperationCodes.MAP)) {
			System.out.println("Got Request");

			Message toSend = new Message(Message.SUCCESS, currentMap);

			return sendToPlayer(toSend);
		}
		return true;
	}

	private boolean join(Message fromClient) {

		String[] details = fromClient.getDetails();
		String levelName = details[0];
		Message toSend = null;
		boolean found = false;
		if (!games.isEmpty()) {
			for (int i = 0; i < games.size(); i++) {
				Game game = games.get(i);
				MapController map = game.getMapController();

				if (map.getLevelName().equals(levelName)) {

					playerID = map.joinBomberman();
					if (playerID != -1) {
						found = true;

						currentMap = map;

						this.game = game;

						toSend = new Message(Message.SUCCESS, playerID,
								currentMap, addPlayer(details[1], game));
					}
					break;
				}
			}
		}

		if (!found) {
			currentMap = new MapController(levelName, details[2]);
			currentMap.joinBomberman();
			Game game = new Game(currentMap);
			games.add(game);
			this.game = games.get(games.indexOf(game));
			toSend = new Message(Message.SUCCESS, playerID, currentMap,
					addPlayer(details[1], game));
		}

		return sendToPlayer(toSend);
	}

	private ArrayList<String> addPlayer(String playerName, Game game) {
		ArrayList<String> players = game.getPlayers();
		players.add(playerName);
		game.setPlayers(players);
		System.out.println(players.size());
		return players;
	}

	private boolean sendToPlayer(Message toSend) {
		try {
			output.writeObject(toSend);
			output.reset();
		} catch (IOException e) {
			e.printStackTrace();
			return false;

		}
		return true;
	}

	int getPlayerID() {
		return playerID;
	}

	void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

}
