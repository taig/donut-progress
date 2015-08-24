description := "Circular ProgressBar Widget for Android"

homepage := Some( url( "https://github.com/taig/donutprogress" ) )

licenses := Seq( "MIT" -> url( "https://raw.githubusercontent.com/taig/donutprogress/master/LICENSE" ) )

organizationHomepage := Some( url( "http://taig.io" ) )

pomExtra := {
    <issueManagement>
        <url>https://github.com/taig/donutprogress/issues</url>
        <system>GitHub Issues</system>
    </issueManagement>
        <developers>
            <developer>
                <id>Taig</id>
                <name>Niklas Klein</name>
                <email>mail@taig.io</email>
                <url>http://taig.io/</url>
            </developer>
        </developers>
}

pomIncludeRepository := { _ => false }

publishArtifact in Test := false

publishMavenStyle := true

publishTo <<= version ( version => {
    val url = Some( "https://oss.sonatype.org/" )

    if( version.endsWith( "SNAPSHOT" ) ) {
        url.map( "snapshot" at _ + "content/repositories/snapshots" )
    } else {
        url.map( "release" at _ + "service/local/staging/deploy/maven2" )
    }
} )

scmInfo := Some(
    ScmInfo(
        url( "https://github.com/taig/donutprogress" ),
        "scm:git:git://github.com/taig/donutprogress.git",
        Some( "scm:git:git@github.com:taig/donutprogress.git" )
    )
)

sonatypeProfileName := "io.taig"

startYear := Some( 2015 )