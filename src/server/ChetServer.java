package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class ChetServer {
	// port that the server listens on
	private static int port = 0;

	// set of all names of clients in the chat room
	public static HashSet<String> clients = new HashSet<String>();
	// set of all the print writers for all the clients
	private static List<PrintWriter> writers = new ArrayList<PrintWriter>();
	private static Scanner input = new Scanner(System.in);

	public static void main(String[] args) throws IOException {

		System.out.println("\n******Welcome to Deny chat room******!");

		System.out.println("\n--- Please enter number of your port: ---");

		port = input.nextInt();
		ServerSocket listener = new ServerSocket(port);
		System.out.println(" --- Starting server on port: --- " + port + "\n --- Server ip:  "
				+ InetAddress.getLocalHost().getHostAddress() + " ---");
		try {
			ServerStatus status = new ServerStatus();
			status.start();
			while (true) {
				new Handler(listener.accept()).start();

			}
		} finally {
			listener.close();
		}
	}

	private static class ServerStatus extends Thread {
		public ServerStatus() {

		}

		public void run() {

			input.nextLine();
			while (true) {
				System.out.println("*****Menu*****:");
				System.out.println("--- press \"O\" and show online users. ---");

				String selection = input.nextLine();

				if (selection.equals("O")) {
					if (clients.isEmpty()) {
						System.out.println("--- No one is online ---");
					} else {
						System.out.println("--- Online users: ---");
						synchronized (clients) {

							for (String n : clients) {
								System.out.print(" - " + n);
							}

							System.out.println();

						}
					}
				} else {
					System.out.println("*** Ups! Try again please ***");
				}
			}
		}
	}

	private static class Handler extends Thread {
		private String name;
		private Socket socket;
		private BufferedReader inputFromClient;
		private PrintWriter out;

		public Handler(Socket socket) {
			this.socket = socket;
		}
		/*
		 * Request a name from this client. Keep requesting until a name is
		 * submitted that is not already used.
		 */

		public void run() {

			try {
				inputFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);

				while (true) {
					out.println("*** Select user name. ***");
					name = inputFromClient.readLine();
					if (name == null) {
						out.println("*** Name cant be empty! ***");
						return;
					}

					synchronized (clients) {

						if (clients.add(name)) {
							System.out.println(name + " registed online!");
							break;

						} else {
							out.println(" *** That name is occupied! ***");
						}
					}
				}
				out.println("*** Name successfuly registerd! ***");
				writers.add(out);

				while (true) {
					String input = inputFromClient.readLine();
					for (PrintWriter writer : writers) {
						writer.println("MESSAGE " + name + ": " + input);
					}
				}

			} catch (IOException e) {
			} finally {
				/*
				 * Remove client name and its print writer from the sets, and
				 * close socket.
				 */
				if (name != null) {
					clients.remove(name);

				}
				if (out != null) {
					writers.remove(out);
				}
				try {
					socket.close();
				} catch (IOException e) {
				}
			}

		}
	}
}
