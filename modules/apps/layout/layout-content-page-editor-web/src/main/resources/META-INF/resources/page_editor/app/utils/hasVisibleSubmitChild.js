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

import getLayoutDataItemUniqueClassName from './getLayoutDataItemUniqueClassName';
import isVisible from './isVisible';

export default function hasVisibleSubmitChild(itemId, globalContext) {
	const element = document.querySelector(
		`.${getLayoutDataItemUniqueClassName(itemId)}`
	);

	return Array.from(
		element.querySelectorAll(
			'input[type=submit], button[type=submit], button:not([type])'
		)
	).some((buttonElement) => isVisible(buttonElement, globalContext));
}
