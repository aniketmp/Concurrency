package introduction;
/*
 * stage 2:
 Here We store the locations in a Map, so we start with a thread-safe Map implementation, ConcurrentHashMap. We also store the location 
 using an immutable Point class instead of MutablePoint.
 
 If we had used the original MutablePoint class instead of Point, we would
be breaking encapsulation by letting getLocations publish a reference to mutable
state that is not thread-safe. Notice that we’ve changed the behavior of the
vehicle tracker class slightly; while the monitor version returned a snapshot of
the locations, the delegating version returns an unmodifiable but “live” view of
the vehicle locations. This means that if thread A calls getLocations and thread
B later modifies the location of some of the points, those changes are reflected
in the Map returned to thread A. As we remarked earlier, this can be a benefit
(more up-to-date data) or a liability (potentially inconsistent view of the fleet),
depending on your requirements.
 */

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class ImmutablePoint {
    public final int x, y;

    public ImmutablePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
public class DelegatingVehicleTracker {
	 	private final ConcurrentMap<String, ImmutablePoint> locations;
	    private final Map<String, ImmutablePoint> unmodifiableMap;

	    public DelegatingVehicleTracker(Map<String, ImmutablePoint> immutablePoints) {
	        locations = new ConcurrentHashMap<String, ImmutablePoint>(immutablePoints);
	        unmodifiableMap = Collections.unmodifiableMap(locations);
	    }

	    public Map<String, ImmutablePoint> getLocations() {
	        return unmodifiableMap;
	    }

	    public ImmutablePoint getLocation(String id) {
	        return locations.get(id);
	    }

	    public void setLocation(String id, int x, int y) {
	        if (locations.replace(id, new ImmutablePoint(x, y)) == null)
	            throw new IllegalArgumentException("invalid vehicle name: " + id);
	    }

	    // Alternate version of getLocations 
	    public Map<String, ImmutablePoint> getLocationsAsStatic() {
	        return Collections.unmodifiableMap(
	                new HashMap<String, ImmutablePoint>(locations));
	    }
}
