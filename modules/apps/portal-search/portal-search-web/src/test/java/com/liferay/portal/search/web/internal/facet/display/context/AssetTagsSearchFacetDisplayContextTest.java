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
import com.liferay.portal.search.web.internal.facet.display.context.builder.AssetTagsSearchFacetDisplayContextBuilder;
import com.liferay.portal.search.web.internal.tag.facet.configuration.TagFacetPortletInstanceConfiguration;
import com.liferay.portal.search.web.internal.util.FacetDisplayContextTextUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author André de Oliveira
 */
public class AssetTagsSearchFacetDisplayContextTest {

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

		AssetTagsSearchFacetDisplayContext assetTagsSearchFacetDisplayContext =
			createDisplayContext(facetParam);

		List<BucketDisplayContext> bucketDisplayContexts =
			assetTagsSearchFacetDisplayContext.getBucketDisplayContexts();

		Assert.assertEquals(
			bucketDisplayContexts.toString(), 0, bucketDisplayContexts.size());

		Assert.assertEquals(
			facetParam, assetTagsSearchFacetDisplayContext.getParameterValue());
		Assert.assertTrue(
			assetTagsSearchFacetDisplayContext.isNothingSelected());
		Assert.assertTrue(assetTagsSearchFacetDisplayContext.isRenderNothing());
	}

	@Test
	public void testEmptySearchResultsWithPreviousSelection() throws Exception {
		String term = RandomTestUtil.randomString();

		String facetParam = term;

		AssetTagsSearchFacetDisplayContext assetTagsSearchFacetDisplayContext =
			createDisplayContext(facetParam);

		List<BucketDisplayContext> bucketDisplayContexts =
			assetTagsSearchFacetDisplayContext.getBucketDisplayContexts();

		Assert.assertEquals(
			bucketDisplayContexts.toString(), 1, bucketDisplayContexts.size());

		BucketDisplayContext bucketDisplayContext = bucketDisplayContexts.get(
			0);

		Assert.assertEquals(term, bucketDisplayContext.getBucketText());
		Assert.assertEquals(term, bucketDisplayContext.getFilterValue());
		Assert.assertEquals(0, bucketDisplayContext.getFrequency());
		Assert.assertTrue(bucketDisplayContext.isSelected());
		Assert.assertTrue(bucketDisplayContext.isFrequencyVisible());

		Assert.assertEquals(
			facetParam, assetTagsSearchFacetDisplayContext.getParameterValue());
		Assert.assertFalse(
			assetTagsSearchFacetDisplayContext.isNothingSelected());
		Assert.assertFalse(
			assetTagsSearchFacetDisplayContext.isRenderNothing());
	}

	@Test
	public void testOneTerm() throws Exception {
		String term = RandomTestUtil.randomString();
		int frequency = RandomTestUtil.randomInt();

		FacetDisplayContextTextUtil.setUpOneTermCollector(
			_facetCollector, term, frequency);

		String facetParam = StringPool.BLANK;

		AssetTagsSearchFacetDisplayContext assetTagsSearchFacetDisplayContext =
			createDisplayContext(facetParam);

		List<BucketDisplayContext> bucketDisplayContexts =
			assetTagsSearchFacetDisplayContext.getBucketDisplayContexts();

		Assert.assertEquals(
			bucketDisplayContexts.toString(), 1, bucketDisplayContexts.size());

		BucketDisplayContext bucketDisplayContext = bucketDisplayContexts.get(
			0);

		Assert.assertEquals(term, bucketDisplayContext.getBucketText());
		Assert.assertEquals(term, bucketDisplayContext.getFilterValue());
		Assert.assertEquals(frequency, bucketDisplayContext.getFrequency());
		Assert.assertFalse(bucketDisplayContext.isSelected());
		Assert.assertTrue(bucketDisplayContext.isFrequencyVisible());

		Assert.assertEquals(
			facetParam, assetTagsSearchFacetDisplayContext.getParameterValue());
		Assert.assertTrue(
			assetTagsSearchFacetDisplayContext.isNothingSelected());
		Assert.assertFalse(
			assetTagsSearchFacetDisplayContext.isRenderNothing());
	}

	@Test
	public void testOneTermWithPreviousSelection() throws Exception {
		String term = RandomTestUtil.randomString();
		int frequency = RandomTestUtil.randomInt();

		FacetDisplayContextTextUtil.setUpOneTermCollector(
			_facetCollector, term, frequency);

		String facetParam = term;

		AssetTagsSearchFacetDisplayContext assetTagsSearchFacetDisplayContext =
			createDisplayContext(facetParam);

		List<BucketDisplayContext> bucketDisplayContexts =
			assetTagsSearchFacetDisplayContext.getBucketDisplayContexts();

		Assert.assertEquals(
			bucketDisplayContexts.toString(), 1, bucketDisplayContexts.size());

		BucketDisplayContext bucketDisplayContext = bucketDisplayContexts.get(
			0);

		Assert.assertEquals(term, bucketDisplayContext.getBucketText());
		Assert.assertEquals(term, bucketDisplayContext.getFilterValue());
		Assert.assertEquals(frequency, bucketDisplayContext.getFrequency());
		Assert.assertTrue(bucketDisplayContext.isSelected());
		Assert.assertTrue(bucketDisplayContext.isFrequencyVisible());

		Assert.assertEquals(
			facetParam, assetTagsSearchFacetDisplayContext.getParameterValue());
		Assert.assertFalse(
			assetTagsSearchFacetDisplayContext.isNothingSelected());
		Assert.assertFalse(
			assetTagsSearchFacetDisplayContext.isRenderNothing());
	}

	@Test
	public void testOrderByTermFrequencyAscending() throws Exception {
		List<TermCollector> termCollectors2 =
			FacetDisplayContextTextUtil.getTermCollectors(
				new String[] {"alpha", "delta", "bravo", "charlie"},
				new int[] {4, 5, 5, 6});

		FacetDisplayContextTextUtil.setUpMultipleTermCollectors(
			_facetCollector, termCollectors2);

		AssetTagsSearchFacetDisplayContext assetTagsSearchFacetDisplayContext2 =
			createDisplayContext(StringPool.BLANK, "count:asc");

		List<BucketDisplayContext> bucketDisplayContexts2 =
			assetTagsSearchFacetDisplayContext2.getBucketDisplayContexts();

		String nameFrequencyString2 =
			FacetDisplayContextTextUtil.buildNameFrequencyString(
				bucketDisplayContexts2);

		Assert.assertEquals(
			bucketDisplayContexts2.toString(),
			"alpha:4|bravo:5|delta:5|charlie:6", nameFrequencyString2);
	}

	@Test
	public void testOrderByTermFrequencyDescending() throws Exception {
		List<TermCollector> termCollectors2 =
			FacetDisplayContextTextUtil.getTermCollectors(
				new String[] {"alpha", "delta", "bravo", "charlie"},
				new int[] {4, 5, 5, 6});

		FacetDisplayContextTextUtil.setUpMultipleTermCollectors(
			_facetCollector, termCollectors2);

		AssetTagsSearchFacetDisplayContext assetTagsSearchFacetDisplayContext2 =
			createDisplayContext(StringPool.BLANK, "count:desc");

		List<BucketDisplayContext> bucketDisplayContexts2 =
			assetTagsSearchFacetDisplayContext2.getBucketDisplayContexts();

		String nameFrequencyString2 =
			FacetDisplayContextTextUtil.buildNameFrequencyString(
				bucketDisplayContexts2);

		Assert.assertEquals(
			bucketDisplayContexts2.toString(),
			"charlie:6|bravo:5|delta:5|alpha:4", nameFrequencyString2);
	}

	@Test
	public void testOrderByTermValueAscending() throws Exception {
		List<TermCollector> termCollectors1 =
			FacetDisplayContextTextUtil.getTermCollectors(
				new String[] {"bravo", "delta", "alpha", "alpha"},
				new int[] {6, 5, 4, 3});

		FacetDisplayContextTextUtil.setUpMultipleTermCollectors(
			_facetCollector, termCollectors1);

		AssetTagsSearchFacetDisplayContext assetTagsSearchFacetDisplayContext1 =
			createDisplayContext(StringPool.BLANK, "key:asc");

		List<BucketDisplayContext> bucketDisplayContexts1 =
			assetTagsSearchFacetDisplayContext1.getBucketDisplayContexts();

		String nameFrequencyString1 =
			FacetDisplayContextTextUtil.buildNameFrequencyString(
				bucketDisplayContexts1);

		Assert.assertEquals(
			bucketDisplayContexts1.toString(),
			"alpha:4|alpha:3|bravo:6|delta:5", nameFrequencyString1);
	}

	@Test
	public void testOrderByTermValueDescending() throws Exception {
		List<TermCollector> termCollectors1 =
			FacetDisplayContextTextUtil.getTermCollectors(
				new String[] {"bravo", "alpha", "alpha", "charlie"},
				new int[] {3, 4, 5, 6});

		FacetDisplayContextTextUtil.setUpMultipleTermCollectors(
			_facetCollector, termCollectors1);

		AssetTagsSearchFacetDisplayContext assetTagsSearchFacetDisplayContext1 =
			createDisplayContext(StringPool.BLANK, "key:desc");

		List<BucketDisplayContext> bucketDisplayContexts1 =
			assetTagsSearchFacetDisplayContext1.getBucketDisplayContexts();

		String nameFrequencyString1 =
			FacetDisplayContextTextUtil.buildNameFrequencyString(
				bucketDisplayContexts1);

		Assert.assertEquals(
			bucketDisplayContexts1.toString(),
			"charlie:6|bravo:3|alpha:5|alpha:4", nameFrequencyString1);
	}

	protected AssetTagsSearchFacetDisplayContext createDisplayContext(
			String facetParam)
		throws Exception {

		return createDisplayContext(facetParam, "count:desc");
	}

	protected AssetTagsSearchFacetDisplayContext createDisplayContext(
			String facetParam, String order)
		throws ConfigurationException {

		AssetTagsSearchFacetDisplayContextBuilder
			assetTagsSearchFacetDisplayContextBuilder =
				new AssetTagsSearchFacetDisplayContextBuilder(
					FacetDisplayContextTextUtil.getRenderRequest(
						TagFacetPortletInstanceConfiguration.class));

		assetTagsSearchFacetDisplayContextBuilder.setDisplayStyle("cloud");
		assetTagsSearchFacetDisplayContextBuilder.setFacet(_facet);
		assetTagsSearchFacetDisplayContextBuilder.setFrequenciesVisible(true);
		assetTagsSearchFacetDisplayContextBuilder.setFrequencyThreshold(0);
		assetTagsSearchFacetDisplayContextBuilder.setMaxTerms(0);
		assetTagsSearchFacetDisplayContextBuilder.setOrder(order);
		assetTagsSearchFacetDisplayContextBuilder.setParameterName(
			_facet.getFieldId());
		assetTagsSearchFacetDisplayContextBuilder.setParameterValue(facetParam);

		return assetTagsSearchFacetDisplayContextBuilder.build();
	}

	private final Facet _facet = Mockito.mock(Facet.class);
	private final FacetCollector _facetCollector = Mockito.mock(
		FacetCollector.class);

}