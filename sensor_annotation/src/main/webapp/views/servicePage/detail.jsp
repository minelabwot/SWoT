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
	body .label {
		font-size: 100%;
	}
	body .btn {
		font-size: 20px;
	}
</style>
</head>
<body>
	<c:import url="/views/common/userInfoBar.jsp"></c:import>


<div class="container">
<br/><br/><br/><br/>
	
	<div class="row">
		<div class="col-xs-12 col-sm-6">
			<div class="col-pad">
				<h1>设备详细信息</h1>
<form accept-charset="UTF-8" class="form-horizontal" id="device_form" method="post">
  <div class="form-group">
    <label class="col-sm-4 col-xs-12 control-label">设备ID(ID)</label>
    <div class="col-sm-8 col-xs-12"><label class="control-label"><c:out value="${avp.get('id') }"/></label></div>
  </div>
  <div class="form-group">
    <label class="col-sm-4 col-xs-12 control-label">设备名称(Name)</label>
    <div class="col-sm-8 col-xs-12"><label class="control-label"><c:out value="${avp.get('name') }"/></label></div>
  </div>
	<div class="form-group">
	    <label class="col-sm-4 col-xs-12 control-label">所有者(Owner)</label>
	    <div class="col-sm-8 col-xs-12"><label class="control-label"><c:out value="${avp.get('user') }"/></label></div>
	 </div>
	<div class="form-group">
		<label class="col-sm-4 col-xs-12 control-label">设备描述(Description)</label>
		<div class="col-sm-8 col-xs-12"><label style="word-break:break-all"><c:out value="${avp.get('description')}"/></label></div>
		
	</div>
      <div class="form-group">
    <label class="col-sm-4 col-xs-12 control-label">设备类型(SensorType)</label>
    <div class="col-sm-8 col-xs-12">
      <label class="control-label"><c:out value="${avp.get('hasType') }"/></label>
    </div>
  </div>
  <div class="form-group">
		    <label class="col-sm-4 col-xs-12 control-label">观测属性(Peoperty)</label>
		   <div class="col-sm-8 col-xs-12"><label class="control-label"><c:out value="${avp.get('forProperty') }"/></label></div>
		</div>
  <div class="form-group">
	    <label class="col-sm-4 col-xs-12 control-label">测量单位(Unit)</label>
	    <div class="col-sm-8 col-xs-12"><label class="control-label"><c:out value="${avp.get('hasUnit') }"/></label></div>
	  </div>
  <div class="form-group">
    <label class="col-sm-4 col-xs-12 control-label">所在地区(Region)</label>
    <div class="col-sm-8 col-xs-12"><label class="control-label"><c:out value="${avp.get('hasLocation') }"/></label></div>
  </div>
  <div class="form-group">
    <label class="col-sm-4 col-xs-12 control-label">部署场所(Spot)</label>
    <div class="col-sm-8 col-xs-12"><label class="control-label"><c:out value="${avp.get('hasSpot') }"/></label></div>
  </div>
      <div class="form-group">
    <label class="col-sm-4 col-xs-12 control-label">所属机构(Organization)</label>
    <div class="col-sm-8 col-xs-12"><label class="control-label"><c:out value="${avp.get('isOwnedBy') }"/></label></div>
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
  <li><p><b>Observation:</b> 描述该设备用于服务那个场景</p></li>
  <li><p><b>Sensor Type:</b> 描述该设备是哪类传感器,能检测哪些传感值</p></li>
  <li><p><b>Location:</b> 描述该设备的地理位置,城市,地区等</p></li>
  <li><p><b>Company:</b> 描述该设备所属的公司,不是生产商而是设备的使用者,所有者</p></li>
  <li><p><b>Unit:</b> 描述该设备的测量值的单位,e.g.温度有摄氏度和华氏度两种单位</p></li>
<!--    <li><p><b>Metadata:</b> Enter information about device data, including JSON, XML, or CSV data. </p></li>
  <li><p><b>Tags:</b> Enter keywords that identify the device. Separate tags with commas.</p></li>
  <li><p><b>Latitude:</b> Specify the position of the sensor or thing that collects data in decimal degrees. For example, the latitude of the city of London is 51.5072.</p></li>
  <li><p><b>Longitude:</b> Specify the position of the sensor or thing that collects data in decimal degrees. For example, the longitude of the city of London is -0.1275.</p></li>
  <li><p><b>Elevation:</b> Specify the position of the sensor or thing that collects data in meters. For example, the elevation of the city of London is 35.052.</p></li>
  <li><p><b>Make Public:</b> If you want to make the device publicly available, check this box.</p></li>
  -->
</ul>
<form accept-charset="UTF-8" action="<%=basePath %>ELrun.do" class="form-horizontal" id="el_control" method="post">
	<div class="form-group">
	    <label class="col-sm-4 control-label"></label>
	    <input type="hidden" name="deviceType" value="${deviceType}">
	    <input type="hidden" name="id" value="${id}">
	    <div class="col-sm-8 col-xs-12"><p class="form-control-static"><input class="btn btn-primary" type="submit" value="链接实体" /></p></div>
	</div>
</form>

  </div>
  
</div>
<div>
	<h3>构造传感器数据:</h3>
	<form accept-charset="UTF-8" action="<%=basePath %>sensor_data_update.do" class="form-horizontal" id="device_form" method="post">
		<div class="form-group">
	    <label class="col-sm-4 col-xs-12 control-label">设备ID</label>
	    <div class="col-sm-8 col-xs-12"> <input class="form-control" name="id" type="text" /></div>
	  </div>
	  <div class="form-group">
	    <label class="col-sm-4 col-xs-12 control-label">当前观测值</label>
	    <div class="col-sm-8 col-xs-12"> <input class="form-control" name="value" type="text" /></div>
	  </div>
	  <div class="form-group">
    <label class="col-sm-4 control-label"></label>
    <div class="col-sm-8 col-xs-12"><p class="form-control-static"><input class="btn btn-primary" type="submit" value="产生数据" /></p></div>
  </div>
	</form>
</div>
</div>



<c:import url="/views/common/importJs.jsp"/>
</body>
</html>