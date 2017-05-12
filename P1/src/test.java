import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class test {
  public static void main(String[] args){
	  p1 test = new p1();
	  //String data1 = p1.readFile("test.txt");
	  //String data2 = p1.readFile("test1.txt");
	  //<Singleings> set = p1.calculateSingleings(data1);
	  //Set<Singleings> set1 = p1.calculateSingleings(data2);
		//ArrayList<Set<Singleings>> temp = new ArrayList<>();
		//temp.add(set);
		//temp.add(set1);
		//int[][] Omatrix = p1.drawMatrix(temp);
		//int[][] signature = p1.calculateSignature(Omatrix);
		//for(int i = 0; i < signature.length; i++){
			//for(int j = 0; j < signature[0].length; j++){
				//System.out.print(signature[i][j]);
			//}
			//System.out.println();
		//}
		//int[] br = test.findBR(signature.length);
		//ArrayList<int[]> candidates = test.findCandidatePairs(signature, br);
		//ArrayList<int[]> similarPair = test.findSimilarPairs(signature, candidates);
		//System.out.println(similarPair.get(0)[0] + " - "+ similarPair.get(0)[1]);
		
		//int[] brtest = test.findBR(8);
		//System.out.println(brtest[0] + " : " + brtest[1]);
	  
	   
	   ArrayList<String> files = parse();
	   System.out.println(files);
	  
	  
	  /**
	  int[][] Omatrix = new int[7][4];
	  Omatrix[0][0] = 1;
	  Omatrix[0][2] = 1;
	  Omatrix[1][0] = 1;
	  Omatrix[1][3] = 1;
	  Omatrix[2][1] = 1;
	  Omatrix[2][3] = 1;
	  Omatrix[3][1] = 1;
	  Omatrix[3][3] = 1;
	  Omatrix[4][1] = 1;
	  Omatrix[4][3] = 1;
	  Omatrix[5][0] = 1;
	  Omatrix[5][2] = 1;
	  Omatrix[6][0] = 1;
	  Omatrix[6][2] = 1;
	 int[][] res = test.calculateSignature(Omatrix);
	 for(int i = 0; i < res.length; i++){
		 for(int j = 0; j < res[0].length; j++){
			 System.out.print(res[i][j] + " ");
		 }
		 System.out.println();
	 }
  **/
   }
   public static ArrayList<String> parse(){
	   String s = " infile  similar.txt			f1.txt    f2.txt";
	   ArrayList<String> files = new ArrayList<>();
	   int start = 0;
	   //String[] file = s.split(" ");
	   //files.addAll(Arrays.asList(file));
   	for(int i = 0; i < s.length(); i++){
   		if(s.charAt(i) == ' ' && start == i){
   			start++;
   			System.out.println(start);
   			continue;
   		}else if(s.charAt(i) == ' ' && start != i){
   			files.add(s.substring(start, i));
   			start = i + 1;
   		}else if(i == s.length() - 1){
   			if(s.charAt(i) != ' '){
   				files.add(s.substring(start, i + 1));
   			}else{
   				files.add(s.substring(start, i));
   			}
   		}
   	}
   	return files;
   }
}
