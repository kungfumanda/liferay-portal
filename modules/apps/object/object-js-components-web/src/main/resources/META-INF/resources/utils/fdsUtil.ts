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

export function onActionDropdownItemClick<T>({
	action,
	event,
	itemData,
}: {
	action: FDSAction;
	event: Event;
	itemData: T;
}) {
	event.preventDefault();

	if (action.target === 'event') {
		Liferay.fire(action.id, {itemData});
	}
}

interface FDSAction {
	id: string;
	target: 'event' | 'async';
}
