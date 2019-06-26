/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.dmn.showcase.client.editor;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.common.client.api.Caller;
import org.kie.workbench.common.stunner.core.client.annotation.DiagramEditor;
import org.kie.workbench.common.stunner.core.client.service.ServiceCallback;
import org.kie.workbench.common.submarine.client.editor.BaseSubmarineEditor;
import org.uberfire.backend.vfs.Path;
import org.uberfire.backend.vfs.VFSService;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.client.promise.Promises;

@ApplicationScoped
public class DMNDiagramSubmarineWrapper {

    private PlaceManager placeManager;
    private Caller<VFSService> vfsServiceCaller;
    private BaseSubmarineEditor baseSubmarineEditor;
    private Promises promises;

    public DMNDiagramSubmarineWrapper() {
        //CDI proxy
    }

    @Inject
    public DMNDiagramSubmarineWrapper(final PlaceManager placeManager,
                                      final Caller<VFSService> vfsServiceCaller,
                                      final @DiagramEditor BaseSubmarineEditor baseSubmarineEditor,
                                      final Promises promises) {
        this.placeManager = placeManager;
        this.vfsServiceCaller = vfsServiceCaller;
        this.baseSubmarineEditor = baseSubmarineEditor;
        this.promises = promises;
    }

    public void newFile() {
        placeManager.registerOnOpenCallback(DMNDiagramsNavigatorScreen.DIAGRAM_EDITOR,
                                            () -> {
                                                baseSubmarineEditor.setContent("");
                                                placeManager.unregisterOnOpenCallbacks(DMNDiagramsNavigatorScreen.DIAGRAM_EDITOR);
                                            });

        placeManager.goTo(DMNDiagramsNavigatorScreen.DIAGRAM_EDITOR);
    }

    public void openFile(final Path path) {
        placeManager.registerOnOpenCallback(DMNDiagramsNavigatorScreen.DIAGRAM_EDITOR,
                                            () -> {
                                                vfsServiceCaller.call((String xml) -> {
                                                    baseSubmarineEditor.setContent(xml);
                                                    placeManager.unregisterOnOpenCallbacks(DMNDiagramsNavigatorScreen.DIAGRAM_EDITOR);
                                                }, (m, t) -> {
                                                    placeManager.unregisterOnOpenCallbacks(DMNDiagramsNavigatorScreen.DIAGRAM_EDITOR);
                                                    return false;
                                                }).readAllString(path);
                                            });

        placeManager.goTo(DMNDiagramsNavigatorScreen.DIAGRAM_EDITOR);
    }

    @SuppressWarnings("unchecked")
    public void saveFile(final Path path,
                         final ServiceCallback<String> callback) {
        baseSubmarineEditor.getContent().then(xml -> {
            vfsServiceCaller.call((Path p) -> callback.onSuccess((String) xml)).write(path, (String) xml);
            return promises.resolve();
        });
    }
}
