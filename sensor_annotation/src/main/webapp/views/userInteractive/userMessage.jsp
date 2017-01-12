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
<title>在线教育系统</title>
<link rel="stylesheet" href="assets/css/userMessage.css"/>
</head>
<body>
<c:import url="/views/common/userInfoBar.jsp"></c:import>

<div class="page-body">
	<div class="row">
		<div class="col-sm-10 col-sm-offset-1">
			<div class="panel panel-primary">
			   <div class="panel-heading">
			      <h3 class="panel-title">好友申请</h3>
			   </div>
			   <div class="panel-body">
			   	 <table class="table-bordered col-sm-12">
			   	 	<tr><th class="col-sm-3" align="center">From</th><th class="col-sm-9" align="center">消息</th></tr>
			   		<c:forEach items="${objectList}" var="object">
			     		<c:if test="${object.type == 'request_addFriend'}">
			     			<tr>
			     				<td class="col-sm-3" align="center">${object.froms}</td>
			     				<td class="col-sm-9" align="center" class="align_right">请求添加您为好友,是否同意<input class="align-right" type="button" value="同意" 
			                   onclick="javascript:window.location.href='<%=basePath%>insertRecord.do?from=${object.froms }'"/></td>
			     			</tr>
			     		</c:if>
			     	</c:forEach>
			   	 </table>
			     	
			   </div>
		   </div>
		   
		   <div class="panel panel-primary">
			   <div class="panel-heading">
			      <h3 class="panel-title">私信消息</h3>
			   </div>
			   <div class="panel-body">
			   	 <table class="table-bordered col-sm-12">
			   	 	<tr><th class="col-sm-3" align="center">From</th><th class="col-sm-9" align="center">消息</th></tr>
			   		<c:forEach items="${objectList}" var="object">
			     		<c:if test="${object.type == 'message'}">
			     			<tr>
			     				<td align="center">${object.froms}</td>
			     				<td align="center">
			     					<c:out value="${object.content }"/>
			     					<label data-toggle="modal" data-target="#modal_send" onclick="javascript:document.getElementById('p').value='${object.froms}'">回复</label>
			                  	 </td>
			     			</tr>
			     		</c:if>
			     	</c:forEach>
			   	 </table>
			     	
			   </div>
		   </div>
		</div>
	</div>
</div>
<ul class="pagination">
			<c:if test="${pageNum != 1}">  
                   <li class="previous"><a href="<%=basePath %>messageShow.do?pageNum=${pageNum - 1}">上一页</a></li> 
            </c:if>
            <c:if test="${pageTotal != 1}">  
                <c:forEach var="pageIndex" begin="1" end="${pageTotal}">  
                    <c:choose>  
                        <c:when test="${pageNum == pageIndex}">
                        	<li class="active"><a>${pageIndex}</a></li>  
                        </c:when>  
                        <c:otherwise>
                        	<li><a href="<%=basePath %>messageShow.do?pageNum=${pageIndex}">${pageIndex}</a></li>
                        </c:otherwise>  
                    </c:choose>  
                </c:forEach>  
            </c:if>
        
           	<c:if test="${pageNum != pageTotal && pageTotal !=0}">  
		           <li class="next"><a href="<%=basePath %>messageShow.do?pageNum=${pageNum+1}">下一页</a></li>  
		   	</c:if>
		</ul>
	
<div class="modal fade" id="modal_send" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" 
               data-dismiss="modal" aria-hidden="true">
                  &times;
            </button>
            <h4 class="modal-title" id="myModalLabel">发送私信</h4>
         </div>
         <div class="modal-body">
			<form name="sendMsg" class="form-horizontal" action="<%=basePath %>messageSend.do" method="post">
					   <div class="form-group">
					      <label class="col-sm-3 control-label">消息内容</label>
					      <span class="col-sm-8">
					         <textarea placeholder="请输入信息" class="form-control" name="message" rows="6" cols="50"></textarea>
					      </span>
					      <input id="p" type="hidden" name="tos"/>
					      <input type="hidden" name="state" value="2"/>
					   </div>
				</form>
         </div>
         <div class="modal-footer">
            <button type="button" class="btn btn-default" 
               data-dismiss="modal">关闭
            </button>
            <button type="button" class="btn btn-primary" onclick="javascript:document.sendMsg.submit()"> 发送</button>
         </div>
      </div><!-- /.modal-content -->
</div><!-- /.modal -->
</div>


	<c:import url="/views/common/importJs.jsp"></c:import>
</body>
</html>