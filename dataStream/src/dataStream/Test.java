package dataStream;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Test {
   public static void main(String[] args) throws NumberFormatException, IOException{
	   BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	   String s;
	   int time = 0;
	   int sum = 0;
	   int _realSum = 0;
	   ArrayList<HashMap<Integer, BucketTest>> _data = new ArrayList<>();
	   for(int i = 0; i <= 15; i++){
		   _data.add(new HashMap<Integer, BucketTest>());
	   }
	   while((s = in.readLine()) != null){
		      if(s.equals("end")){
		    	  for(int i = 0; i <= 15; i++){
		    		  HashMap<Integer, BucketTest> temp = _data.get(i);
		    		  if(! temp.isEmpty()){
		    			  for(Map.Entry<Integer, BucketTest> entry : temp.entrySet()){
		    				  System.out.println("This is the " + i + " th" + " map");
		    				  System.out.println("size " + entry.getKey());
		    				  System.out.println("time" + entry.getValue()._timeStap);
		    				  System.out.println("count: " + entry.getValue()._count);
		    				  sum += entry.getValue()._count * entry.getKey() * Math.pow(2, i);
		    			  }
		    		  }
		   	   }
		    	  System.out.println("The estimated sum is : " + sum);
		    	  System.out.println("The real sum is : " + _realSum);
		    	  System.out.println("The setoff is : " + (double) Math.abs(sum - _realSum) / _realSum);
		   	   	int QuerySum = new Test().queryHandler(10, _data, time);
		   	   	System.out.println("The result is: " + QuerySum);
		    	  return;
		      }
	          int data = Integer.valueOf(s);
	          System.out.println("Server says " + data);
	          time++;
	          for(int i = 0; i <= 15; i++){
	        	  int PrevTime = 0;
	        	  if((data & 1) == 1){
	        	  _realSum += Math.pow(2, i);
	        	  HashMap<Integer, BucketTest> map= _data.get(i);
	        	  if(!map.containsKey(1)){
	        		  map.put(1, new BucketTest(1, time));
	        	  }else{
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
	   	        	  	 freq =  map.get(j)._count + 1;
	   	        	  	 map.get(j)._count = freq;
	        			 int temp = map.get(j)._timeStap;
	        			 map.get(j)._timeStap = PrevTime;
	        			 PrevTime = temp;
	        			 if(freq <= 2) break;
	        			 j = j * 2;
	        		  }
	        		  if(map.get(j / 2)._count > 2){
	        			  //System.out.println(i);
	        			  map.get(j / 2)._count = 1;
		        		  map.put(j, new BucketTest(1, PrevTime));
		        	  }
	        	  	}
	        	  }
	        	}
	        	  data >>>= 1;
	          }
	   }
   }
   public int queryHandler(int k, ArrayList<HashMap<Integer, BucketTest>> data, int totalTime){
		  int sum = 0;
		  //k > timestap, k = bucket timestap(exact), k is in one bucket(estimated)
		  //for loop to search  if any stream in the data then the result is estimated?
		  for(int i = 0; i <= 15; i++){
			  int size = -1;
			  int difference = Integer.MAX_VALUE;
			  boolean exact = false;
			  HashMap<Integer, BucketTest> map = data.get(i);
			  for(Map.Entry<Integer, BucketTest> entry : map.entrySet()){
				  int candidate = totalTime - k + 1;
				  int timeStap = entry.getValue()._timeStap;
				  System.out.println("This is the " + i + " th" + " map");
				  System.out.println("size " + entry.getKey());
				  System.out.println("time" + entry.getValue()._timeStap);
				  System.out.println("count: " + entry.getValue()._count);
				  System.out.println("The Candidate is : " + candidate);
				  if(candidate == timeStap){
					  size = entry.getKey();
					  exact = true;
					  break;
				  }else if(candidate - timeStap < 0 && difference > timeStap - candidate){ // get the size, find difference, the most smallest difference
					  difference = timeStap - candidate;
					  size = entry.getKey();
				  }
			  }
			for(int z = 1; z <= size; z *= 2){
				System.out.println("This is the "+ i + "th map"+"The size is :" + size + "z is : " + z);
				int count = map.get(z)._count;
				if(z == size && !exact){
				sum += z * count * Math.pow(2, i) / 2;
				}else{
					sum += z * count * Math.pow(2, i);
				}
			}
		  }
		  return sum;
	  }
}
   class BucketTest {
	   	int _timeStap;
		int _count; 
		public BucketTest(int count, int timeStap){
			_count = count;
			_timeStap = timeStap;
		}
   }

