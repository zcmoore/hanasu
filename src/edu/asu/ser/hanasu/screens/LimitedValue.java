package edu.asu.ser.hanasu.screens;

public class LimitedValue
{
	private double value;
	private double max;
	private double min;
	
	public LimitedValue(double value, double max, double min)
	{
		this.value = value;
		this.max = max;
		this.min = min;
	}
	
	public LimitedValue(double value)
	{
		this(value, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
	}
	
	public double adjust(double delta)
	{
		this.value += delta;
		validate();
		
		return value;
	}
	
	public double adjust(LimitedValue delta)
	{
		this.value += delta.value;
		validate();
		
		return value;
	}
	
	public double add(double delta)
	{
		return adjust(delta);
	}
	
	public double add(LimitedValue delta)
	{
		return adjust(delta);
	}
	
	public double decrease(double delta)
	{
		return adjust(-delta);
	}
	
	public boolean isAtLimit()
	{
		return ((value <= min) || (value >= max));
	}
	
	public double validate()
	{
		if (value < min)
			value = min;
		if (value > max)
			value = max;
		
		return value;
	}
	
	public double getValue()
	{
		return value;
	}
	
	public void setValue(double value)
	{
		this.value = value;
		validate();
	}
	
	public double getMax()
	{
		return max;
	}
	
	public void setMax(double max)
	{
		this.max = max;
		validate();
	}
	
	public double getMin()
	{
		return min;
	}
	
	public void setMin(double min)
	{
		this.min = min;
		validate();
	}
}
