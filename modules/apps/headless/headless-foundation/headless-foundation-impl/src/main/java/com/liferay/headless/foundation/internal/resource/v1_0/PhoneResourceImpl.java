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

package com.liferay.headless.foundation.internal.resource.v1_0;

import com.liferay.headless.foundation.dto.v1_0.Phone;
import com.liferay.headless.foundation.resource.v1_0.PhoneResource;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Contact;
import com.liferay.portal.kernel.model.ListType;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.OrganizationService;
import com.liferay.portal.kernel.service.PhoneService;
import com.liferay.portal.kernel.service.UserService;
import com.liferay.portal.vulcan.identifier.ClassNameClassPK;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Javier Gamarra
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/phone.properties",
	scope = ServiceScope.PROTOTYPE, service = PhoneResource.class
)
public class PhoneResourceImpl extends BasePhoneResourceImpl {

	@Override
	public Phone getPhone(Long phoneId) throws Exception {
		return _toPhone(_phoneService.getPhone(phoneId));
	}

	@Override
	public Page<Phone> getPhonesByClassNameClassPK(
			ClassNameClassPK classNameClassPK, Pagination pagination)
		throws Exception {

		return Page.of(transform(_getPhones(classNameClassPK), this::_toPhone));
	}

	private List<com.liferay.portal.kernel.model.Phone> _getPhones(
			ClassNameClassPK classNameClassPK)
		throws PortalException {

		String className = classNameClassPK.getClassName();

		if (className.equals(Organization.class.getName())) {
			Organization organization = _organizationService.getOrganization(
				classNameClassPK.getClassPK());

			return _phoneService.getPhones(
				organization.getModelClassName(),
				organization.getOrganizationId());
		}

		User user = _userService.getUserById(classNameClassPK.getClassPK());

		return _phoneService.getPhones(
			Contact.class.getName(), user.getContactId());
	}

	private Phone _toPhone(com.liferay.portal.kernel.model.Phone phone)
		throws PortalException {

		ListType listType = phone.getType();

		return new Phone() {
			{
				extension = phone.getExtension();
				id = phone.getPhoneId();
				phoneNumber = phone.getNumber();
				phoneType = listType.getName();
			}
		};
	}

	@Reference
	private OrganizationService _organizationService;

	@Reference
	private PhoneService _phoneService;

	@Reference
	private UserService _userService;

}