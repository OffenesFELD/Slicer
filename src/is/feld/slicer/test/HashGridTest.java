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

package is.feld.slicer.test;

import is.feld.slicer.Point2D;
import is.feld.slicer.Profiler;
import is.feld.slicer.SpatialHashGrid2D;
import processing.core.PApplet;

import java.util.LinkedList;
import java.util.List;


/**
 * Testing speed and functionality of the hash grid.
 */
public class HashGridTest extends PApplet {

  SpatialHashGrid2D<Point2D> grid;


  @Override
  public void setup() {
    size(500, 500, P3D);

    grid = new SpatialHashGrid2D<Point2D>();

    for(int i = 0; i < 10000; i++) {
      grid.add(new Point2D(random(0, width), random(0, height)));
    }
  }


  @Override
  public void draw() {
    background(0);

    noStroke();
    fill(255);

    for(Point2D point:grid.elements()) {
      ellipse(point.x, point.y, 5, 5);
    }

    fill(255, 0, 0);


    Profiler.start("search");
    Point2D mouse = new Point2D(mouseX, mouseY);
    List<Point2D> p = new LinkedList<Point2D>();
    for(Point2D point:grid.elements()) {
      if(point.isAlmost(mouse)) {
        p.add(point);
      }
    }
    Profiler.stop("search");


    Profiler.start("grid");
    List<Point2D> mousePoints = grid.getAt(mouseX, mouseY);
    Profiler.stop("grid");


    Profiler.start("grid extended");
    List<Point2D> mousePointsExtended = grid.getAtExtended(mouseX, mouseY);
    Profiler.stop("grid extended");

    for(Point2D point:mousePointsExtended) {
      ellipse(point.x, point.y, 7, 7);
    }
  }


  public static void main(String[] args) {
    PApplet.main(HashGridTest.class.getCanonicalName());
  }
}
