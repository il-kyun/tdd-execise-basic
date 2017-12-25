https://technologyconversations.com/2013/12/20/test-driven-development-tdd-example-walkthrough/
Let’s start!

Requirements
Create a simple String calculator with a method int Add(string numbers)
The method can take 0, 1 or 2 numbers, and will return their sum (for an empty string it will return 0) for example “” or “1” or “1,2”
Allow the Add method to handle an unknown amount of numbers
Allow the Add method to handle new lines between numbers (instead of commas).
The following input is ok: “1\n2,3” (will equal 6)
Support different delimiters
To change a delimiter, the beginning of the string will contain a separate line that looks like this: “//[delimiter]\n[numbers…]” for example “//;\n1;2” should return three where the default delimiter is ‘;’ .
The first line is optional. All existing scenarios should still be supported
Calling Add with a negative number will throw an exception “negatives not allowed” – and the negative that was passed. If there are multiple negatives, show all of them in the exception message stop here if you are a beginner.
Numbers bigger than 1000 should be ignored, so adding 2 + 1001 = 2
Delimiters can be of any length with the following format: “//[delimiter]\n” for example: “//[—]\n1—2—3” should return 6
Allow multiple delimiters like this: “//[delim1][delim2]\n” for example “//[-][%]\n1-2%3” should return 6.
Make sure you can also handle multiple delimiters with length longer than one char
Even though this is a very simple program, just looking at those requirements can be overwhelming. Let’s take a different approach. Forget what you just read and let us go through the requirements one by one.

Create a simple String calculator
Requirement 1: The method can take 0, 1 or 2 numbers separated by comma (,).
Let’s write our first set of tests.

[JAVA TEST]

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
package com.wordpress.technologyconversations.tddtest;
 
import org.junit.Test;
import com.wordpress.technologyconversations.tdd.StringCalculator;
 
public class StringCalculatorTest {
    @Test(expected = RuntimeException.class)
    public final void whenMoreThan2NumbersAreUsedThenExceptionIsThrown() {
        StringCalculator.add("1,2,3");
    }
    @Test
    public final void when2NumbersAreUsedThenNoExceptionIsThrown() {
        StringCalculator.add("1,2");
        Assert.assertTrue(true);
    }
    @Test(expected = RuntimeException.class)
    public final void whenNonNumberIsUsedThenExceptionIsThrown() {
        StringCalculator.add("1,X");
    }
}
It’s a good practice to name test methods in a way that it is easy to understand what is being tested. I prefer a variation of BDD with When [ACTION] Then [VERIFICATION]. In this case the name of one of the test methods is whenMoreThan2NumbersAreUsedThenExceptionIsThrown. Our first set of tests verifies that up to two numbers can be passed to the calculator’s add method. If there’s more than two or if one of them is not a number, exception should be thrown. Putting “expected” inside the @Test annotation tells the JUnit runner that the expected outcome is to throw the specified exception. From here on, for brevity reasons, only modified parts of the code will be displayed. Whole code divided into requirements can be obtained from the GitHub repository (tests and implementation).

[JAVA IMPLEMENTATION]

1
2
3
4
5
6
7
8
9
10
11
12
public class StringCalculator {
    public static final void add(final String numbers) {
        String[] numbersArray = numbers.split(",");
        if (numbersArray.length > 2) {
            throw new RuntimeException("Up to 2 numbers separated by comma (,) are allowed");
        } else {
            for (String number : numbersArray) {
                Integer.parseInt(number); // If it is not a number, parseInt will throw an exception
            }
        }
    }
}
Keep in mind that the idea behind TDD is to do the necessary minimum to make the tests pass and repeat the process until the whole functionality is implemented. At this moment we’re only interested in making sure that “the method can take 0, 1 or 2 numbers”. Run all the tests again and see them pass.

Requirement 2: For an empty string the method will return 0
[JAVA TEST]

1
2
3
4
@Test
public final void whenEmptyStringIsUsedThenReturnValueIs0() {
    Assert.assertEquals(0, StringCalculator.add(""));
}
[JAVA IMPLEMENTATION]

