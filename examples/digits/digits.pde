 /**
* Using a dataset of 400 and 100 hand drawn digits of size 20x20,
* the applet trains on the 400 digits using a Weka classifier.
* Then classifies all the 100 test data.
* Displays the digits in a grid with the guess on upper left, and actual value on upper right.
*
* Barkin Aygun - Feb 10 2009
* Carlos Guestrin - updated on Feb 16 2009
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
  PImage digit;
  int[] pixeldata; 
  
  //The pixel data is kept 101 long for 10x10 pixels and 1 number
  public digitImage() {
    pixeldata = new int[101];}
}

WekaData digitsTrain;
WekaData digitsTest;
WekaClassifier classifier;
digitImage[] digits;

int[] results;
PFont courier_font;



void setup() {
  
  // initialize screen and font
  background(0);
  size(560,560);  
  courier_font = loadFont("CourierNew-12.vlw");
  textFont(courier_font, 15);
  
  
  //initialize data structures for datasets into weka format 
  digitsTrain = new WekaData();
  digitsTest = new WekaData();
  
  // create an attribute for each pixel
  for (int i = 0; i < 100; i++) {
      digitsTrain.AddAttribute(Integer.toString(i));
      digitsTest.AddAttribute(Integer.toString(i));
  }
  
  // create an attribute for the class (digit)
  Object[] digitarray = new Object[] {0,1,2,3,4,5,6,7,8,9};
  digitsTrain.AddAttribute("digit",digitarray);
  digitsTest.AddAttribute("digit",digitarray);

  // load data from disk into data structure  
  loadDigits("digits");

  // set the class variable to be the last attribute
  digitsTrain.setClassIndex(100);
  digitsTest.setClassIndex(100);
  
  
  //
  //Train the classifier
  classifier = new WekaClassifier(WekaClassifier.LOGISTIC);
  classifier.Build(digitsTrain);
  
  print("Training done");
  
  //Test the 100 digits
  results = classifier.Classify(digitsTest);
  print("Classification done");

  
  drawResults(); 
}





// load digits from disk 
void loadDigits(String digitfolder) {

  // open file for digit images
  File digitfiles = new File(sketchPath, "data/" + digitfolder);
  String[] files = digitfiles.list(filter);
  digits = new digitImage[files.length];

  // digits.txt contains the actual true class labels for the digit images
  String numbers[] = loadStrings(digitfolder + "/digits.txt");

  // for each image file
  for (int i = 0; i < files.length; i++) {
    println("Loading image " + files[i]);
    
    // load digit image (used for displaying)
    digits[i] = new digitImage();
    digits[i].digit = loadImage("data/" + digitfolder + "/" + files[i]);
    
    // load true class for digit
    digits[i].number = Integer.valueOf(numbers[i]);
    
    // get a 10x10 version of the image, will be used as feature for classifier
    // lower resolution means we can learn from less data (though we could be more
    // accurate with higher resolution if we had more data)
    PImage resizedImg = loadImage("data/" + digitfolder + "/" + files[i]);
    resizedImg.resize(10,10);
    
    // copy the pixels of lower resolution data into data structure that will be
    // used by Weka
    resizedImg.loadPixels();
    for (int j = 0; j < 100; j++) {
      digits[i].pixeldata[j] = resizedImg.pixels[j];
    }
    // the last entry is the true label
    digits[i].pixeldata[100] = digits[i].number;
    

    // the first 100 digits will be used for testing, the rest as training data. 
    if (i < 100) {
      digitsTest.InsertData(digits[i].pixeldata);
    } else { //Otherwise training
      digitsTrain.InsertData(digits[i].pixeldata);
    }
  }   
}


// Draws the digits in a grid
void drawResults() {
  // count the number of digits we got correct
  float num_correct = 0.0, total = 0.0;
  
  // display each image, 10 per row
  int imgx, imgy;
  for (int i = 0; i < 100; i++) {
    //Center every digit on its 56x56 grid
    imgx = (i % 10) * 56 + 14;
    imgy = (i / 10) * 56 + 14;
    image(digits[i].digit,imgx,imgy);
  }
  
  
  //Display the predicted and actual digits above the drawings
  for (int i = 0; i < 100; i++) {
    imgx = (i % 10) * 56 + 14;
    imgy = (i / 10) * 56 + 14;

    //actual value
    fill(255,0,0);    
    text(digits[i].number, imgx, imgy); 

    // predicted value
    fill(0,255,0);
    text(results[i], imgx + 28, imgy);
    
    // if actual value and predicted value are the same, then we got this digit correct
    total += 1.0;
    if(digits[i].number == results[i])
      num_correct += 1.0;
  }

  // print accuracy   
  println("\n" + "Accuracy = " + num_correct/total*100 + "%");
}



// filter for png files
FilenameFilter filter = new FilenameFilter() {
	public boolean accept(File dir, String name) {
		if (name.toLowerCase().endsWith(".png")) return true;
		return false;
	}
};

