import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import java.util.List;
import java.util.EmptyStackException;

public class ArithmeticExpressionEvaluation 
{
	public static final Character[] validOps = {
		'(',
		')',
		'+',
		'-',
		'×',
		'/',
		'%',
		'^',
		'&',
		'|'
	};
	
    public static void main(String[] args)
	{
		while(true)
		{
        	Scanner readExp = new Scanner(System.in);
		
      	 	System.out.print("Enter the expression: ");
        	String exp = readExp.next();
		
        	System.out.println("Result: " + calculateExp(exp));
			System.out.println();
		}
    }

    public static long calculateExp(String expression) 
	{
        Stack<Long> operandStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();

        if(!isValidExp(expression)) 
		{
            try {
                throw new InvalidExpression("");
            }
			catch(InvalidExpression invalidExp) {
                invalidExp.printStackTrace();
            }
        }

        int i = 0;
        String currentInteger = null;
        int isNegative =0;
		
        while(i < expression.length()) 
		{
            if(expression.charAt(i) >= '0' && expression.charAt(i) <= '9')
			{
                currentInteger = expression.charAt(i) + "";
                i++;
				
                while(i != expression.length() && (expression.charAt(i) >= '0' && expression.charAt(i) <= '9'))
				{
                    currentInteger = currentInteger + expression.charAt(i);
                    i++;
                }

                if(isNegative != 0)
                    currentInteger = "-" + currentInteger ;

                operandStack.push(Long.parseLong(currentInteger));
                isNegative=0;
            } 
			else 
			{
                if(expression.charAt(i) == ')') 
				{
                    while(operatorStack.peek() != '(')
                        performArithmeticOperation(operandStack, operatorStack);
					
                    operatorStack.pop();
                }
				else if(expression.charAt(i) == '-' && 
						(i==0 || !Character.isDigit(expression.charAt(i-1))) &&
						(i+1 < expression.length()) && 
						((expression.charAt(i+1) >= '0') &&
						(expression.charAt(i+1) <= '9')))
				{
                    isNegative = -1;
                }
                else 
				{
                    Character currentOperator = expression.charAt(i);
                    Character lastOperator = (operatorStack.isEmpty() ? null : operatorStack.peek());

                    if(lastOperator != null && checkPrecedence(currentOperator, lastOperator))
                        performArithmeticOperation(operandStack, operatorStack);
					
                    operatorStack.push(expression.charAt(i));
                }
                i++;
            }
        }

        while(!operatorStack.isEmpty())
            performArithmeticOperation(operandStack, operatorStack);
		
        return operandStack.pop();
    }

    public static void performArithmeticOperation(Stack<Long> operandStack, Stack<Character> operatorStack) 
	{
        try {
            long value1 = operandStack.pop();
            long value2 = operandStack.pop();
            char operator = operatorStack.pop();

            long intermediateResult = arithmeticOperation(value1, value2, operator);
            operandStack.push(intermediateResult);
        } catch(EmptyStackException e) {
            System.out.println("Not a valid expression to evaluate");
            throw e;
        }
    }

    public static boolean checkPrecedence(Character operator1, Character operator2)
	{
        List<Character> precedenceList = new ArrayList<>();
        for(int i = 0; i <= validOps.length; i++) 
		{
			if(i == validOps.length) break;
			precedenceList.add(validOps[i]);
		}

        if(operator2 == '(' )
            return false;

        if(precedenceList.indexOf(operator1) > precedenceList.indexOf(operator2))
            return true;
        else
            return false;
    }

    public static long arithmeticOperation(long value2, long value1, Character operator)
	{
        long result = 0;
        switch(operator) 
		{
            case '+':
                result = value1 + value2;
            break;
			
            case '-':
                result = value1 - value2;
            break;

            case '×':
                result = value1 * value2;
            break;

            case '/':
				try{
                	result = value1 / value2;
				}catch(ArithmeticException e){
					result = 0;
				}
            break;

            case '%':
                result = value1 % value2;
            break;
			
			case '^':
				result = value1 ^ value2;
			break;

            default:
                result = value1 + value2;
        }
        return result;
    }

    public static boolean isValidExp(String expression)
	{
        if((!Character.isDigit(expression.charAt(0)) && !(expression.charAt(0) == '(')  && !(expression.charAt(0) == '-')) || (!Character.isDigit(expression.charAt(expression.length() - 1)) && !(expression.charAt(expression.length() - 1) == ')')))
            return false;

        HashSet<Character> validCharactersSet = new HashSet<>();
        for(int i = 0; i <= validOps.length; i++)
		{
			if(i == validOps.length) break;
			validCharactersSet.add(validOps[i]);
		}

        Stack<Character> validParenthesisCheck = new Stack<>();

        for(int i = 0; i < expression.length(); i++) 
		{
            if(!Character.isDigit(expression.charAt(i)) && !validCharactersSet.contains(expression.charAt(i)))
                return false;

            if(expression.charAt(i) == '(')
                validParenthesisCheck.push(expression.charAt(i));

            if(expression.charAt(i) == ')') {
                if(validParenthesisCheck.isEmpty())
                    return false;
                validParenthesisCheck.pop();
            }
        }
		
        if(validParenthesisCheck.isEmpty())
            return true;
        else
            return false;
    }
}

class InvalidExpression extends Exception 
{
    public InvalidExpression(String s)
    {
        super(s);
	}
}