1
2
3
4
5
6
7
8
9
10
11
12
13
public static final int add(final String numbers) { // Changed void to int
    String[] numbersArray = numbers.split(",");
    if (numbersArray.length > 2) {
        throw new RuntimeException("Up to 2 numbers separated by comma (,) are allowed");
    } else {
        for (String number : numbersArray) {
            if (!number.isEmpty()) {
                Integer.parseInt(number);
            }
        }
    }
    return 0; // Added return
}
All there was to do to make this test pass was to change the return method from void to int and end it with returning zero.

Requirement 3: Method will return their sum of numbers
[JAVA TEST]

1
2
3
4
5
6
7
8
9
@Test
public final void whenOneNumberIsUsedThenReturnValueIsThatSameNumber() {
    Assert.assertEquals(3, StringCalculator.add("3"));
}
 
@Test
public final void whenTwoNumbersAreUsedThenReturnValueIsTheirSum() {
    Assert.assertEquals(3+6, StringCalculator.add("3,6"));
}
[JAVA IMPLEMENTATION]

1
2
3
4
5
6
7
8
9
10
11
12
13
public static int add(final String numbers) {
    int returnValue = 0;
    String[] numbersArray = numbers.split(",");
    if (numbersArray.length > 2) {
        throw new RuntimeException("Up to 2 numbers separated by comma (,) are allowed");
    }
    for (String number : numbersArray) {
        if (!number.trim().isEmpty()) { // After refactoring
            returnValue += Integer.parseInt(number);
        }
    }
    return returnValue;
}
Here we added iteration through all numbers to create a sum.

Requirement 4: Allow the Add method to handle an unknown amount of numbers
[JAVA TEST]

1
2
3
4
5
6
7
8
//  @Test(expected = RuntimeException.class)
//  public final void whenMoreThan2NumbersAreUsedThenExceptionIsThrown() {
//      StringCalculator.add("1,2,3");
//  }
    @Test
    public final void whenAnyNumberOfNumbersIsUsedThenReturnValuesAreTheirSums() {
        Assert.assertEquals(3+6+15+18+46+33, StringCalculator.add("3,6,15,18,46,33"));
    }
[JAVA IMPLEMENTATION]

1
2
3
4
5
6
7
8
9
10
11
12
13
14
public static int add(final String numbers) {
    int returnValue = 0;
    String[] numbersArray = numbers.split(",");
    // Removed after exception
    // if (numbersArray.length > 2) {
    // throw new RuntimeException("Up to 2 numbers separated by comma (,) are allowed");
    // }
    for (String number : numbersArray) {
        if (!number.trim().isEmpty()) { // After refactoring
            returnValue += Integer.parseInt(number);
        }
    }
    return returnValue;
}
All we had to do to accomplish this requirement was to remove part of the code that throws an exception if there are more than 2 numbers. However, once tests are executed, the first test failed. In order to fulfill this requirement, the test whenMoreThan2NumbersAreUsedThenExceptionIsThrown needed to be removed.

Requirement 5: Allow the Add method to handle new lines between numbers (instead of commas).
[JAVA TEST]

1
2
3
4
@Test
public final void whenNewLineIsUsedBetweenNumbersThenReturnValuesAreTheirSums() {
    Assert.assertEquals(3+6+15, StringCalculator.add("3,6n15"));
}
[JAVA IMPLEMENTATION]

1
2
3
4
5
6
7
8
9
10
public static int add(final String numbers) {
    int returnValue = 0;
    String[] numbersArray = numbers.split(",|n"); // Added |n to the split regex
    for (String number : numbersArray) {
        if (!number.trim().isEmpty()) {
            returnValue += Integer.parseInt(number.trim());
        }
    }
    return returnValue;
}
All we had to do to was to extend the split regex by adding |\n.

Requirement 6: Support different delimiters
To change a delimiter, the beginning of the string will contain a separate line that looks like this: “//[delimiter]\n[numbers…]” for example “//;\n1;2” should take 1 and 2 as parameters and return 3 where the default delimiter is ‘;’ .

[JAVA TEST]

1
2
3
4
@Test
public final void whenDelimiterIsSpecifiedThenItIsUsedToSeparateNumbers() {
    Assert.assertEquals(3+6+15, StringCalculator.add("//;n3;6;15"));
}
[JAVA IMPLEMENTATION]

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
public static int add(final String numbers) {
    String delimiter = ",|n";
    String numbersWithoutDelimiter = numbers;
    if (numbers.startsWith("//")) {
        int delimiterIndex = numbers.indexOf("//") + 2;
        delimiter = numbers.substring(delimiterIndex, delimiterIndex + 1);
        numbersWithoutDelimiter = numbers.substring(numbers.indexOf("n") + 1);
    }
    return add(numbersWithoutDelimiter, delimiter);
}
 
