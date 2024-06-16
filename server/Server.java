import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server {

	private ServerSocket server = null;
	private ExecutorService service = null;

	public Server() {
		try {
			// Initialize the server socket on port 9125
			server = new ServerSocket(9125);

		} catch (IOException e) {
			System.err.println("Could not listen on port: 0.");
			System.exit(1);
		}
		// Create a thread pool of size 20
		service = Executors.newFixedThreadPool(20);
	}

	public void runServer() {
		// Create a log file
		createLogFile();

		Socket client = null;
		while (true) {

			try {
				// Accept incoming client connections
				client = server.accept();
			} catch (IOException e) {
				System.err.println("Accept failed.");
				System.exit(1);
			}
			// Submit client request to ClientHandler
			service.submit(new ClientHandler(client));

		}
	}

	private void createLogFile() {

		String file = "log.txt";
		File log = new File(file);
		try {

			// If log file exists, delete it and create a new empty one
			if (log.exists()) {
				log.delete();
				log.createNewFile();
				FileWriter fileW = new FileWriter(log, false);
				fileW.close();

			} else {
				// Create a new log file if it doesn't exist
				log.createNewFile();
			}

		} catch (IOException e) {
			System.err.println("Error creating log file: " + e.getMessage());
		}
	}

	public static void main(String[] args) {

		// Instantiate the server and start running it
		Server s = new Server();
		s.runServer();

	}
}
