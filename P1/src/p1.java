import java.io.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class p1 {
public static void main(String[] args) throws IOException{
	    p1 test = new p1();
	    String s;
	    //StringBuilder sb = new StringBuilder();
	    ArrayList<String> files = new ArrayList<>();
	    BufferedReader in = new BufferedReader(
	    		new InputStreamReader(System.in)
	    		);
	    	files.add(in.readLine());
	    	files.add(in.readLine());
	    ArrayList<Set<Singleings>> data = new ArrayList<>();
	    for(String file : files){
	    	String text = test.readFile(file);
	    	Set<Singleings> set = test.calculateSingleings(text);
	    	data.add(set);
	    }
	    int[][] Omatrix = test.drawMatrix(data);
	    test.drawMatrix(Omatrix);
	    int number = (int)Math.sqrt(Omatrix.length) + 1;
	    int[] br = test.findBR(number);
	    int[][] signature = test.calculateSignature(Omatrix, br[2]);
	    test.drawMatrix(signature);
	    ArrayList<int[]> candidates = test.findCandidatePairs(signature, br);
	    ArrayList<int[]> similarPairs = test.findSimilarPairs(signature, candidates);
	    for(int[] pairs : similarPairs){
	    	System.out.println(files.get(pairs[0]) + " and " + files.get(pairs[1]) + " are the simialr documents");
	    }
	    }
	    
		//String test2 = readFile("test.txt");
		//String test1 = readFile("test1.txt");
		//Set<Singleings> set = calculateSingleings(test2);
		//Set<Singleings> set1 = calculateSingleings(test1);
		//Iterator<Singleings> it = set.iterator();
		//while(it.hasNext()){
			//System.out.println(it.next()._data);
		//}
		//Iterator<Singleings> it1 = set1.iterator();
		//while(it1.hasNext()){
			//System.out.println(it1.next()._data);
		//}
		//ArrayList<Set<Singleings>> temp = new ArrayList<>();
		//temp.add(set);
		//temp.add(set1);
        
		//drawMatrix(temp);
public  String readFile(String path){
	StringBuilder sb = new StringBuilder();
	File file = new File(path);
	try{
		FileReader isr = new FileReader(file);
		@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(isr);
		String str = br.readLine();
		sb.append(str);
		while(str != null){
			str = br.readLine();
			if(str != null){
			sb.append(str);
				}
			}
		}catch (IOException e){
			System.out.println("No such a file : " + path);
			exit(1);
			//e.printStackTrace();
		}
		System.out.println(sb.toString());
		return sb.toString();
	}

private void exit(int i) {
			// TODO Auto-generated method stub
			
		}

public Set<Singleings> calculateSingleings(String text){
	Set<Singleings> res = new HashSet<>();
	boolean space = false;
	int i = 0;
	int j = 0;
	StringBuilder sb = new StringBuilder();
	while(i < text.length()){
		if(j == 9 || i == text.length() - 1){
			res.add(new Singleings(sb.toString()));
			sb = new StringBuilder();
			j = 0;
		}
		
		if(text.charAt(i) == ' ' && !space || text.charAt(i) != ' '){
				sb.append(text.charAt(i));
				j++;
		}
		
		if(text.charAt(i) == ' '){
			space = true;
		}else{
			space = false;
		}
		
		i++;
	}
	return res;
	}

public int[][] drawMatrix (ArrayList<Set<Singleings>> data){
	if(data.size() == 0){
		throw new IllegalArgumentException("Empty File Input");
	}
	Set<Singleings> set = new HashSet<>();
	for(Set<Singleings> temp : data){
		set.addAll(temp);
	}
	int[][] Omatrix = new int[set.size()][data.size()];
	ArrayList<Singleings> Singleingsindex = new ArrayList<>(set); 
	for(int i = 0; i < Omatrix[0].length; i++){
		Set<Singleings> temp = data.get(i);
		for(int j = 0; j < Omatrix.length; j++){
			if(temp.contains(Singleingsindex.get(j))){
				Omatrix[j][i] = 1;
			}
		}
	}
	
	for(int i = 0; i < Omatrix.length; i++){
		for(int j = 0; j < Omatrix[0].length; j++){
			//System.out.print(Omatrix[i][j]);
		}
		//System.out.println();
	}
	return Omatrix;
}

public int[] permutation(int[] input){
	Random random = new Random();
	for(int i = 0; i < input.length / 2; i++){
		int pick1 = random.nextInt(input.length);
		int pick2 = random.nextInt(input.length);
		int temp = input[pick1];
		input[pick1] = input[pick2];
		input[pick2] = temp;
		}
	return input;
  }

public int[][] calculateSignature(int[][] Omatrix, int numberPer){
	int length = Omatrix.length;
	int[] input = new int[length];
	int[][] signature = new int[numberPer][Omatrix[0].length];
	for(int i = 1; i <= length; i++){
		input[i - 1] = i;
	}
	for(int i = 0; i < numberPer; i++){
		input = permutation(input);
		//System.out.println(input[0]);
		for(int j = 0; j < Omatrix[0].length; j++){
			for(int z = 1; z <= length; z++){
				int row = nextPostion(input, z);
				//System.out.println(row);
				if(Omatrix[row][j] != 0){
					signature[i][j] = z;
					//System.out.println(z);
					break;
				 }
			}
		}
	}
	return signature;
}
public int nextPostion(int[] input, int next){
	for(int i = 0; i < input.length; i++){
		if(input[i] == next){
			return i;
		}
	}
	return -1;
}

public int[] findBR(int number){
	int[] res = new int[3];
	for(int b = 1; b <= number; b++){
		int r = number / b;
		if(b * r == number){
		double po1 = calculatePossi(b, r, 0.2);
		double po2 = calculatePossi(b, r, 0.8);
		if(po1 <= 0.003 && po2 >= 0.997){  //0.997 0.003
			System.out.println("po1 is : " + po1);
			System.out.println("po2 is : " + po2);
			res[0] = b;
			res[1] = r;
			res[2] = number;
			System.out.println("b: " + res[0] + " r: " + res[1] + " permutation number: " + res[2]);
			break;
			}
		}
	}
		// if can not find propser br then use while(true) to add b and r to find 
     if(res[0] == 0 || res[1] == 0){
    	 res = findBR(number + 1);
     }
	return res;
}

public double calculatePossi(int b, int r, double base){
	   return  1 - Math.pow((1 - Math.pow(base,r)), b);
	}

public ArrayList<int[]> findCandidatePairs(int[][] signature, int[] br){
	ArrayList<int[]> candidates = new ArrayList<>();
	//create a key object, use map do store them, calculate the all pairs, store in two dem array (file * file), find candidates
	int i = 0;
	Map<bandKey, ArrayList<Integer>> bucket = new HashMap<>();
	while(i < br[0] * br[1]){
		for(int k = 0; k < signature[0].length; k++){
		int[] keyData = new int[br[1]];
		for(int j = i; j < i + br[1]; j++){
			keyData[j - i] = signature[j][k];
			//System.out.println("j: " +  j + "k: " + k);
		  }
		bandKey dataKey = new bandKey(keyData, i);
		if(bucket.containsKey(dataKey)){
			ArrayList<Integer> files = bucket.get(dataKey);
			//System.out.println("in");
			files.add(k);
			System.out.println(files);
			bucket.put(dataKey, files);
		}else{
			ArrayList<Integer> files = new ArrayList<>(Arrays.asList(k));
			bucket.put(dataKey, files);
			}
		  }
		i = i + br[1];
	}
    int[][] freq = new int[signature[0].length][signature[0].length];
    for(Entry<bandKey, ArrayList<Integer>> entry : bucket.entrySet()){
    	ArrayList<Integer> files = entry.getValue();
    	for(int m = 0; m < files.size(); m++){
    		for(int p = m + 1; p < files.size(); p++){
    			freq[files.get(m)][files.get(p)]++;
    		}
    	}
    }
    for(int j = 0; j < freq.length; j++){
    	for(int k = 0; k < freq[0].length; k++){
    		System.out.print(freq[j][k]);
    	}
    	System.out.println();
    }
    
    for(int j = 0; j < freq.length; j++){
    	for(int k = 0; k < freq[0].length; k++){
    		if(freq[j][k] > 0){
    			int[] candidate = new int[]{j, k};
    			//System.out.println("j: " + j + "k: " + k);
    			candidates.add(candidate);
    		}
    	}
    }
	return candidates;
 }

public ArrayList<int[]> findSimilarPairs(int[][] signature, ArrayList<int[]> candidates){
	ArrayList<int[]> similarPairs = new ArrayList<>();
	for(int[] candidate : candidates){
		if(jaccard(signature, candidate[0], candidate[1]) > 0.8){
			similarPairs.add(candidate);
		}
	}
	return similarPairs;
}


public double jaccard(int[][] signature, int file1, int file2){
	 int total = 0;
	 int same = 0;
	 for(int i = 0; i < signature.length; i++){
		 if(signature[i][file1] == 0 && signature[i][file2] == 0){
			 continue;
		 }
		 if(signature[i][file1] == 1 && signature[i][file2] == 1){
			 same++;
		 }
		 total++;
	 }
	 return same / total;
	}

public void drawMatrix(int[][] matrix){
	for(int i = 0; i < matrix.length; i++){
		//input = permutation(input);
		for(int j = 0; j < matrix[0].length; j++){
			System.out.print(matrix[i][j] + " ");
		}
		System.out.println();
	}
}
}
class Singleings {
	String _data;
	public Singleings(String data){
		_data = data;
	}
	
	@Override
	public boolean equals(Object obj){
	    if (obj == null) {
	        return false;
	    }
	    if (!Singleings.class.isAssignableFrom(obj.getClass())) {
	        return false;
	    }
	    final Singleings other = (Singleings) obj;
	    for(int i = 0; i < _data.length(); i++){
	    	if(_data.charAt(i) != other._data.charAt(i)){
	    		return false;
	    	}
	    }
	    return true;
	}
	
	@Override
	public int hashCode(){
		int hash = 17;
		hash += _data.hashCode();
		return hash;
	}
}

class bandKey {
	int[] _data;
	int _b;
	public bandKey(int[] data, int b){
		_b = b;
		_data = data;
	}
	@Override
    public boolean equals(Object obj){
		final bandKey other = (bandKey) obj;
		if(_b != other._b){
			return false;
		}
		for(int i = 0; i < _data.length; i++){
			if(_data[i] !=  other._data[i]){
				return false;
			}
		}
		return true;
	}
	@Override
	public int hashCode(){
		int hashcode = 17;
		for(int i = 0; i < _data.length; i++){
		hashcode += new Integer(_data[i]).hashCode();
		}
		hashcode += _b;
		return hashcode;
	}
}