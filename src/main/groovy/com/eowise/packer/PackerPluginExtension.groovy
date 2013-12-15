package com.eowise.packer

/**
 * Created by aurel on 15/12/13.
 */
class PackerPluginExtension {
    Set<Resolution> resolutions
    Set<String> packNames
    Resolution baseResolution
    String packsPath

    PackerPluginExtension() {
        resolutions = []
        packNames = []
    }

    def resolutions(Closure closure) {
        closure.delegate = this
        closure()
    }

    def add(int w, h) {
        Resolution r = new Resolution(w, h)
        resolutions.add(r)
    }

    def base(int w, h) {
        Resolution r = new Resolution(w, h)
        resolutions.add(r)
        baseResolution = r
    }

    def packs(Closure closure) {
        closure.delegate = packNames
        closure()
    }

}
