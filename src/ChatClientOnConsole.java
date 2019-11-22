import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class ChatClientOnConsole implements Runnable {

	PrintWriter writer;
	BufferedReader reader;
	
	public void start() {
		
		this.setNetWork();
		
		String message = " ";
		while(true) {
			Scanner scan = new Scanner(System.in);
			message = scan.nextLine();
			if(message.equals("quit"))
				break;
			else {
				writer.println(message);
			}
		}
	}
	
	public static void main(String[] args) {
		ChatClientOnConsole client = new ChatClientOnConsole();
		client.start();
	}

	private void setNetWork() {
		try {
			Socket socket = new Socket("10.80.71.60", 5055);
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
	public void run() {
		try {	// 계속 메시지를 받을 땐 while(true)문 안에 해당 구문 넣기
			String message = reader.readLine();
			System.out.println("읽어온 메시지: "+ message);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}