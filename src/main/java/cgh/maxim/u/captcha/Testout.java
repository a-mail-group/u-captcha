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
package cgh.maxim.u.captcha;

import cgh.maxim.u.captcha.components.Colormaps;
import cgh.maxim.u.captcha.components.ComprehensiveGen;
import cgh.maxim.u.captcha.components.ImageFuncs;
import cgh.maxim.u.captcha.components.OtherImgFuncs;
import cgh.maxim.u.captcha.components.Textgen;
import cgh.maxim.u.captcha.engine.CaptchaWriter;
import com.jhlabs.image.CausticsFilter;
import com.jhlabs.image.CheckFilter;
import com.jhlabs.image.PlasmaFilter;
import com.jhlabs.image.RippleFilter;
import com.jhlabs.image.SphereFilter;
import com.jhlabs.image.SwimFilter;
import com.jhlabs.image.TwirlFilter;
import com.jhlabs.image.WaterFilter;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;
import java.io.File;
import java.util.Random;
import java.util.UUID;
import javax.imageio.ImageIO;

/**
 *
 * @author simon
 */
public class Testout {
    public static final File BASE = new File("/NB-home/simon/nimg");
    public static void main(String[] args) throws Exception {
        Random r = new Random();
        //Point p = new Point(256,256);
        //Point p = new Point(512,512);
        Point p = new Point(300,100);
        //Point p = new Point(512,200);
        BufferedImage img;
        
        String rand = Textgen.randomText(r,10);
        
        CaptchaWriter cw = new CaptchaWriter();
        cw.setPrng(r);
        //cw.grindText = false;
        //cw.randomLines = 1;
        cw.scratchesOnbackground = 1;
        //cw.baseMap = Colormaps.P2G;
        
        img = cw.generate(p, rand);
        
        System.out.println();
        System.out.println();
        System.out.println(rand);
        System.out.println();
        System.out.println();
        
        
        ImageIO.write(img, "png", new File(BASE,"test.png"));
    }
}
