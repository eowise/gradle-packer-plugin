package com.eowise.packer.extension

import org.gradle.api.Named

/**
 * Created by aurel on 04/01/14.
 */
class NamedAtlas extends Atlas implements Named {

    String name

    NamedAtlas(String name) {
        super()
        this.name = name
    }

    String toString() {
        return name
    }
}
