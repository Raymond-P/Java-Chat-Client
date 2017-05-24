package chat_version_3;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ChatServer implements Runnable{
	private Socket socket = null;
	private ServerSocket server = null;
	private DataInputStream strIn = null;
	boolean done = true;// always done until we're not done
	Thread thread = null;
	ChatServerThread client = null;
	
	public ChatServer(int port){
		try{
			server = new ServerSocket(port);//step1
			System.out.println("Started the server...waiting for a client");
			start(); //the chatserver's start method that goes ahead and creates a new thread
		}
		catch(IOException e){
			System.err.println("ERROR "+e.getMessage());
			
		}
	}
	public void start(){
		if(thread == null){
			thread = new Thread(this);
			thread.start();
		}
	}

	@Override
	public void run() {
		while(thread !=null){
			try{
				System.out.println("Waiting for a client...");
				//now we add a new Thread and accept a client
				addThread(server.accept());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void addThread(Socket socket){
		client = new ChatServerThread(this, socket);
		try {
			client.open();//open the stream for the ChatServerThread client
			client.start();//start to run the ChatServerThread client

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	}
	

	public static void main(String [] args){
		ChatServer myServer = null;
		if(args.length !=1){
			System.out.println("You need to specify a port number!!!");
		}
		else{
			int portNum = Integer.parseInt(args[0]);
			myServer = new ChatServer(portNum);//create an instance of my ChatServer
		}
	}
	
}
