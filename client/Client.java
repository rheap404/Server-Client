
import java.net.*;
import java.io.*;

public class Client {

	private static Socket s = null;
	private static PrintWriter output = null;
	private static BufferedReader input = null;

	static void connection(String cmd) {
		try {

			// Makes a connection to the server
			s = new Socket("localhost", 9125);

			// PrintWriter sends data to server
			output = new PrintWriter(s.getOutputStream(), true);
			// BufferedReader recieves data from server
			input = new BufferedReader(new InputStreamReader(s.getInputStream()));

			// Call method for respective command
			if (cmd.equals("list")) {
				list();
			} else {
				putFile(cmd);
			}

			// Closes connection
			output.close();
			input.close();
			s.close();

		} catch (UnknownHostException e) {
			System.err.println("Error: Unknown host. Please ensure the server is running." + e);
			System.exit(1);

		} catch (IOException e) {
			System.err.println("Error: Unable to establish connection to the server." + e);
			System.exit(1);
		}

	}

	public static void putFile(String f) {
		try {

			// Send command to server
			output.println(f);

			File file = new File(f);
			String fromServer, ToServer;

			// Reads the file line by line and sends it to the server
			BufferedReader freader = new BufferedReader(new FileReader(file));

			while ((ToServer = freader.readLine()) != null) {
				output.println(ToServer);
			}
			freader.close();

			// Reads reponse from the server
			fromServer = input.readLine();

			// If response is 0 - file already exists, 1 - file succefully uploaded
			if (fromServer != null) {

				if (Integer.parseInt(fromServer) == 0)
					System.out.println("Error: Can't upload file '" + f + "'. Already exists on server.");
				else
					System.out.println("Uploaded file '" + f + "' to server.");
			}

		} catch (IOException e) {
			System.err.println("Error: Unable to read/write file or communicate with the server.");
			System.exit(1);
		}

	}

	public static void list() {

		String fromServer, ToServer;
		try {
			// Send command to server
			ToServer = "list";
			output.println(ToServer);

			// Reads response from server
			fromServer = input.readLine();
			if (fromServer != null) {

				// Parses the string sent into an array of file names
				fromServer = fromServer.substring(1, fromServer.length() - 1);
				String[] files = fromServer.split(",\\s*");
				System.out.println("Listing " + files.length + " file(s):");

				// Lists the files in serverFiles directory
				for (String fileName : files) {
					System.out.println(fileName.trim());
				}
			}

		} catch (IOException e) {
			System.err.println("Error: Unable to communicate with the server.");
			System.exit(1);
		}
	}

	public static void main(String[] args) {

		// Check if command line arguments are provided
		if (args.length == 0) {
			System.out.println("No command provided");
			System.exit(0);

		} else if (args[0].equals("list")) {
			// If command if list, call connection method
			connection(args[0]);
			System.exit(0);

		} else if (args[0].equals("put")) {

			// If command is "put", check if filename is provided
			if (args[1].length() == 0) {
				System.out.println("No filename given");
				System.exit(0);

			} else {
				// Checks is filename provided exists
				File file = new File(args[1]);
				if (!file.exists()) {
					System.out.println("Can't open file '" + args[1] + "' for reading");
					System.exit(0);
				}

				// Calls connection method
				connection(args[1]);
				System.exit(0);
			}

		} else {
			System.out.println("Invalid command");
			System.exit(0);
		}

	}

}