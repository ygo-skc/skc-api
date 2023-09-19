package com.rtomyj.skc.config

import org.eclipse.jetty.server.HttpConnectionFactory
import org.eclipse.jetty.server.SecureRequestCustomizer
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

/**
 * Disabling SSL SNI check for Jetty
 * Will allow calls using localhost or host name not found in SSL cert (when using an SSL cert / keystore)
 * Needed for health check calls to work from AWS
 */
@Profile("docker-remote", "docker-local")
@Configuration
class DisableJettySNICheck {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @Bean
    fun disableSniHostCheck(): WebServerFactoryCustomizer<JettyServletWebServerFactory> {
        log.info("Disabling SSL SNI check for Jetty")

        return WebServerFactoryCustomizer { factory: JettyServletWebServerFactory ->
            factory.addServerCustomizers(JettyServerCustomizer { server: Server ->
                for (connector in server.getConnectors()) {
                    if (connector is ServerConnector) {
                        disableSNI(connector)
                    }
                }
            })
        }
    }

    private fun disableSNI(connector: ServerConnector) {
        val connectionFactory = connector.getConnectionFactory(HttpConnectionFactory::class.java)
        if (connectionFactory != null) {
            val secureRequestCustomizer =
                connectionFactory.httpConfiguration.getCustomizer(SecureRequestCustomizer::class.java)
            if (secureRequestCustomizer != null) {
                secureRequestCustomizer.isSniHostCheck = false
            }
        }
    }
}