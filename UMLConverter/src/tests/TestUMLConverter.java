package tests;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import model.UMLConverter;
import resources.Resources;


/**
 * Test class for the UMLConverter.
 * 
 * @author Lhomme Lucien
 */
public class TestUMLConverter {

	@Test
	public void testConvertVisibility() {
		assertEquals("public", UMLConverter.convertVisibility('+'));
		assertEquals("private", UMLConverter.convertVisibility('-'));
		assertEquals("protected", UMLConverter.convertVisibility('#'));
	}
	
	@Test
	public void testConvertVariable() {
		assertEquals("int x", UMLConverter.convertVariable("x:int"));
		assertEquals("int x", UMLConverter.convertVariable("x : int"));
		assertEquals("int xyz", UMLConverter.convertVariable("xyz : int"));
		assertEquals("Boolean ok", UMLConverter.convertVariable("ok:Boolean"));
		assertEquals("Boolean ok", UMLConverter.convertVariable("ok : Boolean"));
		assertEquals("Integer index", UMLConverter.convertVariable("index:Integer"));
		assertEquals("Integer index", UMLConverter.convertVariable("index : Integer"));
	}
	
	@Test
	public void testConvertFunction() {
		assertEquals("f()", UMLConverter.convertFunction("f()"));
		assertEquals("f()", UMLConverter.convertFunction("f():int"));
		assertEquals("f()", UMLConverter.convertFunction("f() : int"));
		assertEquals("f()", UMLConverter.convertFunction("f():Boolean"));
		assertEquals("f()", UMLConverter.convertFunction("f() : Boolean"));
		assertEquals("function()", UMLConverter.convertFunction("function()"));
		assertEquals("function()", UMLConverter.convertFunction("function():Integer"));
		assertEquals("function()", UMLConverter.convertFunction("function() : Integer"));
	}
	
	@Test
	public void testConvertUML() {
		assertEquals(Resources.chargeStringFile(new File("bin/resources/output.txt")), UMLConverter.convertUML(Resources.chargeStringFile(new File("bin/resources/input.txt")).split("\n")));
	}
	
}
