
# ItemNBT 3.x

This is an early-work-in-progress 3rd rewrite of the library.\
A stable version is expected to be available in Q1 2022.

The roadmap is as follows:

- [x] Conceptual design
- [x] Manual NBT serialization
- [x] Specification-based serialization
- [x] Linked-specification-based serialization
- [ ] Out-of-the-box collections
- [ ] Advanced features:
    - [ ] Serialization events
    - [ ] Plugin API
    - [ ] Data preprocessors
    - [ ] Data postprocessors
    - [ ] Data fixers

## Use

The library is **not ready** for usage yet, but early alphas are already available.

The latest alpha release is: `3.0-alpha.2`.

Include this in your `build.gradle`:
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
  modImplementation "com.redgrapefruit.itemnbt:itemnbt:3.0-alpha.2"
  include "com.redgrapefruit.itemnbt:itemnbt:3.0-alpha.2"
}
```
