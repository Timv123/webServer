import java.io.*; // Get the Input Output libraries 
import java.net.*;

// Get the Java networking libraries 

class Worker extends Thread { // Class definition
	Socket sock; // Class member, socket, local to Worker.

	Worker(Socket s) {
		sock = s;
	} // Constructor, assign arg s to local sock

	public void run() {
		// Get I/O streams from the socket:
		PrintStream out = null;
		BufferedReader in = null;
		try {
			out = new PrintStream(sock.getOutputStream());
			in = new BufferedReader(
					new InputStreamReader(sock.getInputStream()));
			// Note that it is remotely possible that this branch will not
			// execute:
			if (MyListener.controlSwitch != true) {
				System.out
						.println("Listener is now shutting down as per client request.");
				out.println("Server is now shutting down. Goodbye!");
			} else
				try {
					String name;
					name = in.readLine();
					if (name.indexOf("shutdown") > -1) {
						MyListener.controlSwitch = false;
						System.out.println("Worker has captured a shutdown request.");
						out.println("Shutdown request has been noted by worker.");
						out.println("Please send final shutdown request to listener.");
					} else {
						
						for (int i = 0; i <= name.length(); i++) {
							
							if (name != null){
								out.println(name);
								System.out.println(name);
								}
							name = in.readLine();
						}
						//printMessageToClient(name, out);
						sock.close();
					}
				} catch (IOException x) {
					System.out.println("Server read error");
					x.printStackTrace();
				}

			sock.close(); // close this connection, but not the server;
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}

	static void printMessageToClient(String name, PrintStream out) {
		out.println(name);
	
	}
	
	
}

public class MyListener {

	public static boolean controlSwitch = true;
	

	public static void main(String a[]) throws IOException {
		int q_len = 6; /* Number of requests for OpSys to queue */
		int port = 2540;
		Socket sock;

		ServerSocket servsock = new ServerSocket(port, q_len);
		System.out.println("Tu's server starting up, listening at port .\n");
		while (controlSwitch) {
			// wait for the next client connection:
			sock = servsock.accept();
			new Worker(sock).start(); // Uncomment to see shutdown bug:
			// try{Thread.sleep(10000);} catch(InterruptedException ex) {}
		}
		
	}
}