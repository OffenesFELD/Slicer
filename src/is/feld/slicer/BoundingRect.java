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

import java.util.Collection;


public class BoundingRect {

  public final float width;
  public final float height;

  public final float minX;
  public final float maxX;

  public final float minY;
  public final float maxY;


  public BoundingRect(float theWidth, float theHeight) {
    this.minX = -theWidth * 0.5f;
    this.maxX = theWidth * 0.5f;

    this.minY = -theHeight * 0.5f;
    this.maxY = theHeight * 0.5f;

    this.width = maxX - minX;
    this.height = maxY - minY;
  }


  public BoundingRect(Collection<? extends Locatable2D> thePoints) {
    float minX = Float.MAX_VALUE;
    float maxX = Float.MIN_VALUE;

    float minY = Float.MAX_VALUE;
    float maxY = Float.MIN_VALUE;

    for (Locatable2D point: thePoints) {
      minX = Math.min(minX, point.x());
      maxX = Math.max(maxX, point.x());

      minY = Math.min(minY, point.y());
      maxY = Math.max(maxY, point.y());
    }

    minX = (minX != Float.MAX_VALUE) ? minX : 0;
    maxX = (minX != Float.MIN_VALUE) ? maxX : 0;

    minY = (minY != Float.MAX_VALUE) ? minY : 0;
    maxY = (minY != Float.MIN_VALUE) ? maxY : 0;

    this.minX = minX;
    this.maxX = maxX;

    this.minY = minY;
    this.maxY = maxY;

    this.width = maxX - minX;
    this.height = maxY - minY;
  }


  public Point2D topLeft() {
    return new Point2D(minX, minY);
  }


  public Point2D bottomRight() {
    return new Point2D(maxX, maxY);
  }


  public float maxDimension() {
    return Math.max(width, height);
  }


  public Point2D center() {
    float x = (topLeft().x + bottomRight().x) * 0.5f;
    float y = (topLeft().y + bottomRight().y) * 0.5f;

    return new Point2D(x, y);
  }


  public boolean contains(Point2D theP) {
    return contains(theP.x, theP.y);
  }


  public boolean contains(float theX, float theY) {
    return theX >= minX && theX <= maxX && theY >= minY && theY <= maxY;
  }


  public String toString() {
    return "[" + width + " " + height + "]";
  }
}

