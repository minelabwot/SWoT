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
				<div class="col-sm-8 col-sm-offset-2">
				<div class="panel panel-primary">
				   <div class="panel-heading">
				      <h3 class="panel-title">传感器设备</h3>
				   </div>
				   <div class="panel-body">
	
					<table class="table table-bordered" >
						<tr>
							<td>设备名称</td><td>设备描述</td>
							<c:if test="${status eq 'all' }"><td>所有者</td></c:if>
							<td></td>
							</tr>
						<c:forEach var="device" items="${sensors}">
						<tr>
							<td><c:out value="${device.name }"/></td>   
							<td><c:out value="${device.description }"/></td>
							<c:if test="${status eq 'all' }"><td><c:out value="${device.owner}"/></td></c:if>
							<td><a href="<%=basePath %>deviceDetail.do?id=${device.id}">详细信息</a></td>
						</tr>
						</c:forEach>
					</table>
				</div>
			</div>
		</div>
	</div>

	<div class="row">
				<div class="col-sm-8 col-sm-offset-2">
				<div class="panel panel-primary">
				   <div class="panel-heading">
				      <h3 class="panel-title">执行器设备</h3>
				   </div>
				   <div class="panel-body">
	
					<table class="table table-bordered" >
						<tr>
							<td>设备名称</td><td>设备描述</td>
							<c:if test="${status eq 'all' }"><td>所有者</td></c:if>
							<td></td>
							</tr>
						<c:forEach var="device" items="${actuators}">
						<tr>
							<td><c:out value="${device.name }"/></td>   
							<td><c:out value="${device.description }"/></td>
							<c:if test="${status eq 'all' }"><td><c:out value="${device.owner}"/></td></c:if>
							<td><a href="<%=basePath %>deviceDetail.do?id=${device.id}">详细信息</a></td>
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