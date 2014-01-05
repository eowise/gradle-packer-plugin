package com.eowise.packer.extension

import org.gradle.api.Named

/**
 * Created by aurel on 04/01/14.
 */
class NamedResolution extends Resolution implements Named {

    String name

    NamedResolution(String name) {
        super()
        this.name = name
    }

    NamedResolution(String name, float ratio) {
        super(ratio)
        this.name = name
    }

    String toString() {
        return name
    }
}
