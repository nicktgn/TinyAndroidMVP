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

package com.github.nicktgn.mvp_sample.presensters;



import com.github.nicktgn.mvp.MvpBundle;
import com.github.nicktgn.mvp_sample.models.NoteModel;
import com.github.nicktgn.mvp_sample.models.NotebookModel;
import com.github.nicktgn.mvp_sample.models.NotebooksProvider;

import org.junit.Test;
import org.mockito.InOrder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.withSettings;

/**
 * Created by nick on 2/29/16.
 */
public class NotePresenterTest {

	private static final int DEF_NOTEBOOK_IDX = -1;
	private static final int DEF_NOTE_IDX = -1;

	private static final int NOTEBOOK_IDX = 0;
	private static final int NOTE_IDX = 0;

	private static final int DEF_MAX_HISTORY_SIZE = 20;
	private static final int MAX_HISTORY_SIZE_OF_2 = 2;

	private static final String DEF_TITLE = "Default title";
	private static final String EMPTY_TITLE = "";
	private static final String NOT_DEF_TITLE = "Not default title";
	private static final String DEF_CONTENT = "Default content";
	private static final String EMPTY_CONTENT = "";
	private static final String NOT_DEF_CONTENT = "Not default content";

	private static final String CONTENT_1 = "Content 1";
	private static final String CONTENT_12 = "Content 12";
	private static final String CONTENT_123 = "Content 123";
	private static final String CONTENT_12A = "Content 12A";

	NotePresenter.NoteCtx mockedNoteCtx;
	NotePresenter.NoteView mockedNoteView;
	NotebooksProvider mockedNotebookProvider;
	NotebookModel mockedNotebook;
	NoteModel mockedNote;
	MvpBundle arguments;
	//Bundle mockedState;
	NotePresenter np;

	private void given_defaults(){
		// given

		// ### can use withSettings().verboseLogging() for debugging interactions
		//mockedNoteView = mock(NotePresenter.NoteView.class, withSettings().verboseLogging());
		mockedNoteView = mock(NotePresenter.NoteView.class);

		mockedNoteCtx = mock(NotePresenter.NoteCtx.class);
		when(mockedNoteCtx.getDefaultTitleRes()).thenReturn(DEF_TITLE);
		when(mockedNoteCtx.getDefaultMaxHistorySize()).thenReturn(DEF_MAX_HISTORY_SIZE);

		mockedNote = mock(NoteModel.class);
		when(mockedNote.getTitle()).thenReturn(DEF_TITLE);
		when(mockedNote.getContent()).thenReturn(DEF_CONTENT);

		mockedNotebook = mock(NotebookModel.class);
		when(mockedNotebook.getNote(NOTE_IDX)).thenReturn(mockedNote);
		when(mockedNotebook.getNumNotes()).thenReturn(1);

		mockedNotebookProvider = mock(NotebooksProvider.class);
		when(mockedNotebookProvider.getNotebook(NOTEBOOK_IDX)).thenReturn(mockedNotebook);
		when(mockedNotebookProvider.getNumNotebooks()).thenReturn(1);

		//mockedArguments = new MvpBundle();
		//when(mockedArguments.getInt(NotePresenter.DATA_NOTEBOOK_IDX)).thenReturn(DEF_NOTEBOOK_IDX);
		//when(mockedArguments.getInt(NotePresenter.DATA_NOTE_IDX)).thenReturn(DEF_NOTE_IDX);
		arguments = new MvpBundle();
		arguments.putInt(NotePresenter.DATA_NOTEBOOK_IDX, NOTEBOOK_IDX);
		arguments.putInt(NotePresenter.DATA_NOTE_IDX, NOTE_IDX);
	}

	@Test
	 public void should_throwExceptionIfNoValidArgsProvided(){
		// given
		given_defaults();
		np = new NotePresenter(mockedNoteCtx, mockedNotebookProvider);

		// when
		try {
			np.attachView(mockedNoteView, null, null);
			failBecauseExceptionWasNotThrown(Exception.class);
		} catch(Exception e){

			// should
			assertThat(e).isNotNull();
		}


		// given
		np = new NotePresenter(mockedNoteCtx, mockedNotebookProvider);
		arguments.putInt(NotePresenter.DATA_NOTEBOOK_IDX, DEF_NOTEBOOK_IDX);
		arguments.putInt(NotePresenter.DATA_NOTE_IDX, DEF_NOTE_IDX);

		// when
		try {
			np.attachView(mockedNoteView, arguments, null);
			failBecauseExceptionWasNotThrown(Exception.class);
		} catch(Exception e){

			// should
			assertThat(e).isNotNull();
		}

	}

