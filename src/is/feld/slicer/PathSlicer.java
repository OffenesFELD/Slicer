/**
 * ##library.name##
 * ##library.sentence##
 * ##library.url##
 *
 * Copyright ##copyright## ##author##
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 *
 * @author      ##author##
 * @modified    ##date##
 * @version     ##library.prettyVersion## (##library.version##)
 */

package is.feld.slicer;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.*;

public class PathSlicer {

  protected final PApplet parent;

  protected final List<TriangleFace> faces;
  protected final BoundingBox box;
  protected final BoundingRect rect;
  protected final int slices;


  public PathSlicer(PApplet theP, List<TriangleFace> theFaces, BoundingBox theBox, int theSlices) {
    faces = theFaces;
    slices = theSlices;
    box = theBox;
    rect = new BoundingRect(box.width, box.depth);
    parent = theP;
  }


  /**
   * Get slice number n
   *
   * @param theIndex
   * @return a list of 2D paths
   */
  public List<ContourPath> sliceNumber(int theIndex) {
    float y = PApplet.map(slices, 0, slices - 1, -box.height * 0.5f, box.height * 0.5f);

    return sliceAtY(y);
  }


  /**
   * Slice the stored geometry composed of triangles at the y plane.
   *
   * @param theY
   * @return a list of 2D paths
   */
  public List<ContourPath> sliceAtY(float theY) {
    List<LineSegment2D> segments = sliceFacesWithPlane(faces, theY);
    RectangleBoundsClipper clipper = new RectangleBoundsClipper(rect);

    return mergeSegmentsToPaths(clipper.clip(segments));
  }


  public List<LineSegment2D> sliceSegmentsNumber(int theIndex) {
    float y = PApplet.map(slices, 0, slices - 1, -box.height * 0.5f, box.height * 0.5f);

    return sliceSegmentsAtY(y);
  }


  /**
   * Slice the stored geometry composed of triangles at the y plane.
   * This returns no connected paths, but a list of unrelated line segments.
   * To get connected paths, you might want to use @see PathSlicer.slice
   *
   * @param theY
   * @return a list of 2D paths
   */
  public List<LineSegment2D> sliceSegmentsAtY(float theY) {
    return sliceFacesWithPlane(faces, theY);
  }


  /**
   * Slice a geometry composed of triangles at the y plane.
   * This returns no connected paths, but a list of unrelated line segments.
   * To get connected paths, you might want to use @see PathSlicer.slice
   *
   * @param theFaces
   * @param theY
   * @return
   */
  public static List<LineSegment2D> sliceFacesWithPlane(List<TriangleFace> theFaces, float theY) {
    final float y = theY;

    ArrayList<LineSegment2D> resultingSegments = new ArrayList<LineSegment2D>();

    for (TriangleFace face : theFaces) {
      LineSegment2D segment = sliceTriangleWithPlane(face, y);

      if(segment != null) {
        resultingSegments.add(segment);
      }
    }

    return resultingSegments;
  }


