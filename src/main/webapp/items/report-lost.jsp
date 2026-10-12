<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setAttribute("itemType", "LOST");
    request.getRequestDispatcher("/items/report-item.jsp").forward(request, response);
%>