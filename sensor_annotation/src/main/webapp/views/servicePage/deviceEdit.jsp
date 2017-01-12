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
<link rel="stylesheet" href="<%=basePath%>assets/css/deviceEdit.css">
</head>
<body>
<c:import url="/views/common/userInfoBar.jsp"></c:import>


<div class="container">
<br/><br/><br/><br/>
<div class="row">
	<div class="col-xs-12 col-sm-6">
		<div class="col-pad">
			<h1>新建设备</h1>

<form accept-charset="UTF-8" action="<%=basePath %>devicePropertyAdd.do" class="form-horizontal" id="device_form" method="post">
  <h3>选择设备类型</h3>
 	<div class="form-group"><select class="form-control" name="deviceType">
		<option value="Sensor">Sensor</option>
		<option value="Actuator">Actuator</option>
	</select></div>
<%--   <input type="hidden" name="deviceType" value="${deviceType}"> --%>
  <div class="form-group">
    <label class="col-sm-4 col-xs-12 control-label">设备名称(Name)</label>
    <div class="col-sm-8 col-xs-12"><input class="form-control" id="device_name" name="device[name]" type="text" /></div>
  </div>

	<div class="form-group">
		<label class="col-sm-4 col-xs-12 control-label">设备描述(Description)</label>
		<div class="col-sm-8 col-xs-12"><textarea class="form-control" id="device_description" name="device[description]">
		</textarea></div>
	</div>
      <div class="form-group">
    <label class="col-sm-4 col-xs-12 control-label">设备类型(SensorType)</label>
    <div class="col-sm-8 col-xs-12">
      <input class="form-control" id="device_type" name="device[type]" type="text" />
    </div>
  </div>
  <div class="form-group">
		    <label class="col-sm-4 col-xs-12 control-label">观测属性(Property)</label>
		    <div class="col-sm-8 col-xs-12"><select class="form-control" id="device_property" name="device[property]">
		    <option></option>
		    <option value="Temperature">Temperature</option>
		    <option value="Cooling">Cooling</option>
		    <option value="Occupation">Occupation</option>
		    </select></div>
	</div>
  <div class="form-group">
	    <label class="col-sm-4 col-xs-12 control-label">测量单位(Unit)</label>
	    <div class="col-sm-8 col-xs-12"><input class="form-control" id="device_unit" name="device[unit]" type="text" /></div>
	  </div>
	 <div class="form-group">
	    <label class="col-sm-4 col-xs-12 control-label">所属机构(Owner)</label>
	    <div class="col-sm-8 col-xs-12"><input class="form-control" id="device_company" name="device[company]" type="text" /></div>
    </div>
  <div class="form-group">
    <label class="col-sm-4 col-xs-12 control-label">地区(Region)</label>
    <div class="col-sm-8 col-xs-12">
      <input class="form-control" id="device_region" name="device[region]" type="text" />
    </div>
  </div>
  <div class="form-group">
    <label class="col-sm-4 col-xs-12 control-label">场所(Spot)</label>
    <div class="col-sm-8 col-xs-12">
      <input class="form-control" id="device_spot" name="device[spot]" type="text" />
    </div>
  </div>
    
    

<%--   <c:if test='${deviceType.equals("Sensing Device")}'> --%>
<!-- 	  <div class="form-group"> -->
<!-- 	    <label class="col-sm-4 col-xs-12 control-label">产生事件(Stimulus)</label> -->
<!-- 	    <div class="col-sm-8 col-xs-12"><input class="form-control" id="device_event" name="device[event]" type="text" /></div> -->
<!-- 	  </div> -->
<%--   </c:if> --%>
<%--   <c:if test='${deviceType.equals("Actuator")}'> --%>
<!--   	<div class="form-group"> -->
<!-- 	    <label class="col-sm-4 col-xs-12 control-label">订阅事件(Subscribe)</label> -->
<!-- 	    <div class="col-sm-8 col-xs-12"><input class="form-control" id="device_subscribe" name="device[subscribe]" type="text" /></div> -->
<!-- 	  </div> -->
<!-- 	  <div class="form-group"> -->
<!-- 	    <label class="col-sm-4 col-xs-12 control-label">触发行为(Triggers)</label> -->
<!-- 	    <div class="col-sm-8 col-xs-12"><input class="form-control" id="device_trigger" name="device[trigger]" type="text" /></div> -->
<!-- 	  </div> -->
<%--   </c:if> --%>

  <div class="form-group">
    <label class="col-sm-4 control-label"></label>
    <div class="col-sm-8 col-xs-12"><p class="form-control-static"><input class="btn btn-primary" id="device_form_submit" name="commit" type="submit" value="保存设备" /></p></div>
  </div>

</form>

    </div>

    <br><br><br><br>

  </div>

<div id="sidebar" class="col-xs-12 col-sm-6">
    <h2>帮助</h2>

<h3>设备属性设置</h3>
<ul>
  <li><p><b>device Name:</b> 为一个设备输入唯一的设备名称.</p></li>
  <li><p><b>Description:</b> 简要描述该设备</p></li>
  <li><p><b>Sensor Type:</b> 描述该设备是哪类传感器</p></li>
  <li><p><b>Property:</b> 描述该设备用于检测哪些传感值e.g.Temperature</p></li>
  <li><p><b>Unit:</b> 描述该设备的测量值的单位,e.g.温度有摄氏度和华氏度两种单位</p></li>
  <li><p><b>Owner:</b> 描述该设备所属的公司或机构,不是生产商而是设备的使用者,所有者</p></li>
  <li><p><b>Region:</b> 描述该设备的地理位置,城市,地区等</p></li>
  <li><p><b>Spot:</b> 描述该设备的部署的具体场景</p></li>
</ul>

  </div>
</div>

</div>

<!-- <script> -->
<c:import url="/views/common/importJs.jsp"></c:import>
<!-- </script> -->

</body>
</html>