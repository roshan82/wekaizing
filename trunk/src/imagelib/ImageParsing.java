package imagelib;

import processing.core.*;

/**
 * Class that contains image to data parsing functions such as hue histogram, value histogram
 * <br>
 * They are all static functions, therefore are passed in the Processing Applet, the image 
 * and required variables and return the result directly
 * 
 * @author Barkin Aygun
 *
 */
public class ImageParsing {
	public static final char RED = 0;
	public static final char GREEN = 1;
	public static final char BLUE = 2;
	public static final char ALL = 3;
	
	/**
	 * Returns the value histogram of a PImage as a double array
	 * 
	 * @param app Processing Applet calling the function
	 * @param img PImage to be converted
	 * @return an array of doubles that contains 256 bins
	 */
	public static double[] hist(PApplet app, PImage img) 
	{
		return histBW(app, img, 256);
	}
	
	/** 
	 * Override for hist function using the int array of pixels instead of the image
	 * 
	 * @param app Processing Applet calling the function
	 * @param pixels Array of integers containing pixels of image
	 * @return an array of doubles that contains 256 bins
	 */
	public static double[] hist(PApplet app, int[] pixels) 
	{
		return histBW(app, pixels, 256);
	}
	
	/**
	 * Returns the value histogram of a PImage as a double array
	 * 
	 * @param app Processing Applet calling the function
	 * @param img PImage to be converted
	 * @return an array of doubles that contains 256 bins
	 */
	public static double[] histBW(PApplet app, PImage img) 
	{
		return histBW(app, img, 256);
	}
	
	/** 
	 * Override for histBW function using the int array of pixels instead of the image
	 * 
	 * @param app Processing Applet calling the function
	 * @param pixels Array of integers containing pixels of image
	 * @return an array of doubles that contains 256 bins
	 */
	public static double[] histBW(PApplet app, int[] pixels) 
	{
		return histBW(app, pixels, 256);
	}
	
	/**
	 * Returns the histogram of the image after converting it to black and white
	 * with the specified number of bins
	 * 
	 * @param app Processing Applet calling the function
	 * @param img PImage to be converted
	 * @param bins Number of bins to have in the histogram
	 * @return an array of doubles that contains [bins] bins
	 */
	public static double[] histBW(PApplet app, PImage img, int bins)
	{
		PImage bwimg = img;
		bwimg.filter(PConstants.GRAY);
		
		int[] inthist = new int[bins];
		double[] histogram = new double[bins];
		
		int mColor;
		
		for (int i = 0; i < img.pixels.length; i++)
		 {
				mColor = bwimg.pixels[i] >> 16 & 0xFF;
				inthist[(int) Math.floor(mColor * bins / 256)]++;
			}
		
		for (int i = 0; i < bins; i++) {
			histogram[i] = (double)inthist[i] / (img.pixels.length);
		}
		
		return histogram;
	}
	
	/**
	 * Override for histBW function that uses the pixel array instead of PImage
	 * 
	 * @param app Processing Applet calling the function
	 * @param pixels Array of integers containing pixels of the image
	 * @param bins Number of bins to have in the histogram
	 * @return an array of doubles that contains [bins] bins
	 */
	public static double[] histBW(PApplet app, int[] pixels, int bins)
	{
		
		int[] inthist = new int[bins];
		double[] histogram = new double[bins];
		
		int mColor;
		
		for (int i = 0; i < pixels.length; i++)
		 	{
				mColor = (int) ((pixels[i] >> 16 & 0xFF) * 0.2989 +
						 (pixels[i] >> 8 & 0xFF) * 0.5870 +
						 (pixels[i] & 0xFF) * 0.1140);
				inthist[(int) Math.floor(mColor * bins / 256)]++;
			}
		
		for (int i = 0; i < bins; i++) {
			histogram[i] = (double)inthist[i] / (pixels.length);
		}
		
		return histogram;
	}
	
