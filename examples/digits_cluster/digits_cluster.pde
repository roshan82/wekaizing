 /**
* Using a dataset of 100 hand drawn digits of size 20x20,
* the applet trains on the clusters these images just based on pixels
* Displays the digits in each cluster
*
* Carlos Guestrin - March 25 2009
* This applet and the libraries are used for 10-615 Art That Learns class.
*/

import wekaizing.*;
import java.io.File;
import java.lang.Integer;

// The class we use to store our digits from files
// Contains the number digit stands for, the image of the digit
// And the resized pixel data of the digit
class digitImage {
  int number;
  PImage digit_image;
  int[] pixeldata;   

  //The pixel data is kept image_size*image_size
  public digitImage(int image_size) {
    pixeldata = new int[image_size*image_size];}
  }


WekaData digits_data;
WekaClusterer clusterer;

digitImage[] digits;

// we will cluster 100 digit images
int NUM_DIGITS = 300;

// trainint images given to clusterer will be 10x10
int TRAIN_IMAGE_SIZE = 20;

// number of clusters used:
int NUM_CLUSTERS = 10;
int[] clusters;

PFont courier_font;



void setup() {
  
  // initialize screen and font
  background(0);
  size(800,800);  
  courier_font = loadFont("CourierNew-12.vlw");
  textFont(courier_font, 15);
  
  
  //initialize data structures for datasets into weka format 
  digits_data = new WekaData();
  
  // create an attribute for each pixel
  for (int i = 0; i < TRAIN_IMAGE_SIZE*TRAIN_IMAGE_SIZE; i++) {
      digits_data.AddAttribute(Integer.toString(i));
  }
  
  // load data from disk into data structure  
  loadDigits("digits");

  //Train the clusterer
  clusterer = new WekaClusterer(WekaClusterer.EM); //Create a new clusterer
    
  clusters = clusterer.clusterData(digits_data,NUM_CLUSTERS); 
    
  print("Training done");
  
  drawResults(); 
}





// load digits from disk 
void loadDigits(String digitfolder) {

  // open file for digit images
  File digitfiles = new File(sketchPath, "data/" + digitfolder);

  String[] files = digitfiles.list(filter);
  
  if(files.length < NUM_DIGITS)
    NUM_DIGITS = files.length;
    
  digits = new digitImage[NUM_DIGITS];

  // digits.txt contains the actual true class labels for the digit images
  String numbers[] = loadStrings(digitfolder + "/digits.txt");

  // for each image file
  for (int i = 0; i<NUM_DIGITS ; i++) {
    println("Loading image " + files[i]);
    
    // load digit image (used for displaying)
    digits[i] = new digitImage(TRAIN_IMAGE_SIZE);
    digits[i].digit_image = loadImage("data/" + digitfolder + "/" + files[i]);
    
    // load true class for digit
    digits[i].number = Integer.valueOf(numbers[i]);
    
    // get a 10x10 version of the image, will be used as feature for classifier
    // lower resolution means we can learn from less data (though we could be more
    // accurate with higher resolution if we had more data)
    PImage resizedImg = loadImage("data/" + digitfolder + "/" + files[i]);
    resizedImg.resize(TRAIN_IMAGE_SIZE,TRAIN_IMAGE_SIZE);
    
    // copy the pixels of lower resolution data into data structure that will be
    // used by Weka
    resizedImg.loadPixels();
    for (int j = 0; j < TRAIN_IMAGE_SIZE*TRAIN_IMAGE_SIZE; j++) {
      digits[i].pixeldata[j] = resizedImg.pixels[j];
    }   

    // add to weka data structure
    digits_data.InsertData(digits[i].pixeldata);
  }   
}


// Draws the digits in a grid
// with a white line between clusters

void drawResults() {

  // display each image, 10 per row
  int imgx=0, imgy=0;


  for (int j=0;j<NUM_CLUSTERS;j++)
  {
    for (int i = 0; i < digits.length; i++) {
      if(clusters[i] == j)
      {
        image(digits[i].digit_image,imgx,imgy);
        
        imgx += digits[0].digit_image.width;
        if(imgx>width-digits[0].digit_image.width)
        {
          imgx = 0;
          imgy += digits[0].digit_image.height;
        }
      }
    }
   //draw a horizontal line between clusters
   imgx = 0;
   imgy += digits[0].digit_image.height*3/2;      
   stroke(0,255,0);
   line(0,imgy,width,imgy);
   imgy += digits[0].digit_image.height/2;      
  }
  
}



// filter for png files
FilenameFilter filter = new FilenameFilter() {
	public boolean accept(File dir, String name) {
		if (name.toLowerCase().endsWith(".png")) return true;
		return false;
	}
};

