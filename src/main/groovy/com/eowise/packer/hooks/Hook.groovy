package com.eowise.packer.hooks
/**
 * Created by aurel on 17/12/13.
 */
class Hook {


    Closure configure
    List<String> applyToPacks

    Hook() {
        this.applyToPacks = []
    }
    
    def configure(Closure configure) {
        this.configure = configure
    }
    
    def applyToPacks(String... packs) {
        this.applyToPacks.addAll(packs)
    }

}
