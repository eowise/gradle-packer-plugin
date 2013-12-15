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

                        project.packer.variants.each() {
                            variant ->
                                tasks.create(name: "resizeImages${variant}${packName}", type: Magick, dependsOn: "convertSvg${packName}") {
                                    input "resources/${packName}", "**/*.png"
                                    output "out/resources/${variant}/${packName}"
                                    convert {
                                        resize variant.ratio
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

                                tasks.create(name: "copyPacks${variant}${packName}", type: Copy) {
                                    from packName, "resources/${packName}"
                                    into "out/resources/${variant}/${packName}"
                                    include "${variant}.json"
                                    rename { f -> 'pack.json' }
                                }

                                tasks.create(name: "createPacks${variant}${packName}", type: Packer, dependsOn: ["resizeImages${variant}${packName}", "copyPacks${variant}${packName}"]) {
                                    pack packName, "out/resources/${variant}"
                                    into "${project.packer.packsPath}/${variant}"
                                }

                                project.buildPacks.dependsOn "createPacks${variant}${packName}"
                                project.cleanPacks.dependsOn "cleanCreatePacks${variant}${packName}"
                                project.cleanPacks.dependsOn "cleanResizeImages${variant}${packName}"

                        }
                }
            }
        }
    }
}



