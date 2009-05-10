/**
 * String Data example
 * A simple example that shows how to use string data in Wekaizing and how to convert it to
 * usable data (such as nominal and tfidf)
 * 
 * Barkin Aygun - 2009
 **/

import wekaizing.*;
import weka.filters.unsupervised.attribute.StringToNominal;
import weka.filters.unsupervised.attribute.StringToWordVector;

WekaData myd;
WekaData nomdata;
WekaData tfidfdata;
void setup()
{

  //We initialize and create our dataset
  myd = new WekaData(4, 5);
  nomdata = new WekaData();
  tfidfdata = new WekaData();
  //These are the filter we are using
  StringToNominal fil = new StringToNominal();
  StringToWordVector fil2 = new StringToWordVector();
  
  //Mind the AddStringAttribute function, 
  //AddAttribute only works for numeric attributes
  myd.AddStringAttribute("word1");
  myd.AddStringAttribute("word2");
  myd.AddStringAttribute("word3");
  Object[] classes = {0,1};
  //We add a nominal attribute as a class
  myd.AddAttribute("class", classes); 

  //We add two data points, just for show
  myd.InsertData(new Object[]{"a","b","c",0});
  myd.InsertData(new Object[]{"a","b","d",1});

  //Options line is to set the filter to use all attributes
  //By default String to nominal only edits the last attribute
  String[] options = {"-R", "first-last"};

  println("Before filtering");
  println(myd.myInstances.toString());
  println("\n\n");
  
  try {
  fil.setOptions(options);
  fil.setInputFormat(myd.myInstances);
    nomdata.myInstances = StringToNominal.useFilter(myd.myInstances, fil);
  } catch (Exception e) {
    e.printStackTrace();
  }
  println("After Nominal filtering"); 
  println(myd.myInstances.toString());
  
  println("Check the attribute set above the data, attribute word3"+
          " is now a set with possible values c and d");
  
  try {
  //fil2.setOptions(options);
  fil2.setInputFormat(myd.myInstances);
    tfidfdata.myInstances = StringToWordVector.useFilter(myd.myInstances, fil2);
  } catch (Exception e) {
    e.printStackTrace();
  }
  println("After WordVector filtering"); 
  println(tfidfdata.myInstances.toString());
  
  println("Values might not make any sense, but check the attributes,"+
          " there is an attribute for every string we had in the data,"+
          " which is what tfidf does");
 
}
