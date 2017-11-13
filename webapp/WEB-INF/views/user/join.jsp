<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
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
		this.$imgCheck = $("#img-check");
		this.$inputTextEmail = $("#email");
		this.$buttonCheckEmail = $("#btn-checkemail");

		this.$inputTextEmail.change(this.onInputTextEmailChanged.bind(this));		
		this.$buttonCheckEmail.click(this.onButtonCheckEmailClicked.bind(this));
		//$( "#join-form" ).submit( this.onJoinFormSubmit.bind(this));
	},
	onJoinFormSubmit: function(){
		//이름
		var $inputTextName = $("#name");
		if( $inputTextName.val() === '' ) {
			alert( "이름은 필수 항목입니다." );
			$inputTextName.focus();
			return false;
		}
		
		//이메일
		if( this.$inputTextEmail.val() === '' ){
			alert( "이메일은 필수 항목입니다." );
			this.$inputTextEmail.focus();
			return false;
		}
		
		// 이메일 중복 체크
		if( this.$imgCheck.is(":visible") == false){
			alert( "이메일 중복 체크를 해 주세요." );
			return false;
		}
		
		// 비밀번호
		var $inputPassword = $("#password");
		if( $inputPassword.val() === '' ){
			alert( "비밀번호는 필수 항목입니다." );
			$inputPassword.focus();
			return false;			
		}
		
		//약관 동의
		var $inputCheckAgree = $("#agree-prov");
		if( $inputCheckAgree.is( ":checked" ) == false ) {
			alert( "약관 동의를 해 주세요" );
			return false;
		}
		
		return true;
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

				<form:form
					modelAttribute="userVo"
					id="join-form"
					name="joinForm"
					method="post"
					action="${pageContext.servletContext.contextPath }/user/join">
					
					<label class="block-label" for="name">이름</label>
					<form:input path="name"/>
					<p style="padding-left:0; font-weight:bold; text-align:left; color:red">
						<form:errors path="name" />
					</p>

					<label class="block-label" for="email">이메일</label>
					<input id="email" name="email" type="text" value="">
					<img id="img-check" src="${pageContext.servletContext.contextPath }/assets/images/check-ok.png" style="display:none; width:24px"/>
					
					<input id="btn-checkemail" type="button" value="중복체크">
					
					<label class="block-label">패스워드</label>
					<form:password path="password"/>
					<spring:hasBindErrors name="userVo">
					   <c:if test="${errors.hasFieldErrors('password') }">
					   		<P style="padding-left:0; text-align:left; color:#f00; font-weight:bold">
						   		<spring:message
						   			code="${errors.getFieldError( 'password' ).codes[0] }"
						   			text="${errors.getFieldError( 'password' ).defaultMessage }"
						   		/>
					   		</P>
					   </c:if>
					</spring:hasBindErrors>
					
					
					
					<fieldset>
						<legend>성별</legend>
						<label>여</label> <form:radiobutton path="gender" value="female" />
						<label>남</label> <form:radiobutton path="gender" value="male" />
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