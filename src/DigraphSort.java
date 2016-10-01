import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class DigraphSort {
	private static HashMap<Node,List<Node>> predEdges; //predecessor edges
	private static Set<Node> nodes; //set of nodes 
	private static ArrayList<ArrayList<Node>> sData;
	private static Set<Node> nodesToRemove;
	private static Set<Node> nodesToAdd;
	private static boolean isDAG;
	static {
		predEdges = new HashMap<Node,List<Node>>();
		sData = new ArrayList<ArrayList<Node>>();
		nodes = new TreeSet<Node>();
		nodesToRemove = new HashSet<Node>();
		nodesToAdd = new HashSet<Node>();
		isDAG=true;
	}
	
	
	public static void G3Dsort(){
		System.err.println("new test");
		//for all nodes, do G3Dsort on them.
		ArrayList<Node> arr = new ArrayList<Node>(nodes);
		Node n;
		while(!arr.isEmpty()){
			n = arr.remove(0);
			try {
				G3Dsort(n);
				System.err.println(nodes);
			} catch(NodeSeenException ne){
				
			}
			System.err.println("Nodes before adding:" +nodes);
			System.err.println("Added: " +nodesToAdd);
			System.err.println("Removed: " +nodesToRemove);
			nodes.removeAll(nodesToRemove);
			nodes.addAll(nodesToAdd);
			arr.addAll(nodesToAdd);
			nodesToAdd.clear();
			nodesToRemove.clear();
		}
		if(isDAG){
			System.out.println("DAG");
		}else{
			System.out.println("nonDAG");
		}
		for(Node finished : nodes){
			System.out.println("Node "+finished+" stratum "+finished._weight);
		}
		/*System.out.println(sData.size());
		for(ArrayList<Node> n1 : sData){
			System.out.println(n1.size());
			for(Node n2 : n1){
				System.out.println("Node "+n2+" stratum "+n2._weight);
			}
		}*/
	}
	
	public static int G3Dsort(Node x) throws NodeSeenException{
		if(x._client!=null){x = x._client;}
		if(x._seen){throw new NodeSeenException(x);} //check flag
		x._seen=true;
		int max = -1;
		int tmp = -1;
		List<Node> children = predEdges.get(x);
		if(children!=null){
			NodeSeenException ne = null;
			for(Node y : children){
				if(y.equals(x)){continue;}
				try{
					tmp = G3Dsort(y);
					if(tmp>max){
						max = tmp;
					}
				}catch(NodeSeenException ne2){
					ne = ne2;
					break;
				}
			}
			if(ne!=null){
				//turns out you need to goto line 68, otherwise it repeats for each children node again. RIP.
				isDAG=false;
				boolean equals = false;
				for(Node x1 : ne._nodes){
					if(x.equals(x1)){
						equals = true;
						break;
					}
				}
				if(equals){
					//end recursive backtracking and recalculate max
					Node n = ne.getNewNode();
					List<Node> tempList = new LinkedList<Node>();
					for(Node x1 : ne._nodes){
						x1._client = n;
						List<Node> nodes = predEdges.get(x1);
						if(nodes==null)continue;
						for(Node n2 : nodes){
							n2._seen=false;
						}
						tempList.addAll(nodes);
					}
					for(Node x1 : ne._nodes){
						predEdges.put(x1, tempList);
					}
					for(Node rem : ne._nodes){
						tempList.remove(rem);
					}
					nodesToAdd.add(n);
					nodesToRemove.addAll(ne._nodes);
					predEdges.put(n, tempList);
					tmp=G3Dsort(n);
					if(tmp>max){
						max = tmp;
					}
				}else{
					//add to stack and rethrow
					
					ne.addNodeUpdateStrata(x);
					throw ne;
				}
			}
		}
		x._weight=max+1;
		System.err.println(x+" weight set to "+x._weight);
		x._seen=false;
		return max+1;
		
	}
	
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		int arcs = Integer.parseInt(in.nextLine());
		String[] split;
		List<Node> temp;
		Node node1;
		Node node2;
		for(int i=0;i<arcs;i++){
			split = in.nextLine().split("\\s"); //split by space
			node1 = new Node(split[1]);
			temp = predEdges.get(node1); //get list of predecessors
			node2 = new Node(split[0]); //create new node of predecessor
			if(temp!=null){ 
				temp.add(node2);
			}else{
				temp = new LinkedList<Node>();
				temp.add(node2);
				predEdges.put(node1, temp);
				nodes.add(node1);
			}
			nodes.add(node2);
		}
		in.close();
		G3Dsort();
	}
}
class Node implements Comparable<Node> {
	public TreeSet<Integer> _name;
	public Node _client;
	public Integer _weight;
	public boolean _seen;
	public Node(String name){
		this(name,-1);
	}
	public Node(String name, int weight){
		_name = new TreeSet<Integer>();
		for(String n : name.split("\\s+")){
		_name.add(Integer.parseInt(n));
		}
		_weight = weight;
		_seen = false;
	}
	public Node(TreeSet<Integer> name){
		this(name,-1);
	}
	public Node(TreeSet<Integer> name, int weight){
		_name = name;
		_weight = weight;
		_seen = false;
	}
	public int compareTo(Node arg0) {
		//need to compare weights
		Iterator<Integer> a = _name.iterator();
		Iterator<Integer> b = arg0._name.iterator();
		Integer aN = 0;
		Integer bN = 0;
		while(a.hasNext()&&b.hasNext()){
			aN=a.next();
			bN=b.next();
			if(aN==null){return -1;}
			if(bN==null){return 1;}
			if(a.equals(b)){
				if(!a.hasNext()&&!b.hasNext()){
					return 0;
				}else if(!a.hasNext()){
					return -1;
				}else if(!b.hasNext()){
					return 1;
				}
				continue;
			}
			return aN-bN;
		}
		return aN-bN;
	}
	public String toString(){
		return _name.toString();
	}
	public boolean equals(Object obj){
			Node node = (Node) obj;
			return node._name.equals(this._name);
		
	}
	public int hashCode(){
		return _name.hashCode();
	}
}
class NodeSeenException extends Exception{
	private static final long serialVersionUID = 1L;
	public TreeSet<Node> _nodes;
	public int maxWeight;
	public NodeSeenException(Node x){
		_nodes = new TreeSet<Node>();
		_nodes.add(x);
		maxWeight = x._weight;
	}
	public void addNodeUpdateStrata(Node x){
		boolean valid = _nodes.add(x);
		System.err.println("Exception add node "+x+" "+_nodes+" added? " + valid);
		if(x._weight>maxWeight){
			maxWeight = x._weight;
		}
	}
	public Node getNewNode(){
		TreeSet<Integer> nodename = new TreeSet<Integer>();
		for(Node n : _nodes){
			nodename.addAll(n._name);
		}
		return new Node(nodename,maxWeight);
	}
}
class NotDAGException extends Exception{
	private static final long serialVersionUID = 1L;
	
}
