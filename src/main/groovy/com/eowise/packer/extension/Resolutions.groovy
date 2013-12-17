package com.eowise.packer.extension

/**
 * Created by aurel on 16/12/13.
 */
class Resolutions extends ArrayList<Resolution> {
    
    Resolution base
    
    def add(String name, float ratio) {
        add(new Resolution(name, ratio))
    }

    def base(String name) {
        Resolution r = new Resolution(name, 1f)
        add(r)
        base = r
    }
}
