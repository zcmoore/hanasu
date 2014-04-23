package edu.asu.ser.hanasu.screens;

/**
 * To be used with a graphic component, such as a JPanel, to assist in the
 * transition between screens, as defined by ClientContainer.
 * 
 * @author Moore, Zachary
 * @see ClientContainer
 * 
 */
public interface Managable
{
	/**
	 * Protocol to activate before exiting a screen. This should close all local
	 * streams and processes that belong only to this screen.
	 * 
	 * Note: this method works in conjunction with {@link #onExit()}, which is
	 * called after the transition has been completed.
	 */
	public void prepareToExit();
	
	/**
	 * Protocol to activate before entering a screen. Open necessary streams if
	 * applicable.
	 * 
	 * Note: this method works in conjunction with {@link #onEnter()}, which is
	 * called after the transition has been completed.
	 */
	public void prepareToEnter();
	
	/**
	 * Protocol to activate directly after entering this screen. This method
	 * should be called after the former screen is properly exited (see
	 * {@link #onExit()}).
	 */
	public void onEnter();
	
	/**
	 * Protocol to activate directly after exiting this screen.
	 */
	public void onExit();
	
	/**
	 * Restores this screen to its default state.
	 */
	public void reset();
}
