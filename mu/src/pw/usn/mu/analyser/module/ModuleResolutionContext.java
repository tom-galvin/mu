package pw.usn.mu.analyser.module;

import pw.usn.mu.analyser.Expression;
import pw.usn.mu.analyser.ResolutionContext;
import pw.usn.mu.parser.IdentifierNode;

/**
 * Represents the context in which identifiers declared in modules
 * can be resolved.
 */
public class ModuleResolutionContext extends ResolutionContext {
	private Module rootModule;
	
	/**
	 * Initializes a new ModuleResolutionContext.
	 * @param rootModule The top-level module to use for resolution.
	 */
	public ModuleResolutionContext(Module rootModule) {
		super(null, null);
		this.globalContext = this;
		this.rootModule = rootModule;
	}
	
	@Override
	public Expression resolve(IdentifierNode identifier) {
		Module module = rootModule;
		for(String moduleName : identifier.getModules()) {
			if(module.containsSubmodule(moduleName)) {
				module = module.getSubmodule(moduleName);
			} else {
				return super.resolve(identifier);
			}
		}
		if(module.containsDeclaration(identifier.getName())) {
			return module.getValue(identifier.getName()).newReference(identifier.getLocation());
		} else {
			return super.resolve(identifier);
		}
	}
}
