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

import ClayForm, {ClayCheckbox, ClaySelectWithOption} from '@clayui/form';
import PropTypes from 'prop-types';
import React from 'react';

import {getLayoutDataItemPropTypes} from '../../../prop-types/index';
import {VIEWPORT_SIZES} from '../../config/constants/viewportSizes';
import selectSegmentsExperienceId from '../../selectors/selectSegmentsExperienceId';
import {useDispatch, useSelector} from '../../store/index';
import updateItemConfig from '../../thunks/updateItemConfig';
import updateRowColumns from '../../thunks/updateRowColumns';
import {getResponsiveConfig} from '../../utils/getResponsiveConfig';
import {useId} from '../../utils/useId';
import {
	useSetCustomRowContext,
	useSetUpdatedLayoutDataContext,
} from '../ResizeContext';

const NUMBER_OF_COLUMNS_OPTIONS = ['1', '2', '3', '4', '5', '6'];

const ROW_CONFIGURATION_IDENTIFIERS = {
	gutters: 'gutters',
	numberOfColumns: 'numberOfColumns',
};

export const RowConfigurationPanel = ({item}) => {
	const dispatch = useDispatch();
	const segmentsExperienceId = useSelector(selectSegmentsExperienceId);
	const selectedViewportSize = useSelector(
		(state) => state.selectedViewportSize
	);
	const setUpdatedLayoutData = useSetUpdatedLayoutDataContext();
	const setCustomRow = useSetCustomRowContext();

	const handleConfigurationValueChanged = (identifier, value) => {
		setCustomRow(false);
		setUpdatedLayoutData(null);

		let itemConfig = {[identifier]: value};

		if (
			selectedViewportSize !== VIEWPORT_SIZES.desktop &&
			identifier !== ROW_CONFIGURATION_IDENTIFIERS.gutters
		) {
			itemConfig = {[selectedViewportSize]: itemConfig};
		}

		if (identifier === ROW_CONFIGURATION_IDENTIFIERS.numberOfColumns) {
			const currentNumberOfColumns = rowConfig.numberOfColumns;
			const newNumberOfColumns = value;

			const columnsToBeModified = Math.abs(
				newNumberOfColumns - currentNumberOfColumns
			);

			if (columnsToBeModified === 0) {
				return;
			}

			if (item && item.itemId) {
				dispatch(
					updateRowColumns({
						itemId: item.itemId,
						numberOfColumns: newNumberOfColumns,
						segmentsExperienceId,
						viewportSizeId: selectedViewportSize,
					})
				);
			}

			return;
		}

		dispatch(
			updateItemConfig({
				itemConfig,
				itemId: item.itemId,
				segmentsExperienceId,
			})
		);
	};

	const rowConfig = getResponsiveConfig(item.config, selectedViewportSize);

	return (
		<>
			<div className="page-editor__floating-toolbar__panel-header">
				<p>{Liferay.Language.get('configuration')}</p>
			</div>

			<Select
				configurationKey="numberOfColumns"
				handleChange={handleConfigurationValueChanged}
				label={Liferay.Language.get('number-of-modules')}
				options={NUMBER_OF_COLUMNS_OPTIONS.map((option) => ({
					label: option,
				}))}
				value={rowConfig.numberOfColumns}
			/>

			{rowConfig.numberOfColumns > 1 && (
				<>
					<ClayCheckbox
						checked={rowConfig.gutters}
						label={Liferay.Language.get('show-gutter')}
						onChange={({target: {checked}}) =>
							handleConfigurationValueChanged('gutters', checked)
						}
					/>
				</>
			)}
		</>
	);
};

RowConfigurationPanel.propTypes = {
	item: getLayoutDataItemPropTypes({
		config: PropTypes.shape({numberOfColumns: PropTypes.number}),
	}),
};

const Select = ({configurationKey, handleChange, label, options, value}) => {
	const inputId = useId();

	return (
		<ClayForm.Group small>
			<label htmlFor={inputId}>{label}</label>

			<ClaySelectWithOption
				id={inputId}
				onChange={(event) => {
					const nextValue = event.target.value;

					handleChange(
						configurationKey,
						typeof value === 'string'
							? String(nextValue)
							: Number(nextValue)
					);
				}}
				options={options}
				value={String(value)}
			/>
		</ClayForm.Group>
	);
};

Select.propTypes = {
	configurationKey: PropTypes.string.isRequired,
	handleChange: PropTypes.func.isRequired,
	label: PropTypes.string.isRequired,
	options: PropTypes.arrayOf(
		PropTypes.shape({
			label: PropTypes.string,
			value: PropTypes.oneOfType([
				PropTypes.string.isRequired,
				PropTypes.number.isRequired,
			]),
		})
	),
	value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
};