	@Test
	public void should_presentTitleContentAndDisableUndoRedo(){
		// given
		given_defaults();
		np = new NotePresenter(mockedNoteCtx, mockedNotebookProvider);

		// when
		np.attachView(mockedNoteView, arguments, null);

		// should
		verify(mockedNoteView).showTitle(DEF_TITLE);
		verify(mockedNoteView).showContent(DEF_CONTENT);
		verify(mockedNoteView).enableUndo(false);
		verify(mockedNoteView).enableRedo(false);

	}

	@Test
	public void should_startEditingTitle_whenDefTitle() {
		// given
		// title is same as NoteContext#getDefaultTitleRes()
		given_defaults();
		np = new NotePresenter(mockedNoteCtx, mockedNotebookProvider);

		// when
		np.attachView(mockedNoteView, arguments, null);

		// should
		verify(mockedNoteView).startEditingTitle();
		verify(mockedNoteView, never()).startEditingContent();

	}

	@Test
	public void should_startEditingTitle_whenEmptyTitle(){
		// given
		given_defaults();
		when(mockedNote.getTitle()).thenReturn(EMPTY_TITLE);
		np = new NotePresenter(mockedNoteCtx, mockedNotebookProvider);

		// when
		np.attachView(mockedNoteView, arguments, null);

		// should
		verify(mockedNoteView).startEditingTitle();
		verify(mockedNoteView, never()).startEditingContent();

	}

	@Test
	public void should_startEditingContent() {
		// given
		given_defaults();
		when(mockedNote.getTitle()).thenReturn(NOT_DEF_TITLE);
		when(mockedNote.getContent()).thenReturn(EMPTY_CONTENT);
		np = new NotePresenter(mockedNoteCtx, mockedNotebookProvider);

		// when
		np.attachView(mockedNoteView, arguments, null);

		// should
		verify(mockedNoteView, never()).startEditingTitle();
		verify(mockedNoteView).startEditingContent();

	}

	@Test
	public void should_restoreState(){
		// given
		given_defaults();
		np = new NotePresenter(mockedNoteCtx, mockedNotebookProvider);
		np.attachView(mockedNoteView, arguments, null);
		np.saveContent(NOT_DEF_CONTENT);

		when(mockedNote.getContent()).thenReturn(NOT_DEF_CONTENT);

		// when
		MvpBundle state = np.saveState();
		np.detachView(false);

		np = new NotePresenter(mockedNoteCtx, mockedNotebookProvider);
		np.attachView(mockedNoteView, null, state);


		InOrder inOrder = inOrder(mockedNoteView);
		InOrder inOrder1 = inOrder(mockedNoteView);
		InOrder inOrder2 = inOrder(mockedNoteView);
		InOrder inOrder3 = inOrder(mockedNoteView);

		// should
		inOrder.verify(mockedNoteView).showTitle(DEF_TITLE);
		inOrder1.verify(mockedNoteView).showContent(NOT_DEF_CONTENT);
		inOrder2.verify(mockedNoteView).enableUndo(true);
		inOrder3.verify(mockedNoteView).enableRedo(false);

		np.undoContentEdit();

		inOrder1.verify(mockedNoteView).showContent(DEF_CONTENT);
		inOrder2.verify(mockedNoteView).enableUndo(false);
		inOrder3.verify(mockedNoteView).enableRedo(true);

	}

	@Test
	public void should_saveChangedTitleBackToNoteModel(){
		// given
		given_defaults();
		np = new NotePresenter(mockedNoteCtx, mockedNotebookProvider);
		np.attachView(mockedNoteView, arguments, null);

		// when
		np.saveTitle(NOT_DEF_TITLE);

		// should
		verify(mockedNote).setTitle(NOT_DEF_TITLE);

	}

	@Test
	public void should_saveChangedContentBackToNoteModel(){
		// given
		given_defaults();
		np = new NotePresenter(mockedNoteCtx, mockedNotebookProvider);
		np.attachView(mockedNoteView, arguments, null);

		// when
		np.saveContent(NOT_DEF_CONTENT);

		// should
		verify(mockedNote).setContent(NOT_DEF_CONTENT);

	}

	// history related tests

