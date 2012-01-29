/*
 * Vec3Cache.java
 * Copyright (C) 2003
 *
 * $Id: Vec3Cache.java,v 1.1 2005/01/16 15:16:44 cawe Exp $
 */
package jake2.util;

/**
 * Vec3Cache contains float[3] for temporary usage.
 * The usage can reduce the garbage at runtime.
 *
 * @author cwei
 */
public final class Vec3Cache {
    
    //private static Stack cache = new Stack();
    private static final float[][] cache = new float[64][3];
    private static int index = 0;
    private static int max = 0;
    
    public static final float[] get() {
        //max = Math.max(index, max);
        return cache[index++];
    }
    
    public static final void release() {
        index--;
    }

    public static final void release(int count) {
        index-=count;
    }
    
    public static final void debug() {
        System.err.println("Vec3Cache: max. " + (max + 1) + " vectors used.");
    }
}