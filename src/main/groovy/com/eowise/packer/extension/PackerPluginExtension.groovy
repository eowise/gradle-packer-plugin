package com.eowise.packer.extension
import com.eowise.packer.hooks.TaskHook
import org.gradle.api.Project
/**
 * Created by aurel on 15/12/13.
 */
class PackerPluginExtension {

    final Project project
    Resolutions resolutions
    Packs packs
    List<TaskHook> beforeResize


    PackerPluginExtension(final Project project) {
        this.project = project
        this.resolutions = new Resolutions()
        this.packs = new Packs(project)
        this.beforeResize = []

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
        beforeResize.add(project.configure(new TaskHook(), closure) as TaskHook)
    }
    
}
