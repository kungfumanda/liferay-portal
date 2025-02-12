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

package com.liferay.commerce.product.model.impl;

import com.liferay.commerce.product.model.CommerceChannelRel;
import com.liferay.commerce.product.model.CommerceChannelRelModel;
import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.sql.Blob;
import java.sql.Types;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The base model implementation for the CommerceChannelRel service. Represents a row in the &quot;CommerceChannelRel&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface <code>CommerceChannelRelModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link CommerceChannelRelImpl}.
 * </p>
 *
 * @author Marco Leo
 * @see CommerceChannelRelImpl
 * @generated
 */
@JSON(strict = true)
public class CommerceChannelRelModelImpl
	extends BaseModelImpl<CommerceChannelRel>
	implements CommerceChannelRelModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a commerce channel rel model instance should use the <code>CommerceChannelRel</code> interface instead.
	 */
	public static final String TABLE_NAME = "CommerceChannelRel";

	public static final Object[][] TABLE_COLUMNS = {
		{"mvccVersion", Types.BIGINT}, {"ctCollectionId", Types.BIGINT},
		{"commerceChannelRelId", Types.BIGINT}, {"companyId", Types.BIGINT},
		{"userId", Types.BIGINT}, {"userName", Types.VARCHAR},
		{"createDate", Types.TIMESTAMP}, {"modifiedDate", Types.TIMESTAMP},
		{"classNameId", Types.BIGINT}, {"classPK", Types.BIGINT},
		{"commerceChannelId", Types.BIGINT}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("mvccVersion", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("ctCollectionId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("commerceChannelRelId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("classNameId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("classPK", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("commerceChannelId", Types.BIGINT);
	}

	public static final String TABLE_SQL_CREATE =
		"create table CommerceChannelRel (mvccVersion LONG default 0 not null,ctCollectionId LONG default 0 not null,commerceChannelRelId LONG not null,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,classNameId LONG,classPK LONG,commerceChannelId LONG,primary key (commerceChannelRelId, ctCollectionId))";

	public static final String TABLE_SQL_DROP = "drop table CommerceChannelRel";

	public static final String ORDER_BY_JPQL =
		" ORDER BY commerceChannelRel.createDate DESC";

	public static final String ORDER_BY_SQL =
		" ORDER BY CommerceChannelRel.createDate DESC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long CLASSNAMEID_COLUMN_BITMASK = 1L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long CLASSPK_COLUMN_BITMASK = 2L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long COMMERCECHANNELID_COLUMN_BITMASK = 4L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *		#getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long CREATEDATE_COLUMN_BITMASK = 8L;

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static void setEntityCacheEnabled(boolean entityCacheEnabled) {
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static void setFinderCacheEnabled(boolean finderCacheEnabled) {
	}

	public CommerceChannelRelModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _commerceChannelRelId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setCommerceChannelRelId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _commerceChannelRelId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return CommerceChannelRel.class;
	}

	@Override
	public String getModelClassName() {
		return CommerceChannelRel.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<CommerceChannelRel, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		for (Map.Entry<String, Function<CommerceChannelRel, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<CommerceChannelRel, Object> attributeGetterFunction =
				entry.getValue();

			attributes.put(
				attributeName,
				attributeGetterFunction.apply((CommerceChannelRel)this));
		}

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<CommerceChannelRel, Object>>
			attributeSetterBiConsumers = getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<CommerceChannelRel, Object> attributeSetterBiConsumer =
				attributeSetterBiConsumers.get(attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(CommerceChannelRel)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<CommerceChannelRel, Object>>
		getAttributeGetterFunctions() {

		return AttributeGetterFunctionsHolder._attributeGetterFunctions;
	}

	public Map<String, BiConsumer<CommerceChannelRel, Object>>
		getAttributeSetterBiConsumers() {

		return AttributeSetterBiConsumersHolder._attributeSetterBiConsumers;
	}

	private static class AttributeGetterFunctionsHolder {

		private static final Map<String, Function<CommerceChannelRel, Object>>
			_attributeGetterFunctions;

		static {
			Map<String, Function<CommerceChannelRel, Object>>
				attributeGetterFunctions =
					new LinkedHashMap
						<String, Function<CommerceChannelRel, Object>>();

			attributeGetterFunctions.put(
				"mvccVersion", CommerceChannelRel::getMvccVersion);
			attributeGetterFunctions.put(
				"ctCollectionId", CommerceChannelRel::getCtCollectionId);
			attributeGetterFunctions.put(
				"commerceChannelRelId",
				CommerceChannelRel::getCommerceChannelRelId);
			attributeGetterFunctions.put(
				"companyId", CommerceChannelRel::getCompanyId);
			attributeGetterFunctions.put(
				"userId", CommerceChannelRel::getUserId);
			attributeGetterFunctions.put(
				"userName", CommerceChannelRel::getUserName);
			attributeGetterFunctions.put(
				"createDate", CommerceChannelRel::getCreateDate);
			attributeGetterFunctions.put(
				"modifiedDate", CommerceChannelRel::getModifiedDate);
			attributeGetterFunctions.put(
				"classNameId", CommerceChannelRel::getClassNameId);
			attributeGetterFunctions.put(
				"classPK", CommerceChannelRel::getClassPK);
			attributeGetterFunctions.put(
				"commerceChannelId", CommerceChannelRel::getCommerceChannelId);

			_attributeGetterFunctions = Collections.unmodifiableMap(
				attributeGetterFunctions);
		}

	}

	private static class AttributeSetterBiConsumersHolder {

		private static final Map<String, BiConsumer<CommerceChannelRel, Object>>
			_attributeSetterBiConsumers;

		static {
			Map<String, BiConsumer<CommerceChannelRel, ?>>
				attributeSetterBiConsumers =
					new LinkedHashMap
						<String, BiConsumer<CommerceChannelRel, ?>>();

			attributeSetterBiConsumers.put(
				"mvccVersion",
				(BiConsumer<CommerceChannelRel, Long>)
					CommerceChannelRel::setMvccVersion);
			attributeSetterBiConsumers.put(
				"ctCollectionId",
				(BiConsumer<CommerceChannelRel, Long>)
					CommerceChannelRel::setCtCollectionId);
			attributeSetterBiConsumers.put(
				"commerceChannelRelId",
				(BiConsumer<CommerceChannelRel, Long>)
					CommerceChannelRel::setCommerceChannelRelId);
			attributeSetterBiConsumers.put(
				"companyId",
				(BiConsumer<CommerceChannelRel, Long>)
					CommerceChannelRel::setCompanyId);
			attributeSetterBiConsumers.put(
				"userId",
				(BiConsumer<CommerceChannelRel, Long>)
					CommerceChannelRel::setUserId);
			attributeSetterBiConsumers.put(
				"userName",
				(BiConsumer<CommerceChannelRel, String>)
					CommerceChannelRel::setUserName);
			attributeSetterBiConsumers.put(
				"createDate",
				(BiConsumer<CommerceChannelRel, Date>)
					CommerceChannelRel::setCreateDate);
			attributeSetterBiConsumers.put(
				"modifiedDate",
				(BiConsumer<CommerceChannelRel, Date>)
					CommerceChannelRel::setModifiedDate);
			attributeSetterBiConsumers.put(
				"classNameId",
				(BiConsumer<CommerceChannelRel, Long>)
					CommerceChannelRel::setClassNameId);
			attributeSetterBiConsumers.put(
				"classPK",
				(BiConsumer<CommerceChannelRel, Long>)
					CommerceChannelRel::setClassPK);
			attributeSetterBiConsumers.put(
				"commerceChannelId",
				(BiConsumer<CommerceChannelRel, Long>)
					CommerceChannelRel::setCommerceChannelId);

			_attributeSetterBiConsumers = Collections.unmodifiableMap(
				(Map)attributeSetterBiConsumers);
		}

	}

	@JSON
	@Override
	public long getMvccVersion() {
		return _mvccVersion;
	}

	@Override
	public void setMvccVersion(long mvccVersion) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_mvccVersion = mvccVersion;
	}

	@JSON
	@Override
	public long getCtCollectionId() {
		return _ctCollectionId;
	}

	@Override
	public void setCtCollectionId(long ctCollectionId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_ctCollectionId = ctCollectionId;
	}

	@JSON
	@Override
	public long getCommerceChannelRelId() {
		return _commerceChannelRelId;
	}

	@Override
	public void setCommerceChannelRelId(long commerceChannelRelId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_commerceChannelRelId = commerceChannelRelId;
	}

	@JSON
	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public void setCompanyId(long companyId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_companyId = companyId;
	}

	@JSON
	@Override
	public long getUserId() {
		return _userId;
	}

	@Override
	public void setUserId(long userId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_userId = userId;
	}

	@Override
	public String getUserUuid() {
		try {
			User user = UserLocalServiceUtil.getUserById(getUserId());

			return user.getUuid();
		}
		catch (PortalException portalException) {
			return "";
		}
	}

	@Override
	public void setUserUuid(String userUuid) {
	}

	@JSON
	@Override
	public String getUserName() {
		if (_userName == null) {
			return "";
		}
		else {
			return _userName;
		}
	}

	@Override
	public void setUserName(String userName) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_userName = userName;
	}

	@JSON
	@Override
	public Date getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_createDate = createDate;
	}

	@JSON
	@Override
	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public boolean hasSetModifiedDate() {
		return _setModifiedDate;
	}

	@Override
	public void setModifiedDate(Date modifiedDate) {
		_setModifiedDate = true;

		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_modifiedDate = modifiedDate;
	}

	@Override
	public String getClassName() {
		if (getClassNameId() <= 0) {
			return "";
		}

		return PortalUtil.getClassName(getClassNameId());
	}

	@Override
	public void setClassName(String className) {
		long classNameId = 0;

		if (Validator.isNotNull(className)) {
			classNameId = PortalUtil.getClassNameId(className);
		}

		setClassNameId(classNameId);
	}

	@JSON
	@Override
	public long getClassNameId() {
		return _classNameId;
	}

	@Override
	public void setClassNameId(long classNameId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_classNameId = classNameId;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalClassNameId() {
		return GetterUtil.getLong(
			this.<Long>getColumnOriginalValue("classNameId"));
	}

	@JSON
	@Override
	public long getClassPK() {
		return _classPK;
	}

	@Override
	public void setClassPK(long classPK) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_classPK = classPK;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalClassPK() {
		return GetterUtil.getLong(this.<Long>getColumnOriginalValue("classPK"));
	}

	@JSON
	@Override
	public long getCommerceChannelId() {
		return _commerceChannelId;
	}

	@Override
	public void setCommerceChannelId(long commerceChannelId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_commerceChannelId = commerceChannelId;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalCommerceChannelId() {
		return GetterUtil.getLong(
			this.<Long>getColumnOriginalValue("commerceChannelId"));
	}

	public long getColumnBitmask() {
		if (_columnBitmask > 0) {
			return _columnBitmask;
		}

		if ((_columnOriginalValues == null) ||
			(_columnOriginalValues == Collections.EMPTY_MAP)) {

			return 0;
		}

		for (Map.Entry<String, Object> entry :
				_columnOriginalValues.entrySet()) {

			if (!Objects.equals(
					entry.getValue(), getColumnValue(entry.getKey()))) {

				_columnBitmask |= _columnBitmasks.get(entry.getKey());
			}
		}

		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(
			getCompanyId(), CommerceChannelRel.class.getName(),
			getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public CommerceChannelRel toEscapedModel() {
		if (_escapedModel == null) {
			Function<InvocationHandler, CommerceChannelRel>
				escapedModelProxyProviderFunction =
					EscapedModelProxyProviderFunctionHolder.
						_escapedModelProxyProviderFunction;

			_escapedModel = escapedModelProxyProviderFunction.apply(
				new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		CommerceChannelRelImpl commerceChannelRelImpl =
			new CommerceChannelRelImpl();

		commerceChannelRelImpl.setMvccVersion(getMvccVersion());
		commerceChannelRelImpl.setCtCollectionId(getCtCollectionId());
		commerceChannelRelImpl.setCommerceChannelRelId(
			getCommerceChannelRelId());
		commerceChannelRelImpl.setCompanyId(getCompanyId());
		commerceChannelRelImpl.setUserId(getUserId());
		commerceChannelRelImpl.setUserName(getUserName());
		commerceChannelRelImpl.setCreateDate(getCreateDate());
		commerceChannelRelImpl.setModifiedDate(getModifiedDate());
		commerceChannelRelImpl.setClassNameId(getClassNameId());
		commerceChannelRelImpl.setClassPK(getClassPK());
		commerceChannelRelImpl.setCommerceChannelId(getCommerceChannelId());

		commerceChannelRelImpl.resetOriginalValues();

		return commerceChannelRelImpl;
	}

	@Override
	public CommerceChannelRel cloneWithOriginalValues() {
		CommerceChannelRelImpl commerceChannelRelImpl =
			new CommerceChannelRelImpl();

		commerceChannelRelImpl.setMvccVersion(
			this.<Long>getColumnOriginalValue("mvccVersion"));
		commerceChannelRelImpl.setCtCollectionId(
			this.<Long>getColumnOriginalValue("ctCollectionId"));
		commerceChannelRelImpl.setCommerceChannelRelId(
			this.<Long>getColumnOriginalValue("commerceChannelRelId"));
		commerceChannelRelImpl.setCompanyId(
			this.<Long>getColumnOriginalValue("companyId"));
		commerceChannelRelImpl.setUserId(
			this.<Long>getColumnOriginalValue("userId"));
		commerceChannelRelImpl.setUserName(
			this.<String>getColumnOriginalValue("userName"));
		commerceChannelRelImpl.setCreateDate(
			this.<Date>getColumnOriginalValue("createDate"));
		commerceChannelRelImpl.setModifiedDate(
			this.<Date>getColumnOriginalValue("modifiedDate"));
		commerceChannelRelImpl.setClassNameId(
			this.<Long>getColumnOriginalValue("classNameId"));
		commerceChannelRelImpl.setClassPK(
			this.<Long>getColumnOriginalValue("classPK"));
		commerceChannelRelImpl.setCommerceChannelId(
			this.<Long>getColumnOriginalValue("commerceChannelId"));

		return commerceChannelRelImpl;
	}

	@Override
	public int compareTo(CommerceChannelRel commerceChannelRel) {
		int value = 0;

		value = DateUtil.compareTo(
			getCreateDate(), commerceChannelRel.getCreateDate());

		value = value * -1;

		if (value != 0) {
			return value;
		}

		return 0;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof CommerceChannelRel)) {
			return false;
		}

		CommerceChannelRel commerceChannelRel = (CommerceChannelRel)object;

		long primaryKey = commerceChannelRel.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int)getPrimaryKey();
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	@Override
	public boolean isEntityCacheEnabled() {
		return true;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	@Override
	public boolean isFinderCacheEnabled() {
		return true;
	}

	@Override
	public void resetOriginalValues() {
		_columnOriginalValues = Collections.emptyMap();

		_setModifiedDate = false;

		_columnBitmask = 0;
	}

	@Override
	public CacheModel<CommerceChannelRel> toCacheModel() {
		CommerceChannelRelCacheModel commerceChannelRelCacheModel =
			new CommerceChannelRelCacheModel();

		commerceChannelRelCacheModel.mvccVersion = getMvccVersion();

		commerceChannelRelCacheModel.ctCollectionId = getCtCollectionId();

		commerceChannelRelCacheModel.commerceChannelRelId =
			getCommerceChannelRelId();

		commerceChannelRelCacheModel.companyId = getCompanyId();

		commerceChannelRelCacheModel.userId = getUserId();

		commerceChannelRelCacheModel.userName = getUserName();

		String userName = commerceChannelRelCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			commerceChannelRelCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			commerceChannelRelCacheModel.createDate = createDate.getTime();
		}
		else {
			commerceChannelRelCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			commerceChannelRelCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			commerceChannelRelCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		commerceChannelRelCacheModel.classNameId = getClassNameId();

		commerceChannelRelCacheModel.classPK = getClassPK();

		commerceChannelRelCacheModel.commerceChannelId = getCommerceChannelId();

		return commerceChannelRelCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<CommerceChannelRel, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			(5 * attributeGetterFunctions.size()) + 2);

		sb.append("{");

		for (Map.Entry<String, Function<CommerceChannelRel, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<CommerceChannelRel, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("\"");
			sb.append(attributeName);
			sb.append("\": ");

			Object value = attributeGetterFunction.apply(
				(CommerceChannelRel)this);

			if (value == null) {
				sb.append("null");
			}
			else if (value instanceof Blob || value instanceof Date ||
					 value instanceof Map || value instanceof String) {

				sb.append(
					"\"" + StringUtil.replace(value.toString(), "\"", "'") +
						"\"");
			}
			else {
				sb.append(value);
			}

			sb.append(", ");
		}

		if (sb.index() > 1) {
			sb.setIndex(sb.index() - 1);
		}

		sb.append("}");

		return sb.toString();
	}

	private static class EscapedModelProxyProviderFunctionHolder {

		private static final Function<InvocationHandler, CommerceChannelRel>
			_escapedModelProxyProviderFunction =
				ProxyUtil.getProxyProviderFunction(
					CommerceChannelRel.class, ModelWrapper.class);

	}

	private long _mvccVersion;
	private long _ctCollectionId;
	private long _commerceChannelRelId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _setModifiedDate;
	private long _classNameId;
	private long _classPK;
	private long _commerceChannelId;

	public <T> T getColumnValue(String columnName) {
		Function<CommerceChannelRel, Object> function =
			AttributeGetterFunctionsHolder._attributeGetterFunctions.get(
				columnName);

		if (function == null) {
			throw new IllegalArgumentException(
				"No attribute getter function found for " + columnName);
		}

		return (T)function.apply((CommerceChannelRel)this);
	}

	public <T> T getColumnOriginalValue(String columnName) {
		if (_columnOriginalValues == null) {
			return null;
		}

		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		return (T)_columnOriginalValues.get(columnName);
	}

	private void _setColumnOriginalValues() {
		_columnOriginalValues = new HashMap<String, Object>();

		_columnOriginalValues.put("mvccVersion", _mvccVersion);
		_columnOriginalValues.put("ctCollectionId", _ctCollectionId);
		_columnOriginalValues.put(
			"commerceChannelRelId", _commerceChannelRelId);
		_columnOriginalValues.put("companyId", _companyId);
		_columnOriginalValues.put("userId", _userId);
		_columnOriginalValues.put("userName", _userName);
		_columnOriginalValues.put("createDate", _createDate);
		_columnOriginalValues.put("modifiedDate", _modifiedDate);
		_columnOriginalValues.put("classNameId", _classNameId);
		_columnOriginalValues.put("classPK", _classPK);
		_columnOriginalValues.put("commerceChannelId", _commerceChannelId);
	}

	private transient Map<String, Object> _columnOriginalValues;

	public static long getColumnBitmask(String columnName) {
		return _columnBitmasks.get(columnName);
	}

	private static final Map<String, Long> _columnBitmasks;

	static {
		Map<String, Long> columnBitmasks = new HashMap<>();

		columnBitmasks.put("mvccVersion", 1L);

		columnBitmasks.put("ctCollectionId", 2L);

		columnBitmasks.put("commerceChannelRelId", 4L);

		columnBitmasks.put("companyId", 8L);

		columnBitmasks.put("userId", 16L);

		columnBitmasks.put("userName", 32L);

		columnBitmasks.put("createDate", 64L);

		columnBitmasks.put("modifiedDate", 128L);

		columnBitmasks.put("classNameId", 256L);

		columnBitmasks.put("classPK", 512L);

		columnBitmasks.put("commerceChannelId", 1024L);

		_columnBitmasks = Collections.unmodifiableMap(columnBitmasks);
	}

	private long _columnBitmask;
	private CommerceChannelRel _escapedModel;

}