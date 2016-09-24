import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class DigraphSort {
	private static List<Node> nodes;
	private static HashMap<String,List<Node>> edges;
	static {
		nodes = new LinkedList<Node>();
		edges = new HashMap<String,List<Node>>();
	}
	
	
	
	
	public static int G3Dsort(Node x){
		return 0;
		
	}
	
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		int arcs = Integer.parseInt(in.nextLine());
		String[] split;
		List<Node> temp;
		for(int i=0;i<arcs;i++){
			split = in.nextLine().split("\\s");
			temp = edges.get(split[0]);
			if(temp!=null){
				temp.add(new Node(split[1]));
			}else{
				temp = new LinkedList<Node>();
				temp.add(new Node(split[1]));
				edges.put(split[0], temp);
			}
		}
		in.close();
		while(true){}
	}
}
class Node implements Comparable<Node> {
	public String _name;
	public Integer _weight;
	public Node(String name){
		_name = name;
		_weight = -1;
	}
	public Node(String name, int weight){
		_name = name;
		_weight = weight;
	}
	
	public int compareTo(Node arg0) {
		return this._weight-arg0._weight;
	}
	public boolean equals(Object obj){
		if(obj instanceof Node){
			Node node = (Node) obj;
			return node._name.equals(this._name);
		}
		return false;
	}
}