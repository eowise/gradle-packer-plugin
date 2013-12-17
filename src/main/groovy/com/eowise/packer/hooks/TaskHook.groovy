package com.eowise.packer.hooks

import org.gradle.api.Task

/**
 * Created by aurel on 17/12/13.
 */
class TaskHook {

    String name
    Class<? extends Task> type
    Closure configure
    List<String> applyToPacks

    TaskHook() {
        this.applyToPacks = []

    }

    def name(String name) {
        this.name = name
    }
    
    def type(Class<? extends Task> type) {
        this.type = type
    }
    
    def configure(Closure configure) {
        this.configure = configure
    }
    
    def applyToPacks(String... packs) {
        this.applyToPacks.addAll(packs)
    }

}
