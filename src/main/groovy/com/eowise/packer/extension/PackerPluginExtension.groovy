package com.eowise.packer.extension

import com.eowise.packer.hooks.Hook
import org.gradle.api.Project
/**
 * Created by aurel on 15/12/13.
 */
class PackerPluginExtension {

    final Project project
    Resolutions resolutions
    Packs packs
    List<Hook> beforeResize
    List<Hook> afterResize


    PackerPluginExtension(final Project project) {
        this.project = project
        this.resolutions = new Resolutions()
        this.packs = new Packs(project)
        this.beforeResize = []
        this.afterResize = []

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
    
    def beforeResize(Closure closure) {
        beforeResize.add(project.configure(new Hook(), closure) as Hook)
    }

    def afterResize(Closure closure) {
        afterResize.add(project.configure(new Hook(), closure) as Hook)
    }
    
}
