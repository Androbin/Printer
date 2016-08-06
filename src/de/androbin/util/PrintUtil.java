package de.androbin.util;

import static de.androbin.math.util.floats.FloatMathUtil.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public final class PrintUtil
{
	private PrintUtil()
	{
	}
	
	public static void print( final BufferedImage image, final String title )
	{
		new Thread( () ->
		{
			final JFrame window = new JFrame();
			final PrintJob job = Toolkit.getDefaultToolkit().getPrintJob( window, title, null );
			
			if ( job != null )
			{
				final int res = job.getPageResolution();
				final Dimension dim = job.getPageDimension();
				final Graphics g = job.getGraphics();
				
				if ( g != null && res > 0 )
				{
					final int rand = round( res / 1.27f );
					
					final int w = dim.width - ( rand << 1 );
					final int h = dim.height - ( rand << 1 );
					
					final boolean format = h * image.getWidth() < w * image.getHeight();
					
					final int width = format ? h * image.getWidth() / image.getHeight() : w;
					final int height = format ? h : w * image.getHeight() / image.getWidth();
					
					g.drawImage( image, ( dim.width >> 1 ) - ( width >> 1 ), ( dim.height >> 1 ) - ( height >> 1 ), width, height, window );
					g.dispose();
				}
				
				job.end();
			}
			
			window.dispose();
			
		} , "Printer" ).start();
	}
}