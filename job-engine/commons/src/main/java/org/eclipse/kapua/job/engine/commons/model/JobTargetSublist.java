/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine.commons.model;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@XmlRootElement(name = "jobTargetSublist")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class JobTargetSublist implements Iterable<KapuaId> {

    @XmlElement(name = "targetId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    private List<KapuaId> targetIds = new ArrayList<>();

    public JobTargetSublist() {
    }

    public JobTargetSublist(List<KapuaId> targetIds) {
        this.targetIds.addAll(targetIds);
    }

    public List<KapuaId> getTargetIds() {
        if (targetIds == null) {
            targetIds = new ArrayList<>();
        }

        return targetIds;
    }

    public void setTargetIds(List<KapuaId> targetIds) {
        this.targetIds = targetIds;
    }

    @Override
    public Iterator<KapuaId> iterator() {
        return targetIds.iterator();
    }

    public boolean isEmpty() {
        return getTargetIds().isEmpty();
    }

    public KapuaId[] toArray() {
        return getTargetIds().toArray(new KapuaId[targetIds.size()]);
    }
}
