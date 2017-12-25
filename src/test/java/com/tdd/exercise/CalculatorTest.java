package com.tdd.exercise;

import exercise.Calculator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

public class CalculatorTest {
//    @Test(expected = RuntimeException.class)
//    public void whenMoreThan2NumbersAreUsedThenExceptionIsThrown() throws Exception {
//        Calculator.add("1,2,3");
//    }

    @Test
    public void when2NumbersAreUsedThenNoExceptionIsThrown() throws Exception {
        Calculator.add("1,2");
        Assert.assertTrue(true);
    }

    @Test(expected = RuntimeException.class)
    public void whenNonNumberIsUsedThenExceptionIsThrown() throws Exception {
        Calculator.add("1,X");
    }

    @Test
    public void whenEmptyStringIsUsedThenReturnValueIs0() throws Exception {
        Assert.assertEquals(0, Calculator.add(""));
    }

    @Test
    public void whenOneNumberIsUsedThenReturnValueIsThatSameNumber() throws Exception {
        Assert.assertEquals(3, Calculator.add("3"));
    }

    @Test
    public void whenTwoNumbersAreUsedThenReturnValueIsTheirSum() throws Exception {
        Assert.assertEquals(3 + 6, Calculator.add("3,6"));
    }

    @Test
    public void whenAnyNumberfNumbersIsUsedThenReturnValuesAreTheirSum() throws Exception {
        Assert.assertEquals(3 + 6 + 15 + 18 + 46 + 33, Calculator.add("3,6,15,18,46,33"));
    }

    @Test
    public final void whenNewLineIsUsedBetweenNumbersThenReturnValuesAreTheirSums() {
        Assert.assertEquals(3 + 6 + 15, Calculator.add("3,6n15"));
    }

    @Test
    public final void whenDelimiterIsSpecifiedThenItIsUsedToSeparateNumbers() {
        Assert.assertEquals(3 + 6 + 15, Calculator.add("//;n3;6;15"));
    }

    @Test(expected = RuntimeException.class)
    public final void whenNegativeNumberIsUsedThenRuntimeExceptionIsThrown() {
        Calculator.add("3,-6,15,18,46,33");
    }

    @Test
    public final void whenNegativeNumbersAreUsedThenRuntimeExceptionIsThrown() {
        RuntimeException exception = null;
        try {
            Calculator.add("3,-6,15,-18,46,33");
        } catch (RuntimeException e) {
            exception = e;
        }
        Assert.assertNotNull(exception);
        Assert.assertEquals("Negatives not allowed: [-6, -18]", exception.getMessage());
    }

    @Test
    public final void whenOneOrMoreNumbersAreGreaterThan1000IsUsedThenItIsNotIncludedInSum() {
        Assert.assertEquals(3 + 1000 + 6, Calculator.add("3,1000,1001,6,1234"));
    }


}
