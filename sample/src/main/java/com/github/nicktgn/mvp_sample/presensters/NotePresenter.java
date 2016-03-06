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

import android.os.Bundle;

import com.github.nicktgn.mvp.MvpBaseCtxPresenter;
import com.github.nicktgn.mvp.MvpBundle;
import com.github.nicktgn.mvp.MvpContext;
import com.github.nicktgn.mvp.MvpView;
import com.github.nicktgn.mvp_sample.models.NoteModel;
import com.github.nicktgn.mvp_sample.models.NotebooksProvider;
import com.github.nicktgn.mvp_sample.utils.EditHistory;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;

public class NotePresenter extends MvpBaseCtxPresenter<NotePresenter.NoteView, NotePresenter.NoteCtx> {
	private static final Logger logger = LoggerManager.getLogger(NotePresenter.class.getName());

	// use some tag for storing your model data in the Bundle
	static final String DATA_NOTE_IDX = "data_note_idx";
	static final String DATA_NOTEBOOK_IDX = "data_notebook_idx";

	static final String DATA_EDIT_HISTORY = "data_edit_history";

	public interface NoteCtx extends MvpContext {
		String getDefaultTitleRes();
		int getDefaultMaxHistorySize();
	}

	public interface NoteView extends MvpView{
		void showTitle(String title);
		void showContent(String content);
		void startEditingTitle();
		void startEditingContent();
		void enableUndo(boolean enable);
		void enableRedo(boolean enable);
	}

	private NoteModel mNote;
	private int noteIdx = -1;
	private int notebookIdx = -1;
	private NotebooksProvider provider;

	private EditHistory mEditHistory;

	public NotePresenter(NoteCtx ctx, NotebooksProvider provider){
		super(ctx);
		this.provider = provider;
	}

	/**
	 * Model data can also be injected directly to the presenter through constructor,
	 * or any other conventional means, instead of reading the intent or fragment arguments
	 * (see source of {@link com.github.nicktgn.mvp.MvpActivity#onCreate(Bundle)} or
	 * {@link com.github.nicktgn.mvp.MvpFragment#onActivityCreated(Bundle)})
	 * @param noteModel
	 */
	public NotePresenter(NoteCtx ctx, NoteModel noteModel) {
		super(ctx);
		// model provided when fragment(and thus presenter) is created
		if (noteModel != null) {
			mNote = noteModel;
		}
	}

	@Override
	public MvpBundle saveState() {
		// put your state data under some tag in the Bundle
		MvpBundle bundle = new MvpBundle();
		bundle.putInt(DATA_NOTE_IDX, noteIdx);
		bundle.putInt(DATA_NOTEBOOK_IDX, notebookIdx);
		bundle.putParcelable(DATA_EDIT_HISTORY, mEditHistory);
		return bundle;
	}

	@Override
	protected void present(MvpBundle arguments, MvpBundle savedState) {
		if(mNote == null){
			if(arguments != null){
				notebookIdx = arguments.getInt(DATA_NOTEBOOK_IDX, -1);
				noteIdx = arguments.getInt(DATA_NOTE_IDX, -1);
				logger.d("Got some input arguments");
			}
			else if(savedState != null){
				notebookIdx = savedState.getInt(DATA_NOTEBOOK_IDX, -1);
				noteIdx = savedState.getInt(DATA_NOTE_IDX, -1);
				mEditHistory = savedState.getParcelable(DATA_EDIT_HISTORY);
				logger.d("Got some cached data");
			}

			if(notebookIdx < 0 || notebookIdx >= provider.getNumNotebooks())
				throw new IllegalArgumentException("Can't fetch note: notebook index is not specified");
			if(noteIdx < 0 || noteIdx >= provider.getNotebook(notebookIdx).getNumNotes())
				throw new IllegalArgumentException("Can't fetch note: note index is not specified");

			mNote = provider.getNotebook(notebookIdx).getNote(noteIdx);
		}

		getView().showTitle(mNote.getTitle());
		getView().showContent(mNote.getContent());

		// set up history
		if(mEditHistory == null){
			mEditHistory = new EditHistory(getContext().getDefaultMaxHistorySize(), mNote.getContent());
		}
		getView().enableUndo(mEditHistory.canGoBack());
		getView().enableRedo(mEditHistory.canGoForward());

		if(mNote.getTitle().equals(getContext().getDefaultTitleRes())
			|| mNote.getTitle().isEmpty()){
			getView().startEditingTitle();
		}
		else if(mNote.getContent().isEmpty()){
			getView().startEditingContent();
		}
	}

	public void saveTitle(String title){
		mNote.setTitle(title);
	}

	public void saveContent(String content){
		mNote.setContent(content);
		mEditHistory.addEntry(content);
		getView().enableUndo(mEditHistory.canGoBack());
		getView().enableRedo(mEditHistory.canGoForward());
	}

	/**
	 * @throws IllegalStateException if undo is not allowed in current state
	 */
	public void undoContentEdit(){
		getView().showContent(mEditHistory.goBack());
		getView().enableUndo(mEditHistory.canGoBack());
		getView().enableRedo(mEditHistory.canGoForward());
	}

	/**
	 * @throws IllegalStateException if redo is not allowed in current state
	 */
	public void redoContentEdit(){
		getView().showContent(mEditHistory.goForward());
		getView().enableUndo(mEditHistory.canGoBack());
		getView().enableRedo(mEditHistory.canGoForward());
	}

}
