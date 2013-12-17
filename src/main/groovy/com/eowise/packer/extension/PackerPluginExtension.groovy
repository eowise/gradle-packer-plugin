package com.eowise.packer.extension
import org.gradle.api.Project
/**
 * Created by aurel on 15/12/13.
 */
class PackerPluginExtension {

    final Project project
    Resolutions resolutions
    Packs packs


    PackerPluginExtension(final Project project) {
        this.project = project
        this.resolutions = new Resolutions()
        this.packs = new Packs(project)

    }

    def packs(Closure closure) {
        closure.delegate = packs
        closure()
    }

    def resolutions(Closure closure) {
        closure.delegate = resolutions
        closure()
    }

    def defaultResolutions() {
        resolutions.add('ldpi', 0.375f)
        resolutions.add('mdpi', 0.5f)
        resolutions.add('hdpi', 0.75f)
        resolutions.base('xhdpi')
    }
}
