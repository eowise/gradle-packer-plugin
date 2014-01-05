package com.eowise.packer.extension

import org.gradle.api.Project
/**
 * Created by aurel on 15/12/13.
 */
class PackerPluginExtension {

    final Project project
    Resolutions resolutions
    Atlases atlases

    PackerPluginExtension(final Project project) {
        this.project = project
        this.resolutions = new Resolutions()
        this.atlases = new Atlases(project)
    }

    def atlases(Closure closure) {
        closure.delegate = atlases
        closure()
    }

    def resolutions(Closure closure) {
        closure.delegate = resolutions
        closure()
    }
    
}
