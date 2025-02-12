/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.portal.workflow.metrics.model;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.GroupedModel;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.portal.kernel.model.ShardedModel;
import com.liferay.portal.kernel.model.StagedAuditedModel;
import com.liferay.portal.kernel.model.WorkflowedModel;

import java.util.Date;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The base model interface for the WorkflowMetricsSLADefinitionVersion service. Represents a row in the &quot;WMSLADefinitionVersion&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation <code>com.liferay.portal.workflow.metrics.model.impl.WorkflowMetricsSLADefinitionVersionModelImpl</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in <code>com.liferay.portal.workflow.metrics.model.impl.WorkflowMetricsSLADefinitionVersionImpl</code>.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see WorkflowMetricsSLADefinitionVersion
 * @generated
 */
@ProviderType
public interface WorkflowMetricsSLADefinitionVersionModel
	extends BaseModel<WorkflowMetricsSLADefinitionVersion>, GroupedModel,
			MVCCModel, ShardedModel, StagedAuditedModel, WorkflowedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a workflow metrics sla definition version model instance should use the {@link WorkflowMetricsSLADefinitionVersion} interface instead.
	 */

	/**
	 * Returns the primary key of this workflow metrics sla definition version.
	 *
	 * @return the primary key of this workflow metrics sla definition version
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this workflow metrics sla definition version.
	 *
	 * @param primaryKey the primary key of this workflow metrics sla definition version
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the mvcc version of this workflow metrics sla definition version.
	 *
	 * @return the mvcc version of this workflow metrics sla definition version
	 */
	@Override
	public long getMvccVersion();

	/**
	 * Sets the mvcc version of this workflow metrics sla definition version.
	 *
	 * @param mvccVersion the mvcc version of this workflow metrics sla definition version
	 */
	@Override
	public void setMvccVersion(long mvccVersion);

	/**
	 * Returns the uuid of this workflow metrics sla definition version.
	 *
	 * @return the uuid of this workflow metrics sla definition version
	 */
	@AutoEscape
	@Override
	public String getUuid();

	/**
	 * Sets the uuid of this workflow metrics sla definition version.
	 *
	 * @param uuid the uuid of this workflow metrics sla definition version
	 */
	@Override
	public void setUuid(String uuid);

	/**
	 * Returns the workflow metrics sla definition version ID of this workflow metrics sla definition version.
	 *
	 * @return the workflow metrics sla definition version ID of this workflow metrics sla definition version
	 */
	public long getWorkflowMetricsSLADefinitionVersionId();

	/**
	 * Sets the workflow metrics sla definition version ID of this workflow metrics sla definition version.
	 *
	 * @param workflowMetricsSLADefinitionVersionId the workflow metrics sla definition version ID of this workflow metrics sla definition version
	 */
	public void setWorkflowMetricsSLADefinitionVersionId(
		long workflowMetricsSLADefinitionVersionId);

	/**
	 * Returns the group ID of this workflow metrics sla definition version.
	 *
	 * @return the group ID of this workflow metrics sla definition version
	 */
	@Override
	public long getGroupId();

	/**
	 * Sets the group ID of this workflow metrics sla definition version.
	 *
	 * @param groupId the group ID of this workflow metrics sla definition version
	 */
	@Override
	public void setGroupId(long groupId);

	/**
	 * Returns the company ID of this workflow metrics sla definition version.
	 *
	 * @return the company ID of this workflow metrics sla definition version
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this workflow metrics sla definition version.
	 *
	 * @param companyId the company ID of this workflow metrics sla definition version
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this workflow metrics sla definition version.
	 *
	 * @return the user ID of this workflow metrics sla definition version
	 */
	@Override
	public long getUserId();

	/**
	 * Sets the user ID of this workflow metrics sla definition version.
	 *
	 * @param userId the user ID of this workflow metrics sla definition version
	 */
	@Override
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this workflow metrics sla definition version.
	 *
	 * @return the user uuid of this workflow metrics sla definition version
	 */
	@Override
	public String getUserUuid();

	/**
	 * Sets the user uuid of this workflow metrics sla definition version.
	 *
	 * @param userUuid the user uuid of this workflow metrics sla definition version
	 */
	@Override
	public void setUserUuid(String userUuid);

	/**
	 * Returns the user name of this workflow metrics sla definition version.
	 *
	 * @return the user name of this workflow metrics sla definition version
	 */
	@AutoEscape
	@Override
	public String getUserName();

	/**
	 * Sets the user name of this workflow metrics sla definition version.
	 *
	 * @param userName the user name of this workflow metrics sla definition version
	 */
	@Override
	public void setUserName(String userName);

	/**
	 * Returns the create date of this workflow metrics sla definition version.
	 *
	 * @return the create date of this workflow metrics sla definition version
	 */
	@Override
	public Date getCreateDate();

	/**
	 * Sets the create date of this workflow metrics sla definition version.
	 *
	 * @param createDate the create date of this workflow metrics sla definition version
	 */
	@Override
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this workflow metrics sla definition version.
	 *
	 * @return the modified date of this workflow metrics sla definition version
	 */
	@Override
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this workflow metrics sla definition version.
	 *
	 * @param modifiedDate the modified date of this workflow metrics sla definition version
	 */
	@Override
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the active of this workflow metrics sla definition version.
	 *
	 * @return the active of this workflow metrics sla definition version
	 */
	public boolean getActive();

	/**
	 * Returns <code>true</code> if this workflow metrics sla definition version is active.
	 *
	 * @return <code>true</code> if this workflow metrics sla definition version is active; <code>false</code> otherwise
	 */
	public boolean isActive();

	/**
	 * Sets whether this workflow metrics sla definition version is active.
	 *
	 * @param active the active of this workflow metrics sla definition version
	 */
	public void setActive(boolean active);

	/**
	 * Returns the calendar key of this workflow metrics sla definition version.
	 *
	 * @return the calendar key of this workflow metrics sla definition version
	 */
	@AutoEscape
	public String getCalendarKey();

	/**
	 * Sets the calendar key of this workflow metrics sla definition version.
	 *
	 * @param calendarKey the calendar key of this workflow metrics sla definition version
	 */
	public void setCalendarKey(String calendarKey);

	/**
	 * Returns the description of this workflow metrics sla definition version.
	 *
	 * @return the description of this workflow metrics sla definition version
	 */
	@AutoEscape
	public String getDescription();

	/**
	 * Sets the description of this workflow metrics sla definition version.
	 *
	 * @param description the description of this workflow metrics sla definition version
	 */
	public void setDescription(String description);

	/**
	 * Returns the duration of this workflow metrics sla definition version.
	 *
	 * @return the duration of this workflow metrics sla definition version
	 */
	public long getDuration();

	/**
	 * Sets the duration of this workflow metrics sla definition version.
	 *
	 * @param duration the duration of this workflow metrics sla definition version
	 */
	public void setDuration(long duration);

	/**
	 * Returns the name of this workflow metrics sla definition version.
	 *
	 * @return the name of this workflow metrics sla definition version
	 */
	@AutoEscape
	public String getName();

	/**
	 * Sets the name of this workflow metrics sla definition version.
	 *
	 * @param name the name of this workflow metrics sla definition version
	 */
	public void setName(String name);

	/**
	 * Returns the pause node keys of this workflow metrics sla definition version.
	 *
	 * @return the pause node keys of this workflow metrics sla definition version
	 */
	@AutoEscape
	public String getPauseNodeKeys();

	/**
	 * Sets the pause node keys of this workflow metrics sla definition version.
	 *
	 * @param pauseNodeKeys the pause node keys of this workflow metrics sla definition version
	 */
	public void setPauseNodeKeys(String pauseNodeKeys);

	/**
	 * Returns the process ID of this workflow metrics sla definition version.
	 *
	 * @return the process ID of this workflow metrics sla definition version
	 */
	public long getProcessId();

	/**
	 * Sets the process ID of this workflow metrics sla definition version.
	 *
	 * @param processId the process ID of this workflow metrics sla definition version
	 */
	public void setProcessId(long processId);

	/**
	 * Returns the process version of this workflow metrics sla definition version.
	 *
	 * @return the process version of this workflow metrics sla definition version
	 */
	@AutoEscape
	public String getProcessVersion();

	/**
	 * Sets the process version of this workflow metrics sla definition version.
	 *
	 * @param processVersion the process version of this workflow metrics sla definition version
	 */
	public void setProcessVersion(String processVersion);

	/**
	 * Returns the start node keys of this workflow metrics sla definition version.
	 *
	 * @return the start node keys of this workflow metrics sla definition version
	 */
	@AutoEscape
	public String getStartNodeKeys();

	/**
	 * Sets the start node keys of this workflow metrics sla definition version.
	 *
	 * @param startNodeKeys the start node keys of this workflow metrics sla definition version
	 */
	public void setStartNodeKeys(String startNodeKeys);

	/**
	 * Returns the stop node keys of this workflow metrics sla definition version.
	 *
	 * @return the stop node keys of this workflow metrics sla definition version
	 */
	@AutoEscape
	public String getStopNodeKeys();

	/**
	 * Sets the stop node keys of this workflow metrics sla definition version.
	 *
	 * @param stopNodeKeys the stop node keys of this workflow metrics sla definition version
	 */
	public void setStopNodeKeys(String stopNodeKeys);

	/**
	 * Returns the version of this workflow metrics sla definition version.
	 *
	 * @return the version of this workflow metrics sla definition version
	 */
	@AutoEscape
	public String getVersion();

	/**
	 * Sets the version of this workflow metrics sla definition version.
	 *
	 * @param version the version of this workflow metrics sla definition version
	 */
	public void setVersion(String version);

	/**
	 * Returns the workflow metrics sla definition ID of this workflow metrics sla definition version.
	 *
	 * @return the workflow metrics sla definition ID of this workflow metrics sla definition version
	 */
	public long getWorkflowMetricsSLADefinitionId();

	/**
	 * Sets the workflow metrics sla definition ID of this workflow metrics sla definition version.
	 *
	 * @param workflowMetricsSLADefinitionId the workflow metrics sla definition ID of this workflow metrics sla definition version
	 */
	public void setWorkflowMetricsSLADefinitionId(
		long workflowMetricsSLADefinitionId);

	/**
	 * Returns the status of this workflow metrics sla definition version.
	 *
	 * @return the status of this workflow metrics sla definition version
	 */
	@Override
	public int getStatus();

	/**
	 * Sets the status of this workflow metrics sla definition version.
	 *
	 * @param status the status of this workflow metrics sla definition version
	 */
	@Override
	public void setStatus(int status);

	/**
	 * Returns the status by user ID of this workflow metrics sla definition version.
	 *
	 * @return the status by user ID of this workflow metrics sla definition version
	 */
	@Override
	public long getStatusByUserId();

	/**
	 * Sets the status by user ID of this workflow metrics sla definition version.
	 *
	 * @param statusByUserId the status by user ID of this workflow metrics sla definition version
	 */
	@Override
	public void setStatusByUserId(long statusByUserId);

	/**
	 * Returns the status by user uuid of this workflow metrics sla definition version.
	 *
	 * @return the status by user uuid of this workflow metrics sla definition version
	 */
	@Override
	public String getStatusByUserUuid();

	/**
	 * Sets the status by user uuid of this workflow metrics sla definition version.
	 *
	 * @param statusByUserUuid the status by user uuid of this workflow metrics sla definition version
	 */
	@Override
	public void setStatusByUserUuid(String statusByUserUuid);

	/**
	 * Returns the status by user name of this workflow metrics sla definition version.
	 *
	 * @return the status by user name of this workflow metrics sla definition version
	 */
	@AutoEscape
	@Override
	public String getStatusByUserName();

	/**
	 * Sets the status by user name of this workflow metrics sla definition version.
	 *
	 * @param statusByUserName the status by user name of this workflow metrics sla definition version
	 */
	@Override
	public void setStatusByUserName(String statusByUserName);

	/**
	 * Returns the status date of this workflow metrics sla definition version.
	 *
	 * @return the status date of this workflow metrics sla definition version
	 */
	@Override
	public Date getStatusDate();

	/**
	 * Sets the status date of this workflow metrics sla definition version.
	 *
	 * @param statusDate the status date of this workflow metrics sla definition version
	 */
	@Override
	public void setStatusDate(Date statusDate);

	/**
	 * Returns <code>true</code> if this workflow metrics sla definition version is approved.
	 *
	 * @return <code>true</code> if this workflow metrics sla definition version is approved; <code>false</code> otherwise
	 */
	@Override
	public boolean isApproved();

	/**
	 * Returns <code>true</code> if this workflow metrics sla definition version is denied.
	 *
	 * @return <code>true</code> if this workflow metrics sla definition version is denied; <code>false</code> otherwise
	 */
	@Override
	public boolean isDenied();

	/**
	 * Returns <code>true</code> if this workflow metrics sla definition version is a draft.
	 *
	 * @return <code>true</code> if this workflow metrics sla definition version is a draft; <code>false</code> otherwise
	 */
	@Override
	public boolean isDraft();

	/**
	 * Returns <code>true</code> if this workflow metrics sla definition version is expired.
	 *
	 * @return <code>true</code> if this workflow metrics sla definition version is expired; <code>false</code> otherwise
	 */
	@Override
	public boolean isExpired();

	/**
	 * Returns <code>true</code> if this workflow metrics sla definition version is inactive.
	 *
	 * @return <code>true</code> if this workflow metrics sla definition version is inactive; <code>false</code> otherwise
	 */
	@Override
	public boolean isInactive();

	/**
	 * Returns <code>true</code> if this workflow metrics sla definition version is incomplete.
	 *
	 * @return <code>true</code> if this workflow metrics sla definition version is incomplete; <code>false</code> otherwise
	 */
	@Override
	public boolean isIncomplete();

	/**
	 * Returns <code>true</code> if this workflow metrics sla definition version is pending.
	 *
	 * @return <code>true</code> if this workflow metrics sla definition version is pending; <code>false</code> otherwise
	 */
	@Override
	public boolean isPending();

	/**
	 * Returns <code>true</code> if this workflow metrics sla definition version is scheduled.
	 *
	 * @return <code>true</code> if this workflow metrics sla definition version is scheduled; <code>false</code> otherwise
	 */
	@Override
	public boolean isScheduled();

	@Override
	public WorkflowMetricsSLADefinitionVersion cloneWithOriginalValues();

	public default String toXmlString() {
		return null;
	}

}