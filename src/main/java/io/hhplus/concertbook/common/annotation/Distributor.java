package io.hhplus.concertbook.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Distributor {
    // 필요한 속성을 정의할 수 있습니다.
    String value() default "";
}
