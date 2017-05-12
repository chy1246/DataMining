package dataStream;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	ServerSocket _serverSocket;
	public static void main(String[] args) throws Exception{
		new Server().run();
	}
	   public void run() throws Exception{
		   System.out.println("Server is running");
		   _serverSocket = new ServerSocket(3050);
		   while(true){
			   System.out.println("in");
			   Socket s = _serverSocket.accept();
			   ConnectionHandler handler = new ConnectionHandler(s);
			   handler.start();
		   }
	   }
		  
}
