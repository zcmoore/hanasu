package edu.asu.ser.hanasu.screens;

public class NewtonianBehaviour
{
	protected LimitedValue value;
	protected LimitedValue velocity;
	protected LimitedValue acceleration;
	
	public NewtonianBehaviour(double value, double velocity, double acceleration)
	{
		this.value = new LimitedValue(value);
		this.velocity = new LimitedValue(velocity);
		this.acceleration = new LimitedValue(acceleration);
	}
	
	public boolean isAtLimit()
	{
		return value.isAtLimit();
	}
	
	public double advance()
	{
		increaseValue(velocity);
		increaseVelocity(acceleration);
		
		return value.getValue();
	}
	
	public double increaseValue(double amount)
	{
		value.add(amount);
		return value.getValue();
	}
	
	public double increaseVelocity(double amount)
	{
		velocity.add(amount);
		return velocity.getValue();
	}
	
	public double increaseAcceleration(double amount)
	{
		acceleration.add(amount);
		return acceleration.getValue();
	}
	
	public double increaseValue(LimitedValue amount)
	{
		return increaseValue(amount.getValue());
	}
	
	public double increaseVelocity(LimitedValue amount)
	{
		return increaseVelocity(amount.getValue());
	}
	
	public double increaseAcceleration(LimitedValue amount)
	{
		return increaseAcceleration(amount.getValue());
	}
	
	public double getValue()
	{
		return value.getValue();
	}
	
	public void setValue(double value)
	{
		this.value.setValue(value);
	}
	
	public double getVelocity()
	{
		return velocity.getValue();
	}
	
	public void setVelocity(double velocity)
	{
		this.velocity.setValue(velocity);
	}
	
	public double getAcceleration()
	{
		return acceleration.getValue();
	}
	
	public void setAcceleration(double acceleration)
	{
		this.acceleration.setValue(acceleration);
	}
	
	public void setMaxValue(double max)
	{
		value.setMax(max);
	}
	
	public void setMinValue(double min)
	{
		value.setMin(min);
	}
	
	public void setMaxVelocity(double max)
	{
		velocity.setMax(max);
	}
	
	public void setMinVelocity(double min)
	{
		velocity.setMin(min);
	}
	
	public void setMaxAcceleration(double max)
	{
		acceleration.setMax(max);
	}
	
	public void setMinAcceleration(double min)
	{
		acceleration.setMin(min);
	}
	
	public void setMinimums(double minValue, double minVelocity,
			double minAcceleration)
	{
		setMinValue(minValue);
		setMinVelocity(minVelocity);
		setMinAcceleration(minAcceleration);
	}
	
	public void setMaximums(double maxValue, double maxVelocity,
			double maxAcceleration)
	{
		setMaxValue(maxValue);
		setMaxVelocity(maxVelocity);
		setMaxAcceleration(maxAcceleration);
	}
	
	@Override
	public String toString()
	{
		return "[value: " + value.getValue() + "\tvelocity: "
				+ velocity.getValue() + "\tacceleration: "
				+ acceleration.getValue() + "]";
	}
}
