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

import com.jhlabs.image.BlockFilter;
import com.jhlabs.image.BlurFilter;
import com.jhlabs.image.BoxBlurFilter;
import com.jhlabs.image.Colormap;
import com.jhlabs.image.Gradient;
import com.jhlabs.image.PlasmaFilter;
import com.jhlabs.image.RippleFilter;
import com.jhlabs.image.ShearFilter;
import com.jhlabs.image.SpectrumColormap;
import com.jhlabs.image.SphereFilter;
import com.jhlabs.image.SwimFilter;
import com.jhlabs.image.TwirlFilter;
import com.jhlabs.image.WaterFilter;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
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
public class OtherImgFuncs {
    
    public static void determPrint(BufferedImage image,String args,Paint p) {
        Graphics2D g2d = image.createGraphics();
        double w = image.getWidth();
        double h = image.getHeight();
        
        double x = 0;
        double y = 0;
        double yo = 0;
        
        //Font fnt = new Font("Sans",Font.BOLD,40);
        Font fnt = new Font(Font.MONOSPACED,Font.BOLD,40);
        yo = fnt.getSize();
        
        AffineTransform at = new AffineTransform();
        
        {
            double l = g2d.getFontMetrics(fnt).stringWidth(args);
            double scale;
            if(l>w){
                scale = w/l;
                
            } else {
                scale = w/l;
            }
            scale *= 0.9;
            x = w*0.05;
            {
                double scaley = Math.min(scale,2.9);
                at.scale(scale, scaley);
                yo *= scaley;
            }
            if(yo>h){
                scale = yo/h;
                scale *= 0.9;
                yo *= scale;
                at.scale(1, scale);
                y = h*0.05;
            } else {
                y = (h-yo)/2;
            }
        }
        fnt = fnt.deriveFont(at);
        
        g2d.setFont(fnt);
        g2d.setPaint(p);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.drawString(args, (float)x, (float)(y+yo));
        g2d.dispose();
        
    }
    
    public static void printRand(BufferedImage image,Paint p,Random rnd) {
        Graphics2D g2d = image.createGraphics();
        int w = image.getWidth();
        int h = image.getHeight();
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setPaint(p);
        
        
        
        int x,y,a,b;
        for(int i=0,n=Math.max((int)(Math.sqrt(w*h)),1);i<n;++i){
            x = rnd.nextInt(w);
            y = rnd.nextInt(h);
            /*
            a = rnd.nextInt(Math.min(w-x,w/10));
            b = rnd.nextInt(Math.min(h-y,h/10));
            */
            a = rnd.nextInt(Math.min(w-x,w/3));
            b = rnd.nextInt(Math.min(h-y,h/3));
            switch(rnd.nextInt(3)){
                case 0:
                    g2d.drawLine(x,y,x+a,y+b);
                case 1:
                    g2d.drawArc(x,y,a,b, rnd.nextInt(360), rnd.nextInt(360));
                case 2:
                    a = rnd.nextInt(5)+1;
                    b = rnd.nextInt(5)+1;
                    g2d.fillOval(x, y, a, b);
            }
        }
        
        g2d.dispose();
    }
    
    public static BufferedImage distort(BufferedImage src){
        
        BufferedImage dest1 = ImageFuncs.make(ImageFuncs.size(src));
        BufferedImage dest2 = ImageFuncs.make(ImageFuncs.size(src));
        
        dest1 = new RippleFilter().filter(src, dest1);
        
        {
            TwirlFilter twirl = new TwirlFilter();
            twirl.setAngle(0.3f);
            twirl.setRadius(Math.max(src.getWidth(),src.getHeight()));
            dest2 = twirl.filter(dest1, dest2);
        }
        
        {
            RippleFilter ripple = new RippleFilter();
            ripple.setWaveType(com.jhlabs.image.RippleFilter.NOISE);
            ripple.setXAmplitude(3);
            ripple.setYAmplitude(3);
            ripple.setXWavelength(10);
            ripple.setYWavelength(10);
            ripple.setEdgeAction(com.jhlabs.image.TransformFilter.CLAMP);
            
            dest1 = ripple.filter(dest2, dest1);
        }
        
        {
            SphereFilter sphere = new SphereFilter();
            sphere.setRadius(Math.max(src.getWidth(),src.getHeight())/2.0f);
            sphere.setRefractionIndex(1.1f);
            
            dest2 = sphere.filter(dest1, dest2);
        }
        {
            TwirlFilter twirl = new TwirlFilter();
            twirl.setAngle(-0.6f);
            twirl.setRadius(Math.max(src.getWidth(),src.getHeight()));
            dest1 = twirl.filter(dest2, dest1);
        }
        
        return dest1;
    }
    
