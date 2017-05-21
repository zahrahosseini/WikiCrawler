
/// ***************************************************************************
//                         Seyedehzahra Hosseini 
//                         Hamid Bagheri
//******************************************************************************

import java.util.*;
import java.util.Map.Entry;

public final class DGraph<T> implements Iterable<T> {
	private final Map<T, Set<T>> mGraph = new HashMap<T, Set<T>>();
	private final Map<T, Set<T>> iGraph = new HashMap<T, Set<T>>();
	public HashMap<T, Integer> vertexList = new HashMap<T, Integer>();
	public HashMap<Integer, T> vertexNames = new HashMap<Integer, T>();

	int nodeNum = 0;
	int edgeNum = 0;

	public static void main(String[] args) {

		DGraph<String> g1 = new DGraph<>();
		g1.addNode("a1");
		g1.addNode("a2");
		g1.addNode("a3");
		g1.addNode("a4");

		g1.addEdge("a1", "a2");
		g1.addEdge("a1", "a3");

		Set<String> arcs = g1.edgesFrom("a1");
		System.out.println("a1 out degree " + arcs.size());

		System.out.println(g1.nodeIndex("a4"));

		g1.printGraph();

	}

	// add node to the Graph
	public boolean addNode(T node) {
		/* If the node already exists, don't do anything. */
		if (mGraph.containsKey(node))
			return false;

		if (iGraph.containsKey(node))
			return false;

		// create index for Vertex for the Vector P
		vertexList.put(node, mGraph.size());
		vertexNames.put(mGraph.size(), node);

		/* Otherwise, add the node with an empty set of outgoing edges. */
		mGraph.put(node, new HashSet<T>());
		iGraph.put(node, new HashSet<T>());

		return true;
	}

	// add edge to the Graph p-->q
	public void addEdge(T start, T dest) {
		/* Confirm both endpoints exist. */
		if (!mGraph.containsKey(start) || !mGraph.containsKey(dest))
			throw new NoSuchElementException("Both nodes must be in the graph.");

		/* Add the edge. */
		mGraph.get(start).add(dest);
		iGraph.get(dest).add(start);
		edgeNum++;
	}

//	// remove p-->q
//	public void removeEdge(T start, T dest) {
//		/* Confirm both endpoints exist. */
//		if (!mGraph.containsKey(start) || !mGraph.containsKey(dest))
//			throw new NoSuchElementException("Both nodes must be in the graph.");
//
//		mGraph.get(start).remove(dest);
//	}

//	// Does p-->q exists
//	public boolean edgeExists(T start, T end) {
//		/* Confirm both endpoints exist. */
//		if (!mGraph.containsKey(start) || !mGraph.containsKey(end))
//			throw new NoSuchElementException("Both nodes must be in the graph.");
//
//		return mGraph.get(start).contains(end);
//	}

	// edges from p ={q,s} if we have p-->q and p-->s
	public Set<T> edgesFrom(T node) {
		/* Check that the node exists. */
		Set<T> arcs = mGraph.get(node);
		if (arcs == null)
			throw new NoSuchElementException("Source node does not exist.");

		return Collections.unmodifiableSet(arcs);
	}

	public Set<T> edgesTo(T node) {
		/* Check that the node exists. */
		Set<T> arcs = iGraph.get(node);
		if (arcs == null)
			throw new NoSuchElementException("Source node does not exist.");

		return Collections.unmodifiableSet(arcs);
	}

	public Iterator<T> iterator() {
		return mGraph.keySet().iterator();
	}

	public int graphSize() {
		return mGraph.size();
	}

	public int nodeIndex(T node) {
		return vertexList.get(node);
	}

	public int edgeNumer() {
		return edgeNum;
	}

	public String[] nodeNamesLits() {
		String[] result = vertexNames.values().toArray(new String[0]);
		return result;

	}

	public String nodeName(int vertexIndex) {
		return (String) vertexNames.get(vertexIndex);
	}

	public void printGraph() {
		Iterator<Entry<T, Set<T>>> entries = mGraph.entrySet().iterator();
		while (entries.hasNext()) {
			System.out.println(entries.next());
		}

	}
}