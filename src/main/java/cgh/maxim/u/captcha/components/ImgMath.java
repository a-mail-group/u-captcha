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

/**
 *
 * @author simon
 */
public class ImgMath {
    protected static int a(int col){ return 0xff&(col>>24); }
    protected static int r(int col){ return 0xff&(col>>16); }
    protected static int g(int col){ return 0xff&(col>>8); }
    protected static int b(int col){ return 0xff&(col); }
    protected static int argb(int a,int r,int g,int b){
        return
                (a<<24)|
                (r<<16)|
                (g<<8)|
                b;
    }
    private static final double RGBM = (0xff)*3;
    protected static double ctom(int col){
        return (r(col)+g(col)+b(col))/RGBM;
    }
    protected static double clamp(double i,double min,double max){
        return Math.min(Math.max(i, min), max);
    }
    protected static int mtoc(double m){
        int g = (int)clamp(m*0xff,0,0xff);
        return argb(0xff,g,g,g);
    }
    protected static int ilerp8(int x,int y,double dx,double dy){
        return (int)clamp(x*dx+y*dy,0,0xff);
    }
    protected static int ilerpRGB(int x,int y,double dx,double dy){
        return argb(
                ilerp8(a(x),a(y),dx,dy),
                ilerp8(r(x),r(y),dx,dy),
                ilerp8(g(x),g(y),dx,dy),
                ilerp8(b(x),b(y),dx,dy)
        );
    }
    protected static int lerpRGB(int x,int y,int edy){
        double dy = ctom(edy);
        return ilerpRGB(x,y,1-dy,dy);
    }
}
