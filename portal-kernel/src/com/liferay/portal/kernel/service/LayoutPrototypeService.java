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

package com.liferay.portal.kernel.service;

import com.liferay.portal.kernel.change.tracking.CTAware;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.jsonwebservice.JSONWebService;
import com.liferay.portal.kernel.model.LayoutPrototype;
import com.liferay.portal.kernel.security.access.control.AccessControlled;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Provides the remote service interface for LayoutPrototype. Methods of this
 * service are expected to have security checks based on the propagated JAAS
 * credentials because this service can be accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see LayoutPrototypeServiceUtil
 * @generated
 */
@AccessControlled
@CTAware
@JSONWebService
@ProviderType
@Transactional(
	isolation = Isolation.PORTAL,
	rollbackFor = {PortalException.class, SystemException.class}
)
public interface LayoutPrototypeService extends BaseService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add custom service methods to <code>com.liferay.portal.service.impl.LayoutPrototypeServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface. Consume the layout prototype remote service via injection or a <code>org.osgi.util.tracker.ServiceTracker</code>. Use {@link LayoutPrototypeServiceUtil} if injection and service tracking are not available.
	 */
	public LayoutPrototype addLayoutPrototype(
			Map<Locale, String> nameMap, Map<Locale, String> descriptionMap,
			boolean active, ServiceContext serviceContext)
		throws PortalException;

	public void deleteLayoutPrototype(long layoutPrototypeId)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public LayoutPrototype fetchLayoutPrototype(long layoutPrototypeId)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public LayoutPrototype getLayoutPrototype(long layoutPrototypeId)
		throws PortalException;

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public String getOSGiServiceIdentifier();

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<LayoutPrototype> search(
			long companyId, Boolean active,
			OrderByComparator<LayoutPrototype> orderByComparator)
		throws PortalException;

	public LayoutPrototype updateLayoutPrototype(
			long layoutPrototypeId, Map<Locale, String> nameMap,
			Map<Locale, String> descriptionMap, boolean active,
			ServiceContext serviceContext)
		throws PortalException;

}