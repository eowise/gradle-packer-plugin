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
    add 'xhdpi'
    add 'hdpi', 0.75
    add 'mdpi', 0.5   
    add 'ldpi', 0.375
  }
  
  packs {
    add 'Background'
    add 'Monsters'
    add 'Hero'
    from 'path/to/resources'
    to 'path/to/packs'
  }
}
```

### Terminology

### Workflow

1. Convert all SVGs in resources paths to PNGs.
2. (Before resize hook).
3. Resize all PNGs in resources paths using the ratio specified for each resolution.
4. (After resize hook).
5. Apply 1 pixel 9-patches to all PNG ending with '.9.png.
6. Copy 'resolutionName.json' from the resources path to the working directory, and rename it to 'pack.json'.
7. Call the libgdx texture packer to packs the textures into the output directory (generally android assets directory).

### Resolutions

### Packs
