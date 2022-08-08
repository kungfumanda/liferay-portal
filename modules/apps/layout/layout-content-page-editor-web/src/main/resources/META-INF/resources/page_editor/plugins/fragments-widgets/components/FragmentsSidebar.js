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

import {ClayButtonWithIcon} from '@clayui/button';
import {debounce} from 'frontend-js-web';
import React, {
	useEffect,
	useLayoutEffect,
	useMemo,
	useRef,
	useState,
} from 'react';

import {FRAGMENTS_DISPLAY_STYLES} from '../../../app/config/constants/fragmentsDisplayStyles';
import {LAYOUT_DATA_ITEM_TYPES} from '../../../app/config/constants/layoutDataItemTypes';
import {config} from '../../../app/config/index';
import {useSelector} from '../../../app/contexts/StoreContext';
import {useWidgets} from '../../../app/contexts/WidgetsContext';
import SearchForm from '../../../common/components/SearchForm';
import SidebarPanelContent from '../../../common/components/SidebarPanelContent';
import SidebarPanelHeader from '../../../common/components/SidebarPanelHeader';
import SearchResultsPanel from './SearchResultsPanel';
import TabsPanel from './TabsPanel';

const FRAGMENTS_DISPLAY_STYLE_KEY = 'FRAGMENTS_DISPLAY_STYLE_KEY';

export const COLLECTION_IDS = {
	fragments: 'fragments',
	widgets: 'widgets',
};

const collectionFilter = (collections, searchValue) => {
	const searchValueLowerCase = searchValue.toLowerCase();

	const itemFilter = (item) =>
		item.label.toLowerCase().indexOf(searchValueLowerCase) !== -1;

	const hasChildren = (collection) => {
		if (collection.children?.length) {
			return true;
		}

		return collection.collections?.some(hasChildren) ?? false;
	};

	return collections
		.reduce((acc, collection) => {
			if (itemFilter(collection)) {
				return [...acc, collection];
			}
			else {
				const updateCollection = {
					...collection,
					children: collection.children?.filter(
						(item) =>
							itemFilter(item) ||
							item.portletItems?.some(itemFilter)
					),
					...(collection.collections?.length && {
						collections: collectionFilter(
							collection.collections,
							searchValueLowerCase
						),
					}),
				};

				return [...acc, updateCollection];
			}
		}, [])
		.filter(hasChildren);
};

const normalizeWidget = (widget) => {
	return {
		data: {
			instanceable: widget.instanceable,
			portletId: widget.portletId,
			portletItemId: widget.portletItemId || null,
			used: widget.used,
		},
		disabled: !widget.instanceable && widget.used,
		icon: widget.instanceable ? 'square-hole-multi' : 'square-hole',
		itemId: widget.portletId,
		label: widget.title,
		portletItems: widget.portletItems?.length
			? widget.portletItems.map(normalizeWidget)
			: null,
		preview: '',
		type: LAYOUT_DATA_ITEM_TYPES.fragment,
	};
};

const normalizeCollections = (collection) => {
	const normalizedElement = {
		children: collection.portlets.map(normalizeWidget),
		collectionId: collection.path,
		label: collection.title,
	};

	if (collection.categories?.length) {
		normalizedElement.collections = collection.categories.map(
			normalizeCollections
		);
	}

	return normalizedElement;
};

const normalizeFragmentEntry = (fragmentEntry) => {
	if (!fragmentEntry.fragmentEntryKey) {
		return fragmentEntry;
	}

	return {
		data: {
			fragmentEntryKey: fragmentEntry.fragmentEntryKey,
			groupId: fragmentEntry.groupId,
			type: fragmentEntry.type,
		},
		icon: fragmentEntry.icon,
		itemId: fragmentEntry.fragmentEntryKey,
		label: fragmentEntry.name,
		preview: fragmentEntry.imagePreviewURL,
		type: LAYOUT_DATA_ITEM_TYPES.fragment,
	};
};

