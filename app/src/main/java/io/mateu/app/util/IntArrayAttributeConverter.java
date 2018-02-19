package io.mateu.app.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by miguel on 12/10/16.
 */
@Converter(autoApply = true)
public class IntArrayAttributeConverter implements AttributeConverter<int[], String> {

    @Override
    public String convertToDatabaseColumn(int[] ints) {
        if (ints == null) return null;
        else {
            StringBuffer sb = new StringBuffer();
            for (int pos = 0; pos < ints.length; pos++) {
                if (pos > 0) sb.append(",");
                sb.append(ints[pos]);
            }
            return sb.toString();
        }
    }

    @Override
    public int[] convertToEntityAttribute(String s) {
        if (s == null) return null;
        else {
            String[] xs = s.split(",");
            int[] ints = new int[xs.length];
            for (int pos = 0; pos < xs.length; pos++) ints[pos] = Integer.parseInt(xs[pos]);
            return ints;
        }
    }
}