import java.io.*;
import java.net.Socket;

public class MyClient {
	public static void main(String args[]) {
		String serverName;
		if (args.length < 1)
			serverName = "localhost";
		else
			serverName = args[0];

		System.out.println("Clark Elliott's Inet Client.\n");
		/* printLocalAddress (); */
		System.out.println("Using server: " + serverName + ", Port: 2540");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			String name;
			do {
				System.out
						.print("Enter a message to server, (quit) to end: ");
				System.out.flush();
				name = in.readLine();
				if (name.indexOf("quit") < 0)
					printMessageToServer(name, serverName);
			} while (name.indexOf("quit") < 0);
			System.out.println("Cancelled by user request.");
		} catch (IOException x) {
			x.printStackTrace();
		}
	}

	static void printMessageToServer(String name, String serverName) {
		Socket sock;
		BufferedReader fromServer;
		PrintStream toServer;
		String textFromServer;

		try {
			/*
			 * Open our connection to server port, choose your own port number..
			 */
			sock = new Socket(serverName, 80);

			// Create filter I/O streams for the socket:
			fromServer = new BufferedReader(new InputStreamReader(
					sock.getInputStream()));
			toServer = new PrintStream(sock.getOutputStream());

			// Send machine name or IP address to server:
			toServer.println(name);
			toServer.flush();

			// Read two or three lines of response from the server,
			// and block while synchronously waiting:
			for (int i = 1; i <= 10; i++) {
				textFromServer = fromServer.readLine();
				if (textFromServer != null)
					System.out.println(textFromServer);
			}

			sock.close();
		} catch (IOException x) {
			System.out.println("Socket error.");
			x.printStackTrace();
		}
	}
}