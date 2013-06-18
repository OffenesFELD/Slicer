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

import processing.core.PConstants;

import processing.core.PGraphics;
import processing.core.PVector;


public class TriangleFace {
  public final PVector a;
  public final PVector b;
  public final PVector c;


  public TriangleFace(PVector theA, PVector theB, PVector theC) {
    a = theA;
    b = theB;
    c = theC;
  }


  public PVector[] vertices() {
    return new PVector[]{a, b, c};
  }


  void draw(PGraphics theG) {
    theG.beginShape(PConstants.TRIANGLE);

    theG.vertex(a.x, a.y, a.z);
    theG.vertex(b.x, b.y, b.z);
    theG.vertex(c.x, c.y, c.z);

    theG.endShape();
  }
}

