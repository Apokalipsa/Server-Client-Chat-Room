package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;



public class ChetClient {

	public static void main(String[] args) throws UnknownHostException, IOException {
		
		int port = 0;
		String ipAddress = "";
		Scanner input = new Scanner(System.in);

		System.out.println("\n******Welcome to Deny chat room******!");

		System.out.println("\n--- Please enter number of your port: ---");
		port = input.nextInt();
		input.nextLine();
		System.out.println("--- Enter server ip address: ---");
		ipAddress = input.nextLine();
		// get connection and initialize streams
		Socket socket = new Socket(ipAddress, port);

		BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

		String message = "";
		// show all messages from server
		while (true) {

			while (!message.startsWith("*** Name successfuly registerd! ***")) {
				message = inputFromClient.readLine();
				System.out.println(message);
				out.println(input.nextLine());
				message = inputFromClient.readLine();

			}
			System.out.println("\n**** Welcome to chat room! ****");
			while (true) {
				Listener test = new Listener(socket);
				test.start();
				out.println(input.nextLine());

			}
		}
	}

	
	// thread for listening to server and printing client messages
	
	
	private static class Listener extends Thread {
		Socket socket;

		
		// constructs a handler thread, squirreling away the socket.
		public Listener(Socket soc) {
			socket = soc;
		}

		public void run() {

			try {
				BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				while (true) {
					System.out.println(inputFromClient.readLine());
				}
			} catch (UnknownHostException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}

		}
	}
}