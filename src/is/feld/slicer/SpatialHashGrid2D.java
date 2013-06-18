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

import java.util.*;

public class SpatialHashGrid2D<T extends Locatable2D>  {
  protected final LinkedHashSet<T> elements;
  protected final HashMap<Integer, HashSet<T>> map;
  protected final int resolution;

  protected BoundingRect box = null;

  protected boolean isDirty = false;


  public SpatialHashGrid2D() {
    this(100);
  }


  public SpatialHashGrid2D(int theResolution) {
    elements = new LinkedHashSet<T>();
    map = new HashMap<Integer, HashSet<T>>();
    resolution = theResolution;
  }


  public void add(T theElement) {
    elements.add(theElement);
    isDirty = true;
  }


  public void remove(T theElement) {
    if(isDirty) {
      build();
    }

    elements.remove(theElement);

    Integer hash = hashForElement(theElement);

    if(map.containsKey(hash)) {
      HashSet<T> set = map.get(hash);
      set.remove(theElement);
    }
  }


  public List<T> elements() {
    LinkedList<T> result = new LinkedList<T>();
    result.addAll(elements);

    return result;
  }


  public List<T> getAt(float theX, float theY) {
    if(isDirty) {
      build();
    }

    Integer hash = hashForPosition(theX, theY);

    HashSet<T> bucketElements = map.get(hash);

    if(bucketElements != null) {
      LinkedList<T> result = new LinkedList<T>();
      result.addAll(bucketElements);

      return result;
    } else {
      return new LinkedList<T>();
    }
  }


  public List<T> getAt(T thePoint) {
    return getAt(thePoint.x(), thePoint.y());
  }


  public List<T> getAtExtended(float theX, float theY) {
    if(isDirty) {
      build();
    }

    LinkedList<T> result = new LinkedList<T>();

    for(int xOffset = -1; xOffset <= 1; xOffset++) {
      for(int yOffset = -1; yOffset <= 1; yOffset++) {
        Integer hash = hashForPositionWithOffset(theX, theY, xOffset, yOffset);

        HashSet<T> bucketElements = map.get(hash);

        if(bucketElements != null) {
          result.addAll(bucketElements);
        }
      }
    }

    return result;
  }


  public List<T> getAtExtended(T thePoint) {
    return getAtExtended(thePoint.x(), thePoint.y());
  }


  public void build() {
    map.clear();

    box = new BoundingRect(elements);

    for(T element:elements) {
      Integer hash = hashForElement(element);

      if(!map.containsKey(hash)) {
        map.put(hash, new HashSet<T>());
      }

      map.get(hash).add(element);
    }

    isDirty = false;
  }


  protected Integer hashForElement(T theElement) {
    return hashForPosition(theElement.x(), theElement.y());
  }


  protected Integer hashForPosition(float theX, float theY) {
    int row = Math.round(PApplet.map(theX, box.minX, box.maxX, 0, resolution - 1));
    int column = Math.round(PApplet.map(theY, box.minY, box.maxY, 0, resolution - 1));

    row = PApplet.constrain(row, 0, resolution -1);
    column = PApplet.constrain(column, 0, resolution -1);

    Integer hash = row * resolution + column;
    return hash;
  }


  protected Integer hashForPositionWithOffset(float theX, float theY, int theXOffset, int theYOffset) {
    int row = Math.round(PApplet.map(theX, box.minX, box.maxX, 0, resolution - 1)) + theXOffset;
    int column = Math.round(PApplet.map(theY, box.minY, box.maxY, 0, resolution - 1)) + theYOffset;

    row = PApplet.constrain(row, 0, resolution - 1);
    column = PApplet.constrain(column, 0, resolution -1);

    Integer hash = row * resolution + column;
    return hash;
  }

}
