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


import is.feld.slicer.ContourPath;
import is.feld.slicer.PathSlicer;
import is.feld.slicer.PixelSlicer;
import is.feld.slicer.Slicer;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;


import java.util.List;

public class SimpleTest extends PApplet {

  Slicer slicer;
  boolean record;

  PShape shape;

  @Override
  public void setup() {
    size(500, 500, P3D);

    slicer = new Slicer(this);

    shape = loadShape("Teapot.obj");
  }


  @Override
  public void draw() {
    background(0);

    pushMatrix();

    translate(width * 0.5f, height * 0.5f);
    rotateY(millis() * 0.001f);

    slicer.setBox(500, 500, 500);
    slicer.setPixelScale(1f);

    slicer.setSlices(10);
    slicer.drawBox();
    slicer.drawSlices();
    slicer.begin();


    //translate(width * 0.5f, height * 0.5f);

    //stroke(255);

    scale(0.1f);
    shape.disableStyle();
    shape(shape);
    fill(255);
    //box(200);

    translate(200, 0);
    //sphere(90);

    slicer.end();


    popMatrix();





//    noFill();
//    stroke(255, 69, 0);
//    slicer.drawDebugFaces();
//
//    println(slicer.faces().size());

    background(0);
//
    //println(slicer.faces().get(0).a);

    PathSlicer pathSlicer = slicer.pathSlicer();
    List<ContourPath> paths = pathSlicer.sliceAtY(mouseX - width * 0.5f);


    PixelSlicer pixelSlicer = slicer.pixelSlicer();
    PImage img = pixelSlicer.sliceAtY(mouseX - width * 0.5f);

    image(img, 0, 0);

    noFill();
    stroke(255, 0, 255);
    translate(width * 0.5f, height * 0.5f);



    rect(0, 0, 100, 100);

    randomSeed(23);
    for(ContourPath path:paths) {
      stroke(random(255), random(255), random(255));
      path.draw(this);
    }
  }


  public static void main(String[] args) {
    PApplet.main(SimpleTest.class.getCanonicalName());
  }
}
