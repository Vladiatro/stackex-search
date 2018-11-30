<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <title>StackOverflow questions search</title>
    <link href="<c:url value="/resources/css/main.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/bootstrap.css" />" rel="stylesheet">
    <script type="text/javascript" src="<c:url value="/resources/js/jquery-3.3.1.min.js" />"></script>
    <script type="text/javascript" src="<c:url value="/resources/js/bootstrap.js" />"></script>
  </head>
  <body>
    <nav class="navbar navbar-light bg-dark">
      <span class="navbar-brand mb-0 h1 text-light">StackOverflow questions search</span>
      <form method="get" class="form-inline input-group mb-3">
        <c:forEach items="${params.asMap('query', 'page').entrySet()}" var="p">
          <input type="hidden" name="${p.key}" value="${p.value}" />
        </c:forEach>
        <input type="text" name="query" value="${param.query}" class="form-control"
               placeholder="Search…" aria-label="Search…" aria-describedby="basic-addon2"/>
        <div class="input-group-append">
          <input type="submit" class="btn btn-outline-info" value="Search"/>
        </div>
      </form>
    </nav>
    <div class="row content">

      <div class="col-2 searchParams">
        <form method="get">
          <div class="form-group">
            <label for="pageSize">Page size</label>
            <select class="form-control" id="pageSize" name="pageSize">
              <c:forEach items="${[5, 10, 20, 50, 100]}" var="i">
                <option <c:if test="${i == params.pageSize}"><c:out value= "selected=selected"/></c:if>>${i}</option>
              </c:forEach>
            </select>
          </div>
          <div class="form-group">
            <label for="order">Order</label>
            <select class="form-control" id="order" name="orderAsc">
              <option <c:if test="${params.orderAsc}"><c:out value= "selected=selected"/></c:if>
                      value="true">ascending</option>
              <option <c:if test="${!params.orderAsc}"><c:out value= "selected=selected"/></c:if>
                      value="false">descending</option>
            </select>
          </div>
          <div class="form-group">
            <label for="sortBy">Sort by</label>
            <select class="form-control" id="sortBy" name="sortBy">
              <c:forEach items="${['activity', 'votes', 'creation', 'relevance']}" var="s">
                <option <c:if test="${s.equals(params.sortBy)}"><c:out value= "selected=selected"/></c:if>>${s}</option>
              </c:forEach>
            </select>
          </div>
          <input type="hidden" value="${params.inTitle}" name="query" />
          <input type="submit" class="btn btn-primary mb-2 btn-block" value="Apply"/>
        </form>
      </div>

      <div class="col-7">
        <c:forEach items="${response.items}" var="item">
          <div class="question">
            <div class="media">
              <div class="mr-3 status ${item.answerCount > 0 ? 'answered' : ''}">
                <div class="counts">${item.answerCount}</div>
                <div>${item.answerCount % 10 == 1 ? 'answer' : 'answers'}</div>
              </div>
              <div class="media-body">
                <h5><a href="${item.link}" class="mt-0">${item.title}</a></h5>
                <div class="text-muted small">${item.getBodyPreview()}</div>
              </div>
            </div>
            <div class="question-author">
              Asked ${item.getDateFormatted()} by
              <a href="${item.owner.link eq null ? '#' : item.owner.link}">
                <c:if test="${item.owner.profileImage ne null}"><img src="${item.owner.profileImage}" /></c:if>
                  ${item.owner.displayName}
              </a>
            </div>
            </div>
        </c:forEach>

        <c:if test="${response.items.size() == 0 && response.errorId == 200}">
          <h2>Try to search for <a href="?query=java" >java</a></h2>
        </c:if>

        <c:if test="${response.errorId != 200}">
          <h4>Error ${response.errorId}: ${response.errorMessage}</h4>
        </c:if>

        <c:if test="${response.items.size() > 0}">
          <nav>
            <ul class="pagination">
              <c:forEach items="${pages}" var="page">
                <li class="page-item ${page.current ? 'active' : ''} ${page.enabled ? '' : 'disabled'}">
                  <c:if test="${page.link eq null}">
                    <p class="pages-skip">${page.label}</p>
                  </c:if>
                  <c:if test="${page.link ne null}">
                    <a class="page-link" href="${page.link}">${page.label}</a>
                  </c:if>
                </li>
              </c:forEach>
            </ul>
          </nav>
        </c:if>
      </div>
    </div>
  </body>
</html>
