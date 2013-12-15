package com.eowise.packer

/**
 * Created by aurel on 15/12/13.
 */
class Variant {
    String name
    float ratio

    Variant(String name, float ratio) {
        this.name = name
        this.ratio = ratio
    }

    String toString() {
        return name
    }
}
