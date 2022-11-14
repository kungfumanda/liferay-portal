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

import React from 'react';
import {TFilter} from '../../utils/filter';
import {TPagination} from '../../utils/pagination';
import {TItem} from './Table';
export declare enum Events {
	ChangeFilter = 'CHANGE_FILTER',
	ChangeItems = 'CHANGE_ITEMS',
	ChangeKeywords = 'CHANGE_KEYWORDS',
	ChangePagination = 'CHANGE_PAGINATION',
	FormatData = 'FORMAT_DATA',
	ToggleCheckbox = 'TOGGLE_CHECKBOX',
}
declare type TState = {
	checked: boolean;
	filter: TFilter;
	internalKeywords: string;
	items: TItem[];
	keywords: string;
	pagination: TPagination;
};
declare const useData: () => TState;
declare const useDispatch: () => any;
declare const TableContext: React.FC;
export {useData, useDispatch};
export default TableContext;
