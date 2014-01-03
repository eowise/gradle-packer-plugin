package com.eowise.packer.test

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

/**
 * Created by aurel on 03/01/14.
 */
class PackerTestCase {

    protected Project project
    
    public PackerTestCase() {
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'packer'
    }

}
