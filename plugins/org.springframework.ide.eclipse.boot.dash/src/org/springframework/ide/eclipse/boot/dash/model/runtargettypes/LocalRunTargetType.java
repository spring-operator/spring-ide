/*******************************************************************************
 * Copyright (c) 2015, 2016 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.eclipse.boot.dash.model.runtargettypes;

import org.eclipse.jface.resource.ImageDescriptor;
import org.springframework.ide.eclipse.boot.dash.BootDashActivator;
import org.springframework.ide.eclipse.boot.dash.model.RunTarget;
import org.springsource.ide.eclipse.commons.livexp.core.LiveSetVariable;

/**
 * @author Kris De Volder
 */
public class LocalRunTargetType extends AbstractRunTargetType {
	LocalRunTargetType(String name) {
		super(null, name);
	}

	@Override
	public boolean canInstantiate() {
		return false;
	}

	public String toString() {
		return "RunTargetType(LOCAL)";
	}

	@Override
	public void openTargetCreationUi(LiveSetVariable<RunTarget> targets) {
		throw new UnsupportedOperationException(
				this + " is a Singleton, it is not possible to create additional targets of this type.");
	}

	@Override
	public RunTarget createRunTarget(TargetProperties properties) {
		return null;
	}

	@Override
	public ImageDescriptor getIcon() {
		return BootDashActivator.getDefault().getImageRegistry().getDescriptor(BootDashActivator.BOOT_ICON);
	}
}