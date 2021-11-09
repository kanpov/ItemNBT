
# ItemNBT 3.x

This is the branch for the active rewrite of ItemNBT to ItemNBT3.

All core features have already been delivered, a stable release will be packaged after I finish all the docs.

## Use

To preview the library, add this code into your `build.gradle`:

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
  modImplementation "com.redgrapefruit.itemnbt:itemnbt:3.0-alpha.4"
  include "com.redgrapefruit.itemnbt:itemnbt:3.0-alpha.4"
}
```
