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

import javax.swing.SwingWorker;

import com.digital_indexing.philosophers.auxiliary.Logger4Philosophers;
import com.digital_indexing.philosophers.enums.StatePhil;

/**
 * Class represents a philosopher and implements SwingWorker.<br>
 * <p>
 * Each philosopher runs in a infinite loop.<br>
 * Order:<br>
 * <ol>
 * <li>philosophizing</li>
 * <li>set time value in model</li>
 * <li>try to take sticks. each access to the stick array runs in a synchronized phase</li>
 * <li>eating</li>
 * <li>drop sticks</li>
 * <li>leave of table. managed by supervisor to prevent all philosophers sitting at the table to prevent deadlocks</li>
 * </ol>
 * @author mabo
 *
 */
public class Philosopher extends SwingWorker<Void, Void> {
	
	
//	---------------------------- ATTRIBUTES ----------------------------------
	
	private ModelPhil model;
	private Supervisor superVisor;
	private int actionTime;
	private int philNr;
	private int rightAreaNr, leftAreaNr;
	
	
//	---------------------------- CONSTRUCTOR ----------------------------------
	
	public Philosopher(int philNr, int actionTime, Supervisor sv) 
	{
		//get model
		model = ModelPhil.getSingleInstance();
		
		//set attriubutes
		this.actionTime = actionTime;
		superVisor = sv;	
		int nrOfPhils = model.getNrOfPhilosophers();
		this.philNr = philNr;
		
		//claculate numbers for left and right are of this philosoph
		leftAreaNr = philNr - 1 < 0 ? nrOfPhils - 1 : philNr - 1;
		rightAreaNr = philNr;
	}

	
	@Override
	protected Void doInBackground() throws Exception 
	{
		while (true) 
		{
			try {
				// philosophing
				model.setPhilState(philNr, StatePhil.PHILOSOPHIZING);
				long philTime = Math.round(Math.random() * actionTime);
				Thread.sleep(philTime);
				model.changePhilTime(philNr, philTime);
						
				// waiting and try to take sticks
				model.setPhilState(philNr, StatePhil.WAITING);
				superVisor.allowToSit();
				takeStick(rightAreaNr);
				takeStick(leftAreaNr);
				
				//eat
				model.setPhilState(philNr, StatePhil.EATING);
				Thread.sleep(Math.round(Math.random() * actionTime));
				
				// put sticks and leave
				putStick(rightAreaNr);
				model.setPhilState(philNr, StatePhil.WAITING);
				putStick(leftAreaNr);	
				superVisor.allowToLeave();
			}
			catch (InterruptedException e) {
				Logger4Philosophers.logger4Philosophers.error(e.getMessage());
				return null;
			}
		}
	}
	
	
	void takeStick (int areaNr) throws InterruptedException, InvocationTargetException 
	{
		//block monitor with number of the are left or right
		synchronized (superVisor.getMonitor(areaNr)) {
			//case 1: left
			if (areaNr == philNr) {
				if (!model.getStickValue(areaNr * 3 + 1))
					superVisor.getMonitor(areaNr).wait();
				model.setSticks(areaNr * 3, true, areaNr * 3 + 1, false);
			}
			//case 2: right
			else {
				if (!model.getStickValue(areaNr * 3 + 1))
					superVisor.getMonitor(areaNr).wait();
				model.setSticks( areaNr * 3 + 2, true, areaNr * 3 + 1, false);
			}
		}
	}
	
	
	void putStick (int areaNr) throws InvocationTargetException, InterruptedException 
	{
		//block monitor with number of the are left or right
		synchronized (superVisor.getMonitor(areaNr)) {
			//case 1: left
			if (areaNr == philNr) {
				model.setSticks(areaNr * 3 + 1, true, areaNr * 3, false);
			}
			//case 2: right
			else {
				model.setSticks(areaNr * 3 + 1, true, areaNr * 3 + 2, false);
			}
			superVisor.getMonitor(areaNr).notify();
		}
	}
	
}
