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


public class LineSegment3D {
  public final PVector start;
  public final PVector end;


  public LineSegment3D(PVector theStart, PVector theEnd) {
    start = theStart;
    end = theEnd;
  }


  public PVector intersectWithPlaneY(float theY) {
    return LineSegment3D.intersectWithPlaneY(this.start, this.end, theY);
  }


  public static PVector intersectWithPlaneY(PVector theA, PVector theB, float theY) {
    final PVector a = theA;
    final PVector b = theB;

    final float y = theY;

    float u = (a.y - y) / (b.y - a.y);

    float x =  a.x - u * (b.x - a.x);
    float z =  a.z - u * (b.z - a.z);

    return new PVector(x, y, z);
  }
}
