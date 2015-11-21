import java.awt.*;
import java.awt.image.*;
import java.io.*;


import javax.swing.*;

class Point{
	int x = 0, y = 0;
}

class Centroid{
	int x = 0, y = 0;
	Point[] p = new Point[50700];
	int index = 0;
}

public class A2{
	public static void main(String[] args) 
	{
		String fileName = args[0];
		int width = 352;
		int height = 288;
		int imgLength = width * height;
		int vLength = imgLength/2;
		int N = Integer.parseInt(args[1]); 
		int n = (int) Math.ceil(Math.log(N)/Math.log(2));
		int R;		
		int[] temp = new int[imgLength];
		int[][] IMG = new int[2][vLength];
		byte r, g, b;
		int pindex = 0;

		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		BufferedImage img_out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		try {
			File file = new File(args[0]);
			InputStream is = new FileInputStream(file);
			long len = file.length();
			byte[] bytes = new byte[(int)len];
			int offset = 0;
			int numRead = 0;

			while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length-offset)) >= 0) {
				offset += numRead;
			}

			//Input image
			int j = 0, total_pix;	
			int ind = 0;
			for(int y = 0; y < height; y++){
				for(int x = 0; x < width; x++){
					r = bytes[ind];
					g = bytes[ind];
					b = bytes[ind];
					R = r;
					temp[j] = R+128;
					int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					img.setRGB(x,y,pix);
					ind++;
					j++;
				}
			}


			int l = 256/n;
			int m = 0;
			int[][] cent = new int[2][N];
			//Initialize centroids
			for(int x = 1; x <= n; x++)
			{
				for(int y = 1; y <= n; y++)
				{
					cent[0][m] = (l * x)+l/2;
					cent[1][m] = (l * y)+l/2;

					//System.out.println(cent[0][m]+","+cent[1][m]);
					m++;
				}
			}

			j = 0;
			for(int i = 0; i < imgLength; i += 2)
			{
				IMG[0][j] = temp[i];
				IMG[1][j] = temp[i+1];
				j++;
			}
			total_pix = j;

			Centroid[] centroid = new Centroid[N];
			for(int i = 0; i < N; i++)
			{
				centroid[i] = new Centroid();
				centroid[i].x = cent[0][i];
				centroid[i].y = cent[1][i];
			}

			Point[] point = new Point[60000];
			for(int i = 0; i < vLength; i++)
			{
				point[i] = new Point();
				point[i].x = IMG[0][i];
				point[i].y = IMG[1][i];
				pindex++;
			}

			int test2 = point.length;
			//Compute centroids
			double[] dist = new double[N];
			double min;
			int index=0, i=0, k=0;
			for(int iterations = 0; iterations < 6; iterations++)
			{
				if(iterations > 0)
				{
					for(index=0;index<centroid.length;index++)
						centroid[index].index=0;
					for(i = 0; i < centroid.length; i++)
					{
						for(k = 0; k < centroid[i].index; k++)
						{
							centroid[index].p[centroid[index].index].x = 0;
							centroid[index].p[centroid[index].index++].y = 0;
						}
					}
				}
				index=0;
				int test = pindex;
				for(i = 0; i < pindex; i++)
				{
					for(k = 0; k < centroid.length; k++)
					{
						dist[k] = Math.sqrt(Math.pow((centroid[k].x - point[i].x),2) + Math.pow((centroid[k].y - point[i].y),2));
					}
					min = min(dist);
					index = find_i(dist,min);
					centroid[index].p[centroid[index].index++] = point[i];
				}
				//Compute new centroids
				double sum_x = 0, sum_y = 0;
				for(i = 0; i < centroid.length; i++)
				{
					sum_x = 0;
					sum_y = 0;
					for(k = 0; k < centroid[i].index; k++)
					{
						sum_x += centroid[i].p[k].x;
						sum_y += centroid[i].p[k].y;
					}
					centroid[i].x = (int) Math.floor(sum_x/centroid[i].index);
					centroid[i].y = (int) Math.floor(sum_y/centroid[i].index);
				}
			}
			//Quantize image
			for(i = 0; i < centroid.length; i++)
			{
				for(k = 0; k < centroid[i].index; k++)
				{
					for(int x = 0; x < total_pix; x++)
					{
						if(centroid[i].p[k].x == IMG[0][x] && centroid[i].p[k].y == IMG[1][x])
						{
							if(IMG[0][x] >= 255)
								IMG[0][x] = 255;
							else if(IMG[0][x] <= 0)
								IMG[0][x] = 0;
							else
								IMG[0][x] = centroid[i].x;
							if(IMG[1][x] >= 255)
								IMG[1][x] = 255;
							else if(IMG[1][x] <= 0)
								IMG[1][x] = 0;
							else
								IMG[1][x] = centroid[i].y;
						}
					}
				}
			}

			j = 0;
			for(i = 0; i < imgLength; i += 2)
			{
				temp[i] = IMG[0][j] - 128;
				temp[i+1] = IMG[1][j] - 128;
				j++;
			}

			//Output image
			j = 0;
			ind = 0;
			for(int y = 0; y < height; y++){
				for(int x = 0; x < width; x++){
					r = (byte)temp[j];
					g = (byte)temp[j];
					b = (byte)temp[j];
					int pix1 = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					img_out.setRGB(x,y,pix1);
					ind++; 
					j++;
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

	public static double min(double[]list)
	{
		double min;
		min = list[0];
		for(int i = 1; i < list.length; i++)
		{
			if(min > list[i])
			{
				min = list[i];
			}
		}
		return min;
	}

	public static int find_i(double[]list,double min)
	{
		int index = 0;
		for(int i = 0; i < list.length; i++)
		{
			if(min == list[i])
			{
				index = i;
			}
		}
		return index;
	}
}