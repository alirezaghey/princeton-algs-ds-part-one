/* *****************************************************************************
 *  Author: Alireza Ghey
 *  Name: KdTree
 *  Date: 22-04-2020
 *  Description: A KD Tree that works upon an internal 2D Tree to facilitate
 *  fast range search and nearest point calculations on arbitrary points
 *  in a 2D plane.
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class KdTree {
    // construct an empty set of points
    private final TwoDTree tree;

    public KdTree() {
        tree = new TwoDTree();
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
        tree.insert(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return tree.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        tree.draw();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        return tree.rangeSearch(rect);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return tree.nearest(p);
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        // intentionally left blank
    }

    private class TwoDTree {
        private class Node {
            private final Point2D point;
            private final boolean vertical;
            private Node left;
            private Node right;

            public Node(Point2D p, boolean v) {
                point = p;
                vertical = v;
                left = null;
                right = null;
            }
        }

        private Node root;
        private int sz;

        public TwoDTree() {
            root = null;
            sz = 0;
        }

        public int size() {
            return sz;
        }

        public boolean isEmpty() {
            return size() == 0;
        }

        public void insert(Point2D p) {
            root = insert(root, p, true);
        }

        private Node insert(Node node, Point2D p, boolean vertical) {
            if (node == null) {
                node = new Node(p, vertical);
                sz++;
                return node;
            }
            if (vertical) {
                if (p.x() < node.point.x()) node.left = insert(node.left, p, !vertical);
                else if (node.point.equals(p)) return node;
                else node.right = insert(node.right, p, !vertical);
            }
            // TODO: Take care of duplicate points by checking if the Ys are equal
            else {
                if (p.y() < node.point.y()) node.left = insert(node.left, p, !vertical);
                else if (node.point.equals(p)) return node;
                else node.right = insert(node.right, p, !vertical);
            }
            return node;
        }

        public Iterable<Point2D> rangeSearch(RectHV rect) {
            List<Point2D> list = new ArrayList<>();
            rangeSearch(rect, root, list);
            return list;
        }

        private void rangeSearch(RectHV rect, Node node, List<Point2D> list) {
            if (node == null) return;
            Point2D p = node.point;
            if (isPointInRect(rect, p)) list.add(p);

            if (node.vertical) {
                if (p.x() > rect.xmin()) rangeSearch(rect, node.left, list);
                if (p.x() <= rect.xmax()) rangeSearch(rect, node.right, list);
            }
            else {
                if (p.y() > rect.ymin()) rangeSearch(rect, node.left, list);
                if (p.y() <= rect.ymax()) rangeSearch(rect, node.right, list);
            }
        }

        private boolean isPointInRect(RectHV rect, Point2D point) {
            return (point.x() >= rect.xmin() && point.x() <= rect.xmax() &&
                    point.y() >= rect.ymin() && point.y() <= rect.ymax());
        }

        public Point2D nearest(Point2D p) {
            Point2D[] point = new Point2D[1];
            nearest(p, root, point);
            return point[0];
        }

        private void nearest(Point2D p, Node node, Point2D[] candidate) {
            if (node == null) return;
            double newDist = p.distanceSquaredTo(node.point);
            if (candidate[0] == null) candidate[0] = node.point;
            else {
                double oldDist = p.distanceSquaredTo(candidate[0]);
                if (newDist < oldDist) candidate[0] = node.point;
            }

            if (node.vertical) {
                if (p.x() < node.point.x()) {
                    nearest(p, node.left, candidate);
                    if (candidate[0].distanceTo(p) > Math.abs(node.point.x() - p.x()))
                        nearest(p, node.right, candidate);
                }
                else {
                    nearest(p, node.right, candidate);
                    if (candidate[0].distanceTo(p) > Math.abs(node.point.x() - p.x()))
                        nearest(p, node.left, candidate);
                }
            }
            else {
                if (p.y() < node.point.y()) {
                    nearest(p, node.left, candidate);
                    if (candidate[0].distanceTo(p) > Math.abs(node.point.y() - p.y()))
                        nearest(p, node.right, candidate);
                }
                else {
                    nearest(p, node.right, candidate);
                    if (candidate[0].distanceTo(p) > Math.abs(node.point.y() - p.y()))
                        nearest(p, node.left, candidate);
                }
            }
        }

        public boolean contains(Point2D p) {
            return contains(p, root);
        }

        private boolean contains(Point2D p, Node node) {
            if (node == null) return false;
            Point2D currPoint = node.point;
            if (currPoint.x() == p.x() && currPoint.y() == p.y()) return true;

            if (node.vertical) {
                if (currPoint.x() > p.x()) return contains(p, node.left);
                else return contains(p, node.right);
            }
            else {
                if (currPoint.y() > p.y()) return contains(p, node.left);
                else return contains(p, node.right);
            }
        }

        public void draw() {
            draw(root, 0, 1, 0, 1);
        }


        private void draw(Node node, double minX, double maxX, double minY, double maxY) {
            if (node == null) return;
            draw(node.point, node.vertical, minX, maxX, minY, maxY);
            if (node.vertical) {
                draw(node.left, minX, node.point.x(), minY, maxY);
                draw(node.right, node.point.x(), maxX, minY, maxY);
            }
            else {
                draw(node.left, minX, maxX, minY, node.point.y());
                draw(node.right, minX, maxX, node.point.y(), maxY);
            }

        }


        private void draw(Point2D p, boolean vertical, double minX, double maxX, double minY,
                          double maxY) {
            StdDraw.setPenRadius(0.005d);
            if (vertical) {
                StdDraw.setPenColor(Color.BLUE);
                StdDraw.line(p.x(), minY, p.x(), maxY);
            }
            else {
                StdDraw.setPenColor(Color.RED);
                StdDraw.line(minX, p.y(), maxX, p.y());
            }
            StdDraw.setPenRadius(0.01d);
            StdDraw.setPenColor(Color.black);
            StdDraw.point(p.x(), p.y());
        }
    }
}
