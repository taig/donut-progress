androidBuildAar

autoScalaLibrary := false

githubProject := "donutprogress"

javacOptions ++=
    "-source" :: "1.7" ::
    "-target" :: "1.7" ::
    Nil

libraryDependencies ++=
    "com.android.support" % "appcompat-v7" % "23.2.1" ::
    Nil

minSdkVersion := "7"

name := "DonutProgress"

normalizedName := "donut-progress"

organization := "io.taig.android"

platformTarget := "android-23"

publishArtifact in ( Compile, packageDoc ) := false

scalacOptions ++=
    "-deprecation" ::
    "-feature" ::
    Nil

targetSdkVersion := "23"

typedResources := false

version := "1.0.4"