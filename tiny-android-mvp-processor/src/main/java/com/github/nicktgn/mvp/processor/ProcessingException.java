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

package com.github.nicktgn.mvp.processor;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * Created by nick on 22/03/2018.
 */

public class ProcessingException extends Exception {
    private final Element element;
    private final String msg;
    private final Object[] params;

    public ProcessingException(Element element, String msg, Object... params) {
        this.element = element;
        this.msg = msg;
        this.params = params;
    }

    void print(Messager messager) {
        String message = msg;
        if (params.length > 0) {
            message = String.format(msg, params);
        }
        messager.printMessage(Diagnostic.Kind.ERROR, message, element);
    }
}