private static int add(final String numbers, final String delimiter) {
    int returnValue = 0;
    String[] numbersArray = numbers.split(delimiter);
    for (String number : numbersArray) {
        if (!number.trim().isEmpty()) {
            returnValue += Integer.parseInt(number.trim());
        }
    }
    return returnValue;
}
This time there was quite a lot of refactoring. We split the code into 2 methods. Initial method parses the input looking for the delimiter and later on calls the new one that does the actual sum. Since we already have tests that cover all existing functionality, it was safe to do the refactoring. If anything went wrong, one of the tests would find the problem.

Requirement 7: Negative numbers will throw an exception
Calling Add with a negative number will throw an exception “negatives not allowed” – and the negative that was passed. If there are multiple negatives, show all of them in the exception message.

[JAVA TEST]

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
@Test(expected = RuntimeException.class)
public final void whenNegativeNumberIsUsedThenRuntimeExceptionIsThrown() {
    StringCalculator.add("3,-6,15,18,46,33");
}
@Test
public final void whenNegativeNumbersAreUsedThenRuntimeExceptionIsThrown() {
    RuntimeException exception = null;
    try {
        StringCalculator.add("3,-6,15,-18,46,33");
    } catch (RuntimeException e) {
        exception = e;
    }
    Assert.assertNotNull(exception);
    Assert.assertEquals("Negatives not allowed: [-6, -18]", exception.getMessage());
}
There are two new tests. First one checks whether exception is thrown when there are negative numbers. The second one verifies whether the exception message is correct.

[JAVA IMPLEMENTATION]

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
private static int add(final String numbers, final String delimiter) {
    int returnValue = 0;
    String[] numbersArray = numbers.split(delimiter);
    List negativeNumbers = new ArrayList();
    for (String number : numbersArray) {
        if (!number.trim().isEmpty()) {
            int numberInt = Integer.parseInt(number.trim());
            if (numberInt < 0) {
                negativeNumbers.add(numberInt);
            }
            returnValue += numberInt;
        }
    }
    if (negativeNumbers.size() > 0) {
        throw new RuntimeException("Negatives not allowed: " + negativeNumbers.toString());
    }
    return returnValue;     
}
This time code was added that collects negative numbers in a List and throws an exception if there was any.

Requirement 8: Numbers bigger than 1000 should be ignored
Example: adding 2 + 1001 = 2

[JAVA TEST]

1
2
3
4
@Test
public final void whenOneOrMoreNumbersAreGreaterThan1000IsUsedThenItIsNotIncludedInSum() {
    Assert.assertEquals(3+1000+6, StringCalculator8.add("3,1000,1001,6,1234"));
}
[JAVA IMPLEMENTATION]

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
private static int add(final String numbers, final String delimiter) {
        int returnValue = 0;
        String[] numbersArray = numbers.split(delimiter);
        List negativeNumbers = new ArrayList();
        for (String number : numbersArray) {
                if (!number.trim().isEmpty()) {
                        int numberInt = Integer.parseInt(number.trim());
                        if (numberInt < 0) {
                                negativeNumbers.add(numberInt);
                        } else if (numberInt <= 1000) {
                                returnValue += numberInt;
                        }
                }
        }
        if (negativeNumbers.size() > 0) {
                throw new RuntimeException("Negatives not allowed: " + negativeNumbers.toString());
        }
        return returnValue;                
}
This one was simple. We moved “returnValue += numberInt;” inside an “else if (numberInt <= 1000)”.

There are 3 more requirements left. I encourage you to try them by yourself.

Requirement 9: Delimiters can be of any length
Following format should be used: “//[delimiter]\n”. Example: “//[—]\n1—2—3” should return 6

Requirement 10: Allow multiple delimiters
Following format should be used: “//[delim1][delim2]\n”. Example “//[-][%]\n1-2%3” should return 6.

Requirement 11: Make sure you can also handle multiple delimiters with length longer than one char