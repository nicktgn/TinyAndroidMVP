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

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.LinkedList;

public class EditHistory implements Parcelable {

	private static class Entry implements Parcelable{
		public LinkedList<diff_match_patch.Diff> diff;

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeTypedList(diff);
		}

		public Entry(LinkedList<diff_match_patch.Diff> diff) {
			this.diff = diff;
		}

		protected Entry(Parcel in) {
			this.diff = new LinkedList<diff_match_patch.Diff>(in.createTypedArrayList(diff_match_patch.Diff.CREATOR));
		}

		public static final Creator<Entry> CREATOR = new Creator<Entry>() {
			public Entry createFromParcel(Parcel source) {
				return new Entry(source);
			}

			public Entry[] newArray(int size) {
				return new Entry[size];
			}
		};
	}

	private String lastText;
	private int maxSize;

	private ArrayList<Entry> diffs;

	private int lastEntry = -1;

	private diff_match_patch dmp;

	public EditHistory(int maxSize, String originalText){
		this.maxSize = maxSize;
		lastText = originalText;
		diffs = new ArrayList<Entry>();
		dmp = new diff_match_patch();
	}

	public boolean canGoBack(){
		return lastEntry != -1;
	}
	public boolean canGoForward(){
		return lastEntry != (diffs.size() - 1);
	}

	public void addEntry(String newText){
		LinkedList<diff_match_patch.Diff> diff = dmp.diff_main(newText, lastText);
		dmp.diff_cleanupEfficiency(diff);
		Entry entry = new Entry(diff);

		// if last entry is not the forward most one
		// remove everything in front of it
		for(int i = diffs.size()-1; i>lastEntry; i--){
			diffs.remove(i);
		}

		// if history has reached its maxSize
		// remove back most entry to accommodate a new one at the front
		if(diffs.size() == maxSize){
			diffs.remove(0);
		}

		diffs.add(entry);
		lastEntry = diffs.size() - 1;
		lastText = newText;
	}

	public String goBack(){
		if(lastEntry == -1)
			throw new IllegalStateException("No more entries back in history");

		Entry entry = diffs.get(lastEntry);
		lastEntry--;
		return lastText = dmp.diff_text2(entry.diff);
	}

	public String goForward(){
		if(lastEntry == diffs.size() - 1){
			throw new IllegalStateException("No more entries forward in history");
		}
		lastEntry++;
		Entry entry = diffs.get(lastEntry);
		return lastText = dmp.diff_text1(entry.diff);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.maxSize);
		dest.writeTypedList(diffs);
		dest.writeString(this.lastText);
		dest.writeInt(this.lastEntry);
	}

	protected EditHistory(Parcel in) {
		this.maxSize = in.readInt();
		this.diffs = in.createTypedArrayList(Entry.CREATOR);
		this.lastText = in.readString();
		this.lastEntry = in.readInt();
	}

	public static final Creator<EditHistory> CREATOR = new Creator<EditHistory>() {
		public EditHistory createFromParcel(Parcel source) {
			return new EditHistory(source);
		}

		public EditHistory[] newArray(int size) {
			return new EditHistory[size];
		}
	};
}
