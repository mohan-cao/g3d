import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

public class testDigraphSort {
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
	
	@Test
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
	@Test
	public void test2() {
		strIn.append("12\n11 12\n12 13\n21 22\n22 23\n12 23\n12 22\n22 12\n31 23\n23 24\n23 34\n34 41\n41 23\n");
		strIn.submit();
		
		//make expected output cases below
		strOut.append("nonDAG\r\n4\r\n3\r\n11\r\n21\r\n31\r\n1\r\n12 22\r\n2\r\n13\r\n23 34 41\r\n1\r\n24\r\n");
		
		assertEquals(strOut.toString(),_outputstream.toString());
		
	}
	@Test
	public void test3() {
		//make test cases below
		strIn.append("22\n8 4\n9 4\n9 2\n5 2\n10 4\n4 2\n10 6\n4 6\r\n4 11\n2 11\n6 11\n6 1\r\n6 7\n7 3\n11 3\n2 10\n6 9\n3 11\n6 8\n9 5\n1 2\n11 10\n");
		strIn.submit();
		
		//make expected output cases below
		strOut.append("nonDAG\r\n1\r\n1\r\n1 2 3 4 5 6 7 8 9 10 11");
		
		assertEquals(strOut.toString(),_outputstream.toString());
		
	}
	@Test
	public void test4() {
		//make test cases below
		strIn.append("10\r11 12\r12 13\r21 22\r22 23\r12 23\r31 23\r23 24\r23 34\r41 22\r41 34\r");
		strIn.submit();
		
		//make expected output cases below
		strOut.append("DAG\r\n4\r\n4\r\n11\r\n21\r\n31\r\n41\r\n2\r\n12\r\n22\r\n2\r\n13\r\n23\r\n2\r\n24\r\n34\r\n");
		
		assertEquals(strOut.toString(),_outputstream.toString());
		
	}
	@Test
	public void testNodeComparator(){
		Node n = new Node("2");
		Node n2 = new Node("2");
		assertEquals(0,n.compareTo(n2));
	}
	@Test
	public void testTreeSetSame(){
		Node n = new Node("2");
		TreeSet<Node> n2 = new TreeSet<Node>();
		n2.add(n);
		n2.first()._client = new Node("1");
		assertEquals(n._client,n2.first()._client);
	}
}

/**
 * a nice decorator
 * @author me
 *
 */
class InputStuff {
	private StringBuffer _str;
	public InputStuff(StringBuffer str){
		_str = str;
	}
	public void append(String str){
		_str.append(str);
	}
	public void submit(){
		InputStream in = new ByteArrayInputStream(_str.toString().getBytes());
		System.setIn(in);
		DigraphSort3.main(null);
	}
}