    protected static BufferedImage distortRandomly_old(BufferedImage src,Random r){
        
        BufferedImage dest1 = ImageFuncs.make(ImageFuncs.size(src));
        BufferedImage dest2 = ImageFuncs.make(ImageFuncs.size(src));
        
        float encode = r.nextFloat()*(float)Math.PI;
        
        float xwave = r.nextFloat()*10;
        float ywave = r.nextFloat()*10;
        
        {
            TwirlFilter twirl = new TwirlFilter();
            twirl.setAngle(encode);
            twirl.setRadius(Math.max(src.getWidth(),src.getHeight()));
            dest2 = twirl.filter(src, dest2);
        }
        
        {
            RippleFilter ripple = new RippleFilter();
            ripple.setWaveType(com.jhlabs.image.RippleFilter.NOISE);
            ripple.setXAmplitude(3);
            ripple.setYAmplitude(3);
            ripple.setXWavelength(5+xwave);
            ripple.setYWavelength(5+ywave);
            ripple.setEdgeAction(com.jhlabs.image.TransformFilter.CLAMP);
            
            dest1 = ripple.filter(dest2, dest1);
        }
        
        {
            SwimFilter sphere = new SwimFilter();
            
            sphere.setScale(64*r.nextFloat());
            sphere.setStretch(2*r.nextFloat());
            sphere.setAngle(r.nextFloat());
            sphere.setAmount(1+r.nextFloat());
            sphere.setTurbulence(2*r.nextFloat());
            sphere.setTime(100*r.nextFloat());
            
            dest2 = sphere.filter(dest1, dest2);
        }
        {
            TwirlFilter twirl = new TwirlFilter();
            twirl.setAngle(-encode);
            twirl.setRadius(Math.max(src.getWidth(),src.getHeight()));
            dest1 = twirl.filter(dest2, dest1);
        }
        /*
        {
            TwirlFilter twirl = new TwirlFilter();
            twirl.setAngle(0.3f);
            twirl.setRadius(Math.max(src.getWidth(),src.getHeight()));
            dest2 = twirl.filter(dest1, dest2);
        }
        */
        return dest1;
    }
    private static float rippleAmp(int l){
        return l*3.0f/350.0f;
    }
    public static BufferedImage distortRandomly(BufferedImage src,Random r){
        
        BufferedImage dest1 = ImageFuncs.make(ImageFuncs.size(src));
        BufferedImage dest2 = ImageFuncs.make(ImageFuncs.size(src));
        
        float xwave = r.nextFloat()*10;
        float ywave = r.nextFloat()*10;
        
        
        {
            RippleFilter ripple = new RippleFilter();
            ripple.setWaveType(com.jhlabs.image.RippleFilter.NOISE);
            ripple.setXAmplitude(rippleAmp(src.getWidth()));
            ripple.setYAmplitude(rippleAmp(src.getHeight()));
            ripple.setXWavelength(5+xwave);
            ripple.setYWavelength(5+ywave);
            ripple.setEdgeAction(com.jhlabs.image.TransformFilter.CLAMP);
            
            dest1 = ripple.filter(src, dest1);
        }
        
        {
            SwimFilter sphere = new SwimFilter();
            
            sphere.setScale(64*r.nextFloat());
            sphere.setStretch(2*r.nextFloat());
            sphere.setAngle(r.nextFloat());
            sphere.setAmount(1+r.nextFloat());
            sphere.setTurbulence(2*r.nextFloat());
            sphere.setTime(100*r.nextFloat());
            
            dest2 = sphere.filter(dest1, dest2);
        }
        /*
        {
            TwirlFilter twirl = new TwirlFilter();
            twirl.setAngle(0.3f);
            twirl.setRadius(Math.max(src.getWidth(),src.getHeight()));
            dest2 = twirl.filter(dest1, dest2);
        }
        */
        return dest2;
    }
    public static void grouple(Graphics2D g2d,Point size){
        int w = size.x, h = size.y;
        for(int i=0,n = Math.min(w, h)/5;i<n;++i){
            double x = i*(double)w/n;
            double y = i*(double)h/n;
            g2d.draw(new Arc2D.Double((x/2), (y/2),w-x, h-y, 0.0, 360.0, Arc2D.OPEN));
        }
    }
    
