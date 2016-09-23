import java.util.LinkedList;
import java.util.List;

public class DigraphSort {
	private static List<Node> nodes;
	static {
		nodes = new LinkedList<Node>();
	}
	
	class Node implements Comparable<Node> {
		public int _weight;
		
		public int compareTo(Node arg0) {
			return 0;
		}
		
	}
}
