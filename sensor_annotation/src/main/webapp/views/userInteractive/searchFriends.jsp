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
<link rel="stylesheet" href="<%=basePath %>assets/css/searchFriend.css"/>
</head>
<body>
<c:import url="/views/common/userInfoBar.jsp"></c:import>

	<div class="page-body">
		<div class="col-sm-2">
			<div class="panel panel-primary-my">
			   <div class="panel-heading">
			      <h3 class="panel-title-my">好友管理</h3>
			   </div>
			   <div class="noleftbottomright">
			   <c:choose>
			   <c:when test="${param.index==1 || index==1 }">
			   		<a class="list-group-item active">好友列表</a>
					<a href="<%=basePath %>views/userInteractive/searchFriends.jsp?index=2" class="list-group-item">添加好友</a>
			   	</c:when>
			   	<c:when test="${param.index==2 || index==2}">
			   		<a href="<%=basePath %>getAllFriends.do?&page=1" class="list-group-item">好友列表</a>
					<a class="list-group-item active">添加好友</a>
			   	</c:when>
			   </c:choose>
			   </div>
		   </div>
		</div>
		<div class="col-sm-10" style="height:100%">
		   <div class="panel-my panel-primary panel-right">
		   	 <c:choose>
			   	 <c:when test="${(index == 1 || param.index == 1) }">
			   	 		<div class="search-div">
							<form action="<%=basePath%>getAllFriends.do">
							<label class="label-center">搜索好友:</label>
							<input type="text" name="name" placeholder="请输入好友名称"/>
										<input type="hidden" name="page" value="1"/>
							<input type="submit" value="查询"/>
							</form>
						</div>
						<div class="row">
							<div class="col-sm-8 col-sm-offset-2 text-center">
								<c:if test="${pageTotal>0 }"><p><strong>您的好友为:</strong></p></c:if>
								<table class="table-bordered">
									<c:forEach items="${allFriends}" var="friend">
										<tr>
											<td class="col-sm-2 text-center"><c:out value="${friend}"/></td>
											<td class="col-sm-2 text-center"><label class="interval" data-toggle="modal" data-target="#modal_send" onclick="javascript:document.getElementById('p').value='${friend}'"><a href="#modal_send">发送消息</a></label>
											<label class="interval"><a href="<%=basePath %>friendDelete.do?name=${friend}">删除好友</a></label></td>
										</tr>
									</c:forEach>
								</table>
							</div>
							<div class="pagination-bottom">
								<ul class="pagination">
									<c:if var="tmp_1" test="${pageNum > 1 }"> 
						                   <li class="previous element-visiable"><a href="<%=basePath %>getAllFriends.do?page=${pageNum - 1}">上一页</a></li> 
						            </c:if>
						            <c:if  test="${!tmp_1 }"> 
						                   <li class="previous element-visiable"><a href="<%=basePath %>getAllFriends.do?page=${pageNum - 1}">上一页</a></li> 
						            </c:if>
						            <c:if test="${pageTotal > 1}">  
						                <c:forEach var="pageIndex" begin="1" end="${pageTotal}">  
						                    <c:choose>  
						                        <c:when test="${pageNum == pageIndex}">
						                        	<li class="active"><a>${pageIndex}</a></li>  
						                        </c:when>  
						                        <c:otherwise>
						                        	<li><a href="<%=basePath %>getAllFriends.do?page=${pageIndex}">${pageIndex}</a></li>
						                        </c:otherwise>  
						                    </c:choose>  
						                </c:forEach>  
						            </c:if>
						        
						           	<c:if var="tmp_2" test="${pageNum < pageTotal }">  
								           <li class="next"><a href="<%=basePath %>getAllFriends.do?page=${pageNum+1}">下一页</a></li>  
								   	</c:if>
								   	<c:if test="${!tmp_2 }">  
								           <li class="next element-visiable"><a href="<%=basePath %>getAllFriends.do?page=${pageNum+1}">下一页</a></li>  
								   	</c:if>
								</ul>
							</div>
						</div>
			   	 	</c:when>
		   	 	
			   	 	<c:when test="${(param.index ==2 || index==2)}">
			   	 		<div class="search-div">
							<form action="<%=basePath%>friendQuery.do">
							<label class="label-center">搜索好友:</label>
							<input type="text" name="friendName" placeholder="请输入好友名称"/><c:out value="${firendName}"></c:out>
										<input type="hidden" name="pageNum" value="1"/>
										<input type="hidden" name="index" value="2"/>
							<input type="submit" value="查询"/>
							</form>
						</div>
			   	 		<div class="row">
							<div class="col-sm-8 col-sm-offset-2 text-center">
								<c:if test="${pageTotal>0 }"><p><strong>符合条件的好友:</strong></p></c:if>
								<table class="table-bordered">
									<c:forEach items="${friendList}" var="friend">
										<tr>
											<td  class="col-sm-2 text-center"><c:out value="${friend.alias}"/></td>
											<td  class="col-sm-2 text-center"><a href="<%=basePath %>friendRequest.do?name=${friend.name}">加为好友</a></td>
										</tr>
									</c:forEach>
								</table>
							</div>
						</div>
						<div class="pagination-bottom">
							<ul class="pagination">
								<c:if var="tmp_1" test="${pageNum > 1 }">  
					                   <li class="previous"><a href="<%=basePath %>friendQuery.do?friendName=${friendName}&pageNum=${pageNum - 1}&index=2">上一页</a></li> 
					            </c:if>
					            <c:if test="${!tmp_1}">  
					                   <li class="previous element-visiable"><a href="<%=basePath %>friendQuery.do?friendName=${friendName}&pageNum=${pageNum - 1}&index=2">上一页</a></li> 
					            </c:if>
					            <c:if test="${pageTotal > 1}">  
					                <c:forEach var="pageIndex" begin="1" end="${pageTotal}">  
					                    <c:choose>  
					                        <c:when test="${pageNum == pageIndex}">
					                        	<li class="active"><a>${pageIndex}</a></li>  
					                        </c:when>  
					                        <c:otherwise>
					                        	<li><a href="<%=basePath %>friendQuery.do?friendName=${friendName}&pageNum=${pageIndex}&index=2">${pageIndex}</a></li>
					                        </c:otherwise>  
					                    </c:choose>  
					                </c:forEach>  
					            </c:if>
					           	<c:if var="tmp_2" test="${pageNum != pageTotal}">  
							           <li class="next"><a href="<%=basePath %>friendQuery.do?friendName=${friendName}&pageNum=${pageNum+1}&index=2">下一页</a></li>  
							   	</c:if>
							   	<c:if test="${!tmp_2}">  
							           <li class="next element-visiable"><a href="<%=basePath %>friendQuery.do?friendName=${friendName}&pageNum=${pageNum+1}&index=2">下一页</a></li>  
							   	</c:if>
							</ul>
						</div>	
			   	 	</c:when>
		   	 		<c:when test="pageTotal == 0}"><label class="label-center2"><strong>没有符合条件的好友</strong></label></c:when>
		   	 	<c:otherwise>
					
		   	 	</c:otherwise>
		   	 </c:choose>
		   </div>
		   </div>
	</div>

<!-- 发送消息模态框 -->
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
					      <input type="hidden" name="state" value="1"/>
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