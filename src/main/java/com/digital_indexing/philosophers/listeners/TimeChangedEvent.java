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
package com.digital_indexing.philosophers.listeners;

import java.util.EventObject;
import java.util.TreeSet;

import com.digital_indexing.philosophers.auxiliary.TimeData;


public class TimeChangedEvent extends EventObject {
	
	public int philNr;
	public double newTime;
	public TreeSet<TimeData> timeData;

	public TimeChangedEvent(Object source, int philNr, double newTime, TreeSet<TimeData> timeData) {
		super(source);
		this.timeData = timeData;
		this.philNr = philNr;
		this.newTime = newTime;
	}

}
