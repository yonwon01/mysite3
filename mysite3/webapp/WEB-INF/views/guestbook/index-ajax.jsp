<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%> 
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="${pageContext.request.contextPath }/assets/css/guestbook-ajax.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script type="text/javascript" src="${pageContext.request.contextPath }/assets/js/jquery/jquery-1.9.0.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script type="text/javascript">
//jQuery PlugIn Test
/* jQuery객체를 파라미터로 받는 function에서 파라미터 이름을 '$'로 사용함 */
(function($){
	$.fn.hello = function(){
		/* 여기서 this는 element가 된다...? */
		console.log( $(this).attr("id") + ": hello~");
	}

})(jQuery);
</script>


<script type="text/javascript">
var isEnd = false;
/* messageBox나 아래의 render function같은 경우는 window가 로딩된 후
실행되는 $(function(){...})안에서 실행되기 때문에 DOM이 구성된 상태라 function내에서
"#id" 값들을 가져올 수 있다. */
var messageBox = function(title, message, callback){
	$("#dialog-message").attr("title", title);
	$("#dialog-message p").html(message);
	$("#dialog-message").dialog({
		modal: true,
		buttons: {
			"확인": function(){
				$(this).dialog("close");
			}
		},
		/* callback함수가 null이면 깡통함수를 실행하게끔 */
		close: callback || function(){}
	});
}


var render = function( vo, mode ) {
	var html =
		/* html tag를 아래와 같이 적기엔 실수도 많고 복잡하니, EJS같은 lib를 사용한다.(추후 변경할 것) */ 
		/* html5에는 그냥 no=''라고 숨겨서 쓰면 에러를 발생시킨다. 따라서 숨겨서 사용하는 데이터 속성들은 data-no='5같은 방식으로 사용한다  */
		"<li data-no='" + vo.no + "'>" +
		"<strong>" + vo.name + "</strong>" +
		"<p>" + vo.content.replace("\n", "<br>") + "</p>" +
		"<strong></strong>" +
		"<a href='' data-no='" + vo.no + "'>삭제</a>" +
		"</li>";
	if( mode == true ) {
		$( "#list-guestbook" ).prepend(html);
	} else {
		$( "#list-guestbook" ).append(html);
	}
}

