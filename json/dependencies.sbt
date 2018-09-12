val catsVersion = "1.3.1"

libraryDependencies ++= Seq(
  "org.json4s"                  %% "json4s-jackson"   % "3.6.0",
  "com.fasterxml.jackson.core"  %  "jackson-databind" % "2.9.6",
  "org.typelevel"               %% "cats-macros"      % catsVersion,
  "org.typelevel"               %% "cats-core"        % catsVersion
)
