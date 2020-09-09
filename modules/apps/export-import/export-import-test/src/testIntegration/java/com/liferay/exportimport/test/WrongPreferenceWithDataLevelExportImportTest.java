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

package com.liferay.exportimport.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.exportimport.kernel.lar.BasePortletDataHandler;
import com.liferay.exportimport.kernel.lar.PortletDataException;
import com.liferay.exportimport.kernel.lar.PortletDataHandler;
import com.liferay.exportimport.test.util.lar.BaseExportImportTestCase;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import javax.portlet.Portlet;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Arthur Chan
 */
@RunWith(Arquillian.class)
public class WrongPreferenceWithDataLevelExportImportTest
	extends BaseExportImportTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	@Override
	public void setUp() throws Exception {
		UserTestUtil.setUser(TestPropsValues.getUser());
		super.setUp();
	}

	@Test(expected = PortletDataException.class)
	public void testExportLayoutPortlets() throws Exception {
		Bundle bundle = FrameworkUtil.getBundle(
			WrongPreferenceWithDataLevelExportImportTest.class);

		BundleContext bundleContext = bundle.getBundleContext();

		HashMapDictionary<String, String> portletProperties =
			new HashMapDictionary<>();

		portletProperties.put("javax.portlet.name", _PORTLET_NAME);

		ServiceRegistration<Portlet> portletServiceRegistration =
			bundleContext.registerService(
				Portlet.class, new MVCPortlet(), portletProperties);

		ServiceRegistration<PortletDataHandler>
			portletDataHandlerServiceRegistration =
				bundleContext.registerService(
					PortletDataHandler.class,
					new BasePortletDataHandler() {
					},
					portletProperties);

		try {
			exportLayouts(
				new long[] {layout.getLayoutId()}, getExportParameterMap());
		}
		catch (PortletDataException portletDataException) {
			throw portletDataException;
		}
		finally {
			portletServiceRegistration.unregister();
			portletDataHandlerServiceRegistration.unregister();
		}
	}

	private static final String _PORTLET_NAME =
		"com_liferay_exportimport_test_WrongPreferenceWithDataLevelPortlet";

}