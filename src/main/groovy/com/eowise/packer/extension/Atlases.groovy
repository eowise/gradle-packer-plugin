package com.eowise.packer.extension

import org.gradle.api.Project
/**
 * Created by aurel on 16/12/13.
 */
class Atlases extends ArrayList<Atlas> {

    private final Project project

    Atlases(final Project project) {
        super()
        this.project = project
    }

    def add(String name) {
        Atlas adding = new NamedAtlas(name)
        add(adding)
    }
    
    def add(String name, Closure closure) {
        Atlas adding = project.configure(new NamedAtlas(name), closure)
        add(adding)
    }
  
}