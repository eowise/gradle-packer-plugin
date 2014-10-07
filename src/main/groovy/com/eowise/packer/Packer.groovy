package com.eowise.packer
import com.eowise.imagemagick.tasks.Magick
import com.eowise.imagemagick.tasks.SvgToPng
import com.eowise.packer.extension.Atlas
import com.eowise.packer.extension.Atlases
import com.eowise.packer.extension.Resolution
import com.eowise.packer.extension.Resolutions
import com.eowise.packer.hooks.Hook
import org.gradle.api.DefaultTask
import org.gradle.api.Named
import org.gradle.api.Task
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Input
/**
 * Created by aurel on 04/01/14.
 */
class Packer extends DefaultTask {

    @Input
    private Resolution uniqueResolution
    @Input
    private Resolutions resolutions
    @Input
    private Atlas uniqueAtlas
    @Input
    private Atlases atlases

    Closure resourcesPathClosure
    Closure atlasesPathClosure
    List<Hook> beforeResize
    List<Hook> afterResize
    
    Packer() {
        this.uniqueResolution = extensions.create('resolution', Resolution)
        this.uniqueAtlas = extensions.create('atlas', Atlas)
        this.resolutions = extensions.create('resolutions', Resolutions)
        this.atlases = extensions.create('atlases', Atlases, project)
        this.beforeResize = []
        this.afterResize = []
        
        project.configure(project) {
            afterEvaluate {
                this.setup()
            }
        }
    }

    def defaultResolutions() {
        resolutions.add('ldpi', 0.375f)
        resolutions.add('mdpi', 0.5f)
        resolutions.add('hdpi', 0.75f)
        resolutions.add('xhdpi')
    }
    
    def resourcesInputPath(String resourcesPath) {
        this.resourcesPathClosure = { Atlas atlas -> atlas instanceof Named ? "${resourcesPath}/${atlas}" : resourcesPath }
    }

    def resourcesInputPath(Closure closure) {
        this.resourcesPathClosure = closure
    }


    def atlasesOutputPath(Closure closure) {
        this.atlasesPathClosure = closure
    }

    def atlasesOutputPath(String atlasesPath) {
        this.atlasesPathClosure = { Resolution resolution -> "${atlasesPath}/${resolution}" }
    }

    String resourcesPath(Atlas atlas) {
        return resourcesPathClosure(atlas)
    }

    String atlasesPath(Resolution res) {
        return atlasesPathClosure(res)
    }

    def beforeResize(Closure closure) {
        beforeResize.add(project.configure(new Hook(), closure) as Hook)
    }

    def afterResize(Closure closure) {
        afterResize.add(project.configure(new Hook(), closure) as Hook)
    }

    def setup() {

        if (atlases.size() == 0)
            atlases.add(uniqueAtlas)
        
        if (resolutions.size() == 0)
            resolutions.add(uniqueResolution)
        
        atlases.each() {
            atlas ->


                //FileTree textures = project.fileTree(dir: resourcesPath(atlas), include: ['**/*.png', '**/*.jpg']).matching(atlas.textures)
                //FileTree ninePatches = project.fileTree(dir: resourcesPath(atlas), include: '**/*.9.png').matching(atlas.ninePatches)
                //FileTree svgs = project.fileTree(dir: resourcesPath(atlas), include: '**/*.svg').matching(atlas.svgs)


                Task convertSvg = project.tasks.create(name: "${name}ConvertSvg${atlas}", type: Magick) {
                    convert resourcesPath(atlas), atlas.svgs
                    into resourcesPath(atlas)
                    actions {
                        inputFile()
                        outputFile { fileName, extension -> "${fileName}.png" }
                    }
                }

                dependsOn.each { d -> convertSvg.mustRunAfter d }

                resolutions.each() {
                    resolution ->

                        Task resizeImagesTask = project.tasks.create(name: "${name}ResizeImages${resolution}${atlas}", type: Magick, dependsOn: "${name}ConvertSvg${atlas}")

                        dependsOn.each { d -> resizeImagesTask.mustRunAfter d }

                        beforeResize.each {
                            Hook hook ->
                                // if applyToAtlases is not set, we apply the hook to all atlases
                                if (hook.applyToAtlases.contains(atlas.toString()) || hook.applyToAtlases.size() == 0) {
                                    hook.configure.ext.set('textures', atlas.textures)
                                    hook.configure.ext.set('ninePatches', atlas.ninePatches)
                                    hook.configure.ext.set('svgs', atlas.svgs)
                                    project.configure(resizeImagesTask, hook.configure)
                                }
                        }

                        project.configure(resizeImagesTask) {
                            convert resourcesPath(atlas), atlas.textures
                            into "out/resources/${resolution}/${atlas}"
                            actions {
                                inputFile()
                                -resize(resolution.ratio * 100 + '%')
                            }


                        }

                        afterResize.each {
                            Hook hook ->
                                // if applyToAtlases is not set, we apply the hook to all atlases
                                if (hook.applyToAtlases.contains(atlas.toString()) || hook.applyToAtlases.size() == 0) {
                                    hook.configure.ext.set('textures', atlas.textures)
                                    hook.configure.ext.set('ninePatches', atlas.ninePatches)
                                    hook.configure.ext.set('svgs', atlas.svgs)
                                    project.configure(resizeImagesTask, hook.configure)
                                }
                        }

                        project.configure(resizeImagesTask) {
                            actions {
                                condition atlas.ninePatches, {
                                    -matte
                                    -color('none')
                                    -width(1)
                                    xc('black')
                                    -gravity('North')
                                    -geometry('1x1+0x+0')
                                    -composite
                                    xc('black')
                                    -gravity('West')
                                    -geometry('1x1+0x+0')
                                    -composite
                                }
                                outputFile()
                            }
                        }

                        project.tasks.create(name: "${name}CopyPacks${resolution}${atlas}", type: Copy) {
                            from resourcesPath(atlas)
                            into "out/resources/${resolution}/${atlas}"
                            include "${resolution}.json"
                            rename { f -> 'pack.json' }
                        }

                        project.tasks.create(name: "${name}CreatePacks${resolution}${atlas}", type: TexturePacker, dependsOn: ["${name}ResizeImages${resolution}${atlas}", "${name}CopyPacks${resolution}${atlas}"]) {
                            from atlas.toString(), "out/resources/${resolution}"
                            into atlasesPath(resolution)
                        }

                        dependsOn "${name}CreatePacks${resolution}${atlas}"
                        //project.cleanPacks.dependsOn "cleanCreatePacks${resolution}${atlas}"
                        //project.cleanPacks.dependsOn "cleanResizeImages${resolution}${atlas}"
                }
        }
    }
    
    
}