	@Test
	public void should_throwException_whenTryingUndoOrRedo_givenNoChangesMade(){
		// given
		given_defaults();
		np = new NotePresenter(mockedNoteCtx, mockedNotebookProvider);
		np.attachView(mockedNoteView, arguments, null);

		// when
		try{
			np.undoContentEdit();

			// should
			failBecauseExceptionWasNotThrown(IllegalStateException.class);
		} catch (Exception e){
			// should
			assertThat(e).isInstanceOf(IllegalStateException.class);
		}

		// when
		try{
			np.redoContentEdit();

			// should
			failBecauseExceptionWasNotThrown(IllegalStateException.class);
		} catch (Exception e){
			// should
			assertThat(e).isInstanceOf(IllegalStateException.class);
		}

	}

	@Test
	public void should_enableUndoButNotRedo_whenDone1Change(){
		// given
		given_defaults();
		np = new NotePresenter(mockedNoteCtx, mockedNotebookProvider);
		np.attachView(mockedNoteView, arguments, null);

		// ### Because enableUndo and enableRedo are going to be also called when view is attached
		// ### In future no need for these explanations, it might! be a good practice to clear invocations
		// ### as a part of the "given" step (but nowhere else!)
		// ### In general think twice about using this, if in doubt, for justification of use of
		// ### this function consult:
		// ### https://github.com/mockito/mockito/issues/183 (and its links to other discussions threads)
		clearInvocations(mockedNoteView);

		// when
		np.saveContent(NOT_DEF_CONTENT);

		// should
		verify(mockedNoteView).enableUndo(true);
		verify(mockedNoteView).enableRedo(false);

	}

	@Test
	public void should_changeBackToOriginalContent_given1Change(){
		// given
		given_defaults();
		np = new NotePresenter(mockedNoteCtx, mockedNotebookProvider);
		np.attachView(mockedNoteView, arguments, null);
		np.saveContent(NOT_DEF_CONTENT);

		clearInvocations(mockedNoteView);

		// when
		np.undoContentEdit();

		// should
		verify(mockedNoteView).showContent(DEF_CONTENT);
		verify(mockedNoteView).enableUndo(false);
		verify(mockedNoteView).enableRedo(true);

	}

	@Test
	public void should_redoChangeToNewContent_given1ChangeAnd1Undo(){
		// given
		given_defaults();
		np = new NotePresenter(mockedNoteCtx, mockedNotebookProvider);
		np.attachView(mockedNoteView, arguments, null);
		np.saveContent(NOT_DEF_CONTENT);
		np.undoContentEdit();

		clearInvocations(mockedNoteView);

		// when
		np.redoContentEdit();

		// should
		verify(mockedNoteView).showContent(NOT_DEF_CONTENT);
		verify(mockedNoteView).enableUndo(true);
		verify(mockedNoteView).enableRedo(false);

	}




	@Test
	public void should_enableUndoAndRedo_whenUndone1Change_given2Changes(){
		// given
		given_defaults();
		np = new NotePresenter(mockedNoteCtx, mockedNotebookProvider);
		np.attachView(mockedNoteView, arguments, null);
		np.saveContent(CONTENT_1);
		np.saveContent(CONTENT_12);

		clearInvocations(mockedNoteView);

		// when
		np.undoContentEdit();

		// should
		verify(mockedNoteView).enableUndo(true);
		verify(mockedNoteView).enableRedo(true);

	}


	@Test
	public void should_changeToOriginalText_whenUndone2Times_givenHas2Changes(){
		// given
		given_defaults();
		np = new NotePresenter(mockedNoteCtx, mockedNotebookProvider);
		np.attachView(mockedNoteView, arguments, null);
		np.saveContent(CONTENT_1);
		np.saveContent(CONTENT_12);

		//clearInvocations(mockedNoteView);

		// ### you can change clearInvocations in this case (and in most cases here) to
		// ### checking a strict order of invocations
		InOrder inOrder = inOrder(mockedNoteView);
		InOrder inOrder2 = inOrder(mockedNoteView);
		InOrder inOrder3 = inOrder(mockedNoteView);

		// when
		np.undoContentEdit();
		np.undoContentEdit();

		verify(mockedNoteView).enableRedo(true);
		verify(mockedNoteView).enableRedo(true);

		// should
		inOrder.verify(mockedNoteView).showContent(CONTENT_1);
		inOrder2.verify(mockedNoteView).enableUndo(true);
		inOrder3.verify(mockedNoteView).enableRedo(true);

		inOrder.verify(mockedNoteView).showContent(DEF_CONTENT);
		inOrder2.verify(mockedNoteView).enableUndo(false);
		inOrder3.verify(mockedNoteView).enableRedo(true);

	}

