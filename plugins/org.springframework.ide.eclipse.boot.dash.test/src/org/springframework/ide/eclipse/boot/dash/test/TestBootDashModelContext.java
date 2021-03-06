/*******************************************************************************
 * Copyright (c) 2015, 2016 Pivotal Software, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Pivotal Software, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.eclipse.boot.dash.test;

import java.io.File;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchManager;
import org.springframework.ide.eclipse.boot.core.BootPreferences;
import org.springframework.ide.eclipse.boot.core.cli.BootInstallManager;
import org.springframework.ide.eclipse.boot.dash.cloudfoundry.debug.ssh.SshTunnelFactory;
import org.springframework.ide.eclipse.boot.dash.model.BootDashModelContext;
import org.springframework.ide.eclipse.boot.dash.model.SecuredCredentialsStore;
import org.springframework.ide.eclipse.boot.dash.model.UserInteractions;
import org.springframework.ide.eclipse.boot.dash.model.runtargettypes.RunTargetType;
import org.springframework.ide.eclipse.boot.dash.test.mocks.MockScopedPropertyStore;
import org.springframework.ide.eclipse.boot.dash.test.mocks.MockSecuredCredentialStore;
import org.springframework.ide.eclipse.boot.dash.test.mocks.MockSshTunnel;
import org.springframework.ide.eclipse.boot.pstore.IPropertyStore;
import org.springframework.ide.eclipse.boot.pstore.IScopedPropertyStore;
import org.springframework.ide.eclipse.boot.pstore.InMemoryPropertyStore;
import org.springsource.ide.eclipse.commons.livexp.core.LiveExpression;
import org.springsource.ide.eclipse.commons.livexp.core.LiveVariable;
import org.springsource.ide.eclipse.commons.livexp.util.ExceptionUtil;
import org.springsource.ide.eclipse.commons.tests.util.StsTestUtil;

public class TestBootDashModelContext implements BootDashModelContext {

	private File stateLoc;
	private File installLoc;
	private ILaunchManager launchManager;
	private IWorkspace workspace;
	SecuredCredentialsStore secureStore = new MockSecuredCredentialStore();
	private IScopedPropertyStore<IProject> projectProperties;
	private IScopedPropertyStore<RunTargetType> runTargetProperties;
	private LiveVariable<Pattern> bootProjectExclusion = new LiveVariable<>(BootPreferences.DEFAULT_BOOT_PROJECT_EXCLUDE);
	private IPropertyStore viewProperties = new InMemoryPropertyStore();
	private IPropertyStore privateProperties = new InMemoryPropertyStore();
	private IPropertyStore installProperties = new InMemoryPropertyStore();
	private BootInstallManager bootInstalls;
	private UserInteractions ui;
	private SshTunnelFactory sshTunnelFactory = MockSshTunnel::new;

	public TestBootDashModelContext(IWorkspace workspace, ILaunchManager launchMamager, UserInteractions ui) {
		Assert.isNotNull(ui);
		try {
			this.ui = ui;
			this.workspace = workspace;
			this.launchManager = launchMamager;
			stateLoc = StsTestUtil.createTempDirectory("plugin-state", null);
			installLoc = StsTestUtil.createTempDirectory("boot-installs", null);
			this.projectProperties = new MockScopedPropertyStore<>();
			this.runTargetProperties = new MockScopedPropertyStore<>();
			reload();
		} catch (Exception e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	public IPath getStateLocation() {
		return new Path(stateLoc.toString());
	}

	public IWorkspace getWorkspace() {
		return workspace;
	}

	public void teardownn() throws Exception {
		FileUtils.deleteQuietly(stateLoc);
	}

	public ILaunchManager getLaunchManager() {
		return launchManager;
	}

	@Override
	public void log(Exception e) {
		// No implementation we'll use Mockito to spy on the method instead.
	}

	@Override
	public IScopedPropertyStore<IProject> getProjectProperties() {
		return projectProperties;
	}

	@Override
	public IScopedPropertyStore<RunTargetType> getRunTargetProperties() {
		return runTargetProperties;
	}

	@Override
	public SecuredCredentialsStore getSecuredCredentialsStore() {
		return secureStore;
	}

	@Override
	public LiveExpression<Pattern> getBootProjectExclusion() {
		return bootProjectExclusion;
	}

	@Override
	public IPropertyStore getViewProperties() {
		return viewProperties;
	}

	@Override
	public IPropertyStore getPrivatePropertyStore() {
		return privateProperties;
	}

	@Override
	public BootInstallManager getBootInstallManager() {
		return bootInstalls;
	}

	/**
	 * Simulates reloading
	 * @return
	 */
	public TestBootDashModelContext reload() throws Exception {
		this.bootInstalls = new BootInstallManager(installLoc, installProperties);
		return this;
	}

	@Override
	public UserInteractions getUi() {
		return ui;
	}

	@Override
	public SshTunnelFactory getSshTunnelFactory() {
		return sshTunnelFactory;
	}

}
