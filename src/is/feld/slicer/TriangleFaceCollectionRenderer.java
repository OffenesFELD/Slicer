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

import java.util.ArrayList;
import java.util.List;


public class TriangleFaceCollectionRenderer extends PGraphics {

  protected ArrayList<TriangleFace> faces;
  protected PMatrix initialModelViewMatrix = new PMatrix3D();
  protected PMatrix initialModelViewMatrixInverse = new PMatrix3D();


  public TriangleFaceCollectionRenderer() {
    faces = new ArrayList<TriangleFace>();
  }


  @Override
  protected void allocate() {
  }


  @Override
  public void dispose() {

  }

  @Override
  public boolean displayable() {
    return false;
  }


  @Override
  public boolean is2D() {
    return false;
  }


  @Override
  public boolean is3D() {
    return true;
  }


  @Override
  public void beginDraw() {
    faces.clear();
  }


  @Override
  public void endDraw() {

  }


  protected void storeTriangle(int theIndexA, int theIndexB, int theIndexC) {
    float[] vertexA = vertices[theIndexA];
    PVector vertexAPoint = new PVector(vertexA[X], vertexA[Y], vertexA[Z]);

    float[] vertexB = vertices[theIndexB];
    PVector vertexBPoint = new PVector(vertexB[X], vertexB[Y], vertexB[Z]);

    float[] vertexC = vertices[theIndexC];
    PVector vertexCPoint = new PVector(vertexC[X], vertexC[Y], vertexC[Z]);

    vertexAPoint = initialModelViewMatrixInverse.mult(vertexAPoint, null);
    vertexBPoint = initialModelViewMatrixInverse.mult(vertexBPoint, null);
    vertexCPoint = initialModelViewMatrixInverse.mult(vertexCPoint, null);


    TriangleFace face = new TriangleFace(vertexAPoint, vertexBPoint, vertexCPoint);

    faces.add(face);
  }


  @Override
  public void beginShape(int theKind) {
    shape = theKind;

    if ( (shape != LINES) &&
        (shape != TRIANGLES) &&
        (shape != POLYGON) ) {

      throw new RuntimeException(
          "The slice renderer can only be used with beginRaw() because it only triangles"
      );
    }

    if ((shape == POLYGON) && fill) {
      throw new RuntimeException("The slice renderer only supports non-filled shapes.");
    }

    vertexCount = 0;
  }


  @Override
  public void vertex(float x, float y) {
    vertex(x, y, 0);
  }


  @Override
  public void vertex(float x, float y, float z) {
    float vertex[] = vertices[vertexCount];

    vertex[X] = x;  // we just need the vertex coordinates
    vertex[Y] = y;
    vertex[Z] = z;

    vertexCount++;

    if ((shape == LINES) && (vertexCount == 2)) {
      // storeLine(0, 1); // do nothing
      vertexCount = 0;    // reset counter
    } else if ((shape == TRIANGLES) && (vertexCount == 3)) {
      storeTriangle(0, 1, 2);
      vertexCount = 0;    // reset counter
    }
  }


  @Override
  public void endShape(int mode) {
    if (shape == POLYGON) {
      for (int i = 0; i < vertexCount - 1; i++) {
        //storeLine(i, i + 1);   // do nothing
      }
      if (mode == CLOSE) {
        //storeLine(vertexCount - 1, 0);   // do nothing
      }
    }
  }


  public List<TriangleFace> triangleFaces() {
    return faces;
  }


  public void setOriginalModelView(PMatrix theModelView) {
    initialModelViewMatrix = theModelView;
    initialModelViewMatrixInverse = theModelView;
    initialModelViewMatrixInverse.invert();
  }
}
