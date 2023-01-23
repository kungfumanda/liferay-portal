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
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.search.facet.collector.FacetCollector;
import com.liferay.portal.kernel.search.facet.collector.TermCollector;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.search.web.internal.facet.display.context.builder.ScopeSearchFacetDisplayContextBuilder;
import com.liferay.portal.search.web.internal.site.facet.configuration.SiteFacetPortletInstanceConfiguration;
import com.liferay.portal.search.web.internal.util.FacetDisplayContextTextUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author André de Oliveira
 */
public class ScopeSearchFacetDisplayContextTest {

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
		String parameterValue = "0";

		ScopeSearchFacetDisplayContext scopeSearchFacetDisplayContext =
			createDisplayContext(parameterValue);

		List<BucketDisplayContext> bucketDisplayContexts =
			scopeSearchFacetDisplayContext.getBucketDisplayContexts();

		Assert.assertEquals(
			bucketDisplayContexts.toString(), 0, bucketDisplayContexts.size());

		Assert.assertEquals(
			parameterValue, scopeSearchFacetDisplayContext.getParameterValue());
		Assert.assertTrue(scopeSearchFacetDisplayContext.isNothingSelected());
		Assert.assertTrue(scopeSearchFacetDisplayContext.isRenderNothing());
	}

	@Test
	public void testEmptySearchResultsWithPreviousSelection() throws Exception {
		long groupId = RandomTestUtil.randomLong();
		String name = RandomTestUtil.randomString();

		_addGroup(groupId, name);

		String parameterValue = String.valueOf(groupId);

		ScopeSearchFacetDisplayContext scopeSearchFacetDisplayContext =
			createDisplayContext(parameterValue);

		List<BucketDisplayContext> bucketDisplayContexts =
			scopeSearchFacetDisplayContext.getBucketDisplayContexts();

		Assert.assertEquals(
			bucketDisplayContexts.toString(), 1, bucketDisplayContexts.size());

		BucketDisplayContext bucketDisplayContext = bucketDisplayContexts.get(
			0);

		Assert.assertEquals(name, bucketDisplayContext.getBucketText());
		Assert.assertEquals(
			String.valueOf(groupId), bucketDisplayContext.getFilterValue());
		Assert.assertEquals(0, bucketDisplayContext.getFrequency());
		Assert.assertTrue(bucketDisplayContext.isFrequencyVisible());
		Assert.assertTrue(bucketDisplayContext.isSelected());

		Assert.assertEquals(
			parameterValue, scopeSearchFacetDisplayContext.getParameterValue());
		Assert.assertFalse(scopeSearchFacetDisplayContext.isNothingSelected());
		Assert.assertFalse(scopeSearchFacetDisplayContext.isRenderNothing());
	}

	@Test
	public void testOneTerm() throws Exception {
		long groupId = RandomTestUtil.randomLong();
		String name = RandomTestUtil.randomString();

		_addGroup(groupId, name);

		int count = RandomTestUtil.randomInt();

		FacetDisplayContextTextUtil.setUpOneTermCollector(
			_facetCollector, groupId, count);

		String parameterValue = "0";

		ScopeSearchFacetDisplayContext scopeSearchFacetDisplayContext =
			createDisplayContext(parameterValue);

		List<BucketDisplayContext> bucketDisplayContexts =
			scopeSearchFacetDisplayContext.getBucketDisplayContexts();

		Assert.assertEquals(
			bucketDisplayContexts.toString(), 1, bucketDisplayContexts.size());

		BucketDisplayContext bucketDisplayContext = bucketDisplayContexts.get(
			0);

		Assert.assertEquals(name, bucketDisplayContext.getBucketText());
		Assert.assertEquals(
			String.valueOf(groupId), bucketDisplayContext.getFilterValue());
		Assert.assertEquals(count, bucketDisplayContext.getFrequency());
		Assert.assertTrue(bucketDisplayContext.isFrequencyVisible());
		Assert.assertFalse(bucketDisplayContext.isSelected());

		Assert.assertEquals(
			parameterValue, scopeSearchFacetDisplayContext.getParameterValue());
		Assert.assertTrue(scopeSearchFacetDisplayContext.isNothingSelected());
		Assert.assertFalse(scopeSearchFacetDisplayContext.isRenderNothing());
	}

	@Test
	public void testOneTermWithPreviousSelection() throws Exception {
		long groupId = RandomTestUtil.randomLong();
		String name = RandomTestUtil.randomString();

		_addGroup(groupId, name);

		int count = RandomTestUtil.randomInt();

		FacetDisplayContextTextUtil.setUpOneTermCollector(
			_facetCollector, groupId, count);

		String parameterValue = String.valueOf(groupId);

		ScopeSearchFacetDisplayContext scopeSearchFacetDisplayContext =
			createDisplayContext(parameterValue);

		List<BucketDisplayContext> bucketDisplayContexts =
			scopeSearchFacetDisplayContext.getBucketDisplayContexts();

		Assert.assertEquals(
			bucketDisplayContexts.toString(), 1, bucketDisplayContexts.size());

		BucketDisplayContext bucketDisplayContext = bucketDisplayContexts.get(
			0);

		Assert.assertEquals(name, bucketDisplayContext.getBucketText());
		Assert.assertEquals(
			String.valueOf(groupId), bucketDisplayContext.getFilterValue());
		Assert.assertEquals(count, bucketDisplayContext.getFrequency());
		Assert.assertTrue(bucketDisplayContext.isFrequencyVisible());
		Assert.assertTrue(bucketDisplayContext.isSelected());

		Assert.assertEquals(
			parameterValue, scopeSearchFacetDisplayContext.getParameterValue());
		Assert.assertFalse(scopeSearchFacetDisplayContext.isNothingSelected());
		Assert.assertFalse(scopeSearchFacetDisplayContext.isRenderNothing());
	}

	@Test
	public void testOrderByTermFrequencyAscending() throws Exception {
		FacetDisplayContextTextUtil.setUpMultipleTermCollectors(
			_facetCollector,
			_getTermCollectors(
				new String[] {"able", "baker", "dog", "charlie"},
				new int[] {6, 5, 5, 3}));

		ScopeSearchFacetDisplayContext scopeSearchFacetDisplayContext =
			createDisplayContext(StringPool.BLANK, "count:asc");

		List<BucketDisplayContext> bucketDisplayContexts =
			scopeSearchFacetDisplayContext.getBucketDisplayContexts();

		String nameFrequencyString =
			FacetDisplayContextTextUtil.buildNameFrequencyString(
				bucketDisplayContexts);

		Assert.assertEquals(
			bucketDisplayContexts.toString(), "charlie:3|baker:5|dog:5|able:6",
			nameFrequencyString);
	}

	@Test
	public void testOrderByTermFrequencyDescending() throws Exception {
		FacetDisplayContextTextUtil.setUpMultipleTermCollectors(
			_facetCollector,
			_getTermCollectors(
				new String[] {"able", "dog", "baker", "charlie"},
				new int[] {4, 5, 5, 6}));

		ScopeSearchFacetDisplayContext scopeSearchFacetDisplayContext =
			createDisplayContext(StringPool.BLANK, "count:desc");

		List<BucketDisplayContext> bucketDisplayContexts =
			scopeSearchFacetDisplayContext.getBucketDisplayContexts();

		String nameFrequencyString =
			FacetDisplayContextTextUtil.buildNameFrequencyString(
				bucketDisplayContexts);

		Assert.assertEquals(
			bucketDisplayContexts.toString(), "charlie:6|baker:5|dog:5|able:4",
			nameFrequencyString);
	}

	@Test
	public void testOrderByTermValueAscending() throws Exception {
		FacetDisplayContextTextUtil.setUpMultipleTermCollectors(
			_facetCollector,
			_getTermCollectors(
				new String[] {"baker", "dog", "able", "baker"},
				new int[] {6, 5, 4, 3}));

		ScopeSearchFacetDisplayContext scopeSearchFacetDisplayContext =
			createDisplayContext(StringPool.BLANK, "key:asc");

		List<BucketDisplayContext> bucketDisplayContexts =
			scopeSearchFacetDisplayContext.getBucketDisplayContexts();

		String nameFrequencyString =
			FacetDisplayContextTextUtil.buildNameFrequencyString(
				bucketDisplayContexts);

		Assert.assertEquals(
			bucketDisplayContexts.toString(), "able:4|baker:6|baker:3|dog:5",
			nameFrequencyString);
	}

	@Test
	public void testOrderByTermValueDescending() throws Exception {
		FacetDisplayContextTextUtil.setUpMultipleTermCollectors(
			_facetCollector,
			_getTermCollectors(
				new String[] {"baker", "dog", "able", "dog"},
				new int[] {3, 4, 5, 6}));

		ScopeSearchFacetDisplayContext scopeSearchFacetDisplayContext =
			createDisplayContext(StringPool.BLANK, "key:desc");

		List<BucketDisplayContext> bucketDisplayContexts =
			scopeSearchFacetDisplayContext.getBucketDisplayContexts();

		String nameFrequencyString =
			FacetDisplayContextTextUtil.buildNameFrequencyString(
				bucketDisplayContexts);

		Assert.assertEquals(
			bucketDisplayContexts.toString(), "dog:6|dog:4|baker:3|able:5",
			nameFrequencyString);
	}

	protected ScopeSearchFacetDisplayContext createDisplayContext(
			String parameterValue)
		throws ConfigurationException {

		return createDisplayContext(parameterValue, "count:desc");
	}

	protected ScopeSearchFacetDisplayContext createDisplayContext(
			String parameterValue, String order)
		throws ConfigurationException {

		ScopeSearchFacetDisplayContextBuilder
			scopeSearchFacetDisplayContextBuilder =
				new ScopeSearchFacetDisplayContextBuilder(
					FacetDisplayContextTextUtil.getRenderRequest(
						SiteFacetPortletInstanceConfiguration.class));

		scopeSearchFacetDisplayContextBuilder.setFacet(_facet);
		scopeSearchFacetDisplayContextBuilder.setFrequenciesVisible(true);
		scopeSearchFacetDisplayContextBuilder.setGroupLocalService(
			_groupLocalService);
		scopeSearchFacetDisplayContextBuilder.setOrder(order);
		scopeSearchFacetDisplayContextBuilder.setParameterValue(parameterValue);

		return scopeSearchFacetDisplayContextBuilder.build();
	}

	protected Group createGroup(long groupId, String name) throws Exception {
		Group group = Mockito.mock(Group.class);

		Mockito.doReturn(
			name
		).when(
			group
		).getDescriptiveName(
			Mockito.<Locale>any()
		);

		Mockito.doReturn(
			groupId
		).when(
			group
		).getGroupId();

		return group;
	}

	private void _addGroup(long groupId, String name) throws Exception {
		Mockito.doReturn(
			createGroup(groupId, name)
		).when(
			_groupLocalService
		).fetchGroup(
			groupId
		);
	}

	private List<TermCollector> _getTermCollectors(
			String[] groupNames, int[] frequencies)
		throws Exception {

		List<TermCollector> termCollectors = new ArrayList<>();

		for (int i = 1; i <= groupNames.length; i++) {
			_addGroup(i, groupNames[i - 1]);

			termCollectors.add(
				FacetDisplayContextTextUtil.createTermCollector(
					String.valueOf(i), frequencies[i - 1]));
		}

		return termCollectors;
	}

	private final Facet _facet = Mockito.mock(Facet.class);
	private final FacetCollector _facetCollector = Mockito.mock(
		FacetCollector.class);
	private final GroupLocalService _groupLocalService = Mockito.mock(
		GroupLocalService.class);

}