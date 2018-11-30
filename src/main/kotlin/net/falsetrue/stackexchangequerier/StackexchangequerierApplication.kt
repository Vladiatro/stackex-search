package net.falsetrue.stackexchangequerier

import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean


@SpringBootApplication
class StackexchangequerierApplication {
    @Bean
    fun httpClient(): CloseableHttpClient {
        return HttpClients.createDefault()
    }
}

fun main(args: Array<String>) {
    runApplication<StackexchangequerierApplication>(*args)
}
