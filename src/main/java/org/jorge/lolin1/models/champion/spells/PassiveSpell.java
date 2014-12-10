package org.jorge.lolin1.models.champion.spells;

import lol4j.protocol.dto.lolstaticdata.PassiveDto;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

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

    @SuppressWarnings("unused, FieldCanBeLocal")
    // Used through reflection
    private final String imageName, detail, name;

    public PassiveSpell(PassiveDto passiveDto) {
        this(passiveDto.getName(), passiveDto.getDescription(), passiveDto.getImage().getFull());
    }

    protected PassiveSpell(String _name, String _detail, String _imageName) {
        this.name = _name;
        if (!(this instanceof ActiveSpell)) {
            this.detail = _detail.replaceAll("=\"", "=\\\\\"").replaceAll(
                    "\">", "\\\\\">");
        } else {
            this.detail = _detail;
        }
        this.imageName = _imageName;
        System.out.println("Passive " + name + " built");
    }

    public final String getName() {
        return name;
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
                ret.append("\"").append(x.getName()).append("\":\"").append(x.get(this)).append("\"");
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