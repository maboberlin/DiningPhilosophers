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

public class Supervisor {
	
//	--------------------------- ATTRIBUTES -------------------------------
	
	private Object[] monitors;
	private int cnt = 0;
	private int nrOfPhilosophers;
	
	
//	--------------------- CONSTRUCTOR -----------------------------------
	
	public Supervisor() 
	{
		nrOfPhilosophers = ModelPhil.getSingleInstance().getNrOfPhilosophers();
		monitors = new Object[ nrOfPhilosophers ];
		for (int i = 0; i < monitors.length; i++) {
			monitors[i] = new Object();
		}
	}
	
	
//	----------------------------- METHOD ---------------------------------
	
	synchronized public void allowToSit() throws InterruptedException {
		if (cnt > nrOfPhilosophers - 2) 
			wait();
		cnt++;
	}
	
	synchronized public void allowToLeave() {
		cnt--;
		notify();
	}
	
	public Object getMonitor(int nr) {
		return monitors[nr];
	}
}
