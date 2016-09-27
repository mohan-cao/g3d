import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class DigraphSort {
	private static HashMap<Node,List<Node>> predEdges;
	private static Set<Node> nodes;
	private static ArrayList<ArrayList<ArrayList<Node>>> sData;
	private static boolean currentNodeUpdated;
	static {
		predEdges = new HashMap<Node,List<Node>>();
		sData = new ArrayList<ArrayList<ArrayList<Node>>>();
		nodes = new LinkedHashSet<Node>();
		currentNodeUpdated=false;
	}
	
	
	public static void G3Dsort(){
		//for all nodes, do G3Dsort on them.
		Iterator<Node> it = nodes.iterator();
		Node n;
		while(it.hasNext()){
			n = it.next();
			try {
				G3Dsort(n);
				System.out.println("Node"+n+" has strata: "+n._weight);
			} catch(NodeSeenException ne){
				
			}
		}
	}
	
	public static int G3Dsort(Node x) throws NodeSeenException{
		if(x._seen){throw new NodeSeenException(x);} //check flag
		x._seen=true;
		int max = -1;
		int tmp = -1;
		List<Node> children = predEdges.get(x);
		if(children!=null){
			for(Node y : children){
				try{
					tmp = G3Dsort(y);
					if(tmp>max){
						max = tmp;
					}
				}catch(NodeSeenException ne){
					ne.printStackTrace();
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
							List<Node> nodes = predEdges.get(x1);
							System.err.println(nodes);
							if(nodes==null)continue;
							for(Node n2 : nodes){
								n2._seen=false;
							}
							tempList.addAll(nodes);
							tempList.removeAll(ne._nodes);
							predEdges.remove(x1);
							predEdges.put(n, tempList);
						}
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
		}
		x._weight=max+1;
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
	public String _name;
	public Integer _weight;
	public boolean _seen;
	public Node(String name){
		_name = name;
		_weight = -1;
		_seen = false;
	}
	public Node(String name, int weight){
		_name = name;
		_weight = weight;
		_seen = false;
	}
	
	public int compareTo(Node arg0) {
		return this._weight-arg0._weight;
	}
	public String toString(){
		return _name;
	}
	public boolean equals(Object obj){
		if(obj instanceof Node){
			Node node = (Node) obj;
			return node._name.equals(this._name);
		}
		return false;
	}
	public int hashCode(){
		return _name.hashCode();
	}
}
class NodeSeenException extends Exception{
	private static final long serialVersionUID = 1L;
	public List<Node> _nodes;
	public int minWeight;
	public NodeSeenException(Node x){
		_nodes = new LinkedList<Node>();
		_nodes.add(x);
		minWeight = x._weight;
	}
	public void addNodeUpdateStrata(Node x){
		_nodes.add(x);
		if(x._weight<minWeight){
			minWeight = x._weight;
		}
	}
	public void updateCollapsedNodes(Map<Node,List<Node>> edges){
		for(Node n : _nodes){
			edges.remove(n);
		}
	}
	public Node getNewNode(){
		StringBuilder str = new StringBuilder();
		for(Node n : _nodes){
			str.append(n+" ");
		}
		str.deleteCharAt(str.length()-1);
		return new Node(str.toString(),minWeight);
	}
}
class NotDAGException extends Exception{
	private static final long serialVersionUID = 1L;
	
}