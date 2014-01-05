package com.eowise.packer.extension

/**
 * Created by aurel on 16/12/13.
 */
class Resolutions extends ArrayList<Resolution> {
        
    def add(String name, float ratio) {
        add(new NamedResolution(name, ratio))
    }

    def add(String name) {
        add(name, 1f)
    }
}
