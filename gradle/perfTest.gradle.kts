val perfTestImplementation by configurations.getting {
//	extendsFrom(configurations.implementation.get())
}


configurations {

    "perfTestImplementation" {
        exclude(group = "org.slf4j", module = "slf4j-log4j12")
    }

}


dependencies {

    "perfTestImplementation"("org.scala-lang:scala-library:2.13.4")
    "perfTestImplementation"("io.gatling.highcharts:gatling-charts-highcharts:3.5.0")
    "perfTestImplementation"("io.gatling:gatling-core:3.5.0")

}