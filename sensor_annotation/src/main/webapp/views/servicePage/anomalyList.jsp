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
</head>
<body>
<c:import url="/views/common/userInfoBar.jsp"></c:import>

<div class="page-body">
	<div class="row">
			<div class="col-sm-4 col-sm-offset-1" style="float:left">
				<div class="panel panel-primary">
				   <div class="panel-heading">
				      <h3 class="panel-title">异常列表</h3>
				   </div>
				   <div class="panel-body">
	
					<table class="table table-bordered" >
						<c:forEach var="key" items="${anomalys.keySet()}">
						<tr>
							<td><c:out value="${key}"/></td>
							<td><c:out value="${times.get(key).toString()}"/></td>
						</tr>
						</c:forEach>
					</table>
				</div>
			</div>
		</div>
		<div class="col-sm-5" style="float:left">
				<div class="panel panel-primary">
				   <div class="panel-heading">
				      <h3 class="panel-title">异常致因</h3>
				   </div>
				   <div class="panel-body">
	
					<table class="table table-bordered" >
						<c:forEach var="key" items="${anomalys.keySet()}">
						<tr>
							<c:forEach var="i" begin="0" end="${anomalys.get(key).size()-1 }">
								<td><c:out value="${anomalys.get(key).get(i)}"/></td>
							</c:forEach>
						</tr>
						</c:forEach>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>

<c:import url="/views/common/importJs.jsp"></c:import>
</body>
</html>