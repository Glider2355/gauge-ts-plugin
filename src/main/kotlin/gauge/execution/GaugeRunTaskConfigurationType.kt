//package gauge.execution
//
//import com.intellij.execution.configurations.ConfigurationFactory
//import com.intellij.execution.configurations.ConfigurationType.CONFIGURATION_TYPE_EP
//import com.intellij.execution.configurations.ConfigurationTypeBase
//import com.intellij.execution.configurations.RunConfiguration
//import com.intellij.icons.AllIcons
//import com.intellij.openapi.project.Project
//import com.intellij.openapi.util.NotNullLazyValue
//import gauge.GaugeBundle
//
//class GaugeRunTaskConfigurationType : ConfigurationTypeBase(
//    "executeSpecsTS",
//    GaugeBundle.message("gauge.execution"),
//    GaugeBundle.message("execute.the.gauge.tests"),
//    NotNullLazyValue.createValue { AllIcons.Actions.Execute }
//) {
//    init {
//        val scenarioConfigFactory = object : ConfigurationFactory(this) {
//            override fun getId(): String = "GaugeConfigurationFactory"
//
//            override fun createTemplateConfiguration(project: Project): RunConfiguration {
//                return GaugeRunConfiguration( project, this, "Gauge Execution")
//            }
//        }
//        addFactory(scenarioConfigFactory)
//    }
//
//    fun getInstance(): GaugeRunTaskConfigurationType? {
//        return CONFIGURATION_TYPE_EP.extensions
//            .filterIsInstance<GaugeRunTaskConfigurationType>()
//            .firstOrNull()
//    }
//}
