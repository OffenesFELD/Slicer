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

import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;


public class ContourPath {
  protected LinkedList<Point2D> points;
  protected boolean closed = false;


  public ContourPath() {
    points = new LinkedList<Point2D>();
  }


  public void addFirst(Point2D theP) {
    points.addFirst(theP);
  }


  public void addLast(Point2D theP) {
    points.addLast(theP);
  }


  public Point2D first() {
    return points.getFirst();
  }


  public Point2D last() {
    return points.getLast();
  }


  public void close() {
    closed = true;
  }


  public int size() {
    return points.size();
  }


  public boolean isClosed() {
    return closed;
  }


  public LinkedList<Point2D> points() {
    return points;
  }


  public void draw(PGraphics theG) {
    theG.beginShape();

    for ( final Point2D p : points ) {
      theG.vertex(p.x, p.y);
    }

    if ( isClosed() ) {
      theG.endShape(PConstants.CLOSE);
    } else {
      theG.endShape();
    }
  }


  public void draw(PApplet theG) {
    theG.beginShape();

    for ( final Point2D p : points ) {
      theG.vertex(p.x, p.y);
    }

    if ( isClosed() ) {
      theG.endShape(PConstants.CLOSE);
    } else {
      theG.endShape();
    }
  }
}

