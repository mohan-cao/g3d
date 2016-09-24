import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class DigraphSort {
	private static HashMap<Node,List<Node>> edges;
	private static Set<Node> minimalNodes;
	private static Set<Node> nonMinimalNodes;
	static {
		edges = new HashMap<Node,List<Node>>();
		nonMinimalNodes = new HashSet<Node>();
	}
	
	
	public static void G3Dsort(){
		minimalNodes = new HashSet<Node>(edges.keySet());
		minimalNodes.removeAll(nonMinimalNodes);
		if(minimalNodes.isEmpty()){
			System.out.println("notDAG");
			return;
		}
		for(Node n : minimalNodes){
			try {
				G3Dsort(n, 0);
			} catch (NodeSeenException e) {
				//Should be impossible (caught by checking for empty set above)
				e.printStackTrace();
			}
		}
	}
	
	public static int G3Dsort(Node x, int depth) throws NodeSeenException{
		if(x._seen){throw new NodeSeenException(x);}
		x._seen=true;
		int max = -1;
		int tmp = 0;
		List<Node> children = edges.get(x._name);
		for(Node y : children ){
			tmp = G3Dsort(y,depth+1);
			if(tmp>max){
				max = tmp;
			}
		}
		x._seen=false;
		return max+1;
		
	}
	
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		int arcs = Integer.parseInt(in.nextLine());
		String[] split;
		List<Node> temp;
		Node node2;
		for(int i=0;i<arcs;i++){
			split = in.nextLine().split("\\s");
			temp = edges.get(split[0]);
			node2 = new Node(split[1]);
			if(temp!=null){
				temp.add(node2);
			}else{
				temp = new LinkedList<Node>();
				temp.add(node2);
				edges.put(new Node(split[0]), temp);
			}
			//removes reflexive nodes too, intentional
			nonMinimalNodes.add(node2);
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
	public NodeSeenException(Node x){
		_nodes = new LinkedList<Node>();
		_nodes.add(x);
	}
}