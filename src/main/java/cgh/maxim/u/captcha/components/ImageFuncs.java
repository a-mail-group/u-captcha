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

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

/**
 *
 * @author simon
 */
public class ImageFuncs extends ImgMath {
    public static Point size(Image img){
        return new Point(img.getWidth(null),img.getHeight(null));
    }
    public static BufferedImage make(Point size){
        return new BufferedImage(size.x,size.y,BufferedImage.TYPE_INT_RGB);
    }
    public static BufferedImage makeA(Point size){
        return new BufferedImage(size.x,size.y,BufferedImage.TYPE_INT_ARGB);
    }
    public static BufferedImage makeG(Point size){
        return new BufferedImage(size.x,size.y,BufferedImage.TYPE_BYTE_GRAY);
    }
    public static void fill(BufferedImage image,Paint p) {
        Graphics2D g2d = image.createGraphics();
        g2d.setPaint(p);
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        g2d.dispose();
    }
    public static Paint asPaint(BufferedImage image){
        return new TexturePaint(image,new Rectangle(image.getWidth(),image.getHeight()));
    }
    public static void pressInto(BufferedImage dest,BufferedImage src,BufferedImage mask,boolean flipped){
        if(flipped) pressIntoNegative(dest,src,mask);
        else        pressInto        (dest,src,mask);
    }
    public static void pressInto(BufferedImage dest,BufferedImage src,BufferedImage mask){
        final int w = dest.getWidth();
        final int h = dest.getHeight();
        for(int x=0;x<w;++x) for(int y=0;y<h;++y) {
            int col = lerpRGB(dest.getRGB(x, y),src.getRGB(x, y),mask.getRGB(x, y));
            dest.setRGB(x, y, col);
        }
    }
    public static void pressIntoNegative(BufferedImage dest,BufferedImage src,BufferedImage mask){
        final int w = dest.getWidth();
        final int h = dest.getHeight();
        for(int x=0;x<w;++x) for(int y=0;y<h;++y) {
            int col = lerpRGB(src.getRGB(x, y),dest.getRGB(x, y),mask.getRGB(x, y));
            dest.setRGB(x, y, col);
        }
    }
    public static void addGray(BufferedImage dest,BufferedImage src) {
        final int w = dest.getWidth();
        final int h = dest.getHeight();
        for(int x=0;x<w;++x) for(int y=0;y<h;++y) {
            dest.setRGB(x,y,mtoc(ctom(dest.getRGB(x,y))+ctom(src.getRGB(x,y))));
        }
    }
    public static void andGray(BufferedImage dest,BufferedImage src) {
        final int w = dest.getWidth();
        final int h = dest.getHeight();
        for(int x=0;x<w;++x) for(int y=0;y<h;++y) {
            dest.setRGB(x,y,mtoc(ctom(dest.getRGB(x,y))*ctom(src.getRGB(x,y))));
        }
    }
}
