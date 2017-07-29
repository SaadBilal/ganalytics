package com.github.programmerr47.ganalytics.core

import org.junit.Test
import kotlin.reflect.KClass

class GlobalSettingsTest : AnalyticsWrapperTest {
    override val testProvider: TestEventProvider = TestEventProvider()
    override val wrapper = testSingleWrapper()

    @Test
    fun checkCuttingOffAnalyticsPrefix() {
        arrayOf(
                GanalyticsSettings { cutOffAnalyticsClassPrefix = false } to "analyticsinterface",
                GanalyticsSettings { cutOffAnalyticsClassPrefix = true } to "interface",
                GanalyticsSettings() to "interface")
                .forEach {
                    run(it.first, AnalyticsInterface::class) {
                        assertEquals(Event(it.second, "method1")) { method1() }
                        assertEquals(Event(it.second, "method2")) { method2() }
                    }
                }
    }

    private inline fun <T : Any> run(settings: GanalyticsSettings, clazz: KClass<T>, block: T.() -> Unit) =
            run(testSingleWrapper(settings), clazz, block)

    private fun testSingleWrapper(settings: GanalyticsSettings = GanalyticsSettings()) =
            AnalyticsSingleWrapper(testProvider, settings)

    private inline fun <T : Any> run(wrapper: AnalyticsWrapper, clazz: KClass<T>, block: T.() -> Unit) =
            wrapper.create(clazz).run(block)
}