/// ***************************************************************************
//                         Seyedehzahra Hosseini 
//                         Hamid Bagheri
//******************************************************************************
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

public class PageRank {
	DGraph<String> wikiGraph = new DGraph<>();
	double[] pgRank;
	// public static double Beta ;
	double Beta = 0.85;
	public static boolean ASC = true;
	public static boolean DESC = false;

	public static void main(String[] args) {

		PageRank pr = new PageRank("PavanWikiTennis.txt", 0.005);
		
		pr.topKPageRank(10);
		System.out.println();

		pr.topKOutDegree(2);	
		pr.topKInDegree(5);


	}

	public PageRank(String fileName, double e) {
		textToGraph(fileName);// create Graph from text file

		boolean converged = false;
		int n;

		int nodeNum = wikiGraph.graphSize(); // # of nodes
		double[] Pn = new double[nodeNum];// P n
		double[] Pn1 = new double[nodeNum];// P n+1

		// Set P to 1/N
		for (int i = 0; i < nodeNum; i++)
			Pn[i] = (double) 1 / nodeNum;

		n = 0;
		while (!converged) {
			Pn1 = nextStep(Pn);
			if (NormP(Pn, Pn1) <= e)
				converged = true;
			else
				Pn = Pn1;
			n++;
		}

		pgRank = Pn1;
		System.out.println("Number of Steps to Converge= " + n);

//		double sum = 0;
//		for (int i = 0; i < nodeNum; i++)
//			sum += pgRank[i];
//
//		float k = (float) Math.round(sum * 100) / 100;
//		System.out.println("sum page Rank= " + k);

	}

	public double pageRankOf(String page) {
		return pgRank[wikiGraph.nodeIndex(page)];
	}

	public String[] topKPageRank(int k) {
		double[] pgR = pgRank;
		String[] topKList = new String[k];

		// unsorted Page rank
		Map<Integer, Double> unsortedRank = new HashMap<Integer, Double>();
		for (int i = 0; i < wikiGraph.graphSize(); i++) {
			unsortedRank.put(i, pgRank[i]);
		}
		// Sort page Rank
		Map<Integer, Double> sortedRank = sortByComparator(unsortedRank, DESC);

		int topK = 0;
		for (Entry<Integer, Double> entry : sortedRank.entrySet()) {
			if (topK < k) {
				// System.out.println("Key : " + entry.getKey() + " Value : " +
				// entry.getValue());
				topKList[topK] = wikiGraph.nodeName(entry.getKey());
				topK++;
			} else
				break;
		}
		return topKList;
	}

	public String[] topKOutDegree(int k) {
		String[] topKList = new String[k];
		// System.out.println("Top k=" + k + " out Degree");

		Map<String, Integer> unsortedDegree = new HashMap<String, Integer>();

		String[] nodeLists = wikiGraph.nodeNamesLits();

		for (int i = 0; i < nodeLists.length; i++) {
			unsortedDegree.put(nodeLists[i], outDegreeOf(nodeLists[i]));
			// System.out.println(nodeLists[i] +" " +
			// outDegreeOf(nodeLists[i]));
		}

		// System.out.println("unsorted maria sha" +
		// unsortedDegree.get("/wiki/Maria_Sharapova"));
		Map<String, Integer> sortedDegree = sortByComparator2(unsortedDegree, DESC);

		int topK = 0;
		for (Entry<String, Integer> entry : sortedDegree.entrySet()) {
			if (topK < k) {
				//System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
				topKList[topK] = entry.getKey();
				topK++;
			} else
				break;
		}
		return topKList;

	}

	public String[] topKInDegree(int k) {

		String[] topKList = new String[k];

		Map<String, Integer> unsortedDegree = new HashMap<String, Integer>();

		
		String[] nodeLists = wikiGraph.nodeNamesLits();

		for (int i = 0; i < nodeLists.length; i++)
			unsortedDegree.put(nodeLists[i], inDegreeOf(nodeLists[i]));

		Map<String, Integer> sortedDegree = sortByComparator2(unsortedDegree, DESC);

		int topK = 0;
		for (Entry<String, Integer> entry : sortedDegree.entrySet()) {
			if (topK < k) {	
				//System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
				topKList[topK] = entry.getKey();
				topK++;
			} else
				break;
		}
		return topKList;

	}

