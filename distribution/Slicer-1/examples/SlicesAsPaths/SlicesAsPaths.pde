import is.feld.slicer.*;

Slicer slicer;

boolean clearGeometry = false;

void setup() {
  size(600, 600, P3D);
  smooth();

  slicer = new Slicer(this);
  slicer.setBox(200);
}

void draw() {
  background(50);
  lights();

  translate(width * 0.5, height * 0.5); 

  randomSeed(23);

  noFill();
  stroke(128);

  rotateY(millis() * 0.001);  // tumble
  rotateZ(millis() * 0.0012);

  slicer.drawBox();
  slicer.begin();

  for (int i = 0; i < 100; i++) {    
    pushMatrix();

    float x = random(-150, 150);
    float y = random(-150, 150);
    float z = random(-150, 150);

    noStroke();
    fill(128);
    translate(x, y, z);
    rotate(i * 0.02);

    sphereDetail(10);
    sphere(random(5, 35));    

    popMatrix();
  }

  slicer.end();
  
  if(clearGeometry) background(50);

  noLights();

  PathSlicer pathSlicer = slicer.pathSlicer();    // get the paths
  float sliceY = map(mouseX, 0, width, -100, 100);
  ArrayList<ContourPath> paths = pathSlicer.sliceAtY(sliceY);

  translate(0, sliceY, 0);   // go to the position of the slice we took
  rotateX(HALF_PI);

  noFill();
  stroke(255, 69, 0);
  strokeWeight(2);
  for (int i = 0; i < paths.size(); i++) {
    ContourPath path = paths.get(i); 

    path.draw(this);
  }
}


void keyPressed() {
  clearGeometry = !clearGeometry;
}

