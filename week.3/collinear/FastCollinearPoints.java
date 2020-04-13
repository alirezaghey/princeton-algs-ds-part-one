/* *****************************************************************************
 *  Name: Alireza Ghey
 *  Date: 13-04-2020
 *  Description: Finds distinct lines of 4 or more points on a 2-d plane
 **************************************************************************** */

import java.util.Arrays;

public class FastCollinearPoints {
    private int numOfSegments = 0;
    private LineSegment[] segments;
    private Point[][] pointPairs;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        Point[] rawPoints = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
            for (int j = 0; j < i; j++) {
                if (rawPoints[j].compareTo(points[i]) == 0) throw new IllegalArgumentException();
            }
            rawPoints[i] = points[i];
        }
        points = rawPoints;
        pointPairs = new Point[points.length][2];

        for (int i = 0; i < points.length; i++) {
            Point[] copiedPoints = Arrays.copyOf(points, points.length);
            Arrays.sort(copiedPoints, points[i].slopeOrder());
            double currSlope = Double.NEGATIVE_INFINITY;
            int numPoints = 0;
            for (int j = 0; j < copiedPoints.length; j++) {
                double newSlope = points[i].slopeTo(copiedPoints[j]);
                if (currSlope == newSlope) numPoints++;
                else {
                    if (numPoints >= 3) {
                        addLineSegment(j - numPoints, j - 1, copiedPoints, points[i]);
                    }
                    currSlope = newSlope;
                    numPoints = 1;
                }
            }
            if (numPoints >= 3)
                addLineSegment(copiedPoints.length - numPoints, copiedPoints.length - 1,
                               copiedPoints, points[i]);
        }

    }

    private void addLineSegment(int left, int right, Point[] points, Point firstPoint) {
        Point[] pointsInSegment = new Point[right - left + 2];
        for (int k = left; k < right + 1; k++) {
            pointsInSegment[k - left] = points[k];
        }
        pointsInSegment[pointsInSegment.length - 1] = firstPoint;
        Arrays.sort(pointsInSegment);

        if (pointsInSegment[0].compareTo(firstPoint) != 0) return;

        if (this.numOfSegments == this.pointPairs.length) this.resizePointPairs();

        pointPairs[this.numOfSegments][0] = pointsInSegment[0];
        pointPairs[this.numOfSegments][1] = pointsInSegment[pointsInSegment.length - 1];
        this.numOfSegments++;
    }

    private void resizePointPairs() {
        Point[][] newPointPairs = new Point[this.pointPairs.length * 2][2];
        for (int i = 0; i < this.pointPairs.length; i++) {
            newPointPairs[i][0] = this.pointPairs[i][0];
            newPointPairs[i][1] = this.pointPairs[i][1];
        }
        this.pointPairs = newPointPairs;
    }

    public int numberOfSegments() { // the number of line segments
        return this.numOfSegments;
    }

    public LineSegment[] segments() { // the line segments
        if (this.segments == null) {
            this.segments = new LineSegment[this.numOfSegments];
            for (int i = 0; i < this.numOfSegments; i++) {
                this.segments[i] = new LineSegment(this.pointPairs[i][0],
                                                   this.pointPairs[i][1]);
            }
        }
        return Arrays.copyOf(this.segments, this.segments.length);
    }
}
