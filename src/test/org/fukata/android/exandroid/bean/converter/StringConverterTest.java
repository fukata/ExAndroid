package org.fukata.android.exandroid.bean.converter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class StringConverterTest {
	
	Converter converter;
	
	@Before
	public void setup() {
		converter = new StringConverter();
	}

	public enum TestEnum {
		Value1("aaa")
		, Value2("bbb");
		
		String value;
		
		private TestEnum(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	@Test
	public void testConvertEnum() {
		Object convert = converter.convert(StringConverter.class, TestEnum.Value2);
		Assert.assertEquals(convert, TestEnum.Value2.getValue());
	}
}
