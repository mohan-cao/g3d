import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.TreeSet;

public class DigraphSort {
	public static TreeSet<Node2> nodes;
	public static ArrayList<Node2> copy;
	public static TreeSet<Node2> nodesToRemove;
	public static boolean isDAG;
	static {
		setup(); //for testability, call setup before unit testing
	}
	public static void setup(){
		nodes = new TreeSet<Node2>();
		copy = new ArrayList<Node2>();
		nodesToRemove = new TreeSet<Node2>();
		isDAG=true;
	}
	public static void G3Dsort(){
		//for all nodes, do G3Dsort on them.
		copy = new ArrayList<Node2>(nodes);
		Node2 n;
		while(!copy.isEmpty()){
			n = copy.remove(0);
			try {
				n._weight = G3Dsort(n);
			} catch(NodeSeenException2 ne2){
				//lowest level strongly connected component
				n = ne2.getUpdatedNode();
				n._weight = 0;
				nodes.add(n);
			}
		}
		if(isDAG){
			System.out.println("DAG");
			int digraphMaxWeight = 0;
			for(Node2 nq : nodes){
				if(digraphMaxWeight<nq._weight)digraphMaxWeight=nq._weight;
			}
			System.out.println(digraphMaxWeight+1);
			for(int i=0;i<=digraphMaxWeight;i++){
				Queue<Node2> q = new LinkedList<Node2>();
				Iterator<Node2> it = nodes.iterator();
				Node2 tempnode;
				while(it.hasNext()){
					tempnode = it.next();
					if(tempnode._weight.equals(i)){
						q.add(tempnode);
						it.remove();
					}
				}
				System.out.println(q.size());
				Iterator<Node2> it3 = q.iterator();
				Node2 out;
				while(it3.hasNext()){
					out = it3.next();
					Iterator<Integer> it2 = out._name.iterator();
					System.out.print(it2.next());
					while(it2.hasNext()){
					System.out.print(" "+it2.next());
					}
					System.out.println();
				}
			}
		}else{
			System.out.println("nonDAG");
		}
		
	}
	
	public static int G3Dsort(Node2 x) throws NodeSeenException2{
		if(x._seen){throw new NodeSeenException2(x);} //check flag
		x._seen=true;
		outer:
		for(;;){
			int max = -1;
			int tmp = -1;
			//if(x._weight>-1){return x._weight;} memoization
			TreeSet<Node2> parents = x._parents;
			if(parents!=null && !parents.isEmpty()){
				NodeSeenException2 ne = null;
				Iterator<Node2> it = parents.iterator();
				Node2 y;
				inner:
				while(it.hasNext()){
					try{
					y = it.next();
					}catch(ConcurrentModificationException ce){
						it = parents.iterator();continue inner;
						}
					try{
						tmp = G3Dsort(y);
						if(tmp>max){
							max = tmp;
						}
					}catch(NodeSeenException2 ne2){
						ne = ne2;
						break;
					}
				}
				if(ne!=null){
					isDAG=false;
					boolean equals = false;
					Node2 n = null;
					if(ne._node._name.containsAll(x._name)){
						equals=true;
						n = ne.getUpdatedNode();
						
						continue outer;
					}
					ne.addNodeUpdateStrata(x);
					throw ne;
				}
			}
			x._seen=false;
			return max+1;
		}
	}
	/**
	 * Working, should be.
	 * @param args
	 */
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		int arcs = Integer.parseInt(in.nextLine());
		String[] split;
		Node2 node1;
		Node2 node2;
		HashMap<String,Node2> tempStorage = new HashMap<String,Node2>();
		for(int i=0;i<arcs;i++){
			split = in.nextLine().split("\\s");
			if(split[0].equals(split[1])) isDAG=false;
			node1 = tempStorage.get(split[0]);
			node2 = tempStorage.get(split[1]);
			if(node1==null){node1 = new Node2(split[0]);tempStorage.put(split[0], node1);nodes.add(node1);}
			if(node2==null){node2 = new Node2(split[1]);tempStorage.put(split[1], node2);nodes.add(node2);}
			node1._children.add(node2);
			node2._parents.add(node1);
		}
		in.close();
		G3Dsort();
	}
}
class Node2 implements Comparable<Node2> {
	public TreeSet<Integer> _name;
	public TreeSet<Node2> _parents;
	public TreeSet<Node2> _children;
	public TreeSet<Node2> _clients;
	public Integer _weight;
	public boolean _seen;
	public Node2(String name){
		this(name,-1);
	}
	public Node2(String name, int weight){
		_name = new TreeSet<Integer>();
		_parents = new TreeSet<Node2>();
		_children = new TreeSet<Node2>();
		for(String n : name.split("\\s+")){
			_name.add(Integer.parseInt(n));
		}
		_weight = weight;
		_seen = false;
	}
	public Node2(TreeSet<Integer> name){
		this(name,-1);
	}
	public Node2(TreeSet<Integer> name, int weight){
		_name = name;
		_parents = new TreeSet<Node2>();
		_children = new TreeSet<Node2>();
		_weight = weight;
		_seen = false;
	}
	public boolean mergeNodeWith(Node2 node2){
		if(_clients==null){_clients = new TreeSet<Node2>();}
		boolean done = true;
		if(node2._clients!=null)_clients.addAll(node2._clients);
		_clients.add(node2);
		//_clients.add(this);
		done = _name.addAll(node2._name);
		done = _parents.addAll(node2._parents);
		done = _children.addAll(node2._children);
		for(Node2 n : _parents){
			n._children.removeAll(_clients);
			n._children.add(this);
		}
		for(Node2 n : _children){
			n._parents.removeAll(_clients);
			n._parents.add(this);
		}
		_parents.removeAll(_clients);
		_children.removeAll(_clients);
		_parents.remove(this);
		_children.remove(this);
		if(_weight<node2._weight) _weight = node2._weight;
		return done;
	}
	public int compareTo(Node2 arg0) {
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
			if(aN.compareTo(bN)==0){
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
			Node2 node = (Node2) obj;
			return (node._name.equals(this._name)||super.equals(obj));
	}
	/*public int hashCode(){
		return this._name.hashCode();
	}*/
}
class NodeSeenException2 extends Exception{
	private static final long serialVersionUID = 1L;
	public Node2 _node;
	public int maxWeight;
	public NodeSeenException2(Node2 x){
		x._seen = false;
		_node = x;
		maxWeight = x._weight;
		
	}
	public void addNodeUpdateStrata(Node2 x){
		DigraphSort.nodes.remove(_node);
		DigraphSort.nodes.remove(x);
		_node.mergeNodeWith(x);
		DigraphSort.nodes.add(_node);
		DigraphSort.nodesToRemove.add(x);
		DigraphSort.copy.remove(x);
	}
	public Node2 getUpdatedNode(){
		return _node;
	}
}
