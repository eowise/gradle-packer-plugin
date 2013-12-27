package com.eowise.packer
import com.eowise.imagemagick.tasks.Magick
import com.eowise.imagemagick.tasks.SvgToPng
import com.eowise.packer.extension.PackerPluginExtension
import com.eowise.packer.hooks.Hook
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.Copy

class PackerPlugin implements Plugin<Project> {

    void apply(Project project) {

        project.extensions.create('packer', PackerPluginExtension, project)
        project.configurations.create('tools')
        project.dependencies.add('tools', 'com.badlogicgames.gdx:gdx-tools:0.9.9')
        project.task("buildPacks")
        project.task("cleanPacks")
        
        project.configure(project) {
            afterEvaluate {
                project.packer.packs.each() {
                    pack ->

                        Task convertSvgTask = tasks.create(name: "convertSvg${pack}", type: SvgToPng) {
                            files pack.svgs
                            into "${project.packer.packs.resourcesPath}/${pack}"
                        }

                        
                        project.packer.resolutions.each() {
                            resolution ->
                                
                                Task resizeImagesTask = tasks.create(name: "resizeImages${resolution}${pack}", type: Magick, dependsOn: "convertSvg${pack}")

                                project.packer.beforeResize.each {
                                    Hook hook ->
                                        // if applyToPacks is not set, we apply the hook to all packs
                                        if (hook.applyToPacks.contains(pack.toString()) || hook.applyToPacks.size() == 0) {
                                            hook.configure.ext.set('pack', pack)
                                            project.configure(resizeImagesTask, hook.configure)
                                        }
                                }

                                project.configure(resizeImagesTask) {
                                    input pack.textures
                                    output "out/resources/${resolution}/${pack}"
                                    convert {
                                        resize resolution.ratio
                                    }
                                }

                                project.packer.afterResize.each {
                                    Hook hook ->
                                        // if applyToPacks is not set, we apply the hook to all packs
                                        if (hook.applyToPacks.contains(pack.toString()) || hook.applyToPacks.size() == 0) {
                                            hook.configure.ext.set('pack', pack)
                                            project.configure(resizeImagesTask, hook.configure)
                                        }
                                }

                                project.configure(resizeImagesTask) {
                                    convert {
                                        condition pack.ninePatches, {
                                            border {
                                                color 'none'
                                                width 1
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

                                tasks.create(name: "copyPacks${resolution}${pack}", type: Copy) {
                                    from pack.toString(), "${project.packer.packs.resourcesPath}/${pack}"
                                    into "out/resources/${resolution}/${pack}"
                                    include "${resolution}.json"
                                    rename { f -> 'pack.json' }
                                }

                                Task createPacksTask = tasks.create(name: "createPacks${resolution}${pack}", type: Packer, dependsOn: ["resizeImages${resolution}${pack}", "copyPacks${resolution}${pack}"]) {
                                    from pack.toString(), "out/resources/${resolution}"
                                    into project.packer.packs.packsPath(resolution)
                                }

                                project.buildPacks.dependsOn "createPacks${resolution}${pack}"
                                project.cleanPacks.dependsOn "cleanCreatePacks${resolution}${pack}"
                                project.cleanPacks.dependsOn "cleanResizeImages${resolution}${pack}"
                        }
                }
            }
        }
    }
}



