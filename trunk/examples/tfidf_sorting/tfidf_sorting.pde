  
/**
 * Carlos Guestrin
 * 
 * simple similarity applet using TFIDF
 * Author: Carlos Guestrin, Jan 25 2009
 */

import textlib.*;
import java.io.*;

TfIdf tfidf_docs;
String[] docs;


void setup() {
	
	size(800,600);
        
        //read documents from directory
        // will detect documents with extension .txt
          tfidf_docs = new TfIdf(sketchPath + "/data/nytimes");
          tfidf_docs.buildAllDocuments();
          print(tfidf_docs.docSize);  

        //load documents
        docs = tfidf_docs.documentNames();

	resetDocsDisplay();
	
}


/**
 * Redraws all the documents on the screen to erase the similarity numbers
 * documents are drawn in a grid, 200x200.  
 */
void resetDocsDisplay() {
  
        // set up font
	background(255);
        PFont font_12 = loadFont("CourierNew-12.vlw");
        PFont font_20 = loadFont("Courier-Bold-20.vlw");
        

        // for each document, write first sentence and important words
        
        int picx=0, picy=0;
        for (int i = 0; i < docs.length;i++) {
	  if (picx >= width) { 
            picx = 0; 
            picy += 200; 
          }
        
          // read first sentence
          String first_sentence = getFirstSentence(docs[i]);
          fill(0,0,255);
          textFont(font_20, 20);
          text(first_sentence,picx,picy,200,100);

          // read important words for this document
          String[] bwords;
          String full_text;
          // get the most important words in terms of TFIDF
          bwords = tfidf_docs.documents.get(docs[i]).bestWordList(5);
          full_text="";
          for (int j = 0; j < bwords.length; j++) {
            full_text += bwords[j] + "\n";
          }
          		
          // display most important words
          fill(0);
          textFont(font_12, 12);
          text(full_text,picx,picy+100,200,100);
          picx += 200;

        }

}



void draw() {
}


/* when mouse is clicked, figure out what documents was selected
 * there are 4 documents per row, and the documents are 200x200
*/ 
void mousePressed() {
  int picIndex;
    if (mouseButton == LEFT) {
      picIndex = (mouseY / 200) * 4 + mouseX / 200;
      if (picIndex < docs.length) {
	markSimilars(picIndex);
      }
    }
}



/* image "index" was selected
 * sort other documents in order of similarity
 * and number them appropriately
 */
void markSimilars(int index) {
  int rectx, recty;

  // clear previous numberings from images
  resetDocsDisplay();
		
  // Rectangle around the clicked document
  rectx = (index % 4) * 200;
  recty = (index / 4 ) * 200;
  strokeWeight(3.0f);
  noFill();
  stroke(255,0,0);
  rect(rectx, recty, 197, 200);


  // index other documents in order of similarity
  // similar[0] is index of most similar document
  // similar[1] is next more similar
  //...	
  String[]   simDocs = tfidf_docs.similarDocuments(docs[index]);

  // write numbers on documents in order of similarity
  PFont Serif_48;
  Serif_48 = loadFont("Serif-48.vlw");
  textFont(Serif_48, 48); 

  for (int i = 0; i < simDocs.length; i++) {
    println(i);

    // find index of similar docs
    int ind_sim=0;
    for(int j=0;j<docs.length;j++){
      if(docs[j]==simDocs[i]){
        ind_sim=j;
        break;
      }
    }
    
    rectx = (ind_sim % 4) * 200;
    recty = (ind_sim / 4) * 200;
    fill(255,0,0);
    text(i,rectx+120,recty+140);
  }
}



// get first sentence from a document
String getFirstSentence(String document){
  
          //initialize file reader
          String first_sentence = "";
          File f = new File(sketchPath + "/data/nytimes/" + document);
          BufferedReader in=null;
          
          // try to open file, complain if file doesn't exist
          try{            
            in = new BufferedReader(new FileReader(f));
          } catch (FileNotFoundException e) {
            e.printStackTrace();
            println("file not found");
            exit();
          }
          
          //try to read first sentence, complain if document runs out
          try{            
            first_sentence=in.readLine();
          } catch (IOException e) {
            e.printStackTrace();
            println("I/O Error");
            exit();
          }
          
          return first_sentence;
}
