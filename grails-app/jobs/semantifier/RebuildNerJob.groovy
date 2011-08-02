package semantifier


class RebuildNerJob {

	def learningNerService

	static triggers = {
		simple name: 'periodicallyRebuildNer', startDelay: 10*1000, repeatInterval: 60*60*1000 // once every hour  
	}

	def sessionRequired = true
	def concurrent = false

	def execute() {
		learningNerService.rebuild()
	}
}
