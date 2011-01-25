package org.fukata.android.exandroid.bean;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;


public class ReflectionUtilTest {
	
	static class User {
		public static int ID = 12345;
		
		public String name;
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getName() {
			return this.name;
		}
		
		public String getHello(String name) {
			return "Hello, " + name;
		}
		
		public String getHello() {
			return "Hello, " + name;
		}
		
		public static String getHelloStatic(String name) {
			return "Hello, " + name;
		}
		
		public static String getHelloStatic() {
			return "Hello, getHelloStatic";
		}
	}
	
	@Test
	public void testNewInstance() {
		
		try {
			User newInstance = ReflectionUtil.newInstance(User.class);
			Assert.assertTrue(newInstance!=null);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		
	}
	
	@Test
	public void testInvokeMethod引数あり() {
		try {
			User user = new User();
			Object invokeMethod = ReflectionUtil.invokeMethod(user, "getHello", new Class[]{String.class}, new Object[]{"Tatsuya"});
			Assert.assertTrue(invokeMethod.toString(), true);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testInvokeMethod引数なし() {
		
		try {
			User user = new User();
			Object invokeMethod = ReflectionUtil.invokeMethod(user, "getHello");
			Assert.assertTrue(invokeMethod.toString(), true);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testInvokeMethod引数ありクラスメソッド() {
		try {
			Object invokeMethod = ReflectionUtil.invokeMethod(User.class, "getHelloStatic", new Class[]{String.class}, new Object[]{"Tatsuya"});
			Assert.assertTrue(invokeMethod.toString(), true);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testInvokeMethod引数なしクラスメソッド() {
		try {
			Object invokeMethod = ReflectionUtil.invokeMethod(User.class, "getHelloStatic");
			Assert.assertTrue(invokeMethod.toString(), true);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetSimpleProperty() {
		User user = new User();
		user.name = "Tatsuya";
		try {
			Object property = ReflectionUtil.getSimpleProperty(user, "name");
			Assert.assertEquals(user.name, property);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetSimplePropertyスタティック() {
		try {
			Object property = ReflectionUtil.getSimpleProperty(User.class, "ID");
			Assert.assertEquals(User.ID, property);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetReadbleMethod() {
		Method method = ReflectionUtil.getReadbleMethod(User.class, "name");
		if (method!=null && "getName".equals(method.getName())) {
			Assert.assertTrue(true);
		} else {
			Assert.fail("method is null.");
		}
	}
	
	@Test
	public void testGetWriteableMethod() {
		Method method = ReflectionUtil.getWriteableMethod(User.class, "name");
		if (method!=null && "setName".equals(method.getName())) {
			Assert.assertTrue(true);
		} else {
			Assert.fail("method is null.");
		}
	}
	
}
