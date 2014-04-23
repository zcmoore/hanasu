package edu.asu.ser.hanasu.screens;

import java.awt.Dimension;

public enum AspectRatio
{
	x16_9(16, 9),
	x4_3(4, 3);
	
	public static final double acceptableMargin = 0.0;
	public final int width;
	public final int height;
	
	private AspectRatio(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	public double ratio()
	{
		return ((double) width / (double) height);
	}
	
	public Dimension dimension()
	{
		return new Dimension(width, height);
	}
	
	public Dimension formatDimension(Dimension copy)
	{
		double margin = Math.abs(ratio(copy) - ratio());
		Dimension dimension = new Dimension(copy.width, copy.height);
		
		if (margin > acceptableMargin)
		{
			double widthRatio = divide(dimension.width, width);
			double heightRatio = divide(dimension.height, height);
			
			widthRatio -= 1;
			widthRatio = Math.abs(widthRatio);
			
			heightRatio -= 1;
			heightRatio = Math.abs(heightRatio);
			
			if (widthRatio > heightRatio)
			{
				dimension.width /= width;
				dimension.height = height * dimension.width;
				dimension.width *= width;
			}
			else
			{
				dimension.height /= height;
				dimension.width = width * dimension.height;
				dimension.height *= height;
			}
		}
		
		return dimension;
	}
	
	public boolean isMultiple(Dimension dimension)
	{
		return isMultiple(dimension.width, dimension.height);
	}
	
	public boolean isMultiple(int width, int height)
	{
		boolean widthMatch = (width % this.width) == 0;
		boolean heightMatch = (height % this.height) == 0;
		
		return widthMatch && heightMatch;
	}
	
	public static AspectRatio closest(Dimension dimension)
	{
		// Set default values
		double closestRatio = Double.MAX_VALUE;
		AspectRatio closestAR = x16_9;
		
		// Find the Aspect Ratio with the closest ratio value to dimension's
		for (AspectRatio aspectRatio : AspectRatio.values())
		{
			double comparableRatio = aspectRatio.ratio() / ratio(dimension);
			comparableRatio -= 1;
			comparableRatio = Math.abs(comparableRatio);
			
			if (comparableRatio < closestRatio)
			{
				closestAR = aspectRatio;
				closestRatio = comparableRatio;
			}
		}
		
		return closestAR;
	}
	
	private static double ratio(Dimension dimension)
	{
		return ((double) dimension.width / (double) dimension.height);
	}
	
	private static double divide(int a, int b)
	{
		return ((double) a / (double) b);
	}
}
