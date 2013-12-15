package com.eowise.packer

/**
 * Created by aurel on 15/12/13.
 */
class PackerPluginExtension {
    Set<Variant> variants
    Set<String> packNames
    Variant baseVariant
    String packsPath

    PackerPluginExtension() {
        variants = []
        packNames = []
    }

    def variants(Closure closure) {
        closure.delegate = this
        closure()
    }

    def add(String name, float ratio) {
        variants.add(new Variant(name, ratio))
    }

    def base(String name) {
        Variant r = new Variant(name, 1f)
        variants.add(r)
        baseVariant = r
    }

    def packs(Closure closure) {
        closure.delegate = packNames
        closure()
    }

}
