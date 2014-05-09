import java.io.*;
import java.net.Socket;


public class MyTelnet {
	public static void main(String args[]) {
		String serverName;
		int port = 0;	
		if (args.length < 1)
			serverName = "localhost";
		else{
			serverName = args[0];
			port = Integer.parseInt(args [1]);
			}

		System.out.println("Tu's MyClient.\n");
		/* printLocalAddress (); */
		System.out.println("Using server: " + serverName + ", Port:" + port);
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			String name;
			String host = null;
			do {
				System.out.print("Enter a message to server, (quit) to end: ");
				System.out.flush();
				//System.out.flush();
				name = in.readLine();
				host = in.readLine();
				if (name.indexOf("quit") < 0)
					printMessageToServer(name, serverName , port , host);
			} while (name.indexOf("quit") < 0);
			System.out.println("Cancelled by user request.");
		} catch (IOException x) {
			x.printStackTrace();
		}
	}

	static void printMessageToServer(String name, String serverName , int port , String host) {
		Socket sock;
		BufferedReader fromServer;
		PrintStream toServer;
		String textFromServer;

		String outputData =
				  ""+ name + "\r\n" +		  
				  "Host: " + serverName + "\r\n" +
				  "Connection: keep-alive" + "\r\n" +
				  "\r\n\r\n";

		try {
			/*
			 * Open our connection to server port, choose your own port number..
			 */
			sock = new Socket(serverName, port);

			// Create filter I/O streams for the socket:
			fromServer = new BufferedReader(new InputStreamReader(
					sock.getInputStream()));
			toServer = new PrintStream(sock.getOutputStream());

			toServer.println(outputData);
			toServer.flush();

			// Read two or three lines of response from the server,
			// and block while synchronously waiting:
			boolean streamEnd = false;
			while (streamEnd ==false) {
				textFromServer = fromServer.readLine();
				if (textFromServer != null){
					System.out.println(textFromServer);
					}
				else
					streamEnd = true;
				
			}

			sock.close();
		} catch (IOException x) {
			System.out.println("Socket error.");
			x.printStackTrace();
		}
	}
}