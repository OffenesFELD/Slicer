import is.feld.slicer.*;

Slicer slicer;
PShape teapot;

void setup() {
  size(600, 600, OPENGL);
  smooth();
  
  teapot = loadShape("Teapot.obj");
  teapot.disableStyle();
  
  slicer = new Slicer(this);
  slicer.setBox(200);  // slice volume is 200x200x200
  slicer.setSlices(20);
}

void draw() {
  background(50);
  
  lights();
  
  pushMatrix();
  
  translate(width * 0.75, height * 0.75); 
  
  noFill();
  stroke(200);    // so we see what's going on
  //slicer.drawBox();
  //slicer.drawSlices();  // uncomment to see where the slices are

  slicer.begin();
  
  scale(0.3);   // the pot is too big
  rotateX(millis() * 0.001);
  rotateY(millis() * 0.0012);

  fill(255, 69, 0);
  noStroke();
  shape(teapot);
  
  slicer.end();
  
  popMatrix();
  
  fill(255);
  PixelSlicer pixelSlicer = slicer.pixelSlicer();
  PImage slice = pixelSlicer.sliceNumber(10);
  
  image(slice, 0, 0);  
}

