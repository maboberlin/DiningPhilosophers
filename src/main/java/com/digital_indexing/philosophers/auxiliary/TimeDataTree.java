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
package com.digital_indexing.philosophers.auxiliary;

import java.util.TreeSet;

/**
 * Subclass of TreeSet<br>
 * Additional array guarantees fast access to the elements to change the time value of a philosopher.<br>
 * Overriden add method keeps array and tree synchronized.<br>
 * TimeData holds philosophizing time and nr of the philosoph.
 * 
 * @author mabo
 *
 */
public class TimeDataTree extends TreeSet<TimeData> {
	
	private TimeData[] eachPhilData;
	
	public TimeDataTree(int nrOfPhils) {
		super();
		eachPhilData = new TimeData[nrOfPhils];
	}
	
	@Override
	public boolean add(TimeData data) {
		if (data == null)
			return false;
		if (data.philNr < 0 || data.philNr >= eachPhilData.length)
			return false;
		eachPhilData[data.philNr] = data;
		boolean result = super.add(data);
		return result;
	}
	
	
	public double changeTimeValue(int philNr, double delta) 
	{
		if (philNr < 0 || philNr >= eachPhilData.length)
			return -1.0;
		TimeData toChange = eachPhilData[philNr];
		if (remove(toChange)) {
			toChange.time += delta / 1000.0;
			if (add(toChange))
				return toChange.time;
			else
				return -1.0;
		} 
		else
			return -1.0;
		
	}
	
}