
# End-of-life

The 1.x API has reached its _end-of-life_, please use the [new 2.x API](https://github.com/RedGrapefruit09/ItemNBT/tree/v2.x). \
The 1.x API had critical usability issues and inconveniences, which have been fixed in newer version.

# ItemNBT

ItemNBT is a tiny library for saving & loading NBT data for items.\
It is extremely easy to use & setup, unlike the vanilla approach, which is overly complicated.

## Install

The library is available on [my Maven](https://github.com/RedGrapefruit09/Maven) to include.

Put this into your `gradle.properties`, at the end of the file:
```properties
itemnbt_version=1.1
```

Put this into your `build.gradle`:
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
    modImplementation "com.redgrapefruit.itemnbt:itemnbt:${project.itemnbt_version}"
    // This will bundle the library with your mod, so it doesn't have to be separately installed
    include "com.redgrapefruit.itemnbt:itemnbt:${project.itemnbt_version}"
}
```

## Use

Just put this into your item's body:

```java
    static {
        ItemNBT.register(
            ItemType.instance(MyItem.class), // Swap MyItem with your item's class name
            (nbt, stack) -> {
                // Your saving code. Obtain your item's instance like this: (MyItem) stack.item
            },
            (nbt, stack) -> {
                // Your loading code. Obtain your item's instance like this: (MyItem) stack.item
            }
        );
    }
```
