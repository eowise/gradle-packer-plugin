package com.eowise.packer.extension

import org.gradle.api.tasks.util.PatternSet
/**
 * Created by aurel on 16/12/13.
 */
class Atlas {

    PatternSet textures
    PatternSet svgs
    PatternSet ninePatches

    Atlas() {
        this.textures = new PatternSet()
        this.svgs = new PatternSet()
        this.ninePatches = new PatternSet()
    }

    String toString() {
        return ''
    }
}
