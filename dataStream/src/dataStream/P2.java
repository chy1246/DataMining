package dataStream;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class P2 {
   //two threads, one is to read host:port from system.in and receive the datastream, print them.
 //the other one is to receive the query from system.in and run the algorithm
		boolean enough = false;
		//countDownLatch.countDown();
	  public static void main(String[] args) throws IOException, InterruptedException{
		  //String s = "";
		  Semaphore semaphore = new Semaphore(1, true);
		  String serverName = "";
	      int port = -1;
	      int k = -1;
		  BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		  String IpPort = in.readLine();
		  serverName = IpPort.split(":")[0];
		  String sPort = IpPort.split(":")[1];
		  if(serverName == null || sPort == null){
			  System.out.println("serverName or portNumber is not in the right format");
			  System.exit(1);
		  }
		  port = Integer.valueOf(sPort);
		  ServerConnection connection = new ServerConnection(serverName, port, semaphore);
		  connection.start();
		  String query;
		  //Queue<Integer> queue = connection.queue;
		  Stack<Integer> stack = connection.stack;
		  while((query = in.readLine()) != null){
			  if(query.equals("end")){
				  connection.shutDownClient();
				  return;
			  }else{
				 String[] words = query.split(" ");
				 k = Integer.valueOf(words[6]);
				 while(k > connection.time){
					 try {
						System.out.println("No enough data");
						Thread.sleep(100); //wait input data
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				 }
				 semaphore.acquire();
				 int sum = new P2().queryHandler(k, connection._data, connection.time);
				 int realSum = connection._sum;
				 System.out.println("The sum for the last " + k + " integers is: " + sum);
				 System.out.println("The real sum for the last " + k + " integers is: " + realSum);
				 System.out.println("The ratio of is: " + (double)sum / realSum);
				 semaphore.release();
			  }
		  }
	  }
	  
	  public int queryHandler(int k, ArrayList<HashMap<Integer, Bucket>> data, int totalTime){
		  int sum = 0;
		  for(int i = 0; i <= 15; i++){
			  HashMap<Integer, Bucket> map = data.get(i);
			  int size = -1;
			  int difference = Integer.MAX_VALUE;
			  boolean exact = false;
			  int choice = -1;
			  for(Map.Entry<Integer, Bucket> entry : map.entrySet()){
				  int candidate = totalTime - k + 1;
				  int timeStap = entry.getValue()._timeStap;
				  int timeStap1 = entry.getValue()._timeStap1;
				  if(candidate == timeStap){
					  size = entry.getKey();
					  exact = true;
					  choice = 0;
					  break;
				  }else if(timeStap1 != -1 && candidate == timeStap1){
					  size = entry.getKey();
					  exact = true;
					  choice = 1;
					  break;
				  }else if(candidate - timeStap < 0 && difference > timeStap - candidate){ // get the size, find difference, the most smallest difference
					  difference = timeStap - candidate;
					  size = entry.getKey();
					  choice = 0;
				  }else if(timeStap1 != -1 && candidate - timeStap1 < 0 && difference > timeStap1 - candidate){
					  difference = timeStap1 - candidate;
					  size = entry.getKey();
					  choice = 1;
				  }
			  }
			for(int z = 1; z <= size; z *= 2){
				//System.out.println("The size is :" + size + "z is : " + z);
				int count = choice == 1 ? map.get(z)._count : 1;
				if(!exact && z == size){
				   sum += z * count * Math.pow(2, i) / 2;
				}else{
				sum += z * count * Math.pow(2, i);
				}
			}
		  }
		  return sum;
	  }
}

class ServerConnection extends Thread{
	Semaphore _lock;
	String _serverName;
	int _port;
	ArrayList<HashMap<Integer, Bucket>> _data = new ArrayList<>();   //bucket use linkedList
	Socket _client;
	int time = 0;
	boolean _work = true;
	int _sum = 0;
	//Queue<Integer> queue = new LinkedList<>();
	Stack<Integer> stack = new Stack<>();
	 //record the bucket size,  freq
	public ServerConnection(String serverName, int port, Semaphore lock){
		_serverName = serverName;
		_port = port;
		for(int i = 0; i <= 15; i++){
			_data.add(new HashMap<Integer, Bucket>());
		}
		_lock = lock;
	} 
	 @Override
	  public void run(){
		 getData();
	  }
	public void getData(){
		if(_port <= 0 || (_port > 0 && _port <= 1024)){
			  System.out.println("Input port number is a well konwn number");
			  System.exit(1);
		  }
		  _port = Integer.valueOf(_port);
	      try {
	          System.out.println("Connecting to " + _serverName + " on port " + _port);
	           _client = new Socket(_serverName, _port);
	          //int numericValue = Character.getNumericValue(asciiValue);
	          System.out.println("Just connected to " + _client.getRemoteSocketAddress());
	          InputStream inFromServer = _client.getInputStream();
	          DataInputStream serverData = new DataInputStream(inFromServer);
	          while(_work){
	          //use lock
	          _lock.acquire();
	          @SuppressWarnings("deprecation")
	          String tempData = serverData.readLine();
	          int data = -1;
	          int printData = -1;
	          if(tempData != null){
	          data = Integer.parseInt(tempData);
	          printData = data;
	          _sum += data;
	          Thread.sleep(10);
	          }else{
	          System.out.println("There is no more data");
	          return;
	          }
	          time++;
	          for(int i = 0; i <= 15; i++){
	        	  int PrevTime = 0;
	        	  if((data & 1) == 1){
	        	  HashMap<Integer, Bucket> map= _data.get(i);
	        	  if(!map.containsKey(1)){
	        		  map.put(1, new Bucket(1, time, -1));
	        	  }else{
	        	  if(map.get(1)._count == 1){
	        		  map.get(1)._timeStap1 = time;
	        	  }
	        	  int freq =  map.get(1)._count + 1;
	        	  map.get(1)._count = freq;
	        	  PrevTime = map.get(1)._timeStap;
	        	  map.get(1)._timeStap = time;
	        	  if(freq > 2){
	        		  int j = 2;
	        		  while(j <= Math.pow(2, map.size() - 1)){
	        			  //System.out.println(map.size());
	        			  //System.out.println(j);
	        			 map.get(j / 2)._count = 1;
	        			 map.get(j / 2)._timeStap1 = -1;
	   	        	  	 freq =  map.get(j)._count + 1;
	   	        	  	 map.get(j)._count = freq;
	        			 int temp = map.get(j)._timeStap;
	        			 map.get(j)._timeStap = PrevTime;
	        			 PrevTime = temp;
	        			 if(map.get(j)._count == 2){
	        				 map.get(j)._timeStap1 = temp; 
	        			 }
	        			 if(freq <= 2) break;
	        			 j = j * 2;
	        		  }
	        		  if(map.get(j / 2)._count > 2){
	        			  //System.out.println(i);
	        			  map.get(j / 2)._count = 1;
	        			  map.get(j / 2)._timeStap1 = -1;
		        		  map.put(j, new Bucket(1, PrevTime, -1));
		        	  }
	        	  	}
	        	  }
	        	}
	        	  data >>>= 1;
	          }
	          System.out.println("Server says " + printData);
	          _lock.release();
	          }
	          //client.close(); unlock
	       }catch(IOException | InterruptedException e) {
	    	  System.out.println("The server is down");
	          //e.printStackTrace();
	       }
	}
	public void shutDownClient() throws IOException{
		_client.close();
		_work = false;
		System.out.println("The client is down");
	}
}
class Bucket {
	int _count; 
	int _timeStap;
	int _timeStap1;
	public Bucket(int count, int timeStap, int timeStap1){
		_count = count;
		_timeStap = timeStap;
		_timeStap1 = timeStap1;
	}
}
