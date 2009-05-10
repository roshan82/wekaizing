/**
*  Example of clustering using Weka  (uses the Expectation Maximization algorithm) <br><br>
*
*  Simple applet for showing Clustering<br><br>
*  Left click to create an instance<br><br>
*  Press these keys for different uses:<br>
*    'c' - Clusters the points and draws blobs around them
*    'r' - Clears the canvas, leaving only data points<br>
*
*  Barkin Aygun - 1/3/2009<br><br>
*  Carlos Guestrin - updated on 2/16/09
*
*/

//Imports libraries
import wekaizing.*;

//Declare global variables
WekaData mydata;
WekaClusterer clusterer;

//Setup function gets called once at the beginning
void setup() {
  size(400,400); //Set applet size
  background(255); //Set background color to white
  noStroke(); //Disable edge drawing

  mydata = new WekaData(); //Create a new empty dataset
  mydata.AddAttribute("x"); //Add an x attribute
  mydata.AddAttribute("y"); //Add a y attribute

  clusterer = new WekaClusterer(); //Create a new clusterer
}	

void draw() {
}


// when mouse button is pressed, add point to dataset
void mousePressed(){  
  if (mouseButton == LEFT) {
    noStroke();
    fill(0,0,0);
    ellipse(mouseX, mouseY, 5,5);  
    addPoint(mouseX, mouseY);
  }
}

//Adds the point into the dataset
void addPoint(int x, int y) {
  Object[][] pData = {{x,y}};
  mydata.InsertData(pData);
}



void keyPressed(){
  if (key == 'c' || key == 'C') {
    drawClusters();
  }
  if (key == 'r' || key == 'R') {
    resetCanvas();
  }
}


// runs the clustering algorithm, and draw big circles to mark points in the same cluster
void drawClusters() {

  // run clustering algorithm
  //Get the cluster indices per point
  //Force 4 clusters
  int clusters[] = clusterer.clusterData(mydata, 4); 
  
  // we are going to have one color per cluster
  // at most, the number of clusters = number of data points
  // so we create as many random colors as there are data points 
  color[] colors = new color[clusters.length]; 
  for (int c = 0; c < colors.length; c++) {
    colors[c] = color(random(255), random(255), random(255)); //And randomize each color
  }


  //Get the data into a double matrix, so we know where the points are
  double[][] newpts = mydata.GetDataIntoArray(); 
  
  //Iterate throuch points and draw big blobs of color around them
  for (int i = 0; i < newpts.length; i++) {
    // use the color of the cluster of this data point
    fill(colors[clusters[i]]);
    // draw a circle centered at this point
    ellipse((int)newpts[i][0], (int)newpts[i][1], 100, 100);
  } 
}


//Resets the canvas and draws the normal points
void resetCanvas() {
  background(255);
  double[][] data = mydata.GetDataIntoArray();
  for (int i = 0; i < data.length; i++) {
    fill(0,0,0);
    ellipse((int)data[i][0], (int)data[i][1], 5, 5);
  }
}

