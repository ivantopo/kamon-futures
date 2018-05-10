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

val kamonCore             = "io.kamon"     %%   "kamon-core"              % "1.1.2"
val kamonTestkit          = "io.kamon"     %%   "kamon-testkit"           % "1.1.2"
val kanelaScalaExtension  = "io.kamon"     %%  "kanela-scala-extension"   % "0.0.10"
val kamonExecutors        = "io.kamon"     %%   "kamon-executors"         % "1.0.2"

val scalazConcurrent  = "org.scalaz"   %%   "scalaz-concurrent" % "7.2.8"

resolvers in ThisBuild += Resolver.bintrayRepo("kamon-io", "snapshots")
resolvers in ThisBuild += Resolver.mavenLocal

lazy val `kamon-futures` = (project in file("."))
  .settings(name := "kamon-futures")
  .settings(noPublishing: _*)
  .aggregate(`kamon-scala-future`, `kamon-twitter-future`, `kamon-scalaz-future`)


lazy val `kamon-twitter-future` = (project in file("kamon-twitter-future"))
  .settings(bintrayPackage := "kamon-futures")
  .enablePlugins(JavaAgent)
  .settings(javaAgents ++= resolveAgent)
  .settings(
    libraryDependencies ++=
      compileScope(kamonCore, kanelaScalaExtension, kamonExecutors) ++
      optionalScope(twitterDependency("core").value) ++
      testScope(scalatest, kamonTestkit, logbackClassic))

lazy val `kamon-scalaz-future` = (project in file("kamon-scalaz-future"))
  .settings(bintrayPackage := "kamon-futures")
  .enablePlugins(JavaAgent)
  .settings(javaAgents ++= resolveAgent)
  .settings(
    libraryDependencies ++=
      compileScope(kamonCore, kanelaScalaExtension, kamonExecutors) ++
      optionalScope(scalazConcurrent) ++
      testScope(scalatest, kamonTestkit, logbackClassic))

lazy val `kamon-scala-future` = (project in file("kamon-scala-future"))
  .settings(bintrayPackage := "kamon-futures")
  .enablePlugins(JavaAgent)
  .settings(javaAgents ++= resolveAgent)
  .settings(
    libraryDependencies ++=
      compileScope(kamonCore, kanelaScalaExtension) ++
        testScope(scalatest, kamonTestkit, logbackClassic))


def twitterDependency(moduleName: String) = Def.setting {
  scalaBinaryVersion.value match {
    case "2.10"           => "com.twitter" %% s"util-$moduleName" % "6.34.0"
    case "2.11" | "2.12"  => "com.twitter" %% s"util-$moduleName" % "6.40.0"
  }
}

def resolveAgent: Seq[ModuleID] = {
  val agent = Option(System.getProperty("agent")).getOrElse("aspectj")
  if(agent.equalsIgnoreCase("kanela"))
    Seq("org.aspectj" % "aspectjweaver" % "1.9.1" % "compile", "io.kamon" % "kanela-agent" % "0.0.300" % "compile;test")
  else
    Seq("org.aspectj" % "aspectjweaver" % "1.9.1" % "compile;test", "io.kamon" % "kanela-agent" % "0.0.11" % "compile")
}
