val gatlingVersion = "3.15.1"


configurations {
  "gatlingImplementation" {
    exclude(group = "org.slf4j", module = "slf4j-log4j12")
  }
}


dependencies {
  "gatlingImplementation"("io.gatling.highcharts:gatling-charts-highcharts:$gatlingVersion")
  "gatlingImplementation"("io.gatling:gatling-core:$gatlingVersion")
}