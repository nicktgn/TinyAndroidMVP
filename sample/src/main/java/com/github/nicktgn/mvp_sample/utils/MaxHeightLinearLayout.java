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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.github.nicktgn.mvp_sample.R;

public class MaxHeightLinearLayout extends LinearLayout{

	private int maxHeight = 0;

	public MaxHeightLinearLayout(Context context) {
		super(context);
	}

	public MaxHeightLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		readAttributes(context, attrs);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public MaxHeightLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		readAttributes(context, attrs);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public MaxHeightLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		readAttributes(context, attrs);
	}

	private void readAttributes(Context context, AttributeSet attrs){
		TypedArray a = context.getTheme().obtainStyledAttributes(
			attrs,
			R.styleable.MaxHeightLinearLayout,
			0, 0);

		try {
			setMaxHeight(a.getDimensionPixelSize(R.styleable.MaxHeightLinearLayout_maxHeight, 0));

		} finally {
			a.recycle();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (maxHeight > 0){
			int hSize = MeasureSpec.getSize(heightMeasureSpec);
			int hMode = MeasureSpec.getMode(heightMeasureSpec);

			switch (hMode){
				case MeasureSpec.AT_MOST:
					heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(hSize, maxHeight), MeasureSpec.AT_MOST);
					break;
				case MeasureSpec.UNSPECIFIED:
					heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
					break;
				case MeasureSpec.EXACTLY:
					heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(hSize, maxHeight), MeasureSpec.EXACTLY);
					break;
			}
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public void setMaxHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}


}
