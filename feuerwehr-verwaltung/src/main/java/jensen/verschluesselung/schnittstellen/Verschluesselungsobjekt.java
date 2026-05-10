/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.builder.ReflectionToStringBuilder
 *  org.apache.commons.lang3.builder.ToStringStyle
 */
package jensen.verschluesselung.schnittstellen;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Verschluesselungsobjekt {
    public String toString() {
        return ReflectionToStringBuilder.toString((Object)this, (ToStringStyle)ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

