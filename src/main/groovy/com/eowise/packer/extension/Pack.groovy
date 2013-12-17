package com.eowise.packer.extension

import org.gradle.api.file.ConfigurableFileTree

/**
 * Created by aurel on 16/12/13.
 */
class Pack {
    
    String name
    ConfigurableFileTree svgs
    ConfigurableFileTree textures
    ConfigurableFileTree ninePatches

    Pack(String name) {
        this.name = name
    }

    String toString() {
        return name
    }
}
