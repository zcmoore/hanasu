package edu.asu.ser.hanasu.screens;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;


/*
 * Current Issues With KanaGroup
 *  - Need to set location/bounds of each stroke
 *  - No scale method for panes
 */

@SuppressWarnings("serial")
public class KanaGroup extends JLayeredPane
{
	private ArrayList<KanaStroke> strokes;
	private ScreenManager screenManagerReference;
	
	public KanaGroup(int width, int height, ScreenManager screenManager)
	{
		screenManagerReference = screenManager;
		initializeStrokesAndGenerateKana();
		
		for(int index = 0; index < strokes.size(); index++)
		{
			//strokes.get(index).setPreferredSize(new Dimension(width, height));
			strokes.get(index).setBounds(0, 0, width, height);
			strokes.get(index).setLocation(0, 0);
			
			this.add(strokes.get(index), JLayeredPane.DEFAULT_LAYER);
			
			strokes.get(index).addMouseMotionListener(new KanaMouseMotionListener());
			strokes.get(index).addMouseListener(new KanaMouseListener());
		}
		
		this.setOpaque(false);
		
	}
	
	private void initializeStrokesAndGenerateKana()
	{
		strokes = new ArrayList<>();
		
		String ha1 = "src/Images/Strokes/Ha1.png";
		String ha2 = "src/Images/Strokes/Ha2.png";
		String ha3 = "src/Images/Strokes/Ha3.png";
		String na1 = "src/Images/Strokes/Na1.png";
		String na2 = "src/Images/Strokes/Na2.png";
		String na3 = "src/Images/Strokes/Na3.png";
		String na4 = "src/Images/Strokes/Na4.png";
		String su = "src/Images/Strokes/Su.png";
		
		strokes.add(new KanaStroke(ha1, screenManagerReference));
		strokes.add(new KanaStroke(ha2, screenManagerReference));
		strokes.add(new KanaStroke(ha3, screenManagerReference));
		
		strokes.add(new KanaStroke(na1, screenManagerReference));
		strokes.add(new KanaStroke(na2, screenManagerReference));
		strokes.add(new KanaStroke(na3, screenManagerReference));
		strokes.add(new KanaStroke(na4, screenManagerReference));
		
		strokes.add(new KanaStroke(su, screenManagerReference));	
		
	}
	
	public ArrayList<KanaStroke> getStrokeArray()
	{
		return strokes;
	}
	
	public void setStrokeArray(ArrayList<KanaStroke> strokeArray)
	{
		this.strokes = strokeArray;
	}
	
	private class KanaMouseListener implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
		}

		@Override
		public void mouseEntered(MouseEvent e)
		{
			Point point = e.getPoint();
			for(KanaStroke stroke : strokes)
			{
				if(stroke.checkAlphaValue(point.x, point.y))
					stroke.highlight();
				else
					stroke.dehighlight();
			}
		}

		@Override
		public void mouseExited(MouseEvent e)
		{
			for(KanaStroke stroke : strokes)
			{
				stroke.dehighlight();
			}
		}

		@Override
		public void mousePressed(MouseEvent e)
		{
			Point point = e.getPoint();
			
			for(KanaStroke stroke : strokes)
			{
				if(stroke.checkAlphaValue(point.x, point.y))
				{
					if(stroke == strokes.get(7))
					{
						if(e.getButton() == MouseEvent.BUTTON1)
						{
							System.out.println("Su Stroke");
							stroke.suStrokeConnect();
						}
						else if(e.getButton() == MouseEvent.BUTTON3)
						{
							stroke.onRightMouseClick();
						}
					}
					else
					{
						//TODO change to enum like before
						if(e.getButton() == MouseEvent.BUTTON1)
							stroke.onLeftMouseClick();
						else if(e.getButton() == MouseEvent.BUTTON3)
							stroke.onRightMouseClick();
					}
				
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e)
		{
		}
		
	}
	
	private class KanaMouseMotionListener implements MouseMotionListener
	{
		@Override
		public void mouseMoved(MouseEvent arg0)
		{
			Point point = arg0.getPoint();
			for(KanaStroke stroke : strokes)
			{
				if(stroke.checkAlphaValue(point.x, point.y))
				{
					stroke.highlight();
				}
				else
					stroke.dehighlight();
			}
		}
		
		@Override
		public void mouseDragged(MouseEvent arg0)
		{
		}
	}
}
