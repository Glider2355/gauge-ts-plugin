package gauge.execution.runner;

import com.intellij.execution.Executor;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.testframework.actions.AbstractRerunFailedTestsAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.ui.ComponentContainer;
import com.intellij.psi.search.GlobalSearchScope;
import gauge.GaugeConstants;
import gauge.execution.GaugeCommandLine;
import gauge.execution.GaugeCommandLineState;
import gauge.execution.GaugeRunConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GaugeRerunFailedAction extends AbstractRerunFailedTestsAction {
  public GaugeRerunFailedAction(@NotNull ComponentContainer componentContainer) {
    super(componentContainer);
  }

  @Nullable
  @Override
  protected MyRunProfile getRunProfile(@NotNull ExecutionEnvironment environment) {
    RunProfile config = myConsoleProperties.getConfiguration();
    return config instanceof GaugeRunConfiguration ? new RerunProfile((GaugeRunConfiguration)config) : null;
  }

  private static class RerunProfile extends MyRunProfile {

    private final GaugeRunConfiguration config;

    RerunProfile(GaugeRunConfiguration configuration) {
      super(configuration);
      this.config = configuration;
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment env) {
      GeneralCommandLine commandLine = GaugeCommandLine.getInstance(config.getModule(), getProject());
      commandLine.addParameters(GaugeConstants.RUN, GaugeConstants.FAILED);
      return new GaugeCommandLineState(commandLine, getProject(), env, config);
    }

    @Nullable
    @Override
    public GlobalSearchScope getSearchScope() {
      return null;
    }

    @Override
    public void checkConfiguration() {
    }

    @Override
    public Module @NotNull [] getModules() {
      return this.config.getModules();
    }
  }
}
