package unit_tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.asu.ser.hanasu.screens.ScreenManager;

public class TestScreenManager
{
	@Test
	public void test()
	{
		ScreenManager manager = ScreenManager.createManager();
		manager.transition(ScreenManager.ScreenType.MAIN);
		// wait for transition to finish
		while (manager.isTransitioning())
			System.out.println("true");
		for (int i = 0; i < 10000; i++);
		assertEquals(manager.getMainScreen(), manager.getCurrentScreen());
		
		manager.transition(ScreenManager.ScreenType.CHANNEL);
		// wait for transition to finish
		while (manager.isTransitioning())
			System.out.println("true2");
		for (int i = 0; i < 10000; i++);
		assertEquals(manager.getChannelScreen(), manager.getCurrentScreen());
		
		manager.transition(ScreenManager.ScreenType.CHAT);
		// wait for transition to finish
		while (manager.isTransitioning())
			System.out.println("true3");
		for (int i = 0; i < 10000; i++);
		assertEquals(manager.getChatScreen(), manager.getCurrentScreen());
	}
	
}
