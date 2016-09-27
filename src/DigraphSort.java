import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class DigraphSort {
	private static HashMap<Node,List<Node>> predEdges;
	private static Set<Node> nodes;
	private static StringBuilder output;
	static {
		predEdges = new HashMap<Node,List<Node>>();
		output = new StringBuilder();
		nodes = new HashSet<Node>();
	}
	
	
	public static void G3Dsort(){
		//for all nodes, do G3Dsort on them.
		for(Node n : nodes){
			try {
				G3Dsort(n, 0);
			} catch (NodeSeenException e) {
				//Should be impossible (caught by checking for empty set above)
				e.printStackTrace();
			}
			System.out.println("Node "+n+" Weight "+n._weight);
		}
	}
	
	public static int G3Dsort(Node x, int depth) throws NodeSeenException{
		if(x._seen){throw new NodeSeenException(x);} //check flag
		x._seen=true;
		int max = -1;
		int tmp = -1;
		List<Node> children = predEdges.get(x);
		if(children!=null){
			for(Node y : children){
				try{
					tmp = G3Dsort(y,depth+1);
					if(tmp>max){
						max = tmp;
					}
				}catch(NodeSeenException ne){
					for(Node x1 : ne._nodes){
						if(y.equals(x1)){
							//end recursive backtracking and recalculate max
						}else{
							//collapse nodes
						}
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
			temp = predEdges.get(split[1]); //get list of predecessors
			node2 = new Node(split[0]); //create new node of predecessor
			if(temp!=null){ 
				temp.add(node2);
			}else{
				temp = new LinkedList<Node>();
				temp.add(node2);
				predEdges.put(node1=new Node(split[1]), temp);
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
		_weight = 0;
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