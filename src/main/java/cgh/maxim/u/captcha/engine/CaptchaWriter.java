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
package cgh.maxim.u.captcha.engine;

import cgh.maxim.u.captcha.components.Colormaps;
import cgh.maxim.u.captcha.components.ImageFuncs;
import cgh.maxim.u.captcha.components.OtherImgFuncs;
import com.jhlabs.image.Colormap;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 *
 * @author simon
 */
public class CaptchaWriter {
    private Random prng;
    public int scratchesOnbackground;
    public int randomLines = 1;
    public boolean distortText = true;
    public boolean grindText = true;
    public Colormap baseMap;
    
    public Random getPrng() {
        if(prng==null) prng = new Random();
        return prng;
    }
    public void setPrng(Random prng) {
        this.prng = prng;
    }
    
    
    
    private BufferedImage backround(Point size,boolean front){
        Colormap cm;
        cm = baseMap;
        if(cm==null) switch(getPrng().nextInt(5)){
            case 0: cm = Colormaps.SPECTRUM; break;
            case 1: cm = Colormaps.G2R; break;
            case 2: cm = Colormaps.B2Y; break;
            case 3: cm = Colormaps.C2O; break;
            case 4: cm = Colormaps.P2G; break;
        }
        if(front)
            cm = Colormaps.dark(cm);
        else
            cm = Colormaps.light(cm);
        
        BufferedImage g = OtherImgFuncs.plasma(size, getPrng(), cm);
        
        for(int i=0;i<scratchesOnbackground;++i)
            ImageFuncs.pressInto(g, OtherImgFuncs.plasma(size, getPrng(), cm),
                    OtherImgFuncs.generateScratchMap(size, getPrng()));
        
        return g;
    }
    private BufferedImage textmask(Point size,String data){
        BufferedImage textmask = ImageFuncs.make(size);
        for(int i=0;i<randomLines;++i)
            OtherImgFuncs.printRand(textmask, Color.WHITE, getPrng());
        OtherImgFuncs.determPrint(textmask, data,Color.WHITE);
        
        if(distortText) textmask = OtherImgFuncs.distortRandomly(textmask,
                getPrng());
        
        if(grindText) ImageFuncs.andGray(textmask,
                OtherImgFuncs.generateGrindMap(size,getPrng()));
        
        return textmask;
    }
    public BufferedImage generate(Point size,String data){
        boolean flipped = getPrng().nextBoolean();
        BufferedImage fg = backround(size,true==flipped);
        BufferedImage bg = backround(size,false==flipped);
        
        BufferedImage tm = textmask(size,data);
        
        flipped = getPrng().nextBoolean();
        
        ImageFuncs.pressInto(bg, fg, tm, flipped);
        
        return bg;
    }
}
