/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.handmark.pulltorefresh.library;

import android.annotation.TargetApi;
import android.util.Log;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;

@TargetApi(9)
public final class OverscrollHelper {

	static final String LOG_TAG = "OverscrollHelper";
	static final float DEFAULT_OVERSCROLL_SCALE = 1f;

	/**
	 * Helper method for Overscrolling that encapsulates all of the necessary
	 * function.
	 * <p/>
	 * This should only be used on AdapterView's such as ListView as it just
	 * calls through to overScrollBy() with the scrollRange = 0. AdapterView's
	 * do not have a scroll range (i.e. getScrollY() doesn't work).
	 * 
	 * @param view - PullToRefreshView that is calling this.
	 * @param deltaX - Change in X in pixels, passed through from from
	 *            overScrollBy call
	 * @param scrollX - Current X scroll value in pixels before applying deltaY,
	 *            passed through from from overScrollBy call
	 * @param deltaY - Change in Y in pixels, passed through from from
	 *            overScrollBy call
	 * @param scrollY - Current Y scroll value in pixels before applying deltaY,
	 *            passed through from from overScrollBy call
	 * @param isTouchEvent - true if this scroll operation is the result of a
	 *            touch event, passed through from from overScrollBy call
	 */
	public static void overScrollBy(final PullToRefreshBase<?> view, final int 