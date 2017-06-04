package de.androbin.util;

import static de.androbin.gfx.util.GraphicsUtil.*;
import java.awt.*;
import java.awt.image.*;
import java.util.function.*;
import javax.swing.*;

public final class PrintUtil {
  private static final float EDGE = 2f; // cm
  
  private PrintUtil() {
  }
  
  public static void print( final BufferedImage image, final String title ) {
    print( ( job, g ) -> {
      final Dimension dim = job.getPageDimension();
      final float res = job.getPageResolution() / 2.54f; // px cm^-1
      
      final float edge = res * EDGE;
      
      final float w = dim.width - 2f * edge;
      final float h = dim.height - 2f * edge;
      
      final boolean format = h * image.getWidth() < w * image.getHeight();
      
      final float width = format ? h * image.getWidth() / image.getHeight() : w;
      final float height = format ? h : w * image.getHeight() / image.getWidth();
      
      final float x = ( dim.width - width ) * 0.5f;
      final float y = ( dim.height - height ) * 0.5f;
      
      drawImage( g, image, x, y, width, height );
    }, title );
  }
  
  public static void print( final BiConsumer<PrintJob, Graphics> renderer, final String title ) {
    new Thread( () -> {
      final JFrame window = new JFrame();
      final PrintJob job = Toolkit.getDefaultToolkit().getPrintJob( window, title, null );
      
      if ( job != null ) {
        final Graphics g = job.getGraphics();
        renderer.accept( job, g );
        g.dispose();
        job.end();
      }
      
      window.dispose();
    }, "PrintJob" ).start();
  }
}