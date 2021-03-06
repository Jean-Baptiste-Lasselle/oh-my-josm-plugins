// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.fr.cadastre.wms;

import java.io.Serializable;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.coor.EastNorth;

public class EastNorthBound implements Serializable {

    private static final long serialVersionUID = 8451650309216472069L;

    public EastNorth min, max;
    public EastNorthBound(EastNorth min, EastNorth max) {
        this.min = min;
        this.max = max;
    }

    public boolean contains(EastNorth eastNorth) {
        if (eastNorth.east() < min.east() || eastNorth.north() < min.north())
            return false;
        if (eastNorth.east() > max.east() || eastNorth.north() > max.north())
            return false;
        return true;
    }

    public EastNorthBound interpolate(EastNorthBound en2, double proportion) {
        EastNorthBound enb = new EastNorthBound(this.min.interpolate(en2.min, proportion),
                this.max.interpolate(en2.max, proportion));
        return enb;
    }

    public Bounds toBounds() {
        return new Bounds(Main.getProjection().eastNorth2latlon(min), Main.getProjection().eastNorth2latlon(max));
    }

    @Override public String toString() {
        return "EastNorthBound[" + min.east() + "," + min.north() + "," + max.east() + "," + max.north() + "]";
    }
}

