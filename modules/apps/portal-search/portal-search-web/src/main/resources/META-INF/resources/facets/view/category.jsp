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
CategorySearchFacetDisplayContextBuilder categorySearchFacetDisplayContextBuilder = new CategorySearchFacetDisplayContextBuilder(renderRequest);

categorySearchFacetDisplayContextBuilder.setAssetCategoryLocalService(AssetCategoryLocalServiceUtil.getService());
categorySearchFacetDisplayContextBuilder.setAssetCategoryPermissionChecker(new AssetCategoryPermissionCheckerImpl(themeDisplay.getPermissionChecker()));
categorySearchFacetDisplayContextBuilder.setDisplayStyle(dataJSONObject.getString("displayStyle", "cloud"));
categorySearchFacetDisplayContextBuilder.setFacet(facet);
categorySearchFacetDisplayContextBuilder.setFrequenciesVisible(dataJSONObject.getBoolean("showAssetCount", true));
categorySearchFacetDisplayContextBuilder.setFrequencyThreshold(dataJSONObject.getInt("frequencyThreshold"));
categorySearchFacetDisplayContextBuilder.setLocale(locale);
categorySearchFacetDisplayContextBuilder.setMaxTerms(dataJSONObject.getInt("maxTerms", 10));
categorySearchFacetDisplayContextBuilder.setParameterName(facet.getFieldId());
categorySearchFacetDisplayContextBuilder.setParameterValue(fieldParam);
categorySearchFacetDisplayContextBuilder.setPortal(PortalUtil.getPortal());

CategorySearchFacetDisplayContext categorySearchFacetDisplayContext = categorySearchFacetDisplayContextBuilder.build();
%>

<c:choose>
	<c:when test="<%= categorySearchFacetDisplayContext.isRenderNothing() %>">
		<aui:input autocomplete="off" name="<%= HtmlUtil.escapeAttribute(categorySearchFacetDisplayContext.getParameterName()) %>" type="hidden" value="<%= categorySearchFacetDisplayContext.getParameterValue() %>" />
	</c:when>
	<c:otherwise>
		<div class="panel panel-secondary">
			<div class="panel-heading">
				<div class="panel-title">
					<liferay-ui:message key="categories" />
				</div>
			</div>

			<div class="panel-body">
				<div class="asset-tags <%= cssClass %>" data-facetFieldName="<%= HtmlUtil.escapeAttribute(categorySearchFacetDisplayContext.getParameterName()) %>" id="<%= randomNamespace %>facet">
					<aui:input autocomplete="off" name="<%= HtmlUtil.escapeAttribute(categorySearchFacetDisplayContext.getParameterName()) %>" type="hidden" value="<%= categorySearchFacetDisplayContext.getParameterValue() %>" />

					<ul class="<%= categorySearchFacetDisplayContext.isCloud() ? "tag-cloud" : "tag-list" %> list-unstyled">
						<li class="default facet-value">
							<a class="<%= categorySearchFacetDisplayContext.isNothingSelected() ? "facet-term-selected" : "facet-term-unselected" %>" data-value="" href="javascript:void(0);"><liferay-ui:message key="<%= HtmlUtil.escape(facetConfiguration.getLabel()) %>" /></a>
						</li>

						<%
						for (BucketDisplayContext bucketDisplayContext : categorySearchFacetDisplayContext.getBucketDisplayContexts()) {
						%>

							<li class="facet-value tag-popularity-<%= bucketDisplayContext.getPopularity() %>">
								<a class="<%= bucketDisplayContext.isSelected() ? "facet-term-selected" : "facet-term-unselected" %>" data-value="<%= HtmlUtil.escapeAttribute(bucketDisplayContext.getFilterValue()) %>" href="javascript:void(0);">
									<%= HtmlUtil.escape(bucketDisplayContext.getBucketText()) %>

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
	</c:otherwise>
</c:choose>