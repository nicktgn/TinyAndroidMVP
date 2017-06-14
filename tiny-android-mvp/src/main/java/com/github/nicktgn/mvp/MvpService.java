/*
 * Copyright $year  Nick Tsygankov (nicktgn@gmail.com)
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

package com.github.nicktgn.mvp;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;

import java.lang.reflect.InvocationTargetException;

/**
 * Abstract helper implementation of the View based on {@link Service}. Actual view
 * services can extend from this class.
 * @author nicktgn
 */
public abstract class MvpService<V extends MvpView, P extends MvpPresenter>
                                extends Service
                                implements MvpView {

    private static final Logger logger = LoggerManager.getLogger(MvpService.class.getName());

    protected P presenter;

    private MvpBundle argumentsData;
    private MvpBundle stateData;

    @Override
    public void onCreate() {
        presenter = createPresenter();

        /*
        // ---- CACHING ---- //
        // if we have savedInstanceState (activity was re-created)
        if(savedInstanceState != null){
            Bundle bundle = savedInstanceState.getBundle(Constants.CACHED_STATE_DATA);
            if(bundle != null){
                stateData = new MvpBundle(bundle);
                logger.d("Got some cached state data");
            }
        }*/

        if(!attachOnStartCommand()){
            presenter.attachView(this, argumentsData, stateData);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // ---- INPUT ARGUMENTS ---- //
        // try to get input model data from Intent (that started this Activity)
        try {
            Bundle bundle =  intent.getExtras().getBundle(Constants.ARGUMENTS_DATA);
            if(bundle != null){
                argumentsData = new MvpBundle(bundle);
                logger.d("Got some arguments data");
            }
        } catch(NullPointerException e){
            logger.d("No intent data");
        }

        if(attachOnStartCommand()){
            presenter.attachView(this, argumentsData, stateData);
        }

        return START_NOT_STICKY;
    }

    /**
     * By default view attaches itself in onCreate() and detaches itself in onDestroy().
     * You can override this method to change this behaviour and instead to attach in
     * onStartCommand() and detach in onDestroy()
     * @return true to attach in onStartCommand() and detach in onDestroy()
     */
    protected boolean attachOnStartCommand(){
        return false;
    }

    @Override
    public void onDestroy() {
        presenter.detachView(false);
    }

    /**
     * Use this helper method to create an Intent for starting another View (Activity)
     * with extras containing provided arguments indented for Presenter of this new View
     * @param arguments arguments from this View's Presenter intended for Presenter of another View
     * @return intent to another View (Activity)
     */
    protected Intent getMvpIntent(Class targetView, MvpBundle arguments){
        Intent i = new Intent(this, targetView);
        if(arguments != null){
            Bundle bundle = new Bundle();
            bundle.putBundle(Constants.ARGUMENTS_DATA, arguments.getRealBundle());
            i.putExtras(bundle);
        }
        return i;
    }

    /**
     * Use this helper method to get another View (Fragment)
     * with extras containing provided arguments indented for Presenter of this new View
     * @param arguments arguments from this View's Presenter intended for Presenter of another View
     * @return intent to another View (Activity) (or null if failed to instantiate)
     */
    protected <T extends MvpFragment> T getMvpFragment(Class<T> targetView, MvpBundle arguments){
        try {
            T fragment = targetView.getConstructor().newInstance();
            Bundle bundle = new Bundle();
            bundle.putBundle(Constants.ARGUMENTS_DATA, arguments.getRealBundle());
            fragment.setArguments(bundle);
            return fragment;
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates Presenter instance
     * @return new Presenter instance
     */
    abstract protected P createPresenter();
}
