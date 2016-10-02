import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

public class testDigraphSort2 {
	public InputStuff2 strIn;
	public StringBuffer strOut;
	public ByteArrayOutputStream _outputstream;
	
	/**
	 * sets up stuff
	 * @author meme
	 */
	@Before
	public void setUp(){
		DigraphSort3.setup();
		strIn = new InputStuff2(new StringBuffer());
		strOut = new StringBuffer();
		ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
		_outputstream = outputstream;
		System.setOut(new PrintStream(outputstream));
	}
	
	//@Test
	public void test() {
		//make test cases below
		strIn.append("1\r\n");
		strIn.append("1 1\r\n");
		strIn.submit();
		
		//make expected output cases below
		strOut.append("nonDAG\r\n");
		strOut.append("1\r\n");
		strOut.append("1\r\n");
		strOut.append("1\r\n");
		
		assertEquals(strOut.toString(),_outputstream.toString());
		
	}
	//@Test
	public void test2() {
		strIn.append("12\n11 12\n12 13\n21 22\n22 23\n12 23\n12 22\n22 12\n31 23\n23 24\n23 34\n34 41\n41 23\n");
		strIn.submit();
		
		//make expected output cases below
		strOut.append("nonDAG\r\n4\r\n3\r\n11\r\n21\r\n31\r\n1\r\n12 22\r\n2\r\n13\r\n23 34 41\r\n1\r\n24\r\n");
		
		assertEquals(strOut.toString(),_outputstream.toString());
		
	}
	//@Test
	public void test3() {
		//make test cases below
		strIn.append("22\n8 4\n9 4\n9 2\n5 2\n10 4\n4 2\n10 6\n4 6\r\n4 11\n2 11\n6 11\n6 1\r\n6 7\n7 3\n11 3\n2 10\n6 9\n3 11\n6 8\n9 5\n1 2\n11 10\n");
		strIn.submit();
		
		//make expected output cases below
		strOut.append("nonDAG\r\n1\r\n1\r\n1 2 3 4 5 6 7 8 9 10 11\r\n");
		
		assertEquals(strOut.toString(),_outputstream.toString());
		
	}
	//@Test
	public void test4() {
		//make test cases below
		strIn.append("10\n11 12\n12 13\n21 22\n22 23\n12 23\n31 23\n23 24\n23 34\n41 22\n41 34\n");
		strIn.submit();
		
		//make expected output cases below
		strOut.append("DAG\r\n4\r\n4\r\n11\r\n21\r\n31\r\n41\r\n2\r\n12\r\n22\r\n2\r\n13\r\n23\r\n2\r\n24\r\n34\r\n");
		
		assertEquals(strOut.toString(),_outputstream.toString());
		
	}
	@Test
	public void test5() {
		//make test cases below
		strIn.append("14\n1 2\n2 3\n2 5\n2 6\n3 4\n3 7\r\n4 3\n4 8\n5 1\n5 6\n6 7\n7 6\n8 7\n8 4\n");
		strIn.submit();
		
		//make expected output cases below
		strOut.append("nonDAG\r\n3\r\n1\r\n1 2 5\r\n1\r\n3 4 8\r\n1\r\n6 7\r\n");
		
		assertEquals(strOut.toString(),_outputstream.toString());
		
	}
	@Test
	public void testNodeComparator(){
		Node2 n = new Node2("2");
		Node2 n2 = new Node2("2");
		assertEquals(0,n.compareTo(n2));
		Node2 n3 = new Node2("4 6 8");
		assertEquals(n3._name.size(),3);
		Node2 n4 = new Node2("4 6 8 9");
		assertEquals(n4._name.size(),4);
		assertEquals(n3.compareTo(n4),-1);
	}
	@Test
	public void testEquals(){
		Node2 n = new Node2("4 6 8 9");
		Node2 n2 = new Node2("4 6 8");
		assertNotEquals(n,n2);
		Node2 n3 = new Node2("4");
		Node2 n4 = new Node2("4");
		assertEquals(n3,n4);
	}
	@Test
	public void testTreeSetAdd(){
		Node2 n = new Node2("1 2 7");
		TreeSet<Node2> n2 = new TreeSet<Node2>();
		n2.add(n);
		Node2 n3 = new Node2("1 2 6 7");
		assertTrue(n2.add(n3));
	}
	@Test
	public void testTreeSetSame(){
		Node2 n = new Node2("2");
		TreeSet<Node2> n2 = new TreeSet<Node2>();
		n2.add(n);
		Node2 n3 = new Node2("2 4 6");
		n2.add(n3);
		n._name.add(4);
		assertTrue(n2.contains(n));
		assertTrue(n2.contains(n3));
	}
	@Test
	public void testTreeSetRemove(){
		Node2 n = new Node2("4");
		n._name.add(6);n._name.add((Integer)8);
		Node2 n2 = new Node2("1");
		n2.mergeNodeWith(n);
		TreeSet<Node2> nodes = new TreeSet<Node2>();
		nodes.add(n);
		nodes.add(n2);
		nodes.remove(new Node2("4"));
		assertTrue(nodes.remove(new Node2("1 4 6 8")));
	}
	//@Test
	public void testMergeCorrect(){
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
		Node2 n = new Node2("2");
		n._children.add(new Node2("4"));
		n._children.add(new Node2("3"));
		n._parents.add(new Node2("0"));
		n._parents.add(new Node2("2"));
		Node2 n2 = new Node2("3 4 6");
		n2._children.add(new Node2("5"));
		n2._children.add(new Node2("2"));
		n2._parents.add(new Node2("9"));
		n2._parents.add(new Node2("3"));
		n.mergeNodeWith(n2);
		System.out.println(n._children);
		System.out.println(n._clients);
		assertEquals(n._children.size(),2);
		assertEquals(n._parents.size(),2);
		assertEquals(n._children.size(),2);
		assertEquals(n._parents.size(),2);
		assertEquals(n._name.toString(),"[2, 3, 4, 6]");
	}
}

/**
 * a nice decorator
 * @author me
 *
 */
class InputStuff2 {
	private StringBuffer _str;
	public InputStuff2(StringBuffer str){
		_str = str;
	}
	public void append(String str){
		_str.append(str);
	}
	public void submit(){
		InputStream in = new ByteArrayInputStream(_str.toString().getBytes());
		System.setIn(in);
		DigraphSort.main(null);
	}
}