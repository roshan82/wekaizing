/**
*  ExampleWekaClassifier <br><br>
*
*  Simple applet illustrating a classification algorithm<br><br>
*  Left click to create an instance with class 0, right for 1<br><br>
*  Press these keys for different uses:<br>
*    'c' - Classifies the point under the mouse and draws
*          an empty circle with the class color<br>
*    'a' - Draws the classification areas<br>
*    'r' - Clears the canvas, leaving only data points<br>
*
*  Barkin Aygun - 12/20/2008 - 5:00 PM<br><br>
*  Carlos Guestrin - updated on 2/16/09
*
*/

//Import Wekaizing libraries
import wekaizing.*;

//Declare the weka variables
WekaData mydata;
WekaClassifier classifier;

//This is the setup method, it is called at the beginning of the applet once
void setup() {
  size(400,400); //Set size of applet to 400 x 400
  background(255); //Set background color to white
  noStroke(); //Set all drawings to no stroke, so no edges are drawn
  
  mydata = new WekaData(); //Initialize a WekaData with empty attributes and dataset
  mydata.AddAttribute("x"); //Add an x attribute that is numeric
  mydata.AddAttribute("y"); //Add a y attribute that is numeric
  Object[] classes = {0,1};
  mydata.AddAttribute("class",classes); //Add class attribute
  classifier = new WekaClassifier(WekaClassifier.KSTAR); //Initialize a new classifier with KStar algorithm
}	

void draw(){
}


// when a mouse is pressed, add a new data point
void mousePressed(){
  noStroke();
  // If left button clicked, create a point of class 0
  if (mouseButton == LEFT) {
    fill(0,255,0);
    ellipse(mouseX, mouseY,5,5);  
    addPoint(mouseX, mouseY, 0);
  }
  // If right button clicked, create a point of class 1
  if (mouseButton == RIGHT) {
    fill(255,0,0);
    ellipse(mouseX, mouseY,5,5);  
    addPoint(mouseX, mouseY, 1);
  }  
}

//This function saves the point into the Weka Dataset
void addPoint(int x, int y, int pclass) {
  //Create an Object array of {x,y,class} (remember setup() where we created attributes)
  Object[] pData = {x,y, pclass};
  //Insert that data into the dataset
  mydata.InsertData(pData);
  //Build the classifier, 
  //this call trains the classifier model so that later on for other instances, 
  //the class can be predicted
  classifier.Build(mydata);
}



  // If a key is pressed on keyboard
void keyPressed() {
  // Calling seperate methods instead of writing the functionality here is a much cleaner method and highly advised
  if (key == 'c' || key == 'C') {
    drawClassify(mouseX,mouseY);
  }
  
  if (key == 'a' || key == 'A') {
    for(int x=5;x<width;x+=10)
      for(int y=5;y<height;y+=10) 
        drawClassify(x,y);
  }

  if (key == 'r' || key == 'R') {
    resetCanvas();
  }
}

//This function predicts which class point belongs to and draws an empty circle with edges colored to that class
void drawClassify(int x, int y) {
  //Create an instance to predict the class. 
  // the class part, in this case 0, will be ignored, since we are actually predicting at point x,y
  Object[] pData = {x, y, 0};
  
  //Predict the class
  int pred = classifier.Classify(pData);

  //Draw an empty circle according to class
  if (pred == 0) {
    stroke(0,255,0);
    noFill();
    ellipse(x, y, 5,5);  
  }
  else {
    stroke(255,0,0);
    noFill();
    ellipse(x, y, 5,5);  
  }
}

//This function cleans all predicted points, and leaves the user added points
void resetCanvas() {
  //Draw a white background (over everything)
  background(255);
  //Gets all the dataset into a 2D array of doubles
  double[][] data = mydata.GetDataIntoArray();
  //We go through every instance in the data set
  for (int i = 0; i < data.length; i++) {
    noStroke();
    //And using the hack above, draw a circle, this basically resets the canvas
    fill(255*(int)data[i][2], 255*(int)(1-data[i][2]),0);
    ellipse((int)data[i][0], (int)data[i][1], 5, 5);
  }
}


