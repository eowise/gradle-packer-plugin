package com.eowise.packer.test
import com.eowise.packer.Packer
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

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
        Packer packer = project.task(type: Packer, 'packer') as Packer

        when:
        project.configure(packer) {
            resourcesInputPath 'resources'
            atlasesOutputPath 'out/atlases'
        }

        packer.setup()

        then:
        project.tasks.findByName("convertSvg") != null
    }


    def "Can do packing with two atlases"() {
        String atlasName = 'Atlas'
        String resolutionName = 'base'
        Packer packer = project.task(type: Packer, 'packer') as Packer
        
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
        project.tasks.findByName("convertSvg${atlasName}1") != null
        project.tasks.findByName("convertSvg${atlasName}2") != null
    }

    public void "Can do packing with closures"() {
        String atlasName = 'Atlas1'
        String resolutionName = 'base'
        Packer packer = project.task(type: Packer, 'packer') as Packer
        
        when:
        project.configure(packer) {
            resourcesInputPath { atlas -> "resources/${atlas}" }
            atlasesOutputPath { resolution -> "out/atlases/${resolution}" }
            
            atlases {
                add  atlasName
            }
            resolutions {
                add resolutionName
            }
        }

        packer.setup()

        then:
        project.tasks.getByName("convertSvg${atlasName}") != null
    }
    
}
