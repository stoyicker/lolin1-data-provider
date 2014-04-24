package org.jorge.lolin1.models.champion.spells;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This file is part of lolin1-data-provider.
 * <p/>
 * lolin1-data-provider is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * lolin1-data-provider is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with lolin1-data-provider.  If not, see <http://www.gnu.org/licenses/>.
 */
public class PassiveSpell {

    @SuppressWarnings("unused")
    // Used through reflection
    private String imageName, detail, name;

    @SuppressWarnings("unchecked")
    public PassiveSpell(HashMap<String, Object> passiveDescriptor) {
        this(passiveDescriptor.get("name").toString(), passiveDescriptor.get(
                        "description").toString(),
                ((HashMap<String, String>) passiveDescriptor.get("image"))
                        .get("full")
        );
    }

    protected PassiveSpell(String _name, String _detail, String _imageName) {
        this.name = _name;
        if (!(this instanceof ActiveSpell)) {
            String newOne = _detail.replaceAll("=\"", "=\\\\\"").replaceAll(
                    "\">", "\\\\\">");
            this.detail = newOne;
        } else {
            this.detail = _detail;
        }
        this.imageName = _imageName;
    }

    public String getImageName() {
        return this.imageName;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder("{");
        ArrayList<Field> fields = new ArrayList<>(Arrays.asList(this.getClass()
                .getDeclaredFields()));
        if (this.getClass().getSuperclass() != null) {
            fields.addAll(Arrays.asList(this.getClass().getSuperclass()
                    .getDeclaredFields()));
        }
        for (int i = 0; i < fields.size(); ) {
            Field x = fields.get(i);
            x.setAccessible(Boolean.TRUE);
            try {
                ret.append("\"" + x.getName() + "\":\"" + x.get(this) + "\"");
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace(System.err);
            }
            x.setAccessible(Boolean.FALSE);
            if (++i < fields.size()) {
                ret.append(",");
            }
        }
        ret.append("}");
        return ret.toString();
    }
}