var fetchList = function(){
	if( isEnd == true ) {
		return;
	}
	
	/* attr("data-no")를 쓰는 것이 표준인데, jquery에서 data("이름")을 지원해준다. */
	/* JS에서 '||'를 사용하게 되면, 앞의 데이터 : $("#list-guestbook li").last().data("no")가 null이면 0으로 셋팅하도록 해 지원해줌. 따라서 아래의 startNo == null 체크는 안해도 됨 */
	var startNo = $("#list-guestbook li").last().data("no") || 0;
//	if( startNo == null ) {
//		startNo = 0;
//	}

	$.ajax({
		url: "/mysite3/api/guestbook/list?no=" + startNo,
		type: "get",
		dataType: "json",
		data: "",
		success: function( response ) {
			if( response.result != "success" ){
				console.log( response.message );
				return;
			}
			
			// 끝 감지
			if( response.data.length < 5 ) {
				isEnd = true;
				/* 아래는 btn-next id의 속성값에 disable을 true로 설정하게 함.(attr()과 유사하나 조금 다름) */
				$( "#btn-next" ).prop( "disable", true );
			}
			
			$.each( response.data, function(index, vo){
				render( vo, false );
			});
		}
	});
}
$(function(){
	/* 삭제 시 비밀번호 입력 다이알로그 정의 */
	var deleteDialog = $("#dialog-delete-form").dialog({
		autoOpen: false,
		modal: true,
		buttons: {
			"삭제": function(){
				var no = $("#hidden-no").val();
				var password = $("#password-delete").val();

				/* ajax 통신 */
				$.ajax({
					url: "/mysite3/api/guestbook/delete",
					type: "post",
					dataType: "json",
					data: "no=" + no + "&password=" + password,
					success: function( response ){
						if( response.data == "fail"){
							console.log( response.message);
							return;
						}

						if( response.data == -1 ){
							$(".validateTips.normal").hide();
							$(".validateTips.error").show();
							$("#password-delete").val("");
							return;
						}

						/* server에서 삭제한 데이터의 no값을 받아왔으니, ui를 삭제함 */
						$("#list-guestbook li[data-no=" + response.data + "]").remove();
						deleteDialog.dialog("close");
					},
					error: function( xhr, status, e ){
						console.error( status + ":" + e );
					}
				});
				console.log("삭제!!!:" + no + ":" + password);
				$(this).dialog("close");
			},
			"취소": function(){
				$(this).dialog("close");
			}
		},
		close: function(){
			$("#password-delete").val("");
			$("#hidden-no").val("");
		}
	});

	/* 브라우저에서 scroll할 때마다 호출됨, 기본적으로 scrollTop의 길이와(스크롤 된 후 위쪽 길이) 브라우저에 표시되는 윈도우 길이를 더하면 전체 문서의 길이 */
	$(window).scroll( function(){
		var $window = $(this);
		var scrollTop = $window.scrollTop();
		var windowHeight = $window.height();
		var documentHeight = $( document ).height();
		
		//console.log( scrollTop + ":" + windowHeight + ":" + documentHeight );
		/* 스크롤바의 thumb가 바닥 전 10px까지 도달 했을 때(페이스북처럼 스크롤바 내리면 동적 로딩될 수 있도록 하기 위함) */
		if( scrollTop + windowHeight + 30 > documentHeight ) {
			fetchList();
		}
	});
	
	/* 모든 function에는 event객체가 들어있으나, 안쓸때에는 event를 파라미터에 안 써도 됨 */
	$("#add-form").submit( function(event){
		/* form tag에서 submit button을 눌렀을 때 이동하지 않도록 막아버림 */
		event.preventDefault();

		/* user 객체를 밖에다 선언해주면, $("#id").val()로 읽어올 때, html DOM이 구성되지 않은 상태에서 가져오려하니, user객체는 null값들만 들어가게 된다.
			따라서 window가 로딩 된 후 불리는 $(function(){ ... }) 요 안에서 처리하게 되면 dom이 구성된 상태니까 사용할 수 있다.
			*참고 : 전역으로 선언된 render에서도 jQuery로 불러다 쓰지만, render자체가 fatchList()를 수행하는 과정에서 사용되기에 dom이 구성된 상태이다.*  
		*/
		/*
		var user = {
				name: $("#input-name").val(),	//"name": $("#input-name").val(), <- 이렇게 하는 것과 동일
				password: $("#input-password").val(),
				content: $("#tx-content").val()
		};
		*/
		
		if($("#input-name").val() === ''){
			// $("#dialog-message").dialog();
			messageBox( 
				"메세지 등록", 
				"이름이 비어 있습니다.",
				function(){
					$("#input-name").focus();
				});
			return;
		}

		if($("#input-password").val() === ''){
			messageBox( 
				"메세지 등록", 
				"비밀번호가 비어 있습니다.",
				function(){
					$("#input-password").focus();
				});
			return;
		}

		/* 위의 user객체를 만드는 방식으로 하면 form으로 넘어가는 객체가 많을 때 일일이 다 쳐줘야 하니까 관리가 어렵다. jQuery의 serialize()를 사용해서 form tag의 객체들을 받아와 한번에 넘겨준다. */
		/* 그러나, serialize()에서 데이터 형식을 자동변환해주지는 않고, form tag의 객체들을 [{"name":"", "value": ""}, {...}, {...}] 이런식으로 보내주니, 아래 코드를 작성해서 json형식으로 맞춰 줘야 한다. */
		var data = {};
		$.each($(this).serializeArray(), function(index, o){
			data[o.name] = o.value
		});
		
		/* ajax */
		$.ajax({
			url: "/mysite3/api/guestbook/insert",
			type: "post",
			dataType: "json",	/* 받는 데이터의 타입 */
			contentType: "application/json",	/* contentType을 정해주지 않으면 그냥 form data 타입으로 나간다.(json이 아님) 따라서 json으로 지정 */
			//data: JSON.stringify(user),
			data: JSON.stringify(data),
			success: function( response ) {
				if( response.result != "success" ){
					console.log( response.message );
					return;
				}
				render( response.data, true );
			}
		});
	});
	$("#btn-next").click( function(){
		fetchList();
	});
	
	/* fetchList()를 수행하기 전에 DOM이 구성되지 않았으니 아래 코드는 수행되지 않음. */
	/* 따라서 live event mapping을 시켜줘야 한다. */
	$( document ).on( "click", "#list-guestbook li a", function(event){
		/* 기본적으로 앵커태그라서 click에 대한 event가 있으니까 막아줘야 실행될 수 있음 */
		event.preventDefault();

		/* hidden으로 form tag안에서 Server로 넘겨줄 데이터 */
		var no = $(this).data("no");
		$("#hidden-no").val(no);

		deleteDialog.dialog( "open" );
		console.log("aaa");
	});
	
	// 최초 리스트 가져오기
	fetchList();
});
</script>
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="content">
			<div id="guestbook">
				<h1>방명록</h1>
				<form id="add-form" action="${pageContext.servletContext.contextPath }/api/guestbook/insert" method="post">
					<input type="text" id="input-name" name="name" placeholder="이름">
					<input type="password" id="input-password" name="password" placeholder="비밀번호">
					<textarea id="tx-content" name="content" placeholder="내용을 입력해 주세요."></textarea>
					<input type="submit" value="보내기" />
				</form>
				<ul id="list-guestbook">

					
				</ul>
				<div style="text-align: center;padding: 20px">
					<button id="btn-next" style="padding: 10px 20px">다음</button>
				</div>
			</div>
			<div id="dialog-delete-form" title="메세지 삭제" style="display:none">
  				<p class="validateTips normal">작성시 입력했던 비밀번호를 입력하세요.</p>
  				<p class="validateTips error" style="display:none">비밀번호가 틀립니다.</p>
  				<form>
 					<input type="password" id="password-delete" value="" class="text ui-widget-content ui-corner-all">
					<input type="hidden" id="hidden-no" value="">
					<input type="submit" tabindex="-1" style="position:absolute; top:-1000px">
  				</form>
			</div>
			<div id="dialog-message" title="" style="display:none">
  				<p></p>
			</div>						
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp">
			<c:param name="menu" value="guestbook-ajax"/>
		</c:import>
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>