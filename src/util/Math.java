/**
 * 
 */
package util;

/**
 * @author macurvello
 *
 */
public class Math {
	public static int factorial(int number) {
		int fact = 1; // this  will be the result
        for (int i = 1; i <= number; i++) {
            fact *= i;
        }
        return fact;
	}
}
