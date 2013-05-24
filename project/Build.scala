import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "products"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
      "net.sf.barcode4j" % "barcode4j" % "2.0",
      "postgresql" % "postgresql" % "9.1-901.jdbc4"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      // Add your own project settings here      
    )

}
