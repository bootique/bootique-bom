/*
 * Licensed to ObjectStyle LLC under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ObjectStyle LLC licenses
 * this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.bootique.bom.cayenne;

import org.apache.cayenne.BaseDataObject;

/**
 * A minimal hand-written Cayenne 5.0 entity. Cayenne 5.0 has no generic "CayenneDataObject", so unlike the
 * "legacy" test stack, the T1 entity must be mapped to a real class.
 */
public class T1 extends BaseDataObject {

    public static final String NAME = "name";

    private String name;

    public String getName() {
        beforePropertyRead(NAME);
        return name;
    }

    public void setName(String name) {
        beforePropertyWrite(NAME, this.name, name);
        this.name = name;
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        return NAME.equals(propName) ? name : super.readPropertyDirectly(propName);
    }

    @Override
    public void writePropertyDirectly(String propName, Object val) {
        if (NAME.equals(propName)) {
            this.name = (String) val;
        } else {
            super.writePropertyDirectly(propName, val);
        }
    }
}
