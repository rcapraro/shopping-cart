import sbt._

object Dependencies {

  object V {
    val cats        = "2.8.0"
    val kittens     = "3.0.0"
    val catsEffect  = "3.3.14"
    val catsRetry   = "3.1.0"
    val circe       = "0.14.2"
    val ciris       = "2.4.0"
    val javaxCrypto = "1.0.1"
    val fs2         = "3.3.0"
    val http4s      = "0.23.16"
    val jwt         = "9.1.1"
    val log4cats    = "2.5.0"
    val monocle     = "3.1.0"
    val redis4cats  = "1.2.0"
    val skunk       = "0.3.2"
    val squants     = "1.8.3"

    val logback = "1.4.3"

    val organizeImports = "0.6.0"

    val weaver = "0.8.0"
    val munit  = "0.7.29"
  }

  object Libraries {
    def circe(artifact: String): ModuleID = "io.circe" %% s"circe-$artifact" % V.circe

    def ciris(artifact: String): ModuleID = "is.cir" %% artifact % V.ciris

    def http4s(artifact: String): ModuleID = "org.http4s" %% s"http4s-$artifact" % V.http4s

    val cats       = "org.typelevel"    %% "cats-core"   % V.cats
    val kittens    = "org.typelevel"    %% "kittens"     % V.kittens
    val catsEffect = "org.typelevel"    %% "cats-effect" % V.catsEffect
    val catsRetry  = "com.github.cb372" %% "cats-retry"  % V.catsRetry
    val squants    = "org.typelevel"    %% "squants"     % V.squants
    val fs2        = "co.fs2"           %% "fs2-core"    % V.fs2

    val circeCore    = circe("core")
    val circeGeneric = circe("generic")
    val circeParser  = circe("parser")

    val cirisCore = ciris("ciris")

    val http4sDsl    = http4s("dsl")
    val http4sServer = http4s("ember-server")
    val http4sClient = http4s("ember-client")
    val http4sCirce  = http4s("circe")

    val jwtCore = "com.github.jwt-scala" %% "jwt-core" % V.jwt

    val monocleCore = "dev.optics" %% "monocle-core" % V.monocle

    val log4cats = "org.typelevel" %% "log4cats-slf4j" % V.log4cats

    val javaxCrypto = "javax.xml.crypto" % "jsr105-api" % V.javaxCrypto

    val redis4catsEffects  = "dev.profunktor" %% "redis4cats-effects"  % V.redis4cats
    val redis4catsLog4cats = "dev.profunktor" %% "redis4cats-log4cats" % V.redis4cats

    val skunkCore  = "org.tpolecat" %% "skunk-core"  % V.skunk
    val skunkCirce = "org.tpolecat" %% "skunk-circe" % V.skunk

    // Runtime
    val logback = "ch.qos.logback" % "logback-classic" % V.logback

    // Test
    val catsLaws         = "org.typelevel"       %% "cats-laws"         % V.cats
    val log4catsNoOp     = "org.typelevel"       %% "log4cats-noop"     % V.log4cats
    val monocleLaw       = "dev.optics"          %% "monocle-law"       % V.monocle
    val weaverCats       = "com.disneystreaming" %% "weaver-cats"       % V.weaver
    val weaverDiscipline = "com.disneystreaming" %% "weaver-discipline" % V.weaver
    val weaverScalaCheck = "com.disneystreaming" %% "weaver-scalacheck" % V.weaver
    val munit            = "org.scalameta"       %% "munit"             % V.munit

    // Scalafix rules
    val organizeImports = "com.github.liancheng" %% "organize-imports" % V.organizeImports
  }
}
