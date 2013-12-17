package com.eowise.packer.extension

/**
 * Created by aurel on 15/12/13.
 */
class Resolution {
    String name
    float ratio

    Resolution(String name) {
        this.name = name
        this.ratio = 1
    }

    Resolution(String name, float ratio) {
        this.name = name
        this.ratio = ratio
    }
    
    String toString() {
        return name
    }
}
