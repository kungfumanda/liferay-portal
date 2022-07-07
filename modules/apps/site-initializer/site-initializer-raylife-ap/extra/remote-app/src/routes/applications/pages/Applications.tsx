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

import React, {useContext} from 'react';

import {NewApplicationAutoContext} from '../context/NewApplicationAutoContextProvider';
import DriverInfo from '../forms/steps/DriverInfo';
import VehicleInfo from '../forms/steps/VehicleInfo';
import NewApplication from './NewApplications';

const Applications = () => {
	const [state] = useContext(NewApplicationAutoContext);

	return (
		<NewApplication>
			{state.currentStep === 0 && <DriverInfo />}

			{state.currentStep === 1 && <VehicleInfo />}
		</NewApplication>
	);
};

export default Applications;
