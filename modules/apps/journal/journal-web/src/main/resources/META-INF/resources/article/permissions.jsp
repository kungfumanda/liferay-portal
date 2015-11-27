<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ include file="/init.jsp" %>

<%
JournalArticle article = journalDisplayContext.getArticle();
%>

<liferay-ui:error-marker key="errorSection" value="permissions" />

<aui:model-context bean="<%= article %>" model="<%= JournalArticle.class %>" />

<c:if test="<%= (article == null) || article.isNew() %>">
	<aui:field-wrapper cssClass="journal-article-permissions" label="permissions">
		<liferay-ui:input-permissions
			modelName="<%= JournalArticle.class.getName() %>"
		/>
	</aui:field-wrapper>
</c:if>