package chat_client_gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
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

public class ChatClientGUI extends JFrame implements ActionListener, Runnable{

	private Socket socket = null;
	private final String serverName = "127.0.0.1"; //"localhost"//or your friend's ip address
	private final int serverPortNumber = 8080; //needs to ma
	
	private DataInputStream strIn = null;
	private DataOutputStream strOut = null;
	
	private ChatClientThread client = null;
	private boolean done = true;//until connected you are "done"
	private String line = "";
	
	private JTextArea displayText = new JTextArea();
	private JTextField input = new JTextField(30);
	private JButton btnConnect = new JButton("Connect");
	private JButton btnSend = new JButton("Send");
	private JButton btnQuit = new JButton("Bye");
	private JButton btnPrivate = new JButton("Private");
	private JPanel mainJP = new JPanel();//everything goes in here
	private JPanel displayJP = new JPanel();//textarea.. plus whatever 
	private JPanel btnsJP = new JPanel();//put this on the bottom
	
	
	public ChatClientGUI(){
		this.setTitle("My lovely Chat");
		mainJP.setLayout(new BorderLayout());
		displayJP.setLayout(new GridLayout(2,1));
		displayJP.add(displayText); //added textarea to jpanel
		displayJP.add(input);//added input below textarea to jpanel
		btnsJP.setLayout(new GridLayout(1,4));

		btnPrivate.addActionListener(this);
		btnConnect.addActionListener(this);
		btnSend.addActionListener(this);
		btnQuit.addActionListener(this);
		
		btnsJP.add(btnPrivate);
		btnsJP.add(btnConnect);
		btnsJP.add(btnSend);
		btnsJP.add(btnQuit);
		
		mainJP.add(displayJP, BorderLayout.CENTER);//add to center
		mainJP.add(btnsJP, BorderLayout.SOUTH);//add to bottom
		
		add(mainJP);
		
	}
	
	@Override
	public void run() {
		while(!done){
			try {
				line = strIn.readUTF();
				//displayMessage(line);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	//	if(e.getSource()== btnConnet);
			
		//do something when connect
		//connect(serverName, serverPortNumber);
		//do something when send
		//send()
		//do something when private
		//send... private...
		//do something when quit
		//disconnect();
		
	}
	public void connect(String serverName, int portNum){
		try {
			done=false;
			socket = new Socket(serverName, portNum);
			//display("hooray that we got connected");... 
//			open();
//			send();
//			btnSend.enable();
//			btnConnect.enable();
//			btnQuit.enable();
//			btnPrivate.enable();
			//enable our buttons.... 
			//either.... use .setEnabled... and have separate buttons and use
			//getSource from within actionPerformed
			//or....... use .setText ... and have 1 button whose face changes and use
			//getActionCommand from within actionPerformed
			//or... still use getSource... and compare against the boolean done
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
			done=true;
			//displayMessage("OOPSY"+ e.getMessage()+"... or something nicer");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			done=true;
			//displayMessage("OOPSY"+ e.getMessage()+"... or something nicer");
		}
		finally{
			//displayMessage("the connection attempt completed... success/failure..");
			//onto the next task.. blah blha
		}
		
	}
	

	
	public void send(){
		try {
		//get the message from the input textfield getText(); 
		//...store it in a String
		// make sure to 
			input.requestFocus();
			String msg = input.getText();
			
			strOut.writeUTF(msg);
			strOut.flush();
		    input.setText("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//displayMessage("BLAH");
		}
	}
	private void displaymessage(String message){
		displayText.append(message +"\n");
	}
		
	public void disconnect(){
		done=true;
		input.setText("BYE");
		send();
		///either setEnabled(false/true)... for your buttons
		//or...setText("Connect")... for your buttons...
		//if ... stream... is !=null
		//stream.close();
		//do that for in... and for out... do that for the socket...
	}
	public void open(){
		try {
			strOut = new DataOutputStream(socket.getOutputStream());
			strIn = new DataInputStream(socket.getInputStream());
			new Thread(this).start();//to be able to listen in
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//strIn =...
	}
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater( new Runnable(){
			public void run() {
				ChatClientGUI chatclient = new ChatClientGUI();
				chatclient.pack();
				chatclient.setVisible(true);
			}
			
			
		}
				
				);
	}








}
