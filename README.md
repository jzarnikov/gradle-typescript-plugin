gradle-typescript-plugin
========================

Plugin for the Gradle build system for projects that use the TypeScript programming language


Usage
=====
Simply add the plugin to your Gradle build


```groovy
apply plugin: 'typescript'

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath group: 'at.irian.typescript', name: 'gradle-typescript-plugin', version: '0.12'
    }
}
```

Have a look at the sample project to see how to structure your project, reference libraries and test your code.


Requirements and assumptions
============================
* Gradle >=1.10
* Node.js, PhantomJS, RequireJS, TypeScript
* Your source and your libraries use AMD modules

Available tasks
===============

Run

    gradle tasks --all

to see all available tasks.

Configuration
=============

you can set various configuration properties of the plugin to reflect your project structure:

```groovy
typescript {
    configProp1 = "someValue"
    configProp2 = "someOtherValue"
}
```

The following configurations properties are available:
* `sourcePath` - path (relative to project root) to the directory containing your TypeScript sources. Default is `src/main/typescript`
* `testSourcePath` - path (relative to project root) to the directory containing your TypeScript tests sources. Default is `src/test/typescript`
* `generatedJsPath` - output directory (relative to project root) for the TypeScript compiler. Default is `${project.buildDir}/generatedJs` (=`build/generatedJs` unless you have configured your `buildDir` differently)
* `requireJsConfig` - the filename of your RequireJS config (paths, shims for your libraries). Must be in `sourcePath` and define a global variable with the name `requireConfig`. Default is `requirejsConfig.js`
* `requireConfigGlobalVarName` - allows you to change the name of the global variable described in previous point. Default is `requireConfig`
* `tscOptions` - a string array with additional that will be passed to the TypeScript compiler. Default is empty.
* `cleanMainExcludes` - a list of [path patterns](http://www.gradle.org/docs/current/javadoc/org/gradle/api/tasks/util/PatternFilterable.html) that will not be deleted when cleaning `generatedJs` during the `cleanMain` task. This is usefull when pointing `generatedJsPath` to the same directory as `sourcePath`. Default is empty.
* `vendorPath` - path (relative to `sourcePath`) to the directory that conains your 3rd party JavaScript libraries. Default is `vendor`
* `combineJsModules` - list of names of modules that will be passed to the RequireJS optimizer when generating a single JavaScript file with all your code. This should be the modules(s) that contain the entry-point(s) to your application. See the sample project for details. Default is empty.
* `includeLibsInCombinedJs` - whether or not 3rd parth JavaScript libraries (configured in your `requireJsConfig`) should be included in the result of the RequireJS optimizer. Default is `true`


#Tests#

##Writing tests##

This plugin supports compiling and running [Jasmine](http://jasmine.github.io/1.3/introduction.html) tests written in TypeScript. You can run the task `initTypeScriptTestResources` to copy the Jasmine type definition file (jasmine.d.ts) into your test folder. The Jasmine test runner is provided and automatically configured by this plugin.

##Running tests##

This plugin allows you to run tests as part of your build cycle. There are two different modes for running tests:

1. On the command line - the `test` task. This is useful for quickly checking that everything still works (TDD) or for your CI-build. A nice report is written to the console showing the test results. Any test failures will lead to a build failure.
2. In the browser - the `runTypeScriptTestsInBrowser` task. This is useful debugging failing tests. This tasks starts your default browser and loads the tests for execution.
 
By default all tests from your `testSourcePath` are compiled and executed. If you want to run only a certain tests(s) you can do so using a Gradle propery:

    ./gradlew -Pat.irian.typescript.test=myTest,someSubfolder/myOtherTest test
    
(comma-separated list of paths to your tests relative to `testSourcePath`, the ".ts" suffixes can be omitted).


