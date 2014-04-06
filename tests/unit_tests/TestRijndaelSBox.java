package unit_tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.asu.ser.hanasu.encryption.RijndaelSBox;

public class TestRijndaelSBox
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
	public void testSubstitution()
	{
		RijndaelSBox sBox = new RijndaelSBox();
		
		for (int boxIndex = 1; boxIndex < 256; boxIndex++)
		{
			assertNotEquals(
					"Substitution should alter byte value (except for 0)",
					boxIndex, sBox.substitute((byte) boxIndex));
		}
	}
	
	@Test
	public void testInversion()
	{
		RijndaelSBox sBox = new RijndaelSBox();
		
		for (int boxIndex = 0; boxIndex < 256; boxIndex++)
		{
			byte baseValue = (byte) boxIndex;
			byte substitution = sBox.substitute(baseValue);
			byte inverseSubstitution = sBox.invert(substitution);
			assertEquals(
					"Invert should revert the substitute operation",
					baseValue, inverseSubstitution);
		}
	}
}
