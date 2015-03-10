package pw.usn.mu.analyser;

import pw.usn.mu.analyser.module.Module;
import pw.usn.mu.parser.ModuleNode;

/**
 * Represents an object which accepts one or more top-level {@link 
 * ModuleNode}s from source files, performs semantic analysis, and
 * returns a {@link Module} containing the root module of all the
 * defined submodules in a mu program.
 * TODO Add support for precompiled references
 */
public interface Analyser {
	/**
	 * Analyses the content of {@code nodes} and produces a {@link Module}
	 * containing the top-level module for the program represented in the
	 * given module nodes.
	 * @param nodes The modules to analyse.
	 * @return A top-level module containing the entire program in an
	 * analysed and transformed state.
	 */
	public Module analyse(ModuleNode... nodes);
}
