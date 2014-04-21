/**
 * 
 */
package com.effectivejava.basics;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author AM
 *
 */
public class GUIPrimitive {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}
	
	private class PrimitiveFrame extends JFrame {
		public PrimitiveFrame() {
			
		}
	}
	
	private class PrimitivePanel extends JPanel {
		public PrimitivePanel() {
			
		}
		
		@Override
		public void paint(Graphics g) {
			g.setColor(Color.RED);
			g.drawRect(10, 10, 100, 100);
		}
	}
}