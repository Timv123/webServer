import java.io.*; // Get the Input Output libraries 
import java.net.*;
import java.util.Date;

// Get the Java networking libraries 

class webWorker extends Thread { // Class definition
	Socket sock; // Class member, socket, local to webwebwebWorker.

	webWorker(Socket s) {
		sock = s;
	} // Constructor, assign arg s to local sock

	public void run() {
		// Get I/O streams from the socket:
		PrintStream out = null;
		BufferedReader in = null;
		try {
			out = new PrintStream(sock.getOutputStream());
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			PrintStream pout = new PrintStream(out);
			
			// read first line of request (ignore the rest)
			String request = in.readLine();
			
			// parse the line
			if (!request.startsWith("GET")
					|| request.length() < 14
					|| !(request.endsWith("HTTP/1.0") || request
							.endsWith("HTTP/1.1"))) {
				// bad request
				errorReport(pout, sock, "400", "Bad Request",
						"Your browser sent a request that "
								+ "this server could not understand.");
			} else {
				String req = request.substring(4, request.length() - 9)
						.trim();
				if (req.indexOf("..") != -1 || req.indexOf("/.ht") != -1
						|| req.endsWith("~")) {
					// evil hacker trying to read non-wwwhome or secret file
					errorReport(pout, sock, "403", "Forbidden",
							"You don't have permission to access the requested URL.");
				} 
			}
		
		} catch (IOException ioe) {
			System.out.println(ioe);
		}

	}

	private static void log(Socket sock, String msg) {
		System.err.println(new Date() + " ["
				+ sock.getInetAddress().getHostAddress() + ":" + sock.getPort()
				+ "] " + msg);
	}

	static void printMessageToClient(String name, PrintStream out) {
		out.println(name);

	}

	private static String guessContentType(String path) {
		if (path.endsWith(".html") || path.endsWith(".htm"))
			return "text/html";
		else if (path.endsWith(".txt") || path.endsWith(".java"))
			return "text/plain";
		else if (path.endsWith(".gif"))
			return "image/gif";
		else if (path.endsWith(".class"))
			return "application/octet-stream";
		else if (path.endsWith(".jpg") || path.endsWith(".jpeg"))
			return "image/jpeg";
		else
			return "text/plain";
	}

	private static void sendFile(InputStream file, OutputStream out) {
		try {
			byte[] buffer = new byte[1000];
			while (file.available() > 0)
				out.write(buffer, 0, file.read(buffer));
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	private static void errorReport(PrintStream pout, Socket sock, String code,
			String title, String msg) {
		pout.print("HTTP/1.0 " + code + " " + title + "\r\n" + "\r\n"
				+ "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\r\n"
				+ "<TITLE>" + code + " " + title + "</TITLE>\r\n"
				+ "</HEAD><BODY>\r\n" + "<H1>" + title + "</H1>\r\n" + msg
				+ "<P>\r\n" + "<HR><ADDRESS>FileServer 1.0 at "
				+ sock.getLocalAddress().getHostName() + " Port "
				+ sock.getLocalPort() + "</ADDRESS>\r\n" + "</BODY></HTML>\r\n");
		log(sock, code + " " + title);
	}

}

public class MyWebServer {

	public static boolean controlSwitch = true;

	public static void main(String args[]) throws IOException {
		int q_len = 6; /* Number of requests for OpSys to queue */
		int port = 2550;
		Socket sock;

		ServerSocket servsock = new ServerSocket(port, q_len);
		System.out.println("Tu's server starting up, listening at port .\n");
		while (controlSwitch) {
			// wait for the next client sock:
			sock = servsock.accept();
			new webWorker(sock).start(); // Uncomment to see shutdown bug:
			// try{Thread.sleep(10000);} catch(InterruptedException ex) {}
		}

	}
}