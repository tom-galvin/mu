package pw.usn.mu.analyser.module;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;

import pw.usn.mu.analyser.AnalysisErrorException;
import pw.usn.mu.analyser.Expression;
import pw.usn.mu.analyser.ResolutionContext;
import pw.usn.mu.analyser.closure.ClosureContext;
import pw.usn.mu.parser.ModuleNode;
import pw.usn.mu.tokenizer.Location;

/**
 * Represents a module in mu source code.
 */
public class Module extends Expression {
	private Map<String, Module> submodules;
	private Map<String, ModuleValue> declaredValues;
	private Map<ModuleValue, Expression> definedValues;
	
	/**
	 * Initializes a new Module.
	 * @param location The original location, in a source, of the code that represents
	 * this module.
	 */
	public Module(Location location) {
		super(location);
		submodules = new HashMap<String, Module>();
		declaredValues = new HashMap<String, ModuleValue>();
		definedValues = new HashMap<ModuleValue, Expression>();
	}
	
	/**
	 * Absorbs the content of {@code module} into this module. This discards any info
	 * that is not relevant after the identifier resolution stage. {@code module} should
	 * not be used after calling this method with it.
	 * @param module The module to merge with this module.
	 */
	public void absorbModule(Module module) {
		module.submodules.forEach((name, submodule) -> {
			if(!submodules.containsKey(name)) {
				submodules.put(name, submodule);
			} else {
				Location alreadyDefinedAt = submodules.get(name).getLocation();
				throw new AnalysisErrorException(
						String.format(
								"Submodule %s already exists at %s.",
								name,
								alreadyDefinedAt),
						submodule.getLocation());
			}
		});
		module.declaredValues.forEach((name, submodule) -> {
			if(!declaredValues.containsKey(name)) {
				declaredValues.put(name, submodule);
			} else {
				throw new AnalysisErrorException(
						String.format(
								"Module cannot reclare identifier %s",
								name),
						module.getLocation());
			}
		});
		module.definedValues.forEach((value, expression) -> {
			if(!definedValues.containsKey(value)) {
				addDefinition(value, expression);
			} else {
				Location alreadyDefinedAt = getExpression(value).getLocation();
				throw new AnalysisErrorException(
						String.format(
								"Definition name %s is already defined at %s.",
								value,
								alreadyDefinedAt),
						expression.getLocation());
			}
		});
	}
	
	/**
	 * Determines whether a submodule is contained within this {@link Module}
	 * with the given {@code name}.
	 * @param name The name of the submodule to search for.
	 * @return {@code true} if this module contains a submodule with the name
	 * {@code name}; {@code false} otherwise.
	 */
	public boolean containsSubmodule(String name) {
		return submodules.containsKey(name);
	}
	
	/**
	 * Determines whether the given identifier name is declared in this module.
	 * @param name The name of the identifier to check for.
	 * @return {@code true} if a value is declared with the name {@code name} in
	 * this module; {@code false} otherwise.
	 */
	public boolean containsDeclaration(String name) {
		return declaredValues.containsKey(name);
	}
	
	/**
	 * Determines whether the given value is defined within this module.
	 * @param value The value to check for.
	 * @return {@code true} if {@code value} is defined within this module; {@code
	 * false} otherwise.
	 */
	public boolean containsDefinition(ModuleValue value) {
		return declaredValues.containsKey(value);
	}
	
	/**
	 * Gets the submodule with the given name within this {@link Module}.
	 * @param name The name of the submodule to get.
	 * @return Returns the submodule within this module that is given the
	 * name {@code name}.
	 */
	public Module getSubmodule(String name) {
		return submodules.get(name);
	}
	
	/**
	 * Gets the {@link ModuleValue} corresponding to the given identifier.
	 * This method is only used during the initial analysis stage of
	 * resolution of identifiers.
	 * @param name The name of the identifier for which to get the value.
	 * @return The value corresponding to the identifier {@code name}.
	 * @throws IllegalArgumentException when the module doesn't declare a
	 * value with name {@code name}.
	 */
	public ModuleValue getValue(String name) {
		if(declaredValues.containsKey(name)) {
			return declaredValues.get(name);
		} else {
			throw new IllegalArgumentException(String.format(
					"Module does not declare a value with an identifier name %s.",
					name));
		}
	}
	
