package com.eowise.packer

import org.gradle.api.DefaultTask
import org.gradle.api.file.*
import org.gradle.api.tasks.*

/**
 * Created by aurel on 15/12/13.
 */
class TexturePacker extends DefaultTask {

    @InputFiles
    FileTree resourcesFiles
    @Input
    String packName
    @OutputFiles
    FileTree outputFiles

    def from(String packName, String resourcesPath) {
        this.packName = packName
        this.resourcesFiles = project.fileTree(dir: "${resourcesPath}/${packName}", include: '**/*')
    }

    def into(String outputPath) {
        outputFiles = project.fileTree(dir: outputPath, include: "${packName}*")
    }

    @TaskAction
    def pack() {
        def arguments = []

        arguments.add(resourcesFiles.getDir())
        arguments.add(outputFiles.getDir())
        if (packName != '') arguments.add(packName)

        project.javaexec {
            main = 'com.badlogic.gdx.tools.texturepacker.TexturePacker'
            classpath project.configurations.tools
            args arguments
        }
    }

}
