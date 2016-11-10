# graphQLQueryGen
A GraphQL Query Generator using annotation processing

# Building the library
In most cases, you can just run
`./gradlew jar sourcesJar javadocJar`
from the command line.

You can also build them with an Intellij IDEA "Gradle" build configuration like so:
```
Gradle Project:         /path/to/build.gradle
Tasks:                  jar sourcesJar javadocJar
```

![Screenshot of the Intellij IDEA 'Configure Project' screen, as described.](http://i.imgur.com/uhjWNXn.png)

In both cases, three Jar files will be placed in the
'out' folder - just copy these over to use them in
your own project.

```groovy
// In your personal project's build.gradle
dependencies {
    compile fileTree(include: ['*.jar'], dir: 'whatever/folder/you/put/the/jars/in')
}
```
