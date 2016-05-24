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


public class TimeData implements Comparable<TimeData> {
	
	public int philNr;
	public double time;
	
	public TimeData(int philNr, double time) {
		this.time = time;
		this.philNr = philNr;
	}
	
//	------------------------- COMPARE METHODS -----------------------------
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + philNr;
		long temp;
		temp = Double.doubleToLongBits(time);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeData other = (TimeData) obj;
		if (philNr != other.philNr)
			return false;
		if (Double.doubleToLongBits(time) != Double
				.doubleToLongBits(other.time))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(TimeData o) {
		if (o == null)
			return -1;
		if (Double.doubleToLongBits(time) == Double
				.doubleToLongBits(o.time))
			return o.philNr - this.philNr;
		else if (Double.compare(o.time, this.time) < 0 )
			return -1;
		else
			return 1;
	}
	
}