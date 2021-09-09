package model;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Model class which takes care of the conversion of the UML.
 * 
 * @author Lhomme Lucien
 */
public abstract class UMLConverter {

	private final static String type = "( ?: ?\\w+)";
	private final static String variable = "( ?\\w+"+type+")";
	private final static String parameter = "("+variable+"(, ?"+variable+")*)?";
	private final static Pattern patternVariable = Pattern.compile("(\\+|-|#)"+variable, Pattern.CASE_INSENSITIVE);
	private final static Pattern patternFunction = Pattern.compile("(\\+|-|#) ?\\w+\\("+parameter+"\\)"+type+"?", Pattern.CASE_INSENSITIVE);
	
	public static String convertUML(String[] lines) {
		StringBuffer sb = new StringBuffer(String.format("public class %s {\n\n", lines[0]));
		for (String string : lines) {
		    if (patternVariable.matcher(string).find()) {
		    	sb.append(String.format("\t%s %s;\n", convertVisibility(string.charAt(0)), convertVariable(string.substring(2))));
		    } else {
				if (patternFunction.matcher(string).find()) {
					sb.append(String.format("\n\t%s%s {\n\t}\n", convertVisibility(string.charAt(0)), convertFunction(string.substring(1))));
			    }
		    }
		}
		return sb.append("\n}\n").toString();
	}
	
	public static String convertFunction(String function) {
		int idx = function.indexOf('(');
		String res = "";
		Pattern patternTest = Pattern.compile(variable, Pattern.CASE_INSENSITIVE);
		Matcher matcherTest = patternTest.matcher(function.substring(idx));
		String[] tmp = matcherTest.results().map(MatchResult::group).toArray(String[]::new);
		for (int i = 0; i < tmp.length; i++) {
			res += convertVariable(tmp[i]) + (i+1<tmp.length?",":"");
		}
		return String.format("%s%s)", function.substring(0, idx+1), res);
	}
	
	public static String convertVariable(String string) {
		int idx = string.indexOf(':');
		String name = string.substring(0, idx);
		//if (name.charAt(0) == ' ')
		//	return String.format("%s%s", string.substring(idx+1), name);
		return String.format("%s %s", string.substring(idx+1).replaceAll("^ | $", ""), name.replaceAll("^ | $", ""));
	}
	
	public static String convertVisibility(char c) {
		switch (c) {
		case '-':
			return "private";
		case '#':
			return "protected";
		default:
			return "public";
		}
	}
}
