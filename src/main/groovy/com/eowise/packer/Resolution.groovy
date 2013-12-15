package com.eowise.packer

/**
 * Created by aurel on 15/12/13.
 */
class Resolution {
    int w, h

    Resolution(int w, h) {
        this.w = w
        this.h = h
    }

    String toString() {
        return "${w}x${h}"
    }
}
