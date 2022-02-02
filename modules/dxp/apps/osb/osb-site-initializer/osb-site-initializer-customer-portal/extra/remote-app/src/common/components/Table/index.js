/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

import ClayTable from '@clayui/table';
import classNames from 'classnames';

import {useState} from 'react';
import TablePagination from './Pagination';
import TableSkeleton from './Skeleton';

const Table = ({
	columns,
	hasCheckbox,
	hasPagination,
	isLoading = false,
	paginationConfig,
	rows,
	...props
}) => {
	const [isAllCheckboxsSelected, setIsAllCheckboxsSelected] = useState(false);
	const [checkboxesChecked, setCheckboxesChecked] = useState([]);
	const {
		activePage,
		itemsPerPage,
		labels,
		listItemsPerPage,
		setActivePage,
		setItemsPerPage,
		showDeltasDropDown,
		totalCount,
	} = paginationConfig;

	const handleCheckboxClick = (event, id) => {
		const {checked} = event.target;

		if (checked) {
			return setCheckboxesChecked((prevChecked) => [...prevChecked, id]);
		}

		setCheckboxesChecked((prevChecked) =>
			prevChecked.filter((checked) => checked !== id)
		);
	};

	const handleToggleAllCheckboxsSelected = () => {
		setIsAllCheckboxsSelected(
			(previousIsAllCheckboxsSelected) => !previousIsAllCheckboxsSelected
		);
		if (isAllCheckboxsSelected) {
			setCheckboxesChecked([]);

			return;
		}
		setCheckboxesChecked(
			new Array(rows.length).fill().map((_, index) => index)
		);
	};

	return (
		<>
			<ClayTable {...props}>
				<ClayTable.Head>
					<ClayTable.Row>
						{hasCheckbox && (
							<ClayTable.Cell className="text-center">
								<input
									checked={isAllCheckboxsSelected}
									onChange={handleToggleAllCheckboxsSelected}
									type="checkbox"
								/>
							</ClayTable.Cell>
						)}

						{columns.map((column) => (
							<ClayTable.Cell
								align={column.align}
								className={
									column.header.styles ||
									'bg-neutral-1 font-weight-bold text-neutral-8'
								}
								headingCell
								key={column.accessor}
								noWrap={column.header.noWrap}
							>
								{column.header.description ? (
									<div>
										<p className="font-weight-bold m-0 text-neutral-10">
											{column.header.name}
										</p>

										<p className="font-weight-normal m-0 text-neutral-7 text-paragraph-sm">
											{column.header.description}
										</p>
									</div>
								) : (
									<>{column.header.name}</>
								)}
							</ClayTable.Cell>
						))}
					</ClayTable.Row>
				</ClayTable.Head>

				{!isLoading ? (
					<ClayTable.Body>
						{rows.map((row, rowIndex) => (
							<ClayTable.Row
								className={classNames({
									'common-table-active-row': checkboxesChecked.find(
										(checkboxChecked) =>
											checkboxChecked === rowIndex
									),
								})}
								key={rowIndex}
							>
								{hasCheckbox && (
									<ClayTable.Cell
										align="center"
										className="border-0"
										key={`checkbox-${rowIndex}`}
									>
										<input
											checked={checkboxesChecked.includes(
												rowIndex
											)}
											onChange={(event) =>
												handleCheckboxClick(
													event,
													rowIndex
												)
											}
											type="checkbox"
										/>
									</ClayTable.Cell>
								)}

								{columns.map((column, columnIndex) => (
									<ClayTable.Cell
										align={column.align}
										className={column.bodyClass}
										columnTextAlignment={column.align}
										expanded={column.expanded}
										key={`${rowIndex}-${columnIndex}`}
										noWrap={column.noWrap}
									>
										{row[column.accessor]}
									</ClayTable.Cell>
								))}
							</ClayTable.Row>
						))}
					</ClayTable.Body>
				) : (
					<TableSkeleton
						hasCheckbox={hasCheckbox}
						totalColumns={columns.length}
						totalItems={itemsPerPage}
					/>
				)}
			</ClayTable>

			{!!hasPagination && !!totalCount && (
				<TablePagination
					activePage={activePage || 1}
					itemsPerPage={itemsPerPage || 5}
					labels={labels}
					listItemsPerPage={listItemsPerPage}
					setActivePage={setActivePage}
					setItemsPerPage={setItemsPerPage}
					showDeltasDropDown={showDeltasDropDown}
					totalItems={totalCount}
				/>
			)}
		</>
	);
};

Table.defaultProps = {
	paginationConfig: {
		activePage: 1,
		itemsPerPage: 5,
		labels: '',
		listItemsPerPage: [],
		setActivePage: () => {},
		setItemsPerPage: () => {},
		showDeltasDropDown: false,
		totalCount: 1,
	},
};

export default Table;
