import is.feld.slicer.*;

Slicer slicer;
PShape soldier;

ArrayList<PShape> shapes;

void setup() {
  size(600, 600, OPENGL);
  smooth();

  shapes = new ArrayList<PShape>();

  soldier = loadShape("soldier.obj");
  soldier.disableStyle();

  slicer = new Slicer(this);
  slicer.setBox(600);  // slice volume is 200x200x200
  slicer.setSlices(200);


  /* Only draw the actual object once ... */
  slicer.begin();
  //rotateX(HALF_PI);
  scale(3);
  shape(soldier);
  slicer.end();


  PathSlicer pathSlicer = slicer.pathSlicer();

  for (int i = 0; i < 200; i++) {
    ArrayList<ContourPath> paths = pathSlicer.sliceNumber(i);
    println(paths.size());

    PShape shape = slicer.pathsToShape(paths); 

    shapes.add(shape);
  }
}

void draw() {
  background(50);


  translate(width * 0.5, height * 0.5);
  rotateX(-HALF_PI);

  for (int i = 0; i < shapes.size(); i++) {
    pushMatrix();
    PShape shape = shapes.get(i);
        
    float z = map(i, 0, shapes.size() - 1, mouseX * 0.5, -mouseX * 0.5);
    translate(0, 0, z);
    
    float t = millis() * 0.001 + i * 0.01;
    float rotation = map( sin(t), -1, 1, -1, 1);
    rotate(rotation); 
    
    shape.disableStyle();
    stroke(255);
    noFill();
    shape(shape);
    
    popMatrix();
  }
}