  /**
   * Slice a triangle with a defined y plane.
   *
   * @param theFace a triangle face
   * @param theY y plane
   * @return null or the resulting line segment.
   */
  public static LineSegment2D sliceTriangleWithPlane(TriangleFace theFace, float theY) {
    final TriangleFace face = theFace;
    final float yPlane = theY;

    int countBelow = 0;
    int countAbove = 0;
    int countOn = 0;

    /* Counting cases*/
    for (PVector v : face.vertices()) {
      if (v.y > yPlane) countAbove++;
      if (v.y < yPlane) countBelow++;
      if (v.y == yPlane) countOn++;
    }

    assert(countBelow + countOn + countAbove == 3) : "Wrong number of cases.";


    /* All vertices are above, below or on the plane */
    if (countAbove == 3 || countBelow == 3 || countOn == 3) {
      return null;
    }

    /* Two vertices are on one side, one is on the plane */
    if ((countOn == 1 && countBelow == 2) || (countOn == 1 && countAbove == 2)) {
      return null;
    }

    /* Two vertices are on the plane, one above */
    if (countOn == 2 && countBelow == 1) {
      return null;  // prevent doubled vertices with the next case
    }


    /* Two vertices are on the plane, one above */
    if (countOn == 2 && countAbove == 1) {
      Point2D a = null;
      Point2D b = null;

      for (PVector v : face.vertices()) {
        if (v.y == yPlane) {
          if(a == null) {
            a = new Point2D(v.x, v.z);
          } else {
            b = new Point2D(v.x, v.z);
          }
        }
      }

      assert(a != null && b != null) : "Two points on the plane should yield a result every time.";
      return new LineSegment2D(a, b);
    }

    /* One vertex is on the plane, one above, one below */
    if (countOn == 1 && countAbove == 1 && countBelow == 1) {
      if (face.a.y == yPlane) {
        PVector a = face.a;
        PVector b = LineSegment3D.intersectWithPlaneY(face.b, face.c, yPlane);
        return new LineSegment2D(new Point2D(a.x, a.z), new Point2D(b.x, b.z));
      }

      if (face.b.y == yPlane) {
        PVector a = LineSegment3D.intersectWithPlaneY(face.a, face.c, yPlane);
        PVector b = face.b;
        return new LineSegment2D(new Point2D(a.x, a.z), new Point2D(b.x, b.z));
      }

      if (face.c.y == yPlane) {
        PVector a = LineSegment3D.intersectWithPlaneY(face.a, face.b, yPlane);
        PVector b = face.c;
        return new LineSegment2D(new Point2D(a.x, a.z), new Point2D(b.x, b.z));
      }
    }

    /* Common case: Two vertices on one side, on on the other */
    if ((countAbove == 1 && countBelow == 2) || (countAbove == 2 && countBelow == 1)  ) {
      if (face.a.y > yPlane && face.b.y < yPlane && face.c.y < yPlane) {
        PVector a = LineSegment3D.intersectWithPlaneY(face.a, face.b, yPlane);
        PVector b = LineSegment3D.intersectWithPlaneY(face.a, face.c, yPlane);
        return new LineSegment2D(new Point2D(a.x, a.z), new Point2D(b.x, b.z));
      }

      if (face.a.y < yPlane && face.b.y > yPlane && face.c.y > yPlane) {
        PVector a = LineSegment3D.intersectWithPlaneY(face.a, face.b, yPlane);
        PVector b = LineSegment3D.intersectWithPlaneY(face.a, face.c, yPlane);
        return new LineSegment2D(new Point2D(a.x, a.z), new Point2D(b.x, b.z));
      }


      if (face.b.y > yPlane && face.a.y < yPlane && face.c.y < yPlane) {
        PVector a = LineSegment3D.intersectWithPlaneY(face.b, face.a, yPlane);
        PVector b = LineSegment3D.intersectWithPlaneY(face.b, face.c, yPlane);
        return new LineSegment2D(new Point2D(a.x, a.z), new Point2D(b.x, b.z));
      }

      if (face.b.y < yPlane && face.a.y > yPlane && face.c.y > yPlane) {
        PVector a = LineSegment3D.intersectWithPlaneY(face.b, face.a, yPlane);
        PVector b = LineSegment3D.intersectWithPlaneY(face.b, face.c, yPlane);
        return new LineSegment2D(new Point2D(a.x, a.z), new Point2D(b.x, b.z));
      }


      if (face.c.y > yPlane && face.a.y < yPlane && face.b.y < yPlane) {
        PVector a = LineSegment3D.intersectWithPlaneY(face.c, face.a, yPlane);
        PVector b = LineSegment3D.intersectWithPlaneY(face.c, face.b, yPlane);
        return new LineSegment2D(new Point2D(a.x, a.z), new Point2D(b.x, b.z));
      }

      if (face.c.y < yPlane && face.a.y > yPlane && face.b.y > yPlane) {
        PVector a = LineSegment3D.intersectWithPlaneY(face.c, face.a, yPlane);
        PVector b = LineSegment3D.intersectWithPlaneY(face.c, face.b, yPlane);
        return new LineSegment2D(new Point2D(a.x, a.z), new Point2D(b.x, b.z));
      }
    }

    assert(false) : "Unhandled case.";  // Getting here means we have an unhandled case...
    return null;
  }


  /**
   * Combines a list of unconnected line segments and turns them into connected paths.
   * This is a rather workaround, because Processing does not represent
   * meshes in a connected way. All information on how faces are connected is lost in progress.
   *
   * @param theSegments
   * @return
   */
  public static List<ContourPath> mergeSegmentsToPaths(List<LineSegment2D> theSegments) {
    LinkedList<LineSegment2D> segmentList = new LinkedList<LineSegment2D>(theSegments);

    SpatialHashGrid2D<Point2D> gridMap = new SpatialHashGrid2D<Point2D>();  // speed

    HashMap<Point2D, LineSegment2D> pointSegmentMap = new HashMap<Point2D, LineSegment2D>();

    for(LineSegment2D segment:segmentList) {
      gridMap.add(segment.start);
      gridMap.add(segment.end);

      pointSegmentMap.put(segment.start, segment);
      pointSegmentMap.put(segment.end, segment);
    }


    ArrayList<ContourPath> resultPaths = new ArrayList<ContourPath>();

    while (!segmentList.isEmpty()) {
      ContourPath currentPath = new ContourPath();
      resultPaths.add(currentPath);

      LineSegment2D firstSegment = segmentList.getFirst();

      currentPath.addFirst(firstSegment.start);
      currentPath.addLast(firstSegment.end);

      gridMap.remove(firstSegment.start);
      gridMap.remove(firstSegment.end);

      segmentList.removeFirst();


      boolean doContinue = true;   // did we close a path?

      while (doContinue && !segmentList.isEmpty()) {
        Point2D endPoint = currentPath.last();

        Point2D endCandidate = endPoint.closest(gridMap.getAtExtended(endPoint));


        if(endCandidate != null && endCandidate.isAlmost(endPoint)) {

          LineSegment2D newSegment = pointSegmentMap.get(endCandidate);

          if (endCandidate == newSegment.end) {    // flip
            currentPath.addLast(newSegment.start);
          } else {
            currentPath.addLast(newSegment.end);
          }

          gridMap.remove(newSegment.start);
          gridMap.remove(newSegment.end);

          segmentList.remove(newSegment);

          if (currentPath.first().isAlmost(currentPath.last()) && currentPath.size() > 2) {
            currentPath.close();
            doContinue = false;
          }

        } else {  // no match
          doContinue = false;
        }
      }
    }

    return resultPaths;
  }


}
