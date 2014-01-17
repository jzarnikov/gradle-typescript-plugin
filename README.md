gradle-typescript-plugin
========================

Plugin for the Gradle build system for projects that use the TypeScript programming language


Usage
=====
Simply add the plugin to your Gradle build

    apply plugin: 'typescript'

    buildscript {
        repositories {
            mavenCentral()
        }
        dependencies {
            classpath group: 'at.irian.typescript', name: 'gradle-typescript-plugin', version: '0.3'
        }
    }

Have a look at the sample project to see how to structure your project, reference libraries and test your code.


Requirements and assumptions
============================
* Gradle >=1.10
* Node.js, PhantomJS, RequireJS, TypeScript
* Your source and your libraries use AMD modules
