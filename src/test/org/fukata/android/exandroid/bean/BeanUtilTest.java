package org.fukata.android.exandroid.bean;

import org.junit.Assert;
import org.junit.Test;

public class BeanUtilTest {
	public enum Type {
		Normal("normal"), Admin("admin");

		private String type;

		private Type(String type) {
			this.type = type;
		}

		public String getType() {
			return this.type;
		}

		public static Type find(String value) {
			for (Type t : Type.values()) {
				if (t.getType().equals(value)) {
					return t;
				}
			}
			return null;
		}
		
		public static String getValue(Type t) {
			return t.getType();
		}
	}

	public static class User {
		private String name;
		private String account;
		private Type type;

		public Type getType() {
			return type;
		}

		public void setType(Type type) {
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAccount() {
			return account;
		}

		public void setAccount(String account) {
			this.account = account;
		}

		@Override
		public String toString() {
			return "User [account=" + account + ", name=" + name + ", type="
					+ type + "]";
		}

	}

	public static class RsUser {
		private String name;
		private String account;
		private String type;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAccount() {
			return account;
		}

		public void setAccount(String account) {
			this.account = account;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		@Override
		public String toString() {
			return "RsUser [account=" + account + ", name=" + name + ", type="
					+ type + "]";
		}

	}

	@Test
	public void testCopyProperties() {
		User user1 = new User();
		user1.setAccount("fukata");
		user1.setName("Tatsuya Fukata");
		user1.setType(Type.Admin);

		User user2 = new User();

		BeanUtil.copyProperties(user2, user1);

		System.out.println(user1);
		System.out.println(user2);

		Assert.assertEquals(user1.getAccount(), user2.getAccount());
		Assert.assertEquals(user1.getName(), user2.getName());
	}

	@Test
	public void testCopyProperties文字列からEnum() {
		RsUser rsUser = new RsUser();
		rsUser.setAccount("fukata");
		rsUser.setName("Tatsuya Fukata");
		rsUser.setType(Type.Admin.getType());
		
		User user1 = new User();
		
		User user2 = new User();
		user2.setAccount("fukata");
		user2.setName("Tatsuya Fukata");
		user2.setType(Type.Admin);
		
		BeanUtil.copyProperties(user1, rsUser);
		
		System.out.println(user1);
		System.out.println(rsUser);
		
		Assert.assertEquals(user1.getAccount(), user2.getAccount());
		Assert.assertEquals(user1.getName(), user2.getName());
		Assert.assertEquals(user1.getType(), user2.getType());
	}
	
	@Test
	public void testCopyPropertiesEnumから文字列() {
		User user1 = new User();
		user1.setAccount("fukata");
		user1.setName("Tatsuya Fukata");
		user1.setType(Type.Admin);
		
		RsUser rsUser = new RsUser();
		
		BeanUtil.copyProperties(rsUser, user1);
		
		System.out.println(user1);
		System.out.println(rsUser);
		
		Assert.assertEquals(rsUser.getAccount(), user1.getAccount());
		Assert.assertEquals(rsUser.getName(), user1.getName());
		Assert.assertEquals(rsUser.getType(), user1.getType().getType());
	}
}
