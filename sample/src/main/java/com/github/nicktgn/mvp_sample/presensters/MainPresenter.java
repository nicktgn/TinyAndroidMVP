/*
 * Copyright 2015 Nick Tsygankov (nicktgn@gmail.com)
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

import com.github.nicktgn.mvp.MvpBasePresenter;
import com.github.nicktgn.mvp.MvpBundle;
import com.github.nicktgn.mvp.MvpView;
import com.github.nicktgn.mvp_sample.models.NotebookModel;
import com.github.nicktgn.mvp_sample.models.NotebooksProvider;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;

public class MainPresenter extends MvpBasePresenter<MainPresenter.MainView> {
	private static final Logger logger = LoggerManager.getLogger(NotePresenter.class.getName());

	// use some tag for storing your model data in the Bundle
	static final String DATA_NOTEBOOKS_LIST = "data_notebooks_list";

	public interface MainView extends MvpView{
		void showNumOfNotebooks(int numOfNotebooks);
		void showNumOfNotes(int numOfNotes);
		void showNotebooksList(int numOfNotebooks);
		void gotoNotebook(MvpBundle notebook);
	}

	public interface NotebookView extends MvpView{
		void showNotebookTitle(String title);
		void showNumOfNotes(int numOfNotes);
	}

	private NotebooksProvider mProvider;

	public MainPresenter(NotebooksProvider provider){
		mProvider = provider;
		logger.d("Calling constructor");
	}

	@Override
	public MvpBundle saveState() {
		// put your state data under some tag in the Bundle
		return null;
	}

	@Override
	protected void present(MvpBundle arguments, MvpBundle savedState) {
		// if has input arguments read from it
		if(arguments != null){
			logger.d("Got some input arguments");
		}

		// if has savedState, just read from it
		if(savedState != null){
			logger.d("Got some cached data");
		}

		getView().showNumOfNotebooks(mProvider.getNumNotebooks());
		getView().showNumOfNotes(mProvider.getTotalNumNotes());
		getView().showNotebooksList(mProvider.getNumNotebooks());
	}

	@Override
	public void presentSubView(MvpView subView, int index) {
		NotebookView notebookView = (NotebookView) subView;
		NotebookModel notebook = mProvider.getNotebook(index);
		notebookView.showNotebookTitle(notebook.getTitle());
		notebookView.showNumOfNotes(notebook.getNumNotes());
	}

	public void createNotebook(){
		int notebookIdx = -1;
		MvpBundle bundle = new MvpBundle();
		bundle.putInt(NotebookPresenter.DATA_NOTEBOOK_IDX, notebookIdx);
		getView().gotoNotebook(bundle);
	}

	public void openNotebook(int index){
		MvpBundle bundle = new MvpBundle();
		bundle.putInt(NotebookPresenter.DATA_NOTEBOOK_IDX, index);
		getView().gotoNotebook(bundle);
	}

	public void removeNotebook(int index){
		mProvider.removeNotebook(index);
		getView().showNumOfNotebooks(mProvider.getNumNotebooks());
		getView().showNumOfNotes(mProvider.getTotalNumNotes());
		getView().showNotebooksList(mProvider.getNumNotebooks());
	}
}
