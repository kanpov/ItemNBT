
# ItemNBT

ItemNBT is a simple library adding `ItemData` to save & load NBT data for your modded items.

It's available _exclusively_ for the 1.17.x versions of Minecraft.

## Install

The library is available on [my Maven](https://github.com/RedGrapefruit09/Maven).

At the end of your `gradle.properties` file:

```properties
itemnbt_version=2.0
```

In your `build.gradle`:

```groovy
repositories {
    maven {
        url = "https://raw.githubusercontent.com/RedGrapefruit09/Maven/master"
        content {
            includeGroup "com.redgrapefruit.itemnbt"
        }
    }
}

dependencies {
    // All your other dependencies here
    
    modImplementation "com.redgrapefruit.itemnbt:itemnbt:${project.itemnbt_version}"
    include "com.redgrapefruit.itemnbt:itemnbt:${project.itemnbt_version}"
}
```

This will include the `com.redgrapefruit.itemnbt` group of the Maven, import the dependency\
and bundle it inside your mod's JAR file, so it doesn't have to be installed separately.

## Use

TBD
