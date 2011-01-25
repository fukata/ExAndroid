package org.fukata.android.exandroid.bean.converter;

import java.sql.Timestamp;
import java.util.Date;

import org.fukata.android.exandroid.bean.ReflectionUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class DateConverterTest {
	Converter converter;
	
	@Before
	public void setup() {
		converter = new DateConverter();
	}
	
	@Test
	public void testConvert引数Timestamp(){
		Date expected = new Date(2000, 0, 1, 0, 0);
		Object actual = converter.convert(Timestamp.class, new Timestamp(2000, 0, 1, 0, 0, 0, 0));
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testConvert引数JavaSqlDate(){
		Date expected = new Date(2000, 0, 1, 0, 0);
		Object actual = converter.convert(java.sql.Date.class, new java.sql.Date(2000, 0, 1));
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testConvert引数Long(){
		Date expected = new Date(2000, 0, 1, 0, 0);
		Object actual = converter.convert(Long.class, Long.valueOf(expected.getTime()));
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testConvertTimestamp() {
		try {
			Date expected = new Date(2000, 0, 1, 0, 0);
			Object actual = ReflectionUtil.invokeDeclaredMethod(converter, "convertTimestamp", new Class[]{Class.class, Object.class}, new Object[]{Timestamp.class, new Timestamp(2000, 0, 1, 0, 0, 0, 0)});
			Assert.assertEquals(expected, actual);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
	@Test
	public void testConvertJavaSqlDate() {
		try {
			Date expected = new Date(2000, 0, 1, 0, 0);
			Object actual = ReflectionUtil.invokeDeclaredMethod(converter, "convertJavaSqlDate", new Class[]{Class.class, Object.class}, new Object[]{java.sql.Date.class, new java.sql.Date(2000, 0, 1)});
			Assert.assertEquals(expected, actual);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
	@Test
	public void testConvertLong() {
		try {
			Date expected = new Date(2000, 0, 1, 0, 0);
			Object actual = ReflectionUtil.invokeDeclaredMethod(converter, "convertLong", new Class[]{Class.class, Object.class}, new Object[]{Long.class, Long.valueOf(expected.getTime())});
			Assert.assertEquals(expected, actual);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
}
