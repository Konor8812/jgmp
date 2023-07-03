import java.util.regex.Pattern

class Main {
    static main(String[] args) {
        def expression = args?[0] ?: ''
        println "given ${expression}"
        expression = solve(expression)
        println "result ${expression}"
    }

    static solve(expression) {
        expression = solveBraces(expression)
        solveExpression(expression.substring(0, expression.size()))
    }

    static solveBraces(expression) {
        def begin
        def end

        if (!correctBracesAmount(expression)) {
            Closure closure = { x -> "${x} + first closure " } << { x -> "${x} + second closure" }
            throw new CalculationException('Error not valid braces', closure)
        }
        while (expression.contains('(')) {

            end = expression.indexOf(')')
            for (int i = end - 1; i >= 0; i--) {
                if (expression.charAt(i) == '(') {
                    begin = i + 1
                    break
                }
            }

            def expressionResult = solveExpression(expression.substring(begin, end))
            expression = replaceExpression(expression, begin - 1, end + 1, expressionResult)
        }
        expression
    }

    static solveExpression(expression) {
        println "expression to solve  ${expression}"
        def result = 0
        def operation, operationIndex
        def leftOperand = '', rightOperand = ''
        def from = 0, to = expression.size()

        while (hasMultiplicationOrDivisionOperation(expression)) {
            from = 0
            to = expression.size()

            leftOperand = ''
            rightOperand = ''
            operation = ''
            operationIndex = -1

            for (int i = 0; i < expression.size(); i++) {
                if (expression[i] == '*' || expression[i] == '/') {
                    operation = expression[i]
                    operationIndex = i
                    break
                }
            }

            operation = expression.charAt(operationIndex)

            for (int j = operationIndex - 1; leftOperand == ''; j--) {
                if (isAnOperation(expression, j)) {
                    if (expression[j] == '-') {
                        leftOperand = expression.substring(j, operationIndex)
                    } else {
                        leftOperand = expression.substring(j + 1, operationIndex)
                    }
                    from = j;

                } else if (j == 0 && leftOperand == '') {
                    leftOperand = expression.substring(0, operationIndex)
                }
            }
            println "left operand ${leftOperand}"
            for (int j = operationIndex + 1; rightOperand == ''; j++) {
                if (isAnOperation(expression, j)) {
                    rightOperand = expression.substring(operationIndex + 1, j)
                    to = j;
                } else if (j == expression.size() - 1 && rightOperand == '') {
                    rightOperand = expression.substring(operationIndex + 1, expression.size())
                }
            }
            println "right operand ${rightOperand}"

            result = operation == '*'
                    ? Integer.parseInt(leftOperand) * Integer.parseInt(rightOperand)
                    : (int) (Integer.parseInt(leftOperand) / Integer.parseInt(rightOperand))

            expression = result >= 0
                    ? replaceExpression(expression, from, to, '+' + result.toString())
                    : replaceExpression(expression, from, to, result.toString())
        }

        println "multiplications done, expression  ${expression}"

        while (hasAdditionOrSubtractionOperation(expression)) {
            from = 0
            to = expression.size()

            leftOperand = ''
            rightOperand = ''
            operation = ''
            operationIndex = -1

            for (int i = 1; i < expression.size(); i++) {
                if (expression[i] == '+' || expression[i] == '-') {
                    operation = expression[i]
                    operationIndex = i
                    break
                }
            }

            operation = expression.charAt(operationIndex)

            for (int j = operationIndex - 1; leftOperand == ''; j--) {
                if (isAnOperation(expression, j)) {
                    if (j == 0) {
                        leftOperand = expression.substring(0, operationIndex)
                    } else {
                        leftOperand = expression.substring(j + 1, operationIndex)
                    }
                    from = j;
                }
                if (j == 0 && leftOperand == '') {
                    leftOperand = expression.substring(0, operationIndex)
                }
            }
            println "left operand ${leftOperand}"
            for (int j = operationIndex + 1; rightOperand == ''; j++) {
                if (isAnOperation(expression, j)) {
                    rightOperand = expression.substring(operationIndex, j)
                    to = j;
                } else if (j == expression.size() - 1 && rightOperand == '') {
                    rightOperand = expression.substring(operationIndex + 1, expression.size())
                }
            }
            println "right operand ${rightOperand}"

            result = operation == '+'
                    ? Integer.parseInt(leftOperand) + Integer.parseInt(rightOperand)
                    : Integer.parseInt(leftOperand) - Integer.parseInt(rightOperand)

            if (result >= 0) {
                expression = replaceExpression(expression, from, to, '+' + result.toString())
            } else {
                expression = replaceExpression(expression, from, to, result.toString())
            }
        }
        if (leftOperand == '' || rightOperand == '') {
            return Integer.parseInt(expression)
        }
        expression
    }

    static hasAdditionOrSubtractionOperation(expression) {
        expression.find(Pattern.compile('\\d+[+-][+-]?\\d+'))
    }

    static hasMultiplicationOrDivisionOperation(expression) {
        expression.find(Pattern.compile('[*/]'))
    }

    static isAnOperation(str, index) {
        isAnOperationSymbol(str[index]) && !isAnOperationSymbol(str[index - 1])
    }

    static isAnOperationSymbol(c) {
        c == '+' || c == '-' || c == '*' || c == '/'
    }

    static replaceExpression(expression, beginIndex, endIndex, replacement) {
        println """Full expression $expression
from $beginIndex
to $endIndex
part to replace ${expression.substring(beginIndex, endIndex)}
replacement $replacement"""

        expression.substring(0, beginIndex) + replacement + expression.substring(endIndex, expression.size())
    }

    static boolean correctBracesAmount(String expression) {
        expression.count('(') == expression.count(')')
    }

    static class CalculationException extends Exception {

        CalculationException(msg, modification) {
            super(modification(msg))
        }

        CalculationException(msg) {
            super(msg)
        }

        CalculationException plus(CalculationException exception) {
            new CalculationException("Aggregated exception with causes: ${this.message}, ${exception?.message}")
        }
    }
}