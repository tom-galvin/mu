package pw.usn.mu.analyser;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Consumer;

import pw.usn.mu.analyser.builtin.BuiltinResolutionContext;
import pw.usn.mu.analyser.module.Module;
import pw.usn.mu.analyser.module.ModuleResolutionContext;
import pw.usn.mu.parser.ModuleNode;
import pw.usn.mu.tokenizer.Location;

/**
 * An object which accepts one or more top-level {@link ModuleNode}s 
 * from source files, performs semantic analysis, and returns a
 * {@link Module} containing the root module of all the defined
 * submodules in a mu program.
 * TODO Add support for precompiled references
 */
public class DefaultAnalyser implements Analyser {
	@Override
	public Module analyse(ModuleNode... nodes) {
		Module rootModule = new Module(new Location());
		Queue<Consumer<ResolutionContext>> handlers = new ArrayDeque<Consumer<ResolutionContext>>();
		Consumer<Expression> analyser = e -> e.liftClosures();
		
		for(ModuleNode node : nodes) {
			Module analysedModule = Module.analyseInitial(handlers, analyser, node);
			rootModule.absorbModule(analysedModule);
		}
		
		ModuleResolutionContext moduleResolutionContext = new ModuleResolutionContext(rootModule);
		BuiltinResolutionContext builtinResolutionContext = new BuiltinResolutionContext(moduleResolutionContext);
		
		for(Consumer<ResolutionContext> handler : handlers) {
			handler.accept(builtinResolutionContext);
		}
		
		return rootModule;
	}
}