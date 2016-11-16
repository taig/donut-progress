enablePlugins( AndroidLib )

autoScalaLibrary := false

githubProject := "donut-progress"

javacOptions ++=
    "-source" :: "1.7" ::
    "-target" :: "1.7" ::
    Nil

libraryDependencies ++=
    "com.android.support" % "appcompat-v7" % "25.0.1" ::
    Nil

minSdkVersion := "7"

name := "donut-progress"

organization := "io.taig.android"

platformTarget := "android-24"

publishArtifact in ( Compile, packageDoc ) := false

scalacOptions ++=
    "-deprecation" ::
    "-feature" ::
    Nil

targetSdkVersion := "24"

typedResources := false

version := "1.0.8"