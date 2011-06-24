package semantifier

class TypeController {

    def typeCheckerService

    def isLiteral = {
    	render typeCheckerService.isUriALiteralType(params.path)
    }
}