	public double[] nextStep(double[] curP) {
		int nodeNum; // # of nodes
		String curPage, nextPage;
		int q;
		Set<String> outDegreeSet;

		nodeNum = wikiGraph.graphSize();
		double[] nextP = new double[nodeNum];// Next step of page rank

		// Set P to (1-Beta)/N
		for (int i = 0; i < nodeNum; i++)
			nextP[i] = (1 - Beta) / nodeNum;

		Iterator<String> entries = wikiGraph.iterator();
		while (entries.hasNext()) {
			curPage = (String) entries.next();
			if (outDegreeOf(curPage) > 0) {
				// For every page p if out-degree(p) > 0
				outDegreeSet = wikiGraph.edgesFrom(curPage);

				Iterator<String> out = outDegreeSet.iterator();
				while (out.hasNext()) {
					nextPage = (String) out.next();
					// q = wikiGraph.nodeIndex(nextPage); // q is a out-link
					// from p p-->q

					// System.out.println(q + " " + curPage + " " +nextPage );
					nextP[wikiGraph.nodeIndex(nextPage)] += Beta
							* (curP[wikiGraph.nodeIndex(curPage)] / outDegreeSet.size());
				}
			} else { // For every page p if out-degree(p) = 0
				Iterator<String> allVertex = wikiGraph.iterator();
				while (allVertex.hasNext()) {
					nextP[wikiGraph.nodeIndex((String) allVertex.next())] += Beta
							* (curP[wikiGraph.nodeIndex(curPage)] / nodeNum);
				}
			}
		}
		return nextP;
	}

	public double NormP(double[] Pn1, double[] Pn) {
		double normVec = 0;
		double norm[] = new double[Pn.length];

		for (int i = 0; i < Pn.length; i++)
			norm[i] = Pn1[i] - Pn[i];
		for (int i = 0; i < Pn.length; i++)
			normVec += Math.pow(norm[i], 2);

		return Math.sqrt(normVec);

	}

	public double Norm1(double[] Pn) {
		double normVec = 0;

		for (int i = 0; i < Pn.length; i++)
			normVec += Math.pow(Pn[i], 2);

		return Math.sqrt(normVec);

	}

	public int outDegreeOf(String vertex) {

		Set<String> arcs = wikiGraph.edgesFrom(vertex);
		return (arcs.size());

	}

	public int inDegreeOf(String vertex) {

		Set<String> arcs = wikiGraph.edgesTo(vertex);
		return (arcs.size());

	}

	//Read the text file and convert it to the Graph
	public void textToGraph(String fileName) {
		String line;
		String[] words;

		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			line = br.readLine();// read the first line which is number of links
									// 100 and ignore it

			while ((line = br.readLine()) != null) {
				words = line.split(" ");

				// add nodes to the Graph
				wikiGraph.addNode(words[0]);
				wikiGraph.addNode(words[1]);

				// Add edge to the graph
				wikiGraph.addEdge(words[0], words[1]);

			}
			br.close();

		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		// wikiGraph.printGraph();
	}

	public int numEdges() {
		return wikiGraph.edgeNum;
	}

	private Map<Integer, Double> sortByComparator(Map<Integer, Double> map, final boolean order) {
		List<Entry<Integer, Double>> list = new LinkedList<Entry<Integer, Double>>(map.entrySet());

		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<Integer, Double>>() {
			public int compare(Entry<Integer, Double> o1, Entry<Integer, Double> o2) {
				if (order) {
					return o1.getValue().compareTo(o2.getValue());
				} else {
					return o2.getValue().compareTo(o1.getValue());
				}
			}
		});

		// Maintaining insertion order with the help of LinkedList
		Map<Integer, Double> sortedMap = new LinkedHashMap<Integer, Double>();
		for (Entry<Integer, Double> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	private Map<String, Integer> sortByComparator2(Map<String, Integer> map, final boolean order) {
		List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(map.entrySet());

		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				if (order) {
					return o1.getValue().compareTo(o2.getValue());
				} else {
					return o2.getValue().compareTo(o1.getValue());
				}
			}
		});

		// Maintaining insertion order with the help of LinkedList
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Entry<String, Integer> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	//Print the Graph
	public void printMap(Map<Integer, Double> map) {
		for (Entry<Integer, Double> entry : map.entrySet()) {
			System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
		}
	}

}
