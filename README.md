# gradle-packer-plugin

A Gradle plugin witch automatically packs texture for a libgdx game, using libgdx [texture packer](https://github.com/libgdx/libgdx/wiki/Texture-packer#wiki-NinePatches). Before packing, the plugin can do some actions on the resources provided, such as :

* Resize images for variants
* Convert SVG to PNG images

## Install

```groovy
buildscript {
  repositories {
    mavenCentral()
  }

  dependencies {
    classpath 'com.eowise:packer:0.7.0'
  }
}

apply plugin: 'packer'
```

## Usage

A very basic scenario :
```groovy
task packs(type: com.eowise.packer.Packer) {
  resourcesInputPath 'path/to/resources'
  atlasesOutputPath 'path/to/atlases'
}
```
This task will do the folowing actions :

1. Convert all SVGs to PNGs in the 'path/to/resources' tree
2. Copy all PNGs do a working directory
3. Copy all file named 'pack.json' from the resouces path to the working directory
4. Call the libgdx texture packer to packs the textures from the working directory into the 'path/to/atlases' directory (generally android assets directory)

### Resolutions

You may want to add several resolutions :
```groovy
task packs(type: com.eowise.packer.Packer) {
  resourcesInputPath 'path/to/resources'
  atlasesOutputPath 'path/to/packs'

  resolutions {
    add resolution(name: 'xhdpi')
    add resolution(name: 'hdpi', ratio: 0.75)
    add resolution(name: 'mdpi', ratio: 0.5)
    add resolution(name: 'ldpi', ratio: 0.375)
  }
}

All imput iamges will be resized according given ratio.

```
### Atlases

You can also pack several atlases :
```groovy
task packs(type: com.eowise.packer.Packer) {
  resourcesInputPath 'path/to/resources'
  atlasesOutputPath 'path/to/packs'

  atlases {
    add atlas(name: 'MainMenu')
    add atlas(name: 'Game')
    add atlas(name: 'Options')
  }
}
```
## Workflow

1. Convert all SVGs in resources paths to PNGs.
2. (Before resize hook).
3. Resize all PNGs in resources paths using the ratio specified for each resolution.
4. (After resize hook).
5. Copy 'resolutionName.json' from the resources path to the working directory, and rename it to 'pack.json'.
6. Call the libgdx texture packer to packs the textures into the output directory (generally android assets directory).
