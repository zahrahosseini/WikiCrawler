/// ***************************************************************************
//                         Seyedehzahra Hosseini 
//                         Hamid Bagheri
//******************************************************************************
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyWikiRanker {

	public static void main(String[] args) {
		PageRank pr = new PageRank("MyWikiGraph.txt", 0.01);
		String[] topRank= pr.topKPageRank(15);
		String[] topInDegree= pr.topKInDegree(15);
		String[] topOutDegree= pr.topKOutDegree(15);
		
		MyWikiRanker wiki=new MyWikiRanker();
		
		
        List<String> list1 = new ArrayList<String>(Arrays.asList(topRank));
        List<String> list2 = new ArrayList<String>(Arrays.asList(topInDegree));
        List<String> list3 = new ArrayList<String>(Arrays.asList(topOutDegree));

//        System.out.println(list1);
//        System.out.println();
//        System.out.println(list2);
//		
		System.out.println("Epsilon= 0.01 " );
        System.out.println("Jacc Sim(topRank,topInDegree)= " +(double)wiki.intersection(list1, list2).size()/wiki.union(list1, list2).size());
		
		System.out.println("Jacc Sim(topRank,topOutDegree)= " +(double)wiki.intersection(list1, list3).size()/wiki.union(list1, list3).size());

		//System.out.println(wiki.intersection(list3, list2));
		System.out.println("Jacc Sim(topInDegree,topOutDegree)= " +(double)wiki.intersection(list2, list3).size()/wiki.union(list2, list3).size());

		//Calculate Jacc for each pair
		
		System.out.println();
		
		//Calculate with e=0.005
		pr = new PageRank("MyWikiGraph.txt", 0.005);
		topRank= pr.topKPageRank(15);
		topInDegree= pr.topKInDegree(15);
		topOutDegree= pr.topKOutDegree(15);


        list1 = new ArrayList<String>(Arrays.asList(topRank));
        list2 = new ArrayList<String>(Arrays.asList(topInDegree));
        list3 = new ArrayList<String>(Arrays.asList(topOutDegree));
		
        System.out.println("Epsilon= 0.005 " );
		System.out.println("Jacc Sim(topRank,topInDegree)= " +(double)wiki.intersection(list1, list2).size()/wiki.union(list1, list2).size());
		
		System.out.println("Jacc Sim(topRank,topOutDegree)= " +(double)wiki.intersection(list1, list3).size()/wiki.union(list1, list3).size());

		//System.out.println(wiki.intersection(list3, list2));
		System.out.println("Jacc Sim(topInDegree,topOutDegree)= " +(double)wiki.intersection(list2, list3).size()/wiki.union(list2, list3).size());

		

	}

	 public <T> List<T> union(List<T> list1, List<T> list2) {
	        Set<T> set = new HashSet<T>();

	        set.addAll(list1);
	        set.addAll(list2);

	        return new ArrayList<T>(set);
	    }

	    public <T> List<T> intersection(List<T> list1, List<T> list2) {
	        List<T> list = new ArrayList<T>();

	        for (T t : list1) {
	            if(list2.contains(t)) {
	                list.add(t);
	            }
	        }
	        list.removeAll(Collections.singleton(null));

	        return list;
	    }
	    
}
