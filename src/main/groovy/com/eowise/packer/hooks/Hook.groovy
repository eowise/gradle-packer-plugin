package com.eowise.packer.hooks
/**
 * Created by aurel on 17/12/13.
 */
class Hook {


    Closure configure
    List<String> applyToAtlases

    Hook() {
        this.applyToAtlases = []
    }
    
    def configure(Closure configure) {
        this.configure = configure
    }
    
    def applyToPacks(String... atlases) {
        this.applyToAtlases.addAll(atlases)
    }

}
