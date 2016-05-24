/**
 * "The Dining Club of Philosophers"
 *
 * Copyright (C) 2016 Matthias Boesinger (boesingermatthias@gmail.com).
 *
 * Licensed under GNU General Public License 3.0 or later.
 * Some rights reserved. See COPYING, AUTHORS.
 *
 * @license GPL-3.0+ <http://spdx.org/licenses/GPL-3.0+>
 */
package com.digital_indexing.philosophers.system;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.digital_indexing.philosophers.auxiliary.TimeData;
import com.digital_indexing.philosophers.enums.ModePhil;
import com.digital_indexing.philosophers.listeners.TimeChangedEvent;
import com.digital_indexing.philosophers.listeners.TimeListener;

/**
 * Ranking of the philosophers.<br>
 * Each time the philosophizing time of a philosopher finished the TimeListener method gets invoked.<br>
 * The time-TextField of the related philosopher gets updated.<br>
 * If the order of the philosophers gets changed in this way, the corresponding Labels (for name) and TextFields (for time) are newly positioned in the Panel array (method: swapPhilosophers)<br> 
 * 
 * @author mabo
 *
 */
public class RankingDialog extends JDialog implements TimeListener {
	
//	------------------------- ATTRIBUTES --------------------------
	
	private JLabel[] nameLabels;
	private JTextField[] timeValues;
	private JPanel[][] panels;
	private int[] rankedPhilosophers;
	
	
//	-------------------------- CONSTRUCTOR ------------------------
	
	public RankingDialog(JFrame mainFrame, ModePhil mode, int nrOfPhilosophers) 
	{
		//initialize dialog
		super(mainFrame, String.format("Ranking (Mode: %s)", mode.toString()), false);
		buildInitialLayout(mode, nrOfPhilosophers);
		pack();
		
		//dialog positioning
		int w = mainFrame.getWidth();
		Point mainLoc = mainFrame.getLocationOnScreen();
		setLocation(mainLoc.x + w, mainLoc.y);
		setResizable(false);
		setVisible(true);
	}

	
//	---------------------------- METHODS --------------------------

	private void buildInitialLayout(ModePhil mode, int nrOfPhilosophers) 
	{
		//main panels
		JPanel main = new JPanel(new BorderLayout(15, 5));
		JPanel left = new JPanel(new GridLayout(nrOfPhilosophers + 1, 1, 0, 5));
		JPanel right = new JPanel(new GridLayout(nrOfPhilosophers + 1, 2, 0, 5));
		main.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
		main.add(left, BorderLayout.WEST);
		main.add(right, BorderLayout.CENTER);
		getContentPane().add(main);
		
		//title
		left.add(new JLabel("Rank"));
		right.add(new JLabel("Philosopher"));
		right.add(new JLabel("Philosophy-Time (sec.)"));
		
		//center
		nameLabels = new JLabel[nrOfPhilosophers];
		timeValues = new JTextField[nrOfPhilosophers];
		panels = new JPanel[nrOfPhilosophers][2];
		rankedPhilosophers = new int[nrOfPhilosophers];
		String baseString = mode.getOther();
		int startCodePoint = ModePhil.NORMALO == mode ? 65 : 64;
		JPanel panel;
		String philosopher;
		
		//left->rank number right->name-label and time-textfield
		for (int i = 0; i < nrOfPhilosophers; i++) {
			rankedPhilosophers[i] = i;
			panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
			panel.add(new JLabel(String.format("%d", i + 1)));
			left.add(panel);
			if (i == 0) {
				nameLabels[i] = new JLabel(mode.getHead());
			}
			else {
				philosopher = String.format("%s %s", baseString, String.valueOf((char) (startCodePoint + i)));
				nameLabels[i] = new JLabel(philosopher);
			}
			timeValues[i] = new JTextField(String.format("%.4f", 0.0), 11);
			timeValues[i].setEditable(false);
			panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
			panel.add(nameLabels[i]);
			right.add(panel);
			panels[i][0] = panel;
			panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
			panel.add(timeValues[i]);
			right.add(panel);
			panels[i][1] = panel;
		}
	}


	@Override
	public void philTimeChanged(TimeChangedEvent e) 
	{
		if (e == null)
			return;
		//get relevant data
		TreeSet<TimeData> tree = e.timeData;
		TimeData[] data = tree.toArray(new TimeData[tree.size()]);
		int nrOfUpdated = e.philNr;
		TimeData philData;
		int philNrData, philNrRow;
		
		//loop over data, sorted in decreasing time-order
		for (int i = data.length - 1; i >= 0; i--) {
			philData = data[i];
			philNrData = philData.philNr;
			philNrRow = rankedPhilosophers[i];
			
			//update the time-textfield of the philosopher whose time has changed
			if (philNrRow == nrOfUpdated) {
				JTextField tf = timeValues[philNrRow];
				tf.setText(String.format("%.4f", e.newTime));
			}
			
			//swap philosophers if the panel order and the data order are inconsistent
			if (philNrRow != philNrData) {
				swapPhilosophers(i, philNrRow, philNrData, nrOfUpdated);
			}
		}
		
		//repaint
		revalidate();
		repaint();
	}


	private void swapPhilosophers(int row, int philNrRow, int philNrData, int nrOfUpdated) 
	{
		// get row nr to swap
		int rowToSwap = 0;
		for (int i = 0; i < rankedPhilosophers.length; i++) {
			if (rankedPhilosophers[i] == philNrData) {
				rowToSwap = i;
				break;
			}
		}
		
		//do swap
		panels[row][0].removeAll();
		panels[row][0].add(nameLabels[philNrData]);
		panels[row][1].removeAll();
		panels[row][1].add(timeValues[philNrData]);
		rankedPhilosophers[row] = philNrData;
		panels[rowToSwap][0].removeAll();
		panels[rowToSwap][0].add(nameLabels[philNrRow]);
		panels[rowToSwap][1].removeAll();
		panels[rowToSwap][1].add(timeValues[philNrRow]);
		rankedPhilosophers[rowToSwap] = philNrRow;
	}
	
}