export default function FragmentsSidebar() {
	const activeTabIdSessionKey = `${config.portletNamespace}_fragments-sidebar_active-tab-id`;

	const fragments = useSelector((state) => state.fragments);
	const widgets = useWidgets();
	const wrapperElementRef = useRef(null);

	const [activeTabId, setActiveTabId] = useState(() => {
		const persistedTabId = window.sessionStorage.getItem(
			activeTabIdSessionKey
		);

		if (
			Liferay.FeatureFlags['LPS-153452'] &&
			Object.values(COLLECTION_IDS).includes(persistedTabId)
		) {
			return persistedTabId;
		}

		return COLLECTION_IDS.fragments;
	});

	const updateActiveTabId = (nextTabId) => {
		window.sessionStorage.setItem(activeTabIdSessionKey, nextTabId);
		setActiveTabId(nextTabId);
	};

	const [displayStyle, setDisplayStyle] = useState(
		window.sessionStorage.getItem(FRAGMENTS_DISPLAY_STYLE_KEY) ||
			FRAGMENTS_DISPLAY_STYLES.LIST
	);

	const [searchValue, setSearchValue] = useState('');

	const scrollPositionSessionKey = `${config.portletNamespace}_fragments-sidebar_tab_${activeTabId}_scroll-position`;

	const tabs = useMemo(
		() => [
			{
				collections: fragments.map((collection) => ({
					children: collection.fragmentEntries.map((fragmentEntry) =>
						normalizeFragmentEntry(fragmentEntry)
					),
					collectionId: collection.fragmentCollectionId,
					label: collection.name,
				})),
				id: COLLECTION_IDS.fragments,
				label: Liferay.Language.get('fragments'),
			},
			{
				collections: widgets.map((collection) =>
					normalizeCollections(collection)
				),
				id: COLLECTION_IDS.widgets,
				label: Liferay.Language.get('widgets'),
			},
		],
		[fragments, widgets]
	);

	const filteredTabs = useMemo(
		() =>
			searchValue
				? tabs
						.map((tab) => ({
							...tab,
							collections: collectionFilter(
								tab.collections,
								searchValue
							),
						}))
						.filter((item) => item.collections.length)
				: tabs,
		[tabs, searchValue]
	);

	const displayStyleButtonDisabled =
		searchValue || activeTabId === COLLECTION_IDS.widgets;

	useLayoutEffect(() => {
		const wrapperElement = wrapperElementRef.current;

		if (!wrapperElement) {
			return;
		}

		const initialScrollPosition = parseInt(
			window.sessionStorage.getItem(scrollPositionSessionKey),
			10
		);

		if (
			Liferay.FeatureFlags['LPS-153452'] &&
			!isNaN(initialScrollPosition)
		) {
			wrapperElement.scrollBy({
				behavior: 'auto',
				left: 0,
				top: initialScrollPosition,
			});
		}

		// LPS-157780 We do not want to depend on scrollPositionSessionKey here
		// because we only want to apply the persisted scroll when the panel
		// is initially opened.

		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, []);

	useEffect(() => {
		const wrapperElement = wrapperElementRef.current;

		if (!wrapperElement) {
			return;
		}

		const handleScroll = debounce(() => {
			window.sessionStorage.setItem(
				scrollPositionSessionKey,
				wrapperElement.scrollTop
			);
		}, 300);

		wrapperElement.addEventListener('scroll', handleScroll, {
			passive: true,
		});

		return () => {
			wrapperElement.removeEventListener('scroll', handleScroll);
		};
	}, [scrollPositionSessionKey]);

	return (
		<div className="h-100 overflow-auto" ref={wrapperElementRef}>
			<SidebarPanelHeader>
				{Liferay.Language.get('fragments-and-widgets')}
			</SidebarPanelHeader>

			<SidebarPanelContent className="page-editor__sidebar__fragments-widgets-panel">
				<div className="align-items-center d-flex justify-content-between mb-3">
					<SearchForm
						className="flex-grow-1 mb-0"
						onChange={setSearchValue}
					/>

					<ClayButtonWithIcon
						borderless
						className="lfr-portal-tooltip ml-2 mt-0"
						data-tooltip-align="bottom-right"
						disabled={displayStyleButtonDisabled}
						displayType="secondary"
						onClick={() => {
							const nextDisplayStyle =
								displayStyle === FRAGMENTS_DISPLAY_STYLES.LIST
									? FRAGMENTS_DISPLAY_STYLES.CARDS
									: FRAGMENTS_DISPLAY_STYLES.LIST;

							setDisplayStyle(nextDisplayStyle);

							window.sessionStorage.setItem(
								FRAGMENTS_DISPLAY_STYLE_KEY,
								nextDisplayStyle
							);
						}}
						small
						symbol={
							displayStyleButtonDisabled ||
							displayStyle === FRAGMENTS_DISPLAY_STYLES.LIST
								? 'cards2'
								: 'list'
						}
						title={Liferay.Util.sub(
							Liferay.Language.get('switch-to-x-view'),
							displayStyle === FRAGMENTS_DISPLAY_STYLES.LIST
								? Liferay.Language.get('card')
								: Liferay.Language.get('list')
						)}
					/>
				</div>

				{searchValue ? (
					<SearchResultsPanel filteredTabs={filteredTabs} />
				) : (
					<TabsPanel
						activeTabId={activeTabId}
						displayStyle={displayStyle}
						setActiveTabId={updateActiveTabId}
						tabs={tabs}
					/>
				)}
			</SidebarPanelContent>
		</div>
	);
}
