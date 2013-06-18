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

import java.util.List;

import processing.core.PVector;


public class BoundingBox {

  public final float width;
  public final float height;
  public final float depth;

  public final float minX;
  public final float maxX;

  public final float minY;
  public final float maxY;

  public final float minZ;
  public final float maxZ;


  public BoundingBox(float theSize) {
    this(theSize, theSize, theSize);
  }


  public BoundingBox(float theWidth, float theHeight, float theDepth) {
    width = theWidth;
    height = theHeight;
    depth = theDepth;

    minX = -width * 0.5f;
    maxX = -width * 0.5f;

    minY = -height * 0.5f;
    maxY = -height * 0.5f;

    minZ = -depth * 0.5f;
    maxZ = -depth * 0.5f;
  }


  public BoundingBox(List<PVector> points) {
    float minX = Float.MAX_VALUE;
    float maxX = Float.MIN_VALUE;  

    float minY = Float.MAX_VALUE; 
    float maxY = Float.MIN_VALUE;      

    float minZ = Float.MAX_VALUE; 
    float maxZ = Float.MIN_VALUE;  


    for (PVector point: points) {
      minX = Math.min(minX, point.x);
      maxX = Math.max(maxX, point.x);

      minY = Math.min(minY, point.y);
      maxY = Math.max(maxY, point.y);

      minZ = Math.min(minZ, point.z);
      maxZ = Math.max(maxZ, point.z);
    }


    minX = (minX != Float.MAX_VALUE) ? minX : 0;
    maxX = (minX != Float.MIN_VALUE) ? maxX : 0;

    minY = (minY != Float.MAX_VALUE) ? minY : 0;
    maxY = (minY != Float.MIN_VALUE) ? maxY : 0; 

    minZ = (minZ != Float.MAX_VALUE) ? minZ : 0;
    maxZ = (minZ != Float.MIN_VALUE) ? maxZ : 0;    


    this.minX = minX;
    this.maxX = maxX;

    this.minY = minY;
    this.maxY = maxY;    

    this.minZ = minZ;
    this.maxZ = maxZ;

    this.width = maxX - minX;
    this.height = maxY - minY;
    this.depth = maxZ - minZ;
  }


  public PVector topLeft() {
    return new PVector(minX, minY, minZ);
  }


  public PVector bottomRight() {
    return new PVector(maxX, maxY, maxZ);
  }
  
  
  public float maxDimension() {
    return Math.max(width, Math.max(height, depth));
  }
  
  
  public PVector center() {
    PVector center = topLeft().get();
    center.lerp(bottomRight(), 0.5f);
   
    return center;
  }
  
  
  public String toString() {
     return "[" + width + " " + height + " " + depth + "]";
  }
}

