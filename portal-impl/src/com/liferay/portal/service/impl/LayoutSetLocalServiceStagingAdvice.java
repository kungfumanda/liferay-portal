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

package com.liferay.portal.service.impl;

import com.liferay.exportimport.kernel.staging.LayoutStagingUtil;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.model.LayoutSetStagingHandler;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.service.BaseLocalService;
import com.liferay.portal.kernel.service.LayoutSetLocalService;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.spring.aop.AopInvocationHandler;
import com.liferay.portlet.exportimport.staging.StagingAdvicesThreadLocal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Julio Camarero
 * @author Brian Wing Shun Chan
 * @author Raymond Augé
 */
public class LayoutSetLocalServiceStagingAdvice {

	public void afterPropertiesSet() {
		AopInvocationHandler aopInvocationHandler =
			ProxyUtil.fetchInvocationHandler(
				_layoutSetLocalService, AopInvocationHandler.class);

		aopInvocationHandler.setTarget(
			ProxyUtil.newProxyInstance(
				LayoutSetLocalServiceStagingAdvice.class.getClassLoader(),
				new Class<?>[] {
					IdentifiableOSGiService.class, LayoutSetLocalService.class,
					BaseLocalService.class
				},
				new LayoutSetLocalServiceStagingInvocationHandler(
					aopInvocationHandler.getTarget())));
	}

	protected LayoutSet wrapLayoutSet(LayoutSet layoutSet) {
		try {
			if (!LayoutStagingUtil.isBranchingLayoutSet(
					layoutSet.getGroup(), layoutSet.isPrivateLayout())) {

				return layoutSet;
			}
		}
		catch (PortalException portalException) {

			// LPS-52675

			if (_log.isDebugEnabled()) {
				_log.debug(portalException);
			}

			return layoutSet;
		}

		return LayoutSetStagingHandler.newProxyInstance(
			new LayoutSetStagingHandler(layoutSet));
	}

	protected List<LayoutSet> wrapLayoutSets(List<LayoutSet> layoutSets) {
		if (layoutSets.isEmpty()) {
			return layoutSets;
		}

		List<LayoutSet> wrappedLayoutSets = new ArrayList<>(layoutSets.size());

		for (LayoutSet layoutSet : layoutSets) {
			wrappedLayoutSets.add(wrapLayoutSet(layoutSet));
		}

		return wrappedLayoutSets;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LayoutSetLocalServiceStagingAdvice.class);

	@BeanReference(type = LayoutSetLocalService.class)
	private LayoutSetLocalService _layoutSetLocalService;

	private class LayoutSetLocalServiceStagingInvocationHandler
		implements InvocationHandler {

		@Override
		public Object invoke(Object proxy, Method method, Object[] arguments)
			throws Throwable {

			try {
				Object returnValue = method.invoke(_targetObject, arguments);

				if (!StagingAdvicesThreadLocal.isEnabled()) {
					return returnValue;
				}

				if (returnValue instanceof LayoutSet) {
					return wrapLayoutSet((LayoutSet)returnValue);
				}

				if (returnValue instanceof List<?>) {
					List<?> list = (List<?>)returnValue;

					if (!list.isEmpty() && (list.get(0) instanceof LayoutSet)) {
						returnValue = wrapLayoutSets(
							(List<LayoutSet>)returnValue);
					}
				}

				return returnValue;
			}
			catch (InvocationTargetException invocationTargetException) {
				throw invocationTargetException.getCause();
			}
		}

		private LayoutSetLocalServiceStagingInvocationHandler(
			Object targetObject) {

			_targetObject = targetObject;
		}

		private final Object _targetObject;

	}

}