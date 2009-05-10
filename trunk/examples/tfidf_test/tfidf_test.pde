/**
*  TfIdf test applet.
*  
*  Uses the economist North America set to display 10 articles
*  their top words, and similar documents to one of the articles
*
* Author: Barkin Aygun, Jan 23 2009
* This applet is done as my work for class 10-615 Art That Learns and is not
* related to class work.
*/
import textlib.*;

TfIdf tf;
String[] docs;
int input;
PFont font;
String[] simDocs;

  size(600,300);
  background(255);
  tf = new TfIdf(sketchPath + "/data/na");
  tf.buildAllDocuments();
  print(tf.docSize);  
  docs = tf.documentNames();
  font = loadFont("CourierNew-12.vlw");
  textFont(font, 12);
  fill(0);
  String[] bwords;
  for (int i = 0; i < docs.length;i++) {
    text(docs[i], 10, 10 + i * 15); 
    bwords = tf.documents.get(docs[i]).bestWordList(5);
    for (int j = 0; j < bwords.length; j++) {
      text(bwords[j], 100 + j * 100, 10 + i * 15);
    }
  }
  simDocs = tf.similarDocuments(docs[0]);
  fill(0,255,0);
  text(docs[0], 10, 200);
  fill(255,0,0);
  for (int i = 0; i < 3; i++) {
    text(simDocs[i], 100 + i * 100, 200);
  }

