package com.bigdata2017.mysite.dto;

/**
 * Data Transfer Object를 정의해 놓고(VO와 다름) 이를 클라이언트 <-> 서버간 데이터 통신 시 사용하게하여 데이터 전송 규칙을 통일한 후,
 * 회사에서 개발자별로 서로 다른 방식으로 데이터를 주고 받는 것을 방지한다. 
 * 이를 위해서 setter도 없애서 외부에서 조작을 못하고, 정해진 것으로만 사용할 수 있게 하였다.
 */
public class JSONResult {
	private String result;	//"success" or "fail"
	private String message;	// result 가 "fail"인 경우 응답
	private Object data;	// result 가 "success"인 경우 응답
	
	/* 이렇게 생성자를 private로 하게 하여 바깥에서 new로 생성하지 못하도록 방지함 */
	private JSONResult(String result, String message, Object data) {
		this.result = result;
		this.message = message;
		this.data = data;
	}

	/* ----- 아래의 api들을 제공함으로서 데이터만 넣어서 결과값을 받아갈 수 있도록 함  -----*/
	public static JSONResult success( Object data ) {
		return new JSONResult("success", null, data);
	}
	
	public static JSONResult fail( String message ) {
		return new JSONResult("fail", message, null);
	}
	/* ----- 위의 api들을 제공함으로서 데이터만 넣어서 결과값을 받아갈 수 있도록 함  -------*/

	public String getResult() {
		return result;
	}
	public String getMessage() {
		return message;
	}
	public Object getData() {
		return data;
	}
	
	@Override
	public String toString() {
		return "JSONResult [result=" + result + ", message=" + message + ", data=" + data + "]";
	}
}
