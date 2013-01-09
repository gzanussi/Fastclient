package com.fastclient.util;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public final class ScreenUtil {
    
    
    
    public static void centerScreen(JFrame frame) {

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;
        frame.setLocation(x, y);
        
    }

}