	/**
	 * Returns the color histogram of the image as a single array of doubles 
	 * where it is bins * 3 length, if COLOR is specified to be anything, than
	 * it is only bins length for that color value
	 * 
	 * @param app Processing Applet calling the function
	 * @param img PImage to be converted
	 * @param bins Number of bins in the histogram
	 * @param COLOR RED/GREEN/BLUE/ALL depending on what user wants
	 * 
	 * @return array of doubles containing the histogram
	 */
	public static double[] histColor(PApplet app, PImage img, int bins, int COLOR)
	{
		int[] rhist = new int[bins];
		int[] ghist = new int[bins];
		int[] bhist = new int[bins];
		
		double[] histogram;
		
		
		int r,g,b;
		
		img.loadPixels();
		
		for (int i = 0; i < img.pixels.length; i++)
		{
			r = img.pixels[i] >> 16 & 0xFF;
			g = img.pixels[i] >> 8 & 0xFF;
			b = img.pixels[i] & 0xFF;
			rhist[(int) Math.floor(r * bins / 256)]++;
			ghist[(int) Math.floor(g * bins / 256)]++;
			bhist[(int) Math.floor(b * bins / 256)]++;
		}
		
		if (COLOR == ALL) {
			histogram = new double[bins * 3];
			for (int i = 0; i < bins; i++) {
				histogram[i] = (double)rhist[i] / img.pixels.length;
				histogram[bins+i] = (double)ghist[i] / img.pixels.length;
				histogram[bins+bins+i] = (double)bhist[i] / img.pixels.length;
			}
		}
		else if (COLOR == RED){
			histogram = new double[bins];
			for (int i = 0; i < bins; i++) {
				histogram[i] = (double)rhist[i] / img.pixels.length;
			}
		}
		else if (COLOR == GREEN){
			histogram = new double[bins];
			for (int i = 0; i < bins; i++) {
				histogram[i] = (double)ghist[i] / img.pixels.length;
			}
		}
		else if (COLOR == BLUE){
			histogram = new double[bins];
			for (int i = 0; i < bins; i++) {
				histogram[i] = (double)bhist[i] / img.pixels.length;
			}
		}
		else
		{
			histogram = new double[1];
		}
		
		return histogram;
	}

	/**
	 * Override for histColor function using the pixel array of the image
	 * 
	 * @param app Processing Applet calling the function
	 * @param pixels Array of pixels of the image to be converted
	 * @param bins Number of bins in the histogram
	 * @param COLOR RED/GREEN/BLUE/ALL depending on what user wants
	 * 
	 * @return array of doubles containing the histogram
	 */
	public static double[] histColor(PApplet app, int[] pixels, int bins, int COLOR)
	{
		int[] rhist = new int[bins];
		int[] ghist = new int[bins];
		int[] bhist = new int[bins];
		
		double[] histogram;
		
		
		int r,g,b;
		
		for (int i = 0; i < pixels.length; i++)
		{
			r = pixels[i] >> 16 & 0xFF;
			g = pixels[i] >> 8 & 0xFF;
			b = pixels[i] & 0xFF;
			rhist[(int) Math.floor(r * bins / 256)]++;
			ghist[(int) Math.floor(g * bins / 256)]++;
			bhist[(int) Math.floor(b * bins / 256)]++;
		}
		
		if (COLOR == ALL) {
			histogram = new double[bins * 3];
			for (int i = 0; i < bins; i++) {
				histogram[i] = (double)rhist[i] / pixels.length;
				histogram[bins+i] = (double)ghist[i] / pixels.length;
				histogram[bins+bins+i] = (double)bhist[i] / pixels.length;
			}
		}
		else if (COLOR == RED){
			histogram = new double[bins];
			for (int i = 0; i < bins; i++) {
				histogram[i] = (double)rhist[i] / pixels.length;
			}
		}
		else if (COLOR == GREEN){
			histogram = new double[bins];
			for (int i = 0; i < bins; i++) {
				histogram[i] = (double)ghist[i] / pixels.length;
			}
		}
		else if (COLOR == BLUE){
			histogram = new double[bins];
			for (int i = 0; i < bins; i++) {
				histogram[i] = (double)bhist[i] / pixels.length;
			}
		}
		else
		{
			histogram = new double[1];
		}
		
		return histogram;
	}

	/**
	 * Override of histColor for default selection of 256 bins and all colors
	 */
	public static double[] histColor(PApplet app, PImage img) {
		return histColor(app, img, 256, ALL);
	}
	
	/**
	 *  Override of histColor for default selection of 256 bins and all colors
	 */
	public static double[] histColor(PApplet app, int[] pixels) {
		return histColor(app, pixels, 256, ALL);
	}
	
	/**
	 *  Override of histColor for default selection of all colors 
	 *  with specified number of bins
	 */
	public static double[] histColor(PApplet app, PImage img, int bins) {
		return histColor(app, img, bins, ALL);
	}
	
	/**
	 *  Override of histColor for default selection of and all colors
	 *  with specified number of bins
	 */
	public static double[] histColor(PApplet app, int[] pixels, int bins) {
		return histColor(app, pixels, bins, ALL);
	}
	
	/**
	 *  Override of histColor for default selection of 256 bins with
	 *  specified color selection
	 */
	public static double[] histColor(PApplet app, PImage img, char COLOR) {
		return histColor(app, img, 256, COLOR);
	}
	
