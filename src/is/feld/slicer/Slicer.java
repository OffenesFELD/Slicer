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
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PMatrix;

import java.util.ArrayList;
import java.util.List;


public class Slicer {

  protected PApplet parent;
  protected TriangleFaceCollectionRenderer renderer;

  protected BoundingBox box;
  protected int slices;
  protected float pixelScale;


  protected List<TriangleFace> faces;

  public final static String VERSION = "##library.prettyVersion##";


  public Slicer(PApplet theP) {
    parent = theP;

    renderer = new TriangleFaceCollectionRenderer();
    faces = new ArrayList<TriangleFace>();

    box = new BoundingBox(400);
    slices = 10;

    pixelScale = 1.0f;
  }


  private void welcome() {
    System.out.println("##library.name## ##library.prettyVersion## by ##author##");   // TODO: remove
  }



  public void setBox(float theSize) {
    box = new BoundingBox(theSize);
  }


  public void setBox(float theX, float theY, float theZ) {
    box = new BoundingBox(theX, theY, theZ);
  }


  public void setSlices(int theSlices) {
    slices = theSlices;
  }


  public void setPixelScale(float theScale) {
    pixelScale = theScale;
  }


  public void begin() {
    if(parent.g.is3D()) {
      PMatrix currentModelView = parent.getMatrix();
      renderer.setOriginalModelView(currentModelView);
      parent.beginRaw(renderer);
      parent.pushMatrix();
    } else {
      System.err.println("Slicer can only be used with a 3D renderer like P3D or OPENGL.");
    }

  }


  public void end() {
    if(parent.g.is3D()) {
      parent.popMatrix();
      parent.endRaw();
    }

    faces = renderer.triangleFaces();
  }


  public void begin(PGraphics theG) {
    if(theG.is3D()) {
      PMatrix currentModelView = theG.getMatrix();
      renderer.setOriginalModelView(currentModelView);
      theG.beginRaw(renderer);
      theG.pushMatrix();
    } else {
      System.err.println("Slicer can only be used with a 3D PGraphics renderer like P3D or OPENGL.");
    }
  }


  public void end(PGraphics theG) {
    if(theG.is3D()) {
      theG.popMatrix();
      theG.endRaw();
    }

    faces = renderer.triangleFaces();
  }


  public TriangleFaceCollectionRenderer renderer() {
    return renderer;
  }


  public void drawBox() {
    drawBox(parent.g);
  }


  public void drawBox(PGraphics theG) {
    theG.box(box.width, box.height, box.depth);
  }


  public void drawSlices() {
    drawSlices(parent.g);
  }


  public void drawSlices(PGraphics theG) {

    theG.beginShape(PConstants.QUADS);

    for(int i = 0; i < slices; i++) {
      theG.pushMatrix();

      float y = PApplet.map(i, 0, slices - 1, -box.height * 0.5f, box.height * 0.5f);

      theG.vertex(-box.width * 0.5f, y, -box.depth * 0.5f);
      theG.vertex(+box.width * 0.5f, y, -box.depth * 0.5f);
      theG.vertex(+box.width * 0.5f, y, +box.depth * 0.5f);
      theG.vertex(-box.width * 0.5f, y, +box.depth * 0.5f);

      theG.popMatrix();
    }

    theG.endShape();
  }


  public PathSlicer pathSlicer() {
    return new PathSlicer(parent, faces, box, slices);
  }


  public PixelSlicer pixelSlicer() {
    return new PixelSlicer(parent, faces, box, slices, pixelScale);
  }


  public List<TriangleFace> faces() {
    return new ArrayList<TriangleFace>(faces);
  }


  /**
   * Draws all geometry collected between begin() and end() to the specified PGraphics
   *
   * @param theG
   */
  public void drawDebugFaces(PGraphics theG) {
    for(TriangleFace face:faces) {
      face.draw(theG);
    }
  }


  /**
   * Draws all geometry collected between begin() and end()
   */
  public void drawDebugFaces() {
    drawDebugFaces(parent.g);
  }


  /**
   * return the version of the library.
   *
   * @return String
   */
  public static String version() {
    return VERSION;
  }

}
