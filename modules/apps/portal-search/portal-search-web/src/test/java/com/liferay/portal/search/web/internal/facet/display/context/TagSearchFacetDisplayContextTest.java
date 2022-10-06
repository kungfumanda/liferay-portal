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

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.search.facet.collector.FacetCollector;
import com.liferay.portal.kernel.search.facet.collector.TermCollector;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.search.web.internal.facet.display.context.builder.TagSearchFacetDisplayContextBuilder;
import com.liferay.portal.search.web.internal.tag.facet.configuration.TagFacetPortletInstanceConfiguration;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.Collections;
import java.util.List;

import javax.portlet.RenderRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author André de Oliveira
 */
public class TagSearchFacetDisplayContextTest {

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
		String facetParam = "";

		TagSearchFacetDisplayContext tagSearchFacetDisplayContext =
			createDisplayContext(facetParam);

		List<BucketDisplayContext> bucketDisplayContexts =
			tagSearchFacetDisplayContext.getBucketDisplayContexts();

		Assert.assertEquals(
			bucketDisplayContexts.toString(), 0, bucketDisplayContexts.size());

		Assert.assertEquals(
			facetParam, tagSearchFacetDisplayContext.getParameterValue());
		Assert.assertTrue(tagSearchFacetDisplayContext.isNothingSelected());
		Assert.assertTrue(tagSearchFacetDisplayContext.isRenderNothing());
	}

	@Test
	public void testEmptySearchResultsWithPreviousSelection() throws Exception {
		String term = RandomTestUtil.randomString();

		String facetParam = term;

		TagSearchFacetDisplayContext tagSearchFacetDisplayContext =
			createDisplayContext(facetParam);

		List<BucketDisplayContext> bucketDisplayContexts =
			tagSearchFacetDisplayContext.getBucketDisplayContexts();

		Assert.assertEquals(
			bucketDisplayContexts.toString(), 1, bucketDisplayContexts.size());

		BucketDisplayContext bucketDisplayContext = bucketDisplayContexts.get(
			0);

		Assert.assertEquals(0, bucketDisplayContext.getFrequency());
		Assert.assertEquals(term, bucketDisplayContext.getBucketText());
		Assert.assertTrue(bucketDisplayContext.isSelected());
		Assert.assertTrue(bucketDisplayContext.isFrequencyVisible());

		Assert.assertEquals(
			facetParam, tagSearchFacetDisplayContext.getParameterValue());
		Assert.assertFalse(tagSearchFacetDisplayContext.isNothingSelected());
		Assert.assertFalse(tagSearchFacetDisplayContext.isRenderNothing());
	}

	@Test
	public void testOneTerm() throws Exception {
		String term = RandomTestUtil.randomString();
		int frequency = RandomTestUtil.randomInt();

		setUpOneTermCollector(term, frequency);

		String facetParam = StringPool.BLANK;

		TagSearchFacetDisplayContext tagSearchFacetDisplayContext =
			createDisplayContext(facetParam);

		List<BucketDisplayContext> bucketDisplayContexts =
			tagSearchFacetDisplayContext.getBucketDisplayContexts();

		Assert.assertEquals(
			bucketDisplayContexts.toString(), 1, bucketDisplayContexts.size());

		BucketDisplayContext bucketDisplayContext = bucketDisplayContexts.get(
			0);

		Assert.assertEquals(term, bucketDisplayContext.getBucketText());
		Assert.assertEquals(frequency, bucketDisplayContext.getFrequency());
		Assert.assertFalse(bucketDisplayContext.isSelected());
		Assert.assertTrue(bucketDisplayContext.isFrequencyVisible());

		Assert.assertEquals(
			facetParam, tagSearchFacetDisplayContext.getParameterValue());
		Assert.assertTrue(tagSearchFacetDisplayContext.isNothingSelected());
		Assert.assertFalse(tagSearchFacetDisplayContext.isRenderNothing());
	}

	@Test
	public void testOneTermWithPreviousSelection() throws Exception {
		String term = RandomTestUtil.randomString();
		int frequency = RandomTestUtil.randomInt();

		setUpOneTermCollector(term, frequency);

		String facetParam = term;

		TagSearchFacetDisplayContext tagSearchFacetDisplayContext =
			createDisplayContext(facetParam);

		List<BucketDisplayContext> bucketDisplayContexts =
			tagSearchFacetDisplayContext.getBucketDisplayContexts();

		Assert.assertEquals(
			bucketDisplayContexts.toString(), 1, bucketDisplayContexts.size());

		BucketDisplayContext bucketDisplayContext = bucketDisplayContexts.get(
			0);

		Assert.assertEquals(term, bucketDisplayContext.getBucketText());
		Assert.assertEquals(frequency, bucketDisplayContext.getFrequency());
		Assert.assertTrue(bucketDisplayContext.isSelected());
		Assert.assertTrue(bucketDisplayContext.isFrequencyVisible());

		Assert.assertEquals(
			facetParam, tagSearchFacetDisplayContext.getParameterValue());
		Assert.assertFalse(tagSearchFacetDisplayContext.isNothingSelected());
		Assert.assertFalse(tagSearchFacetDisplayContext.isRenderNothing());
	}

	protected TagSearchFacetDisplayContext createDisplayContext(
			String facetParam)
		throws ConfigurationException {

		TagSearchFacetDisplayContextBuilder
			tagSearchFacetDisplayContextBuilder =
				new TagSearchFacetDisplayContextBuilder(getRenderRequest());

		tagSearchFacetDisplayContextBuilder.setDisplayStyle("cloud");
		tagSearchFacetDisplayContextBuilder.setFacet(_facet);
		tagSearchFacetDisplayContextBuilder.setFrequenciesVisible(true);
		tagSearchFacetDisplayContextBuilder.setFrequencyThreshold(0);
		tagSearchFacetDisplayContextBuilder.setMaxTerms(0);
		tagSearchFacetDisplayContextBuilder.setParameterName(
			_facet.getFieldId());
		tagSearchFacetDisplayContextBuilder.setParameterValue(facetParam);

		return tagSearchFacetDisplayContextBuilder.build();
	}

	protected TermCollector createTermCollector(String term, int frequency) {
		TermCollector termCollector = Mockito.mock(TermCollector.class);

		Mockito.doReturn(
			frequency
		).when(
			termCollector
		).getFrequency();

		Mockito.doReturn(
			term
		).when(
			termCollector
		).getTerm();

		return termCollector;
	}

	protected PortletDisplay getPortletDisplay() throws ConfigurationException {
		PortletDisplay portletDisplay = Mockito.mock(PortletDisplay.class);

		Mockito.doReturn(
			Mockito.mock(TagFacetPortletInstanceConfiguration.class)
		).when(
			portletDisplay
		).getPortletInstanceConfiguration(
			Mockito.any()
		);

		return portletDisplay;
	}

	protected RenderRequest getRenderRequest() throws ConfigurationException {
		RenderRequest renderRequest = Mockito.mock(RenderRequest.class);

		Mockito.doReturn(
			getThemeDisplay()
		).when(
			renderRequest
		).getAttribute(
			WebKeys.THEME_DISPLAY
		);

		return renderRequest;
	}

	protected ThemeDisplay getThemeDisplay() throws ConfigurationException {
		ThemeDisplay themeDisplay = Mockito.mock(ThemeDisplay.class);

		Mockito.doReturn(
			getPortletDisplay()
		).when(
			themeDisplay
		).getPortletDisplay();

		return themeDisplay;
	}

	protected void setUpOneTermCollector(String facetParam, int frequency) {
		Mockito.doReturn(
			Collections.singletonList(
				createTermCollector(facetParam, frequency))
		).when(
			_facetCollector
		).getTermCollectors();
	}

	private final Facet _facet = Mockito.mock(Facet.class);
	private final FacetCollector _facetCollector = Mockito.mock(
		FacetCollector.class);

}