    private static void translate(Graphics2D g2d,AffineTransform raw,Point size,double x,double y){
        g2d.setTransform(raw);
        g2d.translate(x*size.x, y*size.y);
    }
    public static BufferedImage generateDeterministicScratch(Point size){
        //BufferedImage dest = ImageFuncs.makeG(size);
        BufferedImage pad = ImageFuncs.makeG(size);
        Graphics2D g2d = pad.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        BasicStroke bs = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER, 2.0f, new float[]{2.0f, 2.0f}, 0.0f);
        g2d.setStroke(bs);
        AffineTransform raw = new AffineTransform();
        translate(g2d,raw,size,-.5,-.5); grouple(g2d,size);
        translate(g2d,raw,size, .5,-.5); grouple(g2d,size);
        translate(g2d,raw,size, .5, .5); grouple(g2d,size);
        translate(g2d,raw,size,-.5, .5); grouple(g2d,size);
        translate(g2d,raw,size,0.0,0.0); grouple(g2d,size);
        translate(g2d,raw,size,0.0, .4); grouple(g2d,size);
        translate(g2d,raw,size,0.0,-.4); grouple(g2d,size);
        translate(g2d,raw,size, .4,0.0); grouple(g2d,size);
        translate(g2d,raw,size,-.4,0.0); grouple(g2d,size);
        g2d.dispose();
        return pad;
    }
    public static void randomGrouples(Random r,Point size,Graphics2D g2d,int n){
        BasicStroke bs = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER, 2.0f, new float[]{2.0f, 2.0f}, 0.0f);
        g2d.setStroke(bs);
        for(int i=0;i<n;++i){
            if(i!=0)
                g2d.setTransform(new AffineTransform());
            g2d.rotate(r.nextDouble()*Math.PI, size.x/2., size.y/2.);
            g2d.shear(r.nextDouble()-0.5, r.nextDouble()-0.5);
            grouple(g2d,size);
        }
    }
    public static void scratchGrouples(Random r,BufferedImage image,Paint p){
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setPaint(p);
        randomGrouples(r,ImageFuncs.size(image),g2d,5);
        g2d.dispose();
        //new BlurFilter().filter(image,image);
    }
    private static void makeScratches0(Random r,BufferedImage image){
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        g2d.setColor(Color.GRAY);
        //randomGrouples(r,ImageFuncs.size(image),g2d,1);
        int n = (int)(Math.sqrt(image.getHeight()*image.getWidth())/2);
        for(int i=0;i<n;++i){
            int x1 = SpecialMath.edgness(r.nextGaussian(), image.getWidth());
            int y1 = SpecialMath.edgness(r.nextGaussian(), image.getHeight());
            int x2 = SpecialMath.edgness(r.nextGaussian(), image.getWidth());
            int y2 = SpecialMath.edgness(r.nextGaussian(), image.getHeight());
            g2d.drawLine(x1,y1,x2,y2);
        }
        g2d.dispose();
        //new BlurFilter().filter(image,image);
    }
    public static BufferedImage generateGrindMap(Point size,Random r){
        return distortRandomly(generateDeterministicScratch(size),r);
    }
    public static BufferedImage generateScratchMap(Point size,Random r){
        BufferedImage dest = ImageFuncs.makeG(size);
        BufferedImage pad = ImageFuncs.makeG(size);
        BoxBlurFilter bbf = new BoxBlurFilter();
        //bbf.setRadius(2.5f);
        //bbf.setIterations(2);
        for(int i=0;i<5;++i){
            makeScratches0(r,pad);
            bbf.filter(pad,pad);
            ImageFuncs.addGray(dest, pad);
        }
        return dest;
    }
    public static BufferedImage plasma(Point size){
        PlasmaFilter pf = new PlasmaFilter();
        BufferedImage dst = ImageFuncs.make(size);
        return pf.filter(dst, dst);
    }
    public static BufferedImage plasma(Point size,Random rnd,Colormap cm){
        PlasmaFilter pf = new PlasmaFilter();
        pf.setColormap(cm);
        pf.setUseColormap(true);
        pf.setSeed(rnd.nextInt());
        pf.setTurbulence(rnd.nextFloat()*2);
        pf.setScaling(rnd.nextFloat());
        BufferedImage dst = ImageFuncs.make(size);
        return pf.filter(dst, dst);
    }
    public static BufferedImage plasma(Point size,Colormap cm){
        PlasmaFilter pf = new PlasmaFilter();
        pf.setColormap(cm);
        pf.setUseColormap(true);
        BufferedImage dst = ImageFuncs.make(size);
        return pf.filter(dst, dst);
    }
    public static BufferedImage blurry(BufferedImage src){
        BoxBlurFilter bbf = new BoxBlurFilter();
        bbf.setRadius(10);
        return bbf.filter(src, null);
    }
}
