import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
public class image_reader {
	public static void main(String[] args) 
	{
		String fileName = args[0];
		int width = 352;
		int height = 288;
		int imgLength = width * height;
		int Y = Integer.parseInt(args[1]); 
		int U = Integer.parseInt(args[2]);
		int V = Integer.parseInt(args[3]);
		int Q = Integer.parseInt(args[4]);
		double[] YUV = new double[]{0,0,0};
		double[] RGB = new double[]{0,0,0};
		double[][] IMG = new double[3][imgLength];
		double R, G, B;
		byte a;
		byte r, g, b;

		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		BufferedImage img_out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		try {
			File file = new File(args[0]);
			InputStream is = new FileInputStream(file);
			long len = file.length();
			byte[] bytes = new byte[(int)len];
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
				offset += numRead;
			}

			int j=0;
			int ind = 0;

			//Placing RGB into array and setting first image
			for(int y = 0; y < height; y++){
				for(int x = 0; x < width; x++){
					a = 0;
					r = bytes[ind];
					g = bytes[ind+height*width];
					b = bytes[ind+height*width*2]; 
					R = (double)r;
					G = (double)g;
					B = (double)b;

					IMG[0][j] = R;
					IMG[1][j] = G;
					IMG[2][j] = B;

					int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					//int pix = ((a << 24) + (r << 16) + (g << 8) + b);
					img.setRGB(x,y,pix);
					ind++;  

					if(j < imgLength)
						j++;
					else
						j = imgLength;
				}
			}

			//RGB to YUV
			j=0;
			for(int y = 0; y < height; y++){
				for(int x = 0; x < width; x++){

					RGB[0] = IMG[0][j];
					RGB[1] = IMG[1][j];
					RGB[2] = IMG[2][j];

					YUV[0] = 0.299*RGB[0] + 0.587*RGB[1] + 0.114*RGB[2];
					YUV[1] = -0.147*RGB[0] - 0.289*RGB[1] + 0.436*RGB[2];
					YUV[2] = 0.615*RGB[0] - 0.515*RGB[1] - 0.100*RGB[2];					

					IMG[0][j] = YUV[0];
					IMG[1][j] = YUV[1];
					IMG[2][j] = YUV[2];

					if(j < imgLength)
						j++;
					else
						j = imgLength;
				}
			}

			//Subsample
			IMG = subsample(IMG,Y,U,V,height,width);

			//Upsample
			IMG = upsample(IMG,Y,U,V,height,width);

			//YUV to RGB
			j=0;
			double MIN_VALUE = -128;
			double MAX_VALUE = 127;
			for(int y = 0; y < height; y++){
				for(int x = 0; x < width; x++){
					//IMG now contains YUV image
					YUV[0] = IMG[0][j];
					YUV[1] = IMG[1][j];
					YUV[2] = IMG[2][j];

					RGB[0] = 0.999*YUV[0] + 0.000*YUV[1] + 1.140*YUV[2];
					RGB[1] = 1.000*YUV[0] - 0.395*YUV[1] - 0.581*YUV[2];
					RGB[2] = 1.000*YUV[0] + 2.032*YUV[1];

					for (int k = 0; k <= 2; k++)
					{
						IMG[k][j] = Math.floor(RGB[k]);
						//Ensuring Limits
						if(IMG[k][j]>MAX_VALUE)
						{
							IMG[k][j] = 126 ;
						}
						else if(IMG[k][j]<MIN_VALUE)
						{
							IMG[k][j] = -127;
						}
					}

					if(j < imgLength)
						j++;
					else
						j = imgLength;
				}
			}

			//Quantization
			Quantize(IMG, Q, height, width);

			//Output Subsampled and Quantized RGB image 
			j=0;
			ind = 0;
			for(int y = 0; y < height; y++){
				for(int x = 0; x < width; x++){

					a = 0;
					if(Q==256){
						IMG[0][j] = 4* (Math.floor(IMG[0][j]/4) + 0.5);
						IMG[1][j] = 4* (Math.floor(IMG[1][j]/4) + 0.5);
						IMG[2][j] = 4* (Math.floor(IMG[2][j]/4) + 0.5);
					}
					r = (byte)IMG[0][j];
					g = (byte)IMG[1][j];
					b = (byte)IMG[2][j];

					int pix1 = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					img_out.setRGB(x,y,pix1);
					ind++; 

					if(j < imgLength)
						j++;
					else
						j = imgLength;
				}
			}		

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Use a panel and label to display the image
		JPanel  panel = new JPanel ();
		panel.add (new JLabel (new ImageIcon (img)));
		panel.add (new JLabel (new ImageIcon (img_out)));
		JFrame frame = new JFrame("Display images");
		frame.getContentPane().add (panel);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
	}

