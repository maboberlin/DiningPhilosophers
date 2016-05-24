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

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import com.digital_indexing.philosophers.auxiliary.TimeData;
import com.digital_indexing.philosophers.auxiliary.TimeDataTree;
import com.digital_indexing.philosophers.enums.StatePhil;
import com.digital_indexing.philosophers.listeners.ModelListener;
import com.digital_indexing.philosophers.listeners.PhilChangedEvent;
import com.digital_indexing.philosophers.listeners.PhilNumberSetEvent;
import com.digital_indexing.philosophers.listeners.PhilNumberSetListener;
import com.digital_indexing.philosophers.listeners.SticksChangedEvent;
import com.digital_indexing.philosophers.listeners.TimeChangedEvent;
import com.digital_indexing.philosophers.listeners.TimeListener;

/**
 * Model with data for the current state of the simulation<br>
 * <p>
 * - philosophers: array with the states of all philosophers <br>
 * - sticks: array for the sticks. In between two philosophers are three sticks of which one is set at each time. <br>
 * - timeData: tree ordered by the absolute philosophizing time for each philosopher. each value is connected to the nr. of the philosopher <br>
 * <p>
 * Whenever a state of a philosopher, or a stick position, or a time value changed listeners get fired.
 * 
 * @author mabo
 *
 */
public class ModelPhil implements PhilNumberSetListener {
	
//	---------------------------- ATTRIBUTES ----------------------------------
	
	private static ModelPhil instance;
	
	private ModelListener modelListener;
	private TimeListener timeListener;
	
	private StatePhil[] philosophers;
	private boolean[] sticks;
	private TimeDataTree timeData; 
	private int nrOfPhilosophers;
	
	
//	---------------------------- CONSTRUCTOR ----------------------------------
	
	private ModelPhil() {
	}
	
	public static ModelPhil getSingleInstance() { 
		if (instance == null)
			instance = new ModelPhil();
		return instance;
	}
	
	
//	---------------------------- METHODS ----------------------------------
		
	public void setModelListener(ModelListener listener) {
		this.modelListener = listener;
	}
	
	public void setTimeListener(TimeListener listener) {
		this.timeListener = listener;
	}
	
	
	@Override
	public void nrOfPhilosophersSet(PhilNumberSetEvent e) {
		this.nrOfPhilosophers = e.nrOfPhilosophers;
		reset();
	}
	
	
	/**
	 * set initial position
	 */
	public void reset() {
		philosophers = new StatePhil[ nrOfPhilosophers ];
		sticks = new boolean[ nrOfPhilosophers * 3 ];
		timeData = new TimeDataTree(nrOfPhilosophers);
		// value initialization
		TimeData td;
		for (int i = 0; i < philosophers.length; i++) { 
			philosophers[i] = StatePhil.WAITING;
			td = new TimeData(i, 0.0);
			timeData.add(td);
		}
		for (int i = 0; i < sticks.length; i++) {
			if (i % 3 == 1)
				sticks[i] = true;
			else
				sticks[i] = false;
		}
	}
	
	/**
	 * set state of the philosopher nr x. fire event.
	 * @param philNr
	 * @param state
	 */
	public void setPhilState(int philNr, StatePhil state) throws InvocationTargetException, InterruptedException 
	{
		philosophers[philNr] = state;
		PhilChangedEvent e = new PhilChangedEvent(this, philosophers[philNr], philNr);
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				modelListener.philStateChanged(e);	
			}
		});
	}
	
	
	/**
	 * change the values of the passed sticks (this is: swap values of the neighboring sticks true->false and vice versa. fire event. 
	 * @param first stick that becomes set
	 * @param v1
	 * @param second stick that becomes unset
	 * @param v2
	 */
	public void setSticks(int first, boolean v1, int second, boolean v2) throws InvocationTargetException, InterruptedException 
	{
		sticks[first] = v1;
		sticks[second] = v2;
		SticksChangedEvent e = new SticksChangedEvent(this, first, sticks[first], second, sticks[second]);
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				modelListener.sticksChanged(e);
			}
		});
	}
	
	/**
	 * change time in time data tree. fire event.
	 * @param philNr
	 * @param timeDelta
	 */
	synchronized public void changePhilTime(int philNr, double timeDelta) throws InvocationTargetException, InterruptedException
	{
		double newTime = timeData.changeTimeValue(philNr, timeDelta);
		TimeChangedEvent evt = new TimeChangedEvent(this, philNr, newTime, timeData);
		if (timeListener == null)
			return;
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				timeListener.philTimeChanged(evt);
			}
		});
	}
	
	
//	----------------------------- GETTERS -----------------------------
	
	public int getNrOfPhilosophers() {
		return nrOfPhilosophers;
	}
	
	public boolean getStickValue(int nr) {
		return sticks[nr];
	}

}
