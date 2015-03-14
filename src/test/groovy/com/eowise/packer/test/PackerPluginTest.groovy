package com.eowise.packer.test
import com.eowise.packer.Packer
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

import java.util.regex.Pattern

class PackerPluginTest extends Specification {

    protected Project project

    def setup() {
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'packer'
    }
    
    def "Can create task"() {
        when:
        Packer packer = project.task(type: Packer, 'packer') as Packer

        then:
        packer != null
    }

    def "Can do simple packing"() {
        String atlasName = 'Atlas1'
        String resolutionName = 'base'
        Packer packer = project.task(type: Packer, 'packerSimple') as Packer

        when:
        project.configure(packer) {
            resourcesInputPath 'resources'
            atlasesOutputPath 'out/atlases'
        }

        packer.setup()

        then:
        dependenciesCreated('packerSimple')
    }


    def "Can do packing with two atlases"() {
        String atlasName = 'Atlas'
        String resolutionName = 'base'
        Packer packer = project.task(type: Packer, 'packerTwoAtlases') as Packer
        
        when:
        project.configure(packer) {
            resourcesInputPath 'resources'
            atlasesOutputPath 'out/atlases'
            
            atlases {
                add atlasName + '1'
                add atlasName + '2'
            }
            resolutions {
                add resolutionName
            }
        }
        packer.setup()

        then:
        dependenciesCreated('packerTwoAtlases', atlasName + '1', resolutionName)
        dependenciesCreated('packerTwoAtlases', atlasName + '2', resolutionName)
    }

    public void "Can do packing with closures"() {
        String atlasName = 'Atlas1'
        String resolutionName = 'base'
        Packer packer = project.task(type: Packer, 'packerClosures') as Packer
        
        when:
        project.configure(packer) {
            resourcesInputPath { atlas -> "resources/${atlas}" }
            atlasesOutputPath { resolution -> "out/atlases/${resolution}" }
            
            atlases {
                add atlas(name: atlasName)
            }
            resolutions {
                add resolution(name: resolutionName)
            }
        }

        packer.setup()

        then:
        dependenciesCreated('packerClosures', atlasName, resolutionName)
    }

    def "Can do afterResize hook"() {
        String atlasName = 'Atlas1'
        String resolutionName = 'base'
        Packer packer = project.task(type: Packer, 'packerAfterResize') as Packer
        Closure afterResizeClosure = { a, r -> project.task("afterResize${a}${r}")}

        when:
        project.configure(packer) {
            resourcesInputPath 'resources'
            atlasesOutputPath 'out/atlases'

            atlases {
                add atlas(name: atlasName, afterResize: afterResizeClosure )
            }

            resolutions {
                add resolutionName
            }
        }

        packer.setup()

        then:
        dependenciesCreated('packerAfterResize', atlasName, resolutionName)
        assert project.tasks.getByName("afterResizeAtlas1base") != null

    }


    void dependenciesCreated(String taskName, String atlasName = '', String resolutionName = '') {
        assert project.tasks.getByName("${taskName}ConvertSvg${atlasName}") != null
        assert project.tasks.getByName("${taskName}ResizeImages${resolutionName}${atlasName}") != null
        assert project.tasks.getByName("${taskName}CopyPacks${resolutionName}${atlasName}") != null
        assert project.tasks.getByName("${taskName}CreatePacks${resolutionName}${atlasName}") != null
    }
}
