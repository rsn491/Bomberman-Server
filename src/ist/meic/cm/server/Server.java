package ist.meic.cm.server;

import ist.meic.cm.bomberman.controller.MapController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;

public class Server {
	private static ServerSocket serverSocket;
	private static final int PORT = 4444;

	private static ExecutorService threadPool = Executors.newCachedThreadPool();

	public static void main(String[] args) {

		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			System.out.println("Could not listen on port: 4444");
		}

		System.out.println("Server started. Listening to the port 4444");

		ArrayList<Game> games = new ArrayList<Game>();

		boolean running = true;

		while (running) {
			Socket clientSocket = null;

			try {
				clientSocket = serverSocket.accept();
			} catch (IOException e) {
				running = false;
				e.printStackTrace();
			}

			System.out.println("\nUm jogador ligou-se!");

			threadPool.execute(new Threads(clientSocket, games));
		}

		threadPool.shutdown();
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}