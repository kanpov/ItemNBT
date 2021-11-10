
# ItemNBT 3.x

This is the branch for the active rewrite of ItemNBT2 to ItemNBT3.

The library is currently in **beta**, it's very close to a stable release.\
A stable release will be published once all the docs are written.

To use the latest beta in your mod, add this code into your `build.gradle`:

```groovy
repositories {
  maven {
    url = "https://redgrapefruit09.github.io/maven"
    content {
      includeGroup "com.redgrapefruit.itemnbt"
    }
  }
}

dependencies {
  modImplementation "com.redgrapefruit.itemnbt:itemnbt:3.0-beta.1"
  include "com.redgrapefruit.itemnbt:itemnbt:3.0-beta.1"
}
```
