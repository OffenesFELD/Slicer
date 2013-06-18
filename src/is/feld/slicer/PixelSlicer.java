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

import processing.core.*;
import processing.opengl.PGL;
import processing.opengl.PGraphicsOpenGL;

import java.util.ArrayList;
import java.util.List;


public class PixelSlicer {

  public final int xResolution;
  public final int yResolution;

  protected final float scale;
  protected final int slices;

  protected final BoundingBox box;
  protected final PShape shape;

  protected final PGraphicsOpenGL sliceCanvas;
  protected final List<TriangleFace> faces;

  protected final PApplet parent;


  public PixelSlicer(PApplet theP, List<TriangleFace> theFaces, BoundingBox theBox, int theSlices, float theScale) {
    xResolution = PApplet.round(theBox.width * theScale);
    yResolution = PApplet.round(theBox.depth * theScale);

    sliceCanvas = (PGraphicsOpenGL)theP.createGraphics(xResolution, yResolution, PConstants.OPENGL);
    sliceCanvas.noSmooth();

    scale = theScale;
    slices = theSlices;
    box = theBox;

    faces = theFaces;

    parent = theP;

    shape = facesToShape();
  }


  public PImage[] allSlices() {
    ArrayList<PImage> sliceImages = new ArrayList<PImage>();

    for (int i = 0; i < slices; i++) {
      PImage slice = sliceNumber(i);
      sliceImages.add(slice);
    }

    return (PImage[])sliceImages.toArray();
  }


  public PImage sliceNumber(int theSlice) {
    float y = PApplet.map(theSlice, 0, slices - 1, -box.height * 0.5f, box.height * 0.5f);

    return sliceAtY(y);
  }


  public PImage sliceAtY(float theY) {
    PGraphicsOpenGL c = sliceCanvas;

    float sliceY = c.cameraZ - theY * scale;
    float sliceDepth = (box.height / slices) * 0.5f * 0.5f  * 0.5f * scale;

    shape.disableStyle();

    c.beginDraw();

    c.noLights();
    c.translate(c.width * 0.5f, c.height * 0.5f, 0);
    c.scale(scale);
    c.background(0);

    c.noStroke();


    /* Add back faces behind the slice */
    c.ortho(0, c.width, 0, c.height, sliceY + sliceDepth, 10000);

    PGL pgl = c.beginPGL();

    pgl.enable(PGL.DEPTH_TEST);
    pgl.enable(PGL.CULL_FACE);
    pgl.cullFace(PGL.FRONT);

    c.fill(255);
    c.shape(shape);

    c.endPGL();


    /* Subtract front faces behind the slice */
    c.ortho(0, c.width, 0, c.height, sliceY + sliceDepth, 10000);

    pgl = c.beginPGL();
    pgl.enable(PGL.DEPTH_TEST);
    pgl.enable(PGL.CULL_FACE);
    pgl.cullFace(PGL.BACK);

    c.fill(0);
    c.shape(shape);

    c.endPGL();


    /* Draw front and back faces of the actual slice */
    c.ortho(0, c.width, 0, c.height, sliceY - sliceDepth, sliceY + sliceDepth);

    pgl = c.beginPGL();
    pgl.disable(PGL.DEPTH_TEST);
    pgl.disable(PGL.CULL_FACE);

    c.fill(255);
    c.shape(shape);

    c.endPGL();

    c.endDraw();

    return c.get();
  }


  protected PShape facesToShape() {
    PShape shape = parent.createShape();

    shape.beginShape(PShape.TRIANGLES);

    for(TriangleFace triangle:faces) {

      final PVector a = triangle.a;
      final PVector b = triangle.b;
      final PVector c = triangle.c;

      shape.vertex(c.x, c.z, c.y);  // reverse order && cordinates flipped
      shape.vertex(b.x, b.z, b.y);
      shape.vertex(a.x, a.z, a.y);
    }

    shape.endShape();

    return shape;
  }


}

