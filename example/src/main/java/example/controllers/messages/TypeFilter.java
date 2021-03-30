package example.controllers.messages;

import aero.t2s.modes.decoder.df.*;
import javafx.application.Platform;

import java.lang.reflect.Type;

public class TypeFilter {
    private Class<?> type;
    private Class<?> subtype;

    public TypeFilter(Class<?> type, Class<?> subtype) {
        this.type = type;
        this.subtype = subtype;
    }

    public static TypeFilter from(DownlinkFormat df) {
        if (df instanceof DF17) {
            return new TypeFilter(df.getClass(), ((DF17) df).getExtendedSquitter().getClass());
        }

        if (df instanceof DF18) {
            return new TypeFilter(df.getClass(), ((DF18) df).getExtendedSquitter().getClass());
        }

        if (df instanceof DF20) {
            if (((DF20) df).isValid()) {
                return new TypeFilter(df.getClass(), ((DF20) df).getBds().getClass());
            }
        }

        if (df instanceof DF21) {
            if (((DF21) df).isValid()) {
                return new TypeFilter(df.getClass(), ((DF21) df).getBds().getClass());
            }
        }

        return new TypeFilter(df.getClass(), null);
    }

    public Class<?> getType() {
        return type;
    }

    public Class<?> getSubtype() {
        return subtype;
    }

    @Override
    public String toString() {
        if (type == null) {
            return "All";
        }

        if (subtype != null) {
           return String.format("%s - %s", type.getSimpleName(), subtype.getSimpleName());
        }

        return type.getSimpleName();
    }

    public boolean is(DownlinkFormat df) {
        if (!df.getClass().equals(type)) {
            return false;
        }


        if (df instanceof DF17) {
            return ((DF17) df).getExtendedSquitter().getClass().equals(subtype);
        }

        if (df instanceof DF18) {
            return ((DF18) df).getExtendedSquitter().getClass().equals(subtype);
        }

        if (df instanceof DF20) {
            if (((DF20) df).isValid()) {
                return ((DF20) df).getBds().getClass().equals(subtype);
            }
        }

        if (df instanceof DF21) {
            if (((DF21) df).isValid()) {
                return ((DF21) df).getBds().getClass().equals(subtype);
            }
        }

        return true;
    }
}
