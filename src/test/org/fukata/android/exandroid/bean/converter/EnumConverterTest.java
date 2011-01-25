package org.fukata.android.exandroid.bean.converter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class EnumConverterTest {
	Converter converter;
	
	@Before
	public void setup(){
		converter = new EnumConverter();
	}

	public enum TestEnum {
		Value1("aaa")
		, Value2("bbb");
		
		String value;
		
		private TestEnum(String value) {
			this.value = value;
		}
		
		public static TestEnum find(String value) {
			for (TestEnum e : values()) {
				if (e.value.equals(value)) {
					return e;
				}
			}
			
			return null;
		}
	}
	
	@Test
	public void testConvert() {
		Object convert = converter.convert(TestEnum.class, "aaa");
		Assert.assertEquals(convert, TestEnum.Value1);
	}
}
