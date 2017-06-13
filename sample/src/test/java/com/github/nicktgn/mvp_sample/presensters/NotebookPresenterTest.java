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
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NotebookPresenterTest {

	private static final String DEF_NOTEBOOK_TITLE = "Default notebook title";
	private static final String DEF_NOTE_TITLE = "Default note title";
	private static final int DEF_NOTEBOOK_IDX = -1;

	private static final String SAMPLE_NOTEBOOK_TITLE = "Sample notebook title";

	private static final String SAMPLE_NOTE_TITLE1 = "Sample note title 1";
	private static final String SAMPLE_NOTE_CONTENT1 = "Sample note content 1";

	private static final String SAMPLE_NOTE_TITLE2 = "Sample note title 2";
	private static final String SAMPLE_NOTE_CONTENT2 = "Sample note content 2";

	NotebooksProvider providerMock;
	NotebookModel notebookModelMock;

	NoteModel noteModelMock1;
	NoteModel noteModelMock2;

	NotebookPresenter.NotebookCtx notebookCtxMock;
	NotebookPresenter.NotebookView notebookViewMock;
	NotebookPresenter.NoteView noteViewMock1;
	NotebookPresenter.NoteView noteViewMock2;

	MvpBundle arguments;

	NotebookPresenter presenter;

	public void given_default(){
		// given

		notebookModelMock = mock(NotebookModel.class);
		when(notebookModelMock.getNumNotes()).thenReturn(0);
		when(notebookModelMock.getNote(0)).thenReturn(null);

		providerMock = mock(NotebooksProvider.class);
		when(providerMock.getNumNotebooks()).thenReturn(1);
		when(providerMock.getNotebook(0)).thenReturn(notebookModelMock);

		notebookCtxMock = mock(NotebookPresenter.NotebookCtx.class);
		when(notebookCtxMock.getDefaultNotebookTitle()).thenReturn(DEF_NOTEBOOK_TITLE);

		notebookViewMock = mock(NotebookPresenter.NotebookView.class);

		presenter = new NotebookPresenter(notebookCtxMock, providerMock);

		arguments = new MvpBundle();
	}

	public void given_sampleNotebook(){

		noteModelMock1 = mock(NoteModel.class);
		when(noteModelMock1.getContent()).thenReturn(SAMPLE_NOTE_CONTENT1);
		when(noteModelMock1.getTitle()).thenReturn(SAMPLE_NOTE_TITLE1);

		noteModelMock2 = mock(NoteModel.class);
		when(noteModelMock2.getContent()).thenReturn(SAMPLE_NOTE_CONTENT2);
		when(noteModelMock2.getTitle()).thenReturn(SAMPLE_NOTE_TITLE2);

		when(notebookModelMock.getNumNotes()).thenReturn(2);
		when(notebookModelMock.getNote(0)).thenReturn(noteModelMock1);
		when(notebookModelMock.getNote(1)).thenReturn(noteModelMock2);

		noteViewMock1 = mock(NotebookPresenter.NoteView.class);
		noteViewMock2 = mock(NotebookPresenter.NoteView.class);

	}

	@Test
	public void should_presentEmptyNotebook_whenHasNoArguments(){
		// given
		given_default();

		// when
		presenter.attachView(notebookViewMock, null, null);

		// should
		verify(notebookCtxMock).getDefaultNotebookTitle();

		verify(notebookViewMock).showTitle(DEF_NOTEBOOK_TITLE);
		verify(notebookViewMock).showNotes(0);
	}

	@Test
	public void should_presentEmptyNotebook_whenHasDefaultArguments(){
		// given
		given_default();
		arguments.putInt(NotebookPresenter.DATA_NOTEBOOK_IDX, DEF_NOTEBOOK_IDX);

		// when
		presenter.attachView(notebookViewMock, null, null);

		// should
		verify(notebookCtxMock).getDefaultNotebookTitle();

		verify(notebookViewMock).showTitle(DEF_NOTEBOOK_TITLE);
		verify(notebookViewMock).showNotes(0);
	}

	@Test
	public void should_presentSampleTitleAndListTwoNotes(){
		// given
		given_default();
		given_sampleNotebook();
		arguments.putInt(NotebookPresenter.DATA_NOTEBOOK_IDX, 0);

		// when
		presenter.attachView(notebookViewMock, null, null);

		// should
		verify(notebookViewMock).showTitle(SAMPLE_NOTEBOOK_TITLE);
		verify(notebookViewMock).showNotes(2);
	}

	@Test
	public void should_presentTitleAndContentForEachNote(){
		// given
		given_default();
		given_sampleNotebook();
		arguments.putInt(NotebookPresenter.DATA_NOTEBOOK_IDX, 0);
		presenter.attachView(notebookViewMock, null, null);

		// when
		presenter.presentSubView(noteViewMock1, 0);
		presenter.presentSubView(noteViewMock2, 1);

		// should
		verify(noteViewMock1).showTitle(SAMPLE_NOTE_TITLE1);
		verify(noteViewMock1).showContent(SAMPLE_NOTE_CONTENT1);

		verify(noteViewMock2).showTitle(SAMPLE_NOTE_TITLE2);
		verify(noteViewMock2).showContent(SAMPLE_NOTE_CONTENT2);
	}


	@Test
	public void should_requestViewToOpenNote(){
		// given
		given_default();
		given_sampleNotebook();
		arguments.putInt(NotebookPresenter.DATA_NOTEBOOK_IDX, 0);
		presenter.attachView(notebookViewMock, null, null);

		// when
		presenter.openNote(1);

		// should
		ArgumentCaptor<MvpBundle> arg = ArgumentCaptor.forClass(MvpBundle.class);
		verify(notebookViewMock).gotoNoteView(arg.capture());
		assertThat(arg.getValue().getInt(NotePresenter.DATA_NOTEBOOK_IDX))
			.as("notebook index should be 0")
			.isEqualTo(0);
		assertThat(arg.getValue().getInt(NotePresenter.DATA_NOTE_IDX))
			.as("note index should be 1")
			.isEqualTo(1);
	}

	@Test
	public void should_createNewNote_addItToNotebook_requestViewToOpenIt(){
		// given
		given_default();
		given_sampleNotebook();
		arguments.putInt(NotebookPresenter.DATA_NOTEBOOK_IDX, 0);
		presenter.attachView(notebookViewMock, null, null);

		// when
		presenter.createNote(DEF_NOTE_TITLE);

		// should
		ArgumentCaptor<NoteModel> createNoteArg = ArgumentCaptor.forClass(NoteModel.class);
		verify()

		ArgumentCaptor<MvpBundle> arg = ArgumentCaptor.forClass(MvpBundle.class);
		verify(notebookViewMock).gotoNoteView(arg.capture());
		assertThat(arg.getValue().getInt(NotePresenter.DATA_NOTEBOOK_IDX))
			.as("notebook index should be 0")
			.isEqualTo(0);
		assertThat(arg.getValue().getInt(NotePresenter.DATA_NOTE_IDX))
			.as("note index should be 1")
			.isEqualTo(1);
	}
}