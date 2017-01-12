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
<style type="text/css">
.float-left{
float:left;
text-align: center;
}
.bg1 {
	background-color:#efefef;
	padding: 20px;
	margin: 20px;
}
.bg2 {
	background-color:#efeff3;
	padding: 20px;
	margin: 20px;
}
</style>
</head>
<body>
<c:import url="/views/common/userInfoBar.jsp"></c:import>

<div class="page-body">
	<div class="row bg1">
		<form class="form-group" action="<%=basePath %>deviceSearching.do" method="post">
			<label class="col-sm-4 col-xs-12 control-label">Search For</label>
			<div class="form-group">
				<select class="form-control" name="searchType">
					<option value="Anomaly">Anomaly</option>
					<option value="Device">Device</option>
				</select>
			</div>
			<label class="control-label">With Option</label>
			<div class="form-group bg1">
				<div class="col-sm-5 float-left"><input class="form-control" type="text" name="first_value"/></div>
				<div class="col-sm-2 float-left">in</div>
				<div class="col-sm-5 float-left">
					<select class="col-sm-4 form-control float-left" name="first_key">
						<option>Some Field</option>
						<option value="Owner">Owner</option>
						<option value="Region">Region</option>
					</select>
				</div>
			</div>
			
			<div class="form-group bg1">
				<div class="col-sm-5 float-left"><input class="form-control" type="text" name="second_value"/></div>
				<div class="col-sm-2 float-left">in</div>
				<div class="col-sm-5 float-left">
					<select class="col-sm-4 form-control float-left" name="second_key">
						<option>Some Field</option>
						<option value="Owner">Owner</option>
						<option value="Region">Region</option>
					</select>
				</div>
			</div>
			<br/>
			<div class="form-group" style="float:right">
				<input type="submit" value="search"/>
			</div>
		</form>
	</div>
	
	<div class="row bg2">
		<h3>查询结果：</h3>
		<table class="table table-bordered">
			<c:if test="${devices != null }">
				<tr>
					<th>地区(Region)</th><th>场景(Spot)</th><th>机构(Organization)</th><th>名称(name)</th><th>简介(description)</th><th></th>
				</tr>
				<c:forEach var="device" items="${devices}">
				<tr>
					<td><c:out value="${device.getRegion() }"/></td>
					<td><c:out value="${device.getSpot() }"/></td>
					<td><c:out value="${device.getCompany() }"/></td>
					<td><c:out value="${device.getName() }"/></td>
					<td><c:out value="${device.getDescription() }"/></td>
					<td><a href="<%=basePath %>deviceDetail.do?id=${device.getId()}">详细信息</a></td>
				</tr>
			</c:forEach>
			</c:if>
			<c:if test="${anomalys != null }">
				<tr>
					<th>异常设备ID(ID)</th><th>设备名(Name)</th><th>异常事件(Anomaly)</th><th>异常致因(Cause)</th><th>发生时间(Time)</th><th></th>
				</tr>
				<c:forEach var="anomaly" items="${anomalys}">
				<tr>
					<td><c:out value="${anomaly.get(0) }"/></td>
					<td><c:out value="${anomaly.get(1) }"/></td>
					<td><c:out value="${anomaly.get(2) }"/></td>
					<td><c:out value="${anomaly.get(3) }"/></td>
					<td><c:out value="${anomaly.get(4) }"/></td>
					<td><a href="<%=basePath %>deviceDetail.do?id=${anomaly.get(0)}">设备详细信息</a></td>
				</tr>
			</c:forEach>
			</c:if>
		</table>
	</div>
</div>

<c:import url="/views/common/importJs.jsp"></c:import>
</body>
</html>