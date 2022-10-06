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

<%@ include file="/facets/init.jsp" %>

<%
int frequencyThreshold = dataJSONObject.getInt("frequencyThreshold");
boolean showAssetCount = dataJSONObject.getBoolean("showAssetCount", true);

String[] values = new String[0];

if (dataJSONObject.has("values")) {
	JSONArray valuesJSONArray = dataJSONObject.getJSONArray("values");

	values = new String[valuesJSONArray.length()];

	for (int i = 0; i < valuesJSONArray.length(); i++) {
		values[i] = valuesJSONArray.getString(i);
	}
}

TypeSearchFacetDisplayContextBuilder typeSearchFacetDisplayContextBuilder = new TypeSearchFacetDisplayContextBuilder(renderRequest);

typeSearchFacetDisplayContextBuilder.setClassNames(values);
typeSearchFacetDisplayContextBuilder.setFacet(facet);
typeSearchFacetDisplayContextBuilder.setFrequenciesVisible(showAssetCount);
typeSearchFacetDisplayContextBuilder.setFrequencyThreshold(frequencyThreshold);
typeSearchFacetDisplayContextBuilder.setLocale(locale);
typeSearchFacetDisplayContextBuilder.setParameterName(facet.getFieldId());
typeSearchFacetDisplayContextBuilder.setParameterValue(fieldParam);

TypeSearchFacetDisplayContext typeSearchFacetDisplayContext = typeSearchFacetDisplayContextBuilder.build();
%>

<div class="panel panel-secondary">
	<div class="panel-heading">
		<div class="panel-title">
			<liferay-ui:message key="asset-entries" />
		</div>
	</div>

	<div class="panel-body">
		<div class="<%= cssClass %>" data-facetFieldName="<%= HtmlUtil.escapeAttribute(facet.getFieldId()) %>" id="<%= randomNamespace %>facet">
			<aui:input autocomplete="off" name="<%= HtmlUtil.escapeAttribute(facet.getFieldId()) %>" type="hidden" value="<%= fieldParam %>" />

			<ul class="asset-type list-unstyled">
				<li class="default facet-value">
					<a class="<%= typeSearchFacetDisplayContext.isNothingSelected() ? "facet-term-selected" : "facet-term-unselected" %>" data-value="" href="javascript:void(0);"><liferay-ui:message key="<%= HtmlUtil.escape(facetConfiguration.getLabel()) %>" /></a>
				</li>

				<%
				for (BucketDisplayContext bucketDisplayContext : typeSearchFacetDisplayContext.getBucketDisplayContexts()) {
				%>

					<li class="facet-value">
						<a class="<%= bucketDisplayContext.isSelected() ? "facet-term-selected" : "facet-term-unselected" %>" data-value="<%= HtmlUtil.escapeAttribute(bucketDisplayContext.getFilterValue()) %>" href="javascript:void(0);">
							<%= bucketDisplayContext.getBucketText() %>

							<c:if test="<%= bucketDisplayContext.isFrequencyVisible() %>">
								<span class="frequency">(<%= bucketDisplayContext.getFrequency() %>)</span>
							</c:if>
						</a>
					</li>

				<%
				}
				%>

			</ul>
		</div>
	</div>
</div>