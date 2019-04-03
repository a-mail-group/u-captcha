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

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 *
 * @author simon
 */
public abstract class BackgroundGen {
    protected final Random random;
    protected final Point size;
    public BackgroundGen(Random random, Point size) {
        this.random = random;
        this.size = size;
    }
    
    protected int nextInt() {
        return random.nextInt();
    }

    protected int nextInt(int bound) {
        return random.nextInt(bound);
    }
    protected int nextInt(int min,int bound) {
        return random.nextInt(bound-min)+min;
    }

    protected boolean nextBoolean() {
        return random.nextBoolean();
    }

    protected float nextFloat() {
        return random.nextFloat();
    }

    protected double nextDouble() {
        return random.nextDouble();
    }

    protected synchronized double nextGaussian() {
        return random.nextGaussian();
    }
    
    public BufferedImage image(){
        BufferedImage bi = new BufferedImage(size.x, size.y, BufferedImage.TYPE_INT_RGB);
        draw(bi);
        return bi;
    }
    protected abstract void draw(BufferedImage bi);
}
