/* =========================================================================================
 * Copyright © 2013-2016 the kamon project <http://kamon.io/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 * =========================================================================================
 */

val kamonCore         = "io.kamon"     %%   "kamon-core"        % "1.2.0-87cf9bd2b33f153e3ba070c75f7f496005cac295"
val kamonTestkit      = "io.kamon"     %%   "kamon-testkit"     % "1.2.0-87cf9bd2b33f153e3ba070c75f7f496005cac295"
val scalazConcurrent  = "org.scalaz"   %%   "scalaz-concurrent" % "7.2.8"

resolvers in ThisBuild += Resolver.bintrayRepo("kamon-io", "snapshots")

lazy val `kamon-futures` = (project in file("."))
  .settings(name := "kamon-futures")
  .settings(noPublishing: _*)
  .aggregate(`kamon-scala-future`, `kamon-twitter-future`, `kamon-scalaz-future`)


lazy val `kamon-twitter-future` = (project in file("kamon-twitter-future"))
  .settings(bintrayPackage := "kamon-futures")
  .settings(aspectJSettings: _*)
  .settings(
    libraryDependencies ++=
      compileScope(kamonCore) ++
      providedScope(aspectJ) ++
      optionalScope(twitterDependency("core").value) ++
      testScope(scalatest, kamonTestkit, logbackClassic))

lazy val `kamon-scalaz-future` = (project in file("kamon-scalaz-future"))
  .settings(bintrayPackage := "kamon-futures")
  .settings(aspectJSettings: _*)
  .settings(
    libraryDependencies ++=
      compileScope(kamonCore) ++
      providedScope(aspectJ) ++
      optionalScope(scalazConcurrent) ++
      testScope(scalatest, kamonTestkit, logbackClassic))

lazy val `kamon-scala-future` = (project in file("kamon-scala-future"))
  .settings(bintrayPackage := "kamon-futures")
  .settings(aspectJSettings: _*)
  .settings(
    libraryDependencies ++=
      compileScope(kamonCore) ++
        providedScope(aspectJ) ++
        optionalScope(scalazConcurrent, twitterDependency("core").value) ++
        testScope(scalatest, kamonTestkit, logbackClassic))


def twitterDependency(moduleName: String) = Def.setting {
  scalaBinaryVersion.value match {
    case "2.10"           => "com.twitter" %% s"util-$moduleName" % "6.34.0"
    case "2.11" | "2.12"  => "com.twitter" %% s"util-$moduleName" % "6.40.0"
  }
}
