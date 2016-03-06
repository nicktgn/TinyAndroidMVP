/*
 * Copyright 2016 Nick Tsygankov (nicktgn@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.nicktgn.mvp_sample.utils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

/**
 * Created by nick on 2/26/16.
 */
public class EditHistoryTest {

	public static final int DEF_MAX_ENTRIES = 10;
	public static final int MAX_ENTRIES_2 = 2;
	public static final String TEXT_QWE = "qwe";
	public static final String TEXT_QWER = "qwer";
	public static final String TEXT_QWERT = "qwert";
	public static final String TEXT_QWERA = "qwera";
	public static final String TEXT_QWEA = "qwea";
	public static final String TEXT_QWERTY = "qwerty";

	@Test
	public void should_notBeAbleToGoBackOrForward() {
		// given
		EditHistory eh = new EditHistory(DEF_MAX_ENTRIES, TEXT_QWE);

		// should
		assertThat(eh.canGoBack()).as("can go back")
			.isFalse();
		// should
		assertThat(eh.canGoForward()).as("can go forward")
			.isFalse();

		// should
		try {
			eh.goBack();
			failBecauseExceptionWasNotThrown(IllegalStateException.class);
		} catch (Exception e) {
			assertThat(e).as("try to go back")
				.isInstanceOf(IllegalStateException.class);
		}

		// should
		try {
			eh.goForward();
			failBecauseExceptionWasNotThrown(IllegalStateException.class);
		} catch (Exception e) {
			assertThat(e).as("try to go back")
				.isInstanceOf(IllegalStateException.class);
		}
	}

	/*
	@Test
	public void should_goBackAndThenForward(){
		// given
		EditHistory eh = new EditHistory(DEF_MAX_ENTRIES, TEXT_QWE);

		// when
		eh.addEntry(TEXT_QWER);

		// should
		assertThat(eh.canGoBack()).as("can go back when added 1 entry")
			.isTrue();
		assertThat(eh.canGoForward()).as("can go forward when added 1 entry")
			.isFalse();

		// should
		assertThat(eh.goBack()).as("previous text is 'qwe'")
			.isEqualTo(TEXT_QWE);
		assertThat(eh.canGoBack()).as("can go back after just going back (1 entry total)")
			.isFalse();
		assertThat(eh.canGoForward()).as("can go forward after just going back (1 entry total)")
			.isTrue();

		// should
		assertThat(eh.goForward()).as("returning to the newest text 'qwer'")
			.isEqualTo(TEXT_QWER);
		assertThat(eh.canGoBack()).as("can go back after going forward (1 entry total)")
			.isTrue();
		assertThat(eh.canGoForward()).as("cab go forward after going forward (1 entry total)")
			.isFalse();
	}*/

	@Test
	public void should_beAbleToGoBackButNotForward_whenAdded1Entry(){
		// given
		EditHistory eh = new EditHistory(DEF_MAX_ENTRIES, TEXT_QWE);

		// when
		eh.addEntry(TEXT_QWER);

		// should
		assertThat(eh.canGoBack()).as("can go back when added 1 entry")
			.isTrue();
		assertThat(eh.canGoForward()).as("can go forward when added 1 entry")
			.isFalse();
	}

	@Test
	public void should_goBackToPreviousText_whenAdded1Entry(){
		// given
		EditHistory eh = new EditHistory(DEF_MAX_ENTRIES, TEXT_QWE);

		// when
		eh.addEntry(TEXT_QWER);

		// should
		assertThat(eh.goBack()).as("previous text is 'qwe'")
			.isEqualTo(TEXT_QWE);
		assertThat(eh.canGoBack()).as("can go back after just going back (1 entry total)")
			.isFalse();
		assertThat(eh.canGoForward()).as("can go forward after just going back (1 entry total)")
			.isTrue();
	}

	@Test
	public void should_goForwardToLastText_whenWentBack_given1Entry(){
		// given
		EditHistory eh = new EditHistory(DEF_MAX_ENTRIES, TEXT_QWE);
		eh.addEntry(TEXT_QWER);

		// when
		eh.goBack();

		// should
		assertThat(eh.goForward()).as("returning to the newest text 'qwer'")
			.isEqualTo(TEXT_QWER);
		assertThat(eh.canGoBack()).as("can go back after going forward (1 entry total)")
			.isTrue();
		assertThat(eh.canGoForward()).as("cab go forward after going forward (1 entry total)")
			.isFalse();
	}


	@Test
	public void should_beAbleToGoBackAndForward_whenGoBackOnce_given2Entries(){
		// given
		EditHistory eh = new EditHistory(DEF_MAX_ENTRIES, TEXT_QWE);
		eh.addEntry(TEXT_QWER);
		eh.addEntry(TEXT_QWERT);

		// when
		eh.goBack();

		// should
		assertThat(eh.canGoBack()).as("can still go back after going back once (2 entries total)")
			.isTrue();
		assertThat(eh.canGoForward()).as("can go forward after going back once (2 entries total)")
			.isTrue();
	}


	//  [qwe->qwer]{qwer->qwert} qwert
	//  {qwe->qwer}[qwer->qwert] qwer
	//{}[qwe->qwer][qwer->qwert] qwe
	//  {qwe->qwer}[qwer->qwert] qwer
	//  [qwe->qwer]{qwer->qwert} qwert

	/*
	@Test
	public void should_goToBackMostAndThenToFrontMost() {
		// given
		EditHistory eh = new EditHistory(DEF_MAX_ENTRIES, TEXT_QWE);
		eh.addEntry(TEXT_QWER);
		eh.addEntry(TEXT_QWERT);

		// when
		assertThat(eh.goBack()).as("going back to 'qwer'")
			.isEqualTo(TEXT_QWER);
		assertThat(eh.goBack()).as("going back to 'qwe'")
			.isEqualTo(TEXT_QWE);

		// should
		assertThat(eh.canGoBack()).as("can go back more after already going 2 entries back (2 entries total)")
			.isFalse();
		assertThat(eh.canGoForward()).as("can go forward after already going 2 entries back (2 entries total)")
			.isTrue();

		assertThat(eh.goForward()).as("going forward to 'qwer'")
			.isEqualTo(TEXT_QWER);
		assertThat(eh.goForward()).as("going forward to 'qwert'")
			.isEqualTo(TEXT_QWERT);

		assertThat(eh.canGoBack()).as("can go back after returned to forward most entry (2 entries total)")
			.isTrue();
		assertThat(eh.canGoForward()).as("can go forward after returned to forward most entry (2 entries total)")
			.isFalse();
	}
	*/

	@Test
	public void should_goToBackMost_whenAdded2Entries() {
		// given
		EditHistory eh = new EditHistory(DEF_MAX_ENTRIES, TEXT_QWE);

		// when
		eh.addEntry(TEXT_QWER);
		eh.addEntry(TEXT_QWERT);

		// should
		assertThat(eh.goBack()).as("going back to 'qwer'")
			.isEqualTo(TEXT_QWER);
		assertThat(eh.goBack()).as("going back to 'qwe'")
			.isEqualTo(TEXT_QWE);
		assertThat(eh.canGoBack()).as("can go back more after already going 2 entries back (2 entries total)")
			.isFalse();
		assertThat(eh.canGoForward()).as("can go forward after already going 2 entries back (2 entries total)")
			.isTrue();
	}

	@Test
	public void should_goToFrontMost_whenWent2EntriesBack_givenHas2Entries(){
		// given
		EditHistory eh = new EditHistory(DEF_MAX_ENTRIES, TEXT_QWE);
		eh.addEntry(TEXT_QWER);
		eh.addEntry(TEXT_QWERT);

		// when
		eh.goBack();
		eh.goBack();

		// should
		assertThat(eh.goForward()).as("going forward to 'qwer'")
			.isEqualTo(TEXT_QWER);
		assertThat(eh.goForward()).as("going forward to 'qwert'")
			.isEqualTo(TEXT_QWERT);
		assertThat(eh.canGoBack()).as("can go back after returned to forward most entry (2 entries total)")
			.isTrue();
		assertThat(eh.canGoForward()).as("can go forward after returned to forward most entry (2 entries total)")
			.isFalse();
	}


	//  [qwe->qwer]{qwer->qwert} qwert
	//  {qwe->qwer}[qwer->qwert] qwer
	//  [qwe->qwer]{qwer->qwera} qwera

	@Test
	public void should_discardEntriesInFrontOfTheNewOne(){
		// given
		EditHistory eh = new EditHistory(DEF_MAX_ENTRIES, TEXT_QWE);
		eh.addEntry(TEXT_QWER);
		eh.addEntry(TEXT_QWERT);

		// when
		eh.goBack();
		eh.addEntry(TEXT_QWERA);

		// should
		assertThat(eh.canGoBack()).as("can go back after adding new entry not in the front")
			.isTrue();
		assertThat(eh.canGoForward()).as("can go forward after adding new entry not in the front")
			.isFalse();

		assertThat(eh.goBack()).as("going back to 'qwer'")
			.isEqualTo(TEXT_QWER);
		assertThat(eh.goBack()).as("going back to 'qwe'")
			.isEqualTo(TEXT_QWE);
		assertThat(eh.goForward()).as("going forward to 'qwer'")
			.isEqualTo(TEXT_QWER);
		assertThat(eh.goForward()).as("going forward to 'qwera' (and not 'qwert')")
			.isEqualTo(TEXT_QWERA);

		assertThat(eh.canGoBack()).as("can go back after reaching the front most entry")
			.isTrue();
		assertThat(eh.canGoForward()).as("can go forward after reaching the front most entry")
			.isFalse();
	}

	//  [qwe->qwer]{qwer->qwert} qwert
	//  {qwe->qwer}[qwer->qwert] qwer
	//{}[qwe->qwer][qwer->qwera] qwe
	//  {qwe->qwea} qwea

	@Test
	public void should_discardAllEntries_whenAddEntryAtBackMostPosition(){
		// given
		EditHistory eh = new EditHistory(DEF_MAX_ENTRIES, TEXT_QWE);
		eh.addEntry(TEXT_QWER);
		eh.addEntry(TEXT_QWERT);

		// when
		eh.goBack();
		eh.goBack();
		eh.addEntry(TEXT_QWEA);

		// should
		assertThat(eh.canGoBack()).as("can go back after adding new entry at the back most position")
			.isTrue();
		assertThat(eh.canGoForward()).as("can go forward after adding new entry at the back most position")
			.isFalse();

		assertThat(eh.goBack()).as("going back to 'qwe'")
			.isEqualTo(TEXT_QWE);
		assertThat(eh.canGoBack()).as("can go back after going back (now only 1 entry total)")
			.isFalse();
		assertThat(eh.canGoForward()).as("can go forward after going back (now only 1 entry total)")
			.isTrue();
		assertThat(eh.goForward()).as("going forward to 'qwea'")
			.isEqualTo(TEXT_QWEA);
		assertThat(eh.canGoBack()).as("can go back after reaching forward most entry (only 1 entry total)")
			.isTrue();
		assertThat(eh.canGoForward()).as("can go forward after reaching forward most entry (only 1 entry total)")
			.isFalse();
	}

	@Test
	public void should_discardBackMost_whenAddingEntry_givenReachedMaxSize(){
		// given
		EditHistory eh = new EditHistory(MAX_ENTRIES_2, TEXT_QWE);
		eh.addEntry(TEXT_QWER);
		eh.addEntry(TEXT_QWERT);

		// when
		eh.addEntry(TEXT_QWERTY);

		// should
		assertThat(eh.goBack()).as("going back to 'qwert'")
			.isEqualTo(TEXT_QWERT);
		assertThat(eh.goBack()).as("going back to 'qwer'")
			.isEqualTo(TEXT_QWER);
		assertThat(eh.canGoBack()).as("can go back more after going 2 entries back (2 entries total, 2 entries max)")
			.isFalse();
		assertThat(eh.canGoForward()).as("can go forward after going 2 entries back (2 entries total, 2 entries max)")
			.isTrue();
		assertThat(eh.goForward()).as("going forward to 'qwert'")
			.isEqualTo(TEXT_QWERT);
		assertThat(eh.goForward()).as("going forward to 'qwerty'")
			.isEqualTo(TEXT_QWERTY);
		assertThat(eh.canGoBack()).as("can go back after reaching forward most (2 entries total, 2 entries max)")
			.isTrue();
		assertThat(eh.canGoForward()).as("can go forward after reaching forward most (2 entries total, 2 entries max)")
			.isFalse();
	}
}