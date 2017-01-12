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
<link rel="stylesheet" href="<%=basePath %>assets/css/datepicker.css"/>
<link rel="stylesheet" href="<%=basePath %>assets/css/userInformation.css"/>
</head>
<body>
<c:import url="/views/common/userInfoBar.jsp"></c:import>

<div class="page-body">
	<div class="row">
		<div class="col-sm-8 col-sm-offset-2">
			<div class="panel panel-primary">
			   <div class="panel-heading">
			      <h3 class="panel-title">账号安全</h3>
			   </div>
			   <div class="panel-body">
			     	<label class="control-label col-sm-2">修改密码</label>
			     	<span class="col-sm-2">
			     		<button class="btn btn-primary" data-toggle="modal" data-target="#modal_pwd">
   						修改</button></span>
			     	<label class="col-sm-offset-4 control-label col-sm-2">绑定邮箱</label>
			     	<span class="col-sm-2"><button class="btn btn-primary" data-toggle="modal" data-target="#modal_email">
   						绑定</button>
			     	</span>
			   </div>
		   </div>
		</div>
	</div>
	
	<div class="row">
		<div class="col-sm-8 col-sm-offset-2">
			<div class="panel panel-primary">
			   <div class="panel-heading">
			      <h3 class="panel-title">个人资料</h3>
			   </div>
			   <div class="panel-body">			   		
			   		<form class="form-horizontal" role="form" enctype="multipart/form-data" action="<%=basePath %>upload.do" method="post">
			   			<div class="form-group">
					      <label for="alias" class="col-sm-2 control-label">用户头像</label>
					      <span class="col-sm-2">
					  		<img height="140px" width="140px" alt="帅照" src="<%=basePath %>upload/images/userPicture/${sessionScope.userInfo.name}.jpg"/>
					      </span>
					     <span class="form-group">
					      <label for="alias" class="col-sm-2 control-label">上传新头像</label>
					       <span class="col-sm-4">
					       		<input class="form-control" type="file" name="file"/><br>
					            <input class="input-center" type="submit" value="上传"/>
					      </span>
					   </span>
					   </div>
			   		</form>
			     	<form class="form-horizontal" role="form" action="<%=basePath%>userUpdateInfo.do" method="post">
					   <div class="form-group">
					      <label for="alias" class="col-sm-2 control-label">昵称</label>
					      <span class="col-sm-8">
					         <input type="text" class="form-control" name="alias" 
					            placeholder="请输入昵称">
					      </span>
					   </div>
					   <div class="form-group">
					      <label for="gender" class="col-sm-2 control-label">姓别</label>
					      <span class="col-sm-8">
					         <select class="form-control" name="sex">
					         	<option value="male">男</option>
					         	<option value="female">女</option>
					         </select>
					      </span>
					   </div>
					   <div class="form-group">
					      <label for="birthday" class="col-sm-2 control-label">生日</label>
					      	<div class="col-sm-8 input-append date">
							  	<input id="dp" class="span2" type="text" name="date" placeholder="请选择出生日期" data-date-format="yyyy-mm-dd"/>
							  	<span class="glyphicon glyphicon-calendar"></span>
							</div>
					   </div>
					   <div class="form-group">
					   		<span class="col-sm-offset-10">
					   			<input class="from-control" type="submit" value="保存"/>
					   		</span>
					   </div>
					</form>
			   </div>
		   </div>
		</div>
	</div>
</div>

<!-- 模态框 --- 修改密码-->
<div class="modal fade" id="modal_pwd" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" 
               data-dismiss="modal" aria-hidden="true">
                  &times;
            </button>
            <h4 class="modal-title" id="myModalLabel">修改密码</h4>
         </div>
         <div class="modal-body">
			<form name="changePwd" class="form-horizontal" action="<%=basePath %>userChangePwd.do" method="post">
					   <div class="form-group">
					      <label class="col-sm-3 control-label">原始密码</label>
					      <span class="col-sm-8">
					         <input type="password" class="form-control" name="oldpwd" placeholder="请输入原始密码"/>
					      </span>
					   </div>
					   <div class="form-group">
					      <label class="col-sm-3 control-label">新密码</label>
					      <span class="col-sm-8">
					        <input type="password" class="form-control" name="newpwd" placeholder="请输入新密码"/>
					      </span>
					   </div>
					   <div class="form-group">
					      <label class="col-sm-3 control-label">确认新密码</label>
					      	<span class="col-sm-8">
							  	<input type="password" class="form-control" name="confirmpwd" placeholder="请确认新密码"/>
							</span>
					   </div>
				</form>
         </div>
         <div class="modal-footer">
            <button type="button" class="btn btn-default" 
               data-dismiss="modal">关闭
            </button>
            <button type="button" class="btn btn-primary" onclick="javascript:document.changePwd.submit()"> 提交修改</button>
         </div>
      </div><!-- /.modal-content -->
</div><!-- /.modal -->
</div>


<c:import url="/views/common/importJs.jsp"></c:import>
<script type="text/javascript" src="<%=basePath %>assets/js/bootstrap-datepicker.js"></script>
<script type="text/javascript">$('#dp').datepicker();</script>
</body>
</html>