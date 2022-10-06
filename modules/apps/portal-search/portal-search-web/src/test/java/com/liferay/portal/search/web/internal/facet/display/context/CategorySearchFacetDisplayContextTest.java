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

package com.liferay.portal.search.web.internal.facet.display.context;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.search.facet.collector.FacetCollector;
import com.liferay.portal.kernel.search.facet.collector.TermCollector;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.search.web.internal.facet.display.context.builder.AssetCategoryPermissionChecker;
import com.liferay.portal.search.web.internal.facet.display.context.builder.CategorySearchFacetDisplayContextBuilder;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.portlet.RenderRequest;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author André de Oliveira
 */
public class CategorySearchFacetDisplayContextTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() {
		Mockito.doReturn(
			_facetCollector
		).when(
			_facet
		).getFacetCollector();
	}

	@Test
	public void testEmptySearchResults() throws Exception {
		String facetParam = StringPool.BLANK;

		CategorySearchFacetDisplayContext categorySearchFacetDisplayContext =
			createDisplayContext(facetParam);

		List<BucketDisplayContext> bucketDisplayContexts =
			categorySearchFacetDisplayContext.getBucketDisplayContexts();

		Assert.assertEquals(
			bucketDisplayContexts.toString(), 0, bucketDisplayContexts.size());

		Assert.assertEquals(
			facetParam, categorySearchFacetDisplayContext.getParameterValue());
		Assert.assertTrue(
			categorySearchFacetDisplayContext.isNothingSelected());
		Assert.assertTrue(categorySearchFacetDisplayContext.isRenderNothing());
	}

	@Test
	public void testEmptySearchResultsWithPreviousSelection() throws Exception {
		long assetCategoryId = RandomTestUtil.randomLong();

		_setUpAssetCategory(assetCategoryId, 0);

		String facetParam = String.valueOf(assetCategoryId);

		CategorySearchFacetDisplayContext categorySearchFacetDisplayContext =
			createDisplayContext(facetParam);

		List<BucketDisplayContext> bucketDisplayContexts =
			categorySearchFacetDisplayContext.getBucketDisplayContexts();

		Assert.assertEquals(
			bucketDisplayContexts.toString(), 1, bucketDisplayContexts.size());

		BucketDisplayContext bucketDisplayContext = bucketDisplayContexts.get(
			0);

		Assert.assertEquals(
			String.valueOf(assetCategoryId),
			bucketDisplayContext.getFilterValue());
		Assert.assertEquals(
			String.valueOf(assetCategoryId),
			bucketDisplayContext.getBucketText());
		Assert.assertEquals(0, bucketDisplayContext.getFrequency());
		Assert.assertTrue(bucketDisplayContext.isFrequencyVisible());
		Assert.assertTrue(bucketDisplayContext.isSelected());

		Assert.assertEquals(
			facetParam, categorySearchFacetDisplayContext.getParameterValue());
		Assert.assertFalse(
			categorySearchFacetDisplayContext.isNothingSelected());
		Assert.assertFalse(categorySearchFacetDisplayContext.isRenderNothing());
	}

	@Test
	public void testExcludedGroup() throws Exception {
		long assetCategoryId = RandomTestUtil.randomLong();

		long groupId = RandomTestUtil.randomLong();

		long stagingGroupId = RandomTestUtil.randomLong();

		createGroup(groupId, stagingGroupId);

		_setUpAssetCategory(assetCategoryId, stagingGroupId);

		_excludedGroupId = stagingGroupId;

		int frequency = RandomTestUtil.randomInt();

		setUpOneTermCollector(assetCategoryId, frequency);

		String facetParam = StringPool.BLANK;

		CategorySearchFacetDisplayContext categorySearchFacetDisplayContext =
			createDisplayContext(facetParam);

		List<BucketDisplayContext> bucketDisplayContexts =
			categorySearchFacetDisplayContext.getBucketDisplayContexts();

		Assert.assertEquals(
			bucketDisplayContexts.toString(), 0, bucketDisplayContexts.size());

		_excludedGroupId = 0;
	}

	@Test
	public void testOneTerm() throws Exception {
		long assetCategoryId = RandomTestUtil.randomLong();

		_setUpAssetCategory(assetCategoryId, 0);

		int frequency = RandomTestUtil.randomInt();

		setUpOneTermCollector(assetCategoryId, frequency);

		String facetParam = StringPool.BLANK;

		CategorySearchFacetDisplayContext categorySearchFacetDisplayContext =
			createDisplayContext(facetParam);

		List<BucketDisplayContext> bucketDisplayContexts =
			categorySearchFacetDisplayContext.getBucketDisplayContexts();

		Assert.assertEquals(
			bucketDisplayContexts.toString(), 1, bucketDisplayContexts.size());

		BucketDisplayContext bucketDisplayContext = bucketDisplayContexts.get(
			0);

		Assert.assertEquals(
			String.valueOf(assetCategoryId),
			bucketDisplayContext.getFilterValue());
		Assert.assertEquals(
			String.valueOf(assetCategoryId),
			bucketDisplayContext.getBucketText());
		Assert.assertEquals(frequency, bucketDisplayContext.getFrequency());
		Assert.assertTrue(bucketDisplayContext.isFrequencyVisible());
		Assert.assertFalse(bucketDisplayContext.isSelected());

		Assert.assertEquals(
			facetParam, categorySearchFacetDisplayContext.getParameterValue());
		Assert.assertTrue(
			categorySearchFacetDisplayContext.isNothingSelected());
		Assert.assertFalse(categorySearchFacetDisplayContext.isRenderNothing());
	}

	@Test
	public void testOneTermWithPreviousSelection() throws Exception {
		long assetCategoryId = RandomTestUtil.randomLong();

		_setUpAssetCategory(assetCategoryId, 0);

		int frequency = RandomTestUtil.randomInt();

		setUpOneTermCollector(assetCategoryId, frequency);

		CategorySearchFacetDisplayContext categorySearchFacetDisplayContext =
			createDisplayContext(String.valueOf(assetCategoryId));

		List<BucketDisplayContext> bucketDisplayContexts =
			categorySearchFacetDisplayContext.getBucketDisplayContexts();

		Assert.assertEquals(
			bucketDisplayContexts.toString(), 1, bucketDisplayContexts.size());

		BucketDisplayContext bucketDisplayContext = bucketDisplayContexts.get(
			0);

		Assert.assertEquals(
			String.valueOf(assetCategoryId),
			bucketDisplayContext.getFilterValue());
		Assert.assertEquals(
			String.valueOf(assetCategoryId),
			bucketDisplayContext.getBucketText());
		Assert.assertEquals(frequency, bucketDisplayContext.getFrequency());
		Assert.assertTrue(bucketDisplayContext.isFrequencyVisible());
		Assert.assertTrue(bucketDisplayContext.isSelected());

		Assert.assertEquals(
			assetCategoryId,
			GetterUtil.getLong(
				categorySearchFacetDisplayContext.getParameterValue()));
		Assert.assertFalse(
			categorySearchFacetDisplayContext.isNothingSelected());
		Assert.assertFalse(categorySearchFacetDisplayContext.isRenderNothing());
	}

	@Test
	public void testUnauthorized() throws Exception {
		long assetCategoryId = RandomTestUtil.randomLong();

		_setUpAssetCategoryUnauthorized(assetCategoryId);

		int frequency = RandomTestUtil.randomInt();

		setUpOneTermCollector(assetCategoryId, frequency);

		String facetParam = StringPool.BLANK;

		CategorySearchFacetDisplayContext categorySearchFacetDisplayContext =
			createDisplayContext(facetParam);

		List<BucketDisplayContext> bucketDisplayContexts =
			categorySearchFacetDisplayContext.getBucketDisplayContexts();

		Assert.assertEquals(
			bucketDisplayContexts.toString(), 0, bucketDisplayContexts.size());

		Assert.assertEquals(
			facetParam, categorySearchFacetDisplayContext.getParameterValue());
		Assert.assertTrue(
			categorySearchFacetDisplayContext.isNothingSelected());
		Assert.assertTrue(categorySearchFacetDisplayContext.isRenderNothing());
	}

	@Test
	public void testUnauthorizedWithPreviousSelection() throws Exception {
		long assetCategoryId = RandomTestUtil.randomLong();

		_setUpAssetCategoryUnauthorized(assetCategoryId);

		String facetParam = String.valueOf(assetCategoryId);

		CategorySearchFacetDisplayContext categorySearchFacetDisplayContext =
			createDisplayContext(facetParam);

		List<BucketDisplayContext> bucketDisplayContexts =
			categorySearchFacetDisplayContext.getBucketDisplayContexts();

		Assert.assertEquals(
			bucketDisplayContexts.toString(), 0, bucketDisplayContexts.size());

		Assert.assertEquals(
			facetParam, categorySearchFacetDisplayContext.getParameterValue());
		Assert.assertFalse(
			categorySearchFacetDisplayContext.isNothingSelected());
		Assert.assertFalse(categorySearchFacetDisplayContext.isRenderNothing());
	}

	protected CategorySearchFacetDisplayContext createDisplayContext(
		String parameterValue) {

		RenderRequest renderRequest = Mockito.mock(RenderRequest.class);

		CategorySearchFacetDisplayContextBuilder
			categorySearchFacetDisplayContextBuilder =
				new CategorySearchFacetDisplayContextBuilder(renderRequest);

		categorySearchFacetDisplayContextBuilder.setAssetCategoryLocalService(
			_assetCategoryLocalService);
		categorySearchFacetDisplayContextBuilder.
			setAssetCategoryPermissionChecker(_assetCategoryPermissionChecker);
		categorySearchFacetDisplayContextBuilder.setDisplayStyle("cloud");
		categorySearchFacetDisplayContextBuilder.setFacet(_facet);
		categorySearchFacetDisplayContextBuilder.setFrequenciesVisible(true);
		categorySearchFacetDisplayContextBuilder.setFrequencyThreshold(0);
		categorySearchFacetDisplayContextBuilder.setMaxTerms(0);
		categorySearchFacetDisplayContextBuilder.setParameterName(
			_facet.getFieldId());
		categorySearchFacetDisplayContextBuilder.setParameterValue(
			parameterValue);
		categorySearchFacetDisplayContextBuilder.setPortal(_getPortal());

		if (_excludedGroupId > 0) {
			categorySearchFacetDisplayContextBuilder.setExcludedGroupId(
				_excludedGroupId);
		}

		return categorySearchFacetDisplayContextBuilder.build();
	}

	protected Group createGroup(long groupId, long stagingGroupId) {
		Group group = Mockito.mock(Group.class);

		Mockito.doReturn(
			groupId
		).when(
			group
		).getGroupId();

		return group;
	}

	protected TermCollector createTermCollector(
		long assetCategoryId, int frequency) {

		TermCollector termCollector = Mockito.mock(TermCollector.class);

		Mockito.doReturn(
			frequency
		).when(
			termCollector
		).getFrequency();

		Mockito.doReturn(
			String.valueOf(assetCategoryId)
		).when(
			termCollector
		).getTerm();

		return termCollector;
	}

	protected ThemeDisplay getThemeDisplay() {
		ThemeDisplay themeDisplay = Mockito.mock(ThemeDisplay.class);

		Mockito.doReturn(
			Mockito.mock(PortletDisplay.class)
		).when(
			themeDisplay
		).getPortletDisplay();

		return themeDisplay;
	}

	protected void setUpOneTermCollector(long assetCategoryId, int frequency) {
		Mockito.doReturn(
			Collections.singletonList(
				createTermCollector(assetCategoryId, frequency))
		).when(
			_facetCollector
		).getTermCollectors();
	}

	private AssetCategory _createAssetCategory(
		long assetCategoryId, long groupId) {

		AssetCategory assetCategory = Mockito.mock(AssetCategory.class);

		Mockito.doReturn(
			assetCategoryId
		).when(
			assetCategory
		).getCategoryId();

		Mockito.doReturn(
			groupId
		).when(
			assetCategory
		).getGroupId();

		Mockito.doReturn(
			String.valueOf(assetCategoryId)
		).when(
			assetCategory
		).getTitle(
			(Locale)Mockito.any()
		);

		Mockito.doReturn(
			assetCategory
		).when(
			_assetCategoryLocalService
		).fetchAssetCategory(
			assetCategoryId
		);

		return assetCategory;
	}

	private HttpServletRequest _getHttpServletRequest() {
		HttpServletRequest httpServletRequest = Mockito.mock(
			HttpServletRequest.class);

		Mockito.doReturn(
			getThemeDisplay()
		).when(
			httpServletRequest
		).getAttribute(
			WebKeys.THEME_DISPLAY
		);

		return httpServletRequest;
	}

	private Portal _getPortal() {
		Portal portal = Mockito.mock(Portal.class);

		Mockito.doReturn(
			_getHttpServletRequest()
		).when(
			portal
		).getHttpServletRequest(
			Mockito.any()
		);

		return portal;
	}

	private void _setUpAssetCategory(long assetCategoryId, long groupId) {
		AssetCategory assetCategory = _createAssetCategory(
			assetCategoryId, groupId);

		Mockito.doReturn(
			true
		).when(
			_assetCategoryPermissionChecker
		).hasPermission(
			assetCategory
		);
	}

	private void _setUpAssetCategoryUnauthorized(long assetCategoryId) {
		AssetCategory assetCategory = _createAssetCategory(assetCategoryId, 0);

		Mockito.doReturn(
			false
		).when(
			_assetCategoryPermissionChecker
		).hasPermission(
			assetCategory
		);
	}

	private final AssetCategoryLocalService _assetCategoryLocalService =
		Mockito.mock(AssetCategoryLocalService.class);
	private final AssetCategoryPermissionChecker
		_assetCategoryPermissionChecker = Mockito.mock(
			AssetCategoryPermissionChecker.class);
	private long _excludedGroupId;
	private final Facet _facet = Mockito.mock(Facet.class);
	private final FacetCollector _facetCollector = Mockito.mock(
		FacetCollector.class);

}