/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.model.query;

import java.util.List;

/**
 * Query "and" aggregation definition
 *
 * @since 1.0
 */
public interface AndPredicate extends StorablePredicate {

    /**
     * Get the {@link StorablePredicate} list
     *
     * @return
     */
    List<StorablePredicate> getPredicates();
}
