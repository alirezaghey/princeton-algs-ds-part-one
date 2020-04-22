/* *****************************************************************************
 *  Author: Alireza Ghey
 *  Name: PointSet
 *  Date: 22-04-2020
 *  Description: A brute force implementation of a point container that facilitates
 *  inefficient range and nearest methods. The underlying data structure is a
 *  standart java TreeSet.
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> tree;

    // construct an empty set of points
    public PointSET() {
        tree = new TreeSet<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    // number of points in the set
    public int size() {
        return tree.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        this.tree.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return this.tree.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : this.tree) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        List<Point2D> pointsInRange = new ArrayList<>();
        for (Point2D point : tree) {
            if (point.x() >= rect.xmin() && point.x() <= rect.xmax() &&
                    point.y() >= rect.ymin() && point.y() <= rect.ymax()) {
                pointsInRange.add(point);
            }
        }
        return pointsInRange;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        double d = 2.0D;
        Point2D res = null;
        for (Point2D currPoint : tree) {
            double tempD = currPoint.distanceSquaredTo(p);
            if (tempD < d) {
                res = currPoint;
                d = tempD;
            }
        }
        return res;

    }

    // unit testing of the methods (optional)
    public static void main(
            String[] args) {
        // intentionally left blank
    }
}
