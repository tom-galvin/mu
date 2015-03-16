package pw.usn.mu.analyser;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.function.Function;

import pw.usn.mu.analyser.closure.BindingClosureContext;
import pw.usn.mu.analyser.closure.ClosureContext;
import pw.usn.mu.parser.BindingNode;
import pw.usn.mu.parser.IdentifierNode;
import pw.usn.mu.parser.Node;
import pw.usn.mu.parser.binding.BindConsNode;
import pw.usn.mu.parser.binding.BindValueNode;
import pw.usn.mu.tokenizer.Location;


/**
 * Represents the binding of a value in a mu program.
 */
public class Binding extends Expression {
	private Value value;
	private Expression expression, body;
	
	/**
	 * Initializes a new Binding.
	 * @param location The original location, in a source, of the code that represents
	 * this expression.
	 * @param value The bound-to value.
	 * @param expression The expression bound in the binding.
	 * @param body The body of the new binding.
	 */
	public Binding(Location location, Value value, Expression expression, Expression body) {
		super(location);
		this.value = value;
		this.expression = expression;
		this.body = body;
	}
	
	/**
	 * Gets the value to which an expression was bound.
	 * @return The bound-to value.
	 */
	public Value getValue() {
		return value;
	}
	
	/**
	 * Gets the expression bound in this binding.
	 * @return The expression bound.
	 */
	public Expression getExpression() {
		return expression;
	}
	
	/**
	 * Gets the body of this binding.
	 * @return The body of this binding.
	 */
	public Expression getBody() {
		return body;
	}
	
	@Override
	public void liftClosures(ClosureContext context) {
		ClosureContext bindingContext = new BindingClosureContext(this, context);
		expression.liftClosures(bindingContext);
		body.liftClosures(bindingContext);
	}
	
	private static Expression createApplication(ResolutionContext context, Location location, String builtinName, Expression... arguments) {
		Expression expr = context.resolve(new IdentifierNode(location, builtinName));
		for(int i = 0; i < arguments.length; i++) {
			expr = new Application(location, expr, arguments[i]);
		}
		return expr;
	}

	/**
	 * Analyses the given {@code node} in the given context and creates an equivalent
	 * {@link Binding}.
	 * @param context The context in which {@code node} resides.
	 * @param node The AST node to analyse.
	 * @return An {@link Binding} representing the same program structure as {@code
	 * node} but with all identifiers resolved into references to values.
	 */
	public static Binding analyse(ResolutionContext context, BindingNode node) {
		if(node.getBindingStructure() instanceof BindValueNode) {
			/* If the binding is just a single identifier we can just bind directly to it
			 */
			BindValueNode bindValueNode = (BindValueNode)node.getBindingStructure();
			
			// Create a value for this binding
			Value value = new Value(bindValueNode.getValueName());
			
			// Simple resolution context
			ResolutionContext bindingContext = new ResolutionContext(context) {
				@Override
				public Expression resolve(IdentifierNode identifier) {
					String name = identifier.getName();
					if(identifier.isUnqualified() &&
							name.equals(value.getName())) {
						return value.newReference(identifier.getLocation());
					} else {
						return super.resolve(identifier);
					}
				}
			};
			
			return new Binding(node.getLocation(),
					value,
					Expression.analyse(bindingContext, node.getValue()),
					Expression.analyse(bindingContext, node.getContent()));
		} else {
			// All identifiers bound
			Map<String, Value> boundValues = new HashMap<String, Value>();
			
			/* Build a tree of bindings from the bottom up; we don't have pointers in
			 * Java so we'll use a stack of builder functions instead.
			 */
			Stack<Function<Expression, Binding>> reverseTreeStack = new Stack<Function<Expression, Binding>>();
			
			/* This function recursively analyses the binding decomposition format and
			 * creates the appropriate bindings.
			 */
			BiConsumer<Expression, Node> decomposeNode = (lowerExpr, structureNode) -> {
				if(structureNode instanceof BindValueNode) {
					BindValueNode valueNode = (BindValueNode)structureNode;
					Value value = new Value(valueNode.getValueName());
					boundValues.put(valueNode.getValueName(), value);
					reverseTreeStack.push(expr ->
						new Binding(node.getLocation(), value, lowerExpr, expr));
				} else if(structureNode instanceof BindConsNode) {
					BindConsNode consNode = (BindConsNode)structureNode;
					Value valueHead = new Value(), valueTail = new Value();
					reverseTreeStack.push(expr ->
					new Binding(
							node.getLocation(),
							valueHead,
							createApplication(
									context,
									consNode.getLocation(),
									"__head",
									lowerExpr),
							new Binding(
									node.getLocation(),
									valueTail,
									createApplication(
											context,
											consNode.getLocation(),
											"__tail",
											lowerExpr),
									expr)));
				}
			};
			
			ResolutionContext bindingContext = new ResolutionContext(context) {
				@Override
				public Expression resolve(IdentifierNode identifier) {
					String name = identifier.getName();
					if(identifier.isUnqualified() &&
							boundValues.containsKey(name)) {
						return boundValues.get(name).newReference(identifier.getLocation());
					} else {
						return super.resolve(identifier);
					}
				}
			};
			
			if(node.getValue() instanceof IdentifierNode) {
				decomposeNode.accept(Expression.analyse(bindingContext, node), node.getBindingStructure());
			} else {
				Value initialValue = new Value();
				reverseTreeStack.push(expr ->
						new Binding(node.getLocation(),
							initialValue,
							Expression.analyse(bindingContext, node.getValue()),
							expr));
				decomposeNode.accept(initialValue.newReference(node.getLocation()), node.getBindingStructure());
			}
			
			Expression expression = Expression.analyse(bindingContext, node.getContent());
			Binding binding = reverseTreeStack.pop().apply(expression);
			while(!reverseTreeStack.isEmpty()) {
				binding = reverseTreeStack.pop().apply(binding);
			}
			return binding;
		}
	}
}
