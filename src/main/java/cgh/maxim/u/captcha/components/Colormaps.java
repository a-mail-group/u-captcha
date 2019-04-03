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

import com.jhlabs.image.ArrayColormap;
import com.jhlabs.image.Colormap;
import com.jhlabs.image.Gradient;
import com.jhlabs.image.ImageMath;
import com.jhlabs.image.LinearColormap;
import com.jhlabs.image.SpectrumColormap;
import java.awt.Color;

/**
 *
 * @author simon
 */
public class Colormaps {
    private static class MyUtil extends ImgMath {
        static float[] hsb(int rgb,float[] hsbvals){
            return Color.RGBtoHSB(r(rgb), g(rgb), b(rgb), hsbvals);
        }
        public static int dark(int rgb,float[] hsbvals){
            hsbvals = hsb(rgb,hsbvals);
            if(hsbvals[1]>.5f) hsbvals[1] = .5f;
            if(hsbvals[2]>.3f) hsbvals[2] = .3f;
            return Color.HSBtoRGB(hsbvals[0], hsbvals[1], hsbvals[2]);
        }
        public static int light(int rgb,float[] hsbvals){
            hsbvals = hsb(rgb,hsbvals);
            return Color.HSBtoRGB(hsbvals[0], hsbvals[1], 1);
        }
    }
    protected static Colormap gradient_(int col,int ... cols){
        final int n = cols.length;
        switch(n){
            case 0:
                final int hcol = col;
                return v->hcol;
            case 1: return new LinearColormap(col,cols[0]);
        }
        Colormap[] maps = new Colormap[n];
        for(int i=0;i<n;++i){
            maps[i] = new LinearColormap(col,cols[i]);
            col=cols[i];
        }
        
        return v->{
            v = ImageMath.clamp(v, 0, 1)*n;
            return maps[(int)Math.floor(v)%n].getColor(v%1.f);
        };
    }
    public static final Colormap
            SPECTRUM = new SpectrumColormap(),
            G2R = new LinearColormap(Color.GREEN.getRGB(),Color.RED.getRGB()),
            B2Y = new LinearColormap(Color.BLUE.getRGB(),Color.YELLOW.getRGB()),
            C2O = new LinearColormap(Color.CYAN.getRGB(),new Color(255,127,0).getRGB()),
            P2G = new LinearColormap(Color.PINK.getRGB(),Color.GREEN.getRGB())
            ;
    
    
    public static Colormap monochrome(Color col){
        return new LinearColormap(0xff000000,col.getRGB());
    }
    
    public static Colormap dark(Colormap inner){
        final float[] hsbvals = new float[3];
        return v->MyUtil.dark(inner.getColor(v),hsbvals);
    }
    public static Colormap light(Colormap inner){
        final float[] hsbvals = new float[3];
        return v->MyUtil.light(inner.getColor(v),hsbvals);
    }
}
