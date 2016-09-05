// Place holder required by some sbt tools.



lazy val defaultSettings =
  Defaults.defaultSettings ++
// Publish.settings ++
// graphSettings ++
Seq(
  // scalacOptions in Compile ++= Seq("-encoding", "UTF-8",
  //                                  "-target:jvm-1.6",
  //                                  "-deprecation",
  //                                  "-unchecked"),
  javacOptions in Compile ++= Seq("-source", "1.8",
                                  "-target", "1.8",
                                  "-Xlint:unchecked",
                                  "-Xlint:deprecation",
                                  "-Xlint:-options")
)

libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"

