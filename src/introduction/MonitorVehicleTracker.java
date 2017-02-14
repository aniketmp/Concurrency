package introduction;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
/*
  Here even though MutablePoint is not thread-safe, the tracker class is. Neither the
map nor any of the mutable points it contains is ever published. When we need to
return vehicle locations to callers, the appropriate values are copied using either
the MutablePoint copy constructor or deepCopy, which creates a new Map whose
values are copies of the keys and values from the old Map.

 */
class MutablePoint 
{
	
	public int x, y;
	
	
	public MutablePoint() {
		x = 0;
		y = 0;
	}

	public MutablePoint(MutablePoint p) {
		this.x = p.x;
		this.y = p.y;
	}
}

public class MonitorVehicleTracker 
{
	private final Map<String, MutablePoint> locations;

	public MonitorVehicleTracker(Map<String, MutablePoint> locations) 
	{
		this.locations = deepCopy(locations);
	}

	public synchronized Map<String, MutablePoint> getLocations() 
	{
		return deepCopy(locations);
	}

	public synchronized MutablePoint getLocation(String id) 
	{
		MutablePoint loc = locations.get(id);
		return loc == null ? null : new MutablePoint(loc);
	}

	public synchronized void setLocation(String id, int x, int y) 
	{
		MutablePoint loc = locations.get(id);
		if (loc == null)
			throw new IllegalArgumentException("No such ID: " + id);
		loc.x = x;
		loc.y = y;
	}

	private static Map<String, MutablePoint> deepCopy(Map<String, MutablePoint> m) 
	{
		Map<String, MutablePoint> result = new HashMap<String, MutablePoint>();

		for (String id : m.keySet())
			result.put(id, new MutablePoint(m.get(id)));

		return Collections.unmodifiableMap(result);
	}
	
}
/*
  Stage 1:This implementation maintains thread safety in part by copying mutable data
before returning it to the client. This is usually not a performance issue, but could
become one if the set of vehicles is very large.4 Another consequence of copying
the data on each call to getLocation is that the contents of the returned collection
do not change even if the underlying locations change. Whether this is good or
bad depends on your requirements. It could be a benefit if there are internal
consistency requirements on the location set, in which case returning a consistent
snapshot is critical, or a drawback if callers require up-to-date information for
each vehicle and therefore need to refresh their snapshot more often.

We will see the stage 2 of MonitorVehicalTracker in DelegatingVehicleTracker.
 */
