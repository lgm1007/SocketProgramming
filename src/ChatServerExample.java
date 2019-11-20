import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServerExample {
    private static ArrayList<PrintWriter> outputStreams = new ArrayList<>();

    private static void run() {
        try {
            ServerSocket server = new ServerSocket(5001);
            while (true) {
                Socket client = server.accept();
                PrintWriter writer = new PrintWriter(client.getOutputStream());
                outputStreams.add(writer);
                Thread thread = new Thread(new ClientHandler(client, writer));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void broadcast(String message, PrintWriter writer) {
        for (PrintWriter wr : outputStreams) {
            if (wr.equals(writer)) continue;
            wr.println(message);
            wr.flush();
        }
    }

    private static class ClientHandler implements Runnable {
        Socket client;
        PrintWriter writer;
        BufferedReader reader;

        private ClientHandler(Socket client, PrintWriter writer) {
            try {
                this.client = client;
                this.writer = writer;
                reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            String message = String.format("%s connected", client.getRemoteSocketAddress());
            System.out.println(message);
            broadcast(message, writer);
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.printf("%s > %s\n", client.getRemoteSocketAddress(), message);
                    broadcast(client.getRemoteSocketAddress() + " > " + message, writer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
    	ChatServerExample.run();
    }
}