package shop.config

import ciris.ConfigDecoder

enum AppEnvironment {
  case Test extends AppEnvironment
  case Prod extends AppEnvironment
}

object AppEnvironment {
  given appEnvironmentConfigDecoder: ConfigDecoder[String, AppEnvironment] =
    ConfigDecoder[String].map {
      case "Test" => Test
      case "Prod" => Prod
    }
}
