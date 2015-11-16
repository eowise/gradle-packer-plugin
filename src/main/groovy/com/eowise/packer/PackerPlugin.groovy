package com.eowise.packer

import org.gradle.api.Plugin
import org.gradle.api.Project

class PackerPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.configurations.create('tools')
        project.dependencies.add('tools', 'com.badlogicgames.gdx:gdx-tools:1.7.1')
        project.task("buildPacks")
        project.task("cleanPacks")
    }
}



