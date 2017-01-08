package lab3_201_14.uwaterloo.ca.lab3_201_14;

import android.graphics.PointF;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.mapper.MapView;
import ca.uwaterloo.mapper.NavigationalMap;

/**
 * Created by Anthony on 2016-07-07.
 */

class QueuePoint
{

    public PointF point;
    public int count;

    public QueuePoint(PointF pointF, int c)
    {
        point = pointF;
        count = c;
    }

    public boolean isSamePoint(QueuePoint a) { return this.point.equals(a.point); }

}


public class Navigator implements DisplacementConstants {

    public TextView outputOrigin;
    public TextView outputDestination;
    public TextView outputArrival;

    private static PointF origin;
    private static PointF user;
    private static PointF destination;
    private List<PointF> route;
    private List<QueuePoint> routeQueue;

    private static NavigationalMap navMap;
    private MapView mapView;

    private final double stepSize = 2.0;
    private boolean arrived = false;

    public Navigator(NavigationalMap nm, MapView mv, TextView origOut, TextView destOut, TextView arrivOut)
    {
        navMap = nm;
        mapView = mv;
        outputOrigin = origOut;
        outputDestination = destOut;
        outputArrival = arrivOut;

        route = new ArrayList<PointF>();
        routeQueue = new ArrayList<QueuePoint>();
        origin = new PointF(0.0f, 0.0f);
        user = new PointF(0.0f, 0.0f);
        destination = new PointF(0.0f, 0.0f);
    }

    /**
     * If the origin and destination are not separated by walls, a straight-line path is used to
     * connect them.
     * Otherwise, the pathfinding algorithm must be used and will determine an indirect route.
     */
    public void determineRoute()
    {
        List<PointF> r = new ArrayList<PointF>();
        if(user.x > 0.001 && destination.x > 0.001) {
            if (isNotWall(user, destination)) {
                r.add(user);
                r.add(destination);
            } else {
                if(generateRoute() > 0) {
                    r = route;
                }
            }
        }
        mapView.setUserPath(r);
    }

    /**
     * From the routeQueue, a path of points is calculated from the user to the destination
     * by chaining together adjacent points
     *
     * Consider the destination is at (5,5)
     * Consider the user is at (4,4) and it took 2 steps to get there:
     *  (steps taken is calculated from generateRouteQueue())
     *        (4,4,2)
     * The route is initialized with the first point - the user:
     *    (4,4)
     * The algorithm then looks at all points that took 1 step:
     *        (5,4,1), (5,6,1), (4,5,1), (6,5,1)
     * From the points that take 1 step, it looks for points that are adjacent to the point that took 2 steps.
     * Those points are:
     *        (4,5,1) and (5,4,1).
     * Both points are valid so one is randomly picked.
     * One possible route at this time will look like:
     *    (4,4), (4,5)
     * The algorithm then looks at all points that took 0 steps...which is the destination:
     * The route is then concluded as:
     *    (4,4), (4,5), (5,5)
     *
     * More elaboration at:
     * https://en.wikipedia.org/wiki/Pathfinding#Sample_algorithm
     * @return Number of "steps" needed to reach the user from the destination
     */
    public int generateRoute()
        {
        route.clear();
        int countValue = generateRouteQueue();
        if (countValue > 0) {
            int n = 1;
            for(int i = countValue-1; i > 0; i-- ) {
                for(QueuePoint q : routeQueue) {
                    if(q.count == i && isAdjacent(route.get(n),q.point)) {
                        route.add(q.point);
                        break;
                    }
                }
                n++;
            }
            route.add(destination);
        }
        return countValue;
    }

    /**
     * Bruteforce algorithm to spread out from the destination until it reaches the user
     * Generates a routeQueue (all adjacent points that were calculated in the process of finding the user)
     *
     * The queue is a list of graph points and a counter (x, y, count)
     * The first entry in the queue is the destination: (dest.x, dest.y, 0)
     * The algorithm loops through every point in the queue to determine their adjacent points and add those points
     * to the queue as long as they aren't walls and are unique. It continues doing this until a point is reached
     * that is within step-distance of the user.
     *
     * Let's say destination is at (5,5) and user is at (4,4)
     * This is how the queue will look like with a stepSize = 1:
     * (5,5,0), (5,4,1), (5,6,1), (4,5,1), (6,5,1), (5,3,2), (6,4,2), (7,5,2), (6,6,2), (5,7,2), (4,6,2), (3,5,2), (4,4,2)
     *
     * More elaboration at:
     * https://en.wikipedia.org/wiki/Pathfinding#Sample_algorithm
     * @return Number of "steps" needed to reach the user from the destination
     */
    public int generateRouteQueue()
    {
        routeQueue.clear();
        routeQueue.add(new QueuePoint(destination, 0));
        int count = 0;
        arrived = false;

        while (!arrived) {
            count++;
            List<QueuePoint> queueBuffer = new ArrayList<QueuePoint>();
            for (QueuePoint point : routeQueue) {
                List<QueuePoint> tempAdj = adjacentPoints(point);
                for (QueuePoint q : tempAdj) {
                    if(isNotinQueue(queueBuffer, q)) {
                        queueBuffer.add(q);
                    }
                }
            }
            if (queueBuffer.isEmpty()) {
                return -1;
            } else {
                routeQueue.addAll(queueBuffer);
            }
        }

        return count;
    }

    /**
     * Returns a list of the adjacent points that are unique non-walls
     * @param qp QueuePoint
     * @return List of QueuePoints
     */
    public List<QueuePoint> adjacentPoints(QueuePoint qp)
    {
        List<QueuePoint> adj = allAdjacentPoints(qp);
        List<QueuePoint> adjNoWalls = new ArrayList<QueuePoint>();

        for (QueuePoint queuePoint : adj) {
            if(isNotWall(queuePoint.point, qp.point) && isNotinQueue(routeQueue, queuePoint)) {
                adjNoWalls.add(queuePoint);
                if(isClose(user, queuePoint.point) && isNotInRoute(queuePoint.point) && isNotWall(user, queuePoint.point)) {
                    arrived = true;
                    route.add(user);
                    route.add(queuePoint.point);
                    break;
                }
            }
        }
        return adjNoWalls;
    }

    /**
     * Returns a list of all 4 points offset by stepSize from the given point
     * @param qp Point you want to find the adjacents of
     * @return A list of the 4 adjacent points to a given point
     */
    public List<QueuePoint> allAdjacentPoints(QueuePoint qp)
    {
        List<QueuePoint> adj = new ArrayList<QueuePoint>();
        QueuePoint q = new QueuePoint(pointOffset(qp.point, (float) stepSize, 0), qp.count+1);
        adj.add(q);
        q = new QueuePoint(pointOffset(qp.point, (float) -stepSize, 0), qp.count+1);
        adj.add(q);
        q = new QueuePoint(pointOffset(qp.point, 0, (float) stepSize), qp.count+1);
        adj.add(q);
        q = new QueuePoint(pointOffset(qp.point, 0, (float) -stepSize), qp.count+1);
        adj.add(q);
        return adj;
    }

    /**
     * Returns a new PointF offset by the specified amounts
     * @param p Original PointF
     * @param offsetx Offset along the x-axis
     * @param offsety Offset along the y-axis
     * @return New PointF offset from the original
     */
    public static PointF pointOffset(PointF p, float offsetx, float offsety) { return new PointF((float) (p.x + offsetx), (float) (p.y + offsety)); }

    /**
     * Checks for the existence of a QueuePoint in a given list of QueuePoints
     * @param qpList List of QueuePoints
     * @param q QueuePoint
     * @return True if QueuePoint is unique from List
     */
    public boolean isNotinQueue(List<QueuePoint> qpList, QueuePoint q)
    {
        for (QueuePoint queuePoint : qpList) {
            if (q.isSamePoint(queuePoint)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a PointF is already in the specific PointF list "route"
     * @param p A PointF
     * @return True if PointF is not found in route
     */
    public boolean isNotInRoute(PointF p)
    {
        for (PointF pointF : route) {
            if (p.equals(pointF)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determine whether two points are separated by a wall
     * @param a PointF a
     * @param b PointF b
     * @return True if there is no wall
     */
    public static boolean isNotWall(PointF a, PointF b)
    {
        if(navMap.calculateIntersections(a, b).size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * Check if a point is within step-distance of another point
     * @param dest Point you want to reach
     * @param p Point where you are
     * @return True if point is close enough to desired destination
     */
    public boolean isClose(PointF dest, PointF p)
    {
        if (Math.abs(dest.x-p.x) <= stepSize && Math.abs(dest.y-p.y) <= stepSize) {
            return true;
        }
        return false;
    }

    /**
     * Check if a point is exactly a step-distance away from another point in one axis
     * @param a PointF a
     * @param b PointF b
     * @return True if points are adjacent
     */
    public boolean isAdjacent(PointF a, PointF b)
    {
        double e = 0.01;
        if ((Math.abs(a.x-b.x)-stepSize < e && Math.abs(a.y-b.y) < e) || (Math.abs(a.x-b.x) < e && Math.abs(a.y-b.y)-stepSize < e)) {
            return true;
        }
        return false;
    }

    /**
     * Clears the route list and the MapView path
     */
    public void clearRoute()
    {
        route.clear();
        mapView.setUserPath(route);
    }


    /**
     * Call isDestination to check if the user reached the destination
     */
    public boolean isDestination(PointF user, PointF destination) {

        if (isClose(destination, user)) {
            outputArrival.setText("Destination reached");
            return true;
        }
        else
            return false;
    }

    // Getters and setters for origin and destination
    public static void setOrigin(PointF orig) { origin = orig; }

    public static void setUser(PointF u) { user = u; }

    public static void setDestination(PointF dest) { destination = dest; }

    public  static PointF getOrigin() { return origin; }

    public static PointF getUser() { return user; }

    public static PointF getDestination() { return destination; }

}