package com.eowise.packer.test
import com.eowise.packer.extension.Pack
import com.eowise.packer.extension.PackerPluginExtension
import com.eowise.packer.extension.Resolution
import org.gradle.api.DefaultTask
import org.junit.Test

import static junit.framework.TestCase.*

class PackerPluginTest extends PackerTestCase{
    
    @Test
    public void packerPluginAddsTasksToProject() {
        assertTrue(project.tasks.buildPacks instanceof DefaultTask)
        assertTrue(project.tasks.cleanPacks instanceof DefaultTask)
    }

    @Test
    public void packerPluginAddsExtensionToProject() {
        assertTrue(project.packer instanceof PackerPluginExtension)
    }

    @Test
    public void simplePacking() {
        String packName = 'Pack1'
        String resolutionName = 'base'
        
        PackerPluginExtension packer = project.configure(project.packer) {
            packs {
                from 'resources'
                to 'out/packs'
                add packName
            }
            resolutions {
                add resolutionName
            }
        }

        Pack pack = packer.packs.getByName(packName)
        Resolution res = packer.resolutions.isEmpty() ? null : packer.resolutions.first()

        testPack(packer, pack, packName, res, resolutionName)
    }

    @Test
    public void closurePacking() {
        String packName = 'Pack1'
        String resolutionName = 'base'

        PackerPluginExtension packer = project.configure(project.packer) {
            packs {
                from { pack -> "resources/${pack}" }
                to { resolution -> "out/packs/${resolution}" }
                add packName
            }
            resolutions {
                add resolutionName
            }
        }

        Pack pack = packer.packs.getByName(packName)
        Resolution res = packer.resolutions.isEmpty() ? null : packer.resolutions.first()

        testPack(packer, pack, packName, res, resolutionName)
    }
    
    public void testPack(PackerPluginExtension packer, Pack pack, String packName, Resolution res, String resolutionName) {
        assertNotNull(pack)
        assertNotNull(res)
        assertTrue(pack.toString() == packName)
        assertTrue(res.toString() == resolutionName)

        assertTrue(packer.packs.packsPath(res) == "out/packs/${resolutionName}")
        assertTrue(packer.packs.resourcesPath(pack) == "resources/${packName}")
    }
    
    
}
