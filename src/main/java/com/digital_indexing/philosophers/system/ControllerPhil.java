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

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ControllerPhil {
	
//	---------------------------- ATTRIBUTES ----------------------------------
	
	private static Supervisor supervisor;
	private static ExecutorService executor;
	private static List<Philosopher> list;
	
	
//	---------------------------- METHOD ----------------------------------

	public static void runPhilosophers(int nrOfPhilosophers, int actionTime) 
	{
		supervisor = new Supervisor();
		executor = Executors.newFixedThreadPool(nrOfPhilosophers);
		list = new Vector<Philosopher>();
		for (int i = 0; i < nrOfPhilosophers; i++) 
			list.add(new Philosopher(i, actionTime, supervisor));
		for (Philosopher philosopher : list) {
			executor.submit(philosopher);
		}
	}
	
	
	public static void stopPhilosophers() 
	{
		if (executor == null)
			return;
		if (executor.isShutdown())
			return;
		for (Philosopher philosopher : list) {
			philosopher.cancel(true);
		}
	}

}

