
# ItemNBT

ItemNBT is a simple library adding `ItemData` to save & load NBT data for your modded items.

It's available _exclusively_ for the 1.17.x versions of Minecraft.

## Install

The library is available on [my Maven](https://github.com/RedGrapefruit09/Maven).

At the end of your `gradle.properties` file:

```properties
itemnbt_version=2.1
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

### Concepts

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

### Practice

Now, let's get to work!

In this example, we'll create an item which contains a serialized value named `count`, increments it every tick and\
displays its value in the item's tooltip.

##### Item

```java
public class CounterItem extends Item {
    public CounterItem() {
        super(new Item.Settings().group(ItemGroup.MISC)); // put our item into the MISC group for testing purposes
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        // Append the tooltip list with the label indicating the value of the counter
        tooltip.add(new LiteralText("Counter: " + getData(stack).counter));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        // Every tick we'll increment the counter
        getData(stack).counter++;
    }

    private CounterItemData getData(ItemStack stack) { // it's good practice to set up such method to avoid repetitiveness when fetching your data instance
        // We'll fetch the instance of CounterItemData using the ItemStack we have got as an argument
        ItemData uncheckedData = ItemDataManager.get(stack, ExampleMod.COUNTER_DATA_CLASSIFIER);
        // Now we have to make sure the data is instanceof CounterItemData and not null (always check for null since you'll get null if the search failed)
        Objects.requireNonNull(uncheckedData, "Search failed");
        return (CounterItemData) uncheckedData; // now just cast the result to our type
    }
}
```

##### ItemData

```java
public class CounterItemData implements ItemData {
    public int counter; // our counter parameter

    public CounterItemData() {
        // Set the value to default. The actual value will be set after deserialization
        counter = 0;
    }

    @Override
    public void readNbt(@NotNull Item item, @NotNull ItemStack stack, @NotNull NbtCompound nbt) {
        // Read in your data here using the NbtCompound given
        counter = nbt.getInt("Counter");
    }

    @Override
    public void writeNbt(@NotNull Item item, @NotNull ItemStack stack, @NotNull NbtCompound nbt) {
        // Write your data here into the NbtCompound given, so it's read back in next time you log into your world
        nbt.putInt("Counter", counter);
    }
}
```

##### Initializer

```java
public class ExampleMod implements ModInitializer {
    // instances of CounterItemData will be attached to all CounterItem's
    public static final Classifier COUNTER_DATA_CLASSIFIER = Classifier.ofType(CounterItem.class);
    public static final CounterItem COUNTER_ITEM = new CounterItem(); // our actual item to register like usual

    static {
        // Register your ItemData types at static initialization
        ItemDataManager.register(COUNTER_DATA_CLASSIFIER, CounterItemData::new); // pass the constructor reference as the factory
    }

    @Override
    public void onInitialize() {
        // Here we'll register the item
        Registry.register(Registry.ITEM, new Identifier("example_mod", "counter_item"), COUNTER_ITEM);
    }
}
```
