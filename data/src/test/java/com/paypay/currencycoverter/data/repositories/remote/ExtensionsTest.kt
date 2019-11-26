package com.paypay.currencycoverter.data.repositories.remote

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

internal class ExtensionsTest {

    @Test
    fun asJson() {
        val serverResponse =
            javaClass.classLoader?.getResourceAsStream("rates.json")?.asJsonString()
        assertThat(serverResponse).isNotBlank()
    }
}