<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	 
	<link rel="shortcut icon" type="image/x-icon" href="favicon.ico" />
	<title>设备标注系统</title>
	<style type="text/css">
	.span-interval {
		margin-left : 10px;
		margin-right : 30px;
	}
	</style>
	<link rel="stylesheet" href="<%=basePath %>assets/css/common.css"/>
	<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css"/>
</head>
<!-- 负责背景和信息条 -->
<body>
<div id="header">
	<nav class="navbar navbar-inverse">
			  <div class="container-fluid">
				<h3>设备标注系统</h3>
			    <ul class="nav navbar-nav">
			      <li><a href="<%=basePath%>index.jsp">主页</a></li>
			      <li><a href="<%=basePath%>Anomaly_showAllAnomaly.do">设备异常查询</a></li>
			      <li><a href="<%=basePath%>autoGenerator.do">设备自动控制</a></li>
			      <li class="dropdown">
			        <a class="dropdown-toggle" data-toggle="dropdown" href="#">分类
			        <span class="caret"></span></a>
			        <ul class="dropdown-menu-my dropdown-menu">
			          <li><label>test</label></li>
			          <li><span class="span-interval"><span class="span-interval"><a href="">test1</a></span><span><a href="">哈哈哈</a></span></span></li>
			        </ul>
			      </li>

			    </ul>
			    <ul class="nav navbar-nav navbar-right">
			    	<c:if var="state" test="${sessionScope.userInfo.name == null || sessionScope.userInfo.name == '' }">
						<li><a href="<%=basePath %>views/login/register.html"><span class="glyphicon glyphicon-user"></span>用户注册</a></li>
			      		<li><a href="<%=basePath %>views/login/login.html"><span class="glyphicon glyphicon-log-in"></span>用户登录</a></li>
					</c:if>
					<c:if test="${!state }">
						
						<li class="dropdown">
					        <a name="navbar_a" class="dropdown-toggle" data-toggle="dropdown" href="#navbar_a">设备管理
					        <span class="caret"></span></a>
					        <ul class="dropdown-menu">
					          <li><a href="<%=basePath %>deviceShowAll.do?status=current&pageNum=1">我的设备</a><li>
					          <li><a href="<%=basePath %>views/servicePage/deviceEdit.jsp">新建设备</a><li>
<!-- 					          <li><a data-toggle="modal" data-target="#modal_addVideo"href="#">新建设备</a><li>  -->
					        </ul>
					    </li>
						<li class="dropdown">
					        <a class="dropdown-toggle" data-toggle="dropdown" href="#navbar_a">业务管理
					        <span class="caret"></span></a>
					        <ul class="dropdown-menu">
					          <li><a href="<%=basePath %>sensor_data_diagnosis.do">产生诊断结果</a><li>
					          <li><a href="<%=basePath %>views/userInteractive/userInformation.jsp">账户设置</a><li> 
					        </ul>
					    </li>
					    
						<li><a href="<%=basePath%>userLogout.do">退出</a><li>
					</c:if>
			    </ul>
			  </div>
		</nav>
</div>

</body>
</html>
