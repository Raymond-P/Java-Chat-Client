package chat_client_gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import chat_version_4.ChatClientThread;

public class MyChatClientGUI extends JFrame implements Runnable, ActionListener{

	
	private Socket socket = null;
	private final String serverName = "127.0.0.1";
	private final int serverPortNumber = 8080;
	
	
	private DataInputStream strIn = null;
	private DataOutputStream strOut = null;
	
	private ChatClientThread client = null;
	private boolean done = true;//untill concented you are 'done'
	private String line ="";
	
	private JTextArea displayText = new JTextArea();
	private JTextField input = new JTextField();
	private JButton btnConnect =  new JButton("Connect");
	private JButton btnSend = new JButton("Send");
	private JButton btnQuit = new JButton("Bye");
	private JButton btnPrivate = new JButton("Private");
	private JPanel mainJP = new JPanel();//everything goes in here
	private JPanel displayJP = new JPanel();//text area... plus whatever
	private JPanel btnsJP = new JPanel(); //put this on the buttong
	
	public MyChatClientGUI() {
		this.mainJP.setLayout(new BorderLayout());
		this.displayJP.setLayout(new GridLayout(1, 1));
		this.displayJP.add(this.displayText);//added text area to JPanel
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.btnsJP.setLayout(new GridLayout(1, 4));
		
		this.setSize(400, 400);
		this.setResizable(false);
		
		
		
	}
	
	@Override
	public void run() {
		while(!done){
			try {
				line = strIn.readUTF();
				//display message(line);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//DO something when quit
		//disconnect()
		//do something when connect
		//connect(servername,serverPortNumber);
		//do somethign when send
		//do something when private
		
	}
	
	public void connect(String serverName, int portNumber){
		
		try {
			done = false;
			this.socket=new Socket(serverName, portNumber);
			//display("hoorrat connected");....
			open();
			//enable our buttons.....
			//either use .setEnabled... and have separate buttons and use
			//getSource form within ActionPerformed
			//or.. use .setText... and have 1 button whose face changes and use
			//getActionCommand from actionPerformed
			//or.... still use getSource...and compare against the boolean done
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			done=true;
			//displayMessage("Ooops.."+e.getMessage()+"...or something nicer");
		}//catch (UnknownHostException e){
//			e.printStackTrace();
//		}
	}
	
	private void send() {
		// TODO Auto-generated method stub
		//get the message dorm the textfield getText();
		
		
		try {
			strOut.writeUTF("The message.....");
			strOut.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	private void disconnect() {
		this.done = true;
		input.setText("BYE");
		send();
		//either setEnabled(false/true)...for your buttons...
	}
	
	private void open() {
		
		try {
			strOut = new DataOutputStream(socket.getOutputStream());
			strIn = new DataInputStream(socket.getInputStream());
			new Thread(this).start();//to be able to listen in
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				// TODO Auto-generated method stub
				MyChatClientGUI chatclient = new MyChatClientGUI();
				chatclient.pack();
				chatclient.setVisible(true);
			}
			
		}
		);
	}



	
}
