# Constraints demo

1. In the main aerie repo:
   1. Checkout `feat/procedural-constraints-lib`
   2. run `./gradlew :constraints:shadowJar`
   3. deploy aerie and create a plan you want to test, and simulate it
2. In this repo:
   1. update the path to the generated constraints lib jar in `build.gradle`
   2. you may need to manually specify the jar as a dependency in IntelliJ project settings in order for it to be indexed.
   3. in `ConstraintsDemo.kt`, configure the database connection and set the simulation dataset id
   4. in `ConstraintsDemo.kt`, choose which constraint to run
   5. run `./gradlew run`

I also recommend opening the generated documentation in the main repo:
1. run `./gradlew dokkaHtmlMultiModule`
2. in intellij, find the file in `build/dokka/htmlMultiModule/index.html`
3. have intellij host the site for you with `Right click -> Open In -> Browser`. This will appease the CORS gods so the JS search and sidebar will work.

Constraints can be implemented in Java or Kotlin. Java will work fine, but there are some differences
- Java's type inference can struggle on the lower level operations, requiring you to specify the generic type arguments manually. See ActivityAndResource.java for example
- The `Violations` static method constructors such as `Violations.mutex` in `BinaryActivity` are implemented as extension functions in Kotlin. So in Java it would be `Violations.mutex(a, b)`, but in Kotlin it would be `a.mutex(b)` or even `a mutex b`.

I generally recommend Kotlin because it is more concise and readable, and has null safety in the type system.
