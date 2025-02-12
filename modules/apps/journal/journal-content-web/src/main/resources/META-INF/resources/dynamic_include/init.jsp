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

<%@ taglib uri="http://liferay.com/tld/clay" prefix="clay" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ page import="com.liferay.journal.content.web.internal.display.context.JournalContentDisplayContext" %><%@
page import="com.liferay.journal.content.web.internal.servlet.taglib.PortletHeaderActionDropdownItemsProvider" %><%@
page import="com.liferay.portal.kernel.language.LanguageUtil" %>

<liferay-theme:defineObjects />

<%
JournalContentDisplayContext journalContentDisplayContext = (JournalContentDisplayContext)request.getAttribute(JournalContentDisplayContext.getRequestAttributeName(portletDisplay.getId()));
%>