package dataStream;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class ConnectionHandler extends Thread {
	Socket _s;
	DataOutputStream _os;
	public ConnectionHandler(Socket s) throws IOException{
		_s = s;
		_os = new DataOutputStream(_s.getOutputStream());
	}
	@Override
	public void run(){
		Random _r = new Random();
		while(true){
			try {
				_os.writeInt(_r.nextInt((int)Math.pow(2, 15)));
				Thread.sleep(10000);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
