# gradle-packer-plugin

A Gradle plugin witch automatically packs texture for a libgdx game, using libgdx [texture packer](https://github.com/libgdx/libgdx/wiki/Texture-packer#wiki-NinePatches). Before packing, the plugin can do some actions on the resources provided, such as :

* Resize images for variants
* generate 1 pixel 9-patches automatically
* Convert SVG to PNG images

## Install
```groovy
buildscript {
  repositories {
    maven {
      url: 'https://oss.sonatype.org/content/repositories/snapshots/'
  }

  dependencies {
    classpath 'com.eowise:packer:0.5.0-SNAPSHOT'
  }
}

apply plugin: 'packer'
```

## Usage

A very basic scenario :

```groovy
task packs(type: com.eowise.packer.Packer) {
  resourcesInputPath 'path/to/resources'
  atlasesOutputPath 'path/to/packs'
}
```

### resolutions

You may want to add multiple resolutions :

```groovy
task packs(type: com.eowise.packer.Packer) {
  resourcesInputPath 'path/to/resources'
  atlasesOutputPath 'path/to/packs'

  resolutions {
    add 'xhdpi'
    add 'hdpi', 0.75
    add 'mdpi', 0.5   
    add 'ldpi', 0.375
  }
}
```

### Atlases

You can also pack multilple atlases :

```groovy
task packs(type: com.eowise.packer.Packer) {
  resourcesInputPath 'path/to/resources'
  atlasesOutputPath 'path/to/packs'

  atlases {
    add 'MainMenu'
    add 'Game'
    add 'Options'
  }
}
```



## Workflow

1. Convert all SVGs in resources paths to PNGs.
2. (Before resize hook).
3. Resize all PNGs in resources paths using the ratio specified for each resolution.
4. (After resize hook).
5. Apply 1 pixel 9-patches to all PNG ending with '.9.png.
6. Copy 'resolutionName.json' from the resources path to the working directory, and rename it to 'pack.json'.
7. Call the libgdx texture packer to packs the textures into the output directory (generally android assets directory).
