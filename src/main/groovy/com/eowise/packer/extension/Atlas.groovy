package com.eowise.packer.extension

import org.gradle.api.tasks.util.PatternSet
/**
 * Created by aurel on 16/12/13.
 */
class Atlas {

    PatternSet textures
    PatternSet svgs
    Closure[] beforeResize
    Closure[] afterResize

    Atlas() {
        this.textures = new PatternSet() {  }
        this.svgs = new PatternSet()

        textures.include '**/*.png', '**/*.jpg'
        svgs.include  '**/*.svg'
    }

    String toString() {
        return ''
    }
}