	public static double[][] subsample(double[][] IMG,int Y,int U,int V, int height, int width) 
	{
		int j=0;
		int imgLength = width * height;
		double[] YUV = new double[]{0,0,0};

		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){

				YUV[0] = IMG[0][j];
				YUV[1] = IMG[1][j];
				YUV[2] = IMG[2][j];

				if(j % Y!=0)
				{
					YUV[0] = IMG[0][j];
				}
				if(j % U!=0)
				{
					YUV[1] = IMG[0][j];
				}
				if(j % V!=0)
				{
					YUV[2] = IMG[0][j];
				}

				IMG[0][j] = YUV[0];
				IMG[1][j] = YUV[1];
				IMG[2][j] = YUV[2];

				if(j < imgLength)
					j++;
				else
					j = imgLength;
			}
		}
		return IMG;
	}

	public static double[][] upsample(double[][] IMG,int Y,int U,int V, int height, int width) 
	{
		int j=1;
		int imgLength = width * height;
		double Y1,U1,V1;

		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				if(j<imgLength-1)
				{
					Y1 = IMG[0][j];
					U1 = IMG[1][j];
					V1 = IMG[2][j];

					if(j % Y!=0)
					{
						Y1 = (IMG[0][j-1] + IMG[0][j+1])/2;
					}
					if(j % U!=0)
					{
						U1 = (IMG[1][j-1] + IMG[1][j+1])/2;
					}
					if(j % V!=0)
					{
						V1 = (IMG[2][j-1] + IMG[2][j+1])/2;
					}
					/*Code for better upsampling
					 * 
					 * if(j % Y!=0 && j<imgLength)
					{
						Y1 = IMG[0][j-1];
					}
					if(j % U!=0 && j<imgLength-1)
					{
						U1 = IMG[1][j-1];
					}
					if(j % V!=0 && j<imgLength-1)
					{
						V1 = IMG[2][j-1];
					} */
				}
				else
				{
					Y1 = IMG[0][j-1];
					U1 = IMG[1][j-1];
					V1 = IMG[2][j-1];
				}
				try{
					IMG[0][j] = Y1;
					IMG[1][j] = U1;
					IMG[2][j] = V1;
				}
				catch(Exception e)
				{
				}

				if(j < imgLength)
					j++;
				else
					j = imgLength;
			}
		}
		return IMG;
	}

	public static double[][] Quantize(double[][] IMG, int Q, int height, int width)
	{
		int j=0;
		int imgLength = width * height;
		double[] RGB = new double[]{0,0,0};

		int delta = 256/Q;
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				RGB[0] = IMG[0][j];
				RGB[1] = IMG[1][j];
				RGB[2] = IMG[2][j];

				IMG[0][j] = delta * (Math.floor(RGB[0]/delta) + 0.5);
				IMG[1][j] = delta * (Math.floor(RGB[1]/delta) + 0.5);
				IMG[2][j] = delta * (Math.floor(RGB[2]/delta) + 0.5);

				if(j < imgLength)
					j++;
				else
					j = imgLength;
			}
		}
		return IMG;
	}

}