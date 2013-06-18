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


import processing.core.PVector;

import java.util.List;

public class Point2D implements Locatable2D  {
  public final float x;
  public final float y;


  public Point2D(Point2D theP) {
    x = theP.x;
    y = theP.y;
  }


  public Point2D(float theX, float theY) {
    x = theX;
    y = theY;
  }


  @Override
  public float x() {
    return x;
  }


  @Override
  public float y() {
    return y;
  }


  /**
   * This will be true if the two points coincide.
   *
   * @param theOther
   * @return
   */
  public boolean isAlmost(Point2D theOther) {
    return isAlmost(this, theOther);
  }


  /**
   * Distance between this and another point
   *
   * @param theOther
   * @return
   */
  public float distance(Point2D theOther) {
    float deltaX = this.x - theOther.x;
    float deltaY = this.y - theOther.y;

    return (float)Math.sqrt(deltaX * deltaX + deltaY * deltaY);
  }


  /**
   * Square of the distance between this and another point
   *
   * @param theOther
   * @return
   */
  public float distanceSquared(Point2D theOther) {
    float deltaX = this.x - theOther.x;
    float deltaY = this.y - theOther.y;

    return deltaX * deltaX + deltaY * deltaY;
  }


  /**
   * Return the point closest to this point.
   * Will be null if there aren't any.
   *
   * @param theOthers
   * @return
   */
  public Point2D closest(List<Point2D> theOthers) {
    Point2D closest = null;
    float closestDistanceSquared = Float.MAX_VALUE;

    for(Point2D other:theOthers) {
      float distanceSquared = this.distanceSquared(other);
      if(distanceSquared < closestDistanceSquared) {
        closest = other;
        closestDistanceSquared = distanceSquared;
      }
    }

    return closest;
  }


  /**
   * Checks if two points coincide
   *
   * @param theA first point
   * @param theB second point
   * @return true if they coincide
   */
  public static boolean isAlmost(final Point2D theA, final Point2D theB) {
    final float ALMOST_THRESHOLD = 0.001f;

    return theA.distanceSquared(theB) < ALMOST_THRESHOLD;
  }

}
