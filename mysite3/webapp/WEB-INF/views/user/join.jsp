<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%-- spring에서 제공하는 라이브러리로, validation 체크 후 BindingResult를 JSP에서 받아올 수 있게 해줌 --%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%-- springweb에서 제공하는 라이브러리로, form태그를 사용할 수 있게 해 준다. --%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.servletContext.contextPath }/assets/css/user.css" rel="stylesheet" type="text/css">
<script src="${pageContext.servletContext.contextPath }/assets/js/jquery/jquery-1.9.0.js" type="text/javascript"></script>
<script>
var FormValidator = {
	$buttonCheckEmail: null,
	$inputTextEmail:null,
	$imgCheck: null,
	init: function(){
		this.$imgCheck = $( "#img-check" );
		this.$inputTextEmail = $( "#email" );
		this.$buttonCheckEmail = $("#btn-checkemail");
		this.$inputTextEmail.change(this.onInputTextEmailChanged.bind(this));		
		this.$buttonCheckEmail.click(this.onButtonCheckEmailClicked.bind(this));
		$( "#join-form" ).submit(this.onJoinFormSubmit.bind(this))
	},
	onCheckEmailAjaxError: function( xhr, status, e ) {
		console.error( status + ":" + e );
	},
	onCheckEmailAjaxSuccess: function( response ) {
		if(response.result != "success"){
			console.log( response.message );
			return;
		}
		
		if( response.data == true ) {
			alert( "이미 사용하고 있는 email입니다." );
			this.$inputTextEmail.
			val( "" ).
			focus();
			return;
		}
		
		this.$imgCheck.show();
		this.$buttonCheckEmail.hide();	
	},
	onJoinFormSubmit: function(){
		console.log( "submit" );
		//이름
		var $inputTextName = $("#name");
		if( $inputTextName.val() === '' ) {
			alert( "이름은 필수 항목입니다." );
			$inputTextName.focus();
			return false;
		}
		
		//이메일
		if( this.$inputTextEmail.val() === '' ) {
			alert( "이메일은 필수 항목입니다." );
			this.$inputTextEmail.focus();
			return false;
		}
		
		//이메일 중복 체크
		if( this.$imgCheck.is(":visible") == false ) {
			alert( "이메일은 필수 항목입니다." );
			return false;
		}
		
		//비밀번호
		var $inputPassword = $("#password");
		if( $inputPassword.val() === '' ) {
			alert( "비밀번호는 필수 항목입니다." );
			$inputPassword.focus();
			return false;
		}
		
		//약관 동의
		var $inputCheckAgree = $("#agree-prov");
		if( $inputCheckAgree.is( ":checked") == false ) {
			alert( "약관 동의를 해 주세요" );
			return false;
		}
		
		return true;
	},
	onInputTextEmailChanged: function(){
		this.$imgCheck.hide();
		this.$buttonCheckEmail.show();		
	},
	onButtonCheckEmailClicked: function(){
		var email = this.$inputTextEmail.val();
		if( email == "" ) {
			return;
		}
		
		//ajax 통신
		$.ajax( {
			url: "${pageContext.servletContext.contextPath }/api/user/checkemail?email=" + email,
			type: "get",
			dataType: "json",
			data:"",
			success: this.onCheckEmailAjaxSuccess.bind(this),
			error: this.onCheckEmailAjaxError
		} );
	}
}
$(function(){
	FormValidator.init();
});
</script>
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp"/>
		<div id="content">
			<div id="user">

				<!-- springweb의 form lib를 추가했으니, form:form은 html의 form태그가 아니다. -->
				<form:form 
					modelAttribute="userVo"
					id="join-form" 
					name="joinForm" 
					method="post" 
					action="${pageContext.servletContext.contextPath }/user/join">
					<label class="block-label" for="name">이름</label>
					<!-- 사용자가 name을 입력했는데 에러가 나면 userController에서 @ModelAttribute로 userVo가 리턴되니까 --> 
					<%-- value=${userVo.name }로 설정하면 사용자가 적었던 이름을 그대로 남아있게 ux를 좋게 할 수 있다. --%>
					
					<!-- 아래의 input태그도 form:input으로 대체 가능 -->
					<%-- <input id="name" name="name" type="text" value="${userVo.name }"> --%>
					<form:input path="name"/>
					
					<p style="padding-left: 0; font-weight: bold; text-align: left; color: red">
						<%-- 아래의 spring:hasBindErrors를 form:errors 태그 하나로 대체할 수 있다. --%>
						<%-- 단점1: properties의 name값들이 여러개일 경우, 동시에 화면에 출력된다. --%>
						<%-- 단점2: 페이지 소스보기를 누르면 <span> 태그로 소스가 나온다. --%>
						<form:errors path="name"/>
					</p>
					
					<%-- 위에서 <form:errors/> 로 아래 내용들을 대체함 --%>
					<%-- <spring:hasBindErrors name="userVo"> --%>
					   <%-- <c:if test="${errors.hasFieldErrors('name') }"> --%>
					   		<%-- jsp페이지에서 어떨 때는 codes[0]이 나오고, 어떨 때는 defaultMessage가 나오는데, 원인은 모름. 그럴경우 defaultMessage를 제거 --%>
					   		<%-- <p style="padding-left: 0; text-align: left; color: red; font-weight: bold;"> --%>
						   		<%-- <spring:message --%>
						   			<%-- code="${errors.getFieldError( 'name' ).codes[0] }" --%>
						   			<%-- text="${errors.getFieldError( 'name' ).defaultMessage }" --%>
						   		<%-- /> --%>
						   		<%-- messages_ko.properties에 등록한 커스텀 메세지를 사용할 수 있게함 --%>
						   		<!-- 형식은 AnnotationName.Bean객체이름.객체내의 변수 이름 -->
						   		<%-- strong대신 위처럼 spring:message를 사용하여 커스텀 메세지를 사용할 수 있다. --%>
						        <%-- <strong>${errors.getFieldError( 'password' ).defaultMessage }</strong> --%>
					        <!-- </p> -->
					   <%-- </c:if> --%>
					<%-- </spring:hasBindErrors> --%>

					<label class="block-label" for="email">이메일</label>
					<input id="email" name="email" type="text" value="">
					<img id="img-check" src="${pageContext.servletContext.contextPath }/assets/images/check-ok.png" style="display:none; width:24px"/>
					<input id="btn-checkemail" type="button" value="중복체크">
					
					<label class="block-label">패스워드</label>
					
					<!-- 아래의 password또한 form:password로 대체 가능 -->
					<!-- <input id="password" name="password" type="password" value=""> -->
					<form:password path="password"/>
					
					<spring:hasBindErrors name="userVo">
					   <c:if test="${errors.hasFieldErrors('password') }">
					   		<p style="padding-left: 0; text-align: left; color: red; font-weight: bold;">
					   			<%-- jsp페이지에서 어떨 때는 codes[0]이 나오고, 어떨 때는 defaultMessage가 나오는데, 원인은 모름. 그럴경우 defaultMessage를 제거  --%>
						   		<spring:message
						   			code="${errors.getFieldError( 'password' ).codes[0] }"
						   			text="${errors.getFieldError( 'password' ).defaultMessage }"
						   		/>
						   		<%-- messages_ko.properties에 등록한 커스텀 메세지를 사용할 수 있게함 --%>
						   		<%-- 형식은 AnnotationName.Bean객체이름.객체내의 변수 이름 --%>
						   		<%-- strong대신 위처럼 spring:message를 사용하여 커스텀 메세지를 사용할 수 있다. --%>
						        <%-- <strong>${errors.getFieldError( 'password' ).defaultMessage }</strong> --%>
					        </p>
					   </c:if>
					</spring:hasBindErrors>
					   		
										
					<fieldset>
						<legend>성별</legend>
						<label>여</label> 
						<!-- 마찬가지로 모든 input tag들은 form:에서 지원하는 것들로 대체 가능 -->
						<!-- <input type="radio" name="gender" value="female" checked="checked"> -->
						<form:radiobutton path="gender" value="female"/>
						
						<label>남</label> 
						<!-- <input type="radio" name="gender" value="male"> -->
						<form:radiobutton path="gender" value="male"/>
						
					</fieldset>
					
					<fieldset>
						<legend>약관동의</legend>
						<input id="agree-prov" type="checkbox" name="agreeProv" value="y">
						<label>서비스 약관에 동의합니다.</label>
					</fieldset>
					
					<input type="submit" value="가입하기">
					
				</form:form>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"/>
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>