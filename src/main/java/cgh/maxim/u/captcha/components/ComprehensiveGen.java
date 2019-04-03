/*
 * This is free and unencumbered software released into the public domain.
 * 
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 * 
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * 
 * For more information, please refer to <http://unlicense.org/>
 */
package cgh.maxim.u.captcha.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 *
 * @author simon
 */
public class ComprehensiveGen extends BackgroundGen {
    public float brightness = 0.9f;
    public ComprehensiveGen(Random random, Point size) {
        super(random, size);
    }

    private Color level(Color col){
        //float[] f = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), null);
        //return Color.getHSBColor(f[0], f[1], brightness);
        return col;
    }
    @Override
    protected void draw(BufferedImage bi) {
        Graphics2D g2d = bi.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setBackground(level(Color.GRAY));
        g2d.clearRect(0, 0, size.x, size.y);
        multigrouple(g2d,35);
    }
    
    protected void grouple(Graphics2D g2d){
        int w = size.x, h = size.y;
        for(int i=0,n = Math.min(w, h)/5;i<n;++i){
            double x = i*(double)w/n;
            double y = i*(double)h/n;
            g2d.draw(new Arc2D.Double((x/2), (y/2),w-x, h-y, 0.0, 360.0, Arc2D.OPEN));
        }
    }
    
    protected void multigrouple(Graphics2D g2d,int n){
        BasicStroke bs = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER, 2.0f, new float[]{2.0f, 2.0f}, 0.0f);
        g2d.setStroke(bs);
        for(int i=0;i<n;++i){
            if(i!=0)
                g2d.setTransform(new AffineTransform());
            //g2d.setPaint(level(new Color(nextInt(256),nextInt(256),nextInt(256))));
            g2d.setColor(Color.getHSBColor(nextFloat()*360, nextFloat(), nextFloat()));
            g2d.rotate(nextDouble()*Math.PI, size.x/2., size.y/2.);
            g2d.shear(nextDouble()-0.5, nextDouble()-0.5);
            
            grouple(g2d);
        }
    }
    
    
}
