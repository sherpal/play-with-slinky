import sbt._
import sbt.Def.settings
import sbt.Keys.{baseDirectory, libraryDependencies}
import play.sbt.PlayImport.{PlayKeys, guice}

object BackendSettings {

  def apply(): Seq[Def.Setting[_]] = settings(
    libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test,

    // https://www.playframework.com/documentation/2.7.x/SBTCookbook
    //PlayKeys.playRunHooks += baseDirectory.map(FrontendHook.apply).value

  )

}
