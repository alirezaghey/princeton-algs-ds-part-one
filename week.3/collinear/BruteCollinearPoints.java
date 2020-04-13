/* *****************************************************************************
 *  Name: Alireza Ghey
 *  Date: 13-04-2020
 *  Description: Finds lines of 4 or more points on a 2-d plane
 **************************************************************************** */

import java.util.Arrays;

public class BruteCollinearPoints {
    private Point[][] pointPairs;
    private LineSegment[] segments = null;
    private int numSegments;

    public BruteCollinearPoints(Point[] points) { // finds all line segments containing 4 points


        if (points == null) throw new IllegalArgumentException();
        Point[] rawPoints = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
            for (int j = 0; j < i; j++) {
                if (rawPoints[j].compareTo(points[i]) == 0) throw new IllegalArgumentException();
            }

            rawPoints[i] = points[i];
        }

        this.numSegments = 0;
        this.pointPairs = new Point[rawPoints.length * 2][2];

        for (int i = 0; i < rawPoints.length - 3; i++) {
            for (int j = i + 1; j < rawPoints.length - 2; j++) {
                // boolean found = false;
                double currSlope = rawPoints[i].slopeTo(rawPoints[j]);
                for (int k = j + 1; k < rawPoints.length - 1; k++) {
                    if (rawPoints[i].slopeTo(rawPoints[k]) != currSlope) continue;
                    for (int m = k + 1; m < rawPoints.length; m++) {
                        if (rawPoints[i].slopeTo(rawPoints[m]) != currSlope) continue;
                        Point[] tempPoints = { points[i], points[j], points[k], points[m] };
                        Arrays.sort(tempPoints);
                        this.pointPairs[this.numSegments][0] = tempPoints[0];
                        this.pointPairs[this.numSegments][1] = tempPoints[3];
                        this.numSegments++;
                    }
                }
            }
        }
        Point[][] newPointPairs = new Point[this.numSegments][2];
        for (int i = 0; i < this.numSegments; i++) {
            newPointPairs[i][0] = this.pointPairs[i][0];
            newPointPairs[i][1] = this.pointPairs[i][1];
        }
        this.pointPairs = newPointPairs;
    }

    public int numberOfSegments() { // the number of line segments
        return this.numSegments;
    }

    public LineSegment[] segments() {              // the line segments
        if (this.segments == null) {
            this.segments = new LineSegment[this.numSegments];
            for (int i = 0; i < this.numSegments; i++) {
                this.segments[i] = new LineSegment(this.pointPairs[i][0], this.pointPairs[i][1]);
            }

        }
        return Arrays.copyOf(this.segments, this.segments.length);
    }
}
