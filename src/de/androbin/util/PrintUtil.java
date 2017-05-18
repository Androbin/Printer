package de.androbin.util;

import static de.androbin.gfx.util.GraphicsUtil.*;
import java.awt.*;
import java.awt.image.*;
import java.util.function.*;
import javax.swing.*;

public final class PrintUtil {
  private PrintUtil() {
  }
  
  public static void print( final BufferedImage image, final String title ) {
    print( ( job, g ) -> {
      final int res = job.getPageResolution();
      final Dimension dim = job.getPageDimension();
      
      final float rand = res / 1.27f;
      
      final float w = dim.width - rand * 2f;
      final float h = dim.height - rand * 2f;
      
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
    }, "Printer" ).start();
  }
}