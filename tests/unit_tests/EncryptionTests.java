package unit_tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestAESBlock.class, TestAESCryptographer.class,
		TestRijndaelField.class, TestRijndaelSBox.class })
public class EncryptionTests
{
	
}
