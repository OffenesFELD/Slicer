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


import java.util.ArrayList;
import java.util.List;


public class RectangleBoundsClipper {

  protected final BoundingRect rect;

  public RectangleBoundsClipper(BoundingRect theRect) {
    rect = theRect;
  }


  public List<LineSegment2D> clip(List<LineSegment2D> theSegments) {
    ArrayList<LineSegment2D> results = new ArrayList<LineSegment2D>();

    for(LineSegment2D segment:theSegments) {
      Point2D startPoint = segment.start;
      Point2D endPoint = segment.end;

      if(rect.contains(startPoint) && rect.contains(endPoint)) {
        results.add(new LineSegment2D(segment));
      } else  {
        LineSegment2D clipped = clip(segment);

        if(clipped != null) {
          results.add(clipped);
        }
      }
    }

    return results;
  }


  protected LineSegment2D clip(LineSegment2D theSegment) {
    LineSegment2D currentSegment = theSegment;

    /* Divide & Conquer (each edge consecutively) */
    for(final EdgeCase edgeCase:EdgeCase.values()){
      Point2D intersect = intersectWithEdge(edgeCase, currentSegment.start, currentSegment.end);


      if(!isInside(currentSegment.start, edgeCase) && !isInside(currentSegment.end, edgeCase)) {
        return null;
      } else if(!isInside(currentSegment.start, edgeCase) || !isInside(currentSegment.end, edgeCase)) {

        if(isInside(currentSegment.start, edgeCase)) {
          currentSegment = new LineSegment2D(currentSegment.start, intersect);
        } else {
          currentSegment = new LineSegment2D(intersect, currentSegment.end);
        }

      }
    }

    return currentSegment;
  }


  private Point2D intersectWithEdge(final EdgeCase theEdge, Point2D theA, Point2D theB) {
    float x = 0;
    float y = 0;

    switch (theEdge) {
      case NORTH:
        x = theA.x + ((rect.minY - theA.y) * (theB.x - theA.x)) / (theB.y - theA.y);
        y = rect.minY;
        break;

      case EAST:
        x = rect.maxX;
        y = theA.y + ((rect.maxX - theA.x) * (theB.y - theA.y)) / (theB.x - theA.x);
        break;

      case SOUTH:
        x = theA.x + ((rect.maxY - theA.y) * (theB.x - theA.x)) / (theB.y - theA.y);
        y = rect.maxY;
        break;

      case WEST:
        x = rect.minX;
        y = theA.y + ((rect.minX - theA.x) * (theB.y - theA.y)) / (theB.x - theA.x);
        break;
    }

    Point2D result = new Point2D(x, y);

    return result;
  }


  private boolean isInside(Point2D theP, EdgeCase theEdge) {
    switch (theEdge) {
      case NORTH:
        return (theP.y >= rect.minY);

      case EAST:
        return (theP.x <= rect.maxX);

      case SOUTH:
        return (theP.y <= rect.maxY);

      case WEST:
        return (theP.x >= rect.minX);
    }

    return true;
  }



  public enum EdgeCase {
    NORTH, EAST, SOUTH, WEST
  }
}

