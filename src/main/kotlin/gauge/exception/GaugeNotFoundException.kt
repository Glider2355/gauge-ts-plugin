package gauge.exception

import gauge.GaugeBundle

class GaugeNotFoundException : Exception(GaugeBundle.message("gauge.not.found"))