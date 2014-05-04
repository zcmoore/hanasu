package unit_tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ EncryptionTests.class, TestAESBlock.class,
		TestAESCryptographer.class, TestRijndaelField.class,
		TestRijndaelSBox.class, TestScreenManager.class })
public class AllTests
{
	
}
