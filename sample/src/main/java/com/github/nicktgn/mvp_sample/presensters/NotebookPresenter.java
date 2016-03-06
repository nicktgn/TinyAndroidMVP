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
import com.github.nicktgn.mvp.MvpContext;
import com.github.nicktgn.mvp.MvpBaseCtxPresenter;
import com.github.nicktgn.mvp.MvpView;
import com.github.nicktgn.mvp_sample.models.NoteModel;
import com.github.nicktgn.mvp_sample.models.NotebookModel;
import com.github.nicktgn.mvp_sample.models.NotebooksProvider;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;


public class NotebookPresenter extends MvpBaseCtxPresenter<NotebookPresenter.NotebookView, NotebookPresenter.NotebookCtx> {
	//private static final Logger logger = LoggerManager.getLogger(NotePresenter.class.getName());

	// use some tag for storing your  data in the MvpBundle
	static final String DATA_NOTEBOOK_IDX = "data_notebook_idx";

	public interface NotebookCtx extends MvpContext {
		String getDefaultNotebookTitle();
	}

	public interface NotebookView extends MvpView {
		void showTitle(String title);
		void showNotes(int numNotes);
		void gotoNote(MvpBundle noteModel);
	}

	public interface NoteView extends MvpView{
		void showTitle(String title);
		void showContent(String content);
	}

	private NotebookModel mNotebook;
	private NotebooksProvider mProvider;
	private int notebookIdx = -1;

	public NotebookPresenter(NotebookCtx ctx, NotebooksProvider provider){
		super(ctx);
		mProvider = provider;
	}

	@Override
	public MvpBundle saveState() {
		// put your state data under some tag in the MvpBundle
		MvpBundle bundle = new MvpBundle();
		bundle.putInt(DATA_NOTEBOOK_IDX, notebookIdx);
		return bundle;
	}

	@Override
	protected void present(MvpBundle arguments, MvpBundle savedState) {
		if(mNotebook == null){
			notebookIdx = (arguments != null) ? arguments.getInt(DATA_NOTEBOOK_IDX) :
								(savedState != null) ? savedState.getInt(DATA_NOTEBOOK_IDX) : -1;

			if (notebookIdx == -1) {
				mNotebook = new NotebookModel(getContext().getDefaultNotebookTitle());
				mProvider.addNotebook(mNotebook);
				notebookIdx = mProvider.getNumNotebooks() - 1;
			} else {
				mNotebook = mProvider.getNotebook(notebookIdx);
			}
		}

		getView().showTitle(mNotebook.getTitle());
		getView().showNotes(mNotebook.getNumNotes());
	}

	@Override
	public void presentSubView(MvpView view, int index) {
		NoteView noteView = (NoteView) view;
		NoteModel note = mNotebook.getNote(index);
		noteView.showTitle(note.getTitle());
		noteView.showContent(note.getContent());
	}

	public void openNote(int index){
		// get special marked bundle intended for presenter arguments
		MvpBundle bundle = new MvpBundle();
		bundle.putInt(NotePresenter.DATA_NOTE_IDX, index);
		bundle.putInt(NotePresenter.DATA_NOTEBOOK_IDX, notebookIdx);
		getView().gotoNote(bundle);
	}

	public void createNote(String defaultNoteName){
		NoteModel note = new NoteModel(defaultNoteName);
		mNotebook.addNote(note);
		// get special marked bundle intended for presenter arguments
		MvpBundle bundle = new MvpBundle();
		bundle.putInt(NotePresenter.DATA_NOTE_IDX, mNotebook.getNumNotes() - 1);
		bundle.putInt(NotePresenter.DATA_NOTEBOOK_IDX, notebookIdx);
		getView().gotoNote(bundle);
	}

	public void deleteNote(int index){
		mNotebook.removeNote(index);
		getView().showNotes(mNotebook.getNumNotes());
	}
}
