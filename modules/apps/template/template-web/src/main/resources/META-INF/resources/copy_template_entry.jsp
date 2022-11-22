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
String redirect = ParamUtil.getString(request, "redirect");

long templateEntryId = ParamUtil.getLong(request, "templateEntryId");

TemplateEntry templateEntry = TemplateEntryLocalServiceUtil.fetchTemplateEntry(templateEntryId);

DDMTemplate ddmTemplate = DDMTemplateLocalServiceUtil.fetchDDMTemplate(templateEntry.getDDMTemplateId());

portletDisplay.setShowBackIcon(true);
portletDisplay.setURLBack(redirect);

renderResponse.setTitle(LanguageUtil.format(request, "copy-x", HtmlUtil.escape(ddmTemplate.getName(locale))));
%>

<portlet:actionURL name="/template/copy_template_entry" var="copyTemplateEntryURL">
	<portlet:param name="mvcPath" value="/copy_template_entry.jsp" />
</portlet:actionURL>

<liferay-frontend:edit-form
	action="<%= copyTemplateEntryURL %>"
	name="fm"
>
	<aui:input name="redirect" type="hidden" value="<%= redirect %>" />

	<aui:input name="templateEntryId" type="hidden" value="<%= templateEntryId %>" />

	<aui:model-context bean="<%= ddmTemplate %>" model="<%= DDMTemplate.class %>" />

	<liferay-frontend:edit-form-body>
		<liferay-ui:error exception="<%= TemplateNameException.class %>" message="please-enter-a-valid-name" />

		<liferay-frontend:fieldset>
			<aui:input name="name" />

			<aui:input name="description" />
		</liferay-frontend:fieldset>
	</liferay-frontend:edit-form-body>

	<liferay-frontend:edit-form-footer>
		<liferay-frontend:edit-form-buttons
			redirect="<%= redirect %>"
			submitLabel="copy"
		/>
	</liferay-frontend:edit-form-footer>
</liferay-frontend:edit-form>