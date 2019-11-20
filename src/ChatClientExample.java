import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;

public class ChatClientExample extends JFrame implements ActionListener, Runnable {

	JTextArea recvArea = new JTextArea("recv");
	JTextArea sendArea = new JTextArea("send");
	JTextField message = new JTextField();
	JButton sendButton = new JButton("전송");
	PrintWriter writer;
	BufferedReader reader;
	
	public void start() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		JPanel messagePanel = new JPanel(new GridLayout(1, 2));
		
		messagePanel.add(new JScrollPane(recvArea));
		messagePanel.add(new JScrollPane(sendArea));
		this.add(messagePanel, BorderLayout.CENTER);
		sendArea.setEnabled(false);
		recvArea.setEnabled(false);
		
		JPanel sendPanel = new JPanel(new BorderLayout());
		sendPanel.add(message, BorderLayout.CENTER); 
		sendPanel.add(sendButton, BorderLayout.EAST);
		this.add(sendPanel, BorderLayout.SOUTH);
		
		sendButton.addActionListener(this);
		message.addActionListener(this);
		
		this.setSize(800, 600);
		this.setVisible(true);
		
		this.setNetWork();
	}
	
	public static void main(String[] args) {
		ChatClientExample client = new ChatClientExample();
		client.start();
	}

	private void setNetWork() {
		try {
			Socket socket = new Socket("Localhost", 5001);
			//PrintWriter 인자에 true = 오토flush
			writer = new PrintWriter(socket.getOutputStream(), true);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			Thread thread = new Thread(this);
			//start()하면 다른 스레드가 run()메소드를 돌린다.
			thread.start();
		} catch(IOException e) {	
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		sendArea.append(message.getText() + '\n');
		writer.println(message.getText());
		message.setText("");
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				String message = reader.readLine();
				recvArea.append(message + '\n');
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}