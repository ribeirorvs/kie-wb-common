/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.stunner.project.profile;

import org.junit.Test;
import org.kie.workbench.common.profile.api.preferences.Profile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProjectFullProfileTest {

    @Test
    public void testProfile() {
        ProjectFullProfile profile = new ProjectFullProfile();
        assertEquals(Profile.FULL.getName(), profile.getProjectProfileName());
        assertTrue(profile.definitionAllowedFilter().test("anyBeanTypeAllowed1"));
        assertTrue(profile.definitionAllowedFilter().test("anyBeanTypeAllowed2"));
        assertTrue(profile.definitionAllowedFilter().test("anyBeanTypeAllowed3"));
        assertTrue(profile.definitionAllowedFilter().test("others"));
    }
}
