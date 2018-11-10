package app.patuhmobile.model;

import java.io.Serializable;

/**
 * Created by Fahmi Hakim on 10/09/2018.
 * for SERA
 */

public class Point implements Serializable{
    double point;

    public Point(double point) {
        this.point = point;
    }

    public double getPoint() {
        return point;
    }
}
