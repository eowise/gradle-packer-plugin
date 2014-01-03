package com.eowise.packer.extension

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

/**
 * Created by aurel on 16/12/13.
 */
class Packs {

    final Project project
    final NamedDomainObjectContainer set
    Closure resourcesPathClosure
    Closure packsPathClosure

    Packs(final Project project) {
        super()
        this.project = project
        this.set = project.container(Pack)
    }

    def add(String name) {
        Pack adding = new Pack(name)
        adding.textures = project.fileTree(dir: resourcesPath(adding), include: '**/*.png')
        adding.svgs = project.fileTree(dir: resourcesPath(adding), include: '**/*.svg')
        adding.ninePatches = project.fileTree(dir: resourcesPath(adding), include: '**/*.9.png')
        set.add(adding)
    }
    
    def add(String name, Closure closure) {
        Pack adding = project.configure(new Pack(name), closure)
        if (adding.textures == null) adding.textures = project.fileTree(dir: resourcesPath(adding), include: '**/*.png')
        if (adding.svgs == null) adding.svgs = project.fileTree(dir: resourcesPath(adding), include: '**/*.svg')
        if (adding.ninePatches == null) adding.ninePatches = project.fileTree(dir: resourcesPath(adding), include: '**/*.9.png')
        set.add(adding)
    }
    
    Pack getByName(String name) {
        return set.getByName(name)
    }
    
    def each(Closure closure) {
        set.each(closure)
    }

    def from(String resourcesPath) {
        this.resourcesPathClosure = { Pack pack -> "${resourcesPath}/${pack}"}
    }

    def from(Closure closure) {
        this.resourcesPathClosure = closure
    }
    
    def to(Closure closure) {
        this.packsPathClosure = closure
    }
    
    def to(String packsPath) {
        this.packsPathClosure = { Resolution resolution -> "${packsPath}/${resolution}" }
    }

    String resourcesPath(Pack pack) {
        return resourcesPathClosure(pack)
    }

    String packsPath(Resolution res) {
        return packsPathClosure(res)
    }
    
}