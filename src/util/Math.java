/**
 * 
 */
package util;

import java.util.HashSet;
import java.util.Set;

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

	//Produces a list of all the permutations of the characters in a given string
	public static Set<String> permutate(String chars, int pointer) {
		Set<String> result = new HashSet<String>();
		if (pointer == chars.length()) {
	        //stop-condition: returns list with only the input string
			result.add(chars);
	        return result;
	    }
	    for (int i = pointer; i < chars.length(); i++) {
	    	char[] permutation = chars.toCharArray();
	        permutation[pointer] = chars.charAt(i);
	        permutation[i] = chars.charAt(pointer);
	        result.addAll(util.Math.permutate(new String(permutation), pointer + 1));
	    }
	    return result;
	}

}