	/**
	 * Gets the {@link Expression} which {@code value} is defined as in this
	 * module.
	 * @param value The value for which to get the definition.
	 * @return The expression that {@code value} is defined as.
	 * @throws IllegalArgumentException when {@code value} is not defined in
	 * this module.
	 */
	public Expression getExpression(ModuleValue value) {
		if(definedValues.containsKey(value)) {
			return definedValues.get(value);
		} else {
			throw new IllegalArgumentException("Module does not define the given value.");
		}
	}
	
	/**
	 * Adds a submodule to this module.
	 * @param name The name of the submodule.
	 * @param submodule The module to add as a submodule.
	 */
	public void addSubmodule(String name, Module submodule) {
		submodules.put(name, submodule);
	}
	
	/**
	 * Forward-declares a value in this module. This method is only used
	 * during the initial analysis stage of resolution of identifiers.
	 * @param name The name of the value to forward-declare.
	 * @returns The declared value assigned to the given identifier name.
	 */
	public ModuleValue addDeclaration(String name) {
		ModuleValue value = new ModuleValue(name, this);
		declaredValues.put(name, value);
		return value;
	}
	
	/**
	 * Adds the definition to an already-defined value in this modile. This
	 * method is only used during the initial analysis stage, during the
	 * resolution of identifiers.
	 * @param value The forward-declared value to define.
	 * @param expression The definition which will be assigned to {@code value}.
	 * @throws IllegalArgumentException when attempting to define a value which
	 * has not yet been declared. If you want to declare <i>and</i> define in one
	 * statement, consider using the {@link Module#addDefinition(String, Expression)
	 * addDefinition(String, Expression)}
	 * overload instead.
	 */
	public void addDefinition(ModuleValue value, Expression expression) {
		if(declaredValues.containsValue(value)) {
			definedValues.put(value, expression);
		} else {
			throw new IllegalArgumentException(
					"This value is not declared in this module. Consider using the "
					+ "Module#addDefinition(String, Expression) overload instead.");
		}
	}
	
	/**
	 * Declares a value with the given name, and defines it to have the value
	 * of the given expression.
	 * @param name The name of the value to declare.
	 * @param expression The expression containing the value to define to {@code name}.
	 * @return Returns the {@link ModuleValue} representing this declared value in
	 * this module.
	 */
	public ModuleValue addDefinition(String name, Expression expression) {
		ModuleValue value = addDeclaration(name);
		addDefinition(value, expression);
		return value;
	}
	
	@Override
	public void liftClosures(ClosureContext context) {
		throw new IllegalStateException("Cannot lift closures at the Module level.");
	}
	
	/**
	 * Perform the initial {@link Module} analysis step. We declare the definitions in
	 * modules, and then analyse and add their definitions, in two separate passes.
	 * This is so circular references between methods can exist, as there are no
	 * forward declarations possible in mu syntax.
	 * @param handlers A queue of handlers which must be executed in order to resolve
	 * the definitions in this module and add them to the returned {@link Module} object
	 * after initial analysis.
	 * @param analyser The consumer which performs analyser-specific transformations on
	 * each definition in a module.
	 * @param node The node of the module to analyse.
	 * @return A {@link Module} containing the structure of this module and submodules,
	 * but lacking definitions of declared values.
	 */
	public static Module analyseInitial(Queue<Consumer<ResolutionContext>> handlers, Consumer<Expression> analyser, ModuleNode node) {
		Module module = new Module(node.getLocation());
		
		String[] submodules = node.getSubmodules();
		for(String submodule : submodules) {
			module.addSubmodule(
					submodule,
					analyseInitial(
							handlers, analyser,
							node.getSubmodule(submodule)));
		}
		
		String[] declarations = node.getDefinitions();
		for(String declarationName : declarations) {
			ModuleValue value = module.addDeclaration(declarationName);
			handlers.add(context -> {
				// Analyse the expression...
				Expression expression = Expression.analyse(
						context,
						node.getDefinition(declarationName));
				
				// Do whatever the analyser wants us to do to each expression...
				analyser.accept(expression);
				
				// Now add the transformed expression to the module.
				module.addDefinition(value, expression);
			});
		}
		return module;
	}
}
