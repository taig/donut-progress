enablePlugins( AndroidLib )

autoScalaLibrary := false

githubProject := "donut-progress"

javacOptions ++=
    "-source" :: "1.7" ::
    "-target" :: "1.7" ::
    Nil

libraryDependencies ++=
    "com.android.support" % "appcompat-v7" % "27.1.1" ::
    Nil

minSdkVersion := "7"

name := "donut-progress"

organization := "io.taig.android"

platformTarget := "android-27"

publishArtifact in ( Compile, packageDoc ) := false

scalacOptions ++=
    "-deprecation" ::
    "-feature" ::
    Nil

targetSdkVersion := "27"

typedResources := false