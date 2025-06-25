package com.kutalev.auction.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MathUtilsTest {
    @Test
    fun `add should return sum of two numbers`() {
            println("Тест add выполняется!")
        assertEquals(5, MathUtils.add(2, 3))
        assertEquals(0, MathUtils.add(-2, 2))
        assertEquals(-5, MathUtils.add(-2, -3))
    }

    @Test
    fun `multiply should return product of two numbers`() {
        assertEquals(6, MathUtils.multiply(2, 3))
        assertEquals(-4, MathUtils.multiply(-2, 2))
        assertEquals(6, MathUtils.multiply(-2, -3))
        assertEquals(0, MathUtils.multiply(0, 5))
    }
} 