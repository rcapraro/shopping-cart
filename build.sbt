import Dependencies._

ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion     := "3.2.0"
ThisBuild / organization     := "com.capraro"
ThisBuild / organizationName := "RC Corp"

ThisBuild / evictionErrorLevel := Level.Warn
ThisBuild / scalafixDependencies += Libraries.organizeImports
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

resolvers ++= Resolver.sonatypeOssRepos("snapshots")

val scalafixCommonSettings = inConfig(IntegrationTest)(scalafixConfigSettings(IntegrationTest))

val scala3Version = "3.2.0"

lazy val root = (project in file("."))
  .settings(name := "shopping-cart")
  .aggregate(core, tests)

lazy val tests = (project in file("modules/tests"))
  .configs(IntegrationTest)
  .settings(
    name := "shopping-cart-test-suite",
    scalacOptions ++= List("-source:future"),
    scalaVersion := scala3Version,
    testFrameworks += new TestFramework("weaver.framework.CatsEffect"),
    Defaults.itSettings,
    scalafixCommonSettings,
    libraryDependencies ++= Seq(
      Libraries.catsLaws,
      Libraries.log4catsNoOp,
      Libraries.monocleLaw,
      Libraries.weaverCats,
      Libraries.weaverDiscipline,
      Libraries.weaverScalaCheck
    )
  )
  .dependsOn(core)

lazy val jwt = (project in file("modules/jwt"))
  .settings(
    name := "shopping-cart-jwt",
    scalacOptions ++= List("-source:future"),
    scalaVersion      := scala3Version,
    scalafmtOnCompile := true,
    resolvers ++= Resolver.sonatypeOssRepos("snapshots"),
    Defaults.itSettings,
    scalafixCommonSettings,
    libraryDependencies ++= Seq(
      Libraries.cats,
      Libraries.kittens,
      Libraries.catsEffect,
      Libraries.fs2,
      Libraries.http4sDsl,
      Libraries.http4sServer,
      Libraries.jwtCore,
      Libraries.munit % Test
    )
  )

lazy val core = (project in file("modules/core"))
  .enablePlugins(DockerPlugin)
  .enablePlugins(AshScriptPlugin)
  .settings(
    name                 := "shopping-cart-core",
    Docker / packageName := "shopping-cart",
    scalacOptions ++= List("-source:future"),
    scalaVersion      := scala3Version,
    scalafmtOnCompile := true,
    resolvers ++= Resolver.sonatypeOssRepos("snapshots"),
    Defaults.itSettings,
    dockerBaseImage := "openjdk:11-jre-slim-buster",
    dockerExposedPorts ++= Seq(8080),
    makeBatScripts     := Seq(),
    dockerUpdateLatest := true,
    libraryDependencies ++= Seq(
      Libraries.cats,
      Libraries.catsEffect,
      Libraries.catsRetry,
      Libraries.circeCore,
      Libraries.circeGeneric,
      Libraries.circeParser,
      Libraries.cirisCore,
      Libraries.fs2,
      Libraries.http4sDsl,
      Libraries.http4sServer,
      Libraries.http4sClient,
      Libraries.http4sCirce,
      Libraries.javaxCrypto,
      Libraries.log4cats,
      Libraries.logback % Runtime,
      Libraries.monocleCore,
      Libraries.redis4catsEffects,
      Libraries.redis4catsLog4cats,
      Libraries.skunkCore,
      Libraries.skunkCirce,
      Libraries.squants
    )
  )
  .dependsOn(jwt)

addCommandAlias("runLinter", ";scalafmt;scalafixAll --rules OrganizeImports")
