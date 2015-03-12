package com.eowise.packer.hooks

import com.eowise.packer.extension.NamedResolution

/**
 * Created by aurel on 12/03/15.
 */
class Hooks extends ArrayList<Hook> {

    def add(Closure task) {
        add(task, [])
    }

    def add(Closure configure, String... atlases) {
        add(new Hook(configure, atlases))
    }
}
