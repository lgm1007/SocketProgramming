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
			//PrintWriter ���ڿ� true = ����flush
			writer = new PrintWriter(socket.getOutputStream(), true);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			Thread thread = new Thread(this);
			//start()�ϸ� �ٸ� �����尡 run()�޼ҵ带 ������.
			thread.start();
		} catch(IOException e) {	
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public void run() {
		try {	// ��� �޽����� ���� �� while(true)�� �ȿ� �ش� ���� �ֱ�
			String message = reader.readLine();
			System.out.println("�о�� �޽���: "+ message);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}