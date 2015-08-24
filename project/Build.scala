import sbt._
import sbt.Keys._
import android.Keys._
import android.Plugin._
import xerial.sbt.Sonatype.sonatypeSettings

object Build extends sbt.Build {
    val main = Project( "donut-progress", file( "." ), settings = androidBuildAar )
        .settings(
            javacOptions ++=
                "-source" :: "1.7" ::
                "-target" :: "1.7" ::
                Nil,
            libraryDependencies ++=
                "com.android.support" % "appcompat-v7" % "23.0.0" ::
                Nil,
            name := "DonutProgress",
            organization := "io.taig.android",
            scalaVersion := "2.11.7",
            scalacOptions ++=
                "-deprecation" ::
                "-feature" ::
                Nil,
            version := "1.0.0-SNAPSHOT"
        )

    lazy val test = flavorOf( main, "test" )
        .settings(
            fork in Test := true,
            libraryDependencies ++=
                "com.geteit" %% "robotest" % "0.12" ::
                "org.scalatest" %% "scalatest" % "2.2.5" ::
                Nil,
            libraryProject in Android := false
        )
}