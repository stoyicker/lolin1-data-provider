package org.lolin1.models.champion.spells;

import java.lang.reflect.Field;
import java.util.HashMap;

public class PassiveSpell {

	@SuppressWarnings("unused")
	// Used through reflection
	private String imageName, detail, name;

	@SuppressWarnings("unchecked")
	public PassiveSpell(HashMap<String, Object> passiveDescriptor) {
		this((String) passiveDescriptor.get("name"), (String) passiveDescriptor
				.get("description"),
				((HashMap<String, String>) passiveDescriptor.get("image"))
						.get("full"));
	}

	protected PassiveSpell(String _name, String _detail, String _imageName) {
		this.name = _name;
		this.detail = _detail;
		this.imageName = _imageName;
	}

	@Override
	public String toString() {
		System.out.println(this.name);
		System.out.println(this.detail);
		System.out.println(this.imageName);

		StringBuilder ret = new StringBuilder("{");
		Field[] fields = this.getClass().getDeclaredFields();
		System.out.println(fields.length);
		for (int i = 0; i < fields.length;) {
			Field x = fields[i];
			x.setAccessible(Boolean.TRUE);
			try {
				ret.append("\"" + x.getName() + "\":\"" + x.get(this) + "\"");
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace(System.err);
			}
			x.setAccessible(Boolean.FALSE);
			if (++i < fields.length) {
				ret.append(",");
			}
		}
		ret.append("}");
		return ret.toString();
	}
}
