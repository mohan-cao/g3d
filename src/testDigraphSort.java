import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

public class testDigraphSort {
	public InputStuff strIn;
	public StringBuffer strOut;
	public ByteArrayOutputStream _outputstream;
	
	/**
	 * sets up stuff
	 * @author meme
	 */
	@Before
	public void setUp(){
		strIn = new InputStuff(new StringBuffer());
		strOut = new StringBuffer();
		ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
		_outputstream = outputstream;
		System.setOut(new PrintStream(outputstream));
	}
	
	@Test
	public void test() {
		//make test cases below
		strIn.append("1\n");
		strIn.append("1 1\n");
		strIn.submit();
		
		//make expected output cases below
		strOut.append("nonDAG\n");
		strOut.append("1\n");
		strOut.append("1\n");
		strOut.append("1 1\n");
		
		assertEquals(strOut.toString(),_outputstream.toString());
		
	}
	@Test
	public void test2() {
		//make test cases below
		strIn.append("12\n11 12\n12 13\n21 22\n22 23\n12 23\n12 22\n22 12\n31 23\n23 24\n23 34\n34 41\n41 23\n");
		strIn.submit();
		
		//make expected output cases below
		strOut.append("nonDAG\n4\n3\n11\n21\n31\n1\n12 22\n2\n13\n23 34 41\n1\n24\n");
		
		assertEquals(strOut.toString(),_outputstream.toString());
		
	}
	@Test
	public void test3() {
		//make test cases below
		strIn.append("22\n8 4\n9 4\n9 2\n5 2\n10 4\n4 2\n10 6\n4 6\n4 11\n2 11\n6 11\n6 1\n6 7\n7 3\n11 3\n2 10\n6 9\n3 11\n6 8\n9 5\n1 2\n11 10\n");
		strIn.submit();
		
		//make expected output cases below
		strOut.append("nonDAG\n1\n1\n1 2 3 4 5 6 7 8 9 10 11");
		
		assertEquals(strOut.toString(),_outputstream.toString());
		
	}
	@Test
	public void test4() {
		//make test cases below
		strIn.append("10\n11 12\n12 13\n21 22\n22 23\n12 23\n31 23\n23 24\n23 34\n41 22\n41 34\n");
		strIn.submit();
		
		//make expected output cases below
		strOut.append("DAG\n4\n4\n11\n21\n31\n41\n2\n12\n22\n2\n13\n23\n2\n24\n34\n");
		
		assertEquals(strOut.toString(),_outputstream.toString());
		
	}
	@Test
	public void testNodeComparator(){
		Node n = new Node("2");
		Node n2 = new Node("2");
		assertEquals(0,n.compareTo(n2));
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
		DigraphSort.main(null);
	}
}