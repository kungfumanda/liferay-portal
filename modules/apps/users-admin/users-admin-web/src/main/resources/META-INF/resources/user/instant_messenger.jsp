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
Contact selContact = (Contact)request.getAttribute("user.selContact");
%>

<c:choose>
	<c:when test="<%= selContact != null %>">
		<aui:model-context bean="<%= selContact %>" model="<%= Contact.class %>" />

		<div class="instant-messenger">
			<aui:input label="jabber" name="jabberSn" />
		</div>

		<div class="instant-messenger">
			<aui:input label="skype" name="skypeSn" />

			<c:if test="<%= Validator.isNotNull(selContact.getSkypeSn()) %>">
				<div class="form-feedback-group">
					<div class="form-text">
						<clay:link
							decoration="underline"
							href='<%= "skype:" + HtmlUtil.escapeAttribute(selContact.getSkypeSn()) + "?call" %>'
							label='<%= LanguageUtil.get(request, "call-this-user") %>'
						/>
					</div>
				</div>
			</c:if>
		</div>
	</c:when>
	<c:otherwise>
		<clay:alert
			message="this-section-will-be-editable-after-creating-the-user"
		/>
	</c:otherwise>
</c:choose>