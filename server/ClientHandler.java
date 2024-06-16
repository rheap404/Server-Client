import java.net.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.text.SimpleDateFormat;

public class ClientHandler extends Thread {
    private Socket socket = null;

    // Constructor to initialize ClientHandler with the client's socket
    public ClientHandler(Socket socket) {
        super("ClientHandler");
        this.socket = socket;
    }

    public void run() {
        try {

            // Create input and output streams for client communication
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String fromClient;
            String[] toClient;

            try {
                // Read command from client
                fromClient = input.readLine();

                // Get current directory and set user directory to serverFiles path
                String userDirectory = System.getProperty("user.dir");
                userDirectory += "/serverFiles/";

                // Get files in serverFiles directory in an array
                Set<String> stringSet = Stream.of(new File(userDirectory).listFiles())
                        .filter(file -> !file.isDirectory())
                        .map(File::getName)
                        .collect(Collectors.toSet());

                // Convert the set of file names to an array
                toClient = stringSet.toArray(new String[0]);

                // Send the list of files on "list" cmd
                if (fromClient.equals("list")) {
                    // Call the function to add command to log file
                    log("list");
                    output.println(Arrays.toString(toClient));

                } else {

                    // Call the function to add command to log file
                    log("put");

                    // Check if file exists in serverFiles directory
                    String fileName = fromClient;
                    for (String element : toClient) {
                        if (element.equals(fileName)) {
                            // File already exists in severFiles
                            output.println("0");
                        }
                    }

                    // If file does not exist, create a stream to read in the file
                    // line by line from client
                    fileName = userDirectory + fileName;

                    BufferedWriter fwriter = new BufferedWriter(new FileWriter(fileName));
                    String line;
                    while (input.ready() && (line = input.readLine()) != null) {
                        fwriter.write(line + "\n");
                        fwriter.flush();
                    }
                    fwriter.close();

                    // File transfer was sucessful
                    output.println("1");

                }

                // Close streams and socket
                output.close();
                input.close();
                socket.close();
            } catch (IOException e) {
                System.err.println("Error reading from or writing to client: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.err.println("Error accepting client connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void log(String cmd) {

        String file = "log.txt";

        try {
            // Append given command to the log.txt file
            FileWriter writer = new FileWriter(file, true);
            String header;

            // Get clients IP address and current date/time
            InetAddress address = socket.getInetAddress();
            String IP = address.getHostAddress();
            Date currentDate = new Date();

            // Format the date and time
            SimpleDateFormat dateF = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeF = new SimpleDateFormat("HH:mm:ss");
            String date = dateF.format(currentDate);
            String time = timeF.format(currentDate);

            // Create log entry with date, time, IP address, and command
            header = date + " | " + time + " | " + IP + " | " + cmd + "\n";

            // Write to log.txt and then close the file
            writer.write(header);
            writer.close();

        } catch (IOException e) {
            System.err.println(e);
        }
    }

}
