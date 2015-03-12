package com.eowise.packer.hooks
/**
 * Created by aurel on 17/12/13.
 */
class Hook {

    Closure task
    List<String> applyToAtlases

    Hook(Closure task, String... atlases) {
        this.task = task
        this.applyToAtlases = atlases
    }

}
