package com.bigdata2017.mysite.aspect;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class MeasureExecutionTimeAspect {

	private static final Log LOG = LogFactory.getLog( MeasureExecutionTimeAspect.class );
	
	@Around("execution(* *..repository.*.*(..))||execution(* *..service.*.*(..))||execution(* *..controller.*.*(..))")
	public Object aroundAdvice(ProceedingJoinPoint pjp) 
		throws Throwable{
		//before advice code
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		// method 실행
		Object result = pjp.proceed();
		
		//after advice code
		stopWatch.stop();
		Long totalTime = stopWatch.getTotalTimeMillis();

		String className = pjp.getTarget().getClass().getName();
		String methodName = pjp.getSignature().getName();
		String taskName = className + "." + methodName;
		
		LOG.info( "[ExecutionTime]" + "[" + taskName + "]" + totalTime + "mills" );
		
		return result;
	}
}
