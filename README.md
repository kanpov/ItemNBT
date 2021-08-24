
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

### 1. Concepts

Before we begin, you should get yourself familiar with some concepts added by this library:

##### ItemData

`ItemData` is the interface you'll implement to store, read and write your item's data.\
`ItemData` instances are attached **to the stack, not the item**, since items are flyweights.

The data is read from the stack's NBT using the `readNbt` method:
```java
void readNbt(@NotNull Item item, @NotNull ItemStack stack, @NotNull NbtCompound nbt);
```
Then, it's written back into the stack's NBT using the `writeNbt` method:
```java
void writeNbt(@NotNull Item item, @NotNull ItemStack stack, @NotNull NbtCompound nbt);
```

All stored data should be `public` for your item to access and use it.

##### Classifier

`Classifier`s are expressions defining when to use the `ItemData` for a particular stack.

Basically, they do something like this: _this stack's item is `MyItem`, so attach `MyItemData`_.

There are standard types built-in:

- `Classifier.ofType(MyItem.class)` - apply for all instances of `MyItem`
- `Classifier.ofItem(MyItems.MY_ITEM)` - apply for all equal to `MyItems.MY_ITEM`
- `Classifier.ofTypes(MyItemA.class, MyItemB.class)` - apply for all instances of `MyItemA` _or_ `MyItemB`
- `Classifier.ofItems(MyItems.MY_ITEM_A, MyItems.MY_ITEM_B)` - apply for all equal to `MyItems.MY_ITEM_A` or `MyItems.MY_ITEM_B`

##### Classifier Safety

There is also an important concept related to `Classifier`s - their safety.

You see, some `Classifier` might have conditions that return _multiple_ or _unexpected_ results for some stacks.\
To avoid these kinds of inconveniences, using the `ItemDataManager.get` method **is forbidden for
unsafe `Classifier`s**, since it returns the _first_ found result, which can cause problems.

To determine which `Classifier`s are unsafe, you can check if the factory for them has the `@UnsafeClassifier` annotation.

##### ItemDataManager

`ItemDataManager` provides access to the API for fetching and registering `ItemData`.

When registering `ItemData` into the system, you pass the identifying `Classifier` and the factory `Supplier`.\
The factory will be called to create a new instance of your `ItemData` for every stack, so the data stored isn't shared.

The `register` method has this signature:
```java
public static void register(@NotNull Classifier classifier, @NotNull Supplier<ItemData> factory);
```

Registering should typically occur at static initialization, like this: `static { // your register calls }`

To fetch an instance of your `ItemData` (typically so your item can use item), you have two method

- `ItemDataManager.get(ItemStack, Classifier)` - returns the first `ItemData` instance matching the given `Classifier` from the stack.\
  Typically, this method is preferred, except for when you're using unsafe `Classifier`s.
- `ItemDataManager.get(ItemStack)` - returns _all possible results_.\
  This is the only way to securely fetch instances when using unsafe `Classifier`s.

