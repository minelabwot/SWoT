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
<script type="text/javascript">
	function addProperty(index) {
		var board = document.getElementById("addForm"+index);
        var e = document.createElement("div");
		e.innerHTML = "属性名";
		var input1 = document.createElement("input");
		input1.name = "property-key";
		input1.type = "text";
		var input2 = document.createElement("input");
		input2.name = "property-value";
		input2.type = "text";
		e.appendChild(input1);
		e.appendChild(input2);
		var myButton =  document.getElementById("addButton"+index);
		board.insertBefore(e,myButton);
	}
</script>
</head>


<body>
	<c:import url="/views/common/userInfoBar.jsp"></c:import>
	新建概念
	<div class="page-body">
		<div class="row" style="text-align: center">
			<div class="col-sm-8 col-sm-offset-2">
			<div class="panel panel-primary">
			   <div class="panel-heading">
			      <h3 class="panel-title">新建概念(Concept)</h3>
			   </div>
			   <div class="panel-body">
			   		<div>
				   		<form id="addForm1" action="<%=basePath %>ontologyNewConcept.do" method="post">
							概念名<input type="text" name="name"/>
							<input id="addButton1" type="button" value="添加属性" onclick="addProperty(1)"/>
						</form>
					</div>
					<button onclick="window.addForm1.submit()">提交</button>
			   </div>
			</div>
			</div>
		</div>
		
		<div class="row" style="text-align: center">
			<div class="col-sm-8 col-sm-offset-2">
			<div class="panel panel-primary">
			   <div class="panel-heading">
			      <h3 class="panel-title">新建关系(Relation)</h3>
			   </div>
			   <div class="panel-body">
			   		<div>
				   		<div>
							<form id="addForm2" action="<%=basePath %>ontologyNewRelation.do" method="post">
								关系名:<input type="text" name="name"/>
								<div>
									<span>关系主体:<input type="text" name="subject"/>
									<span style="margin-left: 20px">关系客体:<input type="text" name="object"/></span>
								</div>
								
								
								<input id="addButton2" type="button" value="添加属性" onclick="addProperty(2)"/>
							</form><br/><br/>
							<button onclick="window.addForm2.submit()">提交</button>
						</div>
						
					</div>
			   </div>
			</div>
			</div>
		</div>

	</div>
	
	
	<c:import url="/views/common/importJs.jsp"></c:import>
</body>
</html>