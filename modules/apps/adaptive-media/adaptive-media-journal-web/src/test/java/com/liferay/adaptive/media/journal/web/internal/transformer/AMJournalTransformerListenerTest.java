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

package com.liferay.adaptive.media.journal.web.internal.transformer;

import com.liferay.adaptive.media.content.transformer.ContentTransformerHandler;
import com.liferay.adaptive.media.content.transformer.constants.ContentTransformerContentTypes;
import com.liferay.journal.util.JournalContent;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Alejandro Tardín
 */
public class AMJournalTransformerListenerTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() {
		ReflectionTestUtil.setFieldValue(
			_amJournalTransformerListener, "_contentTransformerHandler",
			_contentTransformerHandler);
		ReflectionTestUtil.setFieldValue(
			_amJournalTransformerListener, "_journalContent", _journalContent);
	}

	@Test
	public void testOnActivationClearsJournalCache() throws Exception {
		_amJournalTransformerListener.activate();

		Mockito.verify(
			_journalContent, Mockito.times(1)
		).clearCache();
	}

	@Test
	public void testOnDeactivationClearsJournalCache() throws Exception {
		_amJournalTransformerListener.deactivate();

		Mockito.verify(
			_journalContent, Mockito.times(1)
		).clearCache();
	}

	@Test
	public void testOnOutputTransformsTheOutput() throws Exception {
		String originalOutput = RandomTestUtil.randomString();
		String transformedOutput = RandomTestUtil.randomString();

		Mockito.when(
			_contentTransformerHandler.transform(
				ContentTransformerContentTypes.HTML, originalOutput)
		).thenReturn(
			transformedOutput
		);

		String newOutput = _amJournalTransformerListener.onOutput(
			originalOutput, _LANGUAGE_ID, _tokens);

		Assert.assertSame(transformedOutput, newOutput);
	}

	@Test
	public void testOnScriptDoesNotModifyTheScript() throws Exception {
		String originalScript = RandomTestUtil.randomString();

		String newScript = _amJournalTransformerListener.onScript(
			originalScript, _document, _LANGUAGE_ID, _tokens);

		Assert.assertSame(originalScript, newScript);

		Mockito.verifyNoInteractions(_document);
	}

	@Test
	public void testOnXmlDoesNotModifyTheXml() throws Exception {
		Document newDocument = _amJournalTransformerListener.onXml(
			_document, _LANGUAGE_ID, _tokens);

		Assert.assertSame(_document, newDocument);

		Mockito.verifyNoInteractions(_document);
	}

	private static final String _LANGUAGE_ID = "en";

	private final AMJournalTransformerListener _amJournalTransformerListener =
		new AMJournalTransformerListener();
	private final ContentTransformerHandler _contentTransformerHandler =
		Mockito.mock(ContentTransformerHandler.class);
	private final Document _document = Mockito.mock(Document.class);
	private final JournalContent _journalContent = Mockito.mock(
		JournalContent.class);
	private final Map<String, String> _tokens = new HashMap<>();

}