	@Test
	public void should_revertChangesToNewestText_whenRedone2Times_givenHas2ChangesAndUndone2Times(){
		// given
		given_defaults();
		np = new NotePresenter(mockedNoteCtx, mockedNotebookProvider);
		np.attachView(mockedNoteView, arguments, null);
		np.saveContent(CONTENT_1);
		np.saveContent(CONTENT_12);
		np.undoContentEdit();
		np.undoContentEdit();

		// ### you can change clearInvocations in this case (and in most cases here) to
		// ### checking a strict order of invocations
		InOrder inOrder = inOrder(mockedNoteView);
		InOrder inOrder2 = inOrder(mockedNoteView);
		InOrder inOrder3 = inOrder(mockedNoteView);

		// when
		np.redoContentEdit();
		np.redoContentEdit();

		// should
		inOrder.verify(mockedNoteView).showContent(CONTENT_1);
		inOrder2.verify(mockedNoteView).enableUndo(true);
		inOrder3.verify(mockedNoteView).enableRedo(true);

		inOrder.verify(mockedNoteView).showContent(CONTENT_12);
		inOrder2.verify(mockedNoteView).enableUndo(true);
		inOrder3.verify(mockedNoteView).enableRedo(false);

	}

	@Test
	public void should_discardChangesDoneAfter_whenMadeChangeInTheMiddleOfHistory(){
		// given
		given_defaults();
		np = new NotePresenter(mockedNoteCtx, mockedNotebookProvider);
		np.attachView(mockedNoteView, arguments, null);
		np.saveContent(CONTENT_1);
		np.saveContent(CONTENT_12);
		np.undoContentEdit();

		InOrder inOrder = inOrder(mockedNoteView);
		InOrder inOrder2 = inOrder(mockedNoteView);
		InOrder inOrder3 = inOrder(mockedNoteView);

		// when
		np.saveContent(CONTENT_12A);

		// should
		inOrder2.verify(mockedNoteView).enableUndo(true);
		inOrder3.verify(mockedNoteView).enableRedo(false);

		np.undoContentEdit();
		inOrder.verify(mockedNoteView).showContent(CONTENT_1);
		inOrder2.verify(mockedNoteView).enableUndo(true);
		inOrder3.verify(mockedNoteView).enableRedo(true);

		np.redoContentEdit();
		inOrder.verify(mockedNoteView).showContent(CONTENT_12A);
		inOrder2.verify(mockedNoteView).enableUndo(true);
		inOrder3.verify(mockedNoteView).enableRedo(false);

	}


	@Test
	public void should_discardEarliestChange_whenMakingNewOne_givenReachedMaxHistorySize(){
		// given
		given_defaults();
		when(mockedNoteCtx.getDefaultMaxHistorySize()).thenReturn(MAX_HISTORY_SIZE_OF_2);
		np = new NotePresenter(mockedNoteCtx, mockedNotebookProvider);
		np.attachView(mockedNoteView, arguments, null);
		np.saveContent(CONTENT_1);
		np.saveContent(CONTENT_12);

		// when
		np.saveContent(CONTENT_123);

		// should
		InOrder inOrder = inOrder(mockedNoteView);
		InOrder inOrder2 = inOrder(mockedNoteView);
		InOrder inOrder3 = inOrder(mockedNoteView);

		np.undoContentEdit();
		inOrder.verify(mockedNoteView).showContent(CONTENT_12);
		inOrder2.verify(mockedNoteView).enableUndo(true);
		inOrder3.verify(mockedNoteView).enableRedo(true);

		np.undoContentEdit();
		inOrder.verify(mockedNoteView).showContent(CONTENT_1);
		inOrder2.verify(mockedNoteView).enableUndo(false);
		inOrder3.verify(mockedNoteView).enableRedo(true);

		np.redoContentEdit();
		inOrder.verify(mockedNoteView).showContent(CONTENT_12);
		inOrder2.verify(mockedNoteView).enableUndo(true);
		inOrder3.verify(mockedNoteView).enableRedo(true);

		np.redoContentEdit();
		inOrder.verify(mockedNoteView).showContent(CONTENT_123);
		inOrder2.verify(mockedNoteView).enableUndo(true);
		inOrder3.verify(mockedNoteView).enableRedo(false);


		np.detachView(false);
	}

}