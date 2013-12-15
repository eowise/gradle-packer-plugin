package com.eowise.packer

import com.eowise.imagemagick.tasks.Magick
import com.eowise.imagemagick.tasks.SvgToPng
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy

class PackerPlugin implements Plugin<Project> {

    void apply(Project project) {

        project.extensions.create('packer', PackerPluginExtension)
        project.configurations.create('tools')
        project.dependencies.add('tools', 'com.badlogicgames.gdx:gdx-tools:0.9.9')
        project.task("buildPacks")
        project.task("cleanPacks")

        
        project.configure(project) {
            afterEvaluate {
                project.packer.packNames.each() {
                    packName ->

                        tasks.create(name: "convertSvg${packName}", type: SvgToPng, ) {
                            files fileTree(dir: "resources/${packName}").include('*.svg')
                            into "resources/${packName}"
                        }

                        project.packer.resolutions.each() {
                            res ->
                                tasks.create(name: "resizeImages${res}${packName}", type: Magick, dependsOn: "convertSvg${packName}") {
                                    input "resources/${packName}", "**/*.png"
                                    output "out/resources/${res}/${packName}"
                                    convert {
                                        resize res.w / project.packer.baseResolution.w
                                        condition '.9.png', {
                                            border {
                                                width 1
                                                color 'none'
                                            }
                                            xc 'black'
                                            gravity 'North'
                                            geometry '1x1+0x+0'
                                            composite()
                                            xc 'black'
                                            gravity 'West'
                                            geometry '1x1+0x+0'
                                            composite()
                                        }
                                    }
                                }

                                tasks.create(name: "copyPacks${res}${packName}", type: Copy) {
                                    from packName, "resources/${packName}"
                                    into "out/resources/${res}/${packName}"
                                    include "${res}.json"
                                    rename { f -> 'pack.json' }
                                }

                                tasks.create(name: "createPacks${res}${packName}", type: Packer, dependsOn: ["resizeImages${res}${packName}", "copyPacks${res}${packName}"]) {
                                    pack packName, "out/resources/${res}"
                                    into "${project.packer.packsPath}/${res}"
                                }

                                project.buildPacks.dependsOn "createPacks${res}${packName}"
                                project.cleanPacks.dependsOn "cleanCreatePacks${res}${packName}"
                                project.cleanPacks.dependsOn "cleanResizeImages${res}${packName}"

                        }
                }
            }
        }
    }
}



