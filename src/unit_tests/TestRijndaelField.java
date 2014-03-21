package unit_tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.asu.ser.hanasu.encryption.RijndaelField;

public class TestRijndaelField
{
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}
	
	@Before
	public void setUp() throws Exception
	{
	}
	
	@After
	public void tearDown() throws Exception
	{
	}
	
	@Test
	public void testAddition()
	{
		byte augend = 10;
		byte addend = 1;
		
		byte sum = RijndaelField.add(augend, addend);
		assertEquals("Expected Result: 11 \tReceived: " + sum, 11, sum);
	}
	
}
