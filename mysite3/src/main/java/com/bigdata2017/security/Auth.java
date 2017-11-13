package com.bigdata2017.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* method에 붙여 사용할 수 있음, 런타임시에 확인. method와 type(class)에 둘 다 하려면 여러개 써 주면 됨 */
@Target( {ElementType.METHOD, ElementType.TYPE} )
//@Target(ElementType.METHOD)
@Retention( RetentionPolicy.RUNTIME )
public @interface Auth {
	/* 아래처럼 함수로 정의해 놓고 default값을 써 놓으면 사용할 때 @Auth("admin")이라고 할 때, value는 "admin"이고 test는 1로 세팅된다. */
//	String value() default "user";
//	int test() default 1;
	
	/* String보다는 enum을 사용하여 특정 값들만 지정할 수 있게 한다. */
//	String role() default "user";
	
	public enum Role {ADMIN, USER};
	public Role role() default Role.USER;
}
