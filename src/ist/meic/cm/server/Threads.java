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
		} catch (Exception e) {
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
				try {
					terminate();
				} catch (IOException e1) {
				}
				running = false;
			} catch (IOException e) {
				try {
					terminate();
				} catch (IOException e1) {
				}
				running = false;
			} catch (Exception e) {
				try {
					terminate();
				} catch (IOException ex) {
				}
				running = false;
			}

			if (fromClient == null) {
				try {
					terminate();
				} catch (IOException e) {
				}
				running = false;
			}

			if (running) {
				int request = fromClient.getCode();
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
					try {

						terminate();
						running = false;
					} catch (IOException e) {
						running = false;
					}
				}

			}
		}

	}

	private void terminate() throws IOException {
		LinkedList<BombermanStatus> status = currentMap.getBombermansStatus();

		status.get(playerID).die();

		int i = 0, killed = 0;

		for (BombermanStatus current : status) {
			if (current.isDead())
				killed++;
			i++;
		}

		int removed = game.removePlayer();

		if (removed <= 0 || killed == i)
			synchronized (games) {

				games.remove(game);
			}

		clientSocket.close();
	}

	private void checkToStart() {

		game.setReady(playerID);

		int max = game.getMaxNumPlayers();

		Message toSend;

		if (max == 1)
			toSend = new Message(Message.FAIL);
		else {

			boolean running = true;
			while (running) {
				for (int i = 0; i < max; i++)
					running = !game.getReady()[i];

				max = game.getMaxNumPlayers();
			}

			if (playerID == 0)
				currentMap.moveGhosts();

			toSend = new Message(Message.SUCCESS);
		}
		sendToPlayer(toSend);
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
		} else if (request.equals(OperationCodes.MAP)) {
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
		synchronized (games) {
			if (!games.isEmpty()) {
				for (int i = 0; i < games.size(); i++) {
					Game game = games.get(i);
					MapController map = game.getMapController();

					if (map.getLevelName().equals(levelName)) {
						if (game.getPlayers().contains(details[1])) {
							toSend = new Message(Message.FAIL);
							found = true;
						} else {
							playerID = map.joinBomberman();
							if (playerID != -1) {
								found = true;

								currentMap = map;

								this.game = game;

								toSend = new Message(Message.SUCCESS, playerID,
										currentMap,
										addPlayer(details[1], game),
										game.getDuration());
							}
						}
						break;
					}
				}
			}
		}

		if (!found) {
			playerID = 0;
			currentMap = new MapController(levelName, details);
			currentMap.joinBomberman();
			Game game = new Game(currentMap, details[8]);
			synchronized (games) {
				games.add(game);
				this.game = games.get(games.lastIndexOf(game));
				toSend = new Message(Message.SUCCESS, playerID, currentMap,
						addPlayer(details[1], this.game));
			}
		}

		return sendToPlayer(toSend);
	}

	private ArrayList<String> addPlayer(String playerName, Game game) {
		ArrayList<String> players = game.getPlayers();
		players.add(playerName);
		game.setPlayers(players);
		return players;
	}

	private boolean sendToPlayer(Message toSend) {
		try {
			output.writeObject(toSend);
			output.reset();
		} catch (IOException e) {
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
