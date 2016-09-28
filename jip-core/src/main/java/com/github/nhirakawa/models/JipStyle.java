package com.github.nhirakawa.models;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ImplementationVisibility;

@Target({ ElementType.PACKAGE, ElementType.TYPE })
@Retention(RetentionPolicy.CLASS)
@Value.Style(
    get = { "is*", "get*" },
    init = "set*",
    typeAbstract = { "Abstract*", "*IF" },
    typeImmutable = "*",
    visibility = ImplementationVisibility.SAME,
    defaults = @Value.Immutable(copy = false)
)
public @interface JipStyle {
}
