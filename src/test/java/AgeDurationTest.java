import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AgeDurationTest {

    @Test(description = "Check Add() transitions for positive operands", dataProvider = "getAddTransitions")
    public void testAddTransitions(AgeDuration operand1, AgeDuration operand2, AgeDuration expected, String message) {
        AgeDuration result1 = operand1.Add(operand2);
        AgeDuration result2 = operand2.Add(operand1);
        Assert.assertEquals(result1, expected, message);
        Assert.assertEquals(result2, expected, message); //Commutativity
    }

    @Test(description = "Check Add() for positive operands", dataProvider = "getAddPosVal")
    public void testAddPositiveVal(AgeDuration operand1, AgeDuration operand2, AgeDuration expected, String message) {
        AgeDuration result = operand1.Add(operand2);
        Assert.assertEquals(result, expected, message);
    }

    @Test(description = "Check Add() for negative operands", dataProvider = "getAddNegVal")
    public void testAddNegativeVal(AgeDuration operand1, AgeDuration operand2, AgeDuration expected, String message) {
        AgeDuration result = operand1.Add(operand2);
        Assert.assertEquals(result, expected, message);
    }

    @Test(description = "Check Add() Associativity")
    public void testAddAssociativity() {
        //a + b + c == a + c + b
        AgeDuration op1 = new AgeDuration(0, 11, 30, false);
        AgeDuration op2 = new AgeDuration(1, 4, 1, false);
        AgeDuration op3 = new AgeDuration(2, 1, 2, false);
        AgeDuration result = op1.Add(op2).Add(op3);
        AgeDuration result2 = op1.Add(op3).Add(op2);
        AgeDuration result3 = op3.Add(op2).Add(op1);

        Assert.assertEquals(result,  new AgeDuration(4, 5, 3, false), "0.11.30 + 1.4.1 + 2.1.2");
        Assert.assertEquals(result2, new AgeDuration(4, 5, 3, false), "0.11.30 + 2.1.2 + 1.4.1");
        Assert.assertEquals(result3, new AgeDuration(4, 5, 3, false), "2.1.2 + 1.4.1 + 0.11.30");
    }

    @Test(description = "Check Sub() transitions for positive operands", dataProvider = "getSubTransitions")
    public void testSubTransitions(AgeDuration operand1, AgeDuration operand2, AgeDuration expected, String message) {
        AgeDuration result = operand1.Sub(operand2);
        Assert.assertEquals(result, expected, message);
    }

    @Test(description = "Check Sub() for positive operands", dataProvider = "getSubPosVal")
    public void testSubPositiveVal(AgeDuration operand1, AgeDuration operand2, AgeDuration expected, String message) {
        AgeDuration result = operand1.Sub(operand2);
        Assert.assertEquals(result, expected, message);
    }

    @Test(description = "Check Sub() for negative operands", dataProvider = "getSubNegVal")
    public void testSubNegativeVal(AgeDuration operand1, AgeDuration operand2, AgeDuration expected, String message) {
        AgeDuration result = operand1.Sub(operand2);
        Assert.assertEquals(result, expected, message);
    }

    @Test(description = "Check Sub() Associativity")
    public void testSubAssociativity() {
        //a - b - c == a - c - b
        AgeDuration op1 = new AgeDuration(11, 11, 1, false);
        AgeDuration op2 = new AgeDuration(1, 4, 29, false);
        AgeDuration op3 = new AgeDuration(2, 1, 2, false);
        AgeDuration result = op1.Sub(op2).Sub(op3);
        AgeDuration result2 = op1.Sub(op3).Sub(op2);
        Assert.assertEquals(result, new AgeDuration(8, 5, 0, false), "11.11.1 - 1.4.29 - 2.1.2");
        Assert.assertEquals(result2, new AgeDuration(8, 5, 0, false), "11.11.1 - 2.1.2 - 1.4.29");
    }

    @Test(description = "Check Add() and Sub() associativity for 3rd operand", dataProvider = "getAddSubAss")
    public void testAddSubAssociativity(AgeDuration operand1, AgeDuration operand2, AgeDuration expected, String message) {
        //a + b - b == a - b + b == a
        AgeDuration result = operand1.Add(operand2).Sub(operand2);
        AgeDuration resultRev = operand1.Sub(operand2).Add(operand2);
        Assert.assertEquals(result, expected, message);
        Assert.assertEquals(resultRev, expected, message);
    }

    @Test(description = "Check unaryNegative() operation")
    public void testUnaryNegative() {
        AgeDuration age = new AgeDuration(1, 1, 1, false);
        AgeDuration result = age.unaryNegative();
        Assert.assertEquals(result, new AgeDuration(1, 1, 1, true), "-(+a) --> -a");

        AgeDuration reInverse = result.unaryNegative();
        Assert.assertEquals(reInverse, age, "-(-a) -> a");
    }

    @Test(description = "Check toString()", dataProvider = "getToStr")
    public void testToString(AgeDuration age, String expected, String message) {
        String ageStr = age.toString();
        Assert.assertEquals(ageStr, expected, message);
    }

    @Test(description = "Check equals() positive", dataProvider = "getEquals")
    public void testEquals(AgeDuration operand1, AgeDuration operand2, String message) {
        boolean result = operand1.equals(operand2);
        Assert.assertTrue(result, message);
    }

    @Test(description = "Check equals() negative", dataProvider = "getEqualsNeg")
    public void testEqualsNegative(AgeDuration operand1, AgeDuration operand2, String message) {
        boolean result = operand1.equals(operand2);
        Assert.assertFalse(result, message);
    }

    @Test(description = "Check greaterThan() operation", dataProvider = "getGreaterThan")
    public void testGreaterThan(AgeDuration operand1, AgeDuration operand2, boolean expected, String message) {
        boolean result = operand1.greaterThan(operand2);
        Assert.assertEquals(result, expected, message);
    }

    @DataProvider
    public Object[][] getAddTransitions() {
        return new Object[][]{
                {new AgeDuration(0, 0, 30, false),  new AgeDuration(0, 0, 1, false), new AgeDuration(0, 1, 1, false), "0.0.30 + 0.0.1 == 0.1.1"}, //Hm... Why?
                {new AgeDuration(0, 11, 0, false),  new AgeDuration(0, 1, 0, false), new AgeDuration(1, 0, 0, false), "0.11.0 + 0.1.0 == 1.0.0"},
                {new AgeDuration(0, 11, 29, false), new AgeDuration(0, 0, 1, false), new AgeDuration(0, 11, 30, false), "0.11.29 + 0.0.1 == 0.11.30"}, //Hm...
                {new AgeDuration(0, 11, 29, false), new AgeDuration(0, 0, 2, false), new AgeDuration(1, 0, 1, false), "0.11.29 + 0.0.2 == 1.0.1"},
                {new AgeDuration(0, 0, 29, false),  new AgeDuration(0, 0, 1, false), new AgeDuration(0, 0, 30, false), "0.0.29 + 0.0.1 == 0.0.30"},
                {new AgeDuration(0, 10, 0, false),  new AgeDuration(0, 1, 0, false), new AgeDuration(0, 11, 0, false), "0.10.0 + 0.1.0 == 0.11.0"}
        };
    }

    @DataProvider
    public Object[][] getAddPosVal() {
        return new Object[][]{
                {new AgeDuration(0, 0, 0, false), new AgeDuration(0, 0, 1, false), new AgeDuration(0, 0, 1, false), "1 day plus zero"},
                {new AgeDuration(0, 0, 0, false), new AgeDuration(0, 1, 0, false), new AgeDuration(0, 1, 0, false), "1 month plus zero"},
                {new AgeDuration(0, 0, 0, false), new AgeDuration(1, 0, 0, false), new AgeDuration(1, 0, 0, false), "1 year plus zero"},
                {new AgeDuration(3, 5, 7, false), new AgeDuration(9, 6, 5, false), new AgeDuration(12, 11, 12, false), "Adding positive operands"}
        };
    }

    @DataProvider
    public Object[][] getAddNegVal() {
        return new Object[][]{
                {new AgeDuration(10, 10, 10, false), new AgeDuration(2, 3, 5, true), new AgeDuration(8, 7, 5, false), "a + (-b)"},
                {new AgeDuration(10, 10, 10, true), new AgeDuration(2, 3, 5, false), new AgeDuration(8, 7, 5, true), "-a + b"},
                {new AgeDuration(10, 10, 10, true), new AgeDuration(2, 3, 5, true), new AgeDuration(13, 1, 15, true), "-a + (-b)"},
                {new AgeDuration(0, 0, 10, true), new AgeDuration(0, 0, 5, true), new AgeDuration(0, 0, 15, true), "-0.0.10 + (-0.0.5) == -0.0.15"},
                {new AgeDuration(0, 0, 10, true), new AgeDuration(0, 0, 5, false), new AgeDuration(0, 0, 5, true), "-0.0.10 + 0.0.5 == -0.0.5"}
        };
    }

    @DataProvider
    public Object[][] getSubTransitions() {
        return new Object[][]{
                {new AgeDuration(0, 1, 0, false), new AgeDuration(0, 0, 1, false), new AgeDuration(0, 0, 29, false), "0.1.0 - 0.0.1 == 0.0.29"},
                {new AgeDuration(1, 0, 0, false), new AgeDuration(0, 1, 0, false), new AgeDuration(0, 11, 0, false), "1.0.0 - 0.1.0 == 0.11.0"},
                {new AgeDuration(1, 0, 0, false), new AgeDuration(0, 0, 1, false), new AgeDuration(0, 11, 29, false), "1.0.0 - 0.0.1 == 0.11.29"},
                {new AgeDuration(0, 1, 1, false), new AgeDuration(0, 0, 1, false), new AgeDuration(0, 1, 0, false), "0.1.1 - 0.0.1 == 0.1.0"} //Is it OK we can't get 30d?
        };
    }

    @DataProvider
    public Object[][] getSubPosVal() {
        return new Object[][]{
                {new AgeDuration(1, 1, 1, false), new AgeDuration(1, 1, 1, false), new AgeDuration(0, 0, 0, false), "a - a == 0"},
                {new AgeDuration(1, 1, 1, false), new AgeDuration(0, 0, 0, false), new AgeDuration(1, 1, 1, false), "a - 0 == a"},
                {new AgeDuration(0, 0, 0, false), new AgeDuration(1, 1, 1, false), new AgeDuration(1, 1, 1, true),  "0 - a == -a"},

                {new AgeDuration(1, 1, 1, false), new AgeDuration(1, 1, 2, false), new AgeDuration(0, 0, 1, true),  "1.1.1 - 1.1.2 == -0.0.1"},
                {new AgeDuration(1, 1, 1, false), new AgeDuration(1, 2, 2, false), new AgeDuration(0, 1, 1, true),  "1.1.1 - 1.2.2 == -0.1.1"},
                {new AgeDuration(1, 1, 1, false), new AgeDuration(2, 2, 2, false), new AgeDuration(1, 1, 1, true),  "1.1.1 - 2.2.2 == -1.1.1"},

                {new AgeDuration(12, 11, 18, false), new AgeDuration(7, 5, 9, false), new AgeDuration(5, 6, 9, false),  "a - b, a > b"},
                {new AgeDuration(7, 5, 9, false), new AgeDuration(12, 11, 18, false), new AgeDuration(5, 6, 9, true),  "a - b, a < b"}
        };
    }

    @DataProvider
    public Object[][] getSubNegVal() {
        return new Object[][]{
                {new AgeDuration(10, 10, 10, false), new AgeDuration(2, 1, 9, true),    new AgeDuration(12, 11, 19, false), "+a - (-b) --> a + b"},
                {new AgeDuration(10, 10, 10, true),  new AgeDuration(2, 1, 9, false),   new AgeDuration(12, 11, 19, true),  "-a - (+b) --> -a - b --> -(a + b)"},
                {new AgeDuration(10, 10, 10, true),  new AgeDuration(2, 1, 9, true),    new AgeDuration(8, 9, 1, true),     "-a - (-b) --> b - a, a > b"},
                {new AgeDuration(2, 1, 9, true),     new AgeDuration(10, 10, 10, true), new AgeDuration(8, 9, 1, false),    "-a - (-b) --> b - a, a < b"},
                {new AgeDuration(2, 1, 9, true),     new AgeDuration(2, 1, 9, true),    new AgeDuration(0, 0, 0, false),    "-a - (-a) --> a - a == 0"},
                {new AgeDuration(0, 0, 10, true),    new AgeDuration(0, 0, 5, true),    new AgeDuration(0, 0, 5, true),     "-0.0.10 - (-0.0.5) == -0.0.5"},
                {new AgeDuration(0, 0, 5, true),     new AgeDuration(0, 0, 10, true),   new AgeDuration(0, 0, 5, false),    "-0.0.5 - (-0.0.10) == 0.0.5"}
        };
    }

    @DataProvider
    public Object[][] getToStr() {
        return new Object[][]{
                {new AgeDuration(0, 0, 0, false), "0 days",                       "Zero"},
                {new AgeDuration(0, 0, 1, false), "1 day",                        "Single"},
                {new AgeDuration(0, 1, 0, false), "1 month 0 days",               "Single"},
                {new AgeDuration(1, 0, 0, false), "1 year 0 months 0 days",       "Single"},
                {new AgeDuration(1, 1, 1, false), "1 year 1 month 1 day",         "All single"},
                {new AgeDuration(1, 1, 0, false), "1 year 1 month 0 days",        "All single and Zero"},
                {new AgeDuration(1, 0, 1, false), "1 year 0 months 1 day",        "All single and Zero"},
                {new AgeDuration(1, 1, 2, false), "1 year 1 month 2 days",        "Single and Plural"},
                {new AgeDuration(1, 2, 1, false), "1 year 2 months 1 day",        "Single and Plural"},
                {new AgeDuration(2, 1, 1, false), "2 years 1 month 1 day",        "Single and Plural"},
                {new AgeDuration(13, 10, 3, false), "13 years 10 months 3 days",  "Plural"},
                {new AgeDuration(0, 0, 1, true), "- 1 day",                       "Single negative"},
                {new AgeDuration(0, 1, 0, true), "- 1 month 0 days",              "Single negative"},
                {new AgeDuration(1, 0, 0, true), "- 1 year 0 months 0 days",      "Single negative"},
                {new AgeDuration(13, 10, 3, true), "- 13 years 10 months 3 days", "Plural negative"}
        };
    }

    @DataProvider
    public Object[][] getAddSubAss() {
        return new Object[][]{
                {new AgeDuration(10, 10, 10, false), new AgeDuration(7, 6, 29, false), new AgeDuration(10, 10, 10, false), "10.10.10 +(-) 7.6.29 -(+) 7.6.29 == 10.10.10"},
                {new AgeDuration(0, 0, 29, false), new AgeDuration(0, 0, 1, false), new AgeDuration(0, 0, 29, false),      "0.0.29 +(-) 0.0.1 -(+) 0.0.1 == 0.0.29"},
                {new AgeDuration(0, 0, 30, false), new AgeDuration(0, 0, 1, false), new AgeDuration(0, 0, 30, false),      "0.0.30 +(-) 0.0.1 -(+) 0.0.1 == 0.0.30"},
                {new AgeDuration(0, 11, 29, false), new AgeDuration(0, 0, 1, false), new AgeDuration(0, 11, 29, false),    "0.11.29 +(-) 0.0.1 -(+) 0.0.1 == 0.11.29"},
                {new AgeDuration(0, 11, 30, false), new AgeDuration(0, 0, 1, false), new AgeDuration(0, 11, 30, false),    "0.11.30 +(-) 0.0.1 -(+) 0.0.1 == 0.11.30"},
                {new AgeDuration(0, 10, 29, false), new AgeDuration(0, 1, 2, false), new AgeDuration(0, 10, 29, false),    "0.10.29 +(-) 0.1.2 -(+) 0.1.2 == 0.10.29"},
                {new AgeDuration(0, 10, 30, false), new AgeDuration(0, 1, 1, false), new AgeDuration(0, 10, 30, false),    "0.10.30 +(-) 0.1.1 -(+) 0.1.1 == 0.10.30"},
                {new AgeDuration(0, 11, 0, false), new AgeDuration(0, 1, 0, false), new AgeDuration(0, 11, 0, false),      "0.11.0 +(-) 0.1.0 -(+) 0.1.0 == 0.11.0"},
                {new AgeDuration(0, 0, 1, false),   new AgeDuration(0, 0, 30, false), new AgeDuration(0, 0, 1, false),     "0.0.1 +(-) 0.0.30 -(+) 0.0.30 == 0.0.1"},
                {new AgeDuration(0, 0, 1, false),   new AgeDuration(10, 10, 10, false), new AgeDuration(0, 0, 1, false),   "0.0.1 +(-) 10.10.10 -(+) 10.10.10 == 0.0.1"}
        };
    }

    @DataProvider
    public Object[][] getGreaterThan() {
        return new Object[][]{
                {new AgeDuration(0, 0, 1, false), new AgeDuration(0, 0, 0, false), true,     "0.0.1 >= 0.0.0"},
                {new AgeDuration(0, 1, 0, false), new AgeDuration(0, 0, 1, false), true,     "0.1.0 >= 0.0.1"},
                {new AgeDuration(0, 1, 0, false), new AgeDuration(0, 0, 30, false), true,    "0.1.0 >= 0.0.30"},
                {new AgeDuration(1, 0, 0, false), new AgeDuration(0, 1, 1, false), true,     "1.0.0 >= 0.1.1"},
                {new AgeDuration(1, 0, 0, false), new AgeDuration(0, 11, 29, false), true,   "1.0.0 >= 0.11.29"},
                {new AgeDuration(0, 11, 30, false), new AgeDuration(0, 11, 29, false), true, "0.11.30 >= 0.11.29"},
                {new AgeDuration(2, 11, 30, false), new AgeDuration(2, 11, 29, false), true, "2.11.30 >= 2.11.29"},
                {new AgeDuration(0, 0, 1, true), new AgeDuration(0, 0, 0, false), true,      "-0.0.1 < 0.0.0"},
                {new AgeDuration(0, 0, 1, true), new AgeDuration(0, 0, 1, false), false,     "-0.0.1 < 0.0.1"},
                {new AgeDuration(1, 0, 0, true), new AgeDuration(0, 1, 1, true), false,      "-1.0.0 < -0.1.1"},   //Broken
                {new AgeDuration(1, 0, 0, true), new AgeDuration(0, 11, 29, true), false,    "-1.0.0 < -0.11.29"}, //my
                {new AgeDuration(2, 11, 30, true), new AgeDuration(2, 11, 29, true), false,  "-2.11.30 < -2.11.29"}//heart
        };
    }

    @DataProvider
    public Object[][] getEquals() {
        return new Object[][]{
                {new AgeDuration(0, 0, 0, false), new AgeDuration(0, 0, 0, false),       "0 == 0"},
                {new AgeDuration(0, 0, 1, false), new AgeDuration(0, 0, 1, false),       "0.0.1 == 0.0.1"},
                {new AgeDuration(0, 0, 1, true), new AgeDuration(0, 0, 1, true),         "-0.0.1 == -0.0.1"},
                {new AgeDuration(0, 1, 0, false), new AgeDuration(0, 1, 0, false),       "0.1.0 == 0.1.0"},
                {new AgeDuration(1, 0, 0, false), new AgeDuration(1, 0, 0, false),       "1.0.0 == 1.0.0"},
                {new AgeDuration(25, 11, 29, false), new AgeDuration(25, 11, 29, false), "25.11.29 == 25.11.29"},
                {new AgeDuration(25, 11, 29, true), new AgeDuration(25, 11, 29, true),   "-25.11.29 == -25.11.29"}
        };
    }

    @DataProvider
    public Object[][] getEqualsNeg() {
        return new Object[][]{
                {new AgeDuration(0, 0, 1, false), new AgeDuration(0, 0, 0, false),      "0.0.1 != 0"},
                {new AgeDuration(0, 1, 0, false), new AgeDuration(0, 0, 0, false),      "0.1.0 != 0"},
                {new AgeDuration(1, 0, 0, false), new AgeDuration(0, 0, 0, false),      "1.0.0 != 0"},
                {new AgeDuration(1, 1, 0, false), new AgeDuration(0, 1, 1, false),      "1.1.0 != 0.1.1"},
                {new AgeDuration(0, 0, 1, false), new AgeDuration(0, 0, 1, true),       "0.0.1 != -0.0.1"},
                {new AgeDuration(1, 0, 0, true), new AgeDuration(0, 0, 1, true),        "-1.0.0 != -0.0.1"},
                {new AgeDuration(0, 1, 0, false), new AgeDuration(0, 0, 30, false),     "0.1.0 != 0.0.30"},
                {new AgeDuration(0, 0, 30, false), new AgeDuration(0, 0, 29, false),    "0.0.30 != 0.0.29"},
                {new AgeDuration(1, 0, 0, true), new AgeDuration(1, 0, 0, false),       "-1.0.0 != 1.0.0"},
                {new AgeDuration(25, 11, 29, false), new AgeDuration(25, 11, 29, true), "25.11.29 != -25.11.29"},
                {new AgeDuration(25, 11, 29, true), new AgeDuration(25, 11, 29, false), "-25.11.29 != 25.11.29"},
                {new AgeDuration(25, 11, 30, false), new AgeDuration(25, 11, 29, false), "25.11.30 != 25.11.29"}
        };
    }
}