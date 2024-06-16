# Server-Client
This project implements client and server applications for a simple file transfer system that allows clients to request a list of text files contained on the server and to upload new files to the server. The system is somewhat similar to a basic FTP application but is restricted to text files and file upload only.

## Server Requirements

- Run continuously.
- Use an Executor to manage a fixed thread-pool with 20 connections.
- Query the local folder `serverFiles` and return a list of the files found there to the client upon request.
- Receive a request from a client to upload a new file to `serverFiles`.
- If a file with the same name already exists, return an error to the client; otherwise transfer the file from the client and save it to `serverFiles`.
- Create a `log.txt` file in the server directory and log every valid client request in the format: `date|time|client IP address|request`.

## Client Requirements

- Accepts one of the following commands as command line arguments:
  - `list`: Lists all files in the server's `serverFiles` folder.
  - `put fname`: Uploads the file `fname` to the server.
- Exits after completing each command.
- Handles errors gracefully, providing meaningful messages for invalid inputs or actions.

## Compilation and Execution

### Server

1. Navigate to the server directory and compile the server:
   ```sh
   cd cwk/server
   javac Server.java
   
2. Launch the server:
   ```sh
   java Server

### Client

1. In another terminal tab or shell, navigate to the client directory and compile the client:
   ```sh
   cd cwk/client
   javac Client.java

2. Execute the client with the desired command:

  - To list files:
    ```sh
    java Client list
  
  - To upload a file:
    ```sh
    java Client put lipsum2.txt