	/**
	 * Creates the hue histogram of the image with given number of bins and whether to 
	 * include dark pixels or not
	 *  
	 * @param app Processing Applet calling the function
	 * @param img PImage to be converted
	 * @param hueLim Number of bins to have
	 * @param showBlack Set to true to include black pixels or not
	 * @return array of doubles as the hue histogram
	 */
	public static double[] histHue(PApplet app, PImage img, int hueLim, boolean showBlack)
	{
		int[] inthist = new int[hueLim];
		double[] histogram = new double[hueLim];
		int pixCount = 0;
		
		img.loadPixels();
		app.colorMode(PConstants.HSB, hueLim, 100, 100);
		int mColor;
		int mBrightness;
		for (int i = 0; i < img.pixels.length; i++) {
			mColor = (int) app.hue(img.pixels[i]);
			if (!showBlack) {
				mBrightness = (int) app.brightness(img.pixels[i]);
				if (mBrightness >= 40) {
					inthist[mColor]++;
					pixCount++;
				}
			} else {
				inthist[mColor]++;
				pixCount++;
			}
		}
		
		for (int i = 0; i < hueLim; i++) {
			histogram[i] = (double)inthist[i] / pixCount;
		}
		
		
		return histogram;	
	}
	
	/**
	 * Creates the hue histogram of the image with given number of bins and whether to 
	 * include dark pixels or not
	 *  
	 * @param app Processing Applet calling the function
	 * @param pixels Array of pixels of the image to be converted
	 * @param hueLim Number of bins to have
	 * @param showBlack Set to true to include black pixels or not
	 * @return array of doubles as the hue histogram
	 */
	public static double[] histHue(PApplet app, int[] pixels, int hueLim, boolean showBlack)
	{
		int[] inthist = new int[hueLim];
		double[] histogram = new double[hueLim];
		
		int pixCount = 0;
		int mColor;
		int mBrightness;
		app.colorMode(PConstants.HSB, hueLim, 100, 100);
		for (int i = 0; i < pixels.length; i++) {
			mColor = (int) app.hue(pixels[i]);
			if (!showBlack) {
				mBrightness = (int) app.brightness(pixels[i]);
				if (mBrightness >= 40) {
					inthist[mColor]++;
					pixCount++;
				}
			} else {
				inthist[mColor]++;
				pixCount++;
			}
		}
		
		for (int i = 0; i < hueLim; i++) {
			histogram[i] = (double)inthist[i] / (pixCount);
		}
		
		return histogram;	
	}

	/**
	 * Override for hue histogram function using default values of 360 bins and show black
	 * 
	 * @param app Processing Applet calling the function
	 * @param img PImage to be converted
	 * @return array of doubles of the hue histogram
	 */
	public static double[] histHue(PApplet app, PImage img) {
		return histHue(app, img, 360, true);
	}
	
	/**
	 * Override for hue histogram function using default values of 360 bins and show black
	 * 
	 * @param app Processing Applet calling the function
	 * @param pixels Array of pixels of the image to be converted
	 * @return array of doubles of the hue histogram
	 */
	public static double[] histHue(PApplet app, int[] pixels) {
		return histHue(app, pixels, 360, true);
	}
	
	/**
	 * Override for hue histogram function using default value of show black
	 * with specified number of bins
	 * 
	 * @param app Processing Applet calling the function
	 * @param img PImage to be converted
	 * @param hueLim Number of hue bins
	 * @return array of doubles of the hue histogram
	 */
	public static double[] histHue(PApplet app, PImage img, int hueLim) {
		return histHue(app, img, hueLim, true);
	}
	
	/**
	 * Override for hue histogram function using default values of show black
	 * with specified number of bins
	 * 
	 * @param app Processing Applet calling the function
	 * @param pixels Array of pixels of the image to be converted
	 * @param hueLim Number of hue bins
	 * @return array of doubles of the hue histogram
	 */
	public static double[] histHue(PApplet app, int[] pixels, int hueLim) {
		return histHue(app, pixels, hueLim, true);
	}
	
	/**
	 * Override for hue histogram function using default value of 360 bins
	 * with specified setting of showing black
	 * 
	 * @param app Processing Applet calling the function
	 * @param img PImage to be converted
	 * @param showBlack boolean to whether count dark pixels or not
	 * @return array of doubles of the hue histogram
	 */
	public static double[] histHue(PApplet app, PImage img, boolean showBlack) {
		return histHue(app, img, 360, showBlack);
	}
	
	/**
	 * Override for hue histogram function using default values of 360 bins
	 * with specified setting of showing black
	 * 
	 * @param app Processing Applet calling the function
	 * @param pixels Array of pixels of the image to be converted
	 * @param showBlack boolean to whether count dark pixels or not
	 * @return array of doubles of the hue histogram
	 */
	public static double[] histHue(PApplet app, int[] pixels, boolean showBlack) {
		return histHue(app, pixels, 360, showBlack);
	}
	
}
