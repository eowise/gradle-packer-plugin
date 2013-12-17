package com.eowise.packer.extension

import org.gradle.api.Project

/**
 * Created by aurel on 16/12/13.
 */
class Packs extends ArrayList<Pack> {

    final Project project
    String resourcesPath
    String packsPath

    Packs(final Project project) {
        this.project = project
    }

    def add(String name) {
        Pack adding = new Pack(name)
        adding.textures = project.fileTree(dir: "${resourcesPath}/${name}", include: '**/*.png')
        adding.svgs = project.fileTree(dir: "${resourcesPath}/${name}", include: '**/*.svg')
        adding.ninePatches = project.fileTree(dir: "${resourcesPath}/${name}", include: '**/*.9.png')
        add(adding)
    }
    
    def add(String name, Closure closure) {
        Pack adding = project.configure(new Pack(name), closure)
        if (adding.textures == null) adding.textures = project.fileTree(dir: "${resourcesPath}/${name}", include: '**/*.png')
        if (adding.svgs == null) adding.svgs = project.fileTree(dir: "${resourcesPath}/${name}", include: '**/*.svg')
        if (adding.ninePatches == null) adding.ninePatches = project.fileTree(dir: "${resourcesPath}/${name}", include: '**/*.9.png')
        add(adding)
    }

    def resourcesPath(String resourcesPath) {
        this.resourcesPath = resourcesPath
    }
    
    def packsPath(String packsPath) {
        this.packsPath = packsPath
    }
}