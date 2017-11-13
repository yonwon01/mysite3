package com.bigdata2017.mysite.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class MeasureExecutionTimeAspect {

	/* 모든 패키지 중 마지막 패키지가 repository이고 그 안의 모든 클래스의 모든 함수(모든 파라미터 가능, 모든 리턴값 가능) */
	@Around("execution(* *..repository.*.*(..)) || execution(* *..service.*.*(..)) || execution(* *..controller.*.*(..))")
	public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
		// before advice
		
		/* spring에서 제공해주는 시간 측정 class */
		/* DB에서 list불러오는 시간 측정 테스트를 위해 -> AOP로 뺄 것임 */
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		Object result = pjp.proceed();

		// after advice
		stopWatch.stop();
		Long totalTime = stopWatch.getLastTaskTimeMillis();
		
		String className = pjp.getTarget().getClass().getName();
		String methodName = pjp.getSignature().getName();
		String taskName = className + "." + methodName;
		System.out.println("[ExecutionTime]" + "[" + taskName + "]" + totalTime + "mills");
		
		return result;
	}
}
