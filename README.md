# gradle-packer-plugin

A Gradle plugin witch automatically packs texture for a libgdx game, using libgdx [texture packer](https://github.com/libgdx/libgdx/wiki/Texture-packer#wiki-NinePatches). Before packing, the plugin can do some actions on the resources provided, such as :

* Resize images for variants
* generate 1 pixel 9-patches automatically
* Convert SVG to PNG images

## Install

## Usage

```groovy
packer {
  resolutions {
    // the base resolution
    base 'xhdpi'
    // others resolutions, with a ratio relative to the base resolution
    add 'hdpi', 0.75
    add 'mdpi', 0.5   
    add 'ldpi', 0.375
  }
  
  packs {
    add 'Background'
    add 'Monsters'
    add 'Hero'
    from 'path/to/resources'
    to { resolution -> 'path/to/packs/' + resolution }
  }
}
```

### Resolutions

### Packs
