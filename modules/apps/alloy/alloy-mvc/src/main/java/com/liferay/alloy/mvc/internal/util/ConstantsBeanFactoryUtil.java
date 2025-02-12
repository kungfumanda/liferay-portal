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

package com.liferay.alloy.mvc.internal.util;

import com.liferay.petra.concurrent.ConcurrentReferenceKeyHashMap;
import com.liferay.petra.concurrent.ConcurrentReferenceValueHashMap;
import com.liferay.petra.memory.FinalizeManager;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.lang.ref.Reference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.util.concurrent.ConcurrentMap;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * @author Shuyang Zhou
 */
public class ConstantsBeanFactoryUtil {

	public static Object getConstantsBean(Class<?> constantsClass) {
		Object constantsBean = constantsBeans.get(constantsClass);

		if (constantsBean == null) {
			constantsBean = createConstantsBean(constantsClass);

			constantsBeans.put(constantsClass, constantsBean);
		}

		return constantsBean;
	}

	protected static Object createConstantsBean(Class<?> constantsClass) {
		ClassLoader classLoader = constantsClass.getClassLoader();

		String constantsBeanClassName = constantsClass.getName() + "Bean";

		Class<?> constantsBeanClass = null;

		synchronized (classLoader) {
			try {
				constantsBeanClass = classLoader.loadClass(
					constantsBeanClassName);
			}
			catch (ClassNotFoundException classNotFoundException) {
				if (_log.isDebugEnabled()) {
					_log.debug(classNotFoundException);
				}
			}

			try {
				if (constantsBeanClass == null) {
					byte[] classData = generateConstantsBeanClassData(
						constantsClass);

					constantsBeanClass = (Class<?>)_defineClassMethod.invoke(
						classLoader, constantsBeanClassName, classData, 0,
						classData.length);
				}

				return constantsBeanClass.newInstance();
			}
			catch (Throwable throwable) {
				throw new RuntimeException(throwable);
			}
		}
	}

	protected static byte[] generateConstantsBeanClassData(
		Class<?> constantsClass) {

		String constantsClassBinaryName = getClassBinaryName(constantsClass);

		String constantsBeanClassBinaryName = constantsClassBinaryName + "Bean";

		String objectClassBinaryName = getClassBinaryName(Object.class);

		ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);

		classWriter.visit(
			Opcodes.V1_6, Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER,
			constantsBeanClassBinaryName, null, objectClassBinaryName, null);

		MethodVisitor methodVisitor = classWriter.visitMethod(
			Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);

		methodVisitor.visitCode();
		methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
		methodVisitor.visitMethodInsn(
			Opcodes.INVOKESPECIAL, objectClassBinaryName, "<init>", "()V");
		methodVisitor.visitInsn(Opcodes.RETURN);
		methodVisitor.visitMaxs(1, 1);
		methodVisitor.visitEnd();

		for (Field field : constantsClass.getFields()) {
			if (!Modifier.isStatic(field.getModifiers())) {
				continue;
			}

			Type fieldType = Type.getType(field.getType());

			methodVisitor = classWriter.visitMethod(
				Opcodes.ACC_PUBLIC, "get" + field.getName(),
				"()" + fieldType.getDescriptor(), null, null);

			methodVisitor.visitCode();
			methodVisitor.visitFieldInsn(
				Opcodes.GETSTATIC, constantsClassBinaryName, field.getName(),
				fieldType.getDescriptor());

			methodVisitor.visitInsn(fieldType.getOpcode(Opcodes.IRETURN));

			methodVisitor.visitMaxs(fieldType.getSize(), 1);

			methodVisitor.visitEnd();
		}

		for (Method method : constantsClass.getMethods()) {
			if (!Modifier.isStatic(method.getModifiers())) {
				continue;
			}

			String methodDescriptor = Type.getMethodDescriptor(method);

			methodVisitor = classWriter.visitMethod(
				Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, method.getName(),
				methodDescriptor, null, null);

			methodVisitor.visitCode();

			int i = 0;

			for (Type parameterType : Type.getArgumentTypes(method)) {
				methodVisitor.visitVarInsn(
					parameterType.getOpcode(Opcodes.ILOAD), i);

				i += parameterType.getSize();
			}

			methodVisitor.visitMethodInsn(
				Opcodes.INVOKESTATIC, constantsClassBinaryName,
				method.getName(), methodDescriptor);

			Type returnType = Type.getType(method.getReturnType());

			methodVisitor.visitInsn(returnType.getOpcode(Opcodes.IRETURN));

			methodVisitor.visitMaxs(0, 0);

			methodVisitor.visitEnd();
		}

		classWriter.visitEnd();

		return classWriter.toByteArray();
	}

	protected static String getClassBinaryName(Class<?> clazz) {
		String className = clazz.getName();

		return StringUtil.replace(className, '.', '/');
	}

	protected static ConcurrentMap<Class<?>, Object> constantsBeans =
		new ConcurrentReferenceKeyHashMap<>(
			new ConcurrentReferenceValueHashMap<Reference<Class<?>>, Object>(
				FinalizeManager.WEAK_REFERENCE_FACTORY),
			FinalizeManager.WEAK_REFERENCE_FACTORY);

	private static final Log _log = LogFactoryUtil.getLog(
		ConstantsBeanFactoryUtil.class);

	private static final Method _defineClassMethod;

	static {
		try {
			_defineClassMethod = ReflectionUtil.getDeclaredMethod(
				ClassLoader.class, "defineClass", String.class, byte[].class,
				int.class, int.class);
		}
		catch (Throwable throwable) {
			throw new ExceptionInInitializerError(throwable);
		}
